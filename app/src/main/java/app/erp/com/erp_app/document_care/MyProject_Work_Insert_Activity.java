package app.erp.com.erp_app.document_care;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.Stack;

import app.erp.com.erp_app.Barcode_My_list_Activity;
import app.erp.com.erp_app.Barcode_bus_Activity;
import app.erp.com.erp_app.Barcode_garage_input_Activity;
import app.erp.com.erp_app.Barcode_garage_output_Activity;
import app.erp.com.erp_app.Gtv_Error_Install_Activity;
import app.erp.com.erp_app.New_Bus_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.ReserveItemRepairActivity;
import app.erp.com.erp_app.Work_Report_Activity;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.document_care.myInstallSignFragments.MyInstallationSignActivity;
import app.erp.com.erp_app.document_care.myfragments.Installation_List_Signature_Activity;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment1;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment2;
import app.erp.com.erp_app.error_history.Error_History_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Approval_Activity;


// Fragment_ProJect_List.java 문서에서 등록버튼을 눌렀을 때 이 화면으로 이동시키기
// [등록화면]
public class MyProject_Work_Insert_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView nav;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static Stack<Fragment> fragmentStack;
    public static FragmentManager manager;

    public static MyPageFragment1 myPageFragment1;
    public static MyPageFragment2 myPageFragment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project_work_insert);


        // NOTE: 액티비티에 프래그먼트 붙여주기
        myPageFragment1= new MyPageFragment1();
        myPageFragment2= new MyPageFragment2();
        fragmentStack= new Stack<>();
        manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout, myPageFragment1).commit();   //첫번째 Fragment 로 이동 transition!!




        // FIXME: ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        // STATUS: 자꾸 Option Menu Item 이 적용이 안되고 toolbar 를 setSupportActionBar 할 수가 없었음
        // EDIT: Manifest.xml 에서 theme 에 NoActionBar 적용 -> toolbar 에 actionBar 의 권한을 주는 것으로 수정 -> 잘됌.

        drawerLayout= findViewById(R.id.drawer_layout);
        nav= (NavigationView) findViewById(R.id.nav);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        // NOTE: 햄버거 메뉴 생성
        actionBarDrawerToggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        actionBarDrawerToggle.syncState();


    }//onCreaet()....

    // STATUS: 2단계 화면을 끝내고 처음 화면으로 돌아감 -> 뒤로가기 버튼을 눌렀을 때
    //         [메인화면]으로 돌아가지 않고 2단계 화면으로 다시 돌아감.
    // FIXME: 어떻게 back stack 을 destroy 할까?
    // FIXME: 여기서 뒤로가기 버튼 클릭했을 때 메인화면으로 이동시키기

    // EDIT: MyPageFragment2.java 에서 [완료]버튼 클릭할 때 이전의 모든 fragments 를 소멸시키는 작업으로 수정.
   /* @Override
    public void onBackPressed() {
        if (!fragmentStack.isEmpty()){
            Fragment nextFragment= fragmentStack.pop();
            manager.beginTransaction().replace(R.id.frameLayout, nextFragment).commit();
            System.out.println("[testing>>]"+fragmentStack.size());
        }else {
            super.onBackPressed();
        }
    }//onBackPressed..*/




    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    // NOTE: OPTION MENU 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.installation_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        Log.d("일단실행 ::", "0");
        switch (item.getItemId()){
            case R.id.home_btn:
                Toast.makeText(this, "홈 버튼 클릭함", Toast.LENGTH_SHORT).show();
                Log.d("일단실행 ::", "2");
                Intent intent= new Intent(MyProject_Work_Insert_Activity.this, Call_Center_Activity.class);
                startActivity(intent);
                return true;
            default:
                Toast.makeText(this, "홈 버튼 클릭함", Toast.LENGTH_SHORT).show();
                Log.d("일단실행 ::", "3");
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent i_1 = new Intent(MyProject_Work_Insert_Activity.this , Call_Center_Activity.class);
                startActivity(i_1);
                break;
            case R.id.nav_camera:
                Intent i = new Intent(MyProject_Work_Insert_Activity.this , Barcode_bus_Activity.class);
                startActivity(i);
                break;
            case R.id.reserve_item_give:
                Intent i1 = new Intent(MyProject_Work_Insert_Activity.this , Barcode_garage_input_Activity.class);
                startActivity(i1);
                break;
            case R.id.reserve_item_return:
                Intent i2 = new Intent(MyProject_Work_Insert_Activity.this , Barcode_garage_output_Activity.class);
                startActivity(i2);
                break;
            case R.id.my_barcode_workList:
                Intent i3 = new Intent(MyProject_Work_Insert_Activity.this , Barcode_My_list_Activity.class);
                startActivity(i3);
                break;
            case R.id.new_bus_insert:
                Intent i4 = new Intent(MyProject_Work_Insert_Activity.this , New_Bus_Activity.class);
                startActivity(i4);
                break;
            case R.id.gtv_error_install:
                Intent i5 = new Intent(MyProject_Work_Insert_Activity.this , Gtv_Error_Install_Activity.class);
                startActivity(i5);
                break;
            case R.id.reserve_item_repair:
                Intent i6 = new Intent(MyProject_Work_Insert_Activity.this , ReserveItemRepairActivity.class);
                startActivity(i6);
                break;
            case R.id.work_report_btn:
                Intent i7 = new Intent(MyProject_Work_Insert_Activity.this , Work_Report_Activity.class);
                startActivity(i7);
                break;
            case R.id.installation_List_signature:
                Intent i8 = new Intent(MyProject_Work_Insert_Activity.this , Installation_List_Signature_Activity.class);
                startActivity(i8);
                break;
            case R.id.my_installation_sign:
                Intent i9 = new Intent(MyProject_Work_Insert_Activity.this , MyInstallationSignActivity.class);
                startActivity(i9);
                break;
            case R.id.trouble_serch_btn:
                Intent i10 = new Intent(MyProject_Work_Insert_Activity.this , Error_History_Activity.class);
                startActivity(i10);
                break;
            case R.id.over_work_btn:
                Intent i11 = new Intent(MyProject_Work_Insert_Activity.this , Over_Work_Activity.class);
                startActivity(i11);
                break;
            case R.id.over_work_approval_btn:
                Intent i12 = new Intent(MyProject_Work_Insert_Activity.this , Over_Work_Approval_Activity.class);
                startActivity(i12);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}