package app.erp.com.erp_app.document_care.myfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class Installation_List_Signature_Activity extends AppCompatActivity {

    private Context mContext;
    private Spinner projectSpinner, jobTypeSpinner, busoffNameSpinner;
    static String st_prj, st_prj_value,startDateValue, endDateValue, st_table_name_value, st_jobName, st_jobType_value, st_busoffName, st_busoffName_value, busId, transp_bizr_id, st_reg_dtti;
    private RelativeLayout startDate, endDate, busoffNameLayout;
    private TextView tvStartDate, tvEndDate;
    private DatePickerDialog.OnDateSetListener callbackMethod, callbackMethod2;
    private Button btnSearch2, btnWrite;
    private TableLayout table_layout;

    private RecyclerView recyclerTranspBizr;
    private ArrayList<TranspBizrItems> transpBizrItems;
    private TranspBizrAdapter transpBizrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation__list__signature);

        getSupportActionBar().setTitle("설치확인서 목록");

        mContext= this;
        projectSpinner= findViewById(R.id.project_spinner);
        jobTypeSpinner= findViewById(R.id.jobType_spinner);
        busoffNameSpinner= findViewById(R.id.busoff_name_spinner);
        startDate= findViewById(R.id.iv_start_date);
        tvStartDate= findViewById(R.id.tv_start_date);
        tvEndDate= findViewById(R.id.tv_end_date);
        endDate= findViewById(R.id.iv_end_date);
        busoffNameLayout= findViewById(R.id.busoff_name_layout);
        table_layout= findViewById(R.id.table_layout);
        btnSearch2= findViewById(R.id.btnSearch2);
        btnWrite= findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
                builder.setTitle("확인서 작성하기 화면으로 이동하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Intent i= new Intent(mContext, MyProject_Work_Insert_Activity.class);
                        startActivity(i);*/
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();

            }
        });


        /*먼저 현재날짜 지정*/
        long now= System.currentTimeMillis();
        Date date= new Date(now-259200000);
        Date date2= new Date(now);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        startDateValue= sdf.format(date);
        endDateValue= sdf.format(date2);
        tvStartDate.setText(startDateValue);
        tvEndDate.setText(endDateValue);


        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.JobNameSpinner();
        new projectName().execute(call);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!st_prj.equals("선택") && !st_jobName.equals("선택")){
                    OnClickHandlerStartDate(v);
                }else {
                    Toast.makeText(mContext, "프로젝트와 작업을 선택하세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!st_prj.equals("선택") && !st_jobName.equals("선택")){
                    OnClickHandlerEndDate(v);
                }else {
                    Toast.makeText(mContext, "프로젝트와 작업을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });




        /*차량리스트 리사이클러뷰 테스트..*/
        /*transpBizrItems= new ArrayList<>();
        transpBizrItems.add(new TranspBizrItems(true, "대폐차", "경기고속", "경기고속(감곡)", "20-10", "경기10아1234", "DOC_DTTI"));
        transpBizrItems.add(new TranspBizrItems(true, "대폐차", "경기고속", "경기고속(감곡)", "20-10", "경기10아1234", "DOC_DTTI"));
        transpBizrItems.add(new TranspBizrItems(true, "대폐차", "경기고속", "경기고속(감곡)", "20-10", "경기10아1234", "DOC_DTTI"));
        transpBizrItems.add(new TranspBizrItems(true, "대폐차", "경기고속", "경기고속(감곡)", "20-10", "경기10아1234", "DOC_DTTI"));
        transpBizrItems.add(new TranspBizrItems(true, "대폐차", "경기고속", "경기고속(감곡)", "20-10", "경기10아1234", "DOC_DTTI"));
        transpBizrAdapter= new TranspBizrAdapter(Installation_List_Signature_Activity.this, transpBizrItems);
        recyclerTranspBizr= findViewById(R.id.recyclerview_transp_bizr);
        recyclerTranspBizr.setAdapter(transpBizrAdapter);*/

    }//onCreate...



    /*프로젝트 스피너*/
    public class projectName extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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

            Log.d("bus_officeVOS 사이즈..====> ", bus_officeVOS.size()+"");

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");
                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getPrj_name());
                }
                projectSpinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, spinner_array));
                projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_prj= projectSpinner.getSelectedItem().toString();
                        if (!st_prj.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_prj == bus_officeVOS.get(j).getPrj_name()){
                                    st_prj_value= bus_officeVOS.get(j).getPrj_name();   //프로젝트 선택값
                                    st_table_name_value= bus_officeVOS.get(j).getTable_name(); //??

                                    Log.d("st_prj=> ", st_prj+"");
                                    Log.d("st_prj_value=> ", st_prj_value+"");
                                }
                            }

                            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call= erp.PrjBaseInfraJobSpinner();
                            new PrjBaseInfraJobSpinner().execute(call);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }

    /*작업 스피너*/
    public class PrjBaseInfraJobSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();

                spinner_array.add("전체");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getJob_name());
                }
                jobTypeSpinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, spinner_array));
                jobTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_jobName= jobTypeSpinner.getSelectedItem().toString();
                        if (!st_jobName.equals("전체")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_jobName == bus_officeVOS.get(j).getJob_name()){
                                    st_jobType_value= bus_officeVOS.get(j).getJob_type();
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
    }




    /*DatePicker Dialog*/
    public void OnClickHandlerStartDate(View v){
        callbackMethod= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDateValue= String.format("%4d%02d%02d", year, (month+1), dayOfMonth);
                tvStartDate.setText(startDateValue);
            }
        };
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        Locale.setDefault(Locale.KOREAN);

        DatePickerDialog dialog= new DatePickerDialog(this, callbackMethod, year, month, day);
        dialog.show();
    }


    public void OnClickHandlerEndDate(View v){
        callbackMethod= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateValue= String.format("%4d%02d%02d", year, (month+1), dayOfMonth);
                tvEndDate.setText(endDateValue);
            }
        };
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        Locale.setDefault(Locale.KOREAN);

        DatePickerDialog dialog= new DatePickerDialog(this, callbackMethod, year, month, day);
        dialog.show();
    }


    /*조회버튼 클릭*/
    public void clickSearchBtn(View view) {
        if (st_table_name_value==null){
            Toast.makeText(mContext, "프로젝트를 선택하세요.", Toast.LENGTH_SHORT).show();
        }else if (st_jobType_value==null){
            Toast.makeText(mContext, "작업을 선택하세요.", Toast.LENGTH_SHORT).show();
        }else{
            busoffNameLayout.setVisibility(view.getVisibility());  //click 되면 운수사 스피너 보이게하기
            btnSearch2.setVisibility(view.getVisibility());
            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_OfficeVO>> call= erp.BusOffNameSpinner(st_table_name_value, startDateValue, endDateValue, st_jobType_value);
            Log.d(" Prams ::::::  ", "["+st_table_name_value+"]["+startDateValue+"]["+endDateValue+"]["+st_jobType_value+"]");
            new BusOffName().execute(call);
        }
    }



    public class BusOffName extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                Log.d("response====> ", response.body()+"");
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("예외처리..=====> ", e+"");
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            Log.d("busoff_vo 사이즈 확인..===> ", bus_officeVOS.size()+"");
            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");
                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getBusoff_name());
                }
                busoffNameSpinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));
                busoffNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_busoffName= busoffNameSpinner.getSelectedItem().toString();
                        if (st_busoffName != "선택"){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_busoffName == bus_officeVOS.get(j).getBusoff_name()){
                                    st_busoffName_value= bus_officeVOS.get(j).getBusoff_name();
                                    transp_bizr_id= bus_officeVOS.get(j).getTransp_bizr_id();
                                    String garageName= bus_officeVOS.get(j).getGarage_name();
                                    String busNum= bus_officeVOS.get(j).getBus_num();

                                    Log.d("dd::   ","["+st_busoffName_value+"]["+ transp_bizr_id+"]["+garageName+"]["+busNum+"]");
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
    }


    /*운수사 조회버튼2*/
    public void clickSearch2(View view) {
        if (st_busoffName != "선택"){
            // 리사이클러뷰 Call...
            table_layout.setVisibility(view.getVisibility());
            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_OfficeVO>> call= erp.Transp_Bizr_List(st_table_name_value, startDateValue, endDateValue, transp_bizr_id, st_jobType_value);
            new Recycler_Transp_Bizr_List().execute(call);
        }else {
            Toast.makeText(mContext, "운수사를 선택하세요.", Toast.LENGTH_SHORT).show();
        }

    }


    public class Recycler_Transp_Bizr_List extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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
                Toast.makeText(mContext, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }

            transpBizrItems= new ArrayList<>();
            for (int i=0; i<bus_officeVOS.size(); i++){
                transpBizrItems.add(new TranspBizrItems(false
                                                       ,st_jobName
                                                       ,st_busoffName_value
                                                       ,bus_officeVOS.get(i).getGarage_name()
                                                       ,bus_officeVOS.get(i).getRoute_num()
                                                       ,bus_officeVOS.get(i).getBus_num()
                                                        ,""   //   "○"
                                                        ,st_reg_dtti= bus_officeVOS.get(i).getReg_dtti()
                                                        ,busId= bus_officeVOS.get(i).getBus_id()));
                Log.d("reg_dtti=======>  ", bus_officeVOS.get(i).getReg_dtti());

            }

            transpBizrAdapter= new TranspBizrAdapter(mContext, transpBizrItems);
            recyclerTranspBizr= findViewById(R.id.recyclerview_transp_bizr);
            recyclerTranspBizr.setAdapter(transpBizrAdapter);
            transpBizrAdapter.notifyDataSetChanged();


            transpBizrAdapter.setMyListener(new TranspBizrAdapter.MyOnItemClickListener() {
                @Override
                public void myOnItemClick(View v, int pos) {
                    //아이템을 클릭하면 선택 체크박스도 선택되게하기

                    //파라미터값 intent로 다음화면(step3)으로 전달 및 이동..
                    Intent i= new Intent(mContext, Installation_List_Sginature_Activity2.class);
                    i.putExtra("prj", st_prj_value);
                    i.putExtra("table_name", st_table_name_value);
                    i.putExtra("reg_dtti", st_reg_dtti);
                    i.putExtra("transp_bizr_id", transp_bizr_id);
                    i.putExtra("bus_id", busId);
                    i.putExtra("job_type", st_jobType_value);
                    i.putExtra("job_name",st_jobName );

                    Log.d("prj=> ", st_prj_value+"");

                    startActivity(i);
                }
            });
        }
    }
}