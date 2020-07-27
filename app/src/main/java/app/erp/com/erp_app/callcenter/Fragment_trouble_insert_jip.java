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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import app.erp.com.erp_app.dialog.DialogEduEmpList;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_trouble_insert_jip extends Fragment {


    Button submit_barcode , jip_edit_care_emp_btn;
    Context context;

    private Retrofit retrofit;
    private DialogEduEmpList mdialog;

    String click_type, page_info ,service_id, infra_code, unit_code, trouble_high_code, trouble_low_code, start_hour, start_min;
    List<String> scan_unit_barcodes;
    EditText find_bus_num, nms_garage_id, nms_notice;
    TextView reserve_area_name , reserve_unit_barcode, nms_dep_name, start_day, jip_trouble_care_text;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    CheckBox bs_yn;
    HashMap<String, Object> filed_error_map;

    Spinner nms_infra_type , nms_group , nms_office_group,nms_unit_code ,nms_trouble_high_code , nms_trouble_low_code, nms_care_code, nms_start_hour, nms_start_min;

    RadioGroup today_group;
    RadioButton today_y , today_n;
    ProgressDialog progressDialog;

    List<Edu_Emp_Vo> edu_list;
    List<Edu_Emp_Vo> gblist = new ArrayList<>();
    List<Edu_Emp_Vo> gnlist = new ArrayList<>();
    List<Edu_Emp_Vo> iclist = new ArrayList<>();
    List<Edu_Emp_Vo> pclist = new ArrayList<>();
    List<String> cooperate_list ;

    LinearLayout time_spinner_layout , min_spinner_layout, infra_layout , area_layout , office_group_layout,
                    unit_layout ,high_code_layout,low_code_layout,care_code_layout , jip_care_layout;

    int check_count;

    public Fragment_trouble_insert_jip(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_insert_jip, container ,false);
        context = getActivity();

        // shared 에서 user_info 가져옴
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String dep_name = pref.getString("dep_name",null);

        // 조치 레이아웃 gone
        jip_care_layout = (LinearLayout)view.findViewById(R.id.jip_care_layout);
        jip_care_layout.setVisibility(View.GONE);

        nms_dep_name = (TextView)view.findViewById(R.id.nms_dep_name);
        nms_dep_name.setText(dep_name);
        filed_error_map = new HashMap<>();

        filed_error_map.put("direct_care","Y");
        today_group = (RadioGroup)view.findViewById(R.id.today_group);
        today_y = (RadioButton)view.findViewById(R.id.today_y);
        today_n = (RadioButton)view.findViewById(R.id.today_n);
        today_y.setChecked(true);
        today_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.today_y :
                        today_y.setChecked(true);
                        today_n.setChecked(false);
                        filed_error_map.put("direct_care","Y");
                        break;
                    case R.id.today_n :
                        today_y.setChecked(false);
                        today_n.setChecked(true);
                        filed_error_map.put("direct_care","N");
                        break;
                }
            }
        });

        // 사원리스트 가져옴
        String emp_id = pref.getString("emp_id",null);
        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Edu_Emp_Vo>> call_emp = erp.Edu_care_emp_list(emp_id);
        new Fragment_trouble_insert_jip.Edu_Care_Emp_List().execute(call_emp);

//        time_spinner_layout = (LinearLayout)view.findViewById(R.id.time_spinner_layout);
//        min_spinner_layout = (LinearLayout)view.findViewById(R.id.min_spinner_layout);

        infra_layout = (LinearLayout)view.findViewById(R.id.infra_layout);
        area_layout = (LinearLayout)view.findViewById(R.id.area_layout);
        office_group_layout = (LinearLayout)view.findViewById(R.id.office_group_layout);
        unit_layout = (LinearLayout)view.findViewById(R.id.unit_layout);
        high_code_layout = (LinearLayout)view.findViewById(R.id.high_code_layout);
        low_code_layout = (LinearLayout)view.findViewById(R.id.low_code_layout);
        care_code_layout = (LinearLayout)view.findViewById(R.id.care_code_layout);

//        time_spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

        jip_trouble_care_text = (TextView)view.findViewById(R.id.jip_trouble_care_text);
        jip_edit_care_emp_btn = (Button)view.findViewById(R.id.jip_edit_care_emp_btn);
        jip_edit_care_emp_btn.setOnClickListener(new View.OnClickListener() {
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
                        jip_trouble_care_text.setText("현재 대상자 " + check_count +"명");
//                        care_emp_list = new ArrayList<>();
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
        // 사원리스트 가져와서 모달 띄우는 부분 end

        // dialog 호출
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        // 화면의 달력 날짜 등록 , 달력 활성화
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String get_today = sdf.format(date);
//        start_day = (TextView)view.findViewById(R.id.start_day);
//        start_day.setText(get_today);
//
//        final Calendar cal = Calendar.getInstance();
//        view.findViewById(R.id.start_day).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
//
//                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
//                        start_day.setText(msg);
//
//                    }
//                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//
//                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
//                dialog.show();
//            }
//        });
        new get_app_history_office_group().execute();

        // 장애 등록시 필요한 spiner 등록
        nms_infra_type = (Spinner)view.findViewById(R.id.nms_infra_type);
        nms_group = (Spinner)view.findViewById(R.id.nms_group);
        nms_office_group = (Spinner)view.findViewById(R.id.nms_office_group);
        nms_unit_code = (Spinner)view.findViewById(R.id.nms_unit_code);
        nms_trouble_high_code = (Spinner)view.findViewById(R.id.nms_trouble_high_code);
        nms_trouble_low_code = (Spinner)view.findViewById(R.id.nms_trouble_low_code);
        nms_care_code = (Spinner)view.findViewById(R.id.nms_care_code);

//        nms_start_hour = (Spinner)view.findViewById(R.id.nms_start_hour);
//        nms_start_min = (Spinner)view.findViewById(R.id.nms_start_min);

        //체크박스
        bs_yn = (CheckBox)view.findViewById(R.id.bs_yn);
        bs_yn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    filed_error_map.put("bs_yn","Y");
                    jip_care_layout.setVisibility(View.VISIBLE);
                } else {
                    filed_error_map.put("bs_yn","N");
                    jip_care_layout.setVisibility(View.GONE);
                }
            }
        });

        //장애발생시간 포커스 (백그라운드 수정)
//        nms_start_hour.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

        //edti Text
        nms_garage_id = (EditText)view.findViewById(R.id.nms_garage_id);
        nms_notice = (EditText)view.findViewById(R.id.nms_notice);

        nms_infra_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String serch_type = parent.getItemAtPosition(position).toString();
                if(!serch_type.equals("검색조건")){
                    infra_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                    area_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                    new Getfield_trouble_error_type().execute(serch_type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        nms_start_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                start_hour = parent.getItemAtPosition(position).toString();
//                if(!"- 시간 -".equals(start_hour)){
//                    time_spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
//                    min_spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        nms_start_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String serch_type = parent.getItemAtPosition(position).toString();
//                start_min = serch_type;
//                if(!"- 분 -".equals(start_min)){
//                    min_spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
//                    infra_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        infra_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

        submit_barcode = (Button)view.findViewById(R.id.submit_barcode);
        submit_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emp_id = pref.getString("emp_id",null);
                String dep_code = pref.getString("dep_code",null);
                String care_cd = (String)filed_error_map.get("trouble_care_cd");

                filed_error_map.put("bus_id","000001010");

                filed_error_map.put("emp_id",emp_id);
                filed_error_map.put("dep_code",dep_code);
                filed_error_map.put("infra_code",infra_code);
                filed_error_map.put("service_id",service_id);
                filed_error_map.put("garage_id",nms_garage_id.getText().toString());
                filed_error_map.put("route_id","");
                filed_error_map.put("driver_tel_num","");
                filed_error_map.put("notice",nms_notice.getText().toString());
                filed_error_map.put("job_viewer",emp_id);
                filed_error_map.put("reg_emp_id",emp_id);


                // 이부분 화면에서 입력할때 무조건 입력하게끔으로 바꿔야함
                filed_error_map.put("unit_change_yn","N");
                filed_error_map.put("unit_before_id","");
                filed_error_map.put("unit_after_id","");

                filed_error_map.put("move_distance","");
                filed_error_map.put("move_time","");
                filed_error_map.put("wait_time","");
                filed_error_map.put("work_time","");

                filed_error_map.put("restore_yn","N");


                filed_error_map.put("mintong","N");
                filed_error_map.put("analysis_yn","N");

//                filed_error_map.put("reg_date",start_day.getText().toString());

//                start_min = start_min.replaceAll("분","");
//                start_hour = start_hour.replaceAll("시","");

//                if(start_min.equals("-  -") || start_hour.equals("- 간 -")){
//                    Toast.makeText(context,"시간 , 분을 선택해주세요 . " , Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if(infra_code == null){
                    Toast.makeText(context,"인프라를 선택해주세요" , Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("transp_bizr_id") == null){
                    Toast.makeText(context,"조합,운수사 를 선택해주세요" , Toast.LENGTH_SHORT).show();
                    return;
                }else if(nms_garage_id.getText().toString().equals("")){
                    Toast.makeText(context,"영업소를 입력해주세요" , Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("unit_code") == null){
                    Toast.makeText(context,"장비를 선택해주세요" , Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("trouble_high_cd") == null){
                    Toast.makeText(context,"대분류를 선택해주세요" , Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("trouble_low_cd") == null){
                    Toast.makeText(context,"소분류를 선택해주세요" , Toast.LENGTH_SHORT).show();
                    return;
                }

                if(bs_yn.isChecked()){
                    filed_error_map.put("bs_yn","Y");
                    if("X001".equals(care_cd)){
                        Toast.makeText(context,"BS건입니다. 조치항목을 선택해 주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if("N".equals(filed_error_map.get("direct_care"))){
                        Toast.makeText(context,"BS건입니다. 당일 처리로 등록해 주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    filed_error_map.put("bs_yn","N");
                    if(!"X001".equals(care_cd)){
                        nms_care_code.setSelection(0);
                        filed_error_map.put("trouble_care_cd","X001");
                        Toast.makeText(context,"일반장애 접수입니다. \n조치항목을 미처리로 변경, \n등록하기를 다시 터치 해주세요.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if("N".equals(filed_error_map.get("direct_care"))){
                    if(!"X001".equals(care_cd)){
                        nms_care_code.setSelection(0);
                        filed_error_map.put("trouble_care_cd","X001");
                        Toast.makeText(context,"당일 미처리건입니다. \n조치항목을 미처리로 변경, \n등록하기를 다시 터치 해주세요.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if(check_count > 0){
                    filed_error_map.put("cooperate_key","Y");
                }else{
                    filed_error_map.put("cooperate_key","N");
                }

                progressDialog.setMessage("등록중...");
                progressDialog.show();

//                filed_error_map.put("reg_time",start_hour+start_min);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
                String getdate = sdf.format(date);
                String gettime = sdf2.format(date);

                filed_error_map.put("reg_time",gettime);
                filed_error_map.put("reg_date",getdate);

                if("X001".equals(care_cd)){
                    new Fragment_trouble_insert_jip.insert_filed_error_jip_bus().execute();
                }else{
                    new Fragment_trouble_insert_jip.app_jip_bus_care_insert().execute();
                }
            }
        });

        return view;
    }

    //조합 리스트
    private class get_app_history_office_group extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_infoVo>> call = erp.get_app_history_office_group();
            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    final List<Bus_infoVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("조합 선택");
                    for (Bus_infoVo i : list){
                        spinner_list.add(i.getOffice_group());
                    }
                    nms_group.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nms_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position >0){
                                area_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                office_group_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                String select_nms_group = spinner_list.get(position);
                                new Get_app_error_Bus_Office().execute(select_nms_group);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {
                }
            });
            return null;
        }
    }

    // 운수사 리스트
    private class Get_app_error_Bus_Office extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_infoVo>> call = erp.get_app_error_Bus_Office(strings[0]);
            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    final List<Bus_infoVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("운수사 선택");
                    for (Bus_infoVo i : list){
                        spinner_list.add(i.getBusoff_name());
                    }
                    nms_office_group.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nms_office_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position >0){
                                filed_error_map.put("transp_bizr_id",list.get(position-1).getTransp_bizr_id());
                                office_group_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                unit_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    // 장비 리스트
    private class Getfield_trouble_error_type extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            String serch_type = strings[0];
            if(serch_type.equals("이비카드")){
                service_id = "02";
                infra_code = "1";
            }else if(serch_type.equals("마이비")){
                service_id = "02";
                infra_code = "2";
            }
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type(service_id,infra_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("장비 선택");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
                    nms_unit_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nms_unit_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position > 0){
                                unit_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                high_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

                                unit_code = list.get(position-1).getUnit_code();
                                filed_error_map.put("unit_code",unit_code);
                                new Getfield_trouble_high_code().execute(unit_code);
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

    // 대분류 리스트
    private class Getfield_trouble_high_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_high_code(service_id,infra_code,strings[0]);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("대분류 선택");
                    for(Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_high_name());
                    }
                    nms_trouble_high_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nms_trouble_high_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            nms_trouble_low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                            nms_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                            filed_error_map.put("trouble_care_cd",null);
                            filed_error_map.put("trouble_low_cd",null);
                            filed_error_map.put("trouble_high_cd",null);
                            if(position > 0){
                                high_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                low_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

                                trouble_high_code = list.get(position-1).getTrouble_high_cd();
                                filed_error_map.put("trouble_high_cd",trouble_high_code);
                                new Getfield_trouble_low_code().execute();
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

    //소분류 리스트
    private class Getfield_trouble_low_code extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_low_code(service_id,infra_code,unit_code,trouble_high_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("소분류 선택");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_low_name());
                    }
                    nms_trouble_low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nms_trouble_low_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position > 0){
                                filed_error_map.put("trouble_care_cd","X001");
                                low_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                care_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

                                trouble_low_code = list.get(position-1).getTrouble_low_cd();
                                filed_error_map.put("trouble_low_cd",trouble_low_code);
                                new Getfield_trouble_carecode().execute();
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

    // 조치 항목 리스트
    private class Getfield_trouble_carecode extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_carecode(service_id,infra_code,unit_code,trouble_high_code,trouble_low_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("조치항목 선택 (미선택 시 현재 미처리)");
                    for(Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_care_name());
                    }
                    nms_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nms_care_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_care_name = spinner_list.get(position);
                            String select_care_code = "";
                            if(position > 0){
                                care_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                for(int i = 0 ; i < list.size(); i++){
                                    if(list.get(i).getTrouble_care_name() == select_care_name){
                                        select_care_code = list.get(i).getTrouble_care_cd();
                                        filed_error_map.put("trouble_care_cd",select_care_code);
                                        break;
                                    }
                                }
                            }else{
                                filed_error_map.put("trouble_care_cd","X001");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("scan")){
        }
    }

    private class insert_filed_error_jip_bus extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.insert_filed_error_jip_bus(filed_error_map,cooperate_list);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    boolean result = response.body();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    page_info = "list";
                    a_builder.setTitle("콜 처리");
                    a_builder.setCancelable(false);
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    String title = "";
                                    if(page_info.equals("repg")){
                                        fragment = new Fragment_trouble_insert();
                                    }else{
                                        fragment = new Fragment_trouble_insert();
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

    private class app_jip_bus_care_insert extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.app_jip_bus_care_insert(filed_error_map,cooperate_list);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    boolean result = response.body();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    page_info = "list";
                    a_builder.setTitle("콜 처리");
                    a_builder.setCancelable(false);
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    String title = "";
                                    if(page_info.equals("repg")){
                                        fragment = new Fragment_trouble_insert();
                                    }else{
                                        fragment = new Fragment_trouble_insert();
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
}
