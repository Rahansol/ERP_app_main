package app.erp.com.erp_app;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Fragment_d_4 extends Fragment implements MainActivity.OnBackPressedListener{

    Button bus_num_find , bus_num_barcode_find, submit_barcode;
    Context context;

    private Retrofit retrofit;

    String click_type ,service_id, infra_code, unit_code, trouble_high_code, trouble_low_code;
    List<String> scan_unit_barcodes;
    EditText find_bus_num, bit_notice;
    TextView reserve_area_name , reserve_unit_barcode, bit_dep_name, start_day;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    CheckBox bs_yn;

    HashMap<String, Object> filed_error_map;

    Spinner  bit_unit_code ,bit_trouble_high_code , bit_trouble_low_code, bit_care_code;

    RadioGroup today_group;
    RadioButton today_y , today_n;

    public Fragment_d_4(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d_4, container ,false);
        context = getActivity();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String get_today = sdf.format(date);

        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String dep_name = pref.getString("dep_name",null);

        bit_dep_name = (TextView)view.findViewById(R.id.bit_dep_name);
        bit_dep_name.setText(dep_name);

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

        new Getfield_trouble_error_type().execute();
        bit_unit_code = (Spinner)view.findViewById(R.id.bit_unit_code);
        bit_trouble_high_code = (Spinner)view.findViewById(R.id.bit_trouble_high_code);
        bit_trouble_low_code = (Spinner)view.findViewById(R.id.bit_trouble_low_code);
        bit_care_code = (Spinner)view.findViewById(R.id.bit_care_code);

        bit_notice = (EditText)view.findViewById(R.id.bit_notice);


        submit_barcode = (Button)view.findViewById(R.id.submit_barcode);
        submit_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emp_id = pref.getString("emp_id",null);
                String dep_code = pref.getString("dep_code",null);

                filed_error_map.put("bus_id","000002010");
                filed_error_map.put("transp_bizr_id","9990004");

                filed_error_map.put("emp_id",emp_id);
                filed_error_map.put("dep_code",dep_code);
                filed_error_map.put("infra_code",infra_code);
                filed_error_map.put("service_id",service_id);
                filed_error_map.put("garage_id","");
                filed_error_map.put("route_id","");
                filed_error_map.put("driver_tel_num","");
                filed_error_map.put("notice",bit_notice.getText().toString());
                filed_error_map.put("job_viewer",emp_id);
                filed_error_map.put("reg_emp_id",emp_id);
                filed_error_map.put("unit_before_id","");
                filed_error_map.put("unit_after_id","");

                // 이부분 화면에서 입력할때 무조건 입력하게끔으로 바꿔야함
                filed_error_map.put("unit_change_yn","N");
                filed_error_map.put("move_distance","");
                filed_error_map.put("move_time","");
                filed_error_map.put("wait_time","");
                filed_error_map.put("work_time","");

                filed_error_map.put("restore_yn","N");
                filed_error_map.put("bs_yn","Y");
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
                filed_error_map.put("direct_care","N");

                Log.d("t:" , ":::" + filed_error_map.toString());
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
            service_id = "13";
            infra_code = "3";
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type(service_id,infra_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
                    bit_unit_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bit_unit_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    bit_trouble_high_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bit_trouble_high_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    bit_trouble_low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bit_trouble_low_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            trouble_low_code = list.get(position).getTrouble_low_cd();
                            filed_error_map.put("trouble_low_cd",trouble_high_code);
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
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("내역 미등록");
                    for(Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_care_name());
                    }
                    bit_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bit_care_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            String sercy_type = spinner_list.get(position);
                            if(sercy_type.equals("내역 미등록")){
                                filed_error_map.put("trouble_care_cd","X001");
                            }else{
                                filed_error_map.put("trouble_care_cd",list.get(position).getTrouble_care_cd());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

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
