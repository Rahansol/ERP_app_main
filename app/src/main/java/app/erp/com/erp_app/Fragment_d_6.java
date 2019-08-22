package app.erp.com.erp_app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

public class Fragment_d_6 extends Fragment implements MainActivity.OnBackPressedListener{

    Button bus_num_find , bus_num_barcode_find, update_history;
    Context context;

    private Retrofit retrofit;

    String click_type ,bus_barcode, area_code, emp_id, page_info;
    TextView insert_start_day , insert_start_time, insert_reg_emp_id, insert_unit_code , insert_bus_num, insert_phone_num, insert_area_code,
            insert_office_code, insert_garage, insert_route_code, insert_ars_unit_code, insert_ars_trouble_high_code,insert_ars_trouble_low_code;

    SharedPreferences pref, barcode_type_pref;
    SharedPreferences.Editor editor;

    Spinner insert_process_unit_code , insert_process_trouble_high_code, insert_process_trouble_low_code, insert_process_trouble_care_code ;

    String service_id , infra_id , unit_id , trouble_high_id, trouble_low_id , trouble_care_id;

    EditText insert_care_unit_before , insert_care_unit_after , insert_care_notice;

    int high_intdex, low_index = 0 ;

    HashMap<String, Object> update_trouble_history_map;

    public Fragment_d_6(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d_6, container ,false);
        context = getActivity();

        update_trouble_history_map = new HashMap<>();

        Bundle bundle = getArguments();
        Trouble_HistoryListVO thlvo = (Trouble_HistoryListVO) bundle.getSerializable("Obj");

        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        emp_id = pref.getString("emp_id",null);

        //textView
        insert_start_day = (TextView)view.findViewById(R.id.insert_start_day);
        insert_start_time = (TextView)view.findViewById(R.id.insert_start_time);
        insert_reg_emp_id = (TextView)view.findViewById(R.id.insert_reg_emp_id);
        insert_unit_code = (TextView)view.findViewById(R.id.insert_unit_code);
        insert_bus_num = (TextView)view.findViewById(R.id.insert_bus_num);
        insert_phone_num = (TextView)view.findViewById(R.id.insert_phone_num);
        insert_area_code = (TextView)view.findViewById(R.id.insert_area_code);
        insert_office_code = (TextView)view.findViewById(R.id.insert_office_code);
        insert_garage = (TextView)view.findViewById(R.id.insert_garage);
        insert_route_code = (TextView)view.findViewById(R.id.insert_route_code);
        insert_ars_unit_code = (TextView)view.findViewById(R.id.insert_ars_unit_code);
        insert_ars_trouble_high_code = (TextView)view.findViewById(R.id.insert_ars_trouble_high_code);
        insert_ars_trouble_low_code = (TextView)view.findViewById(R.id.insert_ars_trouble_low_code);

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
        update_history = (Button)view.findViewById(R.id.update_history);
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
                    update_trouble_history_map.put("unit_before_id",care_before);
                    update_trouble_history_map.put("unit_after_id",care_after);
                }

                update_trouble_history_map.put("care_date",getdate);
                update_trouble_history_map.put("care_time",gettime);
                update_trouble_history_map.put("care_emp_id",emp_id);
                update_trouble_history_map.put("notice",insert_care_notice.getText().toString());

                new trouble_history_care_update().execute();

            }
        });

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

        update_trouble_history_map.put("reg_date", list.get(0).getReg_date());
        update_trouble_history_map.put("reg_time",list.get(0).getReg_time());
        update_trouble_history_map.put("unit_code_before",list.get(0).getUnit_code());
        update_trouble_history_map.put("trouble_high_cd_before",list.get(0).getTrouble_high_cd());
        update_trouble_history_map.put("trouble_low_cd_before",list.get(0).getTrouble_low_cd());
        update_trouble_history_map.put("transp_bizr_id",list.get(0).getTransp_bizr_id());
        update_trouble_history_map.put("garage_id",list.get(0).getGarage_id());
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
                    Log.d("d", " test " + high_intdex);
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
                    Log.d("D:", "pos <:" + low_index);
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
                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getTrouble_care_name());
                    }
                    insert_process_trouble_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    insert_process_trouble_care_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            update_trouble_history_map.put("trouble_care_cd",list.get(position).getTrouble_care_cd());
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
            Call<Boolean> call = erp.update_trouble_history(update_trouble_history_map);
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
        Fragment_d_0 fragment_d = new Fragment_d_0();
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
