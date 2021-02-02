package app.erp.com.erp_app.document_care.myfragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.MainActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;
import app.erp.com.erp_app.document_care.PreviewDialogActivity;
import app.erp.com.erp_app.document_care.ProJect_Doc_Write_Activity;
import app.erp.com.erp_app.document_care.ProJect_Work_Insert_Activity;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment1 extends Fragment implements View.OnClickListener {


    DrawerLayout drawer;
    Context mContext;
    Spinner infra_job_spinner, spinner_prj_base_infra_job, spinner_office_group, spinner_project_transp, spinner_project_garage, spinner_project_route_list;
    TextView tvOfficeGroup, tvVersion;
    static String st_job_name, st_job_name_value, st_prj_base_infra_job, st_prj_base_infra_job_value, st_project_transp_value, st_project_garage, st_project_garage_value, st_project_route_list, st_project_route_list_value, st_busoff_name, st_busoff_name_value, st_job_type;
    static String st_office_group_value, st_version_value;
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    static String ItemType_C;
    static String ItemType_P;
    static String ItemName;
    static Uri uri;
    static String imgUriPath;   //어댑터에서 sharedPreference를 통해 전달받은 imgUri 경로

    /* job text 리사이클러뷰 */
    private RecyclerView recyclerView;
    private ArrayList<JobTextItems> jobTextItems;
    private Job_Text_Adapter_P_C job_text_adapter_p_c;
    String mCurrentPhotoPath;

    public Button btn_insert;
    static TextView etProject_bus_list;

    private Map<String, Object> sign_map = new HashMap<>();
    private List<String> seq_list = new ArrayList<>();
    private Map<String, Object> mRequest_map = new HashMap<>();
    private Map<String, Object> image_path_map = new HashMap<>();

    /*private Map<String,Object> sign_map = new HashMap<>();*/
    static String mPath,DB_Path, GarryValue, st_temp_bus_id, area_code, unitNum, GLOBAL, DTTI, REG_EMP_ID, TRANSP_BIZR_ID, BUSOFF_NAME, GARAGE_ID, GARAGE_NAME, ROUTE_ID, ROUTE_NUM, BUS_ID, TempBusId, TempBusId_Value, BUS_NUM, VEHICLE_NUM, JOB_TYPE, TABLE_NAME; //파일명 바꿀때/ 파라미터 필요

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.pager1_my_project_work_insert_fragment, container, false);
        st_job_name = "";
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
        G.Last_seq = "";
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

        spinner_project_transp = rootView.findViewById(R.id.project_transp);           /*운수사 스피너*/
        spinner_project_garage = rootView.findViewById(R.id.project_garage_spinner);   /*영업소 스피너*/
        spinner_project_route_list = rootView.findViewById(R.id.project_route_list);   /*노선번호 스피너*/
        // spinner_project_bus_ver= rootView.findViewById(R.id.project_bus_ver);         /*버전 스피너*/
        spinner_prj_base_infra_job = rootView.findViewById(R.id.prj_base_infra_job);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_job_text);

        etProject_bus_list = rootView.findViewById(R.id.project_bus_list);


        etProject_bus_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final EditText et = new EditText(getContext());
                builder.setIcon(R.drawable.ic_insert);
                builder.setTitle("차량번호 입력").setMessage("차량번호를 입력하세요.");
                builder.setView(et);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (et.getText().length()>12){
                            Toast.makeText(getContext(), "차량번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }else {
                            if (et.getText().toString().contains("경기") || et.getText().toString().contains("인천")){
                                if (et.getText().toString().contains("아") || et.getText().toString().contains("바") || et.getText().toString().contains("사") || et.getText().toString().contains("자")){
                                    st_temp_bus_id = et.getText().toString();

                                    G.TempBusNum = st_temp_bus_id;
                                    Log.d("et_value :::: ", st_temp_bus_id + "");

                                    if (st_temp_bus_id.length() != 0) {
                                        switch (st_temp_bus_id.substring(0, 2)) {
                                            case "경기":
                                                area_code = "141";
                                                break;
                                            case "인천":
                                                area_code = "128";
                                                break;
                                        }
                                        /*작업-2 스피너*/
                                        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                        Call<List<Bus_OfficeVO>> call = erp.PrjBaseInfraJobSpinner();
                                        new PrjBaseInfraJobSpinner().execute(call);
                                    }
                                    etProject_bus_list.setText(st_temp_bus_id);
                                    etProject_bus_list.setTextColor(Color.parseColor("#000000"));
                                    etProject_bus_list.setTextSize(16);
                                    String bus_list_value = area_code + st_temp_bus_id.substring(2, 4) + st_temp_bus_id.substring(5, 9) + "";
                                    G.TempBusId = bus_list_value;
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(getContext(), "차량번호를 다시 입력해주세요. (아/바/사/자) ", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "차량번호를 다시 입력해주세요. (경기/ 인천)", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }//onClick..
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        // st_Project_bus_list= etProject_bus_list.getText().toString();
       /*btn_search= rootView.findViewById(R.id.btn_search);
       btn_search.setOnClickListener(this);*/


        /* [다음]버튼 */
        btn_insert = rootView.findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(this);

        return rootView;
    }//onCreate...


    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
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
                            ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call = erp.GarageSpinner(st_project_transp_value);
                            new GarageSpinner().execute(call);


                            /*노선번호 스피너로 파라미터 넘기기*/
                            ERP_Spring_Controller erp1 = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<List<Bus_OfficeVO>> call1 = erp1.BusRouteSpinner(st_project_transp_value);
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
                infra_job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_job_name = infra_job_spinner.getSelectedItem().toString();
                        if (!st_job_name.equals("선택")) {
                            for (int j = 0; j < bus_officeVOS.size(); j++) {
                                if (st_job_name == bus_officeVOS.get(j).getPrj_name()) {      //작업 선택 이름
                                    st_job_name_value = bus_officeVOS.get(j).getPrj_name();   //작업 선택 [값]
                                    st_office_group_value = bus_officeVOS.get(j).getOffice_group();  //선택된 조합
                                    st_version_value = bus_officeVOS.get(j).getVersion();    //선택된 버전
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
                            new Transp().execute(call1);
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

                spinner_array.add("선택");

                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    spinner_array.add(bus_officeVOS.get(i).getGarage_name());
                }
                spinner_project_garage.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_garage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_project_garage = spinner_project_garage.getSelectedItem().toString();
                        if (!st_project_garage.equals("선택")) {
                            for (int j = 0; j < bus_officeVOS.size(); j++) {
                                if (st_project_garage == bus_officeVOS.get(j).getGarage_name()) {
                                    st_project_garage_value = bus_officeVOS.get(j).getGarage_id();
                                    Log.d("st_project_garage ========> ", st_project_garage + "");

                                    mRequest_map.put("garageName",st_project_garage);
                                    mRequest_map.put("garageId",st_project_garage_value);
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
                List<String> spinner_array = new ArrayList<>();

                spinner_array.add("선택");

                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    spinner_array.add(bus_officeVOS.get(i).getRoute_num());
                }
                spinner_project_route_list.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
                spinner_project_route_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_project_route_list = spinner_project_route_list.getSelectedItem().toString();
                        if (!st_project_route_list.equals("선택")) {
                            for (int j = 0; j < bus_officeVOS.size(); j++) {
                                if (st_project_route_list == bus_officeVOS.get(j).getRoute_num()) {
                                    st_project_route_list_value = bus_officeVOS.get(j).getRoute_id();    // 노선번호 선택 값
                                    Log.d("st_project_route_list 선택한 노선값 ========> ", st_project_route_list + "");

                                    mRequest_map.put("routeNum",st_project_route_list);
                                    mRequest_map.put("routeId",st_project_route_list_value);
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

    }//BusRouteSpinner()........


    //검색버튼


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
                List<String> spinner_array = new ArrayList<>();

                spinner_array.add("선택");

                for (int i = 0; i < bus_officeVOS.size(); i++) {
                    spinner_array.add(bus_officeVOS.get(i).getJob_name());
                }
                spinner_prj_base_infra_job.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_array));
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
                            , uri
                            , "미리보기 (P,C 둘다)"
                            , ItemType_C = bus_officeVOS.get(i).getC_item_seq()
                            , ItemType_P = bus_officeVOS.get(i).getP_item_seq()));   //다시 set....
                } else if (bus_officeVOS.get(i).getC_item_seq() == null && bus_officeVOS.get(i).getP_item_seq() != null) {       //2)   P 만 있을 때
                    Garray.PositionInfo[i][1] = Integer.parseInt(bus_officeVOS.get(i).getP_item_seq());
                    G.Last_seq = bus_officeVOS.get(i).getP_item_seq();
                    jobTextItems.add(new JobTextItems(ItemName = bus_officeVOS.get(i).getItem_name()
                            , "해당없음"
                            , uri
                            , "미리보기  (P 만)"
                            , ""
                            , ItemType_P = bus_officeVOS.get(i).getP_item_seq()));
                } else if (bus_officeVOS.get(i).getC_item_seq() != null && bus_officeVOS.get(i).getP_item_seq() == null) {       //3)   C 만 있을 때
                    Garray.PositionInfo[i][0] = Integer.parseInt(bus_officeVOS.get(i).getC_item_seq());
                    G.Last_seq = bus_officeVOS.get(i).getC_item_seq();
                    jobTextItems.add(new JobTextItems(ItemName = bus_officeVOS.get(i).getItem_name()
                            , ""      //hint: 일련번호 입력
                            , uri
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
            recyclerView.setAdapter(job_text_adapter_p_c);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));


        }
    }//busOffRecyclerviewMedia()...........


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d("resultCode =========> ", resultCode+"");
        pref = getContext().getSharedPreferences("img_pref", Context.MODE_PRIVATE);
        String test = pref.getString("camera_type", "test");
        //Log.d("camera_type", "pref:result:  "+test);


        //받아온 uri 타입 string에서 uri로 바꿔주기
        switch (Math.floorDiv(requestCode, 100)) {
            case 2:
                if (resultCode == RESULT_OK) {
                    Log.d("사진촬영::::: ", "결과를 가져온 intent case :20");   //실행
                    // Intent(Data)를 전달받지 못해서 Adapter(보낸쪽)에서 촬영한 사진 uri를 SharedPreference를 통해 전달받기
                    //pref= getContext().getSharedPreferences("img_pref", Context.MODE_PRIVATE);
                    //String test = pref.getString("camera_type","test");

                    Uri contentUri = Uri.parse(test);

                    if (contentUri != null) {
                        JobTextItems item = jobTextItems.get(Math.floorMod(requestCode, 100));
                        item.preview_uri = contentUri;
                        job_text_adapter_p_c.notifyDataSetChanged();
                    }
                    /*if (data!=null){
                        Log.d("data 있음!!!!!!!!!!!!!!!!!!!!", data.getData()+"   tt");
                        Uri uri= data.getData();
                        if (contentUri!=null){
                            Log.d("uri 잇음", contentUri+" uri 있음");

                            JobTextItems item= jobTextItems.get(Math.floorMod(requestCode, 100));
                            item.preview_uri= contentUri;
                            job_text_adapter_p_c.notifyDataSetChanged();

                        }else {
                            Log.d("uri NULL ㅠ_ㅠ", uri+"   tt");    //지금 uri == null.....
                            if (contentUri!=null) Glide.with(this).load(contentUri).into(iv_test);
                        }
                    }else {
                        Log.d("data NULL ㅠ0ㅠ", data+"   dd");
                        if (uri!=null) Glide.with(this).load(uri).into(iv_test);
                    }*/
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {

                    //REG_DTTI (현재날짜)
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    DTTI = sdf.format(date);
                    G.dtti = DTTI;

                    // 전역변수 G 클래스에 저장된 운수사 고정번호, 차량번호
                    TRANSP_BIZR_ID = G.transpBizrId + "";

                    GLOBAL = G.position + "";

                    //Log.d("사진앨범::  ", "결과를 가져온 intent case:30");   //실행
                    uri = data.getData();
                    if (data != null) {

                        // 전역변수 G 클래스에 저장된
                        TempBusId_Value = G.TempBusId;  //경기/인천 -> 141, 128로 변경된 값

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "";
                        File imageFile = null;
                        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP");

                        if (!storageDir.exists()) {
                            Log.d("storageDir 존재하지 않음", storageDir + "");
                            storageDir.mkdirs();
                        }

                        imageFile = new File(storageDir, imageFileName);
                        System.out.println("imageFile 파일:::::::::::  "+imageFile);
                        mCurrentPhotoPath = imageFile.getAbsolutePath();

                        //Log.d("chk1 ==== > ",Math.floorMod(requestCode,100) + "/"+ requestCode );
                        JobTextItems item = jobTextItems.get(Math.floorMod(requestCode, 100));
                        //Log.d("tt->>>", jobTextItems.get(Math.floorMod(requestCode,100))+"///"+ jobTextItems.get(Math.floorMod(requestCode,100)) );

                        item.preview_uri = uri;
                        job_text_adapter_p_c.notifyDataSetChanged();

                        Garray.value[Garray.PositionInfo[G.position][1]] ="project_img/" + TABLE_NAME + "/" + DTTI + "/" + TABLE_NAME + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + TempBusId_Value + "_" + Garray.PositionInfo[G.position][1] + ".jpg";

                        GarryValue = Garray.PositionInfo[G.position][1] + "";
                        mPath= "nas_image/image/IERP/" + TABLE_NAME + "/" + DTTI + "/" + TABLE_NAME + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + TempBusId_Value + "_" + GarryValue + ".jpg";
                        DB_Path = Garray.value[G.position];
                        mRequest_map.put("sign", DB_Path+"&"+mPath.replaceAll("/","%"));
                        //Log.d("chk_map11111 :", mRequest_map+"");


                        Log.d("chk_map :", mRequest_map+"");

                        /*Intent intent = new Intent(getContext(), PreviewDialogActivity.class);
                        intent.putExtra("imgUri", uri);
                        startActivity(intent);*/
                    }
                }
                break;
            case 6:
                Toast.makeText(mContext, "바코드 결과값", Toast.LENGTH_SHORT).show();
                Log.d("바코드 결과값===> ", "바코드 결과값");
                //바코드 스캔 결과받기..
                if (resultCode == RESULT_OK) {
                    Toast.makeText(mContext, "바코드 결과값", Toast.LENGTH_SHORT).show();
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    String barcode = result.getContents();
                    Log.d("barcode===============> ", barcode + "    tt");
                    if (result != null) {
                        Log.d("result===============> ", result + "    tt");
                        if (result.getContents() != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(result.getContents());
                            builder.setTitle("바코드 스캔중...");
                            builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //scanCode();
                                }
                            }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getContext(), "다시 스캔하세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                }
                break;
        }// Switch문..

}



    //[다음]버튼
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
                } else if (etProject_bus_list.getText().length() == 0) {
                    Toast.makeText(getContext(), "차량지역 번호를 확인 하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    //REG_EMP_ID
                    pref = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    REG_EMP_ID = pref.getString("emp_id", "");  //이렇게??
                    System.out.println(" ### REG_EMP_ID : " + REG_EMP_ID);
                    G.regEmpId = REG_EMP_ID;

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    DTTI = sdf.format(date);
                    G.dtti = DTTI;//민혁 - 전역변수 할당 추가
                    //TRANSP_BIZR_ID  (선택된 운수사 고정번호)
                    TRANSP_BIZR_ID = G.transpBizrId;
                    System.out.println(" ### TRANSP_BIZR_ID 운수사 고정번호: " + TRANSP_BIZR_ID);

                    //BUSOFF_NAME
                    BUSOFF_NAME = st_busoff_name_value;
                    G.busoffName = BUSOFF_NAME;    //이런시긍로 값을 넣으면 G클래스에 저장이 잘 되ㅓ어있더라구요
                    // 여기는 프래그먼트 아니에요ㅗ ? 암까ㅏ는 어뎁터에서 저장하고 프래그먼트에서 불렀던거 네ㅔ 맞는데 아까 어댑터에서 저장한 리사이클러뷰의 포지션값도 저장이 되더라구요 그래서 제가 저장을 다른걸로 하지않았나 생각했어요

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

                    st_temp_bus_id = G.TempBusNum;
                    System.out.println(" ### st_temp_bus_id : " + st_temp_bus_id);

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
                    builder.setMessage("작성하신 업무내역을 등록 하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                            Call<String> call = erp.insert_prj_def_val(TABLE_NAME
                                    , DTTI
                                    , REG_EMP_ID
                                    , TRANSP_BIZR_ID
                                    , BUSOFF_NAME
                                    , GARAGE_ID
                                    , GARAGE_NAME
                                    , ROUTE_ID
                                    , ROUTE_NUM
                                    , TempBusId_Value
                                    , st_temp_bus_id
                                    , VEHICLE_NUM
                                    , JOB_TYPE
                                    , Garray.value[0] + ""
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
                            );
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(getContext(), "ttttt" + response.body(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(),"이미지 업로드 완료" ,Toast.LENGTH_SHORT).show();
                                    new ImageUploadJson().execute();
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getContext(),"이미지 업로드 오류 발생" ,Toast.LENGTH_SHORT).show();
                                }
                            });

                            Bundle bundle = new Bundle();
                            bundle.putString("fromfragment1", st_version_value + "");
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            MyPageFragment2 myPageFragment2 = new MyPageFragment2();
                            myPageFragment2.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, myPageFragment2, null).addToBackStack(null).commit();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setCancelable(false);
                    builder.show();



                    //이미지 로컬PC로 업로드
                    /*final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("프로젝트 업무 등록");
                    builder.setMessage("작성하신 업무 내역을 등록 하시겠습니까? ");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri mUri= uri;
                                    Log.d("dddd ",uri+"");
                                }
                            });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();*/
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
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
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
                progressDialog.dismiss();
            if (aBoolean){
                ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                //Call<Boolean> insert_call = erp.insert_project_work_data(sign_map);
                //new insert_project_work_data().execute(insert_call);
                Toast.makeText(getContext(),"이미지 업로드 완료" ,Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(),"이미지 업로드 오류 발생" ,Toast.LENGTH_SHORT).show();
            }
        }
    }//ImageUploadJson()...



}//Fragment end....

