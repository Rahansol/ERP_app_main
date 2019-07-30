package app.erp.com.erp_app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.erp.com.erp_app.Fragment_d_1_t.downKeyboard;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_d_1 extends Fragment implements MainActivity.OnBackPressedListener{

    Button bus_num_find , bus_num_barcode_find,error_insert_btn;
    Context context;

    private Retrofit retrofit;

    String click_type , page_info;
    EditText find_bus_num ,field_error_garage ,field_error_route, field_error_phone, field_error_notice,unit_before_id,unit_after_id;
    TextView reserve_area_name , reserve_unit_barcode;
    SharedPreferences pref, barcode_type_pref;
    SharedPreferences.Editor editor;

    LinearLayout care_layout , old_new_layout , old_select , old_barcode , new_old_layout , new_selcet ,new_barcode, unit_before_camera,unit_after_camera;

    CheckBox bs_yn;

    Spinner bus_num_list, field_trouble_error_type_list , field_trouble_high_code_list, field_trouble_low_code_list, field_trouble_care_code_list;

    RadioGroup today_group;
    RadioButton today_y , today_n;

    HashMap<String, Object> filed_error_map;

    View topview;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;

    public Fragment_d_1(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d_1, container ,false);
        topview =view;
        context = getActivity();
        //장애등록 담아서 보낼 hashmap
        filed_error_map = new HashMap<>();
        //차량번호 검색 버튼
        find_bus_num = (EditText)view.findViewById(R.id.find_bus_num);
        find_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    new Fragment_d_1.getfield_error_busnum().execute(find_bus_num.getText().toString());
                    handled =true;
                    downKeyboard(context , find_bus_num);
                }
                return handled;
            }
        });

        // 유저정보 가져옴
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        EditText inputField = (EditText)view.findViewById(R.id.field_error_phone);
        inputField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        bus_num_list = (Spinner)view.findViewById(R.id.bus_num_list);
        field_trouble_error_type_list = (Spinner)view.findViewById(R.id.field_trouble_error_type_list);
        field_trouble_high_code_list = (Spinner)view.findViewById(R.id.field_trouble_high_code_list);
        field_trouble_low_code_list = (Spinner)view.findViewById(R.id.field_trouble_low_code_list);
        field_trouble_care_code_list = (Spinner)view.findViewById(R.id.field_trouble_care_code_list);

        // editText
        field_error_garage =(EditText)view.findViewById(R.id.field_error_garage);
        field_error_route = (EditText)view.findViewById(R.id.field_error_route);
        field_error_phone = (EditText)view.findViewById(R.id.field_error_phone);
        field_error_notice = (EditText)view.findViewById(R.id.field_error_notice);
        unit_before_id = (EditText)view.findViewById(R.id.unit_before_id);
        unit_after_id = (EditText)view.findViewById(R.id.unit_after_id);

        barcode_type_pref = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        editor = barcode_type_pref.edit();

        reserve_area_name = (TextView)view.findViewById(R.id.reserve_area_name);
        reserve_unit_barcode = (TextView)view.findViewById(R.id.reserve_unit_barcode);
        //버스 번호 바코드 스캔
        bus_num_barcode_find = (Button)view.findViewById(R.id.bus_num_barcode_find);
        bus_num_barcode_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "scan";
                editor.putString("camera_type" , "bus");
                editor.commit();
                new getfield_error_busnum().execute("02105671016854");
//                IntentIntegrator.forFragment(Fragment_d_1.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        //기존 바코드 스캔
        unit_before_camera = (LinearLayout)view.findViewById(R.id.unit_before_camera);
        unit_before_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "before";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forFragment(Fragment_d_1.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });
        //교체 후 바코드 스캔
        unit_after_camera = (LinearLayout)view.findViewById(R.id.unit_after_camera);
        unit_after_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "after";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forFragment(Fragment_d_1.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });
//      버스번호 텍스트 검색
        bus_num_find = (Button)view.findViewById(R.id.bus_num_find);
        bus_num_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "text";
                downKeyboard(context,find_bus_num);
                new getfield_error_busnum().execute(find_bus_num.getText().toString());
            }
        });
//      당일 처리 미처리 라디오 버튼
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

        care_layout = (LinearLayout)view.findViewById(R.id.care_layout);
        old_new_layout = (LinearLayout)view.findViewById(R.id.old_new_layout);
        old_select = (LinearLayout)view.findViewById(R.id.old_select);
        old_barcode = (LinearLayout)view.findViewById(R.id.old_barcode);
        new_old_layout = (LinearLayout)view.findViewById(R.id.new_old_layout);
        new_selcet = (LinearLayout)view.findViewById(R.id.new_selcet);
        new_barcode = (LinearLayout)view.findViewById(R.id.new_barcode);

        care_layout.setVisibility(View.GONE);
        old_new_layout.setVisibility(View.GONE);
        old_select.setVisibility(View.GONE);
        old_barcode.setVisibility(View.GONE);
        new_old_layout.setVisibility(View.GONE);
        new_selcet.setVisibility(View.GONE);
        new_barcode.setVisibility(View.GONE);

        bs_yn = (CheckBox)view.findViewById(R.id.bs_yn);
        bs_yn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    care_layout.setVisibility(View.VISIBLE);
                    old_new_layout.setVisibility(View.VISIBLE);
//                    old_select.setVisibility(View.VISIBLE);
                    old_barcode.setVisibility(View.VISIBLE);
                    new_old_layout.setVisibility(View.VISIBLE);
//                    new_selcet.setVisibility(View.VISIBLE);
                    new_barcode.setVisibility(View.VISIBLE);
                    filed_error_map.put("bs_yn","Y");
                } else {
                    care_layout.setVisibility(View.GONE);
                    old_new_layout.setVisibility(View.GONE);
                    old_select.setVisibility(View.GONE);
                    old_barcode.setVisibility(View.GONE);
                    new_old_layout.setVisibility(View.GONE);
                    new_selcet.setVisibility(View.GONE);
                    new_barcode.setVisibility(View.GONE);
                    filed_error_map.put("bs_yn","N");
                }
            }
        });

        error_insert_btn = (Button)view.findViewById(R.id.error_insert_btn);
        error_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                filed_error_map.put("job_viewer",emp_id);
                filed_error_map.put("reg_emp_id",emp_id);
                filed_error_map.put("unit_before_id",unit_before_id.getText().toString());
                filed_error_map.put("unit_after_id",unit_after_id.getText().toString());

                // 이부분 화면에서 입력할때 무조건 입력하게끔으로 바꿔야함
                if(unit_before_id.getText().toString().length() != 0 || unit_after_id.getText().toString().length() != 0){
                    filed_error_map.put("unit_change_yn","Y");
                }else{
                    filed_error_map.put("unit_change_yn","N");
                }
                filed_error_map.put("unit_before_id",unit_before_id.getText().toString());
                filed_error_map.put("unit_after_id",unit_after_id.getText().toString());
                filed_error_map.put("move_distance","");
                filed_error_map.put("move_time","");
                filed_error_map.put("wait_time","");
                filed_error_map.put("work_time","");
                filed_error_map.put("restore_yn","N");
                if(bs_yn.isChecked()){
                    filed_error_map.put("bs_yn","Y");
                }else{
                    filed_error_map.put("bs_yn","N");
                }
                filed_error_map.put("mintong","N");
                filed_error_map.put("analysis_yn","N");

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
                String getdate = sdf.format(date);
                String gettime = sdf2.format(date);

                filed_error_map.put("reg_time",gettime);
                filed_error_map.put("reg_date",getdate);
                filed_error_map.put("unit_change_yn","N");
                filed_error_map.put("unit_before_id","");
                filed_error_map.put("unit_after_id","");

                progressDialog.setMessage("등록중...");
                progressDialog.show();
                new insert_filed_error_test().execute();
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
                    final List<Bus_infoVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for(Bus_infoVo i : list){
                        spinner_list.add(i.getBusoff_bus());
                    }
                    bus_num_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_num_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_bus_num = spinner_list.get(position);
                            for(int i = 0 ; i < list.size(); i++){
                                if(list.get(i).getBusoff_bus() == select_bus_num){
                                    reserve_area_name.setText(list.get(i).getOffice_group());
                                    reserve_unit_barcode.setText(list.get(i).getBusoff_name());

                                    filed_error_map.put("transp_bizr_id",list.get(i).getTransp_bizr_id());
                                    filed_error_map.put("bus_id",list.get(i).getBus_id());
                                    break;
                                }
                            }
                            new getfield_trouble_error_type().execute();
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

    private class getfield_trouble_error_type extends AsyncTask<String , Integer , Long >{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type("1","01");
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
                    field_trouble_error_type_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_error_type_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_error_name = spinner_list.get(position);
                            String select_error_code = "";
                            for(int i = 0; i < list.size(); i++){
                                if(list.get(i).getUnit_name() == select_error_name){
                                    select_error_code = list.get(i).getUnit_code();
                                    filed_error_map.put("unit_code",list.get(i).getUnit_code());
                                    break;
                                }
                            }
                            Log.d("select_error_code","select_error_code:"+select_error_code);
                            if(select_error_code.equals("GT")){
                                old_select.setVisibility(View.VISIBLE);
                                new_selcet.setVisibility(View.VISIBLE);
                            }else{
                                old_select.setVisibility(View.GONE);
                                new_selcet.setVisibility(View.GONE);
                            }
                            new getfield_trouble_high_code().execute(select_error_code);

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

    private class getfield_trouble_high_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final String select_error_code = strings[0];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_high_code("1","01",select_error_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_high_name());
                    }
                    field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_high_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_high_name = spinner_list.get(position);
                            String select_high_code = "";
                            for(int i = 0; i < list.size(); i++){
                                if(list.get(i).getTrouble_high_name() == select_high_name){
                                    select_high_code = list.get(i).getTrouble_high_cd();

                                    filed_error_map.put("trouble_high_cd",list.get(i).getTrouble_high_cd());
                                    break;
                                }
                            }
                            new getfield_trouble_low_code().execute(select_error_code,select_high_code);
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
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_low_code("1","01",select_error_code,select_high_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_low_name());
                    }
                    field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_low_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_low_name = spinner_list.get(position);
                            String select_low_code = "";

                            for(int i = 0 ; i < list.size(); i++){
                                if(list.get(i).getTrouble_low_name() == select_low_name){
                                    select_low_code = list.get(i).getTrouble_low_cd();

                                    filed_error_map.put("trouble_low_cd",list.get(i).getTrouble_low_cd());

                                    break;
                                }
                            }
                            new getfield_trouble_carecode().execute(select_error_code,select_high_code,select_low_code);
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

    private class getfield_trouble_carecode extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_carecode("1","01",strings[0],strings[1],strings[2]);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_care_name());
                    }
                    filed_error_map.put("trouble_care_cd","X001");
                    field_trouble_care_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_care_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_care_name = spinner_list.get(position);
                            String select_care_code = "";

                            for(int i = 0 ; i < list.size(); i++){
                                if(list.get(i).getTrouble_care_name() == select_care_name){
                                    select_care_code = list.get(i).getTrouble_care_cd();
                                    filed_error_map.put("trouble_care_cd",select_care_code);
                                    break;
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

    private class insert_filed_error_test extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            progressDialog.dismiss();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.insert_filed_error_test(filed_error_map);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
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
                                        fragment = new Fragment_d_1();
                                        title = "장애등록 (버스)";
                                    }else{
                                        fragment = new Fragment_d_0();
                                        title = "장애처리";
                                    }
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.frage_change,fragment);
                                    ft.commit();

                                    if (((MainActivity)getActivity()).getSupportActionBar() != null) {
                                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(title);
                                    }
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

                }
            });
            return null;
        }
    }

    @Override
    public void onBack() {
        Log.e("Other", "onBack()");
        // 리스너를 설정하기 위해 Activity 를 받아옵니다.
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        activity.setOnBackPressedListener(null);
        // MainFragment 로 교체
        Fragment_d fragment_d = new Fragment_d();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frage_change, fragment_d).commit();
        // Activity 에서도 뭔가 처리하고 싶은 내용이 있다면 하단 문장처럼 호출해주면 됩니다.
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("장애등록");
        // activity.onBackPressed();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Log.e("Other", "onAttach()");
        ((MainActivity)activity).setOnBackPressedListener(this);
    }

    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
