package test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.callcenter.Fragment_trouble_insert_bus_call_center;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import retrofit2.Call;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    TextView test_text ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        test_text = findViewById(R.id.count_text);

        Button test_btn = findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<Bus_infoVo>> test_call = erp.app_busoffice_test("Y");
                new TestActivity.app_busoffice_test().execute(test_call);
            }
        });

        Button test_btn2 = findViewById(R.id.test_btn2);
        test_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<Bus_infoVo>> test_call = erp.app_busoffice_test("N");
                new TestActivity.app_busoffice_test().execute(test_call);
            }
        });
    }//onCreate...



    private class app_busoffice_test extends AsyncTask<Call, Void, List<Bus_infoVo>>{

        @Override
        protected List<Bus_infoVo> doInBackground(Call... calls) {
            Call<List<Bus_infoVo>> call = calls[0];
            try{
                Response<List<Bus_infoVo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Bus_infoVo> bus_infoVos) {
            super.onPostExecute(bus_infoVos);
            if(bus_infoVos != null){
                test_text.setText(""+bus_infoVos.size());
            }
        }
    }
}//Main..
