package app.erp.com.erp_app.callcenter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.erp.com.erp_app.CustomScannerActivity;
import app.erp.com.erp_app.dialog.DialogEduEmpList;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_trobule_care extends Fragment {
    CircleImageView unitBeforeAddPic, unitAfterAddPic, busUnitCancelBtn, beforeUnitDeletelBtn1, beforeUnitDeleteBtn2, afterUnitDeletelBtn1, afterUnitDeletelBtn2;
    Button selectUnitBeforeBtn, selectUnitAfterBtn, busUnitBtn;
    ImageView unitBeforeImage1, unitBeforeImage2, unitAfterImage1, unitAfterImage2, busUnitImage;

    /* 교체 전 */
    public int REQUEST_BEFORE_IMAGE_CAPTURED = 200   //사진선택 버튼 -> 사진촬영
            ,REQUEST_BEFORE_IMAGE_PICK = 300         //사진선택 버튼 -> 사진앨범
            ,REQUEST_BEFORE_ADD_IMAGE_CAPTURED = 23  //추가 버튼 -> 사진촬영
            ,REQUEST_BEFORE_ADD_IMAGE_PICK = 24      //추가 버튼 -> 사진앨범

            /* 교체 후 */
            ,REQUEST_AFTER_IMAGE_CAPTURED = 400      //사진선택 버튼 -> 사진촬영
            ,REQUEST_AFTER_IMAGE_PICK = 500          //사진선택 버튼 -> 사진앨범
            ,REQUEST_AFTER_ADD_IMAGE_CAPTURED = 45   //추가 버튼 -> 사진촬영
            ,REQUEST_AFTER_ADD_IMAGE_PICK = 46       //추가 버튼 -> 사진앨범
            /* 차량/버스 단말기 사진 */
            ,REQUEST_BUS_UNIT_IMAGE_CAPTURED = 600   //사진선택 버튼 -> 사진촬영
            ,REQUEST_BUS_UNIT_IMAGE_PICK = 700       //사진선택 버튼 -> 사진앨범

            /* 교체 전 */
            ,BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_1 = 11  //첫번째 사진 사진촬영
            ,BEFORE_PHOTO_CLICK_IMAGE_PICK_1 = 33     //첫번째 사진 사진앨범
            ,BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_2 = 22  //두번째 사진 사진촬영
            ,BEFORE_PHOTO_CLICK_IMAGE_PICK_2 = 44     //두번째 사진 사진앨범
            /* 교체 후 */
            ,AFTER_PHOTO_CLICK_IMAGE_CAPTURE_1 = 55  //첫번째 사진 사진촬영
            ,AFTER_PHOTO_CLICK_IMAGE_PICK_1 = 66     //첫번째 사진 사진앨범
            ,AFTER_PHOTO_CLICK_IMAGE_CAPTURE_2 = 77  //두번째 사진 사진촬영
            ,AFTER_PHOTO_CLICK_IMAGE_PICK_2 = 88;    //두번째 사진 사진앨범

    Uri imageUri;
    String mCurrentPhotoPath, str;
    File imageFile;
    public static Bitmap resizedPhotoBm;
    Bitmap bm, bmRotated;

    /* 이미지 경로 */
    private String mapValue;
    private ArrayList<String> path_list = new ArrayList<String>();
    private Map<String, Object> path_map = new HashMap<>();
    private String folderName = "nas_image/image/IERP/";
    private String TABLE_NAME, DTTI, TRANSP_BIZR_ID, BUS_ID, UNIT_CODE ;    // tranp_bizr_id_value, bus_id_value
    private int PIC_POSITION;

    Button bus_num_find , bus_num_barcode_find ,edit_care_emp_list;
    RelativeLayout update_history;
    Context context;

    private Retrofit retrofit;
    private DialogEduEmpList mdialog;

    TextView insert_start_day , insert_start_time, insert_reg_emp_id, insert_unit_code , insert_bus_num, insert_phone_num, insert_area_code,
            insert_office_code, insert_ars_unit_code, insert_ars_trouble_high_code,insert_ars_trouble_low_code
            ,trouble_care_list;


    EditText insert_garage, insert_route_code;


    SharedPreferences pref ,page_check_info;
    SharedPreferences.Editor editor;

    Spinner insert_process_unit_code , insert_process_trouble_high_code, insert_process_trouble_low_code, insert_process_trouble_care_code ;

    String click_type , emp_id, page_info, service_id , infra_id , unit_id , trouble_high_id, trouble_low_id ;

    EditText insert_care_unit_before , insert_care_unit_after , insert_care_notice;

    int high_intdex, low_index = 0 ;
    int check_count;
    HashMap<String, Object> update_trouble_history_map;

    ProgressDialog progressDialog;

    RelativeLayout equel_layout;

    List<Edu_Emp_Vo> edu_list;
    List<Edu_Emp_Vo> gblist = new ArrayList<>();
    List<Edu_Emp_Vo> gnlist = new ArrayList<>();
    List<Edu_Emp_Vo> iclist = new ArrayList<>();
    List<Edu_Emp_Vo> pclist = new ArrayList<>();
    List<String> cooperate_list ;

    public Fragment_trobule_care(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_care_new, container ,false);
        context = getActivity();


        //todo: 카메라 권한 허가 체크...
        cameraPermission();

        update_trouble_history_map = new HashMap<>();

        Bundle bundle = getArguments();
        Trouble_HistoryListVO thlvo = (Trouble_HistoryListVO) bundle.getSerializable("Obj");

        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        emp_id = pref.getString("emp_id",null);

        page_check_info = context.getSharedPreferences("page_check_info" ,  Context.MODE_PRIVATE);
        String page_info = page_check_info.getString("page_check","n");

        SharedPreferences barcode_pre = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        editor = barcode_pre.edit();
        // layout


        equel_layout = (RelativeLayout)view.findViewById(R.id.equel_layout);
        if("care_insert".equals(page_info)){
            equel_layout.setVisibility(View.GONE);
        }

        RelativeLayout after_before_unit = (RelativeLayout)view.findViewById(R.id.after_before_unit);
        after_before_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_type = "before";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forSupportFragment(Fragment_trobule_care.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        RelativeLayout after_care_unit = (RelativeLayout)view.findViewById(R.id.after_care_unit);
        after_care_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_type = "after";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forSupportFragment(Fragment_trobule_care.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        //textView
        insert_start_day = (TextView)view.findViewById(R.id.insert_start_day);
        insert_start_time = (TextView)view.findViewById(R.id.insert_start_time);
        insert_reg_emp_id = (TextView)view.findViewById(R.id.insert_reg_emp_id);
        insert_unit_code = (TextView)view.findViewById(R.id.insert_unit_code);
        insert_bus_num = (TextView)view.findViewById(R.id.insert_bus_num);
        insert_phone_num = (TextView)view.findViewById(R.id.insert_phone_num);
        insert_area_code = (TextView)view.findViewById(R.id.insert_area_code);
        insert_office_code = (TextView)view.findViewById(R.id.insert_office_code);

        insert_garage = (EditText) view.findViewById(R.id.insert_garage);
        insert_route_code = (EditText) view.findViewById(R.id.insert_route_code);

        insert_ars_unit_code = (TextView)view.findViewById(R.id.insert_ars_unit_code);
        insert_ars_trouble_high_code = (TextView)view.findViewById(R.id.insert_ars_trouble_high_code);
        insert_ars_trouble_low_code = (TextView)view.findViewById(R.id.insert_ars_trouble_low_code);
        trouble_care_list = (TextView)view.findViewById(R.id.trouble_care_list);

        //spinner
        insert_process_unit_code = (Spinner)view.findViewById(R.id.insert_process_unit_code);
        insert_process_trouble_high_code = (Spinner)view.findViewById(R.id.insert_process_trouble_high_code);
        insert_process_trouble_low_code = (Spinner)view.findViewById(R.id.insert_process_trouble_low_code);
        insert_process_trouble_care_code = (Spinner)view.findViewById(R.id.insert_process_trouble_care_code);

        //editText
        insert_care_unit_before = (EditText)view.findViewById(R.id.insert_care_unit_before);
        insert_care_unit_after = (EditText)view.findViewById(R.id.insert_care_unit_after);
        insert_care_notice = (EditText)view.findViewById(R.id.insert_care_notice);

        //button
        edit_care_emp_list = (Button)view.findViewById(R.id.edit_care_emp_list);
        edit_care_emp_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog = new DialogEduEmpList(context, gblist, gnlist, iclist,pclist, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdialog.dismiss();
                        Map<String,Object> map = mdialog.return_list();
                        gblist = (List<Edu_Emp_Vo>) map.get("gb_list");
                        gnlist = (List<Edu_Emp_Vo>) map.get("gn_list");
                        iclist = (List<Edu_Emp_Vo>) map.get("ic_list");
                        pclist = (List<Edu_Emp_Vo>) map.get("pc_list");

                        check_count = (int)map.get("check_count");
                        trouble_care_list.setText("현재 대상자 " + check_count +"명");
                        cooperate_list = (List<String>) map.get("care_emp_id");
                    }
                });
                mdialog.setCancelable(false);
                mdialog.show();

                DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = dm.widthPixels; //디바이스 화면 너비
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(mdialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = mdialog.getWindow();
                window.setAttributes(lp);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        update_history = view.findViewById(R.id.update_history);
        update_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
                String getdate = sdf.format(date);
                String gettime = sdf2.format(date);

                String care_before = insert_care_unit_before.getText().toString();
                String care_after  = insert_care_unit_after.getText().toString();

                if(!care_before.equals("") || !care_after.equals("")){
                    update_trouble_history_map.put("unit_change_yn","N");
                    update_trouble_history_map.put("unit_before_id",care_before);
                    update_trouble_history_map.put("unit_after_id",care_after);
                }else{
                    update_trouble_history_map.put("unit_change_yn","Y");
                    update_trouble_history_map.put("unit_after_id",care_after);
                    update_trouble_history_map.put("unit_before_id",care_before);
                }

                update_trouble_history_map.put("care_date",getdate);
                update_trouble_history_map.put("care_time",gettime);
                update_trouble_history_map.put("care_emp_id",emp_id);
                update_trouble_history_map.put("notice",insert_care_notice.getText().toString());

                if(check_count > 0){
                    update_trouble_history_map.put("cooperate_key","Y");
                }else{
                    update_trouble_history_map.put("cooperate_key","N");
                }

                update_trouble_history_map.put("garage_id",insert_garage.getText().toString().trim());
                update_trouble_history_map.put("route_id",insert_route_code.getText().toString().trim());

//                StringBuilder sb = new StringBuilder();
//                Set<?> set = update_trouble_history_map.keySet();
//                Iterator<?> it = set.iterator();
//                while(it.hasNext()){
//                    String key = (String)it.next();
//                    if(key != null){
//                        sb.append("------------------------------------------------------------\n");
//                        sb.append("key = "+key+",\t\t\tvalue = "+update_trouble_history_map.get(key)+"\n");
//                    }
//                }
//                Log.i("values info : " , ""+sb);

                String care_cd = update_trouble_history_map.get("trouble_care_cd").toString();    // 'java.lang.String java.lang.Object.toString()' on a null object reference
                if("X001".equals(care_cd)){
                    Toast.makeText(context,"조치 항목을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    new trouble_history_care_update().execute();
                }
            }

        });
        // 사원리스트 가져옴
        String emp_id = pref.getString("emp_id",null);
        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Edu_Emp_Vo>> call_emp = erp.Edu_care_emp_list(emp_id);
        new Fragment_trobule_care.Edu_Care_Emp_List().execute(call_emp);

        new GetMyWork_Job().execute(thlvo.getReg_date(),thlvo.getJob_viewer(),thlvo.getReg_time());


        REG_DTTI();








        //TODO:
        /* 교체 전 단말기 사진 */
        selectUnitBeforeBtn = view.findViewById(R.id.selectUnitBeforeBtn);
        selectUnitBeforeBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();                  //캡쳐된 사진이 저장된 경로 지정
                    if (imageUri != null){
                        Log.d("imageUri~~", imageUri+"");     //   content://app.erp.com.erp_app/hidden/IERP/JPEG_20210527_0245.jpg
                        Log.d("imageFile~~", imageFile+"");   //  /storage/emulated/0/IERP/JPEG_20210527_0245.jpg
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_BEFORE_IMAGE_CAPTURED);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_BEFORE_IMAGE_PICK);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // 교체 전
        // 첫번째 사진 클릭시
        unitBeforeImage1 = view.findViewById(R.id.unit_before_image1);
        unitBeforeImage1.setOnClickListener(v -> {
           // path_list.set(0,"");
            //path_list.remove(0);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_1);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, BEFORE_PHOTO_CLICK_IMAGE_PICK_1);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        // 교체 전
        // 두번째 사진 클릭시
        unitBeforeImage2 = view.findViewById(R.id.unit_before_image2);
        unitBeforeImage2.setOnClickListener(v -> {
           // path_list.set(1,"");
            //path_list.remove(1);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_2);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, BEFORE_PHOTO_CLICK_IMAGE_PICK_2);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        /* 교체 전 사진 */
        /* 사진 촬영/ 선택 후 추가하기 버튼 */
        unitBeforeAddPic = view.findViewById(R.id.unitBeforeAddPic);
        unitBeforeAddPic.setOnClickListener(v -> {
            //다이얼로그로 바꾸기 -> 사진촬영 + 사진앨범
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_BEFORE_ADD_IMAGE_CAPTURED);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_BEFORE_ADD_IMAGE_PICK);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        /* 첫번째 사진 삭제 */
        beforeUnitDeletelBtn1 = view.findViewById(R.id.before_unit_delete_btn_1);
        beforeUnitDeletelBtn1.setOnClickListener(v -> {
            unitBeforeImage1.setVisibility(View.GONE);
            beforeUnitDeletelBtn1.setVisibility(View.GONE);  //삭제버튼 삭제
            unitBeforeAddPic.setVisibility(View.GONE);
            selectUnitBeforeBtn.setVisibility(View.VISIBLE);
           // path_list.set(0,"");
            Log.d("path_list::", path_list.toString());
            //path_list.remove(0);
        });


        /* 두번째 사진 삭제 */
        beforeUnitDeleteBtn2 = view.findViewById(R.id.before_unit_delete_btn_2);
        beforeUnitDeleteBtn2.setOnClickListener(v -> {
            //첫번째 사진이 삭제되었을 때 beforeUnitCancelBtn2 도 삭제해야됨... 어떻게??
            //뷰의 visibility 로 체크하기..
            if (beforeUnitDeletelBtn1.getVisibility() == View.GONE){
                unitBeforeImage2.setVisibility(View.GONE);
                beforeUnitDeleteBtn2.setVisibility(View.GONE);
                unitBeforeAddPic.setVisibility(View.GONE);
                //path_list.set(1,"");
                Log.d("path_list::", path_list.toString());
                //path_list.remove(1);
            }else {
                unitBeforeImage2.setVisibility(View.GONE);
                beforeUnitDeleteBtn2.setVisibility(View.GONE);
                unitBeforeAddPic.setVisibility(View.VISIBLE);
                //path_list.set(1,"");
                Log.d("path_list::", path_list.toString());
                //path_list.remove(1);
            }


        });



        //TODO:
        /* 교체 후 단말기 사진 */
        selectUnitAfterBtn = view.findViewById(R.id.selectUnitAfterBtn);
        selectUnitAfterBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_AFTER_IMAGE_CAPTURED);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_AFTER_IMAGE_PICK);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        /* 교체 후 사진 */
        /* 사진촬영/ 앨범 선택 후 추가하기 버튼 */
        unitAfterAddPic = view.findViewById(R.id.unitAfterAddPic);
        unitAfterAddPic.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_AFTER_ADD_IMAGE_CAPTURED);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_AFTER_ADD_IMAGE_PICK);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        // 교체 후
        // 첫번째 사진 클릭시
        unitAfterImage1 = view.findViewById(R.id.unit_after_image1);
        unitAfterImage1.setOnClickListener(v -> {
           // path_list.set(2,"");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요!!!.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, AFTER_PHOTO_CLICK_IMAGE_CAPTURE_1);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, AFTER_PHOTO_CLICK_IMAGE_PICK_1);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        // 교체 후
        // 두번째 사진 클릭시
        unitAfterImage2 = view.findViewById(R.id.unit_after_image2);
        unitAfterImage2.setOnClickListener(v -> {
           // path_list.set(3,"");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, AFTER_PHOTO_CLICK_IMAGE_CAPTURE_2);
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, AFTER_PHOTO_CLICK_IMAGE_PICK_2);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //교체 후
        /* 첫번째 사진 삭제버튼 */
        afterUnitDeletelBtn1 = view.findViewById(R.id.after_unit_delete_btn_1);
        afterUnitDeletelBtn1.setOnClickListener(v -> {
            unitAfterImage1.setVisibility(View.GONE);
            afterUnitDeletelBtn1.setVisibility(View.GONE);
            unitAfterAddPic.setVisibility(View.GONE);
            selectUnitAfterBtn.setVisibility(View.VISIBLE);
            /*if (path_list.get(2) != null){
                path_list.remove(2);
            }*/

        });


        /* 두번째 사진 삭제버튼 */
        afterUnitDeletelBtn2 = view.findViewById(R.id.after_unit_delete_btn_2);
        afterUnitDeletelBtn2.setOnClickListener(v -> {
            Log.d("drawable state", unitAfterImage1.getDrawableState()+", drawable :"+unitAfterImage1.getDrawable()+"");

            if (afterUnitDeletelBtn1.getVisibility() == View.GONE){
                unitAfterImage2.setVisibility(View.GONE);
                afterUnitDeletelBtn2.setVisibility(View.GONE);
                unitAfterAddPic.setVisibility(View.GONE);
                /*try {
                    path_list.set(3,"");
                    Log.d("path_list::", path_list+"");
                    Log.d("666","666666666");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }else {
                unitAfterImage2.setVisibility(View.GONE);
                afterUnitDeletelBtn2.setVisibility(View.GONE);
                unitAfterAddPic.setVisibility(View.VISIBLE);
                try {
                   // path_list.set(3,"");
                    Log.d("path_list::", path_list+"");
                    Log.d("777","77777777");
                    /*if (path_list.get(3) != null){
                        path_list.remove(3);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


        //TODO:
        /* 차량/버스 단말기 사진 */
        busUnitBtn = view.findViewById(R.id.bus_unit_btn);
        busUnitBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_BUS_UNIT_IMAGE_CAPTURED );
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_BUS_UNIT_IMAGE_PICK);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        /* 차량 단말기 사진 클릭시 */
        busUnitImage = view.findViewById(R.id.bus_unit_image);
        busUnitImage.setOnClickListener(v -> {
           // path_list.set(4,"");
            Log.d("path_list::", path_list.toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_menu_camera);
            builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요.");
            builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setImageUri();
                    if (imageUri != null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 1000 );
                    }
                }
            });
            builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, 2000);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        /* 차량 단말기 사진 삭제버튼 */
        busUnitCancelBtn = view.findViewById(R.id.bus_unit_cancel_btn);
        busUnitCancelBtn.setOnClickListener(v -> {
            busUnitImage.setVisibility(View.GONE);
            busUnitCancelBtn.setVisibility(View.GONE);
            busUnitBtn.setVisibility(View.VISIBLE);
            //path_list.remove(4);
            //path_list.set(4, "");
            Log.d("path_list::", path_list.toString());
        });



        return view;

    }//onCreateView...


    private void cameraPermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한요청 성공
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //권한요청 실패
            }
        };

        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }



    public void setImageUri(){
        Log.d("ttttttt","!!!!!!!!!!");
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/IERP");

        if (!storageDir.exists()){
            storageDir.mkdirs();
        }else {
            Log.d("storageDir~~", storageDir+"");
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.d("imagePath~~", mCurrentPhotoPath+"");

        //File ------> Uri
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            imageUri = Uri.fromFile(imageFile);
        }else {
            imageUri = FileProvider.getUriForFile(context, context.getPackageName(), imageFile);
        }
    }



    public void ResizingBitmapPhoto() throws IOException {

        ExifInterface exif = null;

        try {
            Log.d("imageFile :" , imageFile.toString());
            exif = new ExifInterface(imageFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);   //회전각도
        Matrix matrix = new Matrix();
        Log.d("orientation:" , orientation+"asdf");
        switch (orientation){
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
        }//switch..

        Log.d("orientation>>", orientation+"");

        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        double Wratio = 0.0;
        double Hratio = 0.0;

        if (bmWidth > bmHeight){
            Wratio = ((double)bmWidth / (double)bmHeight) * 512;
            Hratio = 1 * 512;
        }else {
            Wratio = 1 * 512;
            Hratio = ((double)bmHeight / (double)bmWidth) * 512;
        }

        resizedPhotoBm = bm.createScaledBitmap(bm, (int) Wratio , (int) Hratio, false);
        resizedPhotoBm = resizedPhotoBm.createBitmap(resizedPhotoBm, 0, 0, (int) Wratio, (int) Hratio, matrix, true);



        Log.d("resizedPhotoBm_bm>", "resizedBm=> "+resizedPhotoBm+",    bm=> "+bm);  //android.graphics.Bitmap@fb86ab2
        Log.d("orientation>>", orientation+"");
    }

    public void ResizingBitmapPhoto_2(InputStream inputStream) throws IOException {

        ExifInterface exif = null;

        try {
            Log.d("imageFile :" , imageFile.toString());
            exif = new ExifInterface(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);   //회전각도
        Matrix matrix = new Matrix();
        Log.d("orientation:" , orientation+"asdf");
        switch (orientation){
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
        }//switch..

        Log.d("orientation>>", orientation+"");

        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        double Wratio = 0.0;
        double Hratio = 0.0;

        if (bmWidth > bmHeight){
            Wratio = ((double)bmWidth / (double)bmHeight) * 512;
            Hratio = 1 * 512;
        }else {
            Wratio = 1 * 512;
            Hratio = ((double)bmHeight / (double)bmWidth) * 512;
        }

        if(orientation == 0) {
            resizedPhotoBm = bm.createScaledBitmap(bm, (int) Wratio , (int) Hratio, false);
        } else{
            resizedPhotoBm = bm.createScaledBitmap(bm, (int) Wratio , (int) Hratio, false);
            resizedPhotoBm = resizedPhotoBm.createBitmap(resizedPhotoBm, 0, 0, (int) Wratio, (int) Hratio, matrix, true);
        }





        Log.d("resizedPhotoBm_bm>", "resizedBm=> "+resizedPhotoBm+",    bm=> "+bm);  //android.graphics.Bitmap@fb86ab2
        Log.d("orientation>>", orientation+"");
    }




    //이미지 90도 회전 전 기존 코드..
    public void ResizingBitmapPhotoPic() throws IOException {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        double Wratio = 0.0;
        double Hratio = 0.0;

        Matrix matrix = new Matrix();
        if (bmWidth > bmHeight){
            Wratio = ((double)bmWidth / (double)bmHeight) * 512;
            Hratio = 1 * 1024;
        }else {
            Wratio = 1 * 1024;
            Hratio = ((double)bmHeight / (double)bmWidth) * 512;
        }
        //matrix.postRotate(90);
        resizedPhotoBm = bm.createScaledBitmap(bm, (int) Wratio , (int) Hratio, false);
        resizedPhotoBm = resizedPhotoBm.createBitmap(resizedPhotoBm, 0, 0, (int) Wratio, (int) Hratio, matrix, true);

        Log.d("resizedPhotoBm>", resizedPhotoBm+"");  //android.graphics.Bitmap@fb86ab2
        Log.d("bm>", bm+"");   //android.graphics.Bitmap@fb86ab2
    }

    //note: 사진촬영 후 bm 을 File 로..
    public void BmToFile() throws IOException{
        String timeStamp= new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN).format(new Date());
        String imgFileName= "JPEG_"+timeStamp+".jpg";
        imageFile= null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP");

        if (!storageDir.exists()){
            storageDir.mkdirs();
        }else { Log.d("storageDir>", storageDir.toString()); }

        imageFile = new File(storageDir, imgFileName);
        try {
            imageFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imageFile);
            resizedPhotoBm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.d("e---->", e.toString());
            e.printStackTrace();
        }
    }




    //note: 사진앨범에서 선택한 이미지bm 을 file 로 생성 및 리사이즈, 오리엔테이션 지정..
    public void AlbumBmToFile() throws IOException{
        String timeStamp= new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN).format(new Date());
        String imgFileName= "JPEG_"+timeStamp+".jpg";
        imageFile= null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP");

        if (!storageDir.exists()){
            storageDir.mkdirs();
        }else { Log.d("storageDir>", storageDir.toString()); }

        imageFile = new File(storageDir, imgFileName);
        try {
            imageFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imageFile);

            //ResizingBitmapPhoto();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            /** 기존코드 **/
           /** resizedPhotoBm.compress(Bitmap.CompressFormat.JPEG, 100, out); **/
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.d("e---->", e.toString());
            e.printStackTrace();
        }
    }


    public void AlbumBmToFile_2(InputStream inputStream) throws IOException{
        String timeStamp= new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN).format(new Date());
        String imgFileName= "JPEG_"+timeStamp+".jpg";
        imageFile= null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP");

        if (!storageDir.exists()){
            storageDir.mkdirs();
        }else { Log.d("storageDir>", storageDir.toString()); }

        imageFile = new File(storageDir, imgFileName);
        try {
            imageFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imageFile);

            ResizingBitmapPhoto_2(inputStream);

            resizedPhotoBm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.d("e---->", e.toString());
            e.printStackTrace();
        }
    }








    private class GetMyWork_Job extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_HistoryListVO>> call = erp.getMyWork_Job(strings[0],strings[1],strings[2]);
            call.enqueue(new Callback<List<Trouble_HistoryListVO>>() {
                @Override
                public void onResponse(Call<List<Trouble_HistoryListVO>> call, Response<List<Trouble_HistoryListVO>> response) {
                    try {
                        List<Trouble_HistoryListVO> list  = response.body();
                        MakeMyWork_Info(list);
                        service_id = list.get(0).getService_id();
                        infra_id = list.get(0).getInfra_code();
                        unit_id = list.get(0).getUnit_code();
                        trouble_high_id = list.get(0).getTrouble_high_cd();
                        trouble_low_id = list.get(0).getTrouble_low_cd();
                        new get_insert_unit_code().execute();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(context,"데이터를 가져오는데 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Trouble_HistoryListVO>> call, Throwable t) {
                }
            });
            return null;
        }
    }

    private void MakeMyWork_Info(List<Trouble_HistoryListVO> list) {
        insert_start_day.setText(list.get(0).getReg_date());
        insert_start_time.setText(list.get(0).getReg_time());
        insert_reg_emp_id.setText(list.get(0).getWork_reg_emp_name());
        insert_unit_code.setText(list.get(0).getUnit_name());
        insert_bus_num.setText(list.get(0).getBus_num());
        insert_phone_num.setText(list.get(0).getDriver_tel_num());
        insert_area_code.setText(list.get(0).getOffice_group());
        insert_office_code.setText(list.get(0).getBusoff_name());
        insert_garage.setText(list.get(0).getGarage_id());
        insert_route_code.setText(list.get(0).getRoute_id());
        insert_ars_unit_code.setText(list.get(0).getUnit_name());
        insert_ars_trouble_high_code.setText(list.get(0).getTrouble_name());
        insert_ars_trouble_low_code.setText(list.get(0).getTrouble_low_name());

        insert_care_unit_before.setText(list.get(0).getUnit_before_id());
        insert_care_unit_after.setText(list.get(0).getUnit_after_id());
        insert_care_notice.setText(list.get(0).getNotice());

        update_trouble_history_map.put("reg_emp_id", list.get(0).getReg_emp_id());
        update_trouble_history_map.put("reg_date", list.get(0).getReg_date());
        update_trouble_history_map.put("reg_time",list.get(0).getReg_time());
        update_trouble_history_map.put("unit_code_before",list.get(0).getUnit_code());
        update_trouble_history_map.put("trouble_high_cd_before",list.get(0).getTrouble_high_cd());
        update_trouble_history_map.put("trouble_low_cd_before",list.get(0).getTrouble_low_cd());
        update_trouble_history_map.put("transp_bizr_id",list.get(0).getTransp_bizr_id());

        Log.d("transp-bizr-id", list.get(0).getTransp_bizr_id());
        Log.d("bus-num", list.get(0).getBus_num());
        Log.d("bus-id", list.get(0).getBus_id());
        Log.d("unit-code", list.get(0).getUnit_code());

        TRANSP_BIZR_ID = list.get(0).getTransp_bizr_id();
        BUS_ID = list.get(0).getBus_id();
        UNIT_CODE = list.get(0).getUnit_code();

    }

    private class get_insert_unit_code extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type(service_id,infra_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    int pos = 0 ;
                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getUnit_name());
                        if(list.get(i).getUnit_code().equals(unit_id)){
                            pos = i;
                        }
                    }
                    insert_process_unit_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    insert_process_unit_code.setSelection(pos);
                    insert_process_unit_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_high_code = list.get(position).getUnit_code();
                            unit_id = select_high_code;
                            update_trouble_history_map.put("unit_code",unit_id);
                            new get_insert_trobule_high_code().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private class get_insert_trobule_high_code extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_high_code(service_id,infra_id,unit_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    int pos = 0;

                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getTrouble_high_name());
                        if(list.get(i).getTrouble_high_cd().equals(trouble_high_id)){
                            pos = i;
                        }
                    }
                    insert_process_trouble_high_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    if(high_intdex == 0 ){ insert_process_trouble_high_code.setSelection(pos); }
                    insert_process_trouble_high_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_high_code = list.get(position).getTrouble_high_cd();
                            trouble_high_id = select_high_code;
                            update_trouble_history_map.put("trouble_high_cd",trouble_high_id);
                            new getfield_trouble_low_code().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    high_intdex ++;
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });

            return null;
        }
    }

    private class getfield_trouble_low_code extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_low_code(service_id,infra_id,unit_id,trouble_high_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    int pos = 0;
                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getTrouble_low_name());
                        if(list.get(i).getTrouble_low_cd().equals(trouble_low_id)){
                            pos = i;
                        }
                    }
                    insert_process_trouble_low_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    if(low_index == 0 ){insert_process_trouble_low_code.setSelection(pos);}
                    insert_process_trouble_low_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            trouble_low_id = list.get(position).getTrouble_low_cd();
                            update_trouble_history_map.put("trouble_low_cd",trouble_low_id);
                            new get_field_trouble_carecode().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    low_index++;
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });

            return null;
        }
    }

    private class get_field_trouble_carecode extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_carecode(service_id,infra_id,unit_id,trouble_high_id,trouble_low_id);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("조치 항목을 선택해주세요.");
                    for(int i = 0 ; i < list.size(); i++){
                        spinner_list.add(list.get(i).getTrouble_care_name());
                    }
                    insert_process_trouble_care_code.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    insert_process_trouble_care_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position > 0){
                                update_trouble_history_map.put("trouble_care_cd",list.get(position-1).getTrouble_care_cd());
                            }else{
                                update_trouble_history_map.put("trouble_care_cd","X001");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });

            return null;
        }
    }

    private class trouble_history_care_update extends AsyncTask<String, Integer, Long>{

        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.update_trouble_history(update_trouble_history_map,cooperate_list);
            Log.d("update content check>>", update_trouble_history_map+"");
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    //TODO: new ImageUploadJson().execute();
                    new ImageUploadJson().execute();
                    progressDialog.dismiss();
                    boolean result = response.body();

                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    page_info = "list";
                    a_builder.setTitle("콜 처리");
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    String title = "";
                                    if(page_info.equals("repg")){
                                        fragment = new Fragment_trouble_list();
                                    }else{
                                        fragment = new Fragment_trouble_list();
                                    }
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.frage_change,fragment);
                                    ft.commit();
                                }
                            });
                    if(result){
                        page_info = "list";
                        a_builder.setMessage(" 등록 완료.");
                        a_builder.show();
                    }else{
                        page_info = "repg";
                        a_builder.setMessage("오류 발생 다시 시도 해주세요 .");
                        a_builder.show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
            return null;
        }
    }


    private class ImageUploadJson extends AsyncTask<Void, Void, Boolean>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("이미지 업로드 중....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Log.d("path_map_size ###", path_map.size()+"");
                Log.d("path_map ###", path_map.toString());
                Boolean jsonObject = JSONParser.uploadImage(path_map);
                Log.d("pat_map_jsonObject ###", jsonObject.toString());
                if (jsonObject != null){
                    return jsonObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("error", "Error : "+e.getLocalizedMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null){
                progressDialog.dismiss();
                if (aBoolean){

                }else {
                    Toast.makeText(context, "이미지 업로드 오류 발생 !!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }//ImageUploadJson...





    private class Edu_Care_Emp_List extends AsyncTask<Call,Void, List<Edu_Emp_Vo>> {
        @Override
        protected List<Edu_Emp_Vo> doInBackground(Call... calls) {
            try{
                Call<List<Edu_Emp_Vo>> call = calls[0];
                Response<List<Edu_Emp_Vo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Edu_Emp_Vo> user_infoVos) {
            edu_list = user_infoVos;
            if(!(gblist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "강북지부" :
                            gblist.add(i);
                            break;
                    }
                }
            }
            if(!(gnlist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "강남지부":
                            gnlist.add(i);
                            break;
                    }
                }
            }
            if(!(iclist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "인천지부":
                            iclist.add(i);
                            break;
                    }
                }
            }
            if(!(pclist.size() > 1)){
                for(Edu_Emp_Vo i : edu_list){
                    switch (i.getDep_name()){
                        case "집계/충전기":
                            pclist.add(i);
                            break;
                    }
                }
            }
        }
    }


    // 현재날짜
    public void REG_DTTI(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        DTTI = sdf.format(date);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //String strResult = intentResult.getContents();
        Log.d("intentResult>", ""+intentResult);  //null
        Log.d("requestCode>", ""+requestCode);


        //TODO: 작업 전 사진
        /** 사진촬영 **/    //Note: 순번 1
        if (requestCode == REQUEST_BEFORE_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                // Uri -------> Bitmap (사이즈/용량의 문제로 bitmap 으로 변경해주기)
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();
                        PIC_POSITION = 1;
                        path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));

                        Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                        for (String str : path_list){
                            path_map.put("ITEM"+PIC_POSITION, str+"");
                            //edit: DB 로 업로드되는 이미지 경로에는 % 있으면 안됌.!!!!!!!!!!!!
                            //edit: 그래서 mapValue 로 다시 바꿔줌.
                            mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                            update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                        }
                        Log.d("path_map: ", path_map+"");


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //사진촬영된 값(bitmap) -> imageView 에 붙여주기
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitBeforeImage1);
                unitBeforeImage1.setVisibility(View.VISIBLE);
                unitBeforeAddPic.setVisibility(View.VISIBLE);      //사진촬영 후 추가버튼
                beforeUnitDeletelBtn1.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                selectUnitBeforeBtn.setVisibility(View.GONE);
               // path_list.add("교체 전: "+"사진선택 버튼 -> 사진촬영");

            }
        }  //NOTE: 첫번째 사진 클릭시 -> 사진촬영    //Note: 순번 1
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_1){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //사진촬영된 값(bitmap) -> imageView 에 붙여주기
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitBeforeImage1);
                unitBeforeImage1.setVisibility(View.VISIBLE);
                if (unitBeforeImage2.getVisibility() == View.VISIBLE){
                    unitBeforeAddPic.setVisibility(View.GONE);   //사진촬영 후 추가버튼
                }else {
                    unitBeforeAddPic.setVisibility(View.VISIBLE);
                }
                beforeUnitDeletelBtn1.setVisibility(View.VISIBLE);  //삭제버튼 보이기
              //  path_list.add("교체 전: "+"첫번째 사진 클릭시 -> 사진촬영");
                PIC_POSITION = 1;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION  + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");

            }
        }
        //NOTE: 두번째 사진 클릭시 -> 사진촬영  //Note: 순번 2
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_2){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //사진촬영된 값(bitmap) -> imageView 에 붙여주기
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitBeforeImage2);
                unitBeforeImage2.setVisibility(View.VISIBLE);
                unitBeforeAddPic.setVisibility(View.GONE);   //사진촬영 후 추가버튼
                beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                //path_list.add("교체 전: "+"두번째 사진 클릭시 -> 사진촬영");
                PIC_POSITION = 2;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION  + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");
            }
        }
        /** 사진앨범 **/       //Note: 순번 1
        else if (requestCode == REQUEST_BEFORE_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();


                    //ResizingBitmapPhoto_2(in);


                    //ResizingBitmapPhoto();
                    AlbumBmToFile();
                    //AlbumBmToFile_2(in); //변경된 Bitmap -> file 형식으로 변환..



                    Log.d("phtoalbum_imageFile>", imageFile.toString());

                    // 이미지뷰에 세팅
                    unitBeforeImage1.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage1.setVisibility(View.VISIBLE);
                    unitBeforeAddPic.setVisibility(View.VISIBLE);  //사진앨범 선택 후 추가버튼
                    beforeUnitDeletelBtn1.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    selectUnitBeforeBtn.setVisibility(View.GONE);
                    //path_list.add("교체 전: "+"사진선택 버튼 -> 사진앨범");
                    PIC_POSITION = 1;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);


                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } //NOTE: 첫번째 사진 클릭시 -> 사진앨범    //Note: 순번 1
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_PICK_1){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile(); //변경된 Bitmap -> file 형식으로 변환..
                    Log.d("phtoalbum_imageFile>", imageFile.toString());
                    // 이미지뷰에 세팅
                    unitBeforeImage1.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage1.setVisibility(View.VISIBLE);
                    if (unitBeforeImage2.getVisibility() == View.VISIBLE){
                        unitBeforeAddPic.setVisibility(View.GONE);  //사진앨범 선택 후 추가버튼
                    }else {
                        unitBeforeAddPic.setVisibility(View.VISIBLE);
                    }
                    //beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    //path_list.add("교체 전: "+"첫번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 1;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //NOTE: 두번째 사진 클릭시 -> 사진앨범    //Note: 순번 2
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_PICK_2){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile(); //변경된 Bitmap -> file 형식으로 변환..
                    Log.d("phtoalbum_imageFile>", imageFile.toString());
                    // 이미지뷰에 세팅
                    unitBeforeImage2.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage2.setVisibility(View.VISIBLE);
                    unitBeforeAddPic.setVisibility(View.GONE);  //사진앨범 선택 후 추가버튼
                    beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                   // path_list.add("교체 전: "+"두번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 2;
                    //path_list.set(1,imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + ".jpg").replaceAll("/", "%"));
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE+ "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /** 사진 촬영 후 --> 추가버튼1 **/    //Note: 순번 2
        else if (requestCode == REQUEST_BEFORE_ADD_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitBeforeImage2);
                unitBeforeImage2.setVisibility(View.VISIBLE);
                selectUnitBeforeBtn.setVisibility(View.GONE);  //사진선택 버튼 삭제
                beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                unitBeforeAddPic.setVisibility(View.VISIBLE);    //추가버튼 보이기
                //path_list.add("교체 전: "+"추가버튼 -> 사진촬영");
                PIC_POSITION = 2;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");

            }
        } /** 사진앨범 선택 후 --> 추가버튼1 **/     //Note: 순번 2
        else if (requestCode == REQUEST_BEFORE_ADD_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile(); //변경된 Bitmap -> file 형식으로 변환..
                    Log.d("phtoalbum_imageFile>", imageFile.toString());
                    // 이미지뷰에 세팅
                    unitBeforeImage2.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage2.setVisibility(View.VISIBLE);
                    unitBeforeAddPic.setVisibility(View.GONE);
                    selectUnitBeforeBtn.setVisibility(View.GONE);  //사진선택 버튼 삭제
                    beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    //path_list.add("교체 전: "+"추가버튼 -> 사진촬영");
                    PIC_POSITION = 2;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //TODO: 작업 후 사진
        /** 사진촬영 **/               //Note: 순번 3
        else if (requestCode == REQUEST_AFTER_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();

                        Log.d("작업후_사진촬영_이미지파일>", imageFile.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitAfterImage1);
                unitAfterImage1.setVisibility(View.VISIBLE);
                unitAfterAddPic.setVisibility(View.VISIBLE);
                afterUnitDeletelBtn1.setVisibility(View.VISIBLE);
                selectUnitAfterBtn.setVisibility(View.GONE);
                //path_list.add("교체 후: "+"사진선택 버튼 -> 사진촬영");
                PIC_POSITION = 3;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);


                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");

            }

        }  //NOTE: 첫번째 사진 클릭시 -> 사진촬영      //Note: 순번 3
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_CAPTURE_1){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();
                        Log.d("작업후_사진촬영_이미지파일>", imageFile.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitAfterImage1);
                unitAfterImage1.setVisibility(View.VISIBLE);
                unitAfterAddPic.setVisibility(View.VISIBLE);
                selectUnitAfterBtn.setVisibility(View.GONE);
                afterUnitDeletelBtn1.setVisibility(View.VISIBLE);
               // path_list.add("교체 전: "+"첫번째 사진 클릭시 -> 사진촬영");
                PIC_POSITION = 3;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);


                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");

            }
        }//NOTE: 두번째 사진 클릭시 -> 사진촬영        //Note: 순번 4
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_CAPTURE_2){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();
                        Log.d("작업후_사진촬영_이미지파일>", imageFile.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitAfterImage2);
                unitAfterImage2.setVisibility(View.VISIBLE);
                unitAfterAddPic.setVisibility(View.GONE);
                selectUnitAfterBtn.setVisibility(View.GONE);
                afterUnitDeletelBtn2.setVisibility(View.VISIBLE);
                //path_list.add("교체 전: "+"두번째 사진 클릭시 -> 사진촬영");
                PIC_POSITION = 4;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");
            }
        }
        /** 사진앨범 **/                  //Note: 순번 3
        else if (requestCode == REQUEST_AFTER_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile();
                    Log.d("작업후_사진앨범_이미지파일>", imageFile.toString());
                    unitAfterImage1.setImageBitmap(resizedPhotoBm);
                    unitAfterImage1.setVisibility(View.VISIBLE);
                    unitAfterAddPic.setVisibility(View.VISIBLE);
                    afterUnitDeletelBtn1.setVisibility(View.VISIBLE);
                    selectUnitAfterBtn.setVisibility(View.GONE);
                    //path_list.add("교체 후: "+"사진선택 버튼 -> 사진앨범");
                    PIC_POSITION = 3;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);


                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }//NOTE: 첫번째 사진 클릭시 -> 사진앨범       //Note: 순번 3
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_PICK_1){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile();
                    Log.d("작업후_사진앨범_이미지파일>", imageFile.toString());
                    unitAfterImage1.setImageBitmap(resizedPhotoBm);
                    unitAfterImage1.setVisibility(View.VISIBLE);
                    unitAfterAddPic.setVisibility(View.VISIBLE);
                    selectUnitAfterBtn.setVisibility(View.GONE);
                    afterUnitDeletelBtn1.setVisibility(View.VISIBLE);
                    //path_list.add("교체 후: "+"첫번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 3;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);


                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }//NOTE: 두번째 사진 클릭시 -> 사진앨범           //Note: 순번 4
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_PICK_2){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile();
                    Log.d("작업후_사진앨범_이미지파일>", imageFile.toString());
                    unitAfterImage2.setImageBitmap(resizedPhotoBm);
                    unitAfterImage2.setVisibility(View.VISIBLE);
                    unitAfterAddPic.setVisibility(View.GONE);
                    selectUnitAfterBtn.setVisibility(View.GONE);
                    afterUnitDeletelBtn2.setVisibility(View.VISIBLE);
                   // path_list.add("교체 후: "+"두번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 4;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /** 사진 촬영 후 --> 추가버튼2 **/          //Note: 순번 4
        else if (requestCode == REQUEST_AFTER_ADD_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();
                        Log.d("작업후_사진촬영_이미지파일>", imageFile.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(unitAfterImage2);
                unitAfterImage2.setVisibility(View.VISIBLE);
                unitAfterAddPic.setVisibility(View.GONE);
                selectUnitAfterBtn.setVisibility(View.GONE);
                afterUnitDeletelBtn2.setVisibility(View.VISIBLE);
                //path_list.add("교체 후: "+"추가버튼 -> 사진촬영");
                PIC_POSITION = 4;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");
            }
        }  /** 사진앨범 선택 후 --> 추가버튼2 **/          //Note: 순번 4
        else if (requestCode == REQUEST_AFTER_ADD_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile();
                    Log.d("작업후_사진앨범_이미지파일>", imageFile.toString());
                    unitAfterImage2.setImageBitmap(resizedPhotoBm);
                    unitAfterImage2.setVisibility(View.VISIBLE);
                    unitAfterAddPic.setVisibility(View.GONE);
                    selectUnitAfterBtn.setVisibility(View.GONE);
                    afterUnitDeletelBtn2.setVisibility(View.VISIBLE);
                    //path_list.add("교체 후: "+"추가버튼 -> 사진앨범");
                    PIC_POSITION = 4;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);


                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //TODO:  차량 단말기 사진
        /** 사진촬영 **/              //Note: 순번 5
        else if (requestCode == REQUEST_BUS_UNIT_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();
                        Log.d("작업후_사진촬영_이미지파일>", imageFile.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(busUnitImage);
                busUnitImage.setVisibility(View.VISIBLE);
                busUnitCancelBtn.setVisibility(View.VISIBLE); //삭제버튼 보이기
                busUnitBtn.setVisibility(View.GONE);
               // path_list.add("차량 단말기: "+"사진선택 버튼 -> 사진촬영");
                PIC_POSITION = 5;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");

            }
        }
        /** 사진앨범 **/            //Note: 순번 5
        else if (requestCode == REQUEST_BUS_UNIT_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile();
                    Log.d("차량_사진앨범_이미지파일>", imageFile.toString());
                    busUnitImage.setImageBitmap(resizedPhotoBm);
                    busUnitImage.setVisibility(View.VISIBLE);
                    busUnitCancelBtn.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    busUnitBtn.setVisibility(View.GONE);
                    //path_list.add("차량 단말기: "+"사진선택 버튼 -> 사진앨범");
                    PIC_POSITION = 5;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //차량단말기 이미지 클릭시  -> 사진촬영      //Note: 순번 5
        }else if (requestCode == 1000) {
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
                        BmToFile();
                        Log.d("작업후_사진촬영_이미지파일>", imageFile.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(resizedPhotoBm)
                        .into(busUnitImage);
                busUnitImage.setVisibility(View.VISIBLE);
                busUnitCancelBtn.setVisibility(View.VISIBLE); //삭제버튼 보이기
                busUnitBtn.setVisibility(View.GONE);
               // path_list.add("차량 단말기: "+"사진 클릭시 -> 사진촬영");
                PIC_POSITION = 5;
                path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);

                for (String str : path_list){
                    path_map.put("ITEM"+PIC_POSITION, str+"");
                    mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                    update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                }

                Log.d("path_map: ", path_map+"");
            }
        }//차량단말기 이미지 클릭시  -> 사진앨범        //Note: 순번 5
        else if (requestCode == 2000){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    BmToFile();
                    Log.d("차량_사진앨범_이미지파일>", imageFile.toString());
                    busUnitImage.setImageBitmap(resizedPhotoBm);
                    busUnitImage.setVisibility(View.VISIBLE);
                    busUnitCancelBtn.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    busUnitBtn.setVisibility(View.GONE);
                   // path_list.add("차량 단말기: "+"사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 5;
                    path_list.add(imageFile + "&" + (folderName + "TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("path_list  ", PIC_POSITION + " 번째=> " + path_list);



                    for (String str : path_list){
                        path_map.put("ITEM"+PIC_POSITION, str+"");
                        mapValue = ("TROUBLE" + "/" + DTTI + "/" + "TROUBLE" + "_" + DTTI + "_" + TRANSP_BIZR_ID + "_" + BUS_ID + "_" + UNIT_CODE + "_" + PIC_POSITION + ".jpg").replace("%","/");
                        update_trouble_history_map.put("ITEM"+PIC_POSITION, mapValue);
                    }

                    Log.d("path_map: ", path_map+"");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            String barcode = intentResult.getContents();
            if(click_type.equals("stop")){
            }else if(click_type.equals("before")){
                insert_care_unit_before.setText(barcode);
            }else if(click_type.equals("after")){
                insert_care_unit_after.setText(barcode);
            }
        }


        Log.d("마지막체크","===============================================================");
        Log.d("<<<path_list>>>", path_list+"");
        Log.d("<<<path_map>>>", path_map+"");
        //TODO: 여기서 path_map 에 넣어주기..

        Log.d("update_path_map", update_trouble_history_map.toString());


    }//onActivityforResult..


   /* @Override        //기존 onActivityResult 코드
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("before")){
            insert_care_unit_before.setText(barcode);
        }else if(click_type.equals("after")){
            insert_care_unit_after.setText(barcode);
        }
    }*/
}
