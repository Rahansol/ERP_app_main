package app.erp.com.erp_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.adapter.ReserveScanItemAdapter;
import app.erp.com.erp_app.dialog.Dialog_Reserve_Item;
import app.erp.com.erp_app.vo.Reserve_ItemVO;
import app.erp.com.erp_app.vo.UnitList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReserveItemRepairActivity extends AppCompatActivity {

    SharedPreferences pref , user_info_pref;
    SharedPreferences.Editor editor;

    ListView scan_barcode_list;
    ReserveScanItemAdapter scanItemAdapter;

    List<String> scan_list;
    List<UnitList> unit_list_all;

    Context mContext;

    ProgressDialog progressDialog;
    Dialog_Reserve_Item dialogReserveItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_item_repair);

        mContext = this;


        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        scanItemAdapter = new ReserveScanItemAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] tag_split = view.getTag().toString().split(",");
                int pos = Integer.parseInt(tag_split[0]);
                int scan_list_post = scan_list.indexOf(tag_split[1]);
                scan_list.remove(scan_list_post);
                scanItemAdapter.removeItem(pos);
                scanItemAdapter.notifyDataSetChanged();
            }
        });
        scan_barcode_list = (ListView)findViewById(R.id.scan_barcode_list);

        pref = getSharedPreferences("scan_status" , MODE_PRIVATE);

        editor = pref.edit();
        editor.clear();
        editor.commit();

        user_info_pref = getSharedPreferences("user_info",MODE_PRIVATE);
        scan_list = new ArrayList<>();

        new Unist_List().execute();

        Button barcode_scan_btn = (Button)findViewById(R.id.barcode_scan_btn);
        barcode_scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("scan_type","start");
                editor.commit();

//                Reserve_ItemVO reserveItemVO = new Reserve_ItemVO();
//                reserveItemVO.setUnit_barcde("011024011145");
//                reserveItemVO.setArea_name("경기시내");
//                reserveItemVO.setUnit_name("2.0 복수하차");
//
//                if(scan_list.contains("012022022494")){
//                    reserveItemVO.setIndex_num("Y");
//                }else{
//                    reserveItemVO.setIndex_num("N");
//                    scan_list.add("011024011145");
//                }
//                scan_barcode_list.setAdapter(scanItemAdapter);
//                scanItemAdapter.addItem(reserveItemVO);

                new IntentIntegrator(ReserveItemRepairActivity.this).setCaptureActivity(CustomScannerReserveItemActivity.class).initiateScan();
            }
        });

        Button barcode_insert_btn = (Button)findViewById(R.id.barcode_insert);
        barcode_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               List<String> insert_scan = new ArrayList<>();
               Log.d("size : " ,""+scan_list.size());
               if(scan_list.size() == 0){
                   Toast.makeText(mContext,"바코드를 스캔해주세요",Toast.LENGTH_SHORT).show();
                   return ;
               }

               for(int i=0;i<scan_list.size();i++){{
                   for(int j=0;j<unit_list_all.size();j++){
                       if(unit_list_all.get(j).getArea_code().equals(scan_list.get(i).substring(0,6))){
                           insert_scan.add(unit_list_all.get(j).getUnit_name());
                       }
                   }
               }}

               Map<String, Integer> map = new HashMap<>();
               for(String temp : insert_scan){
                   Integer count = map.get(temp);
                   map.put(temp, (count == null) ? 1 : count + 1);
               }
               String dialog_msg = "";
               for( Map.Entry<String, Integer> entry : map.entrySet()){
                   Log.d("unit_name :" , entry.getKey() + "count :" + entry.getValue());
                   dialog_msg += entry.getKey() + " : " + entry.getValue() + " 대" + "\n";
               }

                dialogReserveItem = new Dialog_Reserve_Item(mContext,dialog_msg, cancle_btn_listener , insert_btn_listener);
                dialogReserveItem.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barcode = result.getContents();

        editor.putString("scan_barcode",barcode);
        editor.commit();

        if(barcode != null){
            Reserve_ItemVO reserveItemVO = new Reserve_ItemVO();
            if(barcode.length() == 12){
                for(int i=0;i<unit_list_all.size();i++){
                    if(unit_list_all.get(i).getArea_code().equals(barcode.substring(0,6))){
                        reserveItemVO.setArea_name(unit_list_all.get(i).getArea_name());
                        reserveItemVO.setUnit_name(unit_list_all.get(i).getUnit_name());

                        editor.putString("scan_area",unit_list_all.get(i).getArea_name());
                        editor.putString("scan_unit",unit_list_all.get(i).getUnit_name());
                        editor.commit();
                    }
                }
                reserveItemVO.setUnit_barcde(barcode);

                if(scan_list.contains(barcode)){
                    reserveItemVO.setIndex_num("Y");
                }else{
                    reserveItemVO.setIndex_num("N");
                    scan_list.add(barcode);
                }
                scan_barcode_list.setAdapter(scanItemAdapter);
                scanItemAdapter.addItem(reserveItemVO);

            }else{
                // 바코드 길이 12자리 아님
            }
        }

        String scan_type = pref.getString("scan_type","stop");
        if("stop".equals(scan_type)){
            editor.clear();
            editor.commit();
        }else{
            new IntentIntegrator(ReserveItemRepairActivity.this).setCaptureActivity(CustomScannerReserveItemActivity.class).initiateScan();
        }
    }

    private class Unist_List extends AsyncTask<String , Integer, Long> {
        @Override
        protected Long doInBackground(String... strings) {
            Retrofit retrofit = new Retrofit.Builder()
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
                                Intent i = new Intent(ReserveItemRepairActivity.this , LoginActivity.class );
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

    private class Reserve_Item_Process extends AsyncTask<Call,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            scan_list.clear();
            scanItemAdapter.remove_all();
            scanItemAdapter.notifyDataSetChanged();
            dialogReserveItem.dismiss();
            if(aBoolean){
                Toast.makeText(mContext,"등록완료!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext,"등록실패!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void finish() {
        editor.clear();
        editor.commit();
        super.finish();
    }

    private View.OnClickListener cancle_btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogReserveItem.dismiss();
        }
    };

    private View.OnClickListener insert_btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String input_gubun = dialogReserveItem.result_input_gubun();
            if(input_gubun.equals("0")){
                Toast.makeText(mContext,"입구구분을 선택해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("등록중");
            progressDialog.show();

            ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            String emp_id = user_info_pref.getString("emp_id",null);
            Call<Boolean> call_emp = erp.reserve_item_process(scan_list, emp_id,input_gubun);
            new ReserveItemRepairActivity.Reserve_Item_Process().execute(call_emp);
        }
    };
}
