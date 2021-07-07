package app.erp.com.erp_app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import app.erp.com.erp_app.barcode_old_fragment.Fragment_barcode_bus;
import app.erp.com.erp_app.barcode_old_fragment.Fragment_my_barcode;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("차량 단말기 바코드");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frage_change , new Fragment_barcode_bus());

        fragmentTransaction.commit();

    }//onCreate..

    private long pressedTime = 0;
    private OnBackPressedListener mBackListener;
    public interface OnBackPressedListener {
        public void onBack();
    }
    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        if(mBackListener != null) {

            try {
                mBackListener.onBack();
            }catch (Exception e){

            }
            Log.e("!!!", "Listener is not null");
            // 리스너가 설정되지 않은 상태(예를들어 메인Fragment)라면
            // 뒤로가기 버튼을 연속적으로 두번 눌렀을 때 앱이 종료됩니다.
        } else {
            Log.e("!!!", "Listener is null");
            if ( pressedTime == 0 ) {
                Snackbar.make(findViewById(R.id.drawer_layout), " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            }
            else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if ( seconds > 2000 ) {
                    Snackbar.make(findViewById(R.id.drawer_layout), " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
                    pressedTime = 0 ;
                }
                else {
                    super.onBackPressed();
                    Log.e("!!!", "onBackPressed : finish, killProcess");
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
/*      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        String title = "";
        if (id == R.id.nav_camera) {
            fragment = new Fragment_barcode_bus();
            title = "차량 단말기 바코드";
        } else if (id == R.id.reserve_item_give) {
//            fragment = new Barcode_garage_input_Activity();
//            title = "영업소 단말기 바코드 지급";
        } else if (id == R.id.reserve_item_return) {
//            fragment = new Barcode_garage_output_Activity();
            title = "영업소 단말기 바코드 회수";
        }else if ( id == R.id.my_barcode_workList){
            fragment = new Fragment_my_barcode();
            title = "내 바코드 부착 내역";
        }
//        else if (id == R.id.reserve_item_workList){
////            fragment = new Barcode_input_list_Activity();
//            title = "예비품 지급 내역";
//        }
        else if(id == R.id.new_bus_insert){
            Intent i = new Intent(MainActivity.this , New_Bus_Activity.class);
            startActivity(i);
        }
//        else if(id == R.id.gtv_error_install){
//            Intent i = new Intent(MainActivity.this , Gtv_Error_Install_Activity.class);
//            startActivity(i);
//        }



        if(fragment != null){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frage_change,fragment);
            ft.commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}//Main Activity....
