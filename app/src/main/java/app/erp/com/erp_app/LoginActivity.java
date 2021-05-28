package app.erp.com.erp_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.document_care.myfragments.G;
import app.erp.com.erp_app.vo.User_InfoVo;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-25.
 */

public class LoginActivity extends Activity{

    Button submit_login;
    EditText id_text, pw_text;
    private Retrofit retrofit;
    private Gson mGson;
    ProgressDialog progressDialog;

    CheckBox autoLogin;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private final int MY_REQUEST_CODE = 100;
    private AppUpdateManager mAppUpdateManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 업데이트 사용 가능 상태인지 체크
        mAppUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        Task<AppUpdateInfo> appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();
        // 사용가능 체크 리스너를 달아준다
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && // 유연한 업데이트 사용 시 (AppUpdateType.FLEXIBLE) 사용
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                // 유연한 업데이트 사용 시 (AppUpdateType.FLEXIBLE) 사용
                                AppUpdateType.IMMEDIATE,
                                // 현재 Activity
                                LoginActivity.this,
                                // 전역변수로 선언해준 Code
                                MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("AppUpdater", "AppUpdateManager Error", e);
                        e.printStackTrace();
                    }
                    // 업데이트가 사용 가능한 상태 (업데이트 있음) -> 이곳에서 업데이트를 요청해주자
                } else {
                    // 업데이트가 사용 가능하지 않은 상태(업데이트 없음) -> 다음 액티비티로 넘어가도록
                }
            }
        });

        pref = getSharedPreferences("user_info" , MODE_PRIVATE);
        editor = pref.edit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로그인중..");

        TextView app_version_text = (TextView)findViewById(R.id.app_version_text);
        String version = getVersionInfo(getApplicationContext());
        app_version_text.setText("v"+version);

        id_text = (EditText)findViewById(R.id.id_text);
        pw_text = (EditText)findViewById(R.id.pw_text);
        autoLogin = (CheckBox)findViewById(R.id.auto_login);
        String auto_login = pref.getString("auto_login","");
        if(auto_login.equals("auto")){
            autoLogin.setChecked(true);
            id_text.setText(pref.getString("auto_id",""));
            pw_text.setText(pref.getString("auto_pw", ""));
            if(id_text.length()!=0 && pw_text.length()!=0) {
                new App_Login().execute(id_text.getText().toString(),pw_text.getText().toString());
            }
        }else{
            autoLogin.setChecked(false);
        }

        submit_login = (Button)findViewById(R.id.submit_login);
        submit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoLogin.isChecked()){
                    editor.putString("auto_login" , "auto");
                    editor.putString("auto_id" , id_text.getText().toString());
                    editor.putString("auto_pw" , pw_text.getText().toString());
                    editor.commit();
                }else{
                    editor.putString("auto_login" , "Nauto");
                    editor.commit();
                }
                if(id_text.getText().toString().equals("ratest")){
                    Intent intent = new Intent(LoginActivity.this,Call_Center_Activity.class);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                progressDialog.show();
                try {
                    new App_Login().execute(id_text.getText().toString(),pw_text.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    private class App_Login extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {

            /*OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(1,TimeUnit.MINUTES)
                    .writeTimeout(30,TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);*/
            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            String emp_id = strings[0];
            String emp_pw = strings[1];
            final Call<List<User_InfoVo>> call = erp.app_login(emp_id,emp_pw);
            call.enqueue(new Callback<List<User_InfoVo>>() {
                @Override
                public void onResponse(Call<List<User_InfoVo>> call, Response<List<User_InfoVo>> response) {
                    List<User_InfoVo> list = response.body();
                    if(list == null || list.toString().equals("[null]") ){
                        id_text.setText("");
                        pw_text.setText("");
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this , "아이디랑 비민번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        //로그인 한 사람의 정보를 sharedPreference 에 저장/ 네네
                        editor.putString("emp_id" , list.get(0).getEmp_id());
                        editor.putString("emp_name", list.get(0).getEmp_name());
                        editor.putString("dep_name",list.get(0).getDep_name());
                        editor.putString("dep_code",list.get(0).getDep_code());
                        editor.putString("emp_dep_id", list.get(0).getEmp_dep_id());
                        editor.commit();

                        Log.d("emp_dep_id 확인+++++++++++++++++++++++++++", list.get(0).getEmp_dep_id()+" ooooooooooooooooo");
                        Log.d("emp_id 확인+++++++++++++++++++++++++++", list.get(0).getEmp_id()+" ooooooooooooooooo");
                        Log.d("emp_name 확인+++++++++++++++++++++++++++", list.get(0).getEmp_name()+" ooooooooooooooooo");

                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, Call_Center_Activity.class);
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<List<User_InfoVo>> call, Throwable t) {
                    Log.d("test","tt"+t);

                }
            });
            return null;
        }
    }//App_Login()

    public String getVersionInfo(Context context){
        String version = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch(PackageManager.NameNotFoundException e) { }
        return version;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.d("AppUpdate", "Update flow failed! Result code: " + resultCode); // 로그로 코드 확인
                Toast.makeText(this , "ERP 어플을 사용하기 위해서는 업데이트가 필요해요",Toast.LENGTH_SHORT).show();
                finishAffinity(); // 앱 종료
            }
        }
    }

    //test test

    @Override
    protected void onResume() {
        super.onResume();

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(
                new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        if (appUpdateInfo.updateAvailability()
                                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                            // 인 앱 업데이트가 이미 실행중이었다면 계속해서 진행하도록
                            try {
                                mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, LoginActivity.this, MY_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
