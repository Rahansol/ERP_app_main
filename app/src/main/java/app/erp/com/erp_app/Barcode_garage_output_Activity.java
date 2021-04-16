package app.erp.com.erp_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.UnitList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Barcode_garage_output_Activity extends AppCompatActivity {

    Button scan_btn , scan_unit, submit_barcode, view_refresh;
    Context context;
    private Retrofit retrofit;
    String click_type ,office_barcode,area_code,input_trouble_high_code , input_trouble_low_code, input_bus_id, input_unit_barcode;
    List<String> scan_unit_barcodes;

    TextView scan_office, local_office;
    SharedPreferences pref , pref2 , barcode_type_pref;
    SharedPreferences.Editor editor , barcode_editor;

    AutoCompleteTextView actv;

    ListView listView;

    List<UnitList> unit_list_all;
    List<Trouble_CodeVo> trouble_high_code , trouble_low_code;

    ProgressDialog progressDialog;

    public Barcode_garage_output_Activity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_barcode_garage_output);
        context = this;
        scan_unit_barcodes = new ArrayList<>();
        ArrayList<UnitList> itme = new ArrayList<UnitList>();

        listView = (ListView)findViewById(R.id.add_unit_list);

        barcode_type_pref = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        barcode_editor = barcode_type_pref.edit();

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        scan_office = (TextView)findViewById(R.id.scan_office);
        local_office = (TextView)findViewById(R.id.local_office);
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        pref2 = context.getSharedPreferences("process_status", Context.MODE_PRIVATE);
        editor = pref2.edit();

        boolean status_result = false;
        try {
            status_result = process_status(time_now() , pref2.getString("result_time",null)) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(status_result){
            new office_name_info().execute(pref2.getString("result_office", null));
        }

        new Unist_List().execute();

        scan_btn = (Button)findViewById(R.id.scan_barcode);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "scan";
                barcode_editor.putString("camera_type" , "office");
                barcode_editor.commit();
                new IntentIntegrator(Barcode_garage_output_Activity.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
//                IntentIntegrator.forFragment(Barcode_garage_output_Activity.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        scan_unit = (Button)findViewById(R.id.scan_unit);
        scan_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "unit";
                barcode_editor.putString("camera_type" , "unit");
                barcode_editor.commit();
                new IntentIntegrator(Barcode_garage_output_Activity.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
//                IntentIntegrator.forFragment(Barcode_garage_output_Activity.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        submit_barcode = (Button)findViewById(R.id.submit_barcode);
        submit_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(scan_office.getText().toString().length() == 0){
                    Toast.makeText(context,"영업소 바코드를 스캔해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    EditText reserve_notice = (EditText)findViewById(R.id.reserve_notice);
                    if(input_bus_id == null){
                        input_bus_id = "";
                    }
                    progressDialog.setMessage("등록중...");
                    progressDialog.show();
                    new app_reserve_return_process().execute(office_barcode , input_unit_barcode , input_trouble_high_code , input_trouble_low_code ,
                            reserve_notice.getText().toString() , pref.getString("emp_id",null) , input_bus_id);
                }
            }
        });

        view_refresh = (Button)findViewById(R.id.view_refresh);
        view_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private class app_reserve_return_process extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<String> call = erp.app_reserve_return_process(strings[0],strings[1],strings[2],strings[3],strings[4],strings[5],strings[6]);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body() ;
                    progressDialog.dismiss();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    a_builder.setTitle("처리 완료");
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onResume();
                                }
                            });
                    if(result.equals("true")){
                        editor.putString("result_time" , time_now());
                        editor.putString("result_office" , office_barcode);
                        editor.commit();
                        a_builder.setMessage("단말기 등록 완료.");
                        a_builder.show();
                    }else{
                        a_builder.setMessage("오류 발생 다시 시도 해주세요 .");
                        a_builder.show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                }

            });
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("unit")){
            progressDialog.setMessage("검색중...");
            progressDialog.show();
            make_unit_info(barcode);
        }else if(click_type.equals("scan")){
            progressDialog.setMessage("검색중...");
            progressDialog.show();
            new office_name_info().execute(barcode);
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
            final Call<List<UnitList>> call = erp.getUnisList();
            call.enqueue(new Callback<List<UnitList>>() {
                @Override
                public void onResponse(Call<List<UnitList>> call, Response<List<UnitList>> response) {
                    unit_list_all = response.body();
                }
                @Override
                public void onFailure(Call<List<UnitList>> call, Throwable t) {

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
            Toast.makeText(context,"영업소 바코드를 먼저 스캔 해주세요 . ",Toast.LENGTH_SHORT).show();
            return;
        }
        String unit_area_code = unit_barcode.substring(0,2);
        if(!area_code.equals(unit_area_code)){
            Toast.makeText(context,"지역바코드가 틀립니다. 확인해주세요 .",Toast.LENGTH_SHORT).show();
            return;
        }
        input_unit_barcode = unit_barcode ;
        String unit_barocde_sbt = unit_barcode.substring(0,6);
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
        new app_reserve_high_code().execute(unit_barcode);
        TextView reserve_area_name = (TextView)findViewById(R.id.reserve_area_name);
        TextView reserve_unit_barcode = (TextView)findViewById(R.id.reserve_unit_barcode);
        TextView reserve_unit_name = (TextView)findViewById(R.id.reserve_unit_name);

        reserve_area_name.setText(area_name);
        reserve_unit_barcode.setText(unit_barcode);
        reserve_unit_name.setText(unit_name);

    }

    private class office_name_info extends AsyncTask<String , Integer , Long>{

        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            office_barcode = strings[0];
            final Call<List<String>> call = erp.get_office_name(office_barcode);
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    List<String> result = response.body();
                    progressDialog.dismiss();
                    if(result.get(0) == null ){
                        scan_office.setText("");
                        local_office.setText("");
                        Toast.makeText(context,"잘못된 바코드 입니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        scan_office.setText(result.get(0).toString());
                        String full_area_code = office_barcode.substring(0,3);
                        switch (full_area_code){
                            case "011" : local_office.setText("경기시내");
                                break;
                            case "012" : local_office.setText("경기마을");
                                break;
                            case "013" : local_office.setText("경기시외");
                                break;
                            case "021" : local_office.setText("인천시내");
                                break;
                            default : local_office.setText("지역");
                                break;
                        }
                        area_code = office_barcode.substring(0,2);
                        new bus_name_list().execute(office_barcode);
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("error","error : " + t + "call :" + call);
                }
            });

            return null;
        }
    }

    private class bus_name_list extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            office_barcode = strings[0];
            final Call<List<Bus_infoVo>> call = erp.office_bus_list(office_barcode);
            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    final List<Bus_infoVo> list = response.body();
                    List<String> auto_list = new ArrayList<>();
                    if( list != null){
                        for(int i = 0 ; i < list.size(); i++){
                            auto_list.add(list.get(i).getBus_num());
                        }
//                        키보드 내리기
//                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        actv = (AutoCompleteTextView)findViewById(R.id.bus_num_serch);
                        actv.setAdapter(new ArrayAdapter<String>(context,
                                android.R.layout.simple_dropdown_item_1line,  auto_list ));
                        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                input_bus_id = list.get(position).getBus_id();
//                                imm.hideSoftInputFromWindow(.getWindowToken(),0);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private class app_reserve_high_code extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            final String unit_barcode = strings[0];
            final Call<List<Trouble_CodeVo>> call = erp.app_reserve_high_code(unit_barcode);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    trouble_high_code = response.body();
                    List<String> spiner_list = new ArrayList<>();
                    for(int i = 0 ; i < trouble_high_code.size(); i++){
                        spiner_list.add(trouble_high_code.get(i).getTrouble_high_name());
                    }
                    Spinner high_code = (Spinner)findViewById(R.id.trouble_high_spinner);
                    high_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spiner_list));
                    high_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            make_trouble_low_spinner(trouble_high_code.get(position).getTrouble_high_cd(),unit_barcode);
                            input_trouble_high_code = trouble_high_code.get(position).getTrouble_high_cd();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });
            return null;
        }
        void make_trouble_low_spinner(String high_code, String unit_barcode){
            new app_reserve_low_code().execute(unit_barcode, high_code);

        }
    }

    private class app_reserve_low_code extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            String unit_barcode = strings[0];
            String trouble_high_cd = strings[1];
            final Call<List<Trouble_CodeVo>> call = erp.app_reserve_low_code(unit_barcode,trouble_high_cd);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    trouble_low_code = response.body();
                    final List<String> apinner_low_list = new ArrayList<>();
                    for(int i = 0 ; i < trouble_low_code.size(); i++){
                        apinner_low_list.add(trouble_low_code.get(i).getTrouble_low_name());
                    }
                    Spinner low_code = (Spinner)findViewById(R.id.trouble_low_spinner);
                    low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,apinner_low_list));
                    low_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            input_trouble_low_code = trouble_low_code.get(position).getTrouble_low_cd();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });

            return null;
        }
    }

    public String time_now(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = sdfNow.format(date);
        return formatDate;
    }

    public boolean process_status(String new_itme , String result_itme) throws ParseException {
        if(result_itme == null){
            return false;
        }
        String new_date = new_itme.substring(0,10);
        String result_date = result_itme.substring(0,10);
        if(new_date.equals(result_date)){
            String new_time_sbt = new_itme.substring(11,new_itme.length());
            String result_time_sbt = result_itme.substring(11,result_itme.length());

            SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
            Date d1 = f.parse(new_time_sbt);
            Date d2 = f.parse(result_time_sbt);
            long diff = d1.getTime() - d2.getTime();
            long sec = diff / 1000;
            if(sec < 120){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
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
                                pref = getSharedPreferences("user_info" , MODE_PRIVATE);
                                editor = pref.edit();
                                editor.putString("auto_login" , "Nauto");
                                editor.commit();

                                Intent i = new Intent(Barcode_garage_output_Activity.this , LoginActivity.class );
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
