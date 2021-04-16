package app.erp.com.erp_app.document_care;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.List;

import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;

public class ProJectMainActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_main);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        PermissionCheck();
        Fragment fragment = new Fragment_ProJect_List();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.project_frage_change,fragment);
        ft.commit();
    }

    void PermissionCheck(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String savePath = Environment.getExternalStoragePublicDirectory("IERP").toString();
                File dir = new File(savePath);
                if(!dir.exists())
                {
                    dir.mkdirs();    // 폴더를 만든다
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("프로젝트 업무를 위해 권한이 필요합니다")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2 , menu);
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
                                pref = getSharedPreferences("user_info" , MODE_PRIVATE);
                                editor = pref.edit();
                                editor.putString("auto_login" , "Nauto");
                                editor.commit();

                                Intent i = new Intent(ProJectMainActivity.this , LoginActivity.class );
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
