package app.erp.com.erp_app.document_care.myfragments;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import app.erp.com.erp_app.document_care.myfragments.Installation_List_Sginature_Activity2;
import app.erp.com.erp_app.document_care.myfragments.TranspBizrAdapter;
import app.erp.com.erp_app.document_care.myfragments.TranspBizrItems;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class Installation_List_Signature_Activity extends AppCompatActivity {

    public static Activity AActivity;

    private Context mContext;
    private Spinner projectSpinner, jobTypeSpinner, busoffNameSpinner, garage_name_spinner;
    static  String stTableName, docDttiSign, BusNums, Item_reg_dtti, Item_bus_id, st_garage_name, st_garage_id, st_garage_name_value, garageName, st_prj, st_prj_value,startDateValue, endDateValue, st_table_name_value, st_jobName, st_jobType_value, st_busoffName, st_busoffName_value, busId, transp_bizr_id, st_reg_dtti;
    private RelativeLayout startDate, endDate, busoffNameLayout, garage_name_layout;
    private TextView tvStartDate, tvEndDate;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private Button btnSearch3;
    private TableLayout table_layout;
    private RelativeLayout click_sign, click_write;

    private RecyclerView recyclerTranspBizr;
    public ArrayList<TranspBizrItems> transpBizrItems= new ArrayList<>();
    private TranspBizrAdapter transpBizrAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation__list__signature);

        getSupportActionBar().setTitle("설치확인서 목록");

        AActivity= Installation_List_Signature_Activity.this;

        mContext= this;
        projectSpinner= findViewById(R.id.project_spinner);
        jobTypeSpinner= findViewById(R.id.jobType_spinner);
        busoffNameSpinner= findViewById(R.id.busoff_name_spinner);
        garage_name_spinner= findViewById(R.id.garage_name_spinner);
        startDate= findViewById(R.id.iv_start_date);
        tvStartDate= findViewById(R.id.tv_start_date);
        tvEndDate= findViewById(R.id.tv_end_date);
        endDate= findViewById(R.id.iv_end_date);
        busoffNameLayout= findViewById(R.id.busoff_name_layout);
        garage_name_layout= findViewById(R.id.garage_name_layout);
        btnSearch3= findViewById(R.id.btnSearch3);


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
                if (!st_prj.equals("선택")){
                    OnClickHandlerStartDate(v);
                }else {
                    Toast.makeText(mContext, "프로젝트를 선택하세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!st_prj.equals("선택")){
                    OnClickHandlerEndDate(v);
                }else {
                    Toast.makeText(mContext, "프로젝트를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //작성하기 아이콘
        click_write= findViewById(R.id.click_write);
        click_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
                builder.setTitle("설치확인서 [작성화면]으로 이동하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i= new Intent(Installation_List_Signature_Activity.this, MyProject_Work_Insert_Activity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });

        //서명하기 아이콘
        click_sign= findViewById(R.id.click_sign);
        click_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tmp =0 ;
                int CheckCnt=0;
                int RunCnt=1;
                BusNums="";


                for(int i=0;i<transpBizrItems.size();i++) {
                    TranspBizrItems Item= transpBizrItems.get(i);
                    if( Item.check == true ) {
                        CheckCnt++;
                    }
                }

                for(int i=0;i<transpBizrItems.size();i++){
                    TranspBizrItems Item= transpBizrItems.get(i);
                    Item_reg_dtti= Item.tv_reg_dtti;
                    Item_bus_id= Item.busId;

                    if( Item.check == true ){

                        BusNums = BusNums + "(REG_DTTI  = '"+Item_reg_dtti+"' AND BUS_ID = '"+ Item_bus_id +"')";    //파라미터로 쓸 값들...
                        if(RunCnt<CheckCnt) {
                            RunCnt++;
                            BusNums = BusNums + " OR ";
                        }

                        tmp++;
                    }
                    //Log.d("Item["+i+"] ::::::::", "["+tmp+"]["+Item.check+"]["+Item.check.booleanValue()+"]");
                }

                Log.d("BusNums ::::::::", BusNums);

                if (tmp!=0){
                    // Toast.makeText(mContext, tmp+"/"+transpBizrItems.size()+" 대를 선택했습니다.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, tmp+" 대의 차량을 선택했습니다.", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
                    builder.setTitle("설치확인서 [서명화면]으로 이동하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i= new Intent(Installation_List_Signature_Activity.this, InstallationSignActivity.class);
                            i.putExtra("prj", st_prj_value);   //프로젝트명
                            i.putExtra("table_name", st_table_name_value);
                            i.putExtra("busoffName", st_busoffName); //운수사명
                            i.putExtra("transp_bizr_id", transp_bizr_id); //운수사ID
                            i.putExtra("garageName", garageName);   //영업소명
                            i.putExtra("garageId", st_garage_id);   //영업소ID
                            i.putExtra("job_name", st_jobName);   //작업
                            i.putExtra("job_type", st_jobType_value); //작업타입
                            i.putExtra("reg_dtti", st_reg_dtti);  //날짜 (for bus_num)
                            i.putExtra("bus_id_reg_dtti", Item_reg_dtti);
                            i.putExtra("bus_id", Item_bus_id);
                            i.putExtra("bus_num", BusNums);

                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create();
                    builder.show();
                }else{
                    //Toast.makeText(mContext, tmp+" 대를 선택했습니다.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
                    builder.setTitle("차량을 선택하세요.")
                            .setMessage("설치 확인서를 작성할 차량을 선택하셔야 합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create();
                    builder.show();
                }
                tmp =0 ;
            }
        });


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
                                    st_table_name_value= bus_officeVOS.get(j).getTable_name();
                                    G.PRJ_NAME= st_prj_value;
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

                spinner_array.add("선택");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getJob_name());
                }
                jobTypeSpinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, spinner_array));
                jobTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_jobName= jobTypeSpinner.getSelectedItem().toString();
                        if (!st_jobName.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_jobName == bus_officeVOS.get(j).getJob_name()){
                                    st_jobType_value= bus_officeVOS.get(j).getJob_type();

                                }
                            }

                            if (st_table_name_value==null){
                                Toast.makeText(mContext, "프로젝트를 선택하세요.", Toast.LENGTH_SHORT).show();
                            }else if (st_jobType_value==null){
                                Toast.makeText(mContext, "작업을 선택하세요.", Toast.LENGTH_SHORT).show();
                            }else{
                                // click 되면 운수사 스피너 보이게하기
                                busoffNameLayout.setVisibility(view.getVisibility());
                                //btnSearch2.setVisibility(view.getVisibility());
                                ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                Call<List<Bus_OfficeVO>> call= erp.BusOffNameSpinner(st_table_name_value, startDateValue, endDateValue, st_jobType_value);
                                Log.d(" Prams ::::::  ", "["+st_table_name_value+"]["+startDateValue+"]["+endDateValue+"]["+st_jobType_value+"]");
                                new BusOffName().execute(call);

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
                                    garageName= bus_officeVOS.get(j).getGarage_name();
                                }
                            }

                            if (st_busoffName != "선택"){  //운수사 스피너가 "선택"이 아니면
                                garage_name_layout.setVisibility(view.getVisibility());
                                btnSearch3.setVisibility(view.getVisibility());              //click 되면 영업소 스피너 보이게하기
                                ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                Call<List<Bus_OfficeVO>> call= erp.GarageNameSpinner(st_table_name_value, transp_bizr_id);
                                new GarageNameSpinner().execute(call);

                                // 리사이클러뷰 Call...
                                //table_layout.setVisibility(view.getVisibility());
                                /*ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                Call<List<Bus_OfficeVO>> call= erp.Transp_Bizr_List(st_table_name_value, startDateValue, endDateValue, transp_bizr_id, st_jobType_value);
                                new Recycler_Transp_Bizr_List().execute(call);*/
                            }else {
                                Toast.makeText(mContext, "운수사를 선택하세요.", Toast.LENGTH_SHORT).show();
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


    /*영업소 스피너*/
    public class GarageNameSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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
                    spinner_array.add(bus_officeVOS.get(i).getGarage_name());
                }
                garage_name_spinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));
                garage_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_garage_name= garage_name_spinner.getSelectedItem().toString();
                        if (st_garage_name != "선택"){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_garage_name == bus_officeVOS.get(j).getGarage_name()){
                                    st_garage_name_value= bus_officeVOS.get(j).getGarage_name();  //영업소 선택값
                                    st_garage_id= bus_officeVOS.get(j).getGarage_id();

                                    Log.d("st_garage_id=====================> ",st_garage_id+"");
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




    public void clickSearch3(View view) {
        if (st_prj.equals("선택")){
            Toast.makeText(mContext, "프로젝트를 선택하세요.", Toast.LENGTH_SHORT).show();
        }else if (st_jobName.equals("선택")){
            Toast.makeText(mContext, "작업을 선택하세요.", Toast.LENGTH_SHORT).show();
        }else if (st_busoffName.equals("선택")){
            Toast.makeText(mContext, "운수사를 선택하세요.", Toast.LENGTH_SHORT).show();
        }else if (st_garage_name.equals("선택")){
            Toast.makeText(mContext, "영업소를 선택하세요.", Toast.LENGTH_SHORT).show();
        }else {
            transpBizrItems.clear();   //아이템을 지워줘야 다른 영업소가 add 되지 않고 새로는 영업소 목록이 나옴.

            //차량리스트 리사이클러뷰 보이기
            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_OfficeVO>> call= erp.Transp_Bizr_List2(st_table_name_value, startDateValue, endDateValue, st_garage_name_value, transp_bizr_id, st_jobType_value);
            new Recycler_Transp_Bizr_List().execute(call);
            Log.d("값 ===> ", st_table_name_value+",  "+ startDateValue+",  "+ endDateValue+",  "+ st_garage_name_value+",  "+ transp_bizr_id+",  "+ st_jobType_value);

            // [조회] 버튼 누르는 순간...
            // ############### 프로젝트 데이터 Installation_List_Signature_Activity2.java 로 보내기..
            // 테이블명, 등록시간, 운수사ID, 버스ID, job 타입
            G.TABLE_NAME= st_table_name_value;
            G.TRANSP_BIZR_ID= transp_bizr_id;
            G.BUS_NUM= busId;  //null...
            G.JOB_TYPE= st_jobType_value;


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

            //transpBizrItems= new ArrayList<>();
            for (int i=0; i<bus_officeVOS.size(); i++){
                transpBizrItems.add(new TranspBizrItems(false
                                                       ,st_jobName
                                                       ,st_busoffName_value
                                                       ,garageName= bus_officeVOS.get(i).getGarage_name()
                                                       ,bus_officeVOS.get(i).getRoute_num()
                                                       ,bus_officeVOS.get(i).getBus_num()
                                                        ,docDttiSign= bus_officeVOS.get(i).getDoc_dtti()  //확인서: "완료"
                                                        ,st_reg_dtti= bus_officeVOS.get(i).getReg_dtti()
                                                        ,busId= bus_officeVOS.get(i).getBus_id()
                                                        ,stTableName= bus_officeVOS.get(i).getTable_name()));

                Log.d("차대번호?=>   ", bus_officeVOS.get(i).getBus_id()+"");  //null
                Log.d("등록시간?=>   ", st_reg_dtti+"");
                Log.d("버스ID?=>   ", busId+"");
            }
            Log.d("등록시간?=>   ", st_reg_dtti+"");
            Log.d("버스ID?=>   ", busId+"");
            transpBizrAdapter= new TranspBizrAdapter(mContext, transpBizrItems);
            recyclerTranspBizr= findViewById(R.id.recyclerview_transp_bizr);
            recyclerTranspBizr.setAdapter(transpBizrAdapter);
            transpBizrAdapter.notifyDataSetChanged();
        }
    }
}