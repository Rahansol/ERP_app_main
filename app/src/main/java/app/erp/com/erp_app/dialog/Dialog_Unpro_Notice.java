package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dialog_Unpro_Notice extends Dialog {

    private String unpro_notice, reg_date, reg_time, reg_emp_id, unit_code_before, trouble_high_cd_before, trouble_low_cd_before, transp_bizr_id;
    private Map<String, Object> updateMap = new HashMap<>();
    private Retrofit retrofit;
    public EditText unpro_notice_editText;

    public Dialog_Unpro_Notice(@NonNull Context context
                                , String unpro_notice
                                , String reg_date
                                , String reg_time
                                , String reg_emp_id
                                , String unit_code_before
                                , String trouble_high_cd_before
                                , String trouble_low_cd_before
                                , String transp_bizr_id) {
        super(context);
        this.unpro_notice = unpro_notice;
        this.reg_date = reg_date;
        this.reg_time = reg_time;
        this.reg_emp_id = reg_emp_id;
        this.unit_code_before = unit_code_before;
        this.trouble_high_cd_before = trouble_high_cd_before;
        this.trouble_low_cd_before = trouble_low_cd_before;
        this.transp_bizr_id = transp_bizr_id;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_undisposed_msg);

        TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(v -> { dismiss(); });


        unpro_notice_editText = (EditText) findViewById(R.id.unpro_notice_editText);
        //EDIT: 업데이트 된 메세지 불러오기
        unpro_notice_editText.setText(unpro_notice);

        TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(v -> {
            if (unpro_notice_editText.getText().toString().length() == 0){
                Toast.makeText(getContext(), "메세지를 입력하세요.", Toast.LENGTH_SHORT).show();
            }else {
                updateMap.put("unpro_notice", unpro_notice_editText.getText().toString());
                updateMap.put("reg_date", reg_date+"");
                updateMap.put("reg_time", reg_time+"");
                updateMap.put("reg_emp_id", reg_emp_id+"");
                updateMap.put("unit_code_before", unit_code_before+"");
                updateMap.put("trouble_high_cd_before", trouble_high_cd_before+"");
                updateMap.put("trouble_low_cd_before", trouble_low_cd_before+"");
                updateMap.put("transp_bizr_id", transp_bizr_id+"");

                Log.d("updateMap>",updateMap+"");



                //edit: DB update...
                new app_trouble_history_unpro_notice_editText_update().execute();
                Toast.makeText(getContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            }

        });



    }//onCreate..




    private class app_trouble_history_unpro_notice_editText_update extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getContext().getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.trouble_history_undisposed_msg_update(updateMap);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Log.d("call>", "success");
                    Log.d("call>", response+"");
                    Log.d("call>", updateMap+"");
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("call>", "fail");
                }
            });

            return null;
        }
    }
}
