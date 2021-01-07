package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Over_Work_Insert_List_Adapter;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

// 연장근무 상세보기 다이알로그
public class Dialog_Over_Work_Apporval_View extends Dialog  {

    private Context mContext;
    private List<ProJectVO> item_list = new ArrayList<>();
    private ProJectVO pvo;

    private TextView d_work_date , d_notice , d_sign_notice,work_status_string;
    private View.OnClickListener ok_btn_listener;

    private String work_date , reg_date , reg_time, emp_id, status_string;
    private String bus_num , busoff_name, confirm_work_noice;

    private ERP_Spring_Controller erp;
    private HashMap<String , Object> map = new HashMap<>();

    private SharedPreferences pref;

    private DialogOverWorkNotice notice_dialog;
    public Boolean delete_flag = false;
    //연장근무 승인하는 곳
    public Dialog_Over_Work_Apporval_View(Context context, View.OnClickListener btn_listener, String w_date , String r_date , String r_time , String e_id, String s_s) {
        super(context);
        mContext = context;
        ok_btn_listener = btn_listener;
        work_date = w_date;
        reg_date = r_date;
        reg_time = r_time;
        emp_id = e_id;
        status_string = s_s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_over_work_apporval_view);
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        pref = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final String sign_emp_id = pref.getString("emp_id",null);

        //승인 반려 취소 버튼
        TextView confirm_btn = (TextView)findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(d_sign_notice.getText().toString()) || "입력하려면 터치해 주세요.".equals(d_sign_notice.getText().toString())){
                    map.put("sign_notice","");
                }else{
                    map.put("sign_notice",d_sign_notice.getText().toString());
                }
                delete_flag = true;
                map.put("sign_emp_id",sign_emp_id);
                Call<Boolean> call = erp.over_work_confirm(map);
                new Dialog_Over_Work_Apporval_View.over_work_confirm().execute(call);
                dismiss();
            }
        });

        TextView reject_btn = (TextView)findViewById(R.id.reject_btn);
        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(d_sign_notice.getText().toString()) || "입력하려면 터치해 주세요.".equals(d_sign_notice.getText().toString())){
                    map.put("sign_notice","");
                }else{
                    map.put("sign_notice",d_sign_notice.getText().toString());
                }
                delete_flag = true;
                map.put("sign_emp_id",sign_emp_id);
                Call<Boolean> call = erp.over_work_reject(map);
                new Dialog_Over_Work_Apporval_View.over_work_reject().execute(call);
                dismiss();
            }
        });

        TextView cancel_btn = (TextView)findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(ok_btn_listener);

        map.put("work_date",work_date);
        map.put("reg_date",reg_date);
        map.put("reg_time",reg_time);
        map.put("emp_id",emp_id);

        d_work_date = (TextView)findViewById(R.id.d_work_date);
        d_notice = (TextView)findViewById(R.id.d_notice);
        d_sign_notice = (TextView)findViewById(R.id.d_sign_notice);

        work_status_string = (TextView)findViewById(R.id.work_status_string);
        work_status_string.setText(status_string);

        //문서 상태에 따라 버튼 숨김
        if(!"기안".equals(status_string)){
            LinearLayout btn_line1 = (LinearLayout)findViewById(R.id.btn_line1);
            LinearLayout btn_line2 = (LinearLayout)findViewById(R.id.btn_line2);
            btn_line1.setVisibility(View.GONE);
            btn_line2.setVisibility(View.GONE);
            confirm_btn.setVisibility(View.GONE);
            reject_btn.setVisibility(View.GONE);
            cancel_btn.setText("확인");
        }else{
            //승인 결제 의견 입력
            LinearLayout over_work_confirm_notice_input_layout = (LinearLayout) findViewById(R.id.over_work_confirm_notice_input_layout);
            over_work_confirm_notice_input_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TextView d_sign_notice = (TextView)findViewById(R.id.d_sign_notice);
                    String notice_text = d_sign_notice.getText().toString();
                    if("입력하려면 터치해 주세요.".equals(notice_text)){
                        d_sign_notice.setText("");
                        notice_text = "";
                    }

                    notice_dialog = new DialogOverWorkNotice(mContext, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d_sign_notice.setText(notice_dialog.return_notice_Text());
                            notice_dialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notice_dialog.dismiss();
                        }
                    } , notice_text);
                    notice_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            confirm_work_noice = d_sign_notice.getText().toString();
                        }
                    });
                    notice_dialog.setCancelable(false);
                    notice_dialog.show();

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(notice_dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    Window window = notice_dialog.getWindow();
                    window.setAttributes(lp);

                }
            });
        }

        Call<List<Over_Work_List_VO>> call = erp.select_over_work_date_info(map);
        new Dialog_Over_Work_Apporval_View.select_over_work_date_info().execute(call);
    }

    private class select_over_work_date_info extends AsyncTask<Call, Void , List<Over_Work_List_VO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("연장 근무 등록정보 가져오는중..");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        @Override
        protected List<Over_Work_List_VO> doInBackground(Call... calls) {
            try {
                Call<List<Over_Work_List_VO>> call = calls[0];
                Response<List<Over_Work_List_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Over_Work_List_VO> over_work_list_vos) {
            super.onPostExecute(over_work_list_vos);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(over_work_list_vos != null){
                d_work_date.setText(over_work_list_vos.get(0).getWork_date());

                d_notice.setText(over_work_list_vos.get(0).getNotice());
                d_notice.setIncludeFontPadding(false);
                d_notice.setMovementMethod(new ScrollingMovementMethod());

                if(!"기안".equals(status_string)){
                    d_sign_notice.setText(over_work_list_vos.get(0).getSign_notice());
                    d_sign_notice.setIncludeFontPadding(false);
                    d_sign_notice.setMovementMethod(new ScrollingMovementMethod());
                }
                Call<List<Over_Work_List_VO>> call = erp.select_over_work_trouble_list(map);
                new Dialog_Over_Work_Apporval_View.select_over_work_trouble_list().execute(call);
            }
        }
    }

    private class select_over_work_trouble_list extends AsyncTask<Call , Void , List<Over_Work_List_VO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("연장 근무 등록정보 가져오는중..");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        @Override
        protected List<Over_Work_List_VO> doInBackground(Call... calls) {
            try{
                Call<List<Over_Work_List_VO>> call = calls[0];
                Response<List<Over_Work_List_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Over_Work_List_VO> over_work_list_vos) {
            super.onPostExecute(over_work_list_vos);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(over_work_list_vos != null){
                RecyclerView recyclerView = findViewById(R.id.d_over_work_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                Over_Work_Insert_List_Adapter adapter = new Over_Work_Insert_List_Adapter();
                recyclerView.setAdapter(adapter);

                for(int i=0; i<over_work_list_vos.size(); i++){
                    adapter.addItem(over_work_list_vos.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class over_work_confirm extends AsyncTask<Call ,Void ,Boolean>{
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

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(mContext,"정상적으로 승인처리 되었습니다.",Toast.LENGTH_SHORT).show();
                dismiss();
            }else{
                Toast.makeText(mContext,"연장근무 승인처리 오류발생.",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }

    private class over_work_reject extends AsyncTask<Call ,Void ,Boolean>{
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

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(mContext,"정상적으로 부결처리 되었습니다.",Toast.LENGTH_SHORT).show();
                dismiss();
            }else{
                Toast.makeText(mContext,"연장근무 부결처리 오류발생.",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }

    public void set_select_boolean ( Boolean sb){
        delete_flag = sb;
    }
}
