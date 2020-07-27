package app.erp.com.erp_app.document_care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Over_Work_Insert_List_Adapter;
import app.erp.com.erp_app.adapter.Prj_Doc_Work_List_RecyclerAdapter;
import app.erp.com.erp_app.adapter.Prj_Doc_Write_PagerAdapter;
import app.erp.com.erp_app.adapter.Prj_Work_Insert_PagerAdapter;
import app.erp.com.erp_app.dialog.Dialog_Year_Month;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.vo.Prj_Item_VO;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.vo.Unit_InstallVO;
import retrofit2.Call;
import retrofit2.Response;

public class ProJect_Doc_Insert_Activity extends AppCompatActivity {


    private SharedPreferences pref;
    private Context mcontext;
    private ProJectVO pvo;

    private  String prj_code, get_today ,prj_seq,area_code,sub_area_code,office_group;

    private ERP_Spring_Controller erp;
    private List<ProJectVO> list_size;

    private Spinner project_office_group, project_transp, project_bus_list ,project_garage_spinner,project_route_list,infra_job_spinner;
    private Map<String,Object> request_map = new HashMap<>();
    private Prj_Doc_Work_List_RecyclerAdapter prj_doc_work_list_recyclerAdapter;

    private Intent intent;
    private Button prj_doc_work_list_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj_doc_insert);
        mcontext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        pvo = (ProJectVO) intent.getExtras().get("pvo");
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        //액션 바 설정
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        pref = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String emp_id = pref.getString("emp_id",null);

        // 전역변수 설정
        prj_code = pvo.getBase_infra_code() + pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq();
        prj_seq = pvo.getPrj_seq();
        area_code = pvo.getArea_code();
        sub_area_code = pvo.getSub_area_code();
        office_group = pvo.getOffice_group();

        request_map.put("reg_emp_id",emp_id);
        request_map.put("office_group",office_group);
        request_map.put("base_infra_code",pvo.getBase_infra_code());
        request_map.put("area_code",area_code);
        request_map.put("sub_area_code",sub_area_code);
        request_map.put("prj_seq",prj_seq);
        request_map.put("prj_name","prj_"+pvo.getBase_infra_code()+"_"+pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq());
        request_map.put("prj_code",prj_code);
        request_map.put("pvo",pvo);

        //spinner
//        project_office_group = (Spinner)findViewById(R.id.project_office_group);
        project_transp = (Spinner)findViewById(R.id.project_transp);
        project_bus_list = (Spinner)findViewById(R.id.project_bus_list);
        project_garage_spinner = (Spinner)findViewById(R.id.project_garage_spinner);
        project_route_list = (Spinner)findViewById(R.id.project_route_list);
        infra_job_spinner = (Spinner)findViewById(R.id.infra_job_spinner);
        //spinner end

        //작업 리스트 조회
        prj_doc_work_list_btn = (Button)findViewById(R.id.prj_doc_work_list_btn);
        prj_doc_work_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<ProJectVO>> call = erp.prj_doc_check_work_list(request_map);
                new ProJect_Doc_Insert_Activity.prj_doc_check_work_list().execute(call);
            }
        });

        // 문서작성 버튼
        Button prj_doc_write_btn = (Button)findViewById(R.id.prj_doc_write_btn);
        prj_doc_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ProJectVO> chekc_list = prj_doc_work_list_recyclerAdapter.return_check_work();
                String sql_text = "";
                for(int i=0; i<chekc_list.size(); i++){
                    if(i==0){
                        sql_text +=  "(REG_DTTI=" + "re1" + chekc_list.get(i).getReg_dtti() + "re1"  + "AND REG_EMP_ID =" + "re1" + chekc_list.get(i).getReg_emp_id() + "re1" + "AND BUS_ID=" + "re1"+ chekc_list.get(i).getBus_id() + "re1" + ")";
                    }else{
                        sql_text += "OR" + "(REG_DTTI=" + "re1" + chekc_list.get(i).getReg_dtti() + "re1"  + "AND REG_EMP_ID =" + "re1" + chekc_list.get(i).getReg_emp_id() + "re1" + "AND BUS_ID=" + "re1"+ chekc_list.get(i).getBus_id() + "re1" + ")";
                    }
                }
                request_map.put("check_work_list_sql",sql_text.replaceAll("re1","'" + "'"));
                request_map.put("check_work_list_sql2",sql_text.replaceAll("re1","'"));
                Call<Object> call = erp.create_sql_check_work_list(request_map);
                new ProJect_Doc_Insert_Activity.create_sql_check_work_list().execute(call);
            }
        });


        Call<List<ProJectVO>> call = erp.prj_work_trans_list(request_map);
        new ProJect_Doc_Insert_Activity.get_transp_bizr_select().execute(call);
    }

    private class create_sql_check_work_list extends AsyncTask<Call , Void , Object>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("문서 양식 생성중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Object doInBackground(Call... calls) {
            try{
                Call<Object> call = calls[0];
                Response<Object> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object prj_item_vos) {
            super.onPostExecute(prj_item_vos);
            if (progressDialog != null)
                progressDialog.dismiss();

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

            String res = prj_item_vos.toString();
            JSONObject test = null;
            try {
                test = new JSONObject(res);
            } catch (JSONException e) {
                test = null;
                e.printStackTrace();
            }
            JSONArray jsonArray = null;
            JSONArray jsonArray2 = null;

            try {
                jsonArray =  test.getJSONArray("data1");
                jsonArray2 = test.getJSONArray("data2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if( prj_item_vos != null )
            {
                int jsonSize = jsonArray.length();
                for( int i = 0; i < jsonSize; i++ )
                {
                    Map<String, Object> map = null;
                    try {
                        map = getMapFromJsonObject( (JSONObject) jsonArray.get(i) );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.add( map );
                }
            }

            if( prj_item_vos != null )
            {
                int jsonSize = jsonArray2.length();
                for( int i = 0; i < jsonSize; i++ )
                {
                    Map<String, Object> map = null;
                    try {
                        map = getMapFromJsonObject( (JSONObject) jsonArray2.get(i) );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list2.add( map );
                }
            }

            request_map.put("view_type","i");
            intent = new Intent(mcontext , ProJect_Doc_Write_Activity.class);
            intent.putExtra("list1" , (Serializable) list);
            intent.putExtra("list2" , (Serializable) list2);
            intent.putExtra("request_map" , (Serializable)request_map);

            Call<List<Unit_InstallVO>> call = erp.prj_all_item_list(request_map);
            new ProJect_Doc_Insert_Activity.prj_all_item_list().execute(call);
        }
    }

    private class prj_doc_check_work_list extends AsyncTask<Call , Void , List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call =calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return  response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);

            RecyclerView recyclerView = findViewById(R.id.prj_doc_work_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

            prj_doc_work_list_recyclerAdapter = new Prj_Doc_Work_List_RecyclerAdapter();
            recyclerView.setAdapter(prj_doc_work_list_recyclerAdapter);

            for(int i=0; i<proJectVOS.size(); i++){
                prj_doc_work_list_recyclerAdapter.addItem(proJectVOS.get(i));
            }
            prj_doc_work_list_recyclerAdapter.notifyDataSetChanged();

        }
    }

    private class get_transp_bizr_select extends AsyncTask<Call, Void , List<ProJectVO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("작업내역 조회중...");
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
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(proJectVOS != null){
                List<String> transp_id = new ArrayList<>();
                transp_id.add("운수사 선택");
                for(int i=0; i<proJectVOS.size(); i++){
                    transp_id.add(proJectVOS.get(i).getBusoff_name());
                }
                project_transp.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,transp_id));
                project_transp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i>0){
                            request_map.put("trans_id",proJectVOS.get(i-1).getTransp_bizr_id());
                            request_map.put("busoff_name",proJectVOS.get(i-1).getBusoff_name());

                            Call<List<ProJectVO>> call2 = erp.prj_work_garage_list(request_map);
                            new ProJect_Doc_Insert_Activity.app_prj_garage_select().execute(call2);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }
    }

    private class app_prj_garage_select extends AsyncTask<Call,Void, List<ProJectVO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("작업내역 조회중...");
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
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(proJectVOS != null){
                List<String> garage_name = new ArrayList<>();
                garage_name.add("영업소 선택");
                for(int i=0; i<proJectVOS.size(); i++){
                    garage_name.add(proJectVOS.get(i).getGarage_name());
                }
                project_garage_spinner.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,garage_name));
                project_garage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i>0){

                            request_map.put("garage_id",proJectVOS.get(i-1).getGarage_id());
                            request_map.put("garage_name",proJectVOS.get(i-1).getGarage_name());
                        }else{
                            request_map.put("garage_id","");
                            request_map.put("garage_name","");
                        }

                        Call<List<ProJectVO>> call = erp.prj_work_job_list(request_map);
                        new ProJect_Doc_Insert_Activity.prj_work_job_list().execute(call);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    private class prj_all_item_list extends AsyncTask<Call , Void , List<Unit_InstallVO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("문서 양식 생성중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected List<Unit_InstallVO> doInBackground(Call... calls) {
            try{
                Call<List<Unit_InstallVO>> call =calls[0];
                Response<List<Unit_InstallVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Unit_InstallVO> unit_installVOS) {
            super.onPostExecute(unit_installVOS);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(unit_installVOS != null){
                intent.putExtra("item_vo", (Serializable) unit_installVOS);
                startActivity(intent);
            }
        }
    }

    private class prj_work_job_list extends AsyncTask<Call, Void, List<ProJectVO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("작업내역 조회중...");
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
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(proJectVOS != null){
                List<String> job_work = new ArrayList<>();
                job_work.add("작업 선택");
                for(int i=0; i<proJectVOS.size(); i++){
                    job_work.add(proJectVOS.get(i).getJob_name());
                }
                infra_job_spinner.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,job_work));
                infra_job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        if(pos>0){
                            request_map.put("job_type",proJectVOS.get(pos-1).getJob_type());
                            request_map.put("job_name",proJectVOS.get(pos-1).getJob_name());
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
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
                                Intent i = new Intent(ProJect_Doc_Insert_Activity.this , LoginActivity.class );
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

    public static Map<String, Object> getMapFromJsonObject( JSONObject jsonObj )
    {
        Map<String, Object> map = null;

        try {
            map = new ObjectMapper().readValue(jsonObj.toString(), Map.class) ;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prj_doc_work_list_btn.performClick();
    }
}
