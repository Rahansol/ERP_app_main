package app.erp.com.erp_app.callcenter;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.dialog.DialogEduEmpList;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
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

public class Fragment_trouble_insert_edu extends Fragment {


    Button edu_insert , edit_edu_emp_list;
    Context context;

    private Retrofit retrofit;
    private DialogEduEmpList mdialog;

    String click_type, page_info ,service_id, infra_code, unit_code, trouble_high_code, trouble_low_code, start_hour, start_min,trans_id;
    List<String> scan_unit_barcodes;
    EditText find_bus_num, nms_garage_id, nms_notice;
    TextView reserve_area_name , reserve_unit_barcode, nms_dep_name, start_day, care_emp_count;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    CheckBox bs_yn;
    HashMap<String, Object> filed_error_map;

    Spinner nms_group , nms_unit_code ,nms_trouble_high_code , nms_trouble_low_code, nms_care_code, nms_start_hour, nms_start_min, eud_unit_code;

    ProgressDialog progressDialog;

    LinearLayout hour_layout ,min_layout,office_layout,edu_layout, edu_unit_layout, edu_trouble_layout;

    List<Edu_Emp_Vo> edu_list;
    List<Edu_Emp_Vo> gblist = new ArrayList<>();
    List<Edu_Emp_Vo> gnlist = new ArrayList<>();
    List<Edu_Emp_Vo> iclist = new ArrayList<>();
    List<Edu_Emp_Vo> pclist = new ArrayList<>();
    List<String> care_emp_list ;
    public Fragment_trouble_insert_edu(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_insert_edu, container ,false);
        context = getActivity();

        care_emp_list = new ArrayList<>();

        // shared 에서 user_info 가져옴
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        // dialog 호출
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        String emp_id = pref.getString("emp_id",null);
        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Edu_Emp_Vo>> call_emp = erp.Edu_care_emp_list(emp_id);
        new Fragment_trouble_insert_edu.Edu_Care_Emp_List().execute(call_emp);

        // 화면의 달력 날짜 등록 , 달력 활성화
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String get_today = sdf.format(date);

        start_day = (TextView)view.findViewById(R.id.start_day);
        start_day.setText(get_today);
        start_day.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));

        // 장애 등록시 필요한 spiner 등록
        nms_group = (Spinner)view.findViewById(R.id.nms_group);
        nms_unit_code = (Spinner)view.findViewById(R.id.nms_unit_code);
        nms_trouble_high_code = (Spinner)view.findViewById(R.id.nms_trouble_high_code);
        nms_trouble_low_code = (Spinner)view.findViewById(R.id.nms_trouble_low_code);
        nms_care_code = (Spinner)view.findViewById(R.id.nms_care_code);
        nms_start_hour = (Spinner)view.findViewById(R.id.nms_start_hour);
        nms_start_min = (Spinner)view.findViewById(R.id.nms_start_min);

        eud_unit_code = (Spinner)view.findViewById(R.id.eud_unit_code);

        //edti Text
        nms_garage_id = (EditText)view.findViewById(R.id.nms_garage_id);
        nms_notice = (EditText)view.findViewById(R.id.nms_notice);
        care_emp_count = (TextView)view.findViewById(R.id.care_emp_count);

        //감싸고있는 레이아웃
        hour_layout = (LinearLayout)view.findViewById(R.id.hour_layout);
        min_layout = (LinearLayout)view.findViewById(R.id.min_layout);
        office_layout = (LinearLayout)view.findViewById(R.id.office_layout);
        edu_layout = (LinearLayout)view.findViewById(R.id.edu_layout);
        edu_unit_layout = (LinearLayout)view.findViewById(R.id.edu_unit_layout);
        edu_trouble_layout = (LinearLayout)view.findViewById(R.id.edu_trouble_layout);

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
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hour_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                        start_day.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                    }
                });;

            }
        });

        nms_start_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start_hour = parent.getItemAtPosition(position).toString();
                hour_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                min_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nms_start_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String serch_type = parent.getItemAtPosition(position).toString();
                start_min = serch_type;
                min_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                office_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eud_unit_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    edu_unit_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                }
                edu_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                String serch_type = adapterView.getItemAtPosition(i).toString();
                switch (serch_type){
                    case "버스" :
                        new Getfield_trouble_error_type().execute(serch_type);
                        break;
                    case "집계" :
                        new Getfield_trouble_error_type().execute(serch_type);
                        break;
                    case "충전기" :
                        new Getfield_trouble_error_type().execute(serch_type);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nms_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                office_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                edu_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                String trans_name = adapterView.getItemAtPosition(i).toString();
                switch (trans_name){
                    case "경기시내" :
                        trans_id = "4149998";
                        break;
                    case "경기마을" :
                        trans_id = "4119998";
                        break;
                    case "경기시외" :
                        trans_id = "4159998";
                        break;
                    case "인천시내" :
                        trans_id = "2809998";
                        break;
                    default:
                        trans_id = "not_select";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edit_edu_emp_list = (Button)view.findViewById(R.id.edit_edu_emp_list);
        edit_edu_emp_list.setOnClickListener(new View.OnClickListener() {
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
                        int check_count = (int)map.get("check_count");
                        care_emp_count.setText("현재 대상자 " + check_count +"명");
                        care_emp_list = (List<String>) map.get("care_emp_id");
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

        filed_error_map = new HashMap<>();
        edu_insert = (Button)view.findViewById(R.id.edu_insert);
        edu_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emp_id = pref.getString("emp_id",null);
                filed_error_map.put("reg_emp_id",emp_id);
                filed_error_map.put("transp_id",trans_id);
                filed_error_map.put("service_id",service_id);
                filed_error_map.put("notice",nms_notice.getText().toString());

                filed_error_map.put("reg_date",start_day.getText().toString());
                start_min = start_min.replaceAll("분","");
                start_hour = start_hour.replaceAll("시","");

                if(start_min.equals("-  -") || start_hour.equals("- 간 -")){
                    Toast.makeText(context,"시간 , 분을 선택해주세요. " , Toast.LENGTH_SHORT).show();
                    return;
                }else if ("not_select".equals(trans_id)){
                    Toast.makeText(context,"조합을 선택해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }else if (service_id == null){
                    Toast.makeText(context,"교육주제 선택해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }else if(care_emp_list.size() ==0){
                    Toast.makeText(context,"교육자를 선택해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }else if (filed_error_map.get("unit_cd") == null){
                    Toast.makeText(context,"교육장비를 선택해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }else if (filed_error_map.get("trouble_high_cd") == null){
                    Toast.makeText(context,"교육대분류를 선택해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }else{
//                    Log.d("d",filed_error_map.toString());
                    progressDialog.setMessage("등록중...");
                    progressDialog.show();
                    filed_error_map.put("reg_time",start_hour+start_min);
                    new insert_edu_history().execute();
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
            switch (serch_type){
                case "버스" :
                    infra_code = "98";
                    service_id = "01";
                    break;
                case "집계" :
                    infra_code = "98";
                    service_id = "02";
                    break;
                case "충전기":
                    infra_code = "98";
                    service_id = "04";
                    break;
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
                                edu_unit_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                edu_trouble_layout.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                unit_code = list.get(position-1).getUnit_code();
                                filed_error_map.put("unit_cd",unit_code);
                                new Getfield_trouble_high_code().execute(unit_code);
                            }else{
                                filed_error_map.put("unit_cd",null);
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
                            if(position > 0){
                                edu_trouble_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                trouble_high_code = list.get(position-1).getTrouble_high_cd();
                                filed_error_map.put("trouble_high_cd",trouble_high_code);
                            }else{
                                filed_error_map.put("trouble_high_cd",null);
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

    private class insert_edu_history extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.insert_edu_history(care_emp_list,filed_error_map);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    boolean result = response.body();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    page_info = "list";
                    a_builder.setTitle("교육 등록");
                    a_builder.setCancelable(false);
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    String title = "";
                                    if(page_info.equals("repg")){
                                        fragment = new Fragment_trouble_insert();
                                        title = "장애등록 (버스)";
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
