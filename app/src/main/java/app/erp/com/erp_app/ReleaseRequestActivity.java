
package app.erp.com.erp_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.DialogFragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{
    private ImageView iv_back;

    private SharedPreferences sp_barcode_dep_id_out, sp_response_dep_id_out, sp_request_dep_id_out, sp_current_date_out;  //response-상단지부, request-하단지부

    //선택 현황 리사이클러뷰
    private RecyclerView recyclerView_selected;
    private ArrayList<selectedStatusItems> items_selected;
    private SelectedStatusAdapter adapter_selected;

    //재고 리스트-(1) 리사이클러뷰
    private RecyclerView recyclerView_stock_list;
    private ArrayList<StockListItems> items_stock;   //같은 클래스 어레이아이템 공유
    private ArrayList<StockListItems> items_stock_2;
    static StockListAdapter adapter_stock;
    //재고 리스트-(2) 리사이클러뷰   (1과 같은 ArrayList items 사용)
    private RecyclerView recyclerView_stock_list_2;
    private StockListAdapter2 adapter_stock_2;

    //네비게이션 메뉴
    private DrawerLayout drawer;
    private NavigationView nav;
    private ImageView menu_btn;

    private FloatingActionButton fab;


    //지부/분류/출고위치 스피너
    private Spinner spinnerArea;     //지부
    private Spinner spinnerTerminal; //분류
    private Spinner spinner_release_location;  //출고위치 스피너
    private Button btn_area; //테스트용 버튼
    private TextView tv_area; //테스트용 텍스트뷰

    SharedPreferences pref;
    static String emp_id;     // 아이디 전역변수로..
    static String barcode_dep_id; // 선택한 지부 id- 전역변수로...
    static String emp_dep_id;    //전역변수로..  //response_dep_id (응답을 받는 곳)
    static String select_dep_name; // 지부 스피너 값
    static String select_unit_group;  //분류선택값
    static String select_release_location;
    static String selected_location_id;  //입고 스피너 선택값

    static String unitCode_1;
    static String repUnitCode_2;
    static String getUnitId;
    static String getUn_yn;
    static String getIn_yn;


    //특이사항- 요청 메세지..
    private EditText et_msg;
    static String notice;


    //캘린터
    static TextView tvShowDate;
    private RelativeLayout calendar_layout;
    static String current_date_value;  //날짜 전역변수 현재날자 값 전달

    private AlertDialog.Builder builder;

    //Context  전역변수로 만들어주기   //레트로핏은 AsyncTask를 extends 해서 백그라운드에서 돌아서 this가 안먹힘
    private Context mContext;
    private Object Context;



    //입고신청 옵션메뉴 버튼
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= this.getMenuInflater();
        menuInflater.inflate(R.menu.warehousing_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.warehousing_btn:
                Toast.makeText(mContext, "입고신청 페이지로 이동", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ReleaseRequestActivity.this, InOutStatusActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_release_request);

        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ReleaseRequestActivity.this, WarehousingActivity.class);
                startActivity(i);
            }
        });


        mContext = this;

        //로그인 정보 가져오기
        pref = getSharedPreferences("user_info" , MODE_PRIVATE);


        //지부- 스피너.. 이건가여 ?네네 저기 dep_name 들어간게 거기서 아이디값 가져와서 넣어주시면 로그인할떄 app 에 저장해버리는 기능 sharedPreference
        spinnerArea= findViewById(R.id.spinner_area);
        ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        emp_id= pref.getString("emp_id",""); // 저장된 emp_id  가져오기
        emp_dep_id= pref.getString("emp_dep_id","");

        Call<List<TestAllVO>> Call1= erp1.DepNameList(emp_id, emp_dep_id); // <- 이걸 로그인한 사람의 아이디로 변경해야하는거죠..아하 어떻게 변경해요? 지금은 그냥 String타입 "emp_id" 이걸 던지죠 ? 네 저걸 그 네 이거에 저정된 id를 불러와서
        // 저기에 넣어주면 되는거에여. 그 아이디가 이미 저장이 되어있나요? 로그인할때 저장하는 부분이 있어요
        new DepName().execute(Call1);


        //분류- 스피너..
        spinnerTerminal= findViewById(R.id.spinner_terminal);

        //출고위치- 스피너..
        spinner_release_location= findViewById(R.id.spinner_release_location);


        //캘린더 레이아웃 (출고요청일, 날짜, 아이콘)을 클릭하면 밑에 onDateSet 라는 함수가 실행되어 선택된 날짜로 텍스트를 변경해줌
        calendar_layout= findViewById(R.id.calendar_layout);
        calendar_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker= new DatePickerFragment();        //밑에
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        long now= System.currentTimeMillis();
        Date date= new Date(now);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        String currentDate= sdf.format(date);
        tvShowDate= findViewById(R.id.tv_show_date);
        tvShowDate.setText(currentDate);

        current_date_value= currentDate;

        /*SharePreference로 req_date 데이터 값저장하고 입고대상확인 버튼을 눌렀을때 이 데이터값 뽑아쓰기 */
        sp_current_date_out= getSharedPreferences("sp_current_date_out", MODE_PRIVATE);
        SharedPreferences.Editor editor= sp_current_date_out.edit();
        editor.putString("currentDateValueOut", current_date_value);
        Log.d("currentDateValueOut ============>>>> " ,sp_current_date_out+"");
        editor.commit();

        et_msg= findViewById(R.id.et_msg);
        //notice= et_msg.getText().toString();     // 액티비티가 생성될때 입력할수없어 null
        /*Button btn_msg= findViewById(R.id.btn_msg);
        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice= et_msg.getText().toString();   //여기는 됨
                TextView tv_msg= findViewById(R.id.tv_msg);
                tv_msg.setText(notice);
            }
        });*/

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
                View layout= inflater.inflate(R.layout.custom_toast_complete, (ViewGroup)findViewById(R.id.custum_toast_layout));
                TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                tv_custom_toast.setText("지부를 선택하세요");
                final Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0,80);
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
                        select_dep_name = spinnerArea.getSelectedItem().toString();
                        if(select_dep_name != "선택"){
//                            String barcode_dep_id = ""; // <- 이거 삭제한 이유는 위에 전역변수랑 변수명이 똑같아서 삭제
                            for(int j=0; j < testAllVO.size(); j++){
                                // 선택한 String 값 == 리스트 의 지부명과 동일할때 if문
                                if(select_dep_name == testAllVO.get(j).getDep_name()){
                                    //동일한 리스트이므로 여기서 getBarcode_dep_id 한다
                                    //선택한 지부의 값 => (getBarcode_dep_id) 한 값을 [전역변수= String barcode_dep_id]에 값을 담는다
                                    barcode_dep_id = testAllVO.get(j).getBarcode_dep_id();

                                    /*SharePreference로 barcode_dep_id 데이터 값저장하고 입고대상확인 버튼을 눌렀을때 이 데이터값 뽑아쓰기 */
                                    sp_barcode_dep_id_out= getSharedPreferences("sp_barcode_dep_id_out", MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sp_barcode_dep_id_out.edit();
                                    editor.putString("barcodeDepIdValueOut", testAllVO.get(j).getBarcode_dep_id());
                                    Log.d("barcodeDepIdValueOut =====================>>>>>>>>>>>>>>", testAllVO.get(j).getBarcode_dep_id()+"");
                                    editor.commit();
                                }
                            }
//                             분류 스피너 생성 ->  DepName() 안에서 TestList() 실행시키기...
                            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<TestAllVO>> test_call= erp.InfraList(barcode_dep_id, emp_dep_id);
                            new ReleaseRequestActivity.TestList().execute(test_call); //test_call 이게 뭐징 하고 보면

                            // 지부 선택했을때 해야지 지부 선택값이 들어가게 하기 위해 지부선택한 곳에서 release_location()로 넘겨주기..
                            // 출고 스피너 생성 emp_id는 아마 전역으로
                            // 이렇게 한 이유는 아까 출고 쿼리를 보니까 선택한 location_id (지부_id) 가 들어가는데
                            // 그래서 지부 스피너 선택 이벤트 발생하는곳에서 출고스피너 생성
                            ERP_Spring_Controller erp2= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<TestAllVO>> call2= erp2.release_location(emp_id, barcode_dep_id, emp_dep_id);
                            new release_location().execute(call2);
                            Log.d("emp_id   $    barcode_dep_id   $   emp_dep_id",emp_id+"/ "+ barcode_dep_id+"/ "+emp_dep_id);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

        }
    }



    // 출고대상 목록 최종확인
    public void clickCheck(View view) {

        //출고대상 확인버튼 누르면서 분류, 입고스피너 초기화하기
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> test_call= erp.InfraList(barcode_dep_id, emp_dep_id);
        Log.d("출고대상 코드 확인하기===> ", barcode_dep_id+"/ "+emp_dep_id);
        new TestList().execute(test_call);

        // 액티비티를 다이얼로그처럼 보이게...   // [출고대상확인] 액티비티로 이동
        Intent intent= new Intent(mContext, DialogFinalRequestListActivity.class);
        startActivity(intent);
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

            Log.d("barcode_dep_id확인********************************* ",barcode_dep_id+" tt");
            Log.d("barcode_dep_id확인********************************* ",emp_id+" tt");
            Log.d("barcode_dep_id확인********************************* ",emp_dep_id+" tt");

            if(TestAllVO != null){
                /////// 스피너에 들어갈 array를 만들어준다  ////////
                List<String> spinner_array = new ArrayList<>();
                // 스피너 제일 처음 선택값 push
                spinner_array.add("선택");

                //커스텀 토스트 설정...
                LayoutInflater inflater= getLayoutInflater();
                View layout= inflater.inflate(R.layout.custom_toast_complete, (ViewGroup)findViewById(R.id.custum_toast_layout));
                TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                tv_custom_toast.setText("분류와 입고를 선택하세요");
                final Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0,80);
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
                        select_unit_group= spinnerTerminal.getSelectedItem().toString();  // 선택된 아이템

                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<TestAllVO>> call= erp.selected_unit_status(barcode_dep_id, select_unit_group);   //아뇨 이건 맞죠 분류 쿼리니까 select_unit_group 이걸 분류 쿼리에서 만 쓰여서 전역변수로 바꿀필요가 없어요
                        new ReleaseRequestActivity.selected_unit_status().execute(call);


                        //분류아이템을 선택할때 입고리스트 초기화해야함...
                        items_stock= new ArrayList<>();
                        items_stock_2= new ArrayList<>();
                        adapter_stock_2= new StockListAdapter2(mContext, items_stock_2, items_stock, adapter_stock);
                        adapter_stock= new StockListAdapter(mContext, items_stock, items_stock_2, adapter_stock_2);
                        recyclerView_stock_list= findViewById(R.id.recyclerView_stock_list);
                        recyclerView_stock_list.setAdapter(adapter_stock);
                        recyclerView_stock_list_2= findViewById(R.id.recyclerView_stock_list2);
                        recyclerView_stock_list_2.setAdapter(adapter_stock_2);
                        items_stock.clear();
                        adapter_stock_2.notifyDataSetChanged();
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
                items_selected.add(new selectedStatusItems( testAllVOS.get(i).getUnit_explain(), testAllVOS.get(i).getUnit_cnt(), testAllVOS.get(i).getUnit_code(), testAllVOS.get(i).getRep_unit_code()));
            }
            // array 어뎁터에 던짐
            adapter_selected= new SelectedStatusAdapter(mContext, items_selected);
            recyclerView_selected= findViewById(R.id.recyclerview_selected);
            recyclerView_selected.setLayoutManager(new GridLayoutManager(mContext,1));   //Grid로 설정, span 설정
            recyclerView_selected.setAdapter(adapter_selected);       //리사이클러뷰에 어댑터 붙이기..

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
                    // 여기서 이제 barcode_dep_id , unit_code :" +tt[0] rep_unit_code : " + tt[1] 로 재고리스트 조회

                    /*선택현황 아이템을 클릭하고 스피너와 날짜값이 있는지 확인*/
                    if (select_dep_name.equals("선택")){
                        builder= new AlertDialog.Builder(mContext);
                        builder.setTitle("지부 정보부족").setMessage("지부 선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog= builder.create();
                        alertDialog.show();
                    }else if (select_unit_group.equals("선택")){
                        builder= new AlertDialog.Builder(mContext);
                        builder.setTitle("분류 정보부족").setMessage("분류 선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog= builder.create();
                        alertDialog.show();
                    }else if (select_release_location.equals("선택")) {
                        builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("입고 정보부족").setMessage("입고 선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else if (tvShowDate==null){
                        builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("요청날짜 정보부족").setMessage("출고신청 날짜선택을 완료하세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else {
                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<TestAllVO>> call= erp.select_stock_list(barcode_dep_id, unitCode_1, repUnitCode_2);
                        new ReleaseRequestActivity.select_stock_list().execute(call);
                    }
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
    // 그럼 위에 파라미터 5개는 어디서 받을까요 스프링이요 // 네 맞습니다
    // 요기서 파라미터를 받는게 아니고
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
                Log.d("barcode",testAllVOS.get(i).getBarcode_dep_id()+"ttt");
                Log.d("emp_id   kkkkkkkkkkk", emp_id);
                items_stock.add(new StockListItems(testAllVOS.get(i).getRnum()
                                                 , testAllVOS.get(i).getUnit_ver()
                                                 , testAllVOS.get(i).getUnit_id()
                                                 , false                //checkbox getter/setter는 안필요한가? o o
                                                 , testAllVOS.get(i).getUn_yn()
                                                 , testAllVOS.get(i).getIn_yn()
                                                 , emp_id
                                                 , testAllVOS.get(i).getRep_unit_code()
                                                 , testAllVOS.get(i).getUnit_code()
                                                 , barcode_dep_id     //getBarcode_dep_id
                                                 ,current_date_value                  //getReq_date
                                                 ,notice
                                                 ,selected_location_id   //getRequest_dep_id  (하단지부)
                                                 ,barcode_dep_id));  //getResponse_dep_id  (상단지부)

                Log.d("current_date_value OR req_date ::::::::::::::::",current_date_value+"");  //잘나옴
                Log.d("notice  ::::::::::::",notice+"");                                         //잘나옴
                Log.d("selected_location_id  OR request_dep_id",selected_location_id+"");        //잘나옴
                Log.d("barcode_dep_id OR response_dep_id",barcode_dep_id+"");                    //잘나옴
            }


            adapter_stock_2= new StockListAdapter2(mContext, items_stock_2, items_stock, adapter_stock);
            adapter_stock= new StockListAdapter(mContext, items_stock, items_stock_2, adapter_stock_2);

            recyclerView_stock_list= findViewById(R.id.recyclerView_stock_list);
            recyclerView_stock_list.setAdapter(adapter_stock);

            recyclerView_stock_list_2= findViewById(R.id.recyclerView_stock_list2);
            recyclerView_stock_list_2.setAdapter(adapter_stock_2);
            adapter_stock_2.notifyDataSetChanged();

        }
    }// select_stock_list class...



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

            Log.d("emp_dep_id==================================================", testAllVOS.toString()+"   777777777777777777777777777");    //나옴

            if(testAllVOS != null){
                List<String> spinner_array = new ArrayList<>();
                spinner_array.add("선택");
                //Toast.makeText(mContext, "출고위치를 선택하세요", Toast.LENGTH_SHORT).show();
                for (int i=0; i < testAllVOS.size(); i++){
                    spinner_array.add(testAllVOS.get(i).getDep_name());
                    Log.d("getDep_name()==================================================", testAllVOS.get(i).getDep_name()+"   88888888888888888888888888888");
                }

                spinner_release_location.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinner_array));
                spinner_release_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        select_release_location= spinner_release_location.getSelectedItem().toString();
                        if (select_release_location != "선택"){
                            for (int j=0; j<testAllVOS.size(); j++){
                                if (select_release_location == testAllVOS.get(j).getDep_name()){
                                    //김민수씨 개인지부 값 (파라미터- request_dep_id)
                                    selected_location_id= testAllVOS.get(j).getLocation_id();
                                    Toast.makeText(mContext, "입고 스피너- 개인지부값: "+selected_location_id, Toast.LENGTH_SHORT).show();

                                    sp_request_dep_id_out= getSharedPreferences("sp_request_dep_id_out", MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sp_request_dep_id_out.edit();
                                    editor.putString("requestDepIdOut", testAllVOS.get(j).getLocation_id());
                                    Log.d("requestDepIdOut =============>>>>>>>>>>>>>> ", testAllVOS.get(j).getLocation_id());
                                    editor.commit();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }



    // 캘린터 클릭- 선택된 날짜 보여주기 설정
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        tvShowDate= findViewById(R.id.tv_show_date);
        current_date_value= String.format("%4d-%02d-%02d", year, (month+1), dayOfMonth);
        tvShowDate.setText(current_date_value);
    }













    // 출고신청 -- [완료버튼]
    public void clickOK(View view) {

        notice= et_msg.getText().toString();

        Log.d("select_release_location111111111111111111111111111111111111111111111111111111111",select_release_location+"   11111111111");

        //작업완료 전 이벤트에 대해 체크하기..
        if (select_dep_name.equals("선택")){
            builder= new AlertDialog.Builder(this);
            builder.setTitle("지부 정보부족").setMessage("지부 선택을 완료하세요");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();
        /*}else if (select_unit_group.equals("선택")){
            builder= new AlertDialog.Builder(this);
            builder.setTitle("분류 정보부족").setMessage("분류 선택을 완료하세요");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();*/
        }else if (select_release_location.equals("선택")) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("입고 정보부족").setMessage("입고 선택을 완료하세요");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if (tvShowDate==null){
            builder = new AlertDialog.Builder(this);
            builder.setTitle("요청날짜 정보부족").setMessage("출고신청 날짜선택을 완료하세요");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            //다이얼로그 띄우기
            builder= new AlertDialog.Builder(this);
            builder.setTitle("출고 신청").setMessage("예약을 완료 하시겠습니까?");
            builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // DB에 데이터 insert, update 하기....???
                    //ERP_Spring_Controller= erp....

                    //커스텀 토스트 설정 ...
                    LayoutInflater inflater= getLayoutInflater();
                    View layout= inflater.inflate(R.layout.custom_toast_complete, (ViewGroup)findViewById(R.id.custum_toast_layout));
                    TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                    tv_custom_toast.setText("신청이 완료되었습니다.");
                    final Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0,80);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                    //스프링쪽에서 reg_date, reg_time 해줬으니 여기서 파라미터 필요하지 않음..
                    //완료버튼을 누르면 예약상태 업데이트 및... (INSERT, UPDATE...)
                    ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                    Call<Void> call = erp.AppInsertRequestList(emp_id, current_date_value, notice, selected_location_id, barcode_dep_id, emp_dep_id);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("emp_id", emp_id+ "$$$$$$$$$$$$$$$");
                            Log.d("current_date_value", current_date_value+ " $$$$$$$$$$$$$$$");
                            Log.d("notice", notice+ "$$$$$$$$$$$$$$$");
                            Log.d("selected_location_id", selected_location_id+ "$$$$$$$$$$$$$$$");
                            Log.d("barcode_dep_id", barcode_dep_id+ "$$$$$$$$$$$$$$$");
                            Log.d("emp_dep_id", emp_dep_id+ "$$$$$$$$$$$$$$$");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });

                    Intent intent= new Intent(mContext, InOutStatusActivity.class );
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //커스텀 토스트 설정 ...
                    LayoutInflater inflater= getLayoutInflater();
                    View layout= inflater.inflate(R.layout.custom_toast_cancel2, (ViewGroup)findViewById(R.id.custum_toast_layout));
                    TextView tv_custom_toast= layout.findViewById(R.id.tv_custom_toast);
                    tv_custom_toast.setText("신청이 취소되었습니다.");
                    final Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0,80);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();
        }
        

    }// clickOK 완료버튼

















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
