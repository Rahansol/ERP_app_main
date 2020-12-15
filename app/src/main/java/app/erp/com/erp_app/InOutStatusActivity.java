package app.erp.com.erp_app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Response;

public class InOutStatusActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActionBar actionBar;

    /*단말기 입출고 현황 리사이클러뷰*/
    private RecyclerView recyclerview_layout_in_out;
    private ArrayList<InOutStatusItem> inOutStatusItems;
    private InOutStatusRecyclerviewAdapter inOutStatusAdapter;

    /*조회기준*/
    private Spinner spinner_search_standard;
    static String emp_id;
    SharedPreferences pref;
    Context mContext;
    static String spinner_items_value;

    /*조회기간*/
    private TextView tv_start_date;
    private RelativeLayout iv_start_date;
    static String start_date_value;    //시작일 전역변수 현재날짜 값 전달
    private TextView tv_end_date;
    private RelativeLayout iv_end_date;
    static String end_date_value;      //종료일 전역변수 현재날짜 값 전달
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private AlertDialog.Builder builder;
    private DatePickerDialog mDate;

    /*FAB*/
    Boolean isOpen= false;
    FloatingActionButton clickFAB, fab1, fab2;
    TextView text1, text2;
    Animation fabOpen, fabClose, fabClock, fabAntiClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_in_out_status);

        /*FAB*/
        clickFAB= findViewById(R.id.clickFAB);
        fab1= findViewById(R.id.fab1);
        fab2= findViewById(R.id.fab2);
        fabOpen= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabClock= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clock);
        fabAntiClock= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clock);

        text1= findViewById(R.id.text1);
        text2= findViewById(R.id.text2);

        clickFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen){
                    text1.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                    fab1.startAnimation(fabOpen);
                    fab2.startAnimation(fabOpen);
                    isOpen=true;
                }else {
                    text1.setVisibility(View.INVISIBLE);
                    text2.setVisibility(View.INVISIBLE);
                    fab1.startAnimation(fabClose);
                    fab2.startAnimation(fabClose);
                    isOpen= false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent(InOutStatusActivity.this, ReleaseRequestActivity.class);
               startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(InOutStatusActivity.this, WarehousingActivity.class);
                startActivity(intent);
            }
        });


        /*달력날짜 선택 전 현재날짜로 먼저 설정*/
        long now= System.currentTimeMillis();
        Date date= new Date(now);
        Date date2= new Date(now-259200000);   //3일전으로 텍스트 설정하기
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        String stCurrentDate= sdf.format(date);
        String stCurrentDate2= sdf.format(date2);
        tv_start_date= findViewById(R.id.tv_start_date);  //시작일
        tv_start_date.setText(stCurrentDate2);

        start_date_value = stCurrentDate2; // 시작일 전역변수 현재날짜 값 전달
        end_date_value = stCurrentDate; // 종료일 전역변수 현재날짜 값 전달

        tv_end_date= findViewById(R.id.tv_end_date);  //종료일
        tv_end_date.setText(stCurrentDate);


        /*날짜 선택*/
        /*시작일*/
        iv_start_date= findViewById(R.id.iv_start_date);   //얘는 잘돼요
        iv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DialogFragment datePicker= new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker1");*/
                OnClickHandler2(v);
            }
        });


        /*종료일*/   /*종료일도 시작일때 햇ㅆ던것처럼 그 방버ㅓㅂ으로 못할까요? */
        iv_end_date= findViewById(R.id.iv_end_date);
        iv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHandler(v);
            }
        });

        //조회기준- 전체내역..
        final String[] search_standard_items= {"전체 내역","출고 내역","입고 내역"};
        spinner_search_standard= findViewById(R.id.spinner_search_standard);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, search_standard_items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_search_standard.setAdapter(adapter);
        spinner_search_standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       //선택된 아이템의 스트링값""
                if(spinner_search_standard.getSelectedItem().toString().equals("출고 내역")) {
                    spinner_items_value= "Out";
                }else if(spinner_search_standard.getSelectedItem().toString().equals("입고 내역")){
                    spinner_items_value= "In";
                }else{
                    spinner_items_value= "All";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pref= getSharedPreferences("user_info", MODE_PRIVATE);
        emp_id= pref.getString("emp_id", "");
        Log.d("emp_id 확인",emp_id);


        /*조회버튼을 누르지않아도 화면을 실행하면 바로 보임..- onCreate 안에 실행*/
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> call= erp.InventoryInOutputList(start_date_value, end_date_value, emp_id, spinner_items_value);
        new inOutRecyclerview().execute(call);

    }//onCreate..



    /*날짜 설정*/
    //시작일 달력
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth){
//        String picker_tag = datePicker.getTag().toString();
//        Log.d("tag ::::", picker_tag);

        /*Calendar day= Calendar.getInstance();
        day.add(Calendar.DATE, -3);
        String beforeThreeDays= new java.text.SimpleDateFormat("yyyy-MM-dd").format(day.getTime());
*/
        tv_start_date= findViewById(R.id.tv_start_date);
        start_date_value= String.format("%4d-%02d-%02d", year, (month+1), dayOfMonth);
        tv_start_date.setText(start_date_value);
    }


    public void OnClickHandler(View v){

        callbackMethod= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_end_date= findViewById(R.id.tv_end_date);
                end_date_value= String.format("%4d-%02d-%02d", year, (month+1), dayOfMonth);
                tv_end_date.setText(end_date_value);
                Log.d("end_date_value", end_date_value+"    tt");
            }
        };

        // 여기가 종료달력 만드는부분이져 ?네네 그럼 여기서 date 객체를 하나 만들어서 오늘날짜로 일단 생성하고
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        Locale.setDefault(Locale.KOREAN);


        DatePickerDialog dialog= new DatePickerDialog(this, callbackMethod, year, month, day);  //이것도 맘에 안들어요 선택값이 이미 이렇게 나오는거니까
        // 이거는 오늘날짜 or 오늘날짜 + 2~3일 로 값을 줘버려요
        // 네 그런데 얘는 왜 두번눌러야 나와요? 음 잠시만요
        dialog.show();

    }



    /*시작일 datePickerDialog 설정하기*/
    public void OnClickHandler2(View v){

        callbackMethod= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_start_date= findViewById(R.id.tv_start_date);
                start_date_value= String.format("%4d-%02d-%02d", year, (month+1), dayOfMonth);
                tv_start_date.setText(start_date_value);
            }
        };

        // 여기가 종료달력 만드는부분이져 ?네네 그럼 여기서 date 객체를 하나 만들어서 오늘날짜로 일단 생성하고
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH)-3;
        Locale.setDefault(Locale.KOREAN);


        DatePickerDialog dialog= new DatePickerDialog(this, callbackMethod, year, month, day);  //이것도 맘에 안들어요 선택값이 이미 이렇게 나오는거니까
        // 이거는 오늘날짜 or 오늘날짜 + 2~3일 로 값을 줘버려요
        // 네 그런데 얘는 왜 두번눌러야 나와요? 음 잠시만요
        dialog.show();

    }





    /*조회 버튼 누르면..*/
    public void clickSearch(View view) {

        if (tv_start_date==null){
            builder= new AlertDialog.Builder(this);
            builder.setTitle("조회기간 정보부족").setMessage("시작일과 종료일을 선택하세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();
        }else if (tv_end_date==null){    //이 다이얼로그 왜 안뜸?
            builder= new AlertDialog.Builder(this);
            builder.setTitle("조회기간 정보부족").setMessage("종료일을 선택하세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }else {
            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            Call<List<TestAllVO>> call= erp.InventoryInOutputList(start_date_value, end_date_value, emp_id, spinner_items_value);
            new inOutRecyclerview().execute(call);
        }

    }



    /*입출고 리사이클러뷰*/
    public class inOutRecyclerview extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];   //스프링 통신
            try{
                Response<List<TestAllVO>> response= call.execute();    //스프링과 통신하기 위해 execute
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {      //return 받는곳
            super.onPostExecute(testAllVOS);

            if (testAllVOS == null){
                Toast.makeText(mContext, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }

            inOutStatusItems= new ArrayList<>();
            for (int i=0; i<testAllVOS.size(); i++){
                inOutStatusItems.add(new InOutStatusItem(testAllVOS.get(i).getReq_type()
                                                         ,testAllVOS.get(i).getReg_date()
                                                         ,testAllVOS.get(i).getReg_time()
                                                         ,testAllVOS.get(i).getReq_date()
                                                         ,testAllVOS.get(i).getRes_date()
                                                         ,testAllVOS.get(i).getRes_name()
                                                         ,testAllVOS.get(i).getReq_name()
                                                         ,testAllVOS.get(i).getUnit_cnt()
                                                         ,testAllVOS.get(i).getSchedule_cnt()
                                                         ,testAllVOS.get(i).getCancel_cnt()
                                                         ,testAllVOS.get(i).getReceipt_cnt()
                                                         ,testAllVOS.get(i).getUnrequest_cnt()));
                Log.d("출고불가 ",testAllVOS.get(i).getCancel_cnt()+"");
            }
            inOutStatusAdapter= new InOutStatusRecyclerviewAdapter(InOutStatusActivity.this, inOutStatusItems);
            recyclerview_layout_in_out= findViewById(R.id.recyclerview_layout_in_out);
            recyclerview_layout_in_out.setAdapter(inOutStatusAdapter);
            inOutStatusAdapter.notifyDataSetChanged();
        }
    }









    public class InventorySpinner extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);

            if (testAllVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");

                for (int i=0; i<testAllVOS.size(); i++){
                    spinner_array.add(testAllVOS.get(i).getIn_yn());
                }
                spinner_search_standard.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));
                spinner_search_standard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String select_search_standard= spinner_search_standard.getSelectedItem().toString();
                        if (select_search_standard != "선택"){
                            for (int j=0; j<testAllVOS.size(); j++){
                                if (select_search_standard == testAllVOS.get(j).getIn_yn()){
                                }
                            }
                        }
                    }
                });
           }
        }
    }








    /*Floating Button 으로 만들 수 있음*/
    public void clickIn(View view) {
        Intent i= new Intent(InOutStatusActivity.this, WarehousingActivity.class);
        startActivity(i);
    }

    public void clickOut(View view) {
        Intent i= new Intent(InOutStatusActivity.this, ReleaseRequestActivity.class);
        startActivity(i);
    }
}//InOutStatusActivity....