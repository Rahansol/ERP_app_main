package app.erp.com.erp_app.document_care.myfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class Installation_List_Sginature_Activity2 extends AppCompatActivity {

    private Context mContext;
    private Button btnOk;
    private TextView tvPrj, tvBusoffName, tvBusNum, tvRouteNum, tvGarageName, tvVehicleNum, tvDocEmpName,tvRegEmpName, tvRegDtti, tvDocDtti, tvJobName, tvCableTitle, tvPhotoTitle;
    static String item_sign, item_bus_id, item_reg_dtti, table_name,transp_bizr_id, prj_name,bus_id,job_type, st_reg_dtti, st_jobName, st_busoff_name, st_bus_num, st_route_num, st_garage_name, st_vehicle_num , st_doc_dtti, st_reg_emp_name, st_doc_emp_name;

    /*단말기 업로드 사진 리사이클러뷰*/
    private ArrayList<InstallPhotoItems> installPhotoItems;
    private InstallPhotoAdapter installPhotoAdapter;
    private RecyclerView recyclerInstallPhoto;

    /*케이블 리사이클러뷰*/
    private ArrayList<CableItems> cableItems;
    private CableAdapter cableAdapter;
    private RecyclerView recycler_cable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation__list__sginature_2);

        mContext= this;

        getSupportActionBar().setTitle("설치확인서 상세");

        Intent i= getIntent();
        prj_name= i.getStringExtra("prj");
        table_name= i.getStringExtra("table_name");
        Log.d("table name ::::::::::::::::: >>>>> ", table_name+"");
        st_reg_dtti= i.getStringExtra("reg_dtti");  //등록시간
        transp_bizr_id= i.getStringExtra("transp_bizr_id");
        bus_id= i.getStringExtra("bus_id");
        job_type= i.getStringExtra("job_type");
        st_jobName= i.getStringExtra("job_name");


        // TranspBizrAdapter(차량리스트 리사이클러뷰) 로부터 전달받은 데이터..
        String item_job_name= i.getStringExtra("item_job_name");
        String item_busoff_name= i.getStringExtra("item_busoff_name");
        String item_garage_name= i.getStringExtra("item_garage_name");
        String item_bus_num= i.getStringExtra("item_bus_num");
        item_bus_id= i.getStringExtra("item_bus_id");
        String item_sign= i.getStringExtra("item_sign");
        item_reg_dtti= i.getStringExtra("item_reg_dtti");
        String item_route_num= i.getStringExtra("item_route_num");
        table_name= i.getStringExtra("item_table_name");


        //프로젝트 명
        tvPrj= findViewById(R.id.tv_prj);
        tvPrj.setText(G.PRJ_NAME);

        tvJobName= findViewById(R.id.tv_job_name);
        tvJobName.setText(item_job_name);

        //등록시간
        tvRegDtti= findViewById(R.id.tv_reg_dtti);
        tvRegDtti.setText(item_reg_dtti);
        //tvRegDtti.setText(st_reg_dtti.substring(0,8)+"  "+st_reg_dtti.substring(8,10)+":"+st_reg_dtti.substring(10,12)+":"+st_reg_dtti.substring(12,14));
        //tvRegDtti.setText(st_reg_dtti);

        //운수사명
        tvBusoffName= findViewById(R.id.tv_busoff_name);
        tvBusoffName.setText(item_busoff_name);

        //차량번호
        tvBusNum= findViewById(R.id.tv_bus_num);
        tvBusNum.setText(item_bus_num);

        //노선
        tvRouteNum= findViewById(R.id.tv_route_num);
        tvRouteNum.setText(item_route_num);

        //영업소명
        tvGarageName= findViewById(R.id.tv_garage_name);
        tvGarageName.setText(item_garage_name);

        //차대번호
        tvVehicleNum= findViewById(R.id.tv_vehicle_num);


        //사인시간
        tvDocDtti= findViewById(R.id.tv_doc_dtti);

        //등록자
        tvDocEmpName= findViewById(R.id.tv_doc_emp_name);

        //확인자
        tvRegEmpName= findViewById(R.id.tv_reg_emp_name);


        Log.d("LOG=====================>    ",G.TABLE_NAME+", "+ item_reg_dtti+", "+ G.TRANSP_BIZR_ID+", "+item_bus_id+ ", "+G.JOB_TYPE);

        /*상단 정보*/
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.Transp_Bizr_List_Info_Item(G.TABLE_NAME, item_reg_dtti, G.TRANSP_BIZR_ID, item_bus_id, G.JOB_TYPE);  //null...

        Log.d("데이터 확인=> ",G.TABLE_NAME+", "+item_reg_dtti+", "+ G.TRANSP_BIZR_ID+", "+item_bus_id+", "+G.JOB_TYPE);

        new Transp_Bizr_List_Info().execute(call);


        tvCableTitle= findViewById(R.id.tv_cable_title);
        tvPhotoTitle= findViewById(R.id.tv_photo_title);


        btnOk= findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "확인하셨습니다.", Toast.LENGTH_SHORT).show();
                //Intent i= new Intent(mContext, Call_Center_Activity.class);   //[확인]버튼 누르면 어디로 이동??
                //startActivity(i);
                onBackPressed();   //뒤로가기

            }
        });

    }// onCreate.....


    /*상단*/
    public class Transp_Bizr_List_Info extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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

            if (bus_officeVOS == null){
                Toast.makeText(mContext, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
                for (int i=0; i<bus_officeVOS.size(); i++){
                    st_busoff_name= bus_officeVOS.get(i).getBusoff_name();
                    st_bus_num= bus_officeVOS.get(i).getBus_num();
                    st_route_num= bus_officeVOS.get(i).getRoute_num();
                    st_garage_name= bus_officeVOS.get(i).getGarage_name();
                    st_vehicle_num= bus_officeVOS.get(i).getVehicle_num();
                    st_doc_dtti= bus_officeVOS.get(i).getDoc_dtti();   //null 이 맞음   //사인시간
                    st_reg_emp_name= bus_officeVOS.get(i).getReg_emp_name();  //등록자
                    st_doc_emp_name= bus_officeVOS.get(i).getDoc_emp_name();  //확인자

                }

                //tvPrj.setText(tvPrj.getText().toString().replace("설치/철수/증대/폐차"," ") + tvJobName.getText().toString()+" 확인서");  //전 화면에서 인텐트로 받아온 값 넣어주기..
                //tvRegDtti.setText(st_reg_dtti.substring(0,8)+"  "+st_reg_dtti.substring(8,10)+":"+st_reg_dtti.substring(10,12)+":"+st_reg_dtti.substring(12,14)); //등록시간
                tvRegDtti.setText(item_reg_dtti);
                tvDocDtti.setText(st_doc_dtti);
                tvVehicleNum.setText(st_vehicle_num);
                tvRegEmpName.setText(st_reg_emp_name);  //등록자
                tvDocEmpName.setText(st_doc_emp_name);  //확인자

                Log.d("등록자 =>  ", st_reg_emp_name+"");

                /*하단 리사이클러뷰*/
                ERP_Spring_Controller erp2= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<Bus_OfficeVO>> call1= erp2.Transp_Bizr_List_Info_Item2(G.TABLE_NAME, item_reg_dtti, G.TRANSP_BIZR_ID, item_bus_id, G.JOB_TYPE);
                new Transp_Bizr_List_Info_Item2().execute(call1);



            }
        }
    }


    /*하단- 사진..etc*/
    public class Transp_Bizr_List_Info_Item2 extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("예외: ", e+"");
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS==null){
                Toast.makeText(mContext, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
                tvPhotoTitle.setVisibility(View.VISIBLE);
                tvCableTitle.setVisibility(View.VISIBLE);

                Log.d("bus_officeVOS 사이즈====> ", bus_officeVOS.size()+"");

                installPhotoItems= new ArrayList<>();
                cableItems= new ArrayList<>();
                for (int i=0; i<bus_officeVOS.size(); i++){


                    if(bus_officeVOS.get(i).getItem_type().equals("P")) {
                        installPhotoItems.add(new InstallPhotoItems(bus_officeVOS.get(i).getItem_name()
                                                                    , bus_officeVOS.get(i).getSqltext()));        // 단말기 사진 불러오기
                    }else{

                        if(bus_officeVOS.get(i).getItem_type().equals("C")){                                      // 신청한 케이블 아이템 불러오기
                            cableItems.add(new CableItems(bus_officeVOS.get(i).getItem_name()  
                                                        , bus_officeVOS.get(i).getSqltext()));
                        }else {
                            cableItems.add(new CableItems(bus_officeVOS.get(i).getItem_name()
                                        , bus_officeVOS.get(i).getSqltext()+"개"));
                        }
                    }


                    Log.d("item_name: ", bus_officeVOS.get(i).getItem_name());
                    Log.d("sqlText: ", bus_officeVOS.get(i).getSqltext());
                    Log.d("item_type: ", bus_officeVOS.get(i).getItem_type());
                    Log.d("케이블 사이즈 =>> ", cableItems.size()+"");
                }

                recyclerInstallPhoto= findViewById(R.id.recycler_install_photo);
                recycler_cable= findViewById(R.id.recycler_cable);

                installPhotoAdapter= new InstallPhotoAdapter(mContext, installPhotoItems, cableItems);
                cableAdapter= new CableAdapter(mContext, cableItems, installPhotoItems);

                recyclerInstallPhoto.setLayoutManager(new GridLayoutManager(mContext, 2));
                recyclerInstallPhoto.setAdapter(installPhotoAdapter);

                recycler_cable.setAdapter(cableAdapter);
                cableAdapter.notifyDataSetChanged();
            }


        }
    }




}