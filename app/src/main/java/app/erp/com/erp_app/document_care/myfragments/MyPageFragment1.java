package app.erp.com.erp_app.document_care.myfragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.security.spec.ECParameterSpec;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.WarehousingActivity;
import app.erp.com.erp_app.document_care.JobTextItems;
import app.erp.com.erp_app.document_care.Job_Text_Adapter;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import app.erp.com.erp_app.vo.Bus_infoVo;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment1 extends Fragment {
    Context mContext;
    Spinner spinner_office_group, spinner_project_transp, spinner_project_garage, spinner_project_route_list, spinner_project_bus_ver;
    static String st_office_group_value, st_project_transp_value, st_project_garage_value, st_project_route_list_value, st_busoff_name, st_busoff_name_value, st_unit_ver_value;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    /* job text 리사이클러뷰 */
    private RecyclerView recyclerView;
    private ArrayList<JobTextItems> jobTextItems;
    private Job_Text_Adapter job_text_adapter;
    ImageView iv;
    String mPicturePath;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.pager1_my_project_work_insert_fragment, container, false);

        iv= rootView.findViewById(R.id.iv);

        /*조합 스피너*/
        spinner_office_group= rootView.findViewById(R.id.project_office_group);
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.OfficeGroupSpinner();
        new OfficeGroup().execute(call);

        spinner_project_transp= rootView.findViewById(R.id.project_transp);           /*운수사 스피너*/
        spinner_project_garage= rootView.findViewById(R.id.project_garage_spinner);   /*영업소 스피너*/
        spinner_project_route_list= rootView.findViewById(R.id.project_route_list);   /*노선번호 스피너*/
        spinner_project_bus_ver= rootView.findViewById(R.id.project_bus_ver);         /*버전 스피너*/



        /*이미지 갤러리, 사진촬영을 위한 리사이클러뷰*/
        jobTextItems= new ArrayList<>();
        jobTextItems.add(new JobTextItems("차량전면1", "","사진","미리보기"));
        jobTextItems.add(new JobTextItems("차량전면1", "","사진","미리보기"));
        jobTextItems.add(new JobTextItems("차량전면1", "","사진","미리보기"));
        jobTextItems.add(new JobTextItems("차량전면1", "","사진","미리보기"));
        jobTextItems.add(new JobTextItems("차량전면1", "","사진","미리보기"));
        jobTextItems.add(new JobTextItems("차량전면1", "","사진","미리보기"));
        jobTextItems.add(new JobTextItems("차량전면1", "","사진","미리보기"));

        //recyclerView= recyclerView.findViewById(R.id.recyclerview_job_text);
        recyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerview_job_text);   // fragment 에서는 rootView로 찾아주기
        job_text_adapter= new Job_Text_Adapter(mContext, jobTextItems);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(job_text_adapter);
        job_text_adapter.setmListener(new Job_Text_Adapter.mOnItemClickListener() {
            @Override
            public void mOnItemclick(View v, int post) {
                if (st_office_group_value==null || st_project_transp_value==null || st_project_garage_value==null || st_project_route_list_value==null || st_unit_ver_value==null){
                    Toast.makeText(v.getContext(), "조합, 운수사, 영업소, 노선, 버전을 선택하세요 ", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(v.getContext());
                    builder.setTitle("사진선택").setMessage("이미지를 불러올 방법을 선택하세요");
                    builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 20);*/

                            /*외장 메모리(SD card) 절대경로 알아내기*/
                            String sdCardPath= null;
                            String sdCardState= Environment.getExternalStorageState();
                            if (sdCardState.equals(Environment.MEDIA_MOUNTED)){
                                 sdCardPath= Environment.getExternalStorageDirectory().getAbsolutePath();
                            }
                            Log.d("외장메모리 경로:::::: ", sdCardPath+"");    ///storage/emulated/0 ???


                        }
                    });
                    builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진앨범 앱 실행
                        }
                    });
                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();
                }

            }
        });
        return  rootView;
    }//onCreate...




    private void captureCamera(String cameraType, int intentType){
        String sdCardPathState= Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sdCardPathState)){
            Intent takePicIntent= new Intent(cameraType);
            if (takePicIntent.resolveActivity(mContext.getPackageManager()) != null){
                File file= null;
            }
        }
    }







    /*조합 스피너*/
    public class OfficeGroup extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
               /* Log.d("asdf :::", response + "1818");*/
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");
                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getOffice_group());
                }
                spinner_office_group.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_office_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String st_office_group= spinner_office_group.getSelectedItem().toString();
                        if (st_office_group != "선택"){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_office_group == bus_officeVOS.get(j).getOffice_group()){        // 운수사명 (조합)
                                    st_office_group_value = bus_officeVOS.get(j).getOffice_group();    // 선택한 운수사명 값

                                }
                            }
                        }
                        pref= getContext().getSharedPreferences("office_group_info", Context.MODE_PRIVATE);
                        editor= pref.edit();
                        editor.putString("office_group", st_office_group_value);
                        editor.commit();

                        ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<Bus_OfficeVO>> call1= erp1.BusOffName(st_office_group_value);      // 선택한 운수사명 값 전달받음
                        new Transp().execute(call1);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }//JobType()....

    public class Transp extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();

                spinner_array.add("선택");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getBusoff_name());
                }
                spinner_project_transp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_transp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_busoff_name= spinner_project_transp.getSelectedItem().toString();
                        if (!st_busoff_name.equals("선택") ){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_busoff_name == bus_officeVOS.get(j).getBusoff_name()){
                                    st_busoff_name_value = bus_officeVOS.get(j).getBusoff_name();     //선택된 운수회사명
                                    st_project_transp_value= bus_officeVOS.get(j).getTransp_bizr_id();   //선택된 운수사 고정번호

                                    Log.d("운수회사 고정번호: ", st_project_transp_value+"");
                                    Log.d("운수회사명: ", st_busoff_name+"");


                                }
                            }
                        }
                        /*선택된 운수사명(조합), 운수회사 고정번호 SharedPrefrence 에 저장  ===== > SharePreference 말고 bundle로 데이터 저장 및 전송 */
                        pref= getContext().getSharedPreferences("busoff_info", Context.MODE_PRIVATE);
                        editor= pref.edit();
                        editor.putString("transp_bizr_id", st_project_transp_value);
                        editor.commit();

                        pref= getContext().getSharedPreferences("busoff_info2", Context.MODE_PRIVATE);
                        editor= pref.edit();
                        editor.putString("busoff_name", st_busoff_name_value);
                        editor.commit();

                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<Bus_OfficeVO>> call= erp.GarageSpinner(st_project_transp_value);
                        new GarageSpinner().execute(call);


                        ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<Bus_OfficeVO>> call1= erp1.BusRouteSpinner(st_project_transp_value);
                        new BusRouteSpinner().execute(call1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                }
            }

        }//Transp().....




    public class GarageSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();

                spinner_array.add("선택");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getGarage_name());
                }
                spinner_project_garage.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_garage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String st_project_garage= spinner_project_garage.getSelectedItem().toString();
                        if (!st_project_garage.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_project_garage == bus_officeVOS.get(j).getGarage_name()){
                                    st_project_garage_value= bus_officeVOS.get(j).getGarage_id();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }//GarageSpinner()................



    /*노선번호 스피너*/
    public class BusRouteSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();

                spinner_array.add("선택");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getRoute_num());
                }
                spinner_project_route_list.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_route_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String st_project_route_list= spinner_project_route_list.getSelectedItem().toString();
                        if (!st_project_route_list.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_project_route_list == bus_officeVOS.get(j).getRoute_num()){
                                    st_project_route_list_value= bus_officeVOS.get(j).getRoute_id();    // 노선번호 선택 값
                                }
                            }
                        }


                        ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<Bus_OfficeVO>> call1= erp1.UnitCode_VersionSpinner();
                        new UnitCode_VersionSpinner().execute(call1);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        }

    }//BusRouteSpinner()........




    public class UnitCode_VersionSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");
                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getUnit_ver());
                }
                spinner_project_bus_ver.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_bus_ver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String st_unit_ver= spinner_project_bus_ver.getSelectedItem().toString();
                        if (!st_unit_ver.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_unit_ver == bus_officeVOS.get(j).getUnit_ver()){
                                    st_unit_ver_value= bus_officeVOS.get(j).getUnit_ver();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }//UnitCode_VersionSpinner().........


    }//Fragment..



