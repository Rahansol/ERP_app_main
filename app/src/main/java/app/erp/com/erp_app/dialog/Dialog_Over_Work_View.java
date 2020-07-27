package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Over_Work_Insert_List_Adapter;
import app.erp.com.erp_app.over_work.Fragement_Over_Work_List;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

// 연장근무 상세보기 다이알로그
public class Dialog_Over_Work_View extends Dialog  {

    private Context mContext;
    private List<ProJectVO> item_list = new ArrayList<>();
    private ProJectVO pvo;

    private TextView d_work_date , d_notice , d_sign_notice,work_status_string;
    private View.OnClickListener ok_btn_listener;
    private LinearLayout status_line;

    private String work_date , reg_date , reg_time, emp_id, status_string;
    private String bus_num , busoff_name;

    private ERP_Spring_Controller erp;
    private HashMap<String , Object> map = new HashMap<>();

    private AlertDialog.Builder a_builder;

    public Boolean delete_flag = false;

    public Dialog_Over_Work_View(Context context, View.OnClickListener btn_listener, String w_date , String r_date , String r_time , String e_id, String s_s) {
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
        setContentView(R.layout.dialog_over_work_view);

        TextView ok_btn = (TextView)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(ok_btn_listener);

        map.put("work_date",work_date);
        map.put("reg_date",reg_date);
        map.put("reg_time",reg_time);
        map.put("emp_id",emp_id);

        TextView work_return_btn = (TextView)findViewById(R.id.work_return_btn);
        work_return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a_builder = new AlertDialog.Builder(mContext);
                a_builder.setTitle("업무 내용 등록");
                a_builder.setMessage(work_date + " 연장근무 신청을\n회수하시겠습니까 ?");

                a_builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_flag = false;
                    }
                });
                a_builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete_flag = true;
//                        dismiss();
                        StringBuilder sb = new StringBuilder();
                        Set<?> set = map.keySet();
                        Iterator<?> it = set.iterator();
                        while(it.hasNext()){
                            String key = (String)it.next();
                            if(key != null){
                                sb.append("------------------------------------------------------------\n");
                                sb.append("key = "+key+",\t\t\tvalue = "+map.get(key)+"\n");
                            }
                        }

                        Call<Boolean> call = erp.update_over_work(map);
                        new Dialog_Over_Work_View.update_over_work().execute(call);
                    }
                });
                a_builder.setCancelable(false);
                a_builder.show();
            }
        });


        d_work_date = (TextView)findViewById(R.id.d_work_date);
        d_notice = (TextView)findViewById(R.id.d_notice);
        d_sign_notice = (TextView)findViewById(R.id.d_sign_notice);

        work_status_string = (TextView)findViewById(R.id.work_status_string);

        work_status_string.setText(status_string);

        //문서 상태에 따라 버튼 숨김
        status_line = (LinearLayout)findViewById(R.id.status_line);
        if(!"기안".equals(status_string)){
            status_line.setVisibility(View.GONE);
            work_return_btn.setVisibility(View.GONE);
        }


        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Over_Work_List_VO>> call = erp.select_over_work_date_info(map);
        new Dialog_Over_Work_View.select_over_work_date_info().execute(call);
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

                d_sign_notice.setText(over_work_list_vos.get(0).getSign_notice());
                d_sign_notice.setIncludeFontPadding(false);
                d_sign_notice.setMovementMethod(new ScrollingMovementMethod());

                Call<List<Over_Work_List_VO>> call = erp.select_over_work_trouble_list(map);
                new Dialog_Over_Work_View.select_over_work_trouble_list().execute(call);
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

    private class update_over_work extends AsyncTask<Call ,Void , Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("작업 리스트 불러오는중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
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
            if (progressDialog != null)
                progressDialog.dismiss();
            if(aBoolean){
                Toast.makeText(mContext,"정상적으로 회수처리 되었습니다.",Toast.LENGTH_SHORT).show();
                dismiss();
            }else{
                Toast.makeText(mContext,"연장근무 회수 처리 오류발생.",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }

    public void set_select_boolean ( Boolean sb){
        delete_flag = sb;
    }
}
