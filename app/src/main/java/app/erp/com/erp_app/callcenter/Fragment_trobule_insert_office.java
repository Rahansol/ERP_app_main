package app.erp.com.erp_app.callcenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_trobule_insert_office extends Fragment {

    Button bus_num_find , bus_num_barcode_find, submit_barcode;
    Context context;

    private Retrofit retrofit;

    String click_type ,service_id, infra_code, unit_code, trouble_high_code, trouble_low_code, nomal_hour,nomal_min, page_info;
    List<String> scan_unit_barcodes;
    EditText find_bus_num, nomal_notice;
    TextView reserve_area_name , reserve_unit_barcode, nomal_dep_name, start_day;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    CheckBox bs_yn;

    ProgressDialog progressDialog;

    HashMap<String, Object> filed_error_map;

    Spinner  nomal_unit_code ,nomal_trouble_high_code , nomal_trouble_low_code, nomal_care_code , nomal_start_hour, nomal_start_min;

    LinearLayout hour_layout , min_layout , care_code_layout , office_care_layout;

    public Fragment_trobule_insert_office(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trobule_insert_office, container ,false);
        context = getActivity();

        filed_error_map = new HashMap<>();

//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String get_today = sdf.format(date);

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String dep_name = pref.getString("dep_name",null);

        nomal_dep_name = (TextView)view.findViewById(R.id.nomal_dep_name);
        nomal_dep_name.setText(dep_name);

//        start_day = (TextView)view.findViewById(R.id.start_day);
//        start_day.setText(get_today);

//        hour_layout = (LinearLayout)view.findViewById(R.id.hour_layout);
//        min_layout = (LinearLayout)view.findViewById(R.id.min_layout);

        care_code_layout = (LinearLayout)view.findViewById(R.id.care_code_layout);
        //조치 레이아웃
        office_care_layout = (LinearLayout)view.findViewById(R.id.office_care_layout);
        office_care_layout.setVisibility(View.GONE);

//        hour_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

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

        new Getfield_trouble_error_type().execute();

        //edit Text
        nomal_notice = (EditText)view.findViewById(R.id.nomal_notice);
        //spinner
        nomal_unit_code = (Spinner)view.findViewById(R.id.nomal_unit_code);
        nomal_trouble_high_code = (Spinner)view.findViewById(R.id.nomal_trouble_high_code);
        nomal_trouble_low_code = (Spinner)view.findViewById(R.id.nomal_trouble_low_code);
        nomal_care_code = (Spinner)view.findViewById(R.id.nomal_care_code);

//        nomal_start_hour = (Spinner)view.findViewById(R.id.nomal_start_hour);
//        nomal_start_min = (Spinner)view.findViewById(R.id.nomal_start_min);

//        nomal_start_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String hour = parent.getItemAtPosition(position).toString();
//                nomal_hour = hour;
//                if(!nomal_hour.equals("- 시간 -")){
//                    hour_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
//                    min_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        nomal_start_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String min = parent.getItemAtPosition(position).toString();
//                nomal_min = min;
//                if(!nomal_min.equals("- 분 -")){
//                    min_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
//                    care_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        care_code_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

        submit_barcode = (Button)view.findViewById(R.id.submit_barcode);
        submit_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emp_id = pref.getString("emp_id",null);
                String dep_code = pref.getString("dep_code",null);

                filed_error_map.put("bus_id","000004010");
                filed_error_map.put("transp_bizr_id","8880001");

                filed_error_map.put("emp_id",emp_id);
                filed_error_map.put("dep_code",dep_code);
                filed_error_map.put("infra_code",infra_code);
                filed_error_map.put("service_id",service_id);
                filed_error_map.put("garage_id","");
                filed_error_map.put("route_id","");
                filed_error_map.put("driver_tel_num","");
                filed_error_map.put("notice",nomal_notice.getText().toString());
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
                filed_error_map.put("mintong","N");
                filed_error_map.put("analysis_yn","N");

                filed_error_map.put("direct_care","Y");
                filed_error_map.put("bs_yn","N");

//                filed_error_map.put("reg_date",start_day.getText().toString());

//                nomal_hour = nomal_hour.replaceAll("시","");
//                nomal_min = nomal_min.replaceAll("분","");

//                if(nomal_min.equals("-  -") || nomal_hour.equals("- 간 -")){
//                    Toast.makeText(context,"시간 , 분을 선택해주세요 . " , Toast.LENGTH_SHORT).show();
//                    return;
//                }

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
                String getdate = sdf.format(date);
                String gettime = sdf2.format(date);

                filed_error_map.put("reg_time",gettime);
                filed_error_map.put("reg_date",getdate);

                progressDialog.setMessage("등록중...");
                progressDialog.show();

//                filed_error_map.put("reg_time",nomal_hour+nomal_min);
                new insert_filed_error_test().execute();
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
            service_id = "09";
            infra_code = "99";
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type(service_id,infra_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
                    nomal_unit_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nomal_unit_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    nomal_trouble_high_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nomal_trouble_high_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    nomal_trouble_low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nomal_trouble_low_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            filed_error_map.put("trouble_care_cd","X001");
                            trouble_low_code = list.get(position).getTrouble_low_cd();
                            filed_error_map.put("trouble_low_cd",trouble_low_code);
//                            new Getfield_trouble_carecode().execute();
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
                    spinner_list.add("조치 등록");
                    for(Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_care_name());
                    }
                    nomal_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    nomal_care_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String sercy_type = spinner_list.get(position);
                            if(sercy_type.equals("조치 등록")){
                                filed_error_map.put("trouble_care_cd",null);
                            }else{
                                filed_error_map.put("trouble_care_cd",list.get(position-1).getTrouble_care_cd());

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

    // 등록하기
    private class insert_filed_error_test extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.insert_filed_error_test(filed_error_map);
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
                                        title = "장애등록";
                                    }else{
                                        fragment = new Fragment_trouble_insert();
                                        title = "장애처리";
                                    }
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.frage_change,fragment);
                                    ft.commit();

//                                    if (((MainActivity)getActivity()).getSupportActionBar() != null) {
//                                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(title);
//                                    }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("scan")){
        }
    }
}
