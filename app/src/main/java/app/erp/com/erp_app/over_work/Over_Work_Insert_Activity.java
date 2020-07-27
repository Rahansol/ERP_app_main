package app.erp.com.erp_app.over_work;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import app.erp.com.erp_app.dialog.DialogOverLapWork;
import app.erp.com.erp_app.dialog.DialogOverWorkNotice;
import app.erp.com.erp_app.ic_check_menu.Fragment_cash_check_insert;
import app.erp.com.erp_app.vo.Over_Work_VO;
import retrofit2.Call;
import retrofit2.Response;

public class Over_Work_Insert_Activity extends AppCompatActivity {

    private Context mcontext;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SharedPreferences pref;
    private ERP_Spring_Controller erp;
    private Over_Work_List_Adapter adapter;
    private HashMap<String,Object> insert_request_map = new HashMap<>();
    private DialogOverWorkNotice mdialog;
    private TextView work_date_view;
    private String work_noice="";
    private CheckBox proejct_work_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_work_insert);
        proejct_work_check = (CheckBox)findViewById(R.id.proejct_work_check);

        mcontext = this;
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        pref = mcontext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final String emp_id = pref.getString("emp_id",null);

        work_date_view = (TextView)findViewById(R.id.work_date_view);

        final Calendar cal = Calendar.getInstance();
        findViewById(R.id.work_date_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mcontext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        work_date_view.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });


        HashMap<String, Object> request_map = new HashMap<>();
        request_map.put("emp_id",emp_id);

        RelativeLayout insert_work_notice = (RelativeLayout) findViewById(R.id.over_work_notice_textview_open_btn);
        insert_work_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView insert_work_notice = (TextView)findViewById(R.id.insert_work_notice);
                String notice_text = insert_work_notice.getText().toString();
                mdialog = new DialogOverWorkNotice(mcontext, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insert_work_notice.setText(mdialog.return_notice_Text());
                        mdialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdialog.dismiss();
                    }
                } , notice_text);
                mdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        work_noice = insert_work_notice.getText().toString();
                    }
                });
                mdialog.setCancelable(false);
                mdialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(mdialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = mdialog.getWindow();
                window.setAttributes(lp);

            }
        });

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

                if("날짜 선택".equals(work_date_view.getText().toString())){
                    Toast.makeText(mcontext,"날짜를 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if("".equals(work_noice)){
                    Toast.makeText(mcontext,"신청사유를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if(set.size() == 0 && !proejct_work_check.isChecked()){
                    Toast.makeText(mcontext,"연장근무 작업을 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> info_map = new HashMap<>();
                info_map.put("work_date",work_date_view.getText().toString());
                info_map.put("insert_reg_date",get_today);
                info_map.put("insert_reg_time",get_time);
                info_map.put("emp_id",emp_id);
                info_map.put("work_notice",work_noice);

                Iterator<?> it = set.iterator();
                while(it.hasNext()){
                    String key = (String)it.next();
                    if(key != null){
                        Over_Work_VO vo = new Over_Work_VO();
                        vo = (Over_Work_VO) insert_request_map.get(key);
                        vo.setInsert_reg_date(get_today);
                        vo.setInsert_reg_time(get_time);
                        vo.setWork_date(work_date_view.getText().toString());
                        vo.setEmp_id(emp_id);

                        insert_request_map.put(key,vo);
                    }
                }

                Call<Boolean> insert_call = erp.insert_over_work(info_map);
                new Over_Work_Insert_Activity.insert_over_work().execute(insert_call);
            }
        });


        Call<List<Over_Work_VO>> call = erp.app_serch_over_work_list(request_map);
        new Over_Work_Insert_Activity.app_serch_over_work_list().execute(call);

    }

    private class app_serch_over_work_list extends AsyncTask<Call, Void , List<Over_Work_VO>>{
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
                    new Over_Work_Insert_Activity.insert_over_work_detail().execute(insert_call);
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
