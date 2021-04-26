package app.erp.com.erp_app.document_care.myfragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.Barcode_My_list_Activity;
import app.erp.com.erp_app.Barcode_bus_Activity;
import app.erp.com.erp_app.Barcode_garage_input_Activity;
import app.erp.com.erp_app.Barcode_garage_output_Activity;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.Gtv_Error_Install_Activity;
import app.erp.com.erp_app.New_Bus_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.ReserveItemRepairActivity;
import app.erp.com.erp_app.Work_Report_Activity;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;
import app.erp.com.erp_app.document_care.myInstallSignFragments.MyInstallationSignActivity;
import app.erp.com.erp_app.error_history.Error_History_Activity;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.over_work.Over_Work_Activity;
import app.erp.com.erp_app.over_work.Over_Work_Approval_Activity;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import app.erp.com.erp_app.vo.Bus_infoVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment1 extends Fragment implements View.OnClickListener {

    // 스피너에 쓰일 ArrayList -- 차량, 영업소, 노선, 구분 스피너
    static List<String> spinner_array_project_bus_list, array_spinner_project_garage, array_spinner_project_route_list, array_spinner_prj_base_infra_job;

    DrawerLayout drawer;
    Context mContext;
    Spinner project_bus_list, bus_num_list, infra_job_spinner, spinner_prj_base_infra_job, spinner_office_group, spinner_project_transp, spinner_project_garage, spinner_project_route_list;
    TextView tvOfficeGroup, tvVersion;
    static String bus_num, selected_st_bus_list, st_bus_list, st_job_name, st_job_name_value, st_prj_base_infra_job, st_prj_base_infra_job_value, st_project_transp_value, st_project_garage, st_project_garage_value, st_project_route_list, st_project_route_list_value, st_busoff_name, st_busoff_name_value, st_job_type;
    static String st_bus_list_id, st_office_group_value, st_version_value;
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    static String ItemType_C;
    static String ItemType_P;
    static String ItemName;
    static Uri uri, selectedImageUri;
    static String imgUriPath;   //어댑터에서 sharedPreference를 통해 전달받은 imgUri 경로
    private Button btnRegisterNewBus, btnSearchBusNum;
    private EditText EtBusNum;

    /* job text 리사이클러뷰 */
    private RecyclerView recyclerView;
    private ArrayList<JobTextItems> jobTextItems;
    private Job_Text_Adapter_P_C job_text_adapter_p_c;

    public Button btn_insert;
    static TextView etProject_bus_list;

    private Map<String, Object> sign_map = new HashMap<>();
    private List<String> seq_list = new ArrayList<>();
    private Map<String, Object> mRequest_map = new HashMap<>();
    private Map<String, Object> image_path_map = new HashMap<>();

    private ArrayList<String> path_list= new ArrayList<String>();

    static String mPath,DB_Path, GarryValue, DTTI, DTTI2, REG_EMP_ID, TRANSP_BIZR_ID, BUSOFF_NAME, GARAGE_ID, GARAGE_NAME, ROUTE_ID, ROUTE_NUM, BUS_ID, TempBusId, TempBusId_Value, BUS_NUM, VEHICLE_NUM, JOB_TYPE, TABLE_NAME; //파일명 바꿀때/ 파라미터 필요
    static String selectedNum;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    Bitmap bm, resizeBitmap;
    ImageView ivBitmap;
    File bmImageFile;

    static ImageView photoBitmap;
    public MyPageFragment1() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*ActionBar ab= ((MyProject_Work_Insert_Activity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.pager1_my_project_work_insert_fragment_1, container, false);

            tedPermission();

            /*if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                int permissionOnResult=getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionOnResult==PackageManager.PERMISSION_DENIED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            }*/

            getActivity().setTitle("1단계");

            ivBitmap = rootView.findViewById(R.id.ivBitmap);

            drawerLayout= rootView.findViewById(R.id.drawer_layout);
            nav= rootView.findViewById(R.id.nav);
            nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Toast.makeText(getActivity(), "!!!!", Toast.LENGTH_SHORT).show();
                    switch (item.getItemId()){
                        case R.id.home:
                            Intent i_1 = new Intent(getContext() , Call_Center_Activity.class);
                            startActivity(i_1);
                            break;
                        case R.id.nav_camera:
                            Intent i = new Intent(getContext() , Barcode_bus_Activity.class);
                            startActivity(i);
                            break;
                        case R.id.reserve_item_give:
                            Intent i1 = new Intent(getContext() , Barcode_garage_input_Activity.class);
                            startActivity(i1);
                            break;
                        case R.id.reserve_item_return:
                            Intent i2 = new Intent(getContext() , Barcode_garage_output_Activity.class);
                            startActivity(i2);
                            break;
                        case R.id.my_barcode_workList:
                            Intent i3 = new Intent(getContext() , Barcode_My_list_Activity.class);
                            startActivity(i3);
                            break;
                        case R.id.new_bus_insert:
                            Intent i4 = new Intent(getContext() , New_Bus_Activity.class);
                            startActivity(i4);
                            break;
                        case R.id.gtv_error_install:
                            Intent i5 = new Intent(getContext() , Gtv_Error_Install_Activity.class);
                            startActivity(i5);
                            break;
                        case R.id.reserve_item_repair:
                            Intent i6 = new Intent(getContext() , ReserveItemRepairActivity.class);
                            startActivity(i6);
                            break;
                        case R.id.work_report_btn:
                            Intent i7 = new Intent(getContext() , Work_Report_Activity.class);
                            startActivity(i7);
                            break;
                        case R.id.installation_List_signature:
                            Intent i8 = new Intent(getContext() , Installation_List_Signature_Activity.class);
                            startActivity(i8);
                            break;
                        case R.id.my_installation_sign:
                            Intent i9 = new Intent(getContext() , MyInstallationSignActivity.class);
                            startActivity(i9);
                            break;
                        case R.id.trouble_serch_btn:
                            Intent i10 = new Intent(getContext() , Error_History_Activity.class);
                            startActivity(i10);
                            break;
                        case R.id.over_work_btn:
                            Intent i11 = new Intent(getContext() , Over_Work_Activity.class);
                            startActivity(i11);
                            break;
                        case R.id.over_work_approval_btn:
                            Intent i12 = new Intent(getContext() , Over_Work_Approval_Activity.class);
                            startActivity(i12);
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null) {
                selectedNum = bundle.getString("SelectedNum");    //아이템 선택값 전달받기..
            }

                /*st_job_name = "";
                st_job_name_value = "";
                st_office_group_value = "";
                st_version_value = "";
                TABLE_NAME = "";
                st_job_name = "";
                G.transpBizrId = "";
                G.busoffName = "";
                G.TempBusId = "";
                G.TempBusNum = "";
                G.regEmpId = "";
                G.garageId = "";
                G.garageName = "";
                G.routeId = "";
                G.routeNum = "";
                G.vehicleNum = "";
                G.jopType = "";
                G.Last_seq = "";*/


            for (int i = 0; i < Garray.value.length; i++) {
                Garray.value[i] = null;
            }

            for (int i = 0; i < Garray.PositionInfo.length; i++) {
                Garray.PositionInfo[i][0] = 0;
                Garray.PositionInfo[i][1] = 0;
            }


            //myPageFragment1= new MyPageFragment1();  //또 생성??하면 안됨;

            /*작업 스피너*/
            infra_job_spinner = rootView.findViewById(R.id.infra_job_spinner);
            ERP_Spring_Controller erp_job_name = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_OfficeVO>> call_job_name = erp_job_name.JobNameSpinner();
            new JobNameList().execute(call_job_name);

            tvOfficeGroup = rootView.findViewById(R.id.tv_office_group);
            tvVersion = rootView.findViewById(R.id.tv_version);
            Log.d(" st_version_value 값===> ", st_version_value + "");  //null. 왜?? 이벤트를 안줘서??  //코딩은 순서대로.. 버전이 아직 선택이 되지도 않았음.

            spinner_project_transp = rootView.findViewById(R.id.project_transp);              /*운수사 스피너*/
            spinner_project_garage = rootView.findViewById(R.id.project_garage_spinner);     /*영업소 스피너*/
            spinner_project_route_list = rootView.findViewById(R.id.project_route_list);     /*노선번호 스피너*/
            // spinner_project_bus_ver= rootView.findViewById(R.id.project_bus_ver);         /*버전 스피너*/
            spinner_prj_base_infra_job = rootView.findViewById(R.id.prj_base_infra_job);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_job_text);

            //차량번호 스피너
            project_bus_list = rootView.findViewById(R.id.project_bus_list);
            bus_num_list = rootView.findViewById(R.id.bus_num_list);


            /* NOTE: [차량신규등록]버튼 */
            btnRegisterNewBus = rootView.findViewById(R.id.btn_register_new_bus);
            btnRegisterNewBus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), New_Bus_Activity.class);   //차량 신규등록 화면으로 이동
                    // i.putExtra("busNumVal",);  //차량번호 선택값 전달
                    getContext().startActivity(i);
                }
            });

            // NOTE: 키보드 올리기
            EtBusNum= rootView.findViewById(R.id.et_bus_num);
            /*EtBusNum.requestFocus();
            InputMethodManager imm= (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            EtBusNum.setRawInputType(InputType.TYPE_CLASS_NUMBER);*/

            /* NOTE: [차량검색]버튼 */
            btnSearchBusNum= rootView.findViewById(R.id.btn_search_bus_num);
            btnSearchBusNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //et값 없으면 이벤트 걸기
                    if (st_job_name == null){
                        Toast.makeText(getContext(), "프로젝트를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }else if (EtBusNum.getText().toString().length() < 2) {
                        Toast.makeText(getContext(), "차량번호를 두자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                        // <영업소, 노선, 구분> 스피너 초기화
                        project_bus_list.setAdapter(null);
                        spinner_project_garage.setAdapter(null);
                        spinner_project_route_list.setAdapter(null);
                        spinner_prj_base_infra_job.setAdapter(null);
                        //리사이클러뷰 초기화
                        jobTextItems= new ArrayList<>();
                    }else {
                        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<Bus_infoVo>> call = erp.getBusNumList(EtBusNum.getText().toString(), st_office_group_value);  //안드로이드에서 스프링으로 [파라미터]를 전달하면 스프링쪽에서 map으로 전달받음
                        new getBusNumListSpinner().execute(call);

                        // <영업소, 노선, 구분> 스피너 초기화
                        project_bus_list.setAdapter(null);
                        spinner_project_garage.setAdapter(null);
                        spinner_project_route_list.setAdapter(null);
                        spinner_prj_base_infra_job.setAdapter(null);
                        //리사이클러뷰 초기화
                        jobTextItems= new ArrayList<>();

                        // [차량검색]버튼 클릭후 - 키보드 숨기기/내리기
                        InputMethodManager imm= (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(EtBusNum.getWindowToken(), 0);
                    }
                }
            });



            /* NOTE: [다음]버튼 */
            btn_insert = rootView.findViewById(R.id.btn_insert);
            btn_insert.setOnClickListener(this);



        return rootView;
    }//onCreate...




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "사진촬영 허용", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "사진촬영 불가", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }




    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(getContext(), "권한요청 성공.......", Toast.LENGTH_SHORT).show();
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
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }




    /*[차량검색]버튼을 누르면 실행되는 스피너*/
    public class getBusNumListSpinner extends AsyncTask<Call, Void, List<Bus_infoVo>>{

        @Override
        protected List<Bus_infoVo> doInBackground(Call... calls) {
            Call<List<Bus_infoVo>> call= calls[0];
            try {
                Response<List<Bus_infoVo>> response= call.execute();  // 여기서 응답받기 시도
                return response.body();                               // 스프링에서 <List 타입>의 데이터를 못받으면 response.body() 여기가 실행안됨.
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_infoVo> bus_infoVos) {
            super.onPostExecute(bus_infoVos);

            if (bus_infoVos.size() == 0){
                Toast.makeText(getContext(), "차량번호가 존재하지 않습니다. 차량등록을 해주세요.", Toast.LENGTH_SHORT).show();
                spinner_array_project_bus_list= new ArrayList<>();

            } else if (bus_infoVos != null){
                spinner_array_project_bus_list= new ArrayList<>();
                spinner_array_project_bus_list.add("차량선택");
                for (int i=0; i<bus_infoVos.size(); i++){
                    spinner_array_project_bus_list.add(bus_infoVos.get(i).getBusoff_bus());
                }
                project_bus_list.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array_project_bus_list));
                project_bus_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_bus_list= project_bus_list.getSelectedItem().toString();
                        if (!st_bus_list.equals("차량선택")){
                            for (int j=0; j<bus_infoVos.size(); j++){
                                if (st_bus_list == bus_infoVos.get(j).getBusoff_bus()){
                                    selected_st_bus_list= bus_infoVos.get(j).getBusoff_bus();
                                    G.st_bus_list= st_bus_list;
                                    st_bus_list_id= bus_infoVos.get(j).getBus_id();
                                    G.st_bus_list_id= st_bus_list_id;
                                    st_project_transp_value= bus_infoVos.get(j).getTransp_bizr_id();
                                    G.transpBizrId = st_project_transp_value;

                                    Log.d("차량번호", bus_infoVos.get(j).getBusoff_bus());
                                    Log.d("st_bus_list", st_bus_list+"");        // ex; 경진여객_경기76아1234
                                    Log.d("st_bus_list_id", st_bus_list_id+"");  //ex; 141761234
                                }
                            }
                            ////영업소 스피너로 파라미터 넘기기
                            ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call = erp.GarageSpinner(st_project_transp_value, st_bus_list_id);
                            new GarageSpinner().execute(call);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


    /*[차량검색]버튼 클릭하면-    영업소 & 노선 스피너 둘다 가져오는 메소드... */
    public class BusGarageRouteSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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
                array_spinner_project_garage= new ArrayList<>();
                //spinner_array.add("선택");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    array_spinner_project_garage.add(bus_officeVOS.get(i).getGarage_name());
                }
                spinner_project_garage.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, array_spinner_project_garage));
                spinner_project_garage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_project_garage= spinner_project_garage.getSelectedItem().toString();
                        if (!st_project_garage.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_project_garage == bus_officeVOS.get(j).getGarage_name()){
                                    st_project_garage_value = bus_officeVOS.get(j).getGarage_id();
                                    st_project_route_list_value = bus_officeVOS.get(j).getRoute_num();
                                    Log.d("st_project_garage", st_project_garage+"");
                                    Log.d("st_project_garage_value", st_project_garage_value+"");
                                    Log.d("st_route", st_project_route_list_value+"");
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
    }




    /*운수사 스피너*/
    public class Transp extends AsyncTask<Call, Void, List<Bus_OfficeVO>> {

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call = calls[0];
            try {
                Response<List<Bus_OfficeVO>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null) {
                List<String> spinner_array = new ArrayList<>();

                spinner_array.add("선택");

                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    spinner_array.add(bus_officeVOS.get(i).getBusoff_name());
                }
                spinner_project_transp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_transp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        st_busoff_name = spinner_project_transp.getSelectedItem().toString();

                        if (!st_busoff_name.equals("선택")) {
                            for (int j = 0; j < bus_officeVOS.size(); j++) {
                                if (st_busoff_name == bus_officeVOS.get(j).getBusoff_name()) {
                                    st_busoff_name_value = bus_officeVOS.get(j).getBusoff_name();     //선택된 운수회사명
                                    st_project_transp_value = bus_officeVOS.get(j).getTransp_bizr_id();   //선택된 운수사 고정번호
                                    mRequest_map.put("bussOffName", st_busoff_name_value);
                                    mRequest_map.put("bussOffNameValue", st_project_transp_value);


                                    G.transpBizrId = st_project_transp_value;

                                    Log.d("운수회사 고정번호: ", st_project_transp_value + "");
                                    Log.d("운수회사명: ", st_busoff_name + "");
                                }
                            }

                            /*영업소 스피너로 파라미터 넘기기*/
                            /*ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call = erp.GarageSpinner(st_project_transp_value);
                            new GarageSpinner().execute(call);*/


                            /*노선번호 스피너로 파라미터 넘기기*/
                            /*ERP_Spring_Controller erp1 = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call1 = erp1.BusRouteSpinner(st_project_transp_value);
                            new BusRouteSpinner().execute(call1);*/
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
    public class JobNameList extends AsyncTask<Call, Void, List<Bus_OfficeVO>> {

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call = calls[0];
            try {
                Response<List<Bus_OfficeVO>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null) {
                List<String> spinner_array = new ArrayList<>();
                spinner_array.add("선택");
                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    spinner_array.add(bus_officeVOS.get(i).getPrj_name());
                }
                infra_job_spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));

                if (Integer.parseInt(selectedNum)!=0){
                    infra_job_spinner.setSelection(Integer.parseInt(selectedNum));  //전 화면에서 선택한 값 전달받기..
                }
                    infra_job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            st_job_name = infra_job_spinner.getSelectedItem().toString();
                            if (!st_job_name.equals("선택")) {
                                for (int j = 0; j < bus_officeVOS.size(); j++) {
                                    if (st_job_name == bus_officeVOS.get(j).getPrj_name()) {                //작업 선택 이름
                                        st_job_name_value = bus_officeVOS.get(j).getPrj_name();             //작업 선택 [값]
                                        st_office_group_value = bus_officeVOS.get(j).getOffice_group();     //선택된 조합
                                        st_version_value = bus_officeVOS.get(j).getVersion();               //선택된 버전
                                        TABLE_NAME = bus_officeVOS.get(j).getTable_name();

                                        mRequest_map.put("officeGroup",st_office_group_value);
                                        mRequest_map.put("version",st_version_value);

                                        G.prjName = TABLE_NAME;
                                        tvOfficeGroup.setText(st_office_group_value);
                                        tvVersion.setText(st_version_value);
                                        String job_type = bus_officeVOS.get(j).getJob_type();
                                        System.out.println("job_type????-====> " + job_type);  //null
                                    }
                                }


                                /*운수사 스피너*/
                                ERP_Spring_Controller erp1 = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                Call<List<Bus_OfficeVO>> call1 = erp1.BusOffName(st_office_group_value);
                               // new Transp().execute(call1);
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
    public class GarageSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>> {

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call = calls[0];
            try {
                Response<List<Bus_OfficeVO>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null) {
                List<String> spinner_array = new ArrayList<>();

                //spinner_array.add("선택");

                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    spinner_array.add(bus_officeVOS.get(i).getGarage_name());
                }
                spinner_project_garage.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_garage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_project_garage = spinner_project_garage.getSelectedItem().toString();
                        if (st_project_garage!=null) {
                            for (int j = 0; j < bus_officeVOS.size(); j++) {
                                if (st_project_garage == bus_officeVOS.get(j).getGarage_name()) {
                                    st_project_garage_value = bus_officeVOS.get(j).getGarage_id();
                                    Log.d("st_project_garage ========> ", st_project_garage + "");

                                    mRequest_map.put("garageName",st_project_garage);
                                    mRequest_map.put("garageId",st_project_garage_value);
                                }
                            }

                            ERP_Spring_Controller erp1 = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call1 = erp1.BusRouteSpinner(st_project_transp_value, st_bus_list_id);
                            new BusRouteSpinner().execute(call1);
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
    public class BusRouteSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>> {

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call = calls[0];
            try {
                Response<List<Bus_OfficeVO>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null) {
                array_spinner_project_route_list = new ArrayList<>();

                //spinner_array.add("선택");

                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    array_spinner_project_route_list.add(bus_officeVOS.get(i).getRoute_num());
                }
                spinner_project_route_list.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, array_spinner_project_route_list));
                spinner_project_route_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_project_route_list = spinner_project_route_list.getSelectedItem().toString();
                        if (st_project_route_list!=null) {
                            for (int j = 0; j < bus_officeVOS.size(); j++) {
                                if (st_project_route_list == bus_officeVOS.get(j).getRoute_num()) {
                                    st_project_route_list_value = bus_officeVOS.get(j).getRoute_id();    // 노선번호 선택 값
                                    Log.d("st_project_route_list 선택한 노선값 ========> ", st_project_route_list + "");

                                    mRequest_map.put("routeNum",st_project_route_list);
                                    mRequest_map.put("routeId",st_project_route_list_value);
                                }
                            }
                            ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call= erp.PrjBaseInfraJobSpinner();
                            new PrjBaseInfraJobSpinner().execute(call);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        }

    }//BusRouteSpinner()........






    /*작업-2 스피너 prj_base_infra_job*/
    public class PrjBaseInfraJobSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>> {

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call = calls[0];
            try {
                Response<List<Bus_OfficeVO>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null) {
                array_spinner_prj_base_infra_job = new ArrayList<>();

                array_spinner_prj_base_infra_job.add("선택");

                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    array_spinner_prj_base_infra_job.add(bus_officeVOS.get(i).getJob_name());
                }
                Toast.makeText(getContext(), "구분을 선택하세요.", Toast.LENGTH_SHORT).show();
                spinner_prj_base_infra_job.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, array_spinner_prj_base_infra_job));
                spinner_prj_base_infra_job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        st_prj_base_infra_job = spinner_prj_base_infra_job.getSelectedItem().toString();
                        if (!st_prj_base_infra_job.equals("선택")) {
                            for (int j = 0; j < bus_officeVOS.size(); j++) {
                                if (st_prj_base_infra_job == bus_officeVOS.get(j).getJob_name()) {
                                    st_prj_base_infra_job_value = bus_officeVOS.get(j).getJob_name();
                                    st_job_type = bus_officeVOS.get(j).getJob_type(); //null...

                                    mRequest_map.put("jobName",st_prj_base_infra_job);
                                    mRequest_map.put("jobNameValue",st_prj_base_infra_job_value);
                                    mRequest_map.put("jobType", st_job_type);
                                }
                            }
                            /*리사이클러뷰 카매라 앨범/ 촬영 데이터 넘기기*/
                            ERP_Spring_Controller erp_media = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call_media = erp_media.BusOffRecyclerviewMedia(st_office_group_value, st_version_value);
                            new busOffRecyclerviewMedia().execute(call_media);

                            //차량번호 스피너
                            /*ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call= erp.bus_num_list(G.transpBizrId);
                            Log.d("운수사 아이디:===> ", G.transpBizrId+"");
                            new getBusNumLists().execute(call);*/
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


    //차량번호 스피너
    private class getBusNumLists extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

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
                spinner_array.add("차량선택");
                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getBus_num());
                }
                project_bus_list.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                project_bus_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_bus_list= project_bus_list.getSelectedItem().toString();
                        if (!st_bus_list.equals("차량선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_bus_list == bus_officeVOS.get(j).getBus_num()){
                                    Log.d("차량번호 확인 : st_bus_list====> ", st_bus_list+"");
                                    G.st_bus_list= st_bus_list;    // == bus_num
                                    st_bus_list_id= bus_officeVOS.get(j).getBus_id();
                                    G.st_bus_list_id= st_bus_list_id;   // == bus_id
                                    Log.d("차량번호 확인 : st_bus_list_id====> ", st_bus_list_id+"");
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
    }






    /*리사이클러뷰: 카메라 앨범/ 촬영기능 etc 호출..*/
    public class busOffRecyclerviewMedia extends AsyncTask<Call, Void, List<Bus_OfficeVO>> {
        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call = calls[0];
            try {
                Response<List<Bus_OfficeVO>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            jobTextItems = new ArrayList<>();
            for (int i = 0; i < bus_officeVOS.size(); i++) {
                if (bus_officeVOS.get(i).getC_item_seq() != null && bus_officeVOS.get(i).getP_item_seq() != null) {             //1)   P, C 둘다 있을때
                    // C_item_seq : 바코드 입력부분
                    // P_item_seq : 사진 선택 부분
                    Garray.PositionInfo[i][0] = Integer.parseInt(bus_officeVOS.get(i).getC_item_seq());
                    Garray.PositionInfo[i][1] = Integer.parseInt(bus_officeVOS.get(i).getP_item_seq());
                    G.Last_seq = Math.max(Integer.parseInt(bus_officeVOS.get(i).getP_item_seq()), Integer.parseInt(bus_officeVOS.get(i).getC_item_seq())) + "";
                    jobTextItems.add(new JobTextItems(ItemName = bus_officeVOS.get(i).getItem_name()
                            , ""    //hint: 일련번호 입력
                            , bm
                            , "미리보기 (P,C 둘다)"
                            , ItemType_C = bus_officeVOS.get(i).getC_item_seq()
                            , ItemType_P = bus_officeVOS.get(i).getP_item_seq()));   //다시 set....
                } else if (bus_officeVOS.get(i).getC_item_seq() == null && bus_officeVOS.get(i).getP_item_seq() != null) {       //2)   P 만 있을 때
                    Garray.PositionInfo[i][1] = Integer.parseInt(bus_officeVOS.get(i).getP_item_seq());
                    G.Last_seq = bus_officeVOS.get(i).getP_item_seq();
                    jobTextItems.add(new JobTextItems(ItemName = bus_officeVOS.get(i).getItem_name()
                            , "해당없음"// 일련번호
                            , bm
                            , "미리보기  (P 만)"
                            , ""
                            , ItemType_P = bus_officeVOS.get(i).getP_item_seq()));
                } else if (bus_officeVOS.get(i).getC_item_seq() != null && bus_officeVOS.get(i).getP_item_seq() == null) {       //3)   C 만 있을 때
                    Garray.PositionInfo[i][0] = Integer.parseInt(bus_officeVOS.get(i).getC_item_seq());
                    G.Last_seq = bus_officeVOS.get(i).getC_item_seq();
                    jobTextItems.add(new JobTextItems(ItemName = bus_officeVOS.get(i).getItem_name()
                            , ""      //hint: 일련번호 입력
                            , bm
                            , "해당없음  (C 만)"
                            , ItemType_C = bus_officeVOS.get(i).getC_item_seq()
                            , ""));
                } else {
                    Toast.makeText(mContext, "데이터가 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            for (int i = 0; i < 50; i++) {
                Log.d("PositionInfo :>  ", i + "번째" + Garray.PositionInfo[i][0] + "/" + Garray.PositionInfo[i][1]);
            }

            job_text_adapter_p_c = new Job_Text_Adapter_P_C(getContext(), jobTextItems, MyPageFragment1.this);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
            recyclerView.setAdapter(job_text_adapter_p_c);
            job_text_adapter_p_c.notifyDataSetChanged();

        }
    }//busOffRecyclerviewMedia()...........






    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //[사진앨범]
        if (requestCode/100 == 3){
            Toast.makeText(getContext(), "이미지 불러오기", Toast.LENGTH_SHORT).show();
            Log.d("requestCodeValue", resultCode/300+"");
            if (resultCode == RESULT_OK){       //resultCode == RESULT_OK 이렇게 한번 더 확인해야 사진앨범에서 사진을 선택하지 않고 뒤로가기 버튼을 눌러도 앱이 꺼지지 않음..!!!
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                    selectedImageUri = data.getData();   // NOTE: selectedImage == Uri 타입임.

                    int column_index=0;
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContext().getContentResolver().query(selectedImageUri, proj, null, null, null);
                    if(cursor.moveToFirst()){
                        column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    }
                    String gallery_path =  cursor.getString(column_index);
                    Log.d("galleryPathCheck>>>", gallery_path+"");        // /storage/emulated/0/DCIM/Camera/20210312_153712.jpg
                    in.close();

                    //REG_DTTI (현재날짜)
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                    DTTI = sdf.format(date);
                    DTTI2 = sdf2.format(date);
                    G.dtti = DTTI;
                    G.dtti2 = DTTI2;
                    G.TempBusId=st_bus_list_id+"";
                    TempBusId_Value = G.TempBusId+"";
                    TRANSP_BIZR_ID = G.transpBizrId + "";
                    Garray.value[Garray.PositionInfo[G.position][1]] ="project_img/" + TABLE_NAME + "/" + DTTI2 + "/" + TABLE_NAME + "_" + DTTI2 + "_" + TRANSP_BIZR_ID + "_" + st_bus_list_id + "_" + Garray.PositionInfo[G.position][1] + ".jpg";
                    GarryValue = Garray.PositionInfo[G.position][1] + "";


                    // NOTE: 1) 이미지를 업로드 하는데 용량 너무 차지.. -> 리사이징을 하려면 Bitmap 으로 해야함.
                    // NOTE: Uri -> Bitmap 변경
                    bm = null;
                    if (selectedImageUri != null){
                        try {
                            bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImageUri);   //가로:960, 세로:1280
                            int bmWidth= bm.getWidth();
                            int bmHeight= bm.getHeight();

                            double Wratio= 0.0f;
                            double Hratio= 0.0f;
                            //Matrix matrix= new Matrix();
                                Wratio= (bmWidth >= bmHeight)?((double)bmWidth / (double)bmHeight) * 512 : 512 ;
                                Hratio= (bmHeight >= bmWidth)?((double)bmHeight / (double)bmWidth) * 512 : 512 ;
                                //matrix.postRotate(0);
                            resizeBitmap= bm.createScaledBitmap(bm, (int)Wratio, (int)Hratio, true);
                            //resizeBitmap= resizeBitmap.createBitmap(resizeBitmap, 0, 0, (int)Wratio, (int)Hratio, matrix, true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }



                    //NOTE: 2) 리사이클러뷰 아이템 값 변경작업 (보여지는 화면)
                    JobTextItems item = jobTextItems.get(Math.floorMod(requestCode, 300));
                    //item.preview_uri= selectedImageUri;     // STATUS: 변경될 이미지 저장 = 원본
                    item.preview_bm = resizeBitmap;                     // EDIT: Uri -> Bitmap 로 변경 및 저장

                    DB_Path = Garray.value[G.position];
                    job_text_adapter_p_c.notifyDataSetChanged();    // STATUS: 변경됐다고 adapter 에 알리기 notify




                    //NOTE: 3) 변경된 Bitmap (resizeBitmap) 을 File 형식으로 변환.. -> 새로운 파일생성
                    String timeStamp= new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
                    String imgFileName= "JPEG_"+timeStamp+".jpg";
                    bmImageFile= null;
                    File storageDir= new File(Environment.getExternalStorageDirectory()+"/IERP");

                    if (!storageDir.exists()){
                        storageDir.mkdirs();
                    }else {
                        Log.d("storage체크체크!!!!!!!!>>",storageDir.toString());
                    }
                    bmImageFile= new File(storageDir, imgFileName);
                    try {
                        bmImageFile.createNewFile();
                        FileOutputStream out= new FileOutputStream(bmImageFile);
                        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        Log.d("예외발생>>", e.toString());
                        e.printStackTrace();
                    }
                    Log.d("비트맵이미지파일생성>>", bmImageFile.toString());    // /storage/emulated/0/IERP/JPEG_20210422_1420.jpg



                    path_list.add(bmImageFile + "&"+ ("nas_image/image/IERP/" + TABLE_NAME + "/" + DTTI2+ "/" + TABLE_NAME + "_" + DTTI2+ "_" + TRANSP_BIZR_ID + "_" + st_bus_list_id + "_" + GarryValue + ".jpg").replaceAll("/","%"));
                    Log.d("path_list===========> ", path_list+"");    //[/storage/emulated/0/DCIM/Camera/IMG_20210222_001322.jpg&nas_image%image%IERP%PRJ_BUS_01004%20210222%PRJ_BUS_01004_20210222_4100200_141101234_1.jpg]
                    int cnt = 0;

                    for (String str : path_list){
                        cnt++;
                        System.out.println(cnt+" :  "+str);
                        sign_map.put("sign"+cnt, str);
                    }
                    Log.d("sign_map **************** ", sign_map.size()+"");
                    Log.d("sign_map **************** ", sign_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } //[사진촬영]
        else if (requestCode/100 ==2){
            if (resultCode == RESULT_OK){
                Log.d("ㅇㅇ", "카메라앱 결과 가져옴!!!!!");
                Log.d("카메라앱", resultCode+", "+requestCode);

                // NOTE: ☆ 카메라 촬영 후 특정 단말의 경우 ex; Nexus, Gallaxy, Vega.. etc ☆
                // NOTE: onActivityResult 결과를 받는 쪽에서 data.getData()가 null 값이 경우가 있음..
                // NOTE: 이런경우 보통은 Bitmap 으로 data 값이 넘어오기 때문에
                // NOTE: Bitmap bitmap= (Bitmap) data.getExtras().get("data"); 으로 호출하거나
                // NOTE: 마지막에 저장된 이미지 Uri 를 가져오는 방법이 있다 - EXTERNAL_CONTENT_URI (아래 참조...)





                // STATUS: 1. 안드 11 ->  사진촬영 -> 촬영된 이미지 잘 불려짐
                // STATUS: 2. 안드 10 ->  사진촬영 -> 촬영된 이미지는 잘 저장이 되어있으나 다른 경로에서 다른 사진을 가져옴 ERROR
                // FIXME: NO IDEA WHAT TO DO !!

                // EDIT: 사진촬영을 실행하는 곳 (리사이클러뷰 어댑터- job_text_adapter_p_c)에서
                // EDIT: imgUri 를 G 클래스에 저장하고 여기서 불러줌..
                // EDIT: 리사이클러뷰 item의 값을 변경해주고 notify 해쥼..

                // STATUS: 이미지가 서버로 잘 전달이 되나 이미지 크기가 너무 큰 단점이 있음  (데이터 사이즈가 큰 경우, 기타 메로리 부족 문제)
                // NOTE: 1)   이미지 사이즈를 줄이기 위해 Bitmap 이용.
                // NOTE:      G.CAPTURED_IMAGE_URI 를 bitmap 으로 변경 및 사이즈 조정
                bm = null;
                if (G.CAPTURED_IMAGE_URI != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), G.CAPTURED_IMAGE_URI);   // EDIT:    uri -> bitmap 변경    //가로:960, 세로:1280
                        Log.d("bm체크체크>>", bm+"");                         //   android.graphics.Bitmap@4a63fb7
                        Log.d("uri체크체크>>", G.CAPTURED_IMAGE_URI.toString());    //   content://app.erp.com.erp_app/hidden/IERP/JPEG_20210422_1437.jpg

                        //원본 이미지 사이즈 가져오기
                        int bmWidth = bm.getWidth();
                        int bmHeight = bm.getHeight();
                        double Wratio = 0.0;
                        double Hratio = 0.0;

                        Matrix matrix = new Matrix();

                        if (bmWidth > bmHeight){
                            Wratio =  ((double)bmWidth / (double)bmHeight) * 512;
                            Hratio = 1 * 512;
                        }else {
                            Wratio = 1 * 512;
                            Hratio =  ((double)bmHeight / (double)bmWidth) * 512;
                        }
                        matrix.postRotate(90);
                        G.CAPTURED_IMAGE_BITMAP = bm.createScaledBitmap(bm,  (int) Wratio,  (int) Hratio , false);
                        G.CAPTURED_IMAGE_BITMAP = G.CAPTURED_IMAGE_BITMAP.createBitmap(G.CAPTURED_IMAGE_BITMAP, 0, 0, (int) Wratio, (int) Hratio, matrix, true);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }





                // NOTE: 2) 리사이클러뷰 아이템 값 변경작업
                JobTextItems item= jobTextItems.get(Math.floorMod(requestCode, 200));   //어댑터에서 리사이클러뷰 아이템의 position 까지 intent 로 보내주었으니 자리변경하여 아이템 값 잘 바꿔줌
                //item.preview_uri= G.CAPTURED_IMAGE_URI;    //STATUS: 원래 원본 코드 !!!!!
                item.preview_bm = G.CAPTURED_IMAGE_BITMAP;   //EDIT:   URI -> BITMAP      => 리사이클러뷰의 보여지는 화면만 바뀐것임.
                DB_Path = Garray.value[G.position];
                job_text_adapter_p_c.notifyDataSetChanged();     //리사이클러뷰 아이템 값 변경 알리기

                //TODO: 변경된 Bitmap 파일을 gallery_path (ex; /storage/emulated/0/IERP/JPEG_20210422_0915.jpg)- 파일 형식으로 바꿔야함.
               /**
                * File storage= getContext().getCacheDir();
                String timeStamp= new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
                String bmFileName= "JPEG_"+timeStamp+".jpg";
                File tempFile= new File(storage, bmFileName);
                try {
                    tempFile.createNewFile();
                    FileOutputStream out= new FileOutputStream(tempFile);
                    G.CAPTURED_IMAGE_BITMAP.compress(Bitmap.CompressFormat.JPEG, 90, out);   //넘겨받은 bitmap 을 jpeg(손실압축)으로 저장해줌.
                    out.close();   //마무리로 닫아줌.
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("tempFile체크체크>>", tempFile.toString());    // /data/user/0/app.erp.com.erp_app/cache/JPEG_20210422_1016.jpg
                                                                         // /storage/emulated/0/IERP/JPEG_20210325_0955.jpg  **/
                //TODO: tempFile 은 그냥 File 의 경로가 다르기때문에 new File 생성..
                String timeStamp= new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
                String imgFileName= "JPEG_"+timeStamp+".jpg";
                bmImageFile= null;
                File storageDir= new File(Environment.getExternalStorageDirectory()+"/IERP");

                if (!storageDir.exists()){
                    storageDir.mkdirs();
                }else {
                  Log.d("storage체크체크>>",storageDir.toString());
                }
                bmImageFile= new File(storageDir, imgFileName);
                try {
                    bmImageFile.createNewFile();
                    FileOutputStream out= new FileOutputStream(bmImageFile);
                    G.CAPTURED_IMAGE_BITMAP.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.d("예외발생>>", e.toString());
                    e.printStackTrace();
                }

                Log.d("이미지파일확인>>", bmImageFile.toString());      // /storage/emulated/0/IERP/JPEG_20210422_1035.jpg
                Log.d("비트맵경로확인>>", G.CAPTURED_IMAGE_BITMAP.toString());


                // NOTE: 2) 사진촬영 후 그 이미지 INSERT 및 UPDATE 작업
                //          -> gallery_path 와 path_list 생성
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                DTTI = sdf.format(date);
                DTTI2 = sdf2.format(date);
                G.dtti = DTTI;
                G.dtti2 = DTTI2;

                //Log.d("gallery_path체크체크>>", gallery_path);  // /storage/emulated/0/IERP/JPEG_20210422_0957.jpg
                // EDIT: gallery_path -> bmImageFile             // /storage/emulated/0/IERP/JPEG_20210422_0957.jpg
                Garray.value[Garray.PositionInfo[G.position][1]] ="project_img/" + TABLE_NAME + "/" + G.dtti2 + "/" + TABLE_NAME + "_" + G.dtti2 + "_" + G.transpBizrId + "_" + st_bus_list_id + "_" + Garray.PositionInfo[G.position][1] + ".jpg";
                path_list.add(bmImageFile + "&"+ ("nas_image/image/IERP/" + TABLE_NAME + "/" + DTTI2+ "/" + TABLE_NAME + "_" + DTTI2+ "_" + G.transpBizrId + "_" + st_bus_list_id + "_" + Garray.PositionInfo[G.position][1] + ".jpg").replaceAll("/","%"));
                Log.d("gallery_path ))))", bmImageFile+"");
                Log.d("path_list ))))", path_list+"");

                // NOTE: 업로드할 이미지의 경로 이름을 다르게 주기위한 작업
                int cnt=0;
                for (String str : path_list){
                    cnt++;
                    System.out.println(cnt+" :  "+str);
                    sign_map.put("sign"+cnt, str);
                }
                Log.d("sign_map ))))", sign_map.size()+"");
                Log.d("sign_map ))))", sign_map+"");
            }
        }
        // [바코드 스캐너]
        else {
            Log.d("600실행", "600실행");
            String barcode= intentResult.getContents();
            Log.d("바코드값", barcode+"");
            G.BARCODE= barcode;
            Garray.value[Garray.PositionInfo[G.position][0]]=barcode;
            //Item에 접근하기
            JobTextItems Item= jobTextItems.get(G.position);

            Item.busNum= barcode;
            job_text_adapter_p_c.notifyDataSetChanged();
        }
}//onActivityResult()...








    // NOTE: [다음 2단계로 이동]버튼
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_insert:
                if (st_job_name.equals("선택")) {
                    Toast.makeText(getContext(), "작업을 선택하세요.", Toast.LENGTH_SHORT).show();
                } else if (st_project_garage.equals("선택")) {
                    Toast.makeText(getContext(), "영업소를 선택하세요.", Toast.LENGTH_SHORT).show();
                } else if (st_project_route_list.equals("선택")) {
                    Toast.makeText(getContext(), "노선번호를 선택하세요.", Toast.LENGTH_SHORT).show();
                    // TODO: 이미지 업로드한 값이 없거나 입력한 일련번호가 없으면 이벤트주기
                }else if (   G.EDIT_TEXT_BUS_NUM == null &&
                             G.CAPTURED_IMAGE_URI == null &&
                            selectedImageUri == null){           // NOTE: selectedImage 는 사진앨범에서 선택한 이미지임.
                            Toast.makeText(getContext(), "이미지나 일련번호를 업로드 해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    pref = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    REG_EMP_ID = pref.getString("emp_id", "");  //이렇게??
                    System.out.println(" ### REG_EMP_ID : " + REG_EMP_ID);
                    G.regEmpId = REG_EMP_ID;

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    DTTI = sdf.format(date);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                    DTTI2= sdf2.format(date);
                    G.dtti = DTTI;//민혁 - 전역변수 할당 추가
                    G.dtti2 = DTTI2;//민혁 - 전역변수 할당 추가
                    //TRANSP_BIZR_ID  (선택된 운수사 고정번호)
                    TRANSP_BIZR_ID = G.transpBizrId;
                    System.out.println(" ### TRANSP_BIZR_ID 운수사 고정번호: " + TRANSP_BIZR_ID);

                    //BUSOFF_NAME
                    BUSOFF_NAME = st_busoff_name_value;
                    G.busoffName = BUSOFF_NAME;

                    System.out.println(" ### BUSOFF_NAME 운수사 명: " + BUSOFF_NAME);

                    //GARAGE_ID
                    GARAGE_ID = st_project_garage_value;
                    G.garageId = GARAGE_ID;
                    System.out.println(" ### GARAGE_ID 영업소 아이디: " + GARAGE_ID);

                    //GARAGE_NAME
                    GARAGE_NAME = st_project_garage;
                    G.garageName = GARAGE_NAME;
                    System.out.println(" ### GARAGE_NAME 영업소 아이디: " + GARAGE_NAME);

                    //ROUTE_ID
                    ROUTE_ID = st_project_route_list_value;
                    G.routeId = ROUTE_ID;
                    System.out.println(" ### ROUTE_ID 노선번호 아이디: " + ROUTE_ID);

                    //ROUTE_NUM
                    ROUTE_NUM = st_project_route_list;
                    G.routeNum = ROUTE_NUM;
                    System.out.println(" ### ROUTE_NUM 노선번호: " + ROUTE_NUM);

                    TempBusId_Value = G.TempBusId;    //경기/인천-> 141,128로 변경된 값
                    System.out.println(" ### TempBusId_Value : " + TempBusId_Value);

                    //st_bus_list = G.TempBusNum;
                    System.out.println(" ### st_temp_bus_id : " + G.st_bus_list);

                    VEHICLE_NUM = "12345678901234567";
                    G.vehicleNum = VEHICLE_NUM;

                    //JOB_TYPE
                    JOB_TYPE = st_job_type;
                    G.jopType = JOB_TYPE;
                    System.out.println(" ### JOB_TYPE 작업타입: " + JOB_TYPE);


                    Log.d("ItemType_P", ItemName + "");
                   // Garray.value[G.position]="project_img/" + TABLE_NAME +"/"+ DTTI+ "/"+ TABLE_NAME + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + G.TempBusId + "_"  + G.position+  ".jpg";

                    //전역변수 Garray 클래스에 잘 저장이 되어있는지 출력해보기
                    for (int i = 1; i <= Integer.parseInt(G.Last_seq); i++) {
                        Log.d("i >>>>", i + " = " + Garray.value[i]);
                    }

                    AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                    builder.setTitle("프로젝트 업무 등록");
                    builder.setMessage("작성하신 업무내역을 등록 하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Log.d("181818>>>>>>", "st_bus_list:["+G.st_bus_list+"]st_bus_list:["+G.st_bus_list+"]");
                            Call<String> call = erp.insert_prj_def_val(TABLE_NAME
                                    , DTTI
                                    , REG_EMP_ID
                                    , TRANSP_BIZR_ID
                                    , BUSOFF_NAME
                                    , GARAGE_ID
                                    , GARAGE_NAME
                                    , ROUTE_ID
                                    , ROUTE_NUM
                                    , st_bus_list_id
                                    , st_bus_list
                                    , VEHICLE_NUM
                                    , JOB_TYPE
                                    , Garray.value[1] + ""
                                    , Garray.value[2] + ""
                                    , Garray.value[3] + ""
                                    , Garray.value[4] + ""
                                    , Garray.value[5] + ""
                                    , Garray.value[6] + ""
                                    , Garray.value[7] + ""
                                    , Garray.value[8] + ""
                                    , Garray.value[9] + ""
                                    , Garray.value[10] + ""
                                    , Garray.value[11] + ""
                                    , Garray.value[12] + ""
                                    , Garray.value[13] + ""
                                    , Garray.value[14] + ""
                                    , Garray.value[15] + ""
                                    , Garray.value[16] + ""
                                    , Garray.value[17] + ""
                                    , Garray.value[18] + ""
                                    , Garray.value[19] + ""
                                    , Garray.value[20] + ""
                                    , Garray.value[21] + ""
                                    , Garray.value[22] + ""
                                    , Garray.value[23] + ""
                                    , Garray.value[24] + ""
                                    , Garray.value[25] + ""
                                    , Garray.value[26] + ""
                                    , Garray.value[27] + ""
                                    , Garray.value[28] + ""
                                    , Garray.value[29] + ""
                                    , Garray.value[30] + ""
                                    , Garray.value[31] + ""
                                    , Garray.value[32] + ""
                                    , Garray.value[33] + ""
                                    , Garray.value[34] + ""
                                    , Garray.value[35] + ""
                                    , Garray.value[36] + ""
                                    , Garray.value[37] + ""
                                    , Garray.value[38] + ""
                                    , Garray.value[39] + ""
                                    , Garray.value[40] + ""
                                    , Garray.value[41] + ""
                                    , Garray.value[42] + ""
                                    , Garray.value[43] + ""
                                    , Garray.value[44] + ""
                                    , Garray.value[45] + ""
                                    , Garray.value[46] + ""
                                    , Garray.value[47] + ""
                                    , Garray.value[48] + ""
                                    , Garray.value[49] + ""
                                    , Garray.value[50] + ""
                            );
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    //Toast.makeText(getContext(),"이미지 업로드 시작..." ,Toast.LENGTH_SHORT).show();
                                    new ImageUploadJson().execute();

                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getContext(),"이미지 업로드 오류 발생" ,Toast.LENGTH_SHORT).show();
                                    Log.d("이미지 업로드 오류 이유::: ",t+" : ");
                                }
                            });

                            Bundle bundle = new Bundle();
                            bundle.putString("fromfragment1", st_version_value + "");
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            MyPageFragment2 myPageFragment2 = new MyPageFragment2();
                            myPageFragment2.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, myPageFragment2, null).addToBackStack(null).commit();
                            //[다음]버튼 누르고 MyPageFragment2 페이지로 이동
                            //프로젝트 스피너 초기화해주기...
                           /* st_job_name = "";
                            st_job_name_value = "";
                            st_office_group_value = "";
                            st_version_value = "";
                            TABLE_NAME = "";
                            st_job_name = "";
                            sign_map.isEmpty();
                            path_list.clear();*/
                            ERP_Spring_Controller erp_job_name = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call_job_name = erp_job_name.JobNameSpinner();
                            new JobNameList().execute(call_job_name);

                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "업무내역 등록을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }//else..
        }


    }


    private class ImageUploadJson extends AsyncTask<Void, Void, Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(getContext());
            progressDialog.setMessage("이미지 업로드 중....");
            progressDialog.setCancelable(false);
            progressDialog.show();   //새로운 progressDialog 를 생성한 후 생성된 객체를 반환하는 static 함수임..
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                int sign_map_size= sign_map.size();
                Log.d("sign_map_size 2222=====> ", sign_map_size+"");
                Log.d("sign_map 2222222======>  ",  sign_map+"");
                Boolean jsonObject= JSONParser.uploadImage(sign_map);
                if (jsonObject != null)
//                    Log.d(" result ::" , "" + jsonObject.getString("result").equals("success"));
                    return jsonObject;
            } catch (Exception e) {
                Log.i("frag_TAG", "Error : " + e.getLocalizedMessage());
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();   //작업끝나고 dismiss
            if (aBoolean){

            }else {
                Toast.makeText(getContext(),"이미지 업로드 오류 발생 !!" ,Toast.LENGTH_SHORT).show();
                //onPreExecute();   //이미지 오류일때 progressDialog 다시 실행??
            }
        }
    }//ImageUploadJson()...










}//Fragment end....

