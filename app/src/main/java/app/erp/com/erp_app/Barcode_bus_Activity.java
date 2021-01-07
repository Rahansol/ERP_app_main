package app.erp.com.erp_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.adapter.Bus_InfoAdapter;
import app.erp.com.erp_app.adapter.UnitListAdapter;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.UnitList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Barcode_bus_Activity extends AppCompatActivity {

    Button scan_btn , scan_unit, submit_barcode, view_refresh;
    Context context;

    private Retrofit retrofit;
    private Gson mgsom;

    String click_type ,bus_barcode, area_code;
    List<String> scan_unit_barcodes;
    List<UnitList> unit_list_all;
    TextView bus_num ;
    SharedPreferences pref, barcode_type_pref;
    SharedPreferences.Editor editor;
    int index_num ;

    List<String> eb_barcode_list;

    ListView listView;
    UnitListAdapter adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_barcode_bus);
        context = this;
        scan_unit_barcodes = new ArrayList<>();

        ArrayList<UnitList> itme = new ArrayList<UnitList>();

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        adapter = new UnitListAdapter();
        listView = (ListView)findViewById(R.id.add_unit_list);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        bus_num = (TextView)findViewById(R.id.scan_busnum);

        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        barcode_type_pref = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        editor = barcode_type_pref.edit();

        index_num = 1;
        new Unist_List().execute();

        eb_barcode_list = new ArrayList<>();

        scan_btn = (Button)findViewById(R.id.scan_barcode);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "scan";
                editor.putString("camera_type" , "bus");
                editor.commit();

                if(bus_num.length() > 1){
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                    alertdialog.setTitle("차량 바코드 스캔");

                    alertdialog
                            .setMessage("차량 바코드를 다시 스캔 하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                            .setNegativeButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        new IntentIntegrator(Barcode_bus_Activity.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
                                        }
                                    });
                    AlertDialog adialog = alertdialog.create();
                    adialog.show();
                }else{
                    new IntentIntegrator(Barcode_bus_Activity.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
                }
            }
        });

        scan_unit = (Button)findViewById(R.id.scan_unit);
        scan_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "unit";
                editor.putString("camera_type" , "unit");
                editor.commit();

                new IntentIntegrator(Barcode_bus_Activity.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        submit_barcode = (Button)findViewById(R.id.submit_barcode);
        submit_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UnitList> result_list = new ArrayList<>();
                result_list = adapter.resultItem();
                List<String> scan_unit_barcode_list = new ArrayList<>();
                for(int i = 0 ; i < result_list.size(); i++){
                    scan_unit_barcode_list.add(result_list.get(i).getArea_code());
                }

                if(bus_num.getText().toString().length() == 0){
                    Toast.makeText(context,"버스번호 바코드를 스캔해주세요.",Toast.LENGTH_SHORT).show();
                }else if(scan_unit_barcode_list.size() == 0){
                    Toast.makeText(context,"1개 이상의 바코드를 스캔해주세요.",Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.setMessage("등록중..");
                    progressDialog.show();
                    new app_barcode_install().execute();
                }
            }
        });

        view_refresh = (Button)findViewById(R.id.view_refresh);
        view_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                alertdialog.setTitle("단말기 바코드 삭제");

                alertdialog
                        .setMessage("마지막 단말기 바코드를 삭제 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeItem();
                                    }
                                });
                AlertDialog adialog = alertdialog.create();
                adialog.show();

            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void removeItem(){
        adapter.removeItemAdp();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("unit")){
            progressDialog.setMessage("검색중..");
            progressDialog.show();
            make_unit_info(barcode);
        }else if(click_type.equals("scan")){
            progressDialog.setMessage("검색중..");
            progressDialog.show();
            new Bus_num_info().execute(barcode);
        }
    }
    private class app_barcode_install extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            String emp_id = pref.getString("emp_id",null);

            List<UnitList> result_list = new ArrayList<>();
            result_list = adapter.resultItem();

            List<String> scan_unit_barcode_list = new ArrayList<>();
            List<String> scan_eb_barcode_list = new ArrayList<>();

            for(int i = 0 ; i < result_list.size(); i++){
                scan_unit_barcode_list.add(result_list.get(i).getArea_code());
                if(result_list.get(i).getEb_barcode() == null){
                    scan_eb_barcode_list.add("");
                }else{
                    scan_eb_barcode_list.add(result_list.get(i).getEb_barcode());
                }
            }
            Call<String> call = erp.app_barcode_install(scan_unit_barcode_list,bus_barcode,emp_id,scan_eb_barcode_list);
            final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();

                    a_builder.setTitle("처리 완료");
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 엑티비티 새로고침
                                }
                            });
                    if(result.equals("true")){
                        //Toast.makeText(context,"단말기 등록 완료",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        a_builder.setMessage("단말기 등록 완료");
                        a_builder.show();

                    }else{
                        //Toast.makeText(context,"단말기 등록 실패",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        a_builder.setMessage("단말기 등록 실패");
                        a_builder.show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                    a_builder.setMessage("단말기 등록 오류 발생");
                    a_builder.show();
                }
            });
            return null;
        }
    }

    private class Unist_List extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
//            final Call<List<UnitList>> call = erp.getUnisList();
            final Call<List<UnitList>> call = erp.getUnitListMore();
            call.enqueue(new Callback<List<UnitList>>() {
                @Override
                public void onResponse(Call<List<UnitList>> call, Response<List<UnitList>> response) {
                    unit_list_all = response.body();
                }
                @Override
                public void onFailure(Call<List<UnitList>> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
            return null;
        }
    }

    void make_unit_info(String unit_barcode) {
        progressDialog.dismiss();
        if(unit_barcode == null){
            Toast.makeText(context,"바코드를 다시 스캔해주세요 . ",Toast.LENGTH_SHORT).show();
            return;
        }
        if(area_code == null){
            Toast.makeText(context,"버스 바코드를 먼저 스캔 해주세요 . ",Toast.LENGTH_SHORT).show();
            return;
        }
        List<UnitList> result_list = new ArrayList<>();
        result_list = adapter.resultItem();
        List<String> scan_unit_barcode_list = new ArrayList<>();
        for(int i = 0 ; i < result_list.size(); i++){
            scan_unit_barcode_list.add(result_list.get(i).getArea_code());
        }

        if(scan_unit_barcodes.contains(unit_barcode)){
            Toast.makeText(context,"이미 입력한 바코드 입니다. 확인해주세요 .",Toast.LENGTH_SHORT).show();
            return;
        }
        String unit_area_code = unit_barcode.substring(0,3);
        if(!area_code.equals(unit_area_code)){
            Toast.makeText(context,"지역바코드가 틀립니다. 확인해주세요 .",Toast.LENGTH_SHORT).show();
            return;
        }
        String unit_barocde_sbt = unit_barcode.substring(0,8);
        String area_name = "";
        String unit_name = "";
        for(int i = 0 ; i < unit_list_all.size(); i++){
            if(unit_list_all.get(i).getArea_code().equals(unit_barocde_sbt)){
                area_name = unit_list_all.get(i).getArea_name();
                unit_name = unit_list_all.get(i).getUnit_name();
                break;
            }
        }
        if(area_name.equals("") && unit_name.equals("")){
            Toast.makeText(context,"잘못된 바코드 입니다.",Toast.LENGTH_SHORT).show();
            return ;
        }
        scan_unit_barcodes.add(unit_barcode);
        listView.setAdapter(adapter);
        adapter.addItem(area_name,unit_name,unit_barcode,""+index_num);
        index_num++;
    }

    private class Bus_num_info extends AsyncTask<String , Integer , Long>{

        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);

            bus_barcode = strings[0];

            final Call<List<Bus_infoVo>> call = erp.getBusList(bus_barcode);
            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    List<Bus_infoVo> list = response.body();
                    if(list.size() == 0 ){
                        progressDialog.dismiss();
                        bus_num.setText("");
                        Toast.makeText(context,"잘못된 바코드 입니다.",Toast.LENGTH_SHORT).show();
                    }else if(list.size() == 1 ){
                        progressDialog.dismiss();
                        area_code = bus_barcode.substring(0,3);
                        bus_num.setText(list.get(0).getBus_num());
                    }else{
                        progressDialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.alert_custom_bus_info_dialog, null);
                        builder.setView(view);
                        final ListView alert_listView = (ListView)view.findViewById(R.id.bus_info_listview);
                        final AlertDialog dialog = builder.create();

                        Bus_InfoAdapter bus_infoAdapter = new Bus_InfoAdapter();
                        alert_listView.setAdapter(bus_infoAdapter);

                        for(int i = 1 ; i < list.size(); i++){
                            bus_infoAdapter.addItem(list.get(i).getBusoff_bus() ,list.get(i).getBus_num() , list.get(i).getBus_id() , list.get(i).getVehicle_num() );
                        }
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        area_code = bus_barcode.substring(0,3);
                        bus_num.setText(list.get(0).getBus_num());

                        Button bus_info_dialog_cancle = (Button)view.findViewById(R.id.bus_info_dialog_cancle);
                        bus_info_dialog_cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                }
                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("test","throwable"+t);
                }
            });
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2 , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_btn :
                new AlertDialog.Builder(this)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(Barcode_bus_Activity.this , LoginActivity.class );
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
