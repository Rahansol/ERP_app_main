package app.erp.com.erp_app.document_care;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.install.model.ActivityResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import app.erp.com.erp_app.ActivityResultEvent;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment1;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment2;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment3;


// Fragment_ProJect_List.java 문서에서 등록버튼을 눌렀을 때 이 화면으로 이동시키기
// [등록화면]
public class MyProject_Work_Insert_Activity extends AppCompatActivity{

    public static Stack<Fragment> fragmentStack;
    public static FragmentManager manager;

    public static MyPageFragment1 myPageFragment1;
    public static MyPageFragment2 myPageFragment2;

    final static int FIRST_FRAGMENT= 1001;
    final static int SECOND_FRAGMENT= 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project_work_insert);

        myPageFragment1= new MyPageFragment1();
        myPageFragment2= new MyPageFragment2();
        fragmentStack= new Stack<>();
        manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout, myPageFragment1).commit();


        /*getSupportFragmentManager().beginTransaction()
                .add(new MyPageFragment1())
                .add(R.id.container_b, new MyPageFragment2())
                .commit();*/

        /*Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
       // DrawerLayout drawer(DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        /*NavigationView nav= (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);*/



        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            int permissionOnResult=checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionOnResult== PackageManager.PERMISSION_DENIED){
                //퍼미션 체크 다이얼로그
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }

        /*List<Fragment> list= new ArrayList<>();
        list.add(new MyPageFragment1());
        list.add(new MyPageFragment2());
        //list.add(new MyPageFragment3());

        pager= findViewById(R.id.pager);
        pagerAdapter= new MySlidePagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(list.size());   //초기화를 방지하기 위해서 viewPager에 setOffscreenPageLimit를 fragments들의 사이즈대로 붙여줌.*/
    }//onCreaet()....

    public static void changeFragment(int index){
        switch (index){
            case FIRST_FRAGMENT:
                manager.beginTransaction().replace(R.id.frameLayout, myPageFragment2).commit();
                break;
            case SECOND_FRAGMENT:
                manager.beginTransaction().replace(R.id.frameLayout, myPageFragment1).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!fragmentStack.isEmpty()){
            Fragment nextFragment= fragmentStack.pop();
            manager.beginTransaction().replace(R.id.frameLayout, nextFragment).commit();
            System.out.println("[testing>>]"+fragmentStack.size());
        }else {
            super.onBackPressed();
        }
    }//onBackPressed..

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_btn:
                new AlertDialog.Builder(this)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i= new Intent(MyProject_Work_Insert_Activity.this, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}