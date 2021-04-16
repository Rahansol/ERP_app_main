package app.erp.com.erp_app.document_care.myInstallSignFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.myfragments.G;
import app.erp.com.erp_app.document_care.myfragments.Installation_List_Sginature_Activity2;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class Fragment3_my_status_completed extends Fragment {

    private Spinner prjSpinner;
    static String stPrjSpinner, stPrjSpinnerValue, TableName, startDateVal, endDateVal, REG_EMP_ID
                    ,stJobName,stBusoffName, stGarageName,stRouteNum, stBusNum,stDocDtti,stRegDtti, stBusId, stTableName, stJobType, stTranspBizrId;
    private TextView startDateTv, endDateTv;
    private RelativeLayout startDateLayout, endDateLayout;
    private DatePickerDialog.OnDateSetListener callBackMethod;
    private Button btnSearch;
    private EditText etSearchBusNum;

    private RecyclerView recyclerView;
    private ArrayList<TranspBizrItems2> transpBizrItems2= new ArrayList<>();
    private TranspBizrAdapter2 transpBizrAdapter2;

    static SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment3_my_status_completed, container, false);

        pref= getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        REG_EMP_ID= pref.getString("emp_id","");
        Log.d("emp_id===> ", REG_EMP_ID+"");

        /*프로젝트 스피너*/
        prjSpinner= view.findViewById(R.id.prj_spinner);
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.JobNameSpinner();
        new getPrjName().execute(call);

        startDateLayout= view.findViewById(R.id.iv_start_date);
        startDateTv= view.findViewById(R.id.tv_start_date);
        endDateLayout= view.findViewById(R.id.iv_end_date);
        endDateTv= view.findViewById(R.id.tv_end_date);

        /*현재날짜 지정*/
        long now= System.currentTimeMillis();
        Date date= new Date(now-259200000);
        Date date2= new Date(now);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        startDateVal= sdf.format(date);
        endDateVal= sdf.format(date2);
        startDateTv.setText(startDateVal);
        endDateTv.setText(endDateVal);

        /*날짜에 DatePicker 설정*/
        /*시작일*/
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stPrjSpinner.equals("선택")){
                    OnClickHandlerStartDate(v);
                }else { Toast.makeText(getContext(), "프로젝트를 선택하세요.", Toast.LENGTH_SHORT).show();}
            }
        });
        /*종료일*/
        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHandlerEndDate(v);
            }
        });

        recyclerView= view.findViewById(R.id.recycler);

        etSearchBusNum= view.findViewById(R.id.et_search_bus_num);

        btnSearch= view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stPrjSpinner.equals("선택")) {
                    Toast.makeText(getContext(), "프로젝트를 선택하세요.", Toast.LENGTH_SHORT).show();
                }else if (etSearchBusNum.getText().toString().length() == 1){
                    Toast.makeText(getContext(), "차량번호를 두자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                    Call<List<Bus_OfficeVO>> call= erp.MyInstallationDetail(TableName, startDateVal, endDateVal, REG_EMP_ID, "YY",etSearchBusNum.getText().toString());
                    Log.d("값 확인==> ", TableName+", "+startDateVal+", "+endDateVal+", "+REG_EMP_ID +", "+etSearchBusNum.getText().toString());
                    new MyInstallationDetailLists().execute(call);
                    transpBizrItems2= new ArrayList<>();     //  리사이클러뷰에 뿌려줄 데이터를 담을 ArrayList 초기화하기..
                }
            }
        });

        return view;
    }//onCreate()....


    public class getPrjName extends AsyncTask<Call, Void, List<Bus_OfficeVO>> {

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS!=null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");
                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getPrj_name());
                }
                prjSpinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                prjSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        stPrjSpinner= prjSpinner.getSelectedItem().toString();
                        if (!stPrjSpinner.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (stPrjSpinner == bus_officeVOS.get(j).getPrj_name()) {
                                    stPrjSpinnerValue = bus_officeVOS.get(j).getPrj_name();
                                    TableName = bus_officeVOS.get(j).getTable_name();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }//getPrjName()...




    public void OnClickHandlerStartDate(View v){
        callBackMethod= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDateVal= String.format("%4d%02d%02d", year, (month+1), dayOfMonth);
                startDateTv.setText(startDateVal);
            }
        };
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        Locale.setDefault(Locale.KOREAN);

        DatePickerDialog dialog= new DatePickerDialog(getContext(), callBackMethod, year, month, day);
        dialog.show();
    }


    public void OnClickHandlerEndDate(View v){
        callBackMethod= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateVal= String.format("%4d%02d%02d", year, (month+1), dayOfMonth);
                endDateTv.setText(endDateVal);
            }
        };
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        Locale.setDefault(Locale.KOREAN);

        DatePickerDialog dialog= new DatePickerDialog(getContext(), callBackMethod, year, month, day);
        dialog.show();
    }


    public class MyInstallationDetailLists extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS == null){
                Toast.makeText(getContext(), "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
                for (int i=0; i<bus_officeVOS.size(); i++){
                    transpBizrItems2.add(new TranspBizrItems2(false
                            , stJobName= bus_officeVOS.get(i).getJob_name()
                            , stJobType= bus_officeVOS.get(i).getJob_type()
                            , stBusoffName= bus_officeVOS.get(i).getBusoff_name()
                            , stTranspBizrId= bus_officeVOS.get(i).getTransp_bizr_id()
                            , stGarageName= bus_officeVOS.get(i).getGarage_name()
                            , stRouteNum= bus_officeVOS.get(i).getRoute_num()
                            , stBusNum= bus_officeVOS.get(i).getBus_num()
                            , stDocDtti= bus_officeVOS.get(i).getDoc_dtti()
                            , stRegDtti= bus_officeVOS.get(i).getReg_dtti()
                            , stBusId= bus_officeVOS.get(i).getBus_id()
                            , stTableName= bus_officeVOS.get(i).getTable_name()));
                }
                transpBizrAdapter2= new TranspBizrAdapter2(getContext(), transpBizrItems2);
                recyclerView.setAdapter(transpBizrAdapter2);
                transpBizrAdapter2.setMyListener(new TranspBizrAdapter2.MyOnItemClickListener() {
                    @Override
                    public void myOnItemClick(View v, int pos) {
                        Intent i= new Intent(getContext(), Installation_List_Sginature_Activity2.class);
                        i.putExtra("item_job_name", bus_officeVOS.get(pos).getJob_name());
                        G.TABLE_NAME= TableName;
                        Log.d("G.Table", G.TABLE_NAME+"");
                        G.PRJ_NAME= stPrjSpinnerValue;
                        Log.d("G.PRJ_NAME", G.PRJ_NAME+"");
                        G.JOB_TYPE= bus_officeVOS.get(pos).getJob_type();
                        G.TRANSP_BIZR_ID= bus_officeVOS.get(pos).getTransp_bizr_id();
                        i.putExtra("item_busoff_name", bus_officeVOS.get(pos).getBusoff_name());
                        i.putExtra("item_garage_name", bus_officeVOS.get(pos).getGarage_name());
                        i.putExtra("item_bus_num", bus_officeVOS.get(pos).getBus_num());
                        i.putExtra("item_bus_id", bus_officeVOS.get(pos).getBus_id());
                        i.putExtra("item_reg_dtti", bus_officeVOS.get(pos).getReg_dtti());
                        i.putExtra("item_sign", bus_officeVOS.get(pos).getDoc_dtti());
                        i.putExtra("item_route_num", bus_officeVOS.get(pos).getRoute_num());
                        i.putExtra("item_table_name", bus_officeVOS.get(pos).getTable_name());
                        startActivity(i);
                    }
                });
            }

        }
    }
}
