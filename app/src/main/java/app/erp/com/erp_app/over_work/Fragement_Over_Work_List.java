package app.erp.com.erp_app.over_work;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Over_Work_Process_List_Adapter;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import retrofit2.Call;
import retrofit2.Response;

//연장근무 리스트 신청
public class Fragement_Over_Work_List extends Fragment {
    private Context mcontext;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SharedPreferences pref;
    private ERP_Spring_Controller erp;
    private Over_Work_Process_List_Adapter adapter;
    private View view;
    private TextView st_date_view,ed_date_view;
    private HashMap<String,Object> type_map = new HashMap<>();
    private String status_type;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_over_work_list, container ,false);
        mcontext = getActivity();
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        pref = mcontext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final String emp_id = pref.getString("emp_id",null);

        st_date_view = (TextView)view.findViewById(R.id.st_date);
        ed_date_view = (TextView)view.findViewById(R.id.ed_date);

        //날짜 셋팅
        final Calendar first_cal = Calendar.getInstance();
        first_cal.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(first_cal.getTime());
        ed_date_view.setText(today);
        first_cal.add(Calendar.DATE,-7);
        today = sdf.format(first_cal.getTime());
        st_date_view.setText(today);


        final Calendar cal = Calendar.getInstance();
        view.findViewById(R.id.st_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mcontext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        st_date_view.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        view.findViewById(R.id.ed_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mcontext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        ed_date_view.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        RadioGroup work_status_group = (RadioGroup)view.findViewById(R.id.work_status_group);
        work_status_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                status_type  = "0";
                TextView status_1_text = (TextView)view.findViewById(R.id.status_1_text);
                switch (i){
                    case R.id.type0 :
                        status_1_text.setVisibility(View.GONE);
                        status_type = "0";
                        break;
                    case R.id.type1 :
                        status_1_text.setVisibility(View.GONE);
                        status_type = "2";
                        break;
                    case R.id.type2 :
                        status_1_text.setVisibility(View.VISIBLE);
                        status_type = "1";
                        break;
                    case R.id.type3 :
                        status_1_text.setVisibility(View.GONE);
                        status_type = "4";
                        break;
                }
                type_map.put("emp_id", emp_id);
                type_map.put("status",status_type);
                serch_over_work_list();
            }
        });

        Button type0 = (Button)view.findViewById(R.id.type0);
        type0.performClick();

        Button over_work_list_serch_btn = (Button)view.findViewById(R.id.over_work_list_serch_btn);
        over_work_list_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serch_over_work_list();
            }
        });

        return view;
    }

    public void serch_over_work_list(){
        if("일자 선택".equals(st_date_view.getText().toString())){
            type_map.put("st_date","");
        }else{
            type_map.put("st_date",st_date_view.getText().toString());
        }

        if("일자 선택".equals(ed_date_view.getText().toString())){
            type_map.put("ed_date","");
        }else{
            type_map.put("ed_date",ed_date_view.getText().toString());
        }

        Call<List<Over_Work_List_VO>> call = erp.over_work_data_type(type_map);
        new Fragement_Over_Work_List.over_work_data_type().execute(call);
    }

    private class over_work_data_type extends AsyncTask<Call, Void , List<Over_Work_List_VO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("작업 리스트 불러오는중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        @Override
        protected List<Over_Work_List_VO> doInBackground(Call... calls) {
            try{
                Call<List<Over_Work_List_VO>> call =calls[0];
                Response<List<Over_Work_List_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Over_Work_List_VO> over_work_list_vos) {
            super.onPostExecute(over_work_list_vos);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(over_work_list_vos != null){
                TextView data_zero_text = (TextView)view.findViewById(R.id.data_zero_text);
                RecyclerView recyclerView = view.findViewById(R.id.over_work_process_list);
                if(over_work_list_vos.size() > 0){
                    data_zero_text.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

                    adapter = new Over_Work_Process_List_Adapter();
                    recyclerView.setAdapter(adapter);

                    for(int i=0; i<over_work_list_vos.size(); i++){
                        over_work_list_vos.get(i).setDisplay_type("N");
                        adapter.addItem(over_work_list_vos.get(i));
                    }
                    adapter.notifyDataSetChanged();

                    try{
                        recyclerView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                view.findViewById(R.id.work_notice).getParent().requestDisallowInterceptTouchEvent(false);
                                return false;
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    recyclerView.setVisibility(View.GONE);
                    data_zero_text.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        serch_over_work_list();
    }
}
