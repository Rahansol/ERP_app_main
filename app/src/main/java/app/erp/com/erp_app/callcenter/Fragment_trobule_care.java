package app.erp.com.erp_app.callcenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.CustomScannerActivity;
import app.erp.com.erp_app.dialog.DialogEduEmpList;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_trobule_care extends Fragment {

    Button bus_num_find , bus_num_barcode_find ,edit_care_emp_list;
    RelativeLayout update_history;
    Context context;

    private Retrofit retrofit;
    private DialogEduEmpList mdialog;

    String click_type ,bus_barcode, area_code, emp_id, page_info;
    TextView insert_start_day , insert_start_time, insert_reg_emp_id, insert_unit_code , insert_bus_num, insert_phone_num, insert_area_code,
            insert_office_code, insert_ars_unit_code, insert_ars_trouble_high_code,insert_ars_trouble_low_code
            ,trouble_care_list;


    EditText insert_garage, insert_route_code;


    SharedPreferences pref ,page_check_info;
    SharedPreferences.Editor editor;

    Spinner insert_process_unit_code , insert_process_trouble_high_code, insert_process_trouble_low_code, insert_process_trouble_care_code ;

    String service_id , infra_id , unit_id , trouble_high_id, trouble_low_id , trouble_care_id;

    EditText insert_care_unit_before , insert_care_unit_after , insert_care_notice;

    int high_intdex, low_index = 0 ;
    int check_count;
    HashMap<String, Object> update_trouble_history_map;

    ProgressDialog progressDialog;

    LinearLayout equel_layout;

    List<Edu_Emp_Vo> edu_list;
    List<Edu_Emp_Vo> gblist = new ArrayList<>();
    List<Edu_Emp_Vo> gnlist = new ArrayList<>();
    List<Edu_Emp_Vo> iclist = new ArrayList<>();
    List<Edu_Emp_Vo> pclist = new ArrayList<>();
    List<String> cooperate_list ;

    public Fragment_trobule_care(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_care, container ,false);
        context = getActivity();

        update_trouble_history_map = new HashMap<>();

        Bundle bundle = getArguments();
        Trouble_HistoryListVO thlvo = (Trouble_HistoryListVO) bundle.getSerializable("Obj");

        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        emp_id = pref.getString("emp_id",null);

        page_check_info = context.getSharedPreferences("page_check_info" ,  Context.MODE_PRIVATE);
        String page_info = page_check_info.getString("page_check","n");

        SharedPreferences barcode_pre = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        editor = barcode_pre.edit();
        // layout
        equel_layout = (LinearLayout)view.findViewById(R.id.equel_layout);
        if("care_insert".equals(page_info)){
            equel_layout.setVisibility(View.GONE);
        }

        RelativeLayout after_before_unit = (RelativeLayout)view.findViewById(R.id.after_before_unit);
        after_before_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_type = "before";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forSupportFragment(Fragment_trobule_care.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        RelativeLayout after_care_unit = (RelativeLayout)view.findViewById(R.id.after_care_unit);
        after_care_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_type = "after";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forSupportFragment(Fragment_trobule_care.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        //textView
        insert_start_day = (TextView)view.findViewById(R.id.insert_start_day);
        insert_start_time = (TextView)view.findViewById(R.id.insert_start_time);
        insert_reg_emp_id = (TextView)view.findViewById(R.id.insert_reg_emp_id);
        insert_unit_code = (TextView)view.findViewById(R.id.insert_unit_code);
        insert_bus_num = (TextView)view.findViewById(R.id.insert_bus_num);
        insert_phone_num = (TextView)view.findViewById(R.id.insert_phone_num);
        insert_area_code = (TextView)view.findViewById(R.id.insert_area_code);
        insert_office_code = (TextView)view.findViewById(R.id.insert_office_code);

        insert_garage = (EditText) view.findViewById(R.id.insert_garage);
        insert_route_code = (EditText) view.findViewById(R.id.insert_route_code);

        insert_ars_unit_code = (TextView)view.findViewById(R.id.insert_ars_unit_code);
        insert_ars_trouble_high_code = (TextView)view.findViewById(R.id.insert_ars_trouble_high_code);
        insert_ars_trouble_low_code = (TextView)view.findViewById(R.id.insert_ars_trouble_low_code);
        trouble_care_list = (TextView)view.findViewById(R.id.trouble_care_list);

        //spinner
        insert_process_unit_code = (Spinner)view.findViewById(R.id.insert_process_unit_code);
        insert_process_trouble_high_code = (Spinner)view.findViewById(R.id.insert_process_trouble_high_code);
        insert_process_trouble_low_code = (Spinner)view.findViewById(R.id.insert_process_trouble_low_code);
        insert_process_trouble_care_code = (Spinner)view.findViewById(R.id.insert_process_trouble_care_code);

        //editText
        insert_care_unit_before = (EditText)view.findViewById(R.id.insert_care_unit_before);
        insert_care_unit_after = (EditText)view.findViewById(R.id.insert_care_unit_after);
        insert_care_notice = (EditText)view.findViewById(R.id.insert_care_notice);

        //button
        edit_care_emp_list = (Button)view.findViewById(R.id.edit_care_emp_list);
        edit_care_emp_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog = new DialogEduEmpList(context, gblist, gnlist, iclist,pclist, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdialog.dismiss();
                        Map<String,Object> map = mdialog.return_list();
                        gblist = (List<Edu_Emp_Vo>) map.get("gb_list");
                        gnlist = (List<Edu_Emp_Vo>) map.get("gn_list");
                        iclist = (List<Edu_Emp_Vo>) map.get("ic_list");
                        pclist = (List<Edu_Emp_Vo>) map.get("pc_list");

                        check_count = (int)map.get("check_count");
                        trouble_care_list.setText("현재 대상자 " + check_count +"명");
                        cooperate_list = (List<String>) map.get("care_emp_id");
                    }
                });
                mdialog.setCancelable(false);
                mdialog.show();

                DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = dm.widthPixels; //디바이스 화면 너비
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(mdialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = mdialog.getWindow();
                window.setAttributes(lp);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        update_history = view.findViewById(R.id.update_history);
        update_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
                String getdate = sdf.format(date);
                String gettime = sdf2.format(date);

                String care_before = insert_care_unit_before.getText().toString();
                String care_after  = insert_care_unit_after.getText().toString();

                if(!care_before.equals("") || !care_after.equals("")){
                    update_trouble_history_map.put("unit_change_yn","N");
                    update_trouble_history_map.put("unit_before_id",care_before);
                    update_trouble_history_map.put("unit_after_id",care_after);
                }else{
                    update_trouble_history_map.put("unit_change_yn","Y");
                    update_trouble_history_map.put("unit_after_id",care_after);
                    update_trouble_history_map.put("unit_before_id",care_before);
                }

                update_trouble_history_map.put("care_date",getdate);
                update_trouble_history_map.put("care_time",gettime);
                update_trouble_history_map.put("care_emp_id",emp_id);
                update_trouble_history_map.put("notice",insert_care_notice.getText().toString());

                if(check_count > 0){
                    update_trouble_history_map.put("cooperate_key","Y");
                }else{
                    update_trouble_history_map.put("cooperate_key","N");
                }

                update_trouble_history_map.put("garage_id",insert_garage.getText().toString());
                update_trouble_history_map.put("route_id",insert_route_code.getText().toString());

//                StringBuilder sb = new StringBuilder();
//                Set<?> set = update_trouble_history_map.keySet();
//                Iterator<?> it = set.iterator();
//                while(it.hasNext()){
//                    String key = (String)it.next();
//                    if(key != null){
//                        sb.append("------------------------------------------------------------\n");
//                        sb.append("key = "+key+",\t\t\tvalue = "+update_trouble_history_map.get(key)+"\n");
//                    }
//                }
//                Log.i("values info : " , ""+sb);

                String care_cd = update_trouble_history_map.get("trouble_care_cd").toString();
                if("X001".equals(care_cd)){
                    Toast.makeText(context,"조치 항목을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    new trouble_history_care_update().execute();
                }
            }
        });
        // 사원리스트 가져옴
        String emp_id = pref.getString("emp_id",null);
        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Edu_Emp_Vo>> call_emp = erp.Edu_care_emp_list(emp_id);
        new Fragment_trobule_care.Edu_Care_Emp_List().execute(call_emp);

        new GetMyWork_Job().execute(thlvo.getReg_date(),thlvo.getJob_viewer(),thlvo.getReg_time());
        return view;
    }

    private class GetMyWork_Job extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_HistoryListVO>> call = erp.getMyWork_Job(strings[0],strings[1],strings[2]);
            call.enqueue(new Callback<List<Trouble_HistoryListVO>>() {
                @Override
                public void onResponse(Call<List<Trouble_HistoryListVO>> call, Response<List<Trouble_HistoryListVO>> response) {
                    try {
                        List<Trouble_HistoryListVO> list  = response.body();
                        MakeMyWork_Info(list);
                        service_id = list.get(0).getService_id();
                        infra_id = list.get(0).getInfra_code();
                        unit_id = list.get(0).getUnit_code();
                        trouble_high_id = list.get(0).getTrouble_high_cd();
                        trouble_low_id = list.get(0).getTrouble_low_cd();
                        new get_insert_unit_code().execute();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(context,"데이터를 가져오는데 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Trouble_HistoryListVO>> call, Throwable t) {
                }
            });
            return null;
        }
    }

    private void MakeMyWork_Info(List<Trouble_HistoryListVO> list) {
        insert_start_day.setText(list.get(0).getReg_date());
        insert_start_time.setText(list.get(0).getReg_time());
        insert_reg_emp_id.setText(list.get(0).getWork_reg_emp_name());
        insert_unit_code.setText(list.get(0).getUnit_name());
        insert_bus_num.setText(list.get(0).getBus_num());
        insert_phone_num.setText(list.get(0).getDriver_tel_num());
        insert_area_code.setText(list.get(0).getOffice_group());
        insert_office_code.setText(list.get(0).getBusoff_name());
        insert_garage.setText(list.get(0).getGarage_id());
        insert_route_code.setText(list.get(0).getRoute_id());
        insert_ars_unit_code.setText(list.get(0).getUnit_name());
        insert_ars_trouble_high_code.setText(list.get(0).getTrouble_name());
        insert_ars_trouble_low_code.setText(list.get(0).getTrouble_low_name());

        insert_care_unit_before.setText(list.get(0).getUnit_before_id());
        insert_care_unit_after.setText(list.get(0).getUnit_after_id());
        insert_care_notice.setText(list.get(0).getNotice());

        update_trouble_history_map.put("reg_emp_id", list.get(0).getReg_emp_id());
        update_trouble_history_map.put("reg_date", list.get(0).getReg_date());
        update_trouble_history_map.put("reg_time",list.get(0).getReg_time());
        update_trouble_history_map.put("unit_code_before",list.get(0).getUnit_code());
        update_trouble_history_map.put("trouble_high_cd_before",list.get(0).getTrouble_high_cd());
        update_trouble_history_map.put("trouble_low_cd_before",list.get(0).getTrouble_low_cd());
        update_trouble_history_map.put("transp_bizr_id",list.get(0).getTransp_bizr_id());

    }

    private class get_insert_unit_code extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type(service_id,infra_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    int pos = 0 ;
                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getUnit_name());
                        if(list.get(i).getUnit_code().equals(unit_id)){
                            pos = i;
                        }
                    }
                    insert_process_unit_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    insert_process_unit_code.setSelection(pos);
                    insert_process_unit_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_high_code = list.get(position).getUnit_code();
                            unit_id = select_high_code;
                            update_trouble_history_map.put("unit_code",unit_id);
                            new get_insert_trobule_high_code().execute();

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

    private class get_insert_trobule_high_code extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_high_code(service_id,infra_id,unit_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    int pos = 0;

                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getTrouble_high_name());
                        if(list.get(i).getTrouble_high_cd().equals(trouble_high_id)){
                            pos = i;
                        }
                    }
                    insert_process_trouble_high_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    if(high_intdex == 0 ){ insert_process_trouble_high_code.setSelection(pos); }
                    insert_process_trouble_high_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_high_code = list.get(position).getTrouble_high_cd();
                            trouble_high_id = select_high_code;
                            update_trouble_history_map.put("trouble_high_cd",trouble_high_id);
                            new getfield_trouble_low_code().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    high_intdex ++;
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });

            return null;
        }
    }

    private class getfield_trouble_low_code extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_low_code(service_id,infra_id,unit_id,trouble_high_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    int pos = 0;
                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getTrouble_low_name());
                        if(list.get(i).getTrouble_low_cd().equals(trouble_low_id)){
                            pos = i;
                        }
                    }
                    insert_process_trouble_low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    if(low_index == 0 ){insert_process_trouble_low_code.setSelection(pos);}
                    insert_process_trouble_low_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            trouble_low_id = list.get(position).getTrouble_low_cd();
                            update_trouble_history_map.put("trouble_low_cd",trouble_low_id);
                            new get_field_trouble_carecode().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    low_index++;
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });

            return null;
        }
    }

    private class get_field_trouble_carecode extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_carecode(service_id,infra_id,unit_id,trouble_high_id,trouble_low_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("조치 항목을 선택해주세요.");
                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getTrouble_care_name());
                    }
                    insert_process_trouble_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    insert_process_trouble_care_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position > 0){
                                update_trouble_history_map.put("trouble_care_cd",list.get(position-1).getTrouble_care_cd());
                            }else{
                                update_trouble_history_map.put("trouble_care_cd","X001");
                            }
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

    private class trouble_history_care_update extends AsyncTask<String, Integer, Long>{

        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.update_trouble_history(update_trouble_history_map,cooperate_list);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    boolean result = response.body();

                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    page_info = "list";
                    a_builder.setTitle("콜 처리");
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    String title = "";
                                    if(page_info.equals("repg")){
                                        fragment = new Fragment_trouble_list();
                                    }else{
                                        fragment = new Fragment_trouble_list();
                                    }
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.frage_change,fragment);
                                    ft.commit();
                                }
                            });
                    if(result){
                        page_info = "list";
                        a_builder.setMessage(" 등록 완료.");
                        a_builder.show();
                    }else{
                        page_info = "repg";
                        a_builder.setMessage("오류 발생 다시 시도 해주세요 .");
                        a_builder.show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
            return null;
        }
    }

    private class Edu_Care_Emp_List extends AsyncTask<Call,Void, List<Edu_Emp_Vo>> {
        @Override
        protected List<Edu_Emp_Vo> doInBackground(Call... calls) {
            try{
                Call<List<Edu_Emp_Vo>> call = calls[0];
                Response<List<Edu_Emp_Vo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Edu_Emp_Vo> user_infoVos) {
            edu_list = user_infoVos;
            if(!(gblist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "강북지부" :
                            gblist.add(i);
                            break;
                    }
                }
            }
            if(!(gnlist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "강남지부":
                            gnlist.add(i);
                            break;
                    }
                }
            }
            if(!(iclist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "인천지부":
                            iclist.add(i);
                            break;
                    }
                }
            }
            if(!(pclist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "집계/충전기":
                            pclist.add(i);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("before")){
            insert_care_unit_before.setText(barcode);
        }else if(click_type.equals("after")){
            insert_care_unit_after.setText(barcode);
        }
    }
}
