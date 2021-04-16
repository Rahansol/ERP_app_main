package app.erp.com.erp_app.over_work;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Over_Work_Process_List_Adapter;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import retrofit2.Call;
import retrofit2.Response;

//연장근무 승인화면
public class Over_Work_Approval_Activity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    FragmentManager fm;
    FragmentTransaction ft;
    private Context mcContext;
    private String status_type;
    private HashMap<String,Object> type_map= new HashMap<>();
    private TextView a_st_date_view, a_ed_date_view;
    private ERP_Spring_Controller erp;
    private Over_Work_Process_List_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_work_approval);
        mcContext = this;
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        a_st_date_view = (TextView)findViewById(R.id.a_st_date);
        a_ed_date_view = (TextView)findViewById(R.id.a_ed_date);

        //날짜 셋팅
        final Calendar first_cal = Calendar.getInstance();
        first_cal.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(first_cal.getTime());
        a_ed_date_view.setText(today);
        first_cal.add(Calendar.DATE,-7);
        today = sdf.format(first_cal.getTime());
        a_st_date_view.setText(today);

        final Calendar cal = Calendar.getInstance();
        findViewById(R.id.a_st_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mcContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        a_st_date_view.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        findViewById(R.id.a_ed_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mcContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        a_ed_date_view.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        RadioGroup work_status_group = (RadioGroup)findViewById(R.id.a_work_status_group);
        work_status_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                status_type  = "0";
                switch (i){
                    case R.id.a_type0 :
                        status_type = "0";
                        break;
                    case R.id.a_type2 :
                        status_type = "1";
                        break;
                    case R.id.a_type3 :
                        status_type = "4";
                        break;
                }
                type_map.put("emp_id", "");
                type_map.put("status",status_type);
                serch_over_work_list();
            }
        });

        Button type0 = (Button)findViewById(R.id.a_type0);
        type0.performClick();

        Button over_work_list_serch_btn = (Button)findViewById(R.id.a_over_work_list_serch_btn);
        over_work_list_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serch_over_work_list();
            }
        });
    }

    public void serch_over_work_list(){
        if("일자 선택".equals(a_st_date_view.getText().toString())){
            type_map.put("st_date","");
        }else{
            type_map.put("st_date",a_st_date_view.getText().toString());
        }

        if("일자 선택".equals(a_ed_date_view.getText().toString())){
            type_map.put("ed_date","");
        }else{
            type_map.put("ed_date",a_ed_date_view.getText().toString());
        }

        Call<List<Over_Work_List_VO>> call = erp.over_work_data_type(type_map);
        new Over_Work_Approval_Activity.over_work_data_type().execute(call);

    }

    private class over_work_data_type extends AsyncTask<Call, Void , List<Over_Work_List_VO>> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcContext);
            progressDialog.setMessage("작업 리스트 불러오는중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        @Override
        protected List<Over_Work_List_VO> doInBackground(Call... calls) {
            try{
                Call<List<Over_Work_List_VO>> call =calls[0];
                Response<List<Over_Work_List_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Over_Work_List_VO> over_work_list_vos) {
            super.onPostExecute(over_work_list_vos);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(over_work_list_vos != null){
                RecyclerView recyclerView = findViewById(R.id.a_over_work_process_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(mcContext));
                TextView data_zero_text = (TextView)findViewById(R.id.data_zero_text);

                adapter = new Over_Work_Process_List_Adapter();
                recyclerView.setAdapter(adapter);

                for(int i=0; i<over_work_list_vos.size(); i++){
                    over_work_list_vos.get(i).setDisplay_type("A");
                    adapter.addItem(over_work_list_vos.get(i));
                }
                adapter.notifyDataSetChanged();

                if(over_work_list_vos.size() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    data_zero_text.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    data_zero_text.setVisibility(View.VISIBLE);
                }

                try{
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            view.findViewById(R.id.work_notice).getParent().requestDisallowInterceptTouchEvent(false);
                            return false;
                        }
                    });
                }catch (Exception e){

                }
            }
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

                                Intent i = new Intent(Over_Work_Approval_Activity.this , LoginActivity.class );
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
