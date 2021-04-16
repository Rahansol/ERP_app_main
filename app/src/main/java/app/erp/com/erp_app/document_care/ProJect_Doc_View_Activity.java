package app.erp.com.erp_app.document_care;

import android.app.DatePickerDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Prj_Doc_Sign_List_RecyclerAdapter;
import app.erp.com.erp_app.dialog.Dialog_Year_Month;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

public class ProJect_Doc_View_Activity extends AppCompatActivity {


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mcontext;
    private ProJectVO pvo;

    private  String prj_code, get_today ,prj_seq,area_code,sub_area_code,office_group;

    private ERP_Spring_Controller erp;
    private List<ProJectVO> list_size;

    private Spinner prj_doc_office_spinner , prj_doc_garage_spinner;
    private Map<String,Object> request_map = new HashMap<>();
    private Prj_Doc_Sign_List_RecyclerAdapter prj_doc_sign_list_recyclerAdapter;

    private Intent intent;
    private Button prj_doc_work_list_btn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj_doc_view);
        mcontext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        pvo = (ProJectVO) intent.getExtras().get("pvo");
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        //액션 바 설정
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        pref = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String emp_id = pref.getString("emp_id",null);

        final TextView st_date_view = (TextView)findViewById(R.id.st_date);
//        final TextView ed_date_view = (TextView)findViewById(R.id.ed_date);

        final Calendar cal = Calendar.getInstance();

        findViewById(R.id.st_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        Log.d("YearMonthPickerTest", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
                        Log.d("YearMonthPickerTest", String.format("% d-%02d", year, monthOfYear));
                        st_date_view.setText(String.format("% d-%02d", year, monthOfYear));
                    }
                };

                Log.d("여기로 들어오나,","들어ㅏ오나ㅏㅏㅏㅏㅏ");

                final Dialog_Year_Month dym = new Dialog_Year_Month();
                dym.setListener(d);
                dym.show(getSupportFragmentManager(), "YearMonthPickerTest");
                getSupportFragmentManager().executePendingTransactions();
                dym.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dym.dismiss();
                        String st_date = st_date_view.getText().toString().replaceAll(" ","");
                        request_map.put("st_date",st_date.replaceAll("-",""));
                        Call<List<ProJectVO>> call = erp.prj_doc_office_list(request_map);
                        new ProJect_Doc_View_Activity.prj_doc_office_list().execute(call);
                    }
                });
            }
        });

        // 전역변수 설정
        prj_code = pvo.getBase_infra_code() + pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq();
        prj_seq = pvo.getPrj_seq();
        area_code = pvo.getArea_code();
        sub_area_code = pvo.getSub_area_code();
        office_group = pvo.getOffice_group();

        request_map.put("office_group",office_group);
        request_map.put("base_infra_code",pvo.getBase_infra_code());
        request_map.put("area_code",area_code);
        request_map.put("sub_area_code",sub_area_code);
        request_map.put("prj_seq",prj_seq);
        request_map.put("prj_name","prj_"+pvo.getBase_infra_code()+"_"+pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq());
        request_map.put("pvo",pvo);

        //spinner
        prj_doc_office_spinner = (Spinner)findViewById(R.id.prj_doc_office_spinner);
        prj_doc_garage_spinner = (Spinner)findViewById(R.id.prj_doc_garage_spinner);

        //검색 버튼
        Button history_serch_btn = (Button)findViewById(R.id.history_serch_btn);
        history_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<ProJectVO>> call = erp.serch_prj_doc_sign_list(request_map);
                new ProJect_Doc_View_Activity.serch_prj_doc_sign_list().execute(call);
            }
        });

    }

    //운수사 정보 가져옴
    private class prj_doc_office_list extends AsyncTask<Call , Void , List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(proJectVOS != null){
                List<String> spinner_list = new ArrayList<>();
                for(int i=0; i < proJectVOS.size(); i++){
                    spinner_list.add(proJectVOS.get(i).getBusoff_name());
                }
                prj_doc_office_spinner.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                prj_doc_office_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        String trans_id = proJectVOS.get(pos).getTransp_bizr_id();
                        request_map.put("transp_bizr_id",trans_id);
                        Call<List<ProJectVO>> call = erp.prj_doc_garage_list(request_map);
                        new ProJect_Doc_View_Activity.prj_doc_garage_list().execute(call);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    //영업소 정보 가져옴
    private class prj_doc_garage_list extends AsyncTask<Call,Void, List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(proJectVOS != null){
                List<String> spinner_list = new ArrayList<>();
                for(int i=0; i < proJectVOS.size(); i++){
                    spinner_list.add(proJectVOS.get(i).getGarage_name());
                }
                prj_doc_garage_spinner.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                prj_doc_garage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        String garage_id = proJectVOS.get(pos).getGarage_id();
                        request_map.put("garage_id",garage_id);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    //검색하여 검색결고 가져옴
    private class serch_prj_doc_sign_list extends AsyncTask<Call,Void,List<ProJectVO>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("문서 조회 중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(proJectVOS != null){
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.prj_doc_sign_list_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

                String prj_name = (String)request_map.get("prj_name");

                prj_doc_sign_list_recyclerAdapter = new Prj_Doc_Sign_List_RecyclerAdapter(prj_name,request_map);
                recyclerView.setAdapter(prj_doc_sign_list_recyclerAdapter);

                for(int i=0; i<proJectVOS.size(); i++){
                    prj_doc_sign_list_recyclerAdapter.addItem(proJectVOS.get(i));
                }
                prj_doc_sign_list_recyclerAdapter.notifyDataSetChanged();
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

                                Intent i = new Intent(ProJect_Doc_View_Activity.this , LoginActivity.class );
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