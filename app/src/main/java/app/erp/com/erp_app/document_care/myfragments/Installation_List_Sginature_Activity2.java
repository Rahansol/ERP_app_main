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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class Installation_List_Sginature_Activity2 extends AppCompatActivity {

    private Context mContext;
    private TextView tvPrj, tvBusoffName, tvBusNum, tvRouteNum, tvGarageName, tvVehicleNum, tvDocDtti, tvDocEmpName, tvRegDtti, tvJobName, tvCableTitle, tvPhotoTitle;
    static String table_name,transp_bizr_id, prj_name,bus_id,job_type, st_reg_dtti, st_jobName, st_busoff_name, st_bus_num, st_route_num, st_garage_name, st_vehicle_num , st_doc_dtti, st_doc_emp_name;

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
        st_reg_dtti= i.getStringExtra("reg_dtti");
        transp_bizr_id= i.getStringExtra("transp_bizr_id");
        bus_id= i.getStringExtra("bus_id");
        job_type= i.getStringExtra("job_type");
        st_jobName= i.getStringExtra("job_name");

        Log.d("전달받은 데이터 확인 ====> ", table_name+", "+st_reg_dtti+", "+transp_bizr_id+", "+bus_id+", "+job_type);

        tvPrj= findViewById(R.id.tv_prj);
        tvRegDtti= findViewById(R.id.tv_reg_dtti);
        tvBusoffName= findViewById(R.id.tv_busoff_name);
        tvBusNum= findViewById(R.id.tv_bus_num);
        tvRouteNum= findViewById(R.id.tv_route_num);
        tvGarageName= findViewById(R.id.tv_garage_name);
        tvVehicleNum= findViewById(R.id.tv_vehicle_num);
        tvDocDtti= findViewById(R.id.tv_doc_dtti);
        tvDocEmpName= findViewById(R.id.tv_doc_emp_name);
        tvJobName= findViewById(R.id.tv_job_name);

        /*상단 정보*/
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.Transp_Bizr_List_Info_Item(table_name, st_reg_dtti, transp_bizr_id, bus_id, job_type);
        new Transp_Bizr_List_Info().execute(call);


        tvCableTitle= findViewById(R.id.tv_cable_title);
        tvPhotoTitle= findViewById(R.id.tv_photo_title);



        /*사진 리사이클러뷰 테스트..*/
        /*installPhotoItems= new ArrayList<>();
        installPhotoItems.add(new InstallPhotoItems("차량전면", ""));
        installPhotoItems.add(new InstallPhotoItems("차량전면2", ""));
        installPhotoItems.add(new InstallPhotoItems("차량전면3", ""));
        installPhotoItems.add(new InstallPhotoItems("차량전면4", ""));
        installPhotoItems.add(new InstallPhotoItems("차량전면5", ""));
        recyclerInstallPhoto= findViewById(R.id.recycler_install_photo);
        installPhotoAdapter= new InstallPhotoAdapter(mContext, installPhotoItems);
        recyclerInstallPhoto.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerInstallPhoto.setAdapter(installPhotoAdapter);*/


        /*케이블 리사이클러뷰 테스트..*/
        /*cableItems= new ArrayList<>();
        cableItems.add(new CableItems("운전자 단말기 1.5m", "1"));
        cableItems.add(new CableItems("운전자 단말기 1.5m", "2"));
        cableItems.add(new CableItems("운전자 단말기 1.5m", "3"));
        cableItems.add(new CableItems("운전자 단말기 1.5m", "4"));
        cableItems.add(new CableItems("운전자 단말기 1.5m", "5"));
        recycler_cable= findViewById(R.id.recycler_cable);
        cableAdapter= new CableAdapter(mContext, cableItems);
        recycler_cable.setAdapter(cableAdapter);*/
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
                    //prj_name= bus_officeVOS.get(i).getTable_name();   //왜 null???
                    st_busoff_name= bus_officeVOS.get(i).getBusoff_name();
                    st_bus_num= bus_officeVOS.get(i).getBus_num();
                    st_route_num= bus_officeVOS.get(i).getRoute_num();
                    st_garage_name= bus_officeVOS.get(i).getGarage_name();
                    st_vehicle_num= bus_officeVOS.get(i).getVehicle_num();
                    st_doc_dtti= bus_officeVOS.get(i).getDoc_dtti();   //null 이 맞음
                    st_doc_emp_name= bus_officeVOS.get(i).getDoc_emp_name();

                    Log.d("확인====> ", st_busoff_name+", "+st_bus_num+", "+st_route_num+", "+st_garage_name+", "+st_vehicle_num+", "+st_doc_dtti+", "+st_doc_emp_name);
                }
                tvPrj.setText(prj_name);  //전 화면에서 인텐트로 받아온 값 넣어주기..
                tvRegDtti.setText(st_reg_dtti);
                tvBusoffName.setText(st_busoff_name);
                tvBusNum.setText(st_bus_num);
                tvRouteNum.setText(st_route_num);
                tvGarageName.setText(st_garage_name);
                tvVehicleNum.setText(st_vehicle_num);
                tvDocDtti.setText(st_doc_dtti);
                tvDocEmpName.setText(st_doc_emp_name);
                tvJobName.setText(st_jobName);


                /*하단 리사이클러뷰*/
                ERP_Spring_Controller erp2= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<Bus_OfficeVO>> call1= erp2.Transp_Bizr_List_Info_Item2(table_name, st_reg_dtti, transp_bizr_id, bus_id, job_type);
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
            }
            tvPhotoTitle.setVisibility(View.VISIBLE);
            tvCableTitle.setVisibility(View.VISIBLE);

            Log.d("bus_officeVOS 사이즈====> ", bus_officeVOS.size()+"");

            installPhotoItems= new ArrayList<>();
            cableItems= new ArrayList<>();
            for (int i=0; i<bus_officeVOS.size(); i++){


                if(bus_officeVOS.get(i).getItem_type().equals("P")) {
                    installPhotoItems.add(new InstallPhotoItems(bus_officeVOS.get(i).getItem_name()
                                                              , bus_officeVOS.get(i).getSqltext()));
                }else{

                    if(bus_officeVOS.get(i).getItem_type().equals("C")){
                        cableItems.add(new CableItems(bus_officeVOS.get(i).getItem_name(), bus_officeVOS.get(i).getSqltext()));
                    }else {
                        cableItems.add(new CableItems(bus_officeVOS.get(i).getItem_name(), bus_officeVOS.get(i).getSqltext()+"개"));
                    }
                }


                Log.d("item_name: ", bus_officeVOS.get(i).getItem_name());
                Log.d("sqlText: ", bus_officeVOS.get(i).getSqltext());
                Log.d("item_type: ", bus_officeVOS.get(i).getItem_type());
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