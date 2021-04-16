package app.erp.com.erp_app.document_care.myInstallSignFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import app.erp.com.erp_app.Barcode_My_list_Activity;
import app.erp.com.erp_app.Barcode_bus_Activity;
import app.erp.com.erp_app.Barcode_garage_input_Activity;
import app.erp.com.erp_app.Barcode_garage_output_Activity;
import app.erp.com.erp_app.Gtv_Error_Install_Activity;
import app.erp.com.erp_app.Help_Actaivity;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.New_Bus_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.ReserveItemRepairActivity;
import app.erp.com.erp_app.Work_Report_Activity;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;
import app.erp.com.erp_app.document_care.myfragments.Installation_List_Signature_Activity;
import app.erp.com.erp_app.error_history.Error_History_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Approval_Activity;

public class MyInstallationSignActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView nav;
    ActionBarDrawerToggle actionBarDrawerToggle;

    TabLayout tabLayout;
    ViewPager pager;
    TabAdapter tabAdapter;  //내가 만든 어댑터 클래스.. => tabLayout 과 viewPager 를 연결해준다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_installation_sign);

        toolbar= findViewById(R.id.tool_bar);
        drawerLayout= findViewById(R.id.drawer_layout);
        nav= findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);

        //ActionBar actionBar= getSupportActionBar();
        //actionBar.hide();                        //기본 actionBar 없애기
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("프로젝트 업무 조회");
        actionBarDrawerToggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);                           //햄버거 메뉴 버튼 색 변경
        actionBarDrawerToggle.syncState();


        tabLayout= findViewById(R.id.tab_layout);
        pager= findViewById(R.id.pager);
        tabAdapter= new TabAdapter(getSupportFragmentManager());  //tabAdapter 객체생성
        pager.setAdapter(tabAdapter);                             //pager 에 tabAdapter 연결
        tabLayout.setupWithViewPager(pager);                      //tabLayout 에 pager 연결
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }//onCreate()...


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.installation_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
          /*  case R.id.prj_btn:
                Toast.makeText(this, "프로젝트 업무 버튼 클릭!!", Toast.LENGTH_SHORT).show();
                //Intent intent= new Intent(MyInstallationSignActivity.this, )
                break;*/
            case R.id.home_btn:
                Toast.makeText(this, "홈 버튼 클릭", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(MyInstallationSignActivity.this, Call_Center_Activity.class);
                startActivity(intent);
                break;
        }//switch..
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent i_1 = new Intent(MyInstallationSignActivity.this , Call_Center_Activity.class);
                startActivity(i_1);
                break;
            case R.id.nav_camera:
                Intent i = new Intent(MyInstallationSignActivity.this , Barcode_bus_Activity.class);
                startActivity(i);
                break;
            case R.id.reserve_item_give:
                Intent i1 = new Intent(MyInstallationSignActivity.this , Barcode_garage_input_Activity.class);
                startActivity(i1);
                break;
            case R.id.reserve_item_return:
                Intent i2 = new Intent(MyInstallationSignActivity.this , Barcode_garage_output_Activity.class);
                startActivity(i2);
                break;
            case R.id.my_barcode_workList:
                Intent i3 = new Intent(MyInstallationSignActivity.this , Barcode_My_list_Activity.class);
                startActivity(i3);
                break;
            case R.id.new_bus_insert:
                Intent i4 = new Intent(MyInstallationSignActivity.this , New_Bus_Activity.class);
                startActivity(i4);
                break;
            case R.id.gtv_error_install:
                Intent i5 = new Intent(MyInstallationSignActivity.this , Gtv_Error_Install_Activity.class);
                startActivity(i5);
                break;
            case R.id.reserve_item_repair:
                Intent i6 = new Intent(MyInstallationSignActivity.this , ReserveItemRepairActivity.class);
                startActivity(i6);
                break;
            case R.id.work_report_btn:
                Intent i7 = new Intent(MyInstallationSignActivity.this , Work_Report_Activity.class);
                startActivity(i7);
                break;
            case R.id.installation_List_signature:
                Intent i8 = new Intent(MyInstallationSignActivity.this , Installation_List_Signature_Activity.class);
                startActivity(i8);
                break;
            case R.id.my_installation_sign:
                Intent i9 = new Intent(MyInstallationSignActivity.this , MyInstallationSignActivity.class);
                startActivity(i9);
                break;
            case R.id.trouble_serch_btn:
                Intent i10 = new Intent(MyInstallationSignActivity.this , Error_History_Activity.class);
                startActivity(i10);
                break;
            case R.id.over_work_btn:
                Intent i11 = new Intent(MyInstallationSignActivity.this , Over_Work_Activity.class);
                startActivity(i11);
                break;
            case R.id.over_work_approval_btn:
                Intent i12 = new Intent(MyInstallationSignActivity.this , Over_Work_Approval_Activity.class);
                startActivity(i12);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}