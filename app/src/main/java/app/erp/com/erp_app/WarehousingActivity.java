package app.erp.com.erp_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class WarehousingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    /*Context 전역변수로 생성- Retrofit은 AsyncTask를 extends해서 백그라운드에서 돌아서 this가 안먹힘*/
    static Context mContext;


    /*달력*/
    private RelativeLayout calendar_layout;
    static TextView tvShowDate;
    static String currentDateValue;

    /*로그인 아이디*/
    static String emp_id;
    static String emp_dep_id;
    SharedPreferences pref;

    /*출고위치 스피너*/
    private Spinner spinnerArea;
    static String selectedSpinnerAreaItems;  //출고스피너 선택값
    static String barcode_dep_id;      //출고스피너- (개인??)지부값
    static String barcode_dep_id_intent;
    /*SharedPreference 통해 barcode_dep_id_intent 값 저장 */
    private SharedPreferences sp_barcode_dep_id, sp_response_dep_id, sp_current_date;

    /*분류 스피너*/
    private Spinner spinnerUnitGroup;
    static String selectedUnitGroup;

    /*입고위치 스피너*/
    private
    Spinner spinnerReleaseLocation;
    static String selectedReleaseLocation;  //입고스피너 선택값
    static String selectedLocationId;  //입고스피너- 지부값

    /*선택현황 분류 리사이클러뷰*/
    private RecyclerView recyclerviewSelectedInUnit;    // 리사이클러뷰 레이아웃
    private ArrayList<selectedStatusItems2> itemsSelected; //새로만든 어레이 아이템
    private InUnitSelectedStatusAdapter adapterSelected;  //새로만든 어댑터

    /*입고 리스트 리사이클러뷰(1)*/
    private RecyclerView recyclerViewInUnitList;          //리사이클러뷰 레이아웃
    private ArrayList<InUnitListItems> inUnitListItems;   //같은 ArrayList item 공유
    private ArrayList<InUnitListItems> inUnitListItems2;  //같은 ArrayList item 공유
    static InUnitListAdapter inUnitListAdapter;
    /*입고 리스트 리사이클러뷰 확인(2)*/
    private RecyclerView recyclerViewInUnitList2;
    private InUnitListAdapter2 inUnitListAdapter2;
    /*넘겨받은 아이템..*/
    static String InUnitCode;
    static String InRepUnitCode;
    static String InUnitId;

    /*특이사항 메세지*/
    private EditText etMsg;
    static String notice;
    private TextView tv_msg_notice;

    /*입고신청 완료버튼*/
    private RelativeLayout btnInCheck;
    private RelativeLayout btnInOk;
    private AlertDialog.Builder builder;

    static TextView tv_notification_cnt;
    private FloatingActionButton fab;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= this.getMenuInflater();
        menuInflater.inflate(R.menu.release_request_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.release_request_btn:{
                Toast.makeText(this, "출고신청 페이지로 이동", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(WarehousingActivity.this, ReleaseRequestActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_warehousing);

        mContext= this;


        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, ReleaseRequestActivity.class);
                startActivity(intent);
            }
        });

        /*달력*/
        calendar_layout= findViewById(R.id.calendar_layout);
        calendar_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment2 dialogFragment= new DatePickerFragment2();
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        long now= System.currentTimeMillis();
        Date date= new Date(now);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        String currentDate= sdf.format(date);
        tvShowDate= findViewById(R.id.tv_show_date);
        tvShowDate.setText(currentDate);

        currentDateValue= currentDate;

        /*SharePreference로 req_date 데이터 값저장하고 입고대상확인 버튼을 눌렀을때 이 데이터값 뽑아쓰기 */
        sp_current_date= getSharedPreferences("sp_current_date", MODE_PRIVATE);
        SharedPreferences.Editor editor= sp_current_date.edit();
        editor.putString("currentDateValue",currentDateValue);
        Log.d("currentDateValue" ,currentDateValue+"  %%%%%%%%%% ");
        editor.commit();


        spinnerArea= findViewById(R.id.spinner_area);
        pref= getSharedPreferences("user_info", MODE_PRIVATE);
        emp_id= pref.getString("emp_id","");
        emp_dep_id= pref.getString("emp_dep_id","");
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> call= erp.AppOutLocationSpinnerList(emp_id, "In", emp_dep_id);
        new OutLocation().execute(call);

        spinnerUnitGroup= findViewById(R.id.spinner_unit_group);
        spinnerReleaseLocation= findViewById(R.id.spinner_release_location2);


        btnInOk= findViewById(R.id.btn_in_ok);
        btnInOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 특이사항 메세지 전달..
                etMsg= findViewById(R.id.et_msg_notice);
                notice= etMsg.getText().toString();
                builder = new AlertDialog.Builder(mContext);
                if (selectedSpinnerAreaItems.equals("선택")){
                    builder.setTitle("지부 정보부족").setMessage("지부 선택을 완료하세요");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();
                }else if (selectedReleaseLocation.equals("선택")){
                    builder.setTitle("입고 정보부족").setMessage("입고 선택을 완료하세요");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else if (tvShowDate==null){
                    builder.setTitle("요청날짜 정보부족").setMessage("출고신청 날짜선택을 완료하세요");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    builder = new AlertDialog.Builder(WarehousingActivity.this);
                    builder.setTitle("입고 신청").setMessage("예약을 완료 하시겠습니까?");
                    builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            LayoutInflater inflater= getLayoutInflater();
                            View layout= inflater.inflate(R.layout.custom_toast_complete, (ViewGroup)findViewById(R.id.custum_toast_layout));
                            TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                            tv_custom_toast.setText("신청이 완료되었습니다.");
                            final Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0,80);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                            //완료버튼을 누르면 예약상태 업데이트및... 통신....
                            ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<Void> call1= erp1.AppInsertRequestList_in(emp_id, currentDateValue, notice, barcode_dep_id, selectedLocationId, emp_dep_id, "In" );
                            call1.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {

                                    Toast.makeText(WarehousingActivity.this, "완료", Toast.LENGTH_SHORT).show();

                                    Log.d("완료버튼: emp_id ",emp_id+"");
                                    Log.d("완료버튼: currentDateValue ",currentDateValue+"");
                                    Log.d("완료버튼: notice ",notice+"");
                                    Log.d("완료버튼: barcode_dep_id ",barcode_dep_id+"");
                                    Log.d("완료버튼: selectedLocationId ",selectedLocationId+"");
                                    Log.d("완료버튼: emp_dep_id ",emp_dep_id+"");
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });

                            Intent intent = new Intent(WarehousingActivity.this, InOutStatusActivity.class);  //단말기 입출고 상태현황 페이지로 이동
                            startActivity(intent);

                        }
                    });
                }

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }//onClick
        });

    }// onCreate()....












    /*달력*/
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth){
        tvShowDate= findViewById(R.id.tv_show_date);
        currentDateValue= String.format("%4d-%02d-%02d",year,(month+1),dayOfMonth);
        tvShowDate.setText(currentDateValue);
    }


    /*스피너 아이템 클릭?*/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    /*지부- 출고위치 스피너*/
    public class OutLocation extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();   //통신
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);

            Log.d("통신 ","ㅇㅇ");
            Log.d("emp_id ",emp_id+" ㅇㅇ");                             //msookim
            Log.d("testAllVOS 사이즈 ",testAllVOS.size()+" ㅇㅇ");       //2
            Log.d("testAllVOS 사이즈 ",testAllVOS.toString()+" ㅇㅇ");

            if (testAllVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");

                for (int i=0; i<testAllVOS.size(); i++){
                    spinner_array.add(testAllVOS.get(i).getDep_name());
                    Log.d("testAllVOS.get(i).getDep_name()======> ",testAllVOS.get(i).getDep_name()+" dddd");    //재고관리, 강북지구
                }


                spinnerArea= findViewById(R.id.spinner_area);
                spinnerArea.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));
                spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                       selectedSpinnerAreaItems= spinnerArea.getSelectedItem().toString();
                        if (!selectedSpinnerAreaItems.equals("선택")){
                            for (int j=0; j<testAllVOS.size(); j++){
                                if (selectedSpinnerAreaItems == testAllVOS.get(j).getDep_name()){
                                    barcode_dep_id= testAllVOS.get(j).getBarcode_dep_id();     // (개인??)지부값
                                    barcode_dep_id_intent= testAllVOS.get(j).getBarcode_dep_id();

                                    Log.d("출고요청 스피너- 개인지부값 : ",barcode_dep_id_intent+"" );    //99000201

                                    /*SharePreference로 barcode_dep_id 데이터 값저장하고 입고대상확인 버튼을 눌렀을때 이 데이터값 뽑아쓰기 */
                                    sp_barcode_dep_id= getSharedPreferences("sp_barcode_dep_id", MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sp_barcode_dep_id.edit();
                                    editor.putString("barcodeDepIdValue",testAllVOS.get(j).getBarcode_dep_id());
                                    Log.d("barcodeDepIdValue  ^^^^^^^^^^^^^^^^^^ SharedPreference 를 통해 값전달전 로그찍어보기", testAllVOS.get(j).getBarcode_dep_id()+"");
                                    editor.commit();
                                }
                            }

                            //분류 스피너 생성하기 전 통신값 보내기..
                            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<TestAllVO>> call= erp.AppUnitGroupSpinnerList(barcode_dep_id, "In", emp_dep_id);
                            new UnitGroupList().execute(call);

                            //입고위치 스피너 생성하기 전 통신값 보내기..
                            ERP_Spring_Controller erp2= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<TestAllVO>> call2= erp2.AppInventoryInOfficeList("In",emp_id,  barcode_dep_id,  emp_dep_id);
                            new InLocationSpinner().execute(call2);


                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    } //OutLocation()  -출고위치 스피너 함수...


    /*입고대상 확인버튼..*/
    public void clickBtnCheck(View view) {

        /*입고대상 확인버튼 누르면서 분류, 입고스피너 초기화하기*/
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> call= erp.AppUnitGroupSpinnerList(barcode_dep_id, "In", emp_dep_id);
        new UnitGroupList().execute(call);

        Intent i= new Intent(mContext, DialogFinalCheckInActivity.class);
        mContext.startActivity(i);
    }


    /*분류 스피너*/
    public class UnitGroupList extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();   //통신- 스프링과 연결
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<TestAllVO> testAllVOS) {     //결과값 받는 곳
            super.onPostExecute(testAllVOS);

            Log.d("testAllVOS 사이즈 #################### ",testAllVOS.size()+" 분류");
            Log.d("barcode_dep_id #################", barcode_dep_id+" tt");     //99000201
            Log.d("emp_id #####################",emp_id+" tt" );                     //msookim

            if (testAllVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");

                for (int i=0; i<testAllVOS.size(); i++){
                    spinner_array.add(testAllVOS.get(i).getUnit_group());
                    Log.d("분류 ",testAllVOS.get(i).getUnit_group()+" ㅇㅇ");      //AFC_1.5    //AFC_2.0    //AFC_3.0
                }
                spinnerUnitGroup.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line,spinner_array));
                spinnerUnitGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedUnitGroup= spinnerUnitGroup.getSelectedItem().toString();
                        Log.d("선택된 분류 아이템 스피너 ", spinnerUnitGroup.getSelectedItem().toString()+" aaaaa");

                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<TestAllVO>> call= erp.AppUnitGroupSelectedList(selectedUnitGroup, barcode_dep_id,"In");
                        new selectedUnitGroupStatus().execute(call);



                        //분류아이템을 선택할때 입고리스트 초기화해야함...
                        inUnitListItems= new ArrayList<>();
                        inUnitListItems2= new ArrayList<>();
                        //inUnitListItems.add(new InUnitListItems("1","rnum","rnum",false,"rnum","rnum","rnum","rnum","rnum","rnum","rnum","rnum","rnum","rnum"));

                        inUnitListAdapter2= new InUnitListAdapter2(mContext, inUnitListItems2, inUnitListItems, inUnitListAdapter);
                        inUnitListAdapter= new InUnitListAdapter(mContext, inUnitListItems, inUnitListItems2, inUnitListAdapter2);
                        recyclerViewInUnitList= findViewById(R.id.recyclerView_in_list);
                        recyclerViewInUnitList.setAdapter(inUnitListAdapter);

                        recyclerViewInUnitList2= findViewById(R.id.recyclerView_in2);
                        recyclerViewInUnitList2.setAdapter(inUnitListAdapter2);
                        inUnitListItems.clear();
                        inUnitListAdapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        }
    }

    /*분류 선택현황*/
    public class selectedUnitGroupStatus extends AsyncTask<Call,Void,List<TestAllVO>> {

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call = calls[0];
            try {
                Response<List<TestAllVO>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);
            Log.d("분류함수에서 넘겨받은 값=====> ", "선택한 분류 아이템: " + selectedUnitGroup);
            Log.d("분류함수에서 넘겨받은 값=====> ", "선택한 분류 바코드: " + barcode_dep_id);
            Log.d("testAllVOS 사이즈 ", testAllVOS.size() + " gg");
            Log.d("testAllVOS 스트링 ", testAllVOS.toString()+"" );

            itemsSelected = new ArrayList<>();
            for (int i = 0; i < testAllVOS.size(); i++) {
                itemsSelected.add(new selectedStatusItems2(testAllVOS.get(i).getUnit_explain()
                                                         , testAllVOS.get(i).getUnit_cnt()
                                                         , testAllVOS.get(i).getUnit_code()
                                                         , testAllVOS.get(i).getRep_unit_code()
                                                         , testAllVOS.get(i).getUnit_id()));
            }
            adapterSelected = new InUnitSelectedStatusAdapter(mContext, itemsSelected);
            recyclerviewSelectedInUnit = findViewById(R.id.recyclerview_selected_in_unit);
            recyclerviewSelectedInUnit.setLayoutManager(new GridLayoutManager(mContext, 2));
            recyclerviewSelectedInUnit.setAdapter(adapterSelected);


            // 4. InUnitSelectedStatusAdapter 에서 넘겨받은 커스텀 리스너 객체를 생성하여 어댑터에 전달..
            adapterSelected.setmListener(new InUnitSelectedStatusAdapter.OnitemClickListener() {
                @Override
                public void onItemClick(View v, int post) {

                    String tagFromAdapter= (String) v.getTag();
                    String tt[]= tagFromAdapter.split("&");

                    InUnitCode= tt[0];
                    InRepUnitCode= tt[1];
                    InUnitId= tt[2];   // 왜 null?? 쿼리문에 unit_id 가 없어서??  ==> o o

                    /*선택현황 아이템을 클릭하고 스피너와 날짜값이 있는지 확인*/
                    if (tvShowDate==null){
                        builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("요청날짜 정보부족").setMessage("출고신청 날짜선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else if (selectedSpinnerAreaItems.equals("선택")){
                        builder= new AlertDialog.Builder(mContext);
                        builder.setTitle("지부 정보부족").setMessage("지부 선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog= builder.create();
                        alertDialog.show();
                    }else if (selectedUnitGroup.equals("선택")){
                        builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("입고 정보부족").setMessage("입고 선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else if (selectedReleaseLocation.equals("선택")){
                        builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("입고위치 정보부족").setMessage("입고위치 선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }else {
                        /*입고리스트로 통신할때 보내줄 파라미터 던지기..*/
                        ERP_Spring_Controller erp2= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Log.d("AppInUnitList Param ", ""+barcode_dep_id+"/"+InUnitCode+"/"+ InRepUnitCode);
                        Call<List<TestAllVO>> call2= erp2.AppSelectInUnitList(barcode_dep_id, InUnitCode, InRepUnitCode, "In");
                        new InUnitRecyclerView().execute(call2);
                    }

                }
            });
            adapterSelected.notifyDataSetChanged();
        }
    }

    /*입고리스트 리사이클러뷰*/
    public class InUnitRecyclerView extends AsyncTask<Call, Void, List<TestAllVO>>{
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
        protected void onPostExecute(List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);

            Log.d("testAllVO 사이즈 @@@@@@@@@@@@@@@@ ",testAllVOS.size()+"");    // 사이즈 0
            Log.d("testAllVO toString @@@@@@@@@@@@@@@" ,testAllVOS.toString()+"");
            Log.d("넘겨받은 barcode_dep_id @@@@@@@@@@@@@@@@@@@@@", barcode_dep_id+"");
            Log.d("넘겨받은 rep_unit_code @@@@@@@@@@@@@@@@@@@@@", InRepUnitCode+"");
            Log.d("넘겨받은 unit_code @@@@@@@@@@@@@@@@@@@@@", InUnitCode+"");

            inUnitListItems= new ArrayList<>();
            inUnitListItems2= new ArrayList<>();                                                    //ArrayList size가 null일 때는 안드로이드쪽과 스프링의 파라미터가 맞는지 확인해보기.
            for (int i=0; i<testAllVOS.size(); i++){
                Log.d("testAllVOS 사이즈 체크 ",testAllVOS.size()+"");
                inUnitListItems.add(new InUnitListItems(testAllVOS.get(i).getRnum()
                                                        ,testAllVOS.get(i).getUnit_ver()
                                                        ,testAllVOS.get(i).getUnit_id()
                                                        ,false
                                                        ,testAllVOS.get(i).getUn_yn()
                                                        ,testAllVOS.get(i).getIn_yn()
                                                        ,emp_id
                                                        ,testAllVOS.get(i).getUnit_code()
                                                        ,testAllVOS.get(i).getRep_unit_code()
                                                        ,barcode_dep_id
                                                        ,currentDateValue
                                                        ,notice
                                                        ,barcode_dep_id   //getRequest_dep_id (상단지부 스피너 값)
                                                        ,selectedLocationId)); //getResponse_dep_id (하단지부 스피너 값)
                Log.d("testAllVO의 getUnit_ver ************************ ",testAllVOS.get(i).getUnit_ver()+"");   //null

                Log.d("testAllVOS.get(i).getReq_date()  =============================>>>>>>>>>>  ", currentDateValue+"");
                Log.d("testAllVOS.get(i).getNotice()   ==============================>>>>>>>>>>  ", notice+"");
                Log.d("testAllVOS.get(i).getRequest_dep_id()  =======================>>>>>>>>>>  ", barcode_dep_id+"");
                Log.d("testAllVOS.get(i).getResponse_dep_id() =======================>>>>>>>>>>  ", selectedLocationId+"");

            }
            inUnitListAdapter2= new InUnitListAdapter2(mContext, inUnitListItems2, inUnitListItems, inUnitListAdapter);
            inUnitListAdapter= new InUnitListAdapter(mContext, inUnitListItems, inUnitListItems2, inUnitListAdapter2);
            recyclerViewInUnitList= findViewById(R.id.recyclerView_in_list);
            recyclerViewInUnitList.setAdapter(inUnitListAdapter);

            recyclerViewInUnitList2= findViewById(R.id.recyclerView_in2);
            recyclerViewInUnitList2.setAdapter(inUnitListAdapter2);
            inUnitListAdapter2.notifyDataSetChanged();


        }
    }

    /*입고위치 스피너*/
    public class InLocationSpinner extends AsyncTask<Call, Void, List<TestAllVO>> {

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call = calls[0];
            try {
                Response<List<TestAllVO>> response = call.execute();
                Log.d("입고위치 response----> ",response+" ");
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);

            Log.d("입고위치 사이즈----> ",testAllVOS.size()+" ");

            Log.d("입고위치 스피너 emp_id", emp_id+" ");
            Log.d("barcode_dep_id", barcode_dep_id+" ");
            Log.d("emp_dep_id", emp_dep_id+" ");

            if (testAllVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");

                for (int i=0; i<testAllVOS.size(); i++){
                    spinner_array.add(testAllVOS.get(i).getDep_name());
                }
                spinnerReleaseLocation.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));
                spinnerReleaseLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedReleaseLocation= spinnerReleaseLocation.getSelectedItem().toString();
                        if (!selectedReleaseLocation.equals("선택")){
                            for (int j=0; j<testAllVOS.size(); j++){
                                if (selectedReleaseLocation == testAllVOS.get(j).getDep_name()){
                                    selectedLocationId= testAllVOS.get(j).getBarcode_dep_id();    //완료버튼 누를때 파라미터로 전달.. 전역변수로 만들어주기.   //입고스피너- 지부값  //barcode값인가 location값인가>
                                    Log.d("selectedLocationId : ",selectedLocationId+"");
                                    Toast.makeText(mContext, "입고 스피너- 지부값: "+selectedLocationId, Toast.LENGTH_SHORT).show();  //null- why?


                                    sp_response_dep_id= getSharedPreferences("sp_response_dep_id", MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sp_response_dep_id.edit();
                                    editor.putString("responseDepId",testAllVOS.get(j).getBarcode_dep_id());
                                    Log.d("responseDepId  ^^^^^^^^^^^^^^^^^^ SharedPreference 를 통해 값전달전 로그찍어보기", testAllVOS.get(j).getBarcode_dep_id()+"");
                                    editor.commit();
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





}
