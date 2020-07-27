package app.erp.com.erp_app.over_work;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Over_Work_List_Adapter;
import app.erp.com.erp_app.dialog.DialogOverWorkNotice;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import app.erp.com.erp_app.vo.Over_Work_VO;
import retrofit2.Call;
import retrofit2.Response;

public class Over_Work_Re_Insert_Activity extends AppCompatActivity {

    private Context mcontext;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SharedPreferences pref;
    private ERP_Spring_Controller erp;
    private Over_Work_List_Adapter adapter;
    private HashMap<String,Object> insert_request_map = new HashMap<>();
    private DialogOverWorkNotice mdialog;
    private TextView re_work_date_view , re_insert_work_notice;
    private String work_noice="";
    private Over_Work_List_VO intent_data;
    private HashMap<String , Object> request_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_over_work_insert);
        mcontext = this;
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        Intent intent = getIntent(); /*데이터 수신*/
        intent_data = (Over_Work_List_VO) intent.getExtras().get("data");

        // 받아온 정보 입력
        re_work_date_view = (TextView)findViewById(R.id.re_work_date_view);
        re_work_date_view.setText(intent_data.getWork_date());

        re_insert_work_notice = (TextView)findViewById(R.id.re_insert_work_notice);
        re_insert_work_notice.setText(intent_data.getNotice());
        work_noice=intent_data.getNotice();

        TextView insert_over_work_btn = (TextView)findViewById(R.id.insert_over_work_btn);
        insert_over_work_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_request_map = adapter.get_select_tiem();

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timesdf = new SimpleDateFormat("HHmmss");
                String get_today = sdf.format(date);
                String get_time = timesdf.format(date);

                Set<?> set = insert_request_map.keySet();

                if("".equals(work_noice)){
                    Toast.makeText(mcontext,"신청사유를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if(set.size() == 0){
                    Toast.makeText(mcontext,"연장근무 작업을 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> info_map = new HashMap<>();
                info_map.put("work_date",intent_data.getWork_date());
                info_map.put("insert_reg_date",intent_data.getReg_date());
                info_map.put("insert_reg_time",intent_data.getReg_time());
                info_map.put("emp_id",intent_data.getEmp_id());
                info_map.put("work_notice",work_noice);

                Iterator<?> it = set.iterator();
                while(it.hasNext()){
                    String key = (String)it.next();
                    if(key != null){
                        Over_Work_VO vo = new Over_Work_VO();
                        vo = (Over_Work_VO) insert_request_map.get(key);
                        vo.setInsert_reg_date(intent_data.getReg_date());
                        vo.setInsert_reg_time(intent_data.getReg_time());
                        vo.setWork_date(intent_data.getWork_date());
                        vo.setEmp_id(intent_data.getEmp_id());

                        insert_request_map.put(key,vo);
                    }
                }

                Call<Boolean> insert_call = erp.update_delete_re_over_work(request_map);
                new Over_Work_Re_Insert_Activity.insert_over_work().execute(insert_call);
            }
        });

        request_map.put("work_date",intent_data.getWork_date());
        request_map.put("reg_date",intent_data.getReg_date());
        request_map.put("reg_time",intent_data.getReg_time());
        request_map.put("emp_id",intent_data.getEmp_id());

        Call<List<Over_Work_VO>> call = erp.status2_over_work_insert(request_map);
        new Over_Work_Re_Insert_Activity.status2_over_work_insert().execute(call);

    }

    private class status2_over_work_insert extends AsyncTask<Call, Void , List<Over_Work_VO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("미처리 작업 불러오는중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        @Override
        protected List<Over_Work_VO> doInBackground(Call... calls) {

            try{
                Call<List<Over_Work_VO>> call = calls[0];
                Response<List<Over_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Over_Work_VO> over_work_vo) {
            super.onPostExecute(over_work_vo);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(over_work_vo != null){

                RecyclerView recyclerView = findViewById(R.id.over_work_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

                adapter = new Over_Work_List_Adapter();
                recyclerView.setAdapter(adapter);

                for(int i=0; i<over_work_vo.size(); i++){
                    adapter.addItem(over_work_vo.get(i));
                }
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(over_work_vo.size()-1);
                recyclerView.smoothScrollToPosition(0);

            }
        }
    }

    private class insert_over_work extends AsyncTask<Call , Void , Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("연장 근무 신청 입력중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(aBoolean != null){
                if(aBoolean) {
                    Call<Boolean> insert_call = erp.insert_over_work_detail(insert_request_map);
                    new Over_Work_Re_Insert_Activity.insert_over_work_detail().execute(insert_call);
                }else{
                    Toast.makeText(mcontext,"이미 연장근무가 등록되어있습니다. \n 신청 현황을 확인해 주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class insert_over_work_detail extends AsyncTask<Call , Void , Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("연장 근무 신청 입력중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(aBoolean){
                Toast.makeText(mcontext, "연장 근무 신청 완료",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(mcontext, "오류 발생, 신청 현황을 확인해 주세요.",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
