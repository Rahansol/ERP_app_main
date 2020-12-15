package app.erp.com.erp_app.callcenter;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import app.erp.com.erp_app.Barcode_My_list_Activity;
import app.erp.com.erp_app.Barcode_bus_Activity;
import app.erp.com.erp_app.Barcode_garage_input_Activity;
import app.erp.com.erp_app.Barcode_garage_output_Activity;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.Gtv_Error_Install_Activity;
import app.erp.com.erp_app.Help_Actaivity;
import app.erp.com.erp_app.InOutStatusActivity;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.New_Bus_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.ReleaseRequestActivity;
import app.erp.com.erp_app.ReserveItemRepairActivity;
import app.erp.com.erp_app.WarehousingActivity;
import app.erp.com.erp_app.Work_Report_Activity;
import app.erp.com.erp_app.document_care.ProJectMainActivity;
import app.erp.com.erp_app.error_history.Error_History_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Approval_Activity;
import retrofit2.Call;
import retrofit2.Response;
import test.TestActivity;

public class Call_Center_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener {

    TextView trouble_insert, trouble_care, trouble_clear;
    FragmentManager fm;
    FragmentTransaction ft;
    String page_check ;

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("user_info" , MODE_PRIVATE);
        String token_dep_code = pref.getString("dep_code","");
        String emp_id = pref.getString("emp_id","");
        String token = FirebaseInstanceId.getInstance().getToken();
        message_group(token_dep_code);

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<Boolean> call = erp.app_changeTokenId(emp_id , token);
        new Call_Center_Activity.Update_changeTokenId().execute(call);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        trouble_insert = (TextView)findViewById(R.id.trouble_insert);
        trouble_care = (TextView)findViewById(R.id.trouble_care);
        trouble_clear = (TextView)findViewById(R.id.trouble_clear);

        trouble_insert.setOnClickListener(this);
        trouble_care.setOnClickListener(this);
        trouble_clear.setOnClickListener(this);

        page_check = "list";
        Fragment fragment = new Fragment_trouble_list();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
//        ft.addToBackStack(null);
        ft.add(R.id.frage_change,fragment);
        ft.commit();
    }

    private class Update_changeTokenId extends AsyncTask<Call , Void , Boolean>{
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        page_check = "nav";

        if (id == R.id.nav_camera) {
            //차량 바코드 등록
            Intent i = new Intent(Call_Center_Activity.this , Barcode_bus_Activity.class);
            startActivity(i);
        }else if (id == R.id.reserve_item_give) {
            // 예비품 지급
            Intent i = new Intent(Call_Center_Activity.this , Barcode_garage_input_Activity.class);
            startActivity(i);
        }else if (id == R.id.reserve_item_return) {
            // 예비품 회수
            Intent i = new Intent(Call_Center_Activity.this , Barcode_garage_output_Activity.class);
            startActivity(i);
        }else if ( id == R.id.my_barcode_workList){
            // 내 바코드 부착내역
            Intent i = new Intent(Call_Center_Activity.this , Barcode_My_list_Activity.class);
            startActivity(i);
   /*     }else if (id == R.id.reserve_item_workList){
            // 에비품 지급내역
            Intent i = new Intent(Call_Center_Activity.this , Barcode_input_list_Activity.class);
            startActivity(i);*/
        }else if(id == R.id.new_bus_insert){
            // 버스등록
            Intent i = new Intent(Call_Center_Activity.this , New_Bus_Activity.class);
            startActivity(i);
        }
        else if(id == R.id.gtv_error_install){
            // GTV 가동률
            Intent i = new Intent(Call_Center_Activity.this , Gtv_Error_Install_Activity.class);
            startActivity(i);
        }
        else if(id == R.id.reserve_item_repair){
            Intent i = new Intent(Call_Center_Activity.this , ReserveItemRepairActivity.class);
            startActivity(i);
        }else if(id == R.id.work_report_btn){
            Intent i = new Intent(Call_Center_Activity.this , Work_Report_Activity.class);
            startActivity(i);
        }
        else if(id == R.id.project_work_btn){
            Intent i = new Intent(Call_Center_Activity.this , ProJectMainActivity.class);
            startActivity(i);
        }
        else if(id == R.id.trouble_serch_btn){
            Intent i = new Intent(Call_Center_Activity.this , Error_History_Activity.class);
            startActivity(i);
        }
        else if(id == R.id.over_work_btn){
            Intent i = new Intent(Call_Center_Activity.this , Over_Work_Activity.class);
            startActivity(i);
        }
        else if(id == R.id.over_work_approval_btn) {
            Intent i = new Intent(Call_Center_Activity.this, Over_Work_Approval_Activity.class);
            startActivity(i);
        }

        //테스트
        else if(id == R.id.test_menu){
            Intent i = new Intent(Call_Center_Activity.this , TestActivity.class);
            startActivity(i);
        }

        //단말기 입출고 현황 액티비티로 이동
        else if (id==R.id.inventory_in_out_status){
            Intent i= new Intent(Call_Center_Activity.this, InOutStatusActivity.class);
            startActivity(i);
        }


        //출고신청 액티비티로 이동
        else if(id == R.id.release_request){
            Intent i = new Intent(Call_Center_Activity.this, ReleaseRequestActivity.class);
            startActivity(i);
        }

        //입고신청 액티비티로 이동
        else if(id == R.id.warehousing_request){
            Intent i= new Intent(Call_Center_Activity.this, WarehousingActivity.class);
            startActivity(i);
        }





//        else if(id == R.id.camera_tset){
//            Intent i = new Intent(Call_Center_Activity.this , CameraTest2Activity.class);
//            startActivity(i);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        Fragment fragment = null;

        switch (v.getId()){
            case R.id.trouble_insert :
                page_check = "insert";
                fragment = new Fragment_trouble_insert();
                trouble_insert.setBackground(getResources().getDrawable(R.drawable.text_btn));
                trouble_care.setBackground(null);
                trouble_clear.setBackground(null);

                trouble_insert.setTextColor(getResources().getColor(R.color.green_text_color));
                trouble_clear.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_care.setTextColor(getResources().getColor(R.color.origin_color));
                break;
            case R.id.trouble_care :
                page_check = "list";
                fragment = new Fragment_trouble_list();

                trouble_insert.setBackground(null);
                trouble_care.setBackground(getResources().getDrawable(R.drawable.text_btn));
                trouble_clear.setBackground(null);

                trouble_insert.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_clear.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_care.setTextColor(getResources().getColor(R.color.green_text_color));
                break;
            case R.id.trouble_clear:
                page_check = "care";
                fragment = new Fragment_trobule_care_insert();
                trouble_insert.setBackground(null);
                trouble_care.setBackground(null);
                trouble_clear.setBackground(getResources().getDrawable(R.drawable.text_btn));

                trouble_insert.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_care.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_clear.setTextColor(getResources().getColor(R.color.green_text_color));
                break;
        }
        if(fragment != null){

            FragmentManager fmanager = getSupportFragmentManager();
            FragmentTransaction ftrans = fmanager.beginTransaction();
            ftrans.replace(R.id.frage_change,fragment);
            ftrans.commit();
        }
    }

    public void switchFragment (String top_head ){

        switch (top_head){
            case "insert":
                trouble_insert.setBackground(getResources().getDrawable(R.drawable.text_btn));
                trouble_care.setBackground(null);
                trouble_clear.setBackground(null);

                trouble_insert.setTextColor(getResources().getColor(R.color.green_text_color));
                trouble_clear.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_care.setTextColor(getResources().getColor(R.color.origin_color));
                break;
            case "care":
                trouble_insert.setBackground(null);
                trouble_care.setBackground(getResources().getDrawable(R.drawable.text_btn));
                trouble_clear.setBackground(null);

                trouble_insert.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_clear.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_care.setTextColor(getResources().getColor(R.color.green_text_color));
                break;
            case "claer":
                trouble_insert.setBackground(null);
                trouble_care.setBackground(null);
                trouble_clear.setBackground(getResources().getDrawable(R.drawable.text_btn));

                trouble_insert.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_care.setTextColor(getResources().getColor(R.color.origin_color));
                trouble_clear.setTextColor(getResources().getColor(R.color.green_text_color));
                break;
        }
    }

    //뒤로가기 버튼을 두번 연속으로 눌러야 종료
    private long time= 0;
    @Override
    public void onBackPressed(){

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if(System.currentTimeMillis()-time>=2000){
                Fragment_trouble_list ftc = new Fragment_trouble_list();
                FragmentManager fmanager = getSupportFragmentManager();
                FragmentTransaction ftrans = fmanager.beginTransaction();
                ftrans.replace(R.id.frage_change,ftc);
                ftrans.commit();

                time=System.currentTimeMillis();
                Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
            }else if(System.currentTimeMillis()-time<2000){
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_btn :
                new AlertDialog.Builder(this)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(Call_Center_Activity.this , LoginActivity.class );
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
                return true;
            case R.id.help_btn :
                Intent i = new Intent(Call_Center_Activity.this , Help_Actaivity.class );
                switch (page_check){
                    case "insert" :
                        i.putExtra("help_key","insert");
                        startActivity(i);
                        break;
                    case "list" :
                        i.putExtra("help_key","list");
                        startActivity(i);
                        break;
                    case "care":
                        i.putExtra("help_key","care");
                        startActivity(i);
                        break;
                }
                return true;
            default:
                Toast.makeText(this,"선택하신 메뉴의 도움말은 준비중 입니다.",Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    public void message_group ( String dep_code ){
        FirebaseMessaging.getInstance().subscribeToTopic("interpass");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("base");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("incheon");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("gangbuk");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("gangnam");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("incheon2");

        switch (dep_code) {
            case "D25":
                FirebaseMessaging.getInstance().subscribeToTopic("base");
                Log.d("UserTopic", dep_code);
                break;
            case "D21":
                FirebaseMessaging.getInstance().subscribeToTopic("incheon");
                Log.d("UserTopic", dep_code);
                break;
            case "D22":
                FirebaseMessaging.getInstance().subscribeToTopic("gangnam");
                Log.d("UserTopic", dep_code);
                break;
            case "D23":
                FirebaseMessaging.getInstance().subscribeToTopic("gangbuk");
                Log.d("UserTopic", dep_code);
                break;
            case "D29":
                FirebaseMessaging.getInstance().subscribeToTopic("incheon2");
                Log.d("UserTopic", dep_code);
                break;
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//            Log.d("event :" , ":" + event.getAction());
//        if (event.getAction() == EditorInfo.IME_ACTION_DONE) {
//            Log.d("test","ttttttttttttttttttttttttt");
//            View v = getCurrentFocus();
//            if (v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }

}
