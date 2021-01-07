package app.erp.com.erp_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Response;

public class InOutDetailActivity extends AppCompatActivity {
    Context mContext;

   /*단말기 입출고 상세정보 리사이클러뷰*/
    private RecyclerView recyclerView;
    private ArrayList<InOutDetailItems> items;
    private InOutDetailAdapter inOutDetailAdapter;

    /*로그인 아이디*/
    static String emp_id;
    SharedPreferences pref;

    static String req_type;
    static String reg_date;
    static String reg_time;

    private TextView tv_emp_id;
    private TextView tv_reg_date_value, tv_reg_time_value, tv_req_type_value;
    private TextView tv_req_date_value, tv_res_date_value;
    private TextView tv_res_name_value, tv_req_name_value;
    private TextView tv_unit_cnt_value, tv_receipt_cnt_value, tv_schedule_cnt_value, tv_unrequest_cnt_value, tv_cancel_cnt_value;
    static String stExeType;

    /*FAB*/
    Boolean isOpen= false;
    FloatingActionButton clickFAB, fab1, fab2;
    TextView text1, text2;
    Animation fabOpen, fabClose, fabClock, fabAntiClock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_detail);

        /*FAB*/
        clickFAB= findViewById(R.id.clickFAB);
        fab1= findViewById(R.id.fab1);
        fab2= findViewById(R.id.fab2);
        fabOpen= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabClock= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clock);
        fabAntiClock= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clock);

        text1= findViewById(R.id.text1);
        text2= findViewById(R.id.text2);

        clickFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen){
                    text1.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                    fab1.startAnimation(fabOpen);
                    fab2.startAnimation(fabOpen);
                    isOpen=true;
                }else {
                    text1.setVisibility(View.INVISIBLE);
                    text2.setVisibility(View.INVISIBLE);
                    fab1.startAnimation(fabClose);
                    fab2.startAnimation(fabClose);
                    isOpen= false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(InOutDetailActivity.this, ReleaseRequestActivity.class );
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(InOutDetailActivity.this, WarehousingActivity.class );
                startActivity(intent2);
            }
        });

        //InOutStatusAdapter 에서 전달받은 데이터
        Intent intent= getIntent();
        String reqType= intent.getStringExtra("reqType");
        String regDate= intent.getStringExtra("regDate");
        String regTime= intent.getStringExtra("regTime");

        String reqDate= intent.getStringExtra("reqDate");
        String resDate= intent.getStringExtra("resDate");

        String resName= intent.getStringExtra("resName");
        String reqName= intent.getStringExtra("reqName");

        String unitCnt= intent.getStringExtra("unitCnt");
        String scheduleCnt= intent.getStringExtra("scheduleCnt");
        String receiptCnt= intent.getStringExtra("receiptCnt");
        String unrequestCnt= intent.getStringExtra("unrequestCnt");
        String cancelCnt= intent.getStringExtra("cancelCnt");

        req_type= reqType;
        reg_date= regDate;
        reg_time= regTime;

        pref= getSharedPreferences("user_info" , MODE_PRIVATE);
        emp_id= pref.getString("emp_id","");

        tv_req_type_value= findViewById(R.id.tv_req_type);        //요청타입
        tv_req_type_value.setText(req_type);
        if (req_type.equals("출고")){
            tv_req_type_value.setTextColor(Color.parseColor("#ffffff"));
            tv_req_type_value.setBackground(this.getResources().getDrawable(R.drawable.box_border_thin_green));
        }else{  //입고
            tv_req_type_value.setTextColor(Color.parseColor("#ffffff"));
            tv_req_type_value.setBackground(this.getResources().getDrawable(R.drawable.box_border_thin_blue));
        }

        tv_reg_date_value= findViewById(R.id.tv_reg_date_value);  //요청날짜
        tv_reg_date_value.setText(regDate);
        tv_reg_time_value= findViewById(R.id.tv_reg_time_value);   //요청시간
        tv_reg_time_value.setText(reg_time);

        tv_req_date_value= findViewById(R.id.tv_req_date_value);  //요청일
        tv_req_date_value.setText(reqDate);
        tv_res_date_value= findViewById(R.id.tv_res_date_value);  //물류일
        tv_res_date_value.setText(resDate);

        tv_res_name_value= findViewById(R.id.tv_res_name_value);
        tv_res_name_value.setText(resName);
        tv_req_name_value= findViewById(R.id.tv_req_name_value);
        tv_req_name_value.setText(reqName);

        tv_unit_cnt_value= findViewById(R.id.tv_unit_cnt_value);
        tv_unit_cnt_value.setText(unitCnt);
        tv_receipt_cnt_value= findViewById(R.id.tv_receipt_cnt_value);
        tv_receipt_cnt_value.setText(receiptCnt);
        tv_schedule_cnt_value= findViewById(R.id.tv_schedule_cnt_value);
        tv_schedule_cnt_value.setText(scheduleCnt);
        tv_unrequest_cnt_value= findViewById(R.id.tv_unrequest_cnt_value);
        tv_unrequest_cnt_value.setText(unrequestCnt);
        tv_cancel_cnt_value= findViewById(R.id.tv_cancel_cnt_value);
        tv_cancel_cnt_value.setText(cancelCnt);

        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> call= erp.AppInventoryInOutputListDetail(reg_date, reg_time, req_type, emp_id);
        new InOutputListDetail().execute(call);

    }





    public class InOutputListDetail extends AsyncTask<Call, Void, List<TestAllVO>> {

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);



            items= new ArrayList<>();
            for (int i=0; i<testAllVOS.size(); i++){
                items.add(new InOutDetailItems(   testAllVOS.get(i).getRnum()
                                                , testAllVOS.get(i).getUnit_ver()
                                                , testAllVOS.get(i).getUnit_id()
                                                , testAllVOS.get(i).getUnit_code()
                                                , testAllVOS.get(i).getRep_unit_code()
                                                , testAllVOS.get(i).getExe_type()));    //unit_code, rep_unit_code 빼고 exe_type 실행타입으로 변경..
                //stExeType= testAllVOS.get(i).getExe_type();

            }


            inOutDetailAdapter= new InOutDetailAdapter(mContext, items);
            recyclerView=  findViewById(R.id.recyclerview_inout_detail);
            recyclerView.setAdapter(inOutDetailAdapter);
            inOutDetailAdapter.notifyDataSetChanged();




            /*inOutDetailAdapter.setmListener(new InOutDetailAdapter.OnitemClickListener() {
                @Override
                public void onItemClick(View v, int post) {
                    String tag_exe_type= (String) v.getTag();
                    Log.d("exe_type 실행타입: ", tag_exe_type+"");  //나옴

                    if (tag_exe_type.equals("수령완료")){

                    }
               }
            });*/

        }


    }
}