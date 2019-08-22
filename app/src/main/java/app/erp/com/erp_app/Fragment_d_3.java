package app.erp.com.erp_app;

import android.app.DatePickerDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_d_3 extends Fragment implements MainActivity.OnBackPressedListener{

    Button bus_num_find , bus_num_barcode_find, submit_barcode;
    Context context;

    private Retrofit retrofit;

    String click_type ,service_id, infra_code, unit_code, trouble_high_code, trouble_low_code, page_info, charger_hour , charger_min;
    List<String> scan_unit_barcodes;
    EditText find_bus_num, charger_notice, charger_cms_code, charger_order_tel;
    TextView reserve_area_name , reserve_unit_barcode, charger_dep_name, start_day;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    CheckBox bs_yn;

    HashMap<String, Object> filed_error_map;

    Spinner charger_infra_type , charger_unit_code ,charger_trouble_high_code , charger_trouble_low_code, charger_care_code, charger_start_hour , charger_start_min;

    RadioGroup today_group;
    RadioButton today_y , today_n;

    ProgressDialog progressDialog;

    public Fragment_d_3(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d_3, container ,false);
        context = getActivity();



        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String dep_name = pref.getString("dep_name",null);

        charger_dep_name = (TextView)view.findViewById(R.id.charger_dep_name);
        charger_dep_name.setText(dep_name);

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        filed_error_map = new HashMap<>();

        // 화면 일자 선택부분 달력 활성화 날짜 입력
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String get_today = sdf.format(date);

        start_day = (TextView)view.findViewById(R.id.start_day);
        start_day.setText(get_today);

        final Calendar cal = Calendar.getInstance();
        view.findViewById(R.id.start_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        start_day.setText(msg);

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        // editText
        charger_notice = (EditText)view.findViewById(R.id.charger_notice);
        charger_cms_code = (EditText)view.findViewById(R.id.charger_cms_code);
        charger_order_tel = (EditText)view.findViewById(R.id.charger_order_tel);

        // spinner
        charger_infra_type = (Spinner)view.findViewById(R.id.charger_infra_type);
        charger_unit_code = (Spinner)view.findViewById(R.id.charger_unit_code);
        charger_trouble_high_code = (Spinner)view.findViewById(R.id.charger_trouble_high_code);
        charger_trouble_low_code = (Spinner)view.findViewById(R.id.charger_trouble_low_code);
        charger_care_code = (Spinner)view.findViewById(R.id.charger_care_code);
        charger_start_hour = (Spinner)view.findViewById(R.id.charger_start_hour);
        charger_start_min = (Spinner)view.findViewById(R.id.charger_start_min);

        charger_start_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String hour = parent.getItemAtPosition(position).toString();
                charger_hour = hour;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        charger_start_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String min = parent.getItemAtPosition(position).toString();
                charger_min = min;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        charger_infra_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String serch_type = parent.getItemAtPosition(position).toString();
                if(!serch_type.equals("검색조건")){new Getfield_trouble_error_type().execute(serch_type);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit_barcode = (Button)view.findViewById(R.id.submit_barcode);
        submit_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emp_id = pref.getString("emp_id",null);
                String dep_code = pref.getString("dep_code",null);

                filed_error_map.put("bus_id","000002010");
                filed_error_map.put("transp_bizr_id","9990004");

                filed_error_map.put("emp_id",emp_id);
                filed_error_map.put("dep_code",dep_code);
                filed_error_map.put("infra_code",infra_code);
                filed_error_map.put("service_id",service_id);
                filed_error_map.put("route_id","");
                filed_error_map.put("driver_tel_num",charger_order_tel.getText().toString());
                filed_error_map.put("notice",charger_notice.getText().toString());
                filed_error_map.put("job_viewer",emp_id);
                filed_error_map.put("reg_emp_id",emp_id);
                filed_error_map.put("unit_before_id","");

                if(charger_cms_code.getText().toString().equals("")){
                    filed_error_map.put("unit_change_yn","N");
                    filed_error_map.put("unit_after_id","");
                    filed_error_map.put("garage_id",charger_cms_code.getText().toString());
                }else{
                    filed_error_map.put("unit_change_yn","Y");
                    filed_error_map.put("unit_after_id",charger_cms_code.getText().toString());
                    filed_error_map.put("garage_id",charger_cms_code.getText().toString());
                }

                // 이부분 화면에서 입력할때 무조건 입력하게끔으로 바꿔야함

                filed_error_map.put("move_distance","");
                filed_error_map.put("move_time","");
                filed_error_map.put("wait_time","");
                filed_error_map.put("work_time","");

                filed_error_map.put("restore_yn","N");
                filed_error_map.put("mintong","N");
                filed_error_map.put("analysis_yn","N");

                filed_error_map.put("reg_date",start_day.getText().toString());

                charger_hour = charger_hour.replaceAll("시","");
                charger_min = charger_min.replaceAll("분","");

                if(charger_min.equals("-  -") || charger_hour.equals("- 간 -")){
                    Toast.makeText(context,"시간 , 분을 선택해주세요 . " , Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    filed_error_map.put("reg_time",charger_hour+charger_min);
                    new insert_filed_error_test().execute();
                }
            }
        });


        return view;
    }

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
                service_id = "04";
                infra_code = "1";
            }else if(serch_type.equals("마이비")){
                service_id = "04";
                infra_code = "2";
            }
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type(service_id,infra_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
                    charger_unit_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    charger_unit_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            unit_code = list.get(position).getUnit_code();
                            filed_error_map.put("unit_code",unit_code);
                            new Getfield_trouble_high_code().execute(unit_code);
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
                    for(Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_high_name());
                    }
                    charger_trouble_high_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    charger_trouble_high_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            trouble_high_code = list.get(position).getTrouble_high_cd();
                            filed_error_map.put("trouble_high_cd",trouble_high_code);
                            new Getfield_trouble_low_code().execute();

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
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_low_name());
                    }
                    charger_trouble_low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    charger_trouble_low_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            trouble_low_code = list.get(position).getTrouble_low_cd();
                            filed_error_map.put("trouble_low_cd",trouble_low_code);
                            new Getfield_trouble_carecode().execute();
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
                    // 데이터 통신하여 가져온 조치내역이 담긴 list
                    final List<Trouble_CodeVo> list = response.body();
                    // 스피너에 표출시킬 String list
                    final List<String> spinner_list = new ArrayList<>();
                    // 미등록 위한 String list 에 값 추가
                    spinner_list.add("내역 미등록");
                    // 데이터 통신하여 가져온 list 를 for문으로 돌려 조치내역 이름만 String list 에 추가
                    for(Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_care_name());
                    }
                    // 조차내역 스피너 어뎁터 등록
                    charger_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    // 조치내역 스피너 select Listener 등록
                    charger_care_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // 스피너에서 선택한 값 ( position 이 인덱스 값) 을 이용하여 스피너 등록을 위한 String list 에서 선택한 조치이름 가져옴
                            String care_type = spinner_list.get(position);
                            // 만약 선택한 조치내역 이름이 미등록이라면 조치 코드를 X001 로 등록
                            if(care_type.equals("내역 미등록")){
                                filed_error_map.put("trouble_care_cd","X001");
                                filed_error_map.put("direct_care","N");
                                filed_error_map.put("bs_yn","N");
                            }else{
                                // 미등록이 아닌 조치내역 선택시 데이터 통신에서 가져온 리스트에서 코드를 가져온다 여기서 index는 postion-1 로 해야함
                                filed_error_map.put("trouble_care_cd",list.get(position-1).getTrouble_care_cd());
                                filed_error_map.put("direct_care","Y");
                                filed_error_map.put("bs_yn","Y");
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("scan")){
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


}
