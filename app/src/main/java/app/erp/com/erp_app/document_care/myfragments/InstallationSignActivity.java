package app.erp.com.erp_app.document_care.myfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.CameraTestActivity;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class InstallationSignActivity extends AppCompatActivity {

    public static Activity step1Activity;

    private Context mContext;
    private RecyclerView recycler;
    private ArrayList<UnitItems> unitItems;
    private UnitAdapter unitAdapter;

    private TextView tv_prj, tv_dtti, tv_busoff_name, tv_garage_name;
    private Button btn_next, btn_TEST;
    private static String st_text, st_val, st_text2, st_val2;
    private static String mPrj_JobName2, st_prj, st_table_name, st_busoffName, st_transo_bizr_id, st_garageId, st_garageName, st_job_name, st_job_type, today, st_reg_dtti, st_bus_id, st_bus_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_sign);

        getSupportActionBar().setTitle("1단계");

        step1Activity= InstallationSignActivity.this;

        //전달받은 데이터..
        Intent i= getIntent();
        st_prj= i.getStringExtra("prj");
        st_table_name= i.getStringExtra("table_name");
        st_busoffName= i.getStringExtra("busoffName");
        st_transo_bizr_id= i.getStringExtra("transp_bizr_id");
        st_garageName= i.getStringExtra("garageName");
        st_garageId= i.getStringExtra("garageId");
        st_job_name= i.getStringExtra("job_name");
        st_job_type= i.getStringExtra("job_type");
        st_reg_dtti= i.getStringExtra("reg_dtti");   //현재 날짜로..
        st_bus_id= i.getStringExtra("bus_id");
        st_bus_num= i.getStringExtra("bus_num");
        String mPrj= Arrays.toString(st_prj.split("설치/철수/증대폐차"));  // 결과= [경기시내 L2 ]
        String mPrj_JobName= mPrj+" "+st_job_name+" "+"확인서";
        mPrj_JobName2=  mPrj+" "+st_job_name;


        Log.d("두번째 ===============", "");
        Log.d("프로젝트명 SPLITE::   ", mPrj);
        Log.d("프로젝트명 ::   ", st_prj);
        Log.d("테이블명 ::     ", st_table_name);
        Log.d("날짜 ::         ", st_reg_dtti);
        Log.d("운수사명 ::     ", st_busoffName);
        Log.d("운수사 아이디:: ", st_transo_bizr_id);
        Log.d("영업소명 ::     ", st_garageName);
        //Log.d("영업소아이디 ::     ", st_garageId);
        Log.d("작업 ::         ", st_job_name);
        Log.d("작업타입 ::     ", st_job_type);
        Log.d("버스넘- 날짜 ::  ", st_reg_dtti);
        Log.d("버스 아이디 ::  ", st_bus_id);
        Log.d("BUS_NUM ::  ", st_bus_num);


        tv_prj= findViewById(R.id.tv_prj);
        tv_dtti= findViewById(R.id.tv_dtti);
        tv_busoff_name= findViewById(R.id.tv_busoff_name);
        tv_garage_name= findViewById(R.id.tv_garage_name);
        tv_prj.setText(mPrj_JobName);
        //tv_dtti.setText(st_reg_dtti);
        tv_busoff_name.setText(st_busoffName);
        tv_garage_name.setText(st_garageName);


        /*먼저 현재날짜 지정*/
        long now= System.currentTimeMillis();
        Date date= new Date(now);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
        today= sdf.format(date);
        tv_dtti.setText(today);
        //Log.d("설치일자 - 현재날짜 ============> ", today+"");
        //설치일자- 현재날짜
        tv_dtti.setText(today.substring(0,8)+"  "+today.substring(8,10)+":"+today.substring(10,12)+":"+today.substring(12,14));

        recycler= findViewById(R.id.recycler);
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.unitLists_detail(st_table_name, st_transo_bizr_id, st_job_type, st_bus_num, st_garageName ); // 파라미터 확인..
        new unitList_recycler().execute(call);

        //[다음]버튼 클릭..
        btn_next= findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(InstallationSignActivity.this, InstallationSignActivity2.class);  //서명 2단계 액티비티
                i.putExtra("prj",st_prj);   //프로젝트
                i.putExtra("table_name",st_table_name);
                i.putExtra("busoffName",st_busoffName);
                i.putExtra("transp_bizr_id",st_transo_bizr_id);
                i.putExtra("garageName",st_garageName);
                i.putExtra("job_name",st_job_name);  //작업
                i.putExtra("job_type",st_job_type);
                i.putExtra("reg_dtti",st_reg_dtti);
                i.putExtra("today", today);
                i.putExtra("bus_id",st_bus_id);
                i.putExtra("bus_num",st_bus_num);
                i.putExtra("mPrj_JobName", mPrj_JobName2);

               startActivity(i);
            }
        });


    }//onCreate..



    public class unitList_recycler extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            Log.d("단말기 상세목록 리사이클러뷰 사이즈 ::: ", bus_officeVOS.size()+"");

            if (bus_officeVOS == null){
                Toast.makeText(mContext, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
                unitItems= new ArrayList<>();
                int base_int=0;
                for (int i=0; i<bus_officeVOS.size(); i++){
                    if( i%2 == base_int) {

                        if( i == bus_officeVOS.size()-1){
                            unitItems.add(new UnitItems(st_text = bus_officeVOS.get(i).getText()
                                    , st_val = bus_officeVOS.get(i).getVal()
                                    , st_text2 =" "
                                    , st_val2 = " "));
                            //Log.d("Data:::::::::::::::::::::::::", bus_officeVOS.get(i).getText()+"/"+bus_officeVOS.get(i).getVal()+"/");
                        }else if(bus_officeVOS.get(i + 1).getText().equals("작업일")==true){
                            unitItems.add(new UnitItems(st_text = bus_officeVOS.get(i).getText()
                                    , st_val = bus_officeVOS.get(i).getVal()
                                    , st_text2 =" "
                                    , st_val2 = " "));
                            //Log.d("Data:::::::::::::::::::::::::", bus_officeVOS.get(i).getText()+"/"+bus_officeVOS.get(i).getVal()+"/");
                            unitItems.add(new UnitItems(st_text = bus_officeVOS.get(i+1).getText()
                                    , st_val = bus_officeVOS.get(i+1).getVal()
                                    , st_text2 = bus_officeVOS.get(i + 2).getText()
                                    , st_val2 = bus_officeVOS.get(i + 2).getVal()));
                            //Log.d("Data:::::::::::::::::::::::::", bus_officeVOS.get(i+1).getText()+"/"+bus_officeVOS.get(i+1).getVal()+"/"+bus_officeVOS.get(i+2).getText()+"/"+bus_officeVOS.get(i+2).getVal());
                            i=i+2;
                            if(base_int == 0 ) {
                                base_int = 1;
                            }else{
                                base_int = 0;
                            }
                        }else {
                            unitItems.add(new UnitItems(st_text = bus_officeVOS.get(i).getText()
                                    , st_val = bus_officeVOS.get(i).getVal()
                                    , st_text2 = bus_officeVOS.get(i + 1).getText()
                                    , st_val2 = bus_officeVOS.get(i + 1).getVal()));
                            //Log.d("Data:::::::::::::::::::::::::", bus_officeVOS.get(i).getText()+"/"+bus_officeVOS.get(i).getVal()+"/"+bus_officeVOS.get(i+1).getText()+"/"+bus_officeVOS.get(i+1).getVal());
                        }
                    }
                }
                unitAdapter= new UnitAdapter(mContext, unitItems);
                recycler.setAdapter(unitAdapter);
                //unitAdapter.notifyItemChanged(unitAdapter.getItemId(R.id.line));
                unitAdapter.notifyDataSetChanged();

                //서명 1단계 화면
                // 단말기 상세목록 아이템을 클릭하면 -> 설치확인서 상세 화면으로 이동..
                unitAdapter.setMyListener(new UnitAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Toast.makeText(InstallationSignActivity.this, pos+" 번 아이템", Toast.LENGTH_SHORT).show();
                        //설치확인서 상세 화면으로 이동
                        Intent i= new Intent(InstallationSignActivity.this, Installation_List_Sginature_Activity2_1.class);
                        //프로젝트, 작업, 운수사, 영업소, 차량번호, 차대번호, 등록시간, 노선, 등록자, 확인자 데이터 값 전달..
                        i.putExtra("prj",st_prj);
                        i.putExtra("job_name",st_job_name);
                        i.putExtra("transp_bizr_id",st_transo_bizr_id);
                        i.putExtra("busoffName", st_busoffName);
                        i.putExtra("garage_name",st_garageName);
                        i.putExtra("reg_dtti",st_reg_dtti);

                        i.putExtra("table_name",st_table_name);
                        i.putExtra("job_type",st_job_type);
                        i.putExtra("bus_id",st_bus_id);
                        startActivity(i);
                    }
                });


            }
        }
    }

}//Activity...