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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import app.erp.com.erp_app.New_Bus_Activity;
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

public class Fragment_trouble_insert_bus_call_center extends Fragment {

    Button bus_num_find , bus_num_barcode_find,error_insert_btn ,insert_bus_info,edit_care_emp_list;
    Context context;

    private Retrofit retrofit;
    private DialogEduEmpList mdialog;

    String click_type , page_info;
    EditText find_bus_num ,field_error_garage ,field_error_route, field_error_phone, field_error_notice,unit_before_id,unit_after_id;
    TextView bus_area_name , bus_office_name, trouble_care_list;
    SharedPreferences pref, barcode_type_pref;
    SharedPreferences.Editor editor;

    LinearLayout care_layout , old_new_layout , old_select , old_barcode
            , new_old_layout , new_selcet ,new_barcode, unit_before_camera
            ,unit_after_camera, bus_num_9999 , bus_num_nomal , call_layout;

    CheckBox bs_yn;

    Spinner bus_num_list, field_trouble_error_type_list , field_trouble_high_code_list,
            field_trouble_low_code_list, field_trouble_care_code_list , bus_area_spinner,
            bus_office_group;

    RadioGroup job_viewer;
    RadioButton hq , ic ,gb ,gn;

    HashMap<String, Object> filed_error_map;

    View topview;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;
    Fragment tt;

    InputMethodManager imm;

    List<Edu_Emp_Vo> edu_list;
    List<Edu_Emp_Vo> gblist = new ArrayList<>();
    List<Edu_Emp_Vo> gnlist = new ArrayList<>();
    List<Edu_Emp_Vo> iclist = new ArrayList<>();
    List<Edu_Emp_Vo> pclist = new ArrayList<>();
    List<String> cooperate_list ;

    ScrollView main_container;

    int check_count;
    public Fragment_trouble_insert_bus_call_center(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_insert_bus_call_center, container ,false);
        topview =view;
        context = getActivity();

        main_container = (ScrollView) view.findViewById(R.id.main_container);

        imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        tt = this;
        //장애등록 담아서 보낼 hashmap
        filed_error_map = new HashMap<>();
        //차량번호 검색 버튼
        find_bus_num = (EditText)view.findViewById(R.id.find_bus_num);
        find_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    new Fragment_trouble_insert_bus_call_center.getfield_error_busnum().execute(find_bus_num.getText().toString());

                    handled =true;
                    downKeyboard(find_bus_num);
                    find_bus_num.clearFocus();
                }
                return handled;
            }
        });
        // 버스번호 포커스
        find_bus_num.requestFocus();
        upKeyboard(find_bus_num);

        // 유저정보 가져옴
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        //조합 운수사 감싸주는 레이아웃
        bus_num_9999 = (LinearLayout)view.findViewById(R.id.bus_num_9999);
        bus_num_9999.setVisibility(View.GONE);
        bus_num_nomal = (LinearLayout)view.findViewById(R.id.bus_num_nomal);



        // 사원리스트 가져옴
        String emp_id = pref.getString("emp_id",null);

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Edu_Emp_Vo>> call_emp = erp.Edu_care_emp_list(emp_id);
        new Fragment_trouble_insert_bus_call_center.Edu_Care_Emp_List().execute(call_emp);

        trouble_care_list = (TextView)view.findViewById(R.id.trouble_care_list);

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

        EditText inputField = (EditText)view.findViewById(R.id.field_error_phone);
        inputField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        bus_num_list = (Spinner)view.findViewById(R.id.bus_num_list);
        field_trouble_error_type_list = (Spinner)view.findViewById(R.id.field_trouble_error_type_list);
        field_trouble_high_code_list = (Spinner)view.findViewById(R.id.field_trouble_high_code_list);
        field_trouble_low_code_list = (Spinner)view.findViewById(R.id.field_trouble_low_code_list);


        // editText
        field_error_garage =(EditText)view.findViewById(R.id.field_error_garage);
        field_error_route = (EditText)view.findViewById(R.id.field_error_route);
        field_error_phone = (EditText)view.findViewById(R.id.field_error_phone);
        field_error_notice = (EditText)view.findViewById(R.id.field_error_notice);

        barcode_type_pref = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        editor = barcode_type_pref.edit();

        bus_area_name = (TextView)view.findViewById(R.id.bus_area_name);
        bus_office_name = (TextView)view.findViewById(R.id.bus_office_name);

        //조합 spinner
        bus_area_spinner = (Spinner)view.findViewById(R.id.bus_area_spinner);
        //운수사 spinner
        bus_office_group = (Spinner)view.findViewById(R.id.bus_office_group);
        new get_app_history_office_group().execute();

        insert_bus_info = (Button)view.findViewById(R.id.insert_bus_info);
        insert_bus_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , New_Bus_Activity.class);
                startActivity(i);
            }
        });

//      버스번호 텍스트 검색
        bus_num_find = (Button)view.findViewById(R.id.bus_num_find);
        bus_num_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "text";
                downKeyboard(find_bus_num);
                find_bus_num.clearFocus();
                if(find_bus_num.getText().length() == 0){
                    Toast.makeText(context,"버스번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    new getfield_error_busnum().execute(find_bus_num.getText().toString());

                }
            }
        });

        // 지부 라디오 버튼
        job_viewer = (RadioGroup)view.findViewById(R.id.job_viewer);
        ic = (RadioButton)view.findViewById(R.id.ic);
        hq = (RadioButton)view.findViewById(R.id.hq);
        gb = (RadioButton)view.findViewById(R.id.gb);
        gn = (RadioButton)view.findViewById(R.id.gn);
        job_viewer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ic :
                        ic.setChecked(true);
                        hq.setChecked(false);
                        gb.setChecked(false);
                        gn.setChecked(false);
                        filed_error_map.put("job_viewer","D29");
                        break;
                    case R.id.hq :
                        ic.setChecked(false);
                        hq.setChecked(true);
                        gb.setChecked(false);
                        gn.setChecked(false);
                        filed_error_map.put("job_viewer","C30");
                        break;
                    case R.id.gb :
                        ic.setChecked(false);
                        hq.setChecked(false);
                        gb.setChecked(true);
                        gn.setChecked(false);
                        filed_error_map.put("job_viewer","D23");
                        break;
                    case R.id.gn :
                        ic.setChecked(false);
                        hq.setChecked(false);
                        gb.setChecked(false);
                        gn.setChecked(true);
                        filed_error_map.put("job_viewer","D22");
                        break;
                }
            }
        });

//      당일 처리 미처리 라디오 버튼
        filed_error_map.put("direct_care","Y");

        error_insert_btn = (Button)view.findViewById(R.id.error_insert_btn);
        error_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                all_keyboard_down();
                String emp_id = pref.getString("emp_id",null);
                String dep_code = pref.getString("dep_code",null);

                filed_error_map.put("emp_id",emp_id);
                filed_error_map.put("dep_code",dep_code);
                filed_error_map.put("infra_code","1");
                filed_error_map.put("service_id","01");
                filed_error_map.put("garage_id",field_error_garage.getText().toString());
                filed_error_map.put("route_id",field_error_route.getText().toString());
                filed_error_map.put("driver_tel_num",field_error_phone.getText().toString());
                filed_error_map.put("notice",field_error_notice.getText().toString());
                filed_error_map.put("reg_emp_id",emp_id);

                filed_error_map.put("unit_change_yn","N");
                filed_error_map.put("unit_before_id","");
                filed_error_map.put("unit_after_id","");

                // 이부분 화면에서 입력할때 무조건 입력하게끔으로 바꿔야함
                filed_error_map.put("move_distance","");
                filed_error_map.put("move_time","");
                filed_error_map.put("wait_time","");
                filed_error_map.put("work_time","");
                filed_error_map.put("restore_yn","N");

                filed_error_map.put("mintong","N");
                filed_error_map.put("analysis_yn","N");

                filed_error_map.put("bs_yn","N");

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
                String getdate = sdf.format(date);
                String gettime = sdf2.format(date);

                filed_error_map.put("reg_time",gettime);
                filed_error_map.put("reg_date",getdate);

                if(null == filed_error_map.get("transp_bizr_id") ){
                    Toast.makeText(context,"운수사를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("bus_id") == null){
                    Toast.makeText(context,"버스를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if (filed_error_map.get("unit_code") == null){
                    Toast.makeText(context,"장비를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("trouble_high_cd") == null){
                    Toast.makeText(context,"대분류를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("trouble_low_cd") == null){
                    Toast.makeText(context,"소분류를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(null == filed_error_map.get("job_viewer")){
                    Toast.makeText(context,"지부를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                // 공동 작업자 인원수 체크해서 0명이면 key 값 n으로 0명이상이면 key y 로 넘기고 받은다음에 reg_emp_id 랑 care_emp_id  바꾸고
                // 컨트롤러에서 어차피 맵으로 받으니까 맵에다가 리스트 넣고 y 일때만 꺼내서 사용
                // 잡뷰어 코드 수정해야하는지 확인해야함 그 다음에 for 문으로 인원수만큼 인서트
                // insert_filed_error_test 현재 미조치 인서트
                // app_fieldError_care_insert 조치 완료 인서트

                if(check_count > 0){
                    filed_error_map.put("cooperate_key","Y");
                }else{
                    filed_error_map.put("cooperate_key","N");
                }
                progressDialog.setMessage("등록중...");
                progressDialog.show();
                new app_fieldError_insert_call().execute();

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("scan")){
            new getfield_error_busnum().execute(barcode);

        }else if(click_type.equals("before")){
            unit_before_id.setText(barcode);
        }else if(click_type.equals("after")){
            unit_after_id.setText(barcode);
        }
    }

    //버스번호 검색
    private class getfield_error_busnum extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_infoVo>> call = erp.getfield_error_busnum(strings[0]);

            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    start_spinner();
                    find_bus_num.clearFocus();
                    bus_num_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                    final List<Bus_infoVo> list = response.body();
                    if(list.size() > 0){
                        Toast.makeText(context,"버스를 선택해주세요",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"검색결과가 없습니다 다른 버스번호로 다시 검색해보세요",Toast.LENGTH_SHORT).show();
                    }
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("버스번호를 선택해주세요.");
                    for(Bus_infoVo i : list){
                        spinner_list.add(i.getBusoff_bus());
                    }

                    bus_num_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_num_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            filed_error_map.put("transp_bizr_id",null);
                            filed_error_map.put("trouble_high_cd",null);
                            filed_error_map.put("trouble_low_cd",null);

                            filed_error_map.put("unit_code",null);

                            if(position > 0){
                                String select_bus_num = spinner_list.get(position);
                                for(int i = 0 ; i < list.size(); i++){
                                    if(list.get(i).getBusoff_bus() == select_bus_num){
                                        if("999999999".equals(list.get(i).getBus_id()) || "998999999".equals(list.get(i).getBus_id()) || "997999999".equals(list.get(i).getBus_id()) ){
                                            office_display_check(true);
                                            bus_area_name.setText("");
                                            bus_office_name.setText("");
                                        }else{
                                            office_display_check(false);
                                            filed_error_map.put("transp_bizr_id",list.get(i).getTransp_bizr_id());
                                            bus_area_name.setText(list.get(i).getOffice_group());
                                            bus_office_name.setText(list.get(i).getBusoff_name());
                                        }
                                        filed_error_map.put("bus_id",list.get(i).getBus_id());
                                        break;
                                    }
                                }
                                bus_num_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
//                                field_error_route.requestFocus();
//                                upKeyboard(field_error_route);
                                field_trouble_error_type_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                // 장비 가져오는 레트로핏 execute
                                new getfield_trouble_error_type().execute();
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

    // 장비 리스트 가져옴
    private class getfield_trouble_error_type extends AsyncTask<String , Integer , Long >{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            //스프링 프로젝트에서 장비리스트 가져오는 Call
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type("01","1");
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {

                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("장비를 선택해주세요");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
//                    move_scroll(field_trouble_error_type_list);
                    field_trouble_error_type_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                    field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));


                    field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                    field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));

                    field_trouble_error_type_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(position > 0){
                                field_trouble_error_type_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                String select_error_name = spinner_list.get(position);
                                String select_error_code = "";
                                for(int i = 0; i < list.size(); i++){
                                    if(list.get(i).getUnit_name() == select_error_name){
                                        select_error_code = list.get(i).getUnit_code();
                                        filed_error_map.put("unit_code",list.get(i).getUnit_code());
                                        break;
                                    }
                                }
                                field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                new getfield_trouble_high_code().execute(select_error_code);
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

    //장애 대분류 리스트 가져옴
    private class getfield_trouble_high_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final String select_error_code = strings[0];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_high_code("01","1",select_error_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("장애대분류를 선택해주세요");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_high_name());
                    }
                    move_scroll(field_trouble_high_code_list);
                    field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_high_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));

                            filed_error_map.put("trouble_high_cd",null);
                            filed_error_map.put("trouble_low_cd",null);

                            if(position > 0){
                                field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                String select_high_name = spinner_list.get(position);
                                String select_high_code = "";
                                for(int i = 0; i < list.size(); i++){
                                    if(list.get(i).getTrouble_high_name() == select_high_name){
                                        select_high_code = list.get(i).getTrouble_high_cd();

                                        filed_error_map.put("trouble_high_cd",list.get(i).getTrouble_high_cd());
                                        break;
                                    }
                                }
                                field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                new getfield_trouble_low_code().execute(select_error_code,select_high_code);
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

    // 소분류 리스트 가져옴
    private class getfield_trouble_low_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final String select_error_code = strings[0];
            final String select_high_code = strings[1];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_low_code("01","1",select_error_code,select_high_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("장애소분류를 선택해주세요");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_low_name());
                    }
                    move_scroll(field_trouble_low_code_list);
                    field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_low_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(position > 0){
                                field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                String select_low_name = spinner_list.get(position);
                                String select_low_code = "";

                                for(int i = 0 ; i < list.size(); i++){
                                    if(list.get(i).getTrouble_low_name() == select_low_name){
                                        select_low_code = list.get(i).getTrouble_low_cd();
                                        filed_error_map.put("trouble_low_cd",list.get(i).getTrouble_low_cd());
                                        break;
                                    }
                                }
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
                    bus_area_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position >0){
                                String select_nms_group = spinner_list.get(position);
                                new Fragment_trouble_insert_bus_call_center.Get_app_error_Bus_Office().execute(select_nms_group);
                            }else{
                                bus_office_group.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
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
                    for (Bus_infoVo i : list){
                        spinner_list.add(i.getBusoff_name());
                    }
                    bus_office_group.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_office_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            filed_error_map.put("transp_bizr_id",list.get(position).getTransp_bizr_id());
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

    private class app_fieldError_insert_call extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.app_fieldError_insert_call(filed_error_map);
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
                    Log.d("t:",t.toString());
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

    public void downKeyboard(EditText editText) {
//        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void upKeyboard(EditText editText){
//        imm.showSoftInput(editText,0);
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void move_scroll(View view){
        main_container.smoothScrollTo(0,400);
    }

    private void all_keyboard_down(){
        downKeyboard(find_bus_num);
        downKeyboard(field_error_garage);
        downKeyboard(field_error_route);
        downKeyboard(field_error_phone);
        downKeyboard(field_error_notice);
    }

    private void office_display_check(boolean check){
        if(check){
            field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
            field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));


            field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
            field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));

            bus_area_spinner.setSelection(0);
            bus_num_9999.setVisibility(View.VISIBLE);
            bus_num_nomal.setVisibility(View.GONE);
        }else{
            bus_num_nomal.setVisibility(View.VISIBLE);
            bus_num_9999.setVisibility(View.GONE);
        }
    }

    private void start_spinner(){

        field_trouble_error_type_list.setSelection(0);

        bus_area_name.setText("");
        bus_office_name.setText("");

        field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
        field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));

        field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
        field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));

    }

}