package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.dialog.DialogPrj_ItemView;
import app.erp.com.erp_app.dialog.Dialog_Over_Work_Apporval_View;
import app.erp.com.erp_app.dialog.Dialog_Over_Work_View;
import app.erp.com.erp_app.over_work.Fragement_Over_Work_List;
import app.erp.com.erp_app.over_work.Over_Work_Approval_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Re_Insert_Activity;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import app.erp.com.erp_app.vo.Over_Work_VO;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by hsra on 2019-06-24.
 */

//기안 , 회수 , 승인 , 반려  항목별 리스트 생성 어뎁터
public class Over_Work_Process_List_Adapter extends RecyclerView.Adapter<Over_Work_Process_List_Adapter.ViewHolder> {

    private ArrayList<Over_Work_List_VO> mData = new ArrayList<>();
    private SparseBooleanArray selectItems = new SparseBooleanArray();
    private Context mContext;

    private HashMap<String , Object> return_map = new HashMap<>();
    private Map<String,Object> test_map = new HashMap<>();
    private int select_postion = 0;

    private Dialog_Over_Work_View dialog;
    private Dialog_Over_Work_Apporval_View dialogOverWorkApporvalView;
    private ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Over_Work_List_VO data;
        private int postion;

        TextView work_date;
        TextView reg_date;
        TextView reg_tiem;
        TextView work_notice;

        LinearLayout over_work_layout;

        ViewHolder(View itemView){
            super(itemView);

            work_date = (TextView)itemView.findViewById(R.id.work_date);
            reg_date = (TextView)itemView.findViewById(R.id.reg_date);
            reg_tiem= (TextView)itemView.findViewById(R.id.reg_tiem);
            work_notice = (TextView)itemView.findViewById(R.id.work_notice);

            over_work_layout = (LinearLayout)itemView.findViewById(R.id.over_work_layout);
        }

        void onBind(Over_Work_List_VO datas , final int pos){
            this.data = datas;
            this.postion = pos;

            work_date.setText("근무일 : "+data.getWork_date());
            reg_date.setText("등록일 : "+data.getReg_date());

            // 연장 근무 리스트  A = 승인 화면 N = 신청 화면
            if(data.getDisplay_type().equals("A")){
                reg_tiem.setText("등록자 : "+data.getEmp_name());
                switch (data.getStatus()){
                    case "0":
                        over_work_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogOverWorkApporvalView = new Dialog_Over_Work_Apporval_View(mContext, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogOverWorkApporvalView.dismiss();
                                        dialogOverWorkApporvalView.set_select_boolean(false);
                                    }
                                },data.getWork_date() , data.getReg_date() , data.getReg_time() , data.getEmp_id(),"기안");
                                dialogOverWorkApporvalView.setCancelable(false);
                                dialogOverWorkApporvalView.show();
                                dialogOverWorkApporvalView.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        boolean result = dialogOverWorkApporvalView.delete_flag;
                                        if(result){
                                            mData.remove(pos);
                                            notifyDataSetChanged();
                                        }
                                    }
                                });

                                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                                int width = dm.widthPixels; //디바이스 화면 너비
                                width = (int)(width * 0.9);

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialogOverWorkApporvalView.getWindow().getAttributes());
                                lp.width = width;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                Window window = dialogOverWorkApporvalView.getWindow();
                                window.setAttributes(lp);
                                dialogOverWorkApporvalView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                        });
                        break;
                    case "1":
                        over_work_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogOverWorkApporvalView = new Dialog_Over_Work_Apporval_View(mContext, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogOverWorkApporvalView.dismiss();
                                    }
                                },data.getWork_date() , data.getReg_date() , data.getReg_time() , data.getEmp_id(),"승인");
                                dialogOverWorkApporvalView.setCancelable(false);
                                dialogOverWorkApporvalView.show();

                                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                                int width = dm.widthPixels; //디바이스 화면 너비
                                width = (int)(width * 0.9);

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialogOverWorkApporvalView.getWindow().getAttributes());
                                lp.width = width;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                Window window = dialogOverWorkApporvalView.getWindow();
                                window.setAttributes(lp);
                                dialogOverWorkApporvalView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                        });
                        break;
                    case "4":
                        over_work_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogOverWorkApporvalView = new Dialog_Over_Work_Apporval_View(mContext, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogOverWorkApporvalView.dismiss();
                                    }
                                },data.getWork_date() , data.getReg_date() , data.getReg_time() , data.getEmp_id(),"부결");
                                dialogOverWorkApporvalView.setCancelable(false);
                                dialogOverWorkApporvalView.show();

                                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                                int width = dm.widthPixels; //디바이스 화면 너비
                                width = (int)(width * 0.9);

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialogOverWorkApporvalView.getWindow().getAttributes());
                                lp.width = width;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                Window window = dialogOverWorkApporvalView.getWindow();
                                window.setAttributes(lp);
                                dialogOverWorkApporvalView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                        });
                        break;
                }
            }else{

                switch (data.getStatus()){
                    case "0":
                        reg_tiem.setText("등록시간 : "+substr_time(data.getReg_time()));
                        over_work_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog = new Dialog_Over_Work_View(mContext, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        dialog.set_select_boolean(false);
                                    }
                                },data.getWork_date() , data.getReg_date() , data.getReg_time() , data.getEmp_id(),"기안");
                                dialog.setCancelable(false);
                                dialog.show();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        boolean result = dialog.delete_flag;
                                        if(result){
                                            mData.remove(pos);
                                            notifyDataSetChanged();
                                        }
                                        Log.d("testetset","Setstset" + result);
                                    }
                                });

                                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                                int width = dm.widthPixels; //디바이스 화면 너비
                                width = (int)(width * 0.9);

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = width;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                Window window = dialog.getWindow();
                                window.setAttributes(lp);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                        });
                        break;
                    case "2":
                        reg_tiem.setText("등록시간 : "+substr_time(data.getReg_time()));
                        over_work_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder a_builder = new AlertDialog.Builder(mContext);
                                a_builder.setTitle("연장근무 재기안 / 삭제");
                                a_builder.setMessage("연장근무 회수건을\n재기안 하시겠습니까 ?");
                                a_builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                a_builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        select_postion = pos;
                                        HashMap<String,Object> reques_map = new HashMap<>();
                                        reques_map.put("work_date" , data.getWork_date());
                                        reques_map.put("reg_date" , data.getReg_date());
                                        reques_map.put("reg_time" , data.getReg_time());
                                        reques_map.put("emp_id" , data.getEmp_id());
                                        Call<Boolean> call = erp.delete_over_work(reques_map);
                                        new Over_Work_Process_List_Adapter.delete_over_work().execute(call);

                                    }
                                });
                                a_builder.setNeutralButton("재기안", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(mContext, Over_Work_Re_Insert_Activity.class);
                                        intent.putExtra("data",data);
                                        mContext.startActivity(intent);
                                    }
                                });
                                a_builder.setCancelable(false);
                                a_builder.show();
                            }
                        });
                        break;
                    case "1":
                        if(null == data.getSt()){
                            reg_tiem.setText("인정 ST : "+0);
                        }else{
                            reg_tiem.setText("인정 ST : "+data.getSt());
                        }

                        over_work_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog = new Dialog_Over_Work_View(mContext, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                },data.getWork_date() , data.getReg_date() , data.getReg_time() , data.getEmp_id(),"승인");
                                dialog.setCancelable(false);
                                dialog.show();

                                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                                int width = dm.widthPixels; //디바이스 화면 너비
                                width = (int)(width * 0.9);

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = width;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                Window window = dialog.getWindow();
                                window.setAttributes(lp);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                        });
                        break;
                    case "4":
                        reg_tiem.setText("등록시간 : "+substr_time(data.getReg_time()));
                        over_work_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog = new Dialog_Over_Work_View(mContext, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                },data.getWork_date() , data.getReg_date() , data.getReg_time() , data.getEmp_id(),"부결");
                                dialog.setCancelable(false);
                                dialog.show();

                                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                                int width = dm.widthPixels; //디바이스 화면 너비
                                width = (int)(width * 0.9);

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = width;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                Window window = dialog.getWindow();
                                window.setAttributes(lp);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                        });
                        break;
                }
            }

            work_notice.setText(data.getNotice());
            work_notice.setIncludeFontPadding(false);
            work_notice.setMovementMethod(new ScrollingMovementMethod());
            work_notice.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true); return false; } });
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int postion) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.my_over_work_process_layout, viewGroup , false);
        Over_Work_Process_List_Adapter.ViewHolder vh  = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int postion) {
        viewHolder.onBind(mData.get(postion) , postion);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Over_Work_List_VO list){
        mData.add(list);
        notifyDataSetChanged();
    }

    public HashMap<String , Object> get_select_tiem(){
        return return_map;
    }

    public String substr_time( String time){
        if(time != null){
            String result_time = time.substring(0,2) +":"+ time.substring(2,4);
            return result_time;
        }
        return "시간 미입력";
    }

    public String substr_date (String date ){
        if ( date != null){
            String result_date = date.substring(5,date.length());
            return result_date;
        }
        return "날짜 미입력";
    }

    private class delete_over_work extends AsyncTask<Call , Void , Boolean>{
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call =calls[0];
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
                Toast.makeText(mContext,"내역 삭제처리 완료",Toast.LENGTH_SHORT).show();
                mData.remove(select_postion);
                notifyDataSetChanged();
            }else{
                Toast.makeText(mContext,"내역 삭제처리 오류",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
