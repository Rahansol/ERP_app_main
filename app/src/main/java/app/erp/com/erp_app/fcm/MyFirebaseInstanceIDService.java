package app.erp.com.erp_app.fcm;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by hsra on 2018-04-10.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    public MyFirebaseInstanceIDService(){

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // 설치할때 여기서 토큰을 자동으로 만들어 준다
        String refreshedToken = "";
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firbase id login", "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(refreshedToken == null){
            refreshedToken ="sdf";
        }
//        sendRegistrationToServer(refreshedToken);
    }

    public void send_token_app_server(String torken, String user_id , String trans_id , String user_tel, String user_name, String user_check){
        // OKHTTP를 이용해 웹서버로 토큰값을 날려준다.
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user_token", torken)
                .add("user_tel", user_tel)
                .add("user_id", user_id)
                .add("trans_id", trans_id)
                .add("user_name", user_name)
                .add("user_key" , user_check)
                .build();

        //request
        Request request = new Request.Builder()
//                .url("http://192.168.113.4:8080/BCS_Spring/app_token_update.do")
                .url("http://interpass.co.kr/BCS_Spring/app_token_update.do")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

