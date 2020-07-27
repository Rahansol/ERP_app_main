package app.erp.com.erp_app.document_care;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.ProJectItem_Adapter;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.dialog.DialogPrj_ItemView;
import app.erp.com.erp_app.dialog.Dialog_Year_Month;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.over_work.Over_Work_Approval_Activity;
import app.erp.com.erp_app.vo.ProJectSelectBoxVO;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.work.Work_Insert_Activity;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment_ProJect_Work_List extends Fragment {
    Context mcontext;
    Map<String ,  Object> requestMap ;
    ERP_Spring_Controller erp;
    ProJectItem_Adapter pjia;
    ListView prj_item_listview;
    DialogPrj_ItemView mdialog;
    ProJectVO pvo;
    String bus_num, busoff_name, pref_string;

    TimePickerDialog start_timePickerDialog;
    TimePickerDialog end_timePickerDialog;

    String trans_id ;
    String reg_emp_id;
    String serch_bus_num ,save_path;
    String st_date, prj_code;
    String base_infra_code ,area_code ,sub_area_code,prj_seq;

    EditText find_bus_num;

    Spinner prj_office_list ,prj_reg_emp_spinner;

    private HashMap<String,Object> image_path_map = new HashMap<>();

    private String select_reg_date , select_trans_id, select_bus_id;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_project_work_list, container ,false);
        mcontext = getActivity();
        pref_string = "";

        final TextView st_date_view = (TextView)view.findViewById(R.id.st_date);
        final Calendar cal = Calendar.getInstance();
        view.findViewById(R.id.st_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        Log.d("YearMonthPickerTest", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
                        Log.d("YearMonthPickerTest", String.format("% d-%02d", year, monthOfYear));
                        st_date_view.setText(String.format("% d-%02d", year, monthOfYear));
                    }
                };

                Dialog_Year_Month dym = new Dialog_Year_Month();
                dym.setListener(d);
                dym.show(getFragmentManager(), "YearMonthPickerTest");
            }
        });

        requestMap = new HashMap<>();
        Bundle bundle = getArguments();
        pvo = (ProJectVO) bundle.getSerializable("Obj");

        prj_item_listview = (ListView)view.findViewById(R.id.prj_item_listview);
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        prj_office_list = (Spinner)view.findViewById(R.id.prj_office_list);
        prj_reg_emp_spinner = (Spinner)view.findViewById(R.id.prj_reg_emp_spinner);

        pjia = new ProJectItem_Adapter();
        pjia.setPrj_open_item(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProJectVO select_item = (ProJectVO) pjia.getItem((Integer) view.getTag());

                bus_num = select_item.getBus_num();
                busoff_name = select_item.getBusoff_name();

                select_reg_date = select_item.getReg_date();
                select_trans_id = select_item.getTransp_bizr_id();
                select_bus_id = select_item.getBus_id();

                String area_code = pvo.getArea_code();
                String sub_area_code = pvo.getSub_area_code();
                String prj_seq = pvo.getPrj_seq();

                String trans_id = select_item.getTransp_bizr_id();
                String reg_date = select_item.getReg_date();
                String bus_id = select_item.getBus_id();

                Call<List<ProJectVO>> call = erp.app_prj_item_data(area_code, sub_area_code,prj_seq,trans_id,bus_id,reg_date);
                new Fragment_ProJect_Work_List.app_prj_item_data().execute(call);
            }
        });

        //프로젝트 이름 set
        TextView prj_name_text = (TextView)view.findViewById(R.id.prj_name_text);
        prj_name_text.setText(pvo.getPrj_name());

        base_infra_code = pvo.getBase_infra_code();
        area_code = pvo.getArea_code();
        sub_area_code = pvo.getSub_area_code();
        prj_seq = pvo.getPrj_seq();

        trans_id = "운수사";
        reg_emp_id = "작업자";
        serch_bus_num = "";
        st_date = "";
        find_bus_num = (EditText)view.findViewById(R.id.prj_find_bus_num);

        Button prj_item_serch_btn = (Button)view.findViewById(R.id.prj_item_serch_btn);
        prj_item_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(st_date_view.getText().length() == 1){
                    st_date = "";
                }else{
                    st_date = st_date_view.getText().toString().replaceAll(" ","");
                }
                serch_bus_num = find_bus_num.getText().toString();
                Call<List<ProJectVO>> call = erp.app_project_detail_info(base_infra_code, area_code , sub_area_code, prj_seq,trans_id,reg_emp_id,serch_bus_num,st_date);
                new Fragment_ProJect_Work_List.app_project_detail_info().execute(call);
            }
        });

        String prj_id = "PRJ_"+ base_infra_code + "_"+ area_code+sub_area_code+prj_seq;
        Call<Object> call2 = erp.app_prj_detail_serch_val(prj_id);
        new Fragment_ProJect_Work_List.app_prj_detail_serch_val().execute(call2);

        return view;
    }

    private class app_project_detail_info extends AsyncTask<Call , Void , List<ProJectVO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("등록된 업무 리스트 조회중...");
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
        protected void onPostExecute(List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(null != proJectVOS){
                if(proJectVOS.size() > 0){
                    pjia.clearItem();
                    prj_item_listview.setAdapter(pjia);
                    for(int i=0; i<proJectVOS.size(); i++){
                        proJectVOS.get(i).setBase_infra_code(base_infra_code);
                        proJectVOS.get(i).setArea_code(area_code);
                        proJectVOS.get(i).setSub_area_code(sub_area_code);
                        proJectVOS.get(i).setPrj_seq(prj_seq);
                        pjia.addItem(proJectVOS.get(i));
                    }
                    prj_item_listview.setAdapter(pjia);
                }else{
                    pjia.clearItem();
                    prj_item_listview.setAdapter(pjia);
                    Toast.makeText(mcontext,"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mcontext,"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class app_prj_item_data extends AsyncTask<Call, Void, List<ProJectVO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("업무 조회중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try {
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(null != proJectVOS){

                String pref_string = select_reg_date+ "_" + select_trans_id + "_" +select_bus_id;

                mdialog = new DialogPrj_ItemView(mcontext, proJectVOS, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdialog.dismiss();
                    }
                },pvo,bus_num,busoff_name,pref_string,select_trans_id,select_bus_id);

                mdialog.setCancelable(false);
                mdialog.show();

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Window window = mdialog.getWindow();

                int x = (int)(size.x * 0.9f);
                int y = (int)(size.y * 0.8f);

                window.setLayout(x,y);
            }
        }
    }

    private class app_prj_detail_serch_val extends AsyncTask<Call , Void, Object>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("검색 항목 가져오는중...");
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
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(null != o){
                String res = o.toString();
                List<ProJectSelectBoxVO> office_list = new ArrayList<>();
                List<ProJectSelectBoxVO> emp_list = new ArrayList<>();
                try{
                    JSONObject test = new JSONObject(res);
                    JSONArray jsonArray = (JSONArray) test.get("trans_data");
                    JSONArray jsonArray2 = (JSONArray) test.get("reg_data");

                    for(int i =0; i <jsonArray.length(); i++){
                        JSONObject obj2 = (JSONObject) jsonArray.get(i);
                        ProJectSelectBoxVO put_list = new ProJectSelectBoxVO();
                        String ccc = (String) obj2.get("busoff_name");
                        String ttt = obj2.get("transp_bizr_id").toString();
                        put_list.setBusoff_name(ccc);
                        put_list.setTransp_bizr_id(ttt);
                        office_list.add(put_list);
                    }

                    for(int i =0; i <jsonArray2.length(); i++){
                        JSONObject obj2 = (JSONObject) jsonArray2.get(i);
                        ProJectSelectBoxVO put_list = new ProJectSelectBoxVO();
                        String ccc = (String) obj2.get("emp_name");
                        String ttt = obj2.get("reg_emp_id").toString();
                        put_list.setEmp_name(ccc);
                        put_list.setReg_emp_id(ttt);
                        emp_list.add(put_list);
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
                make_spinner( office_list , emp_list);
            }
        }
    }
    private void make_spinner(final List<ProJectSelectBoxVO> office_list , final List<ProJectSelectBoxVO> emp_list){
        List<String> spinner_list = new ArrayList<>();
        spinner_list.add("운수사");
        for(int i=0; i<office_list.size(); i++){
            spinner_list.add(office_list.get(i).getBusoff_name());
        }
        prj_office_list.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,spinner_list));
        prj_office_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    trans_id = office_list.get(i-1).getTransp_bizr_id();
                }else{
                    trans_id = "운수사";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> spinner_list2 = new ArrayList<>();
        spinner_list2.add("작업자");
        for(int i=0; i<emp_list.size(); i++){
            spinner_list2.add(emp_list.get(i).getEmp_name());
        }
        prj_reg_emp_spinner.setAdapter(new ArrayAdapter<String>(mcontext , android.R.layout.simple_spinner_dropdown_item,spinner_list2));
        prj_reg_emp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    reg_emp_id = emp_list.get(i-1).getReg_emp_id();
                }else{
                    reg_emp_id = "작업자";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
