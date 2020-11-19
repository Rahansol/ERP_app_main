package app.erp.com.erp_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TooltipCompat;
import android.util.AndroidException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.erp.com.erp_app.error_history.Error_History_Activity;
import app.erp.com.erp_app.vo.TestAllVO;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import retrofit2.Call;
import retrofit2.Response;

public class ReleaseRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ImageView iv_back;

    //선택 현황 리사이클러뷰
    private RecyclerView recyclerView_selected;
    private ArrayList<selectedStatusItems> items_selected;
    private SelectedStatusAdapter adapter_selected;
    private TextView tv_tag_test;
    private Boolean isChecked= true;

    //재고 리스트-(1) 리사이클러뷰
    private RecyclerView recyclerView_stock_list;
    private ArrayList<StockListItems> items_stock;
    private ArrayList<StockListItems> items_stock_2;
    private StockListAdapter adapter_stock;
    //재고 리스트-(2) 리사이클러뷰   (1과 같은 ArrayList items 사용)
    private RecyclerView recyclerView_stock_list_2;
    private StockListAdapter2 adapter_stock_2;

    //네비게이션 메뉴
    private DrawerLayout drawer;
    private NavigationView nav;
    private ImageView menu_btn;

    SharedPreferences pref;

    //지부/분류/출고위치 스피너
    private Spinner spinnerArea;     //지부
    private Spinner spinnerTerminal; //분류
    private Spinner spinner_release_location;  //출고위치 스피너
    private Spinner spinnerReleaseLocation;  //출고위치
    private Button btn_area; //테스트용 버튼
    private TextView tv_area; //테스트용 텍스트뷰

    private String emp_id;     // 아이디 전역변수로..
    private String barcode_dep_id; // 선택한 지부 id- 전역변수로...
    private String select_dep_name; // 선택한 분류?
    private String unitCode_1;
    private String repUnitCode_2;
    private String getUnitId;



    //캘린터
    private TextView tvShowDate;
    private RelativeLayout calendar_layout;

    private AlertDialog.Builder builder;

    //Context  전역변수로 만들어주기   //레트로핏은 AsyncTask를 extends 해서 백그라운드에서 돌아서 this가 안먹힘
    private Context mContext;
    private Object Context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_request);

        mContext = this;

        //로그인 정보 가져오기
        pref = getSharedPreferences("user_info" , MODE_PRIVATE);

        // back 버튼 누르면 장애처리 조회 화면으로 돌아감
        /*iv_back= findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, Error_History_Activity.class);
                startActivity(intent);
            }
        });


        //이미지에 네비게이션 뷰 달기
        drawer= findViewById(R.id.drawer_layout);
        nav= findViewById(R.id.nav);
        menu_btn= findViewById(R.id.iv_nav_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_camera:
                        Intent intent_barcode= new Intent(ReleaseRequestActivity.this, Barcode_bus_Activity.class);
                        startActivity(intent_barcode);
                        break;
                    case R.id.reserve_item_give:
                        Intent intent_reserve= new Intent(ReleaseRequestActivity.this, Barcode_garage_input_Activity.class);
                        break;
                    case R.id.release_request:
                        Intent intent_release= new Intent(ReleaseRequestActivity.this, ReleaseRequestActivity.class);
                        startActivity(intent_release);
                        break;
                    case R.id.warehousing_request:
                        Intent intent_warehouseing= new Intent(ReleaseRequestActivity.this, WarehousingActivity.class);
                        startActivity(intent_warehouseing);
                        break;
                }
                drawer.closeDrawer(nav);
                return false;
            }
        });*/



        //지부- 스피너.. 이건가여 ?네네 저기 dep_name 들어간게 거기서 아이디값 가져와서 넣어주시면 로그인할떄 app 에 저장해버리는 기능 sharedPreference
        spinnerArea= findViewById(R.id.spinner_area);
        ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        emp_id= pref.getString("emp_id",""); // 저장된 emp_id  가져오기
        Call<List<TestAllVO>> Call1= erp1.DepNameList(emp_id); // <- 이걸 로그인한 사람의 아이디로 변경해야하는거죠..아하 어떻게 변경해요? 지금은 그냥 String타입 "emp_id" 이걸 던지죠 ? 네 저걸 그 네 이거에 저정된 id를 불러와서
        // 저기에 넣어주면 되는거에여. 그 아이디가 이미 저장이 되어있나요? 로그인할때 저장하는 부분이 있어요
        new DepName().execute(Call1);


        //분류- 스피너..
        spinnerTerminal= findViewById(R.id.spinner_terminal);

        //출고위치- 스피너..
        spinner_release_location= findViewById(R.id.spinner_release_location);


        //현재 날짜 보여주기
        tvShowDate= findViewById(R.id.tv_show_date);
        long now= System.currentTimeMillis();
        Date date= new Date(now);
        SimpleDateFormat mFormat= new SimpleDateFormat("yyyy/MM/dd");
        String stCurrentDate= mFormat.format(date);
        tvShowDate= findViewById(R.id.tv_show_date);
        tvShowDate.setText(stCurrentDate);




        //캘린더 레이아웃 (출고요청일, 날짜, 아이콘)을 클릭하면 밑에 onDateSet 라는 함수가 실행되어 선택된 날짜로 텍스트를 변경해줌
        calendar_layout= findViewById(R.id.calendar_layout);
        calendar_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DialogFragment datePicker= new DatePickerFragment();        //밑에
                datePicker.show(getSupportFragmentManager(), "date picker");*/
            }
        });






    }//onCreate...


    // 지부 선택...
    public class DepName extends AsyncTask<Call, Void, List<TestAllVO>>{

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
        protected void onPostExecute(final List<TestAllVO> testAllVO) {
            super.onPostExecute(testAllVO);

            if(testAllVO != null){
                List<String> spinner_array = new ArrayList<>();
                spinner_array.add("선택");

                //커스텀 토스트 설정 ...
                LayoutInflater inflater= getLayoutInflater();
                View layout= inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custum_toast_layout));
                TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                tv_custom_toast.setText("지부를 선택하세요");
                final Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0,200);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                for(int i=0; i < testAllVO.size(); i++){
                    spinner_array.add(testAllVO.get(i).getDep_name());
//                    Toast.makeText(mContext, testAllVO.toString()+"사이즈: "+testAllVO.size(), Toast.LENGTH_SHORT).show();
                }
                spinnerArea.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));  //스피너 목록이 생김
                spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {                  //스피너의 각 아이템을 클릭하면..
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        // 지부 스피너 선택한 화면에 보이는 String 값 select_dep_name     //select_dep_name = 선택한 지부코드?
                        //String select_dep_name = spinnerArea.getSelectedItem().toString();
                        String select_dep_name = spinnerArea.getSelectedItem().toString();
                        if(select_dep_name != "선택"){
//                            String barcode_dep_id = ""; // <- 이거 삭제한 이유는 위에 전역변수랑 변수명이 똑같아서 삭제했어요
                            for(int j=0; j < testAllVO.size(); j++){
                                // 선택한 String 값 == 리스트 의 지부명과 동일할때 if문
                                if(select_dep_name == testAllVO.get(j).getDep_name()){
                                    //동일한 리스트이므로 여기서 getBarcode_dep_id 한다
                                    //선택한 지부의 값 => (getBarcode_dep_id) 한 값을 [전역변수= String barcode_dep_id]에 값을 담는다
                                    barcode_dep_id = testAllVO.get(j).getBarcode_dep_id();
                                }
                            }
//                             분류 스피너 생성 ->  DepName() 안에서 TestList() 실행시키기...
                            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<TestAllVO>> test_call= erp.InfraList(barcode_dep_id);
                            new ReleaseRequestActivity.TestList().execute(test_call); //test_call 이게 뭐징 하고 보면

                            // 지부 선택했을때 해야지 지부 선택값이 들어가게 하기 위해 지부선택한 곳에서 release_location()로 넘겨주기..
                            // 출고 스피너 생성 emp_id는 아마 전역으로 바꾸시면될것같아요
                            // 이렇게 한 이유는 아까 출고 쿼리를 보니까 선택한 location_id (지부_id) 가 들어가는데
                            // 그래서 지부 스피너 선택 이벤트 발생하는곳에서 출고스피너 생성입니다.
                            ERP_Spring_Controller erp2= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<TestAllVO>> call2= erp2.release_location(emp_id, barcode_dep_id);  //그럼 여기다 erp2.Dename() 이 말씀ㅇ딘가요?
                            new release_location().execute(call2);
                        }

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

            }

        }
    }





    // 단말기 분류
    class TestList extends AsyncTask<Call, Void, List<TestAllVO>>{

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
        protected void onPostExecute(final List<TestAllVO> TestAllVO) {
            super.onPostExecute(TestAllVO);
            if(TestAllVO != null){
                /////// 스피너에 들어갈 array를 만들어준다  ////////
                List<String> spinner_array = new ArrayList<>();
                // 스피너 제일 처음 선택값 push
                spinner_array.add("선택");

                //커스텀 토스트 설정...
                LayoutInflater inflater= getLayoutInflater();
                View layout= inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custum_toast_layout));
                TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                tv_custom_toast.setText("분류와 입고를 선택하세요");
                final Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0,200);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                // array에 값 push
                for(int i=0; i < TestAllVO.size(); i++){
                    spinner_array.add(TestAllVO.get(i).getUnit_group());   //단말기 분류 이름 받기.. ex; AFC1.5, AFC2.0, AFC3.0 ....
                }
//                // mContext 전역변수로 사용하는 이유는 레트로핏은 AsyncTask를 extends 해서 백그라운드에서 돌아서 this가 안먹힘
                spinnerTerminal.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_dropdown_item_1line,spinner_array));
                spinnerTerminal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String select_unit_group= spinnerTerminal.getSelectedItem().toString();  // 선택된 아이템

                        Log.d("이미 담겨져있음 ",barcode_dep_id);
                        Log.d("분류 선택값 ",select_unit_group);        //얘는 왜 전역변수로 안함??

                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<TestAllVO>> call= erp.selected_unit_status(barcode_dep_id, select_unit_group);   //아뇨 이건 맞죠 분류 쿼리니까 select_unit_group 이걸 분류 쿼리에서 만 쓰여서 전역변수로 바꿀필요가 없어요
                        new ReleaseRequestActivity.selected_unit_status().execute(call);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        }
    }

    //선택 현황
    // 단말기 분류 스피너 아이템 선택하고 나서.. 리사이클러뷰에 붙이기
    public class selected_unit_status extends AsyncTask<Call, Void, List<TestAllVO>> {
        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {      // testAllVOS 의 타입은 List<TestAllVO>
            super.onPostExecute(testAllVOS);
                Log.d("test_unit_code :",""+testAllVOS.size());
            items_selected = new ArrayList<>();                       //선택현황 리사이클러뷰 아이템 array
            for(int i=0; i < testAllVOS.size(); i++){                 // testAllVOS <- 결과 list 를 for 문 돌려서 리사이클러 뷰 item 생성
                items_selected.add(new selectedStatusItems( testAllVOS.get(i).getUnit_explain(), testAllVOS.get(i).getUnit_cnt(),  testAllVOS.get(i).getUnit_code(), testAllVOS.get(i).getRep_unit_code()));

                //그럼 여기서 unit_code 변수를 만들어서 어뎁터에 전달해서 어뎁터에서 클릭 이벤트 발생할때 재고 리스트를 뿌리는거 라고 저는 생각하는데 ㅇ떻게 생각하시나요.
                //Log.d("지부 선택값",barcode_dep_id);
            }
            // array 어뎁터에 던짐
            adapter_selected= new SelectedStatusAdapter(mContext, items_selected);
            recyclerView_selected= findViewById(R.id.recyclerview_selected);
            recyclerView_selected.setLayoutManager(new GridLayoutManager(mContext,2));   //Grid로 설정, span 설정
            recyclerView_selected.setAdapter(adapter_selected);       //리사이클러뷰에 어댑터 붙이기..

            //커스텀 토스트 설정...
            /*LayoutInflater inflater= getLayoutInflater();
            View layout= inflater.inflate(R.layout.custom_toast2, (ViewGroup)findViewById(R.id.custum_toast_layout));
            TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
            tv_custom_toast.setText("원하는 단말기를 클릭하세요");
            final Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP, 250,120);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();*/


            // 4. 마지막으로, 액티비티(or 프래그먼트)에서 커스텀 리스너 객체를 생성하여 어댑터에 전달
            adapter_selected.setmListener(new SelectedStatusAdapter.OnitemClickListener() {
                @Override
                public void onItemClick(View v, int post) {
                    String tag_test = (String) v.getTag();      //어댑터에서 setTag 한것 불러오기...
                    String tt[] = tag_test.split("&");    //tt[0]= unitCode, tt[1]= repUnitCode
                    //tv_tag_test = findViewById(R.id.tv_tag_test);
                    //tv_tag_test.setText(tag_test);
                    Log.d("value ","unit_code : "+tt[0] + ", rep_unit_code : "+tt[1]);
                    unitCode_1 = tt[0];
                    repUnitCode_2 = tt[1];   //전역변수로 만들어서 0번방 1번방 각각 넣어줘 select_stock_list(unitCode, repUnitCode)로 전달 ...->안그래도됨??
                    //tv_tag_test.setText(tag_test);




                    // 여기서 이제 barcode_dep_id , unit_code :" +tt[0] rep_unit_code : " + tt[1] 로 재고리스트 조회   //그럼 이제 아이템을 넣어줘야되네요?
                    // 재고리스트 조회해서 리사이클러뷰 만들어주면 끝인데 지금
                    // 뭔가 수량이 많은것같긴해요 오잉 여긴또 10개씩이네 아까 많이뜬거 한번 확인해보셔요 쿼리가 잘못된거일수도있으니 네넹
                    // 강북 AFC2.0 할때 10개씩뜸
                    // 이렇게 하면 눌렀을때 한번에 좌라락 네 그러내ㅔ요 , ㅜ연습이 조금 필요하겠네요
                    // 여기 전까지 한번 연습을 계속해보셔요 어차피 마지막에는 재고리스트 뿌리는거니까 네네
                    // tt[0] tt[1] 이 필요한곳이 재고리스트 를 조회 할때 필요한거입니다



                    ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                    //Call<List<TestAllVO>> call= erp.select_stock_list(barcode_dep_id,"unit_code","rep_unit_code");   //이렇게하면 String "" 으로만 가니까..
                    Call<List<TestAllVO>> call= erp.select_stock_list(barcode_dep_id, unitCode_1, repUnitCode_2);     //tt[0]= unitCode, tt[1]= repUnitCode   //이렇게 파라미터로 넘기려고 했어요
                    // select_stock_list 여기에 지금  barcode_dep_id, tt[0], tt[1], tt[2], tt[3] 이걸 넣었잖아여 ?네 select_stock_list 요게 어떤거죠
                    // 그쳐 ?네 그런데 Call 이걸 보니까 변수명 call = 하고 erp.select_stock_list 이렇ㄱ ㅔ있네여네
                    // 자 erp.select_stock_list 이걸 이제 보면
                    // 이제 call 이게 어떤건지 감 오시져 ?네네
                    //그걸

                    //저기에서 +item.unitName+"&"+item.unitTotalNum) 이건 어디서 사용하는거죠
                    // ReleaseRequestActivity에 있는  select_stock_list 에다가  execute 하고 안에 call 이걸 넣었죠
                    // 해석하면 select_stock_list 에서 execute 하는데 어떤걸? call 이걸 하는거입니다 네네
                    // 그럼 저기로 가보져
                    new ReleaseRequestActivity.select_stock_list().execute(call);

                }
            });
            adapter_selected.notifyDataSetChanged();

        }
    }




    //선택한 재고 리스트..... 리사이클러뷰에 붙이기
    // 여기가 스프링이랑 통신하기 위해 execute 하는곳이져 ?네
    // 거기에 필요한게 Call 요친구고 네 그친구를 위에서 전달하는중이고여
    // 여기로 와서 execute 작업은 그때 doInBackground 여기서 하고
    // 결과는 onPostExecute ㅇ여기서 받는걸로 설명드렸져 ?네
    // 그럼 doInBackground 에서 execute 를 Call<List<TestAllVO>> call = calls[0]; 이걸 하는건데
    // 결국엔 이게 스프링쪽이겠져 ?네
    // 그럼 위에 파라미터 5개는 어디서 받을까요 스프링이요
    // 네 맞습니다
    //요기서 파라미터를 받는게 아니고
    // onPostExecute 여기는 스프링 프로젝트의 결과 값을 받는건데
    // 여기서 결과값 은 무엇이냐
   public class select_stock_list extends AsyncTask<Call, Void, List<TestAllVO>>{   //여기요

        // 여기서 execute 해서 결과를 Response<List<TestAllVO>> 이걸로 받죠 ? 네
        // 그래서 return response.body(); 이렇게 하는데 이게 무슨뜻이냐면
        // onPostExecute(response.body();)  이거라고 보시면됩니다 네네
        // 그래서 onPostExecute 이친구가 결과값을 받을수있고여
        // 그럼 결과값 타입은 무엇일까요 똑같이 List<TestAllVO> 이거 아닌가요 네
        // 맞아요 그럼 저걸 보면 스프링이랑 똑같이 되어있껬져 ? 네네
        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call = calls[0];     //스프링 통신 Call
            try{
                Response<List<TestAllVO>> response = call.execute();     //스프링과 통신하기위해 execute
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // 여기가 return 되는곳
        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {       //스프링과 통신한 결과값 onPostExecute 여기서 받기
            super.onPostExecute(testAllVOS);  //testAllVOS = 결과값
            // 타입 = TestAllVO
            // 여기는 재고리스트 리사이클러 만드는곳
            /*Toast.makeText(mContext, "재고리스트 함수실행"+testAllVOS.size(), Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, "지부선택값ㅋㅋㅋㅋㅋㅋ"+barcode_dep_id, Toast.LENGTH_SHORT).show();*/
            /*Log.d("받아온 unitCode_1 : ", unitCode_1);
            Log.d("받아온 repUnitCode_2 : ", repUnitCode_2);*/

            items_stock = new ArrayList<>();
            items_stock_2 = new ArrayList<>();

            for(int i=0; i<testAllVOS.size(); i++){
                items_stock.add(new StockListItems(testAllVOS.get(i).getRnum(), testAllVOS.get(i).getUnit_ver(), testAllVOS.get(i).getUnit_id(), false));
                //items_stock_2.add(new StockListItems(testAllVOS.get(i).getRnum(), testAllVOS.get(i).getUnit_ver(), testAllVOS.get(i).getUnit_id(), false));
                getUnitId= testAllVOS.get(i).getUnit_id();
            }

            adapter_stock_2= new StockListAdapter2(mContext, items_stock_2, items_stock, adapter_stock
            );
            adapter_stock= new StockListAdapter(mContext, items_stock, items_stock_2, adapter_stock_2);
            recyclerView_stock_list= findViewById(R.id.recyclerView_stock_list);
            recyclerView_stock_list.setAdapter(adapter_stock);


            recyclerView_stock_list_2= findViewById(R.id.recyclerView_stock_list2);
            recyclerView_stock_list_2.setAdapter(adapter_stock_2);

            //adapter_stock.notifyDataSetChanged();
        }

    }// select_stock_list class...



    //선택한 재고리스트(2) 확인.....리사이클러뷰에 붙이기
    public class select_stock_list_2 extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);

            /*items_stock_2= new ArrayList<>();
            for (int i=0; i<testAllVOS.size(); i++){
                items_stock_2.add(new StockListItems(testAllVOS.get(i).getRnum(), testAllVOS.get(i).getUnit_ver(), testAllVOS.get(i).getUnit_id(), false));

            }
            adapter_stock_2 = new StockListAdapter2(mContext,items_stock ,items_stock_2);
            recyclerView_stock_list_2 = findViewById(R.id.recyclerView_stock_list2);
            recyclerView_stock_list_2.setAdapter(adapter_stock_2);*/


        }
    }





    //출고위치
    public class release_location extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {      //스프링쪽과 통신
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<TestAllVO> testAllVOS) {      // 스프링 프로젝트의 결과값을 받는 곳
            super.onPostExecute(testAllVOS);

            if(testAllVOS != null){
                List<String> spinner_array = new ArrayList<>();
                spinner_array.add("선택");
                //Toast.makeText(mContext, "출고위치를 선택하세요", Toast.LENGTH_SHORT).show();
                for (int i=0; i < testAllVOS.size(); i++){
                    spinner_array.add(testAllVOS.get(i).getDep_name());
                }
                spinner_release_location.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));
                spinner_release_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       /* String select_dep_name = spinner_release_location.getSelectedItem().toString();
                        if(select_dep_name != "선택"){
                            for(int j=0; j<testAllVOS.size(); j++){
                                if (select_dep_name == testAllVOS.get(i).getDep_name()){
                                    barcode_dep_id = testAllVOS.get(i).getBarcode_dep_id();     //아이템 선택한 값 어덯게 처리?
                                }
                            }
                        }*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                LayoutInflater inflater= getLayoutInflater();
                View layout= inflater.inflate(R.layout.custom_toast2, (ViewGroup)findViewById(R.id.custum_toast_layout));
                TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                tv_custom_toast.setText("원하는 단말기를 클릭하세요");
                final Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP, 250,120);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

            }
        }
    }



    // 캘린터 클릭- 선택된 날짜 보여주기 설정
    /*@Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        *//*long now= System.currentTimeMillis();
        Date date= new Date(now);
        SimpleDateFormat mFormat= new SimpleDateFormat("yyyy/MM/dd");
        String stCurrentDate= mFormat.format(date);
        tvShowDate= findViewById(R.id.tv_show_date);
        tvShowDate.setText(stCurrentDate);*//*


        Calendar c= Calendar.getInstance(Locale.KOREA);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, dayOfMonth);
        String currentDateString= DateFormat.getDateInstance().format(c.getTime());
        tvShowDate= findViewById(R.id.tv_show_date);
        tvShowDate.setText(currentDateString);
    }*/


    //장비리스트 retrofit   //여기서부터 하시면 되요. 제가잠깐 테스트 해본거라
    private class getfield_trouble_error_type extends AsyncTask<Call,Void,List<Trouble_CodeVo>>{
        // 실질적으로 Call이 돌아가는 부분
        @Override
        protected List<Trouble_CodeVo> doInBackground(Call... calls) {
            Call<List<Trouble_CodeVo>> call = calls[0];             //call[0]은 뭐죠? -> Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type("01","1"); 요거입니다 짝짝 ㅎㅎ
            // execute 는 try catch 로 예외처리를 해줘야함
            try{
                // execute 해서 결과 값 받는 변수
                Response<List<Trouble_CodeVo>> response = call.execute();
                return response.body();
            }catch (Exception e){                                   //Toast로 확인가능하나요? 토스트로 어떤거요 ? 요렇게 하면 에러떳을때 토스트로 나오고 로그는
                e.printStackTrace();
//                Toast.makeText(ReleaseRequestActivity.this, "fail"+e.toString(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        // doInBackground 의 return 이 넘어오는곳
        @Override
        protected void onPostExecute(List<Trouble_CodeVo> trouble_codeVos) {
            super.onPostExecute(trouble_codeVos);
            if(trouble_codeVos != null){
                for (Trouble_CodeVo i : trouble_codeVos){
                    Log.d("result ", i.getUnit_name());
                }
            }
        }
    }





    // 재고리스트 전체/ 삭제 버튼.....
    public void clickDeleteStockList(View view) {
        Toast.makeText(mContext, "재고 리스트 삭제.....", Toast.LENGTH_SHORT).show();
    }

    public void clickAllStockList(View view) {
        Toast.makeText(mContext, "재고 리스트 전체 선택..........", Toast.LENGTH_SHORT).show();

    }




    // 출고신청 -- [취소버튼]
    public void clickCancel(View view) {
        Toast.makeText(mContext, "출고신청을 취소하였습니다.", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // 출고신청 -- [완료버튼]
    public void clickOK(View view) {
        //다이얼로그 띄우기
        builder= new AlertDialog.Builder(this);
        builder.setTitle("출고 신청").setMessage("예약을 완료 하시겠습니까?");
        builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // DB에 데이터 insert, update 하기....???
                //ERP_Spring_Controller= erp....
                Toast.makeText(mContext, "신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                //완료버튼을 누르면 예약상태 업데이트 및... (UPDATE, INSERT...)
                ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<TestAllVO>> call= erp.booking(unitCode_1, repUnitCode_2, getUnitId, barcode_dep_id, emp_id);
                new ReleaseRequestActivity.booking_status().execute(call);

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, "신청이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }


    public class booking_status extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {      //스프링쪽과 통신
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<TestAllVO> testAllVOS) {      // 스프링 프로젝트의 결과값을 받는 곳
            super.onPostExecute(testAllVOS);


        }
    }














    //스피너 아이템 클릭
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),  text+" 선택", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}
