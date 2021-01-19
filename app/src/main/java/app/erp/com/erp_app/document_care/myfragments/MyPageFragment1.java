package app.erp.com.erp_app.document_care.myfragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.PreviewDialogActivity;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;
import com.bumptech.glide.module.AppGlideModule;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment1 extends Fragment {
    Context mContext;
    Spinner infra_job_spinner, spinner_office_group, spinner_project_transp, spinner_project_garage, spinner_project_route_list, spinner_project_bus_ver;
    static String st_job_name_value, st_office_group_value, st_project_transp_value, st_project_garage_value, st_project_route_list_value, st_busoff_name, st_busoff_name_value, st_unit_ver_value;
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    static String takePic_tag;
    static String preview_tag;
    static String ItemType;
    static String ItemName;
    static Uri uri;
    static String imgUriPath;   //어댑터에서 sharedPreference를 통해 전달받은 imgUri 경로

    /* job text 리사이클러뷰 */
    private RecyclerView recyclerView;
    private ArrayList<JobTextItems> jobTextItems;
    private Job_Text_Adapter job_text_adapter;
    static MyPageFragment1 myPageFragment1;
    String mPicturePath;
    String mCurrentPhotoPath;

    static Intent i;


    MyPageFragment2 myPageFragment2;

    private String stJobName;
    private String stOfficeGroup;
    Uri imgUri1;

    ImageView iv_test;
    Uri mImageCaptureUri=null;
    Uri mImgUri;

    public static MyPageFragment1 newInstance(String st_job_name_value, String st_office_group_value  ){
        MyPageFragment1 myPageFragment1= new MyPageFragment1();
        Bundle bundle= new Bundle();
        bundle.putString("jobName", st_job_name_value);
        bundle.putString("officeGroup", st_office_group_value);
        myPageFragment1.setArguments(bundle);
        return myPageFragment1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stJobName= getArguments().getString("jobName");
        stOfficeGroup= getArguments().getString("officeGroup");
    }

    private void askForPermission(){
        String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(getActivity(), permissions, 15);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 15){
            for (int i=0; i<permissions.length; i++){
                String permission= permissions[i];
                int grantResult= grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if (grantResult == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(mContext, "permission allowed!!!!!!!!", Toast.LENGTH_SHORT).show();
                    }else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 15);
                    }
                }
            }
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.pager1_my_project_work_insert_fragment, container, false);

        //iv_test= (ImageView)rootView.findViewById(R.id.iv_test);
        /*PermissionCheck();
        askForPermission();*/

        tedPermission();

        //ImageView iv_test= (ImageView)rootView.findViewById(R.id.iv_test);
        //myPageFragment1= new MyPageFragment1();  //또 생성??하면 안됨;

        /*작업 스피너*/
        infra_job_spinner= rootView.findViewById(R.id.infra_job_spinner);
        ERP_Spring_Controller erp_job_name= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call_job_name= erp_job_name.JobNameSpinner();
        new JobNameList().execute(call_job_name);

        /*조합 스피너*/
        spinner_office_group= rootView.findViewById(R.id.project_office_group);
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.OfficeGroupSpinner();
        new OfficeGroup().execute(call);

        spinner_project_transp= rootView.findViewById(R.id.project_transp);           /*운수사 스피너*/
        spinner_project_garage= rootView.findViewById(R.id.project_garage_spinner);   /*영업소 스피너*/
        spinner_project_route_list= rootView.findViewById(R.id.project_route_list);   /*노선번호 스피너*/
        spinner_project_bus_ver= rootView.findViewById(R.id.project_bus_ver);         /*버전 스피너*/

        recyclerView= (RecyclerView)rootView.findViewById(R.id.recyclerview_job_text);




        return  rootView;


    }//onCreate...


    @GlideModule
    public class MyGlideApp extends AppGlideModule{

    }




    private void tedPermission(){
        PermissionListener permissionListener= new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getContext(), "권한요청 성공.......", Toast.LENGTH_SHORT).show();
                //권한요청 성공
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //권한요청 실패
            }
        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setRationaleMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


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
                        Log.d("asdf :: ", "["+st_office_group+"], ["+spinner_office_group.getSelectedItem().toString()+"]");
                        if ( !st_office_group.equals("선택") ){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_office_group == bus_officeVOS.get(j).getOffice_group()){        // 운수사명 (조합)
                                    st_office_group_value = bus_officeVOS.get(j).getOffice_group();    // 선택한 운수사명 값

                                    //bundle로 데이터 던지기
                                    myPageFragment2= new MyPageFragment2();
                                    Bundle bundle= new Bundle();
                                    bundle.putString("st_office_group", st_office_group);
                                    bundle.putString("st_office_group_value", st_office_group_value);
                                    myPageFragment2.setArguments(bundle);
                                }
                            }
                            ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call1= erp1.BusOffName(st_office_group_value);      // 선택한 운수사명 값 전달받음
                            new Transp().execute(call1);
                        }



                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }//JobType()....


    /*운수사 스피너*/
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
                            /*리사이클러뷰 카매라 앨범/ 촬영 데이터 넘기기*/
                            ERP_Spring_Controller erp_media= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call_media= erp_media.BusOffRecyclerviewMedia(st_office_group_value);
                            new busOffRecyclerviewMedia().execute(call_media);

                            /*영업소 스피너로 파라미터 넘기기*/
                            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call= erp.GarageSpinner(st_project_transp_value);
                            new GarageSpinner().execute(call);

                            /*노선번호 스피너로 파라미터 넘기기*/
                            ERP_Spring_Controller erp1= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call1= erp1.BusRouteSpinner(st_project_transp_value);
                            new BusRouteSpinner().execute(call1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                }
            }

        }//Transp().....


    /*작업 스피너*/
    public class JobNameList extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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
                    spinner_array.add(bus_officeVOS.get(i).getJob_name());
                }
                infra_job_spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                infra_job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String st_job_name= infra_job_spinner.getSelectedItem().toString();
                        if (!st_job_name.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_job_name == bus_officeVOS.get(j).getJob_name()){
                                    st_job_name_value= bus_officeVOS.get(j).getJob_type();
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
    }//jobNameList()



    /*영업소 스피너*/
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



    /*버전 스피너*/
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


    /*리사이클러뷰: 카메라 앨범/ 촬영기능 etc 호출..*/
    public class busOffRecyclerviewMedia extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{
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

            jobTextItems= new ArrayList<>();
            for (int i=0; i<bus_officeVOS.size(); i++){
                jobTextItems.add(new JobTextItems(  ItemName= bus_officeVOS.get(i).getItem_name()
                                                    , ItemType= bus_officeVOS.get(i).getItem_type()
                                                    , uri
                                                    ,"미리보기"
                                                    , bus_officeVOS.get(i).getItem_seq())) ;   //다시 set....
            }

            job_text_adapter= new Job_Text_Adapter(getContext(), jobTextItems, MyPageFragment1.this);
            recyclerView.setAdapter(job_text_adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));



        }
    }//busOffRecyclerviewMedia()...........


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("test",requestCode+"");
        // 아마 찍으면 일로올꺼에요 소스를 못찾겠네요
        // 실제 바코드로 찍어보면서 테스트 해야겠는데요  네 그러면 여기로 온다고 했을때
        // 그값은 스트링으로 오는건가요?? 그러니까 data죠?  resuld requestcode를 준것도 없어서 어떻게 값을 전달받는다ㄹ는 건지 모르겠어요
        // 바코드만 사용하는 메뉴라 카메라 호출할때 따로 코드를 준게없을걸...?
        /*IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("result ++++++ ", result+"    tt");
        String barcode= result.getContents();*/

        /*IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == 600 && resultCode == RESULT_OK){
            Log.d("result ++++++ ", result+"    tt");
        }*/

        /////////////////// 사진촬영 및 사진앨범
        switch (Math.floorDiv(requestCode,100)){    //Math.floorDiv(requestCode,100)
            case 2:
                if (resultCode == RESULT_OK){
                    Log.d("사진촬영::::: ", "결과를 가져온 intent case :20");   //실행

                    // Intent(Data)를 전달받지 못해서 Adapter(보낸쪽)에서 촬영한 사진 uri를 SharedPreference를 통해 전달받기
                    pref= getContext().getSharedPreferences("img_pref", Context.MODE_PRIVATE);
                    String test = pref.getString("camera_type","test");

                    // SharedPreference로 받아온 (String)uri을 Uri로 바꿔주기
                    // 1) File 로 바꿔주는 법
                    /*File f = new File(test);
                    Uri contentUri = Uri.fromFile(f);*/
                    // 2) Uri로 parse
                    Uri contentUri= Uri.parse(test);

                    //맨 아래있는 함수로 객체화 한 다음
                    // 그 걸 비트맵으로 바꿔서 지지고볶고 뭐어ㄸㅎ게하는데 그 함수가 setImageView 이거에요 네네..
                    // 저도 테스트 계속하면서 만든거라 소스 하나하나 기억이 가물가물하네요 다음은 어떤거죠 ?

                    // 찍히네여 이tttttttttttt: /storage/emulated/0/IERP/JPEG_20210118_0938.jpg걸로 하면 될것같은데 ㅇㅗ..그러면.. 다시 uri로 바꿔야하지 않나ㅛㅇ?
                    Log.d("tttttttttttttt",test);

                    if (data!=null){
                        Log.d("data 있음!!!!!!!!!!!!!!!!!!!!", data.getData()+"   tt");
                        Uri uri= data.getData();
                        if (contentUri!=null){
                            Log.d("uri 잇음", contentUri+" uri 있음");
                            //GlideApp.with(this).load(uri).into(iv_test);
                            // 여기서 객체화 하는 함수 있었던것같은데 어떤 객체요? 이미지를 객체화 하는 함수여

                            JobTextItems item= jobTextItems.get(Math.floorMod(requestCode, 100)); // 미리보기에 넣으실려고하는거죠 ? 네네 해보시고 안되면 제꺼 소스에 객체화하는 함수 있어요 그걸로 하시면 될꺼에요
                            //저도 경로 가져오고 객체화 시켜서 다시 넣은거라 한번 해보세여 다음 그 바코드는 어떤거요 ? 잠시만요 그런데 저 받아온 string은 uri로 바꿔주ㅑ 하는거 아닌가요?
                            // 어 그래서 객체화 하는소스에 그게 있었던것같아서  아.. 그 객체화라는게 뭐져??
                            item.takePic= contentUri;
                            job_text_adapter.notifyDataSetChanged();

                        }else {
                            Log.d("uri NULL ㅠ_ㅠ", uri+"   tt");    //지금 uri == null.....
                            if (contentUri!=null) Glide.with(this).load(contentUri).into(iv_test);
                        }
                    }else {
                        Log.d("data NULL ㅠ0ㅠ", data+"   dd");
                        if (uri!=null) Glide.with(this).load(uri).into(iv_test);
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK){
                    Log.d("사진앨범::  ", "결과를 가져온 intent case:30");   //실행
                    uri= data.getData();
                    if (data != null){
                        Log.d("data 있음!!!!!!!!!!!!!!!!!!!!", data+"   tt");
                        Log.d("uri 있음!!!!!!!!!!!!!!!!!!!!", uri+"   ss");
                        String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName= "JPEG_"+timeStamp+"";
                        File imageFile= null;
                        File storageDir= new File(Environment.getExternalStorageDirectory()+"/IERP");

                        if (!storageDir.exists()){
                            Log.d("storageDir 존재하지 않음", storageDir+"");
                            storageDir.mkdirs();
                        }

                        imageFile= new File(storageDir, imageFileName);
                        mCurrentPhotoPath= imageFile.getAbsolutePath();

                        Log.d("chk1 ==== > ",Math.floorMod(requestCode,100) + "/"+ requestCode );
                        JobTextItems item= jobTextItems.get(Math.floorMod(requestCode,100));
                        item.takePic= uri;
                        job_text_adapter.notifyDataSetChanged();

                        /*Intent intent= new Intent(getContext(), PreviewDialogActivity.class);
                        intent.putExtra("imgUri", uri);
                        getContext().startActivity(intent);*/
                        SharedPreferences pref= getContext().getSharedPreferences("imgUri_data",Context.MODE_PRIVATE );
                        SharedPreferences.Editor editor= pref.edit();
                        editor.putString("imgUri", uri.toString());
                        editor.commit();

                        //return imageFile;
                    }
                }break;
            case 6:
                Toast.makeText(mContext, "바코드 결과값", Toast.LENGTH_SHORT).show();
                //바코드 스캔 결과받기..
                if (resultCode == RESULT_OK){
                    Toast.makeText(mContext, "바코드 결과값", Toast.LENGTH_SHORT).show();
                    IntentResult result= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    String barcode= result.getContents();
                    Log.d("result===============> ", result+"    tt");
                    if (result != null){
                        Log.d("result===============> ", result+"    tt");
                    if (result.getContents() != null){
                        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                        builder.setMessage(result.getContents());
                        builder.setTitle("바코드 스캔중...");
                        builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scanCode();
                            }
                        }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                        AlertDialog dialog= builder.create();
                        dialog.show();
                    }
                    else {
                        Toast.makeText(getContext(), "다시 스캔하세요.", Toast.LENGTH_SHORT).show();
                    }
                    }else {
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                }break;
        }// Switch문..
    }

    private void scanCode(){
        IntentIntegrator integrator= new IntentIntegrator((Activity) getContext());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }



    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

}//Fragment end....

