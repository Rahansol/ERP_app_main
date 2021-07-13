package app.erp.com.erp_app.callcenter;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.CustomScannerActivity;
import app.erp.com.erp_app.dialog.DialogEduEmpList;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.New_Bus_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.dialog.Dialog_Doc_Info_View;
import app.erp.com.erp_app.vo.Bus_infoVo;
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

public class Fragment_trouble_insert_bus extends Fragment {
    /*최근 장애건수 확인버튼 및 리사이클러뷰 다이얼로그*/
    static String bus_id_value, transp_bizr_id_value;
    public static RecentErrorListAdapter recentErrorListAdapter;
    public static RecyclerView recyclerView_recentError;
    public static ArrayList<RecentErrorListItems> recentErrorListItems;

    CircleImageView unitBeforeAddPic, unitAfterAddPic, busUnitCancelBtn, beforeUnitDeletelBtn1, beforeUnitDeleteBtn2, afterUnitDeletelBtn1, afterUnitDeletelBtn2;
    Boolean isClicked = true;
    Button  error_insert_btn ,edit_care_emp_list, btn_error_event_num
            , selectUnitBeforeBtn, selectUnitAfterBtn, busUnitBtn;
    ImageView unitBeforeImage1, unitBeforeImage2, unitAfterImage1, unitAfterImage2, busUnitImage;

                /* 교체 전 */
    public int REQUEST_BEFORE_IMAGE_CAPTURED = 200      //사진선택 버튼 -> 사진촬영
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

    /* 이미지 경로 */
    private String mapValue;
    private ArrayList<String> path_list = new ArrayList<String>();
    private Map<String, Object> path_map = new HashMap<>();
    private String folderName = "nas_image/image/IERP/";
    private String DTTI, TRANSP_BIZR_ID, BUS_ID, UNIT_CODE ;    // tranp_bizr_id_value, bus_id_value
    private int PIC_POSITION;

    Uri imageUri, albumUri;
    String click_type , page_info, mCurrentPhotoPath;
    public File imageFile, cacheFilePath;
    public static Bitmap resizedPhotoBm, bm;
    //Bitmap bm;

    LinearLayout bus_num_barcode_find, insert_bus_info, bus_num_find;
    Context context;

    private Retrofit retrofit;
    private DialogEduEmpList mdialog;






    EditText find_bus_num ,field_error_garage ,field_error_route, field_error_phone, field_error_notice,unit_before_id,unit_after_id;
    TextView bus_area_name , bus_office_name, trouble_care_list;
    SharedPreferences pref, barcode_type_pref;
    SharedPreferences.Editor editor;

    LinearLayout care_layout , old_new_layout , old_barcode
            , new_old_layout ,new_barcode , bus_num_9999 , bus_num_nomal, new_selcet, old_select;
    ImageView unit_before_camera, unit_after_camera;

    CheckBox bs_yn;

    Spinner bus_num_list, field_trouble_error_type_list , field_trouble_high_code_list,
            field_trouble_low_code_list, field_trouble_care_code_list , bus_area_spinner,
            bus_office_group;

    RadioGroup today_group;
    RadioButton today_y , today_n;

    HashMap<String, Object> filed_error_map;

    View topview;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;
    Fragment tt;

    InputMethodManager imm;

    List<Edu_Emp_Vo> edu_list;
    List<Edu_Emp_Vo> gblist = new ArrayList<>();
    List<Edu_Emp_Vo> gnlist = new ArrayList<>();
    List<Edu_Emp_Vo> iclist = new ArrayList<>();
    List<Edu_Emp_Vo> pclist = new ArrayList<>();
    List<String> cooperate_list ;

    ScrollView main_container;

    public String st_field_error_garage, st_field_error_route;

    int check_count;
    public Fragment_trouble_insert_bus(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_insert_bus_new, container ,false);

        topview =view;
        context = getActivity();

        btn_error_event_num= (Button) view.findViewById(R.id.btn_error_event_num);

        main_container = (ScrollView) view.findViewById(R.id.main_container);

        imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        tt = this;
        //장애등록 담아서 보낼 hashmap
        filed_error_map = new HashMap<>();
        //차량번호 검색 버튼
        find_bus_num = (EditText)view.findViewById(R.id.find_bus_num);
        find_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    new Fragment_trouble_insert_bus.getfield_error_busnum().execute(find_bus_num.getText().toString());
                    handled =true;
                    downKeyboard(find_bus_num);
                    find_bus_num.clearFocus();
                }
                return handled;
            }
        });
        // 버스번호 포커스
        find_bus_num.requestFocus();
        upKeyboard(find_bus_num);

        // 유저정보 가져옴
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        //조합 운수사 감싸주는 레이아웃
        bus_num_9999 = (LinearLayout)view.findViewById(R.id.bus_num_9999);
        bus_num_9999.setVisibility(View.GONE);
        bus_num_nomal = (LinearLayout)view.findViewById(R.id.bus_num_nomal);

        //최근 장애건수 불러올때 필요한 파라미터
        String emp_id2 = pref.getString("emp_id","inter");

        // 사원리스트 가져옴
        String emp_id = pref.getString("emp_id",null);
        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Edu_Emp_Vo>> call_emp = erp.Edu_care_emp_list(emp_id);
        new Fragment_trouble_insert_bus.Edu_Care_Emp_List().execute(call_emp);

        trouble_care_list = (TextView)view.findViewById(R.id.trouble_care_list);

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
//                        care_emp_list = new ArrayList<>();
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
        // 사원리스트 가져와서 모달 띄우는 부분 end

        EditText inputField = (EditText)view.findViewById(R.id.field_error_phone);
        inputField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        bus_num_list = (Spinner)view.findViewById(R.id.bus_num_list);
        field_trouble_error_type_list = (Spinner)view.findViewById(R.id.field_trouble_error_type_list);
        field_trouble_high_code_list = (Spinner)view.findViewById(R.id.field_trouble_high_code_list);
        field_trouble_low_code_list = (Spinner)view.findViewById(R.id.field_trouble_low_code_list);
        field_trouble_care_code_list = (Spinner)view.findViewById(R.id.field_trouble_care_code_list);

        // editText
        field_error_garage =(EditText)view.findViewById(R.id.field_error_garage);
        field_error_garage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    downKeyboard(field_error_garage);
                    field_error_garage.clearFocus();
                }
                return handled;
            }
        });

        //영업소 editText 입력
        /*field_error_garage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (field_error_garage.getText().toString().contains(" ")){
                    Log.d("@@!!","공백있음");
                }
                field_error_garage.setText(field_error_garage.getText().toString().trim());
                field_error_route.setText(field_error_route.getText().toString().trim());
            }
        });*/



        field_error_route = (EditText)view.findViewById(R.id.field_error_route);
        field_error_route.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    downKeyboard(field_error_route);
                    field_error_route.clearFocus();
                }
                return handled;
            }
        });

        field_error_phone = (EditText)view.findViewById(R.id.field_error_phone);
        field_error_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    downKeyboard(field_error_phone);
                    field_error_phone.clearFocus();
                }
                return handled;
            }
        });

        field_error_notice = (EditText)view.findViewById(R.id.field_error_notice);
        field_error_notice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    downKeyboard(field_error_notice);
                    field_error_notice.clearFocus();
                }
                return handled;
            }
        });
        unit_before_id = (EditText)view.findViewById(R.id.unit_before_id);
        unit_before_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    downKeyboard(unit_before_id);
                    unit_before_id.clearFocus();
                }
                return handled;
            }
        });

        unit_after_id = (EditText)view.findViewById(R.id.unit_after_id);
        unit_after_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    downKeyboard(unit_after_id);
                    unit_after_id.clearFocus();
                }
                return handled;
            }
        });

        barcode_type_pref = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        editor = barcode_type_pref.edit();

        bus_area_name = (TextView)view.findViewById(R.id.bus_area_name);
        bus_office_name = (TextView)view.findViewById(R.id.bus_office_name);

        //조합 spinner
        bus_area_spinner = (Spinner)view.findViewById(R.id.bus_area_spinner);
        //운수사 spinner
        bus_office_group = (Spinner)view.findViewById(R.id.bus_office_group);
        new get_app_history_office_group().execute();

        //버스 번호 바코드 스캔
        //bus_num_barcode_find = (LinearLayout) view.findViewById(R.id.bus_num_barcode_find);
        /*bus_num_barcode_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "scan";
                editor.putString("camera_type" , "bus");
                editor.commit();
                IntentIntegrator.forSupportFragment(Fragment_trouble_insert_bus.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });*/

        insert_bus_info = (LinearLayout) view.findViewById(R.id.insert_bus_info);
        insert_bus_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , New_Bus_Activity.class);
                startActivity(i);
            }
        });

        //기존 바코드 스캔
        unit_before_camera = (ImageView)view.findViewById(R.id.unit_before_camera);
        unit_before_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "before";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forSupportFragment(Fragment_trouble_insert_bus.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });
        //교체 후 바코드 스캔
        unit_after_camera = (ImageView) view.findViewById(R.id.unit_after_camera);
        unit_after_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "after";
                editor.putString("camera_type" , "unit");
                editor.commit();
                IntentIntegrator.forSupportFragment(Fragment_trouble_insert_bus.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });



        bus_num_find = (LinearLayout) view.findViewById(R.id.bus_num_find);

        //버스검색 editText
        find_bus_num.setImeOptions(EditorInfo.IME_ACTION_DONE);   //키보드 [다음] -> [완료]버튼으로 변경
        find_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.find_bus_num && actionId == EditorInfo.IME_ACTION_DONE){   //키보드의 완료키 입력 검출

                    //edit: [버스검색]버튼 에 onClick 강제로 걸기
                    bus_num_find.performClick();
                }
                return false;
            }
        });

        //버스검색 버튼
        bus_num_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "text";
                downKeyboard(find_bus_num);
                find_bus_num.clearFocus();
                if(find_bus_num.getText().length() == 0){
                    Toast.makeText(context,"버스번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    new getfield_error_busnum().execute(find_bus_num.getText().toString());

                }
            }
        });
//      당일 처리 미처리 라디오 버튼
        filed_error_map.put("direct_care","Y");
        today_group = (RadioGroup)view.findViewById(R.id.today_group);
        today_y = (RadioButton)view.findViewById(R.id.today_y);
        today_n = (RadioButton)view.findViewById(R.id.today_n);
        today_y.setChecked(true);
        today_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.today_y :
                        today_y.setChecked(true);
                        today_n.setChecked(false);
                        filed_error_map.put("direct_care","Y");
                        break;
                    case R.id.today_n :
                        today_y.setChecked(false);
                        today_n.setChecked(true);
                        filed_error_map.put("direct_care","N");
                        break;
                }
            }
        });

        care_layout = (LinearLayout)view.findViewById(R.id.care_layout);
        old_new_layout = (LinearLayout)view.findViewById(R.id.old_new_layout);
        old_select = (LinearLayout)view.findViewById(R.id.old_select);
        old_barcode = (LinearLayout)view.findViewById(R.id.old_barcode);
        new_old_layout = (LinearLayout)view.findViewById(R.id.new_old_layout);
        new_selcet = (LinearLayout) view.findViewById(R.id.new_selcet);
        new_barcode = (LinearLayout)view.findViewById(R.id.new_barcode);

        care_layout.setVisibility(View.GONE);
//        old_new_layout.setVisibility(View.GONE);
        old_select.setVisibility(View.GONE);
//        old_barcode.setVisibility(View.GONE);
//        new_old_layout.setVisibility(View.GONE);
        new_selcet.setVisibility(View.GONE);
//        new_barcode.setVisibility(View.GONE);

        bs_yn = (CheckBox)view.findViewById(R.id.bs_yn);
        bs_yn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    filed_error_map.put("bs_yn","Y");
                    care_layout.setVisibility(View.VISIBLE);
                } else {
                    filed_error_map.put("bs_yn","N");
                    care_layout.setVisibility(View.GONE);
                }
            }
        });

        error_insert_btn = (Button)view.findViewById(R.id.error_insert_btn);
        error_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                all_keyboard_down();

                String emp_id = pref.getString("emp_id",null);
                String dep_code = pref.getString("dep_code",null);
                String care_cd = (String)filed_error_map.get("trouble_care_cd");



                filed_error_map.put("emp_id",emp_id);
                filed_error_map.put("dep_code",dep_code);
                filed_error_map.put("infra_code","1");
                filed_error_map.put("service_id","01");
                filed_error_map.put("garage_id",field_error_garage.getText().toString().trim());
                filed_error_map.put("route_id",field_error_route.getText().toString().trim());
                filed_error_map.put("driver_tel_num",field_error_phone.getText().toString());
                filed_error_map.put("notice",field_error_notice.getText().toString());
                filed_error_map.put("job_viewer",emp_id);
                filed_error_map.put("reg_emp_id",emp_id);
                filed_error_map.put("unit_before_id",unit_before_id.getText().toString());
                filed_error_map.put("unit_after_id",unit_after_id.getText().toString());

                // 이부분 화면에서 입력할때 무조건 입력하게끔으로 바꿔야함
                if(unit_before_id.getText().toString().length() != 0 || unit_after_id.getText().toString().length() != 0){
                    filed_error_map.put("unit_change_yn","Y");
                }else{
                    filed_error_map.put("unit_change_yn","N");
                }
                filed_error_map.put("unit_before_id",unit_before_id.getText().toString());
                filed_error_map.put("unit_after_id",unit_after_id.getText().toString());
                filed_error_map.put("move_distance","");
                filed_error_map.put("move_time","");
                filed_error_map.put("wait_time","");
                filed_error_map.put("work_time","");
                filed_error_map.put("restore_yn","N");

                filed_error_map.put("mintong","N");
                filed_error_map.put("analysis_yn","N");

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
                String getdate = sdf.format(date);
                String gettime = sdf2.format(date);

                filed_error_map.put("reg_time",gettime);
                filed_error_map.put("reg_date",getdate);

                if(null == filed_error_map.get("transp_bizr_id") ){
                    Toast.makeText(context,"운수사를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("bus_id") == null){
                    Toast.makeText(context,"버스를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if (filed_error_map.get("unit_code") == null){
                    Toast.makeText(context,"장비를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("trouble_high_cd") == null){
                    Toast.makeText(context,"대분류를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(filed_error_map.get("trouble_low_cd") == null){
                    Toast.makeText(context,"소분류를 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(bs_yn.isChecked()){
                    filed_error_map.put("bs_yn","Y");
                    if("X001".equals(care_cd)){
                        Toast.makeText(context,"BS건입니다. 조치항목을 선택해주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if("N".equals(filed_error_map.get("direct_care"))){
                        Toast.makeText(context,"BS건입니다. 당일 처리로 등록해 주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    filed_error_map.put("bs_yn","N");
                    if(!"X001".equals(care_cd)){
                        field_trouble_care_code_list.setSelection(0);
                        filed_error_map.put("trouble_care_cd","X001");
                        Toast.makeText(context,"일반장애 접수입니다. \n조치항목을 미처리로 변경, \n등록하기를 다시 터치 해주세요.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if("N".equals(filed_error_map.get("direct_care"))){
                    if(!"X001".equals(care_cd)){
                        field_trouble_care_code_list.setSelection(0);
                        filed_error_map.put("trouble_care_cd","X001");
                        Toast.makeText(context,"당일 미처리건입니다. \n조치항목을 미처리로 변경, \n등록하기를 다시 터치 해주세요.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                // 공동 작업자 인원수 체크해서 0명이면 key 값 n으로 0명이상이면 key y 로 넘기고 받은다음에 reg_emp_id 랑 care_emp_id  바꾸고
                // 컨트롤러에서 어차피 맵으로 받으니까 맵에다가 리스트 넣고 y 일때만 꺼내서 사용
                // 잡뷰어 코드 수정해야하는지 확인해야함 그 다음에 for 문으로 인원수만큼 인서트
                // insert_filed_error_test 현재 미조치 인서트
                // app_fieldError_care_insert 조치 완료 인서트

                if(check_count > 0){
                    filed_error_map.put("cooperate_key","Y");
                }else{
                    filed_error_map.put("cooperate_key","N");
                }

//                StringBuilder sb = new StringBuilder();
//                Set<?> set = filed_error_map.keySet();
//                Iterator<?> it = set.iterator();
//                while(it.hasNext()){
//                    String key = (String)it.next();
//                    if(key != null){
//                        sb.append("------------------------------------------------------------\n");
//                        sb.append("key = "+key+",\t\t\tvalue = "+filed_error_map.get(key)+"\n");
//                    }
//                }
//                Log.i("values info : " , ""+sb);

                progressDialog.setMessage("등록중...");
                progressDialog.show();

                if("X001".equals(care_cd)){
                    new insert_filed_error_jip_bus().execute();
                }else{
                    new app_jip_bus_care_insert().execute();
                }
            }


        });




        /* 단말기 교체 전/후 이미지 업로딩.. */

        /* 교체 전 이미지1,2 */
        unitBeforeImage1 = view.findViewById(R.id.unit_before_image1);
        unitBeforeImage2 = view.findViewById(R.id.unit_before_image2);

        /* 교체 전/후 추가버튼 */
        unitBeforeAddPic = view.findViewById(R.id.unitBeforeAddPic);
        unitAfterAddPic = view.findViewById(R.id.unitAfterAddPic);

        /* 교체 후 이미지1,2 */
        unitAfterImage1 = view.findViewById(R.id.unit_after_image1);
        unitAfterImage2 = view.findViewById(R.id.unit_after_image2);

        /* 교체 전/후 사진선택 버튼 */
        selectUnitBeforeBtn = view.findViewById(R.id.selectUnitBeforeBtn);
        selectUnitAfterBtn = view.findViewById(R.id.selectUnitAfterBtn);

        /* 교체 전 사진 삭제버튼1,2 */
        beforeUnitDeletelBtn1 = view.findViewById(R.id.before_unit_delete_btn_1);
        beforeUnitDeleteBtn2 = view.findViewById(R.id.before_unit_delete_btn_2);

        /* 교체 후 사진 삭제버튼1,2 */
        afterUnitDeletelBtn1 = view.findViewById(R.id.after_unit_delete_btn_1);
        afterUnitDeletelBtn2 = view.findViewById(R.id.after_unit_delete_btn_2);

        /* 차량 단말기 이미지 업로딩 */
        busUnitImage = view.findViewById(R.id.bus_unit_image);
        busUnitBtn = view.findViewById(R.id.bus_unit_btn);
        busUnitCancelBtn = view.findViewById(R.id.bus_unit_cancel_btn);

        /* 교체 전 사진선택 버튼 */
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


        /* 교체 전 사진 */
        /* 사진 촬영/ 선택 후 추가하기 버튼 */
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


        // 교체 전
        // 첫번째 사진 클릭시
        unitBeforeImage1.setOnClickListener(v -> {
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
        unitBeforeImage2.setOnClickListener(v -> {
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


        /* 첫번째 사진 삭제 */
        beforeUnitDeletelBtn1.setOnClickListener(v -> {
            unitBeforeImage1.setVisibility(View.GONE);
            beforeUnitDeletelBtn1.setVisibility(View.GONE);  //삭제버튼 삭제
            unitBeforeAddPic.setVisibility(View.GONE);
            selectUnitBeforeBtn.setVisibility(View.VISIBLE);
            path_list.remove(0);
        });


        /* 두번째 사진 삭제 */
        beforeUnitDeleteBtn2.setOnClickListener(v -> {
            //첫번째 사진이 삭제되었을 때 beforeUnitCancelBtn2 도 삭제해야됨... 어떻게??
            //뷰의 visibility 로 체크하기..
            if (beforeUnitDeletelBtn1.getVisibility() == View.GONE){
                unitBeforeImage2.setVisibility(View.GONE);
                beforeUnitDeleteBtn2.setVisibility(View.GONE);
                unitBeforeAddPic.setVisibility(View.GONE);
                path_list.remove(1);
            }else {
                unitBeforeImage2.setVisibility(View.GONE);
                beforeUnitDeleteBtn2.setVisibility(View.GONE);
                unitBeforeAddPic.setVisibility(View.VISIBLE);
                path_list.remove(1);
            }

        });








       /*교체 후 버튼*/
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


        //TODO:
        // 교체 후
        // 첫번째 사진 클릭시
        unitAfterImage1.setOnClickListener(v -> {
            //AFTER_PHOTO_CLICK_IMAGE_CAPTURE_1
            //AFTER_PHOTO_CLICK_IMAGE_PICK_1
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
        unitAfterImage2.setOnClickListener(v -> {
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
        afterUnitDeletelBtn1.setOnClickListener(v -> {
            unitAfterImage1.setVisibility(View.GONE);
            afterUnitDeletelBtn1.setVisibility(View.GONE);
            unitAfterAddPic.setVisibility(View.GONE);
            selectUnitAfterBtn.setVisibility(View.VISIBLE);
            path_list.remove(2);
        });

        /* 두번째 사진 삭제버튼 */
        afterUnitDeletelBtn2.setOnClickListener(v -> {
            Log.d("drawable state", unitAfterImage1.getDrawableState()+", drawable :"+unitAfterImage1.getDrawable()+"");

            if (afterUnitDeletelBtn1.getVisibility() == View.GONE){
                unitAfterImage2.setVisibility(View.GONE);
                afterUnitDeletelBtn2.setVisibility(View.GONE);
                unitAfterAddPic.setVisibility(View.GONE);
                path_list.remove(3);
            }else {
                unitAfterImage2.setVisibility(View.GONE);
                afterUnitDeletelBtn2.setVisibility(View.GONE);
                unitAfterAddPic.setVisibility(View.VISIBLE);
                path_list.remove(3);
            }

        });







        /* 차량 단말기 사진 업로드 */
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
        busUnitImage.setOnClickListener(v -> {
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
        busUnitCancelBtn.setOnClickListener(v -> {
            busUnitImage.setVisibility(View.GONE);
            busUnitCancelBtn.setVisibility(View.GONE);
            busUnitBtn.setVisibility(View.VISIBLE);
            path_list.remove(4);
        });


        REG_DTTI();


        return view;
    }//onCreateView..









    //버스번호 검색
    private class getfield_error_busnum extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_infoVo>> call = erp.getfield_error_busnum(strings[0]);

            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    start_spinner();
                    find_bus_num.clearFocus();
                    bus_num_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                    final List<Bus_infoVo> list = response.body();
                    if(list.size() > 0){
                        Toast.makeText(context,"버스를 선택해주세요",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"검색결과가 없습니다 다른 버스번호로 다시 검색해보세요",Toast.LENGTH_SHORT).show();
                    }
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("버스 선택");
                    for(Bus_infoVo i : list){
                        spinner_list.add(i.getBusoff_bus());
                    }
                    bus_num_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_num_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            filed_error_map.put("transp_bizr_id",null);
                            filed_error_map.put("trouble_high_cd",null);
                            filed_error_map.put("trouble_low_cd",null);
                            filed_error_map.put("trouble_care_cd",null);
                            filed_error_map.put("unit_code",null);

                            if(position > 0){
                                String select_bus_num = spinner_list.get(position);
                                for(int i = 0 ; i < list.size(); i++){
                                    if(list.get(i).getBusoff_bus() == select_bus_num){
                                        if("999999999".equals(list.get(i).getBus_id()) || "998999999".equals(list.get(i).getBus_id()) || "997999999".equals(list.get(i).getBus_id()) ){
                                            office_display_check(true);
                                            bus_area_name.setText("");
                                            bus_office_name.setText("");
                                        }else{
                                            office_display_check(false);
                                            filed_error_map.put("transp_bizr_id",list.get(i).getTransp_bizr_id());
                                            bus_area_name.setText(list.get(i).getOffice_group());
                                            bus_office_name.setText(list.get(i).getBusoff_name());

                                            transp_bizr_id_value= list.get(i).getTransp_bizr_id();
                                            Log.d("transp_bizr_id_value",  transp_bizr_id_value);
                                            bus_id_value= list.get(i).getBus_id();
                                            Log.d("bus_id_value",  bus_id_value);
                                        }
                                        filed_error_map.put("bus_id",list.get(i).getBus_id());
                                        break;
                                    }
                                }
                                bus_num_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
//                                field_error_route.requestFocus();
//                                upKeyboard(field_error_route);
                                field_trouble_error_type_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                new getfield_trouble_error_type().execute();     //장비 스피너 실행시키는 메소드



                                if("999999999".equals(transp_bizr_id_value) || "998999999".equals(transp_bizr_id_value) || "997999999".equals(transp_bizr_id_value) ) {

                                }else {
                                    /*최근 장애건수 텍스트 버튼 불러오기*/
                                    ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                    Call<List<Trouble_HistoryListVO>> call1 = erp.app_fieldError_6Month_Cnt(transp_bizr_id_value, bus_id_value);   //?????
                                    new getError_event_num().execute(call1);

                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {
                }
            });
            return null;
        }
    }

    // 장비 리스트 가져옴
    private class getfield_trouble_error_type extends AsyncTask<String , Integer , Long >{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type("01","1");
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {

                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("장비를 선택해주세요");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
//                    move_scroll(field_trouble_error_type_list);
                    field_trouble_error_type_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                    field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                    field_trouble_care_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));

                    field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                    field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                    field_trouble_care_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));

                    field_trouble_error_type_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            all_keyboard_down();
                            if(position > 0){
                                field_trouble_error_type_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                String select_error_name = spinner_list.get(position);
                                String select_error_code = "";
                                for(int i = 0; i < list.size(); i++){
                                    if(list.get(i).getUnit_name() == select_error_name){
                                        select_error_code = list.get(i).getUnit_code();
                                        filed_error_map.put("unit_code",list.get(i).getUnit_code());
                                        break;
                                    }
                                }
                                if(select_error_code.equals("GT")){
                                    old_select.setVisibility(View.VISIBLE);
                                    new_selcet.setVisibility(View.VISIBLE);
                                }else{
                                    old_select.setVisibility(View.GONE);
                                    new_selcet.setVisibility(View.GONE);
                                }
                                field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                new getfield_trouble_high_code().execute(select_error_code);
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



    // 최근 장애건수 가져옴
    private class getError_event_num extends AsyncTask<Call, Void, List<Trouble_HistoryListVO>>{

        @Override
        protected List<Trouble_HistoryListVO> doInBackground(Call... calls) {
            Call<List<Trouble_HistoryListVO>> call= calls[0];
            try {
                Response<List<Trouble_HistoryListVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trouble_HistoryListVO> trouble_historyListVOS) {
            super.onPostExecute(trouble_historyListVOS);

            Log.d("transp_bizr_id_value chk", transp_bizr_id_value+"" );   //왜 두번나옴??
            Log.d("bus_id_value chk", bus_id_value+"" );                   //왜 두번나옴 ??


            if (trouble_historyListVOS != null){
                btn_error_event_num.setText("발생 : "+trouble_historyListVOS.get(0).getBef_err_cnt()+" 건");
            }

            btn_error_event_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (trouble_historyListVOS.get(0).getBef_err_cnt().equals('0')){
                        Toast.makeText(context, "확인할 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "장애 발생건수 확인하기", Toast.LENGTH_SHORT).show();
                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<Trouble_HistoryListVO>> call= erp.app_fieldError_not_care_history(transp_bizr_id_value, bus_id_value);
                        new app_fieldError_not_care_history_dialog().execute(call);
                    }

                }
            });
        }
    }//getError_event_num


    private class app_fieldError_not_care_history_dialog extends AsyncTask<Call, Void, List<Trouble_HistoryListVO>>{

        @Override
        protected List<Trouble_HistoryListVO> doInBackground(Call... calls) {
            Call<List<Trouble_HistoryListVO>> call= calls[0];
            try {
                Response<List<Trouble_HistoryListVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trouble_HistoryListVO> trouble_historyListVOS) {
            super.onPostExecute(trouble_historyListVOS);

            if (trouble_historyListVOS.size() < 1){    //???
                Toast.makeText(context, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
               // Toast.makeText(context, "데이터를 확인합니다.", Toast.LENGTH_SHORT).show();
                Log.d("장애건수 사이즈 ::::: ", trouble_historyListVOS.size()+"");

                View view_dialog= getLayoutInflater().inflate(R.layout.recent_error_list_dialog_layout, null);
                recentErrorListItems= new ArrayList<>();
                for (int i=0; i<trouble_historyListVOS.size(); i++){
                    recentErrorListItems.add(new RecentErrorListItems(trouble_historyListVOS.get(i).getReg_date()
                                                                     ,trouble_historyListVOS.get(i).getReg_emp_name()
                                                                     ,trouble_historyListVOS.get(i).getUnit_before_id()
                                                                     ,trouble_historyListVOS.get(i).getCare_date()
                                                                     ,trouble_historyListVOS.get(i).getCare_emp_name()
                                                                     ,trouble_historyListVOS.get(i).getUnit_after_id()
                                                                     ,trouble_historyListVOS.get(i).getBusoff_name()
                                                                     ,trouble_historyListVOS.get(i).getGarage_id()
                                                                     ,trouble_historyListVOS.get(i).getRoute_num()
                                                                     ,trouble_historyListVOS.get(i).getUnit_name()
                                                                     ,trouble_historyListVOS.get(i).getTrouble_high_name()
                                                                     ,trouble_historyListVOS.get(i).getTrouble_low_name()
                                                                     ,trouble_historyListVOS.get(i).getTrouble_care_name()
                                                                     ,trouble_historyListVOS.get(i).getNotice()));
                }
                recyclerView_recentError= view_dialog.findViewById(R.id.recent_error_recyclerview_dialog);
                recentErrorListAdapter= new RecentErrorListAdapter(context, recentErrorListItems);
                recyclerView_recentError.setAdapter(recentErrorListAdapter);

                AlertDialog.Builder recentDialog= new AlertDialog.Builder(context);
                recentDialog.setView(view_dialog);
                recentDialog.setIcon(R.drawable.ic_error);
                recentDialog.setTitle("최근 6개월 장애건수");
                recentDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "장애건수를 확인하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                recentDialog.show();
            }
        }
    }





    //장애 대분류 리스트 가져옴
    private class getfield_trouble_high_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final String select_error_code = strings[0];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_high_code("01","1",select_error_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {

                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("장애대분류를 선택해주세요");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_high_name());
                    }
                    move_scroll(field_trouble_high_code_list);
                    field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_high_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            all_keyboard_down();
                            field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                            field_trouble_care_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                            filed_error_map.put("trouble_high_cd",null);
                            filed_error_map.put("trouble_low_cd",null);
                            filed_error_map.put("trouble_care_cd",null);
                            if(position > 0){
                                field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                String select_high_name = spinner_list.get(position);
                                String select_high_code = "";
                                for(int i = 0; i < list.size(); i++){
                                    if(list.get(i).getTrouble_high_name() == select_high_name){
                                        select_high_code = list.get(i).getTrouble_high_cd();

                                        filed_error_map.put("trouble_high_cd",list.get(i).getTrouble_high_cd());
                                        break;
                                    }
                                }
                                field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                new getfield_trouble_low_code().execute(select_error_code,select_high_code);
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

    // 소분류 리스트 가져옴
    private class getfield_trouble_low_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final String select_error_code = strings[0];
            final String select_high_code = strings[1];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_low_code("01","1",select_error_code,select_high_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {

                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("장애소분류를 선택해주세요");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_low_name());
                    }
                    move_scroll(field_trouble_low_code_list);
                    field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_low_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            all_keyboard_down();
                            if(position > 0){
                                filed_error_map.put("trouble_care_cd","X001");
                                field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                String select_low_name = spinner_list.get(position);
                                String select_low_code = "";

                                for(int i = 0 ; i < list.size(); i++){
                                    if(list.get(i).getTrouble_low_name() == select_low_name){
                                        select_low_code = list.get(i).getTrouble_low_cd();
                                        filed_error_map.put("trouble_low_cd",list.get(i).getTrouble_low_cd());
                                        break;
                                    }
                                }
                                field_trouble_care_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_select_background));
                                new getfield_trouble_carecode().execute(select_error_code,select_high_code,select_low_code);
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

    //조치항목
    private class getfield_trouble_carecode extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_carecode("01","1",strings[0],strings[1],strings[2]);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {

                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("조치항목 선택 (미선택 시 현재 미처리)");
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_care_name());
                    }
                    move_scroll(field_trouble_care_code_list);

                    field_trouble_care_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_care_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            all_keyboard_down();
                            if(position > 0){
                                field_trouble_care_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                                String select_care_name = spinner_list.get(position);
                                String select_care_code = "";
                                for(int i = 0 ; i < list.size(); i++){
                                    if(list.get(i).getTrouble_care_name() == select_care_name){
                                        select_care_code = list.get(i).getTrouble_care_cd();
                                        filed_error_map.put("trouble_care_cd",select_care_code);
                                        break;
                                    }
                                }
                            }else{
                                filed_error_map.put("trouble_care_cd","X001");
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

    private class insert_filed_error_jip_bus extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.insert_filed_error_jip_bus(filed_error_map,cooperate_list);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    boolean result = response.body();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    page_info = "list";
                    a_builder.setTitle("콜 처리");
                    a_builder.setCancelable(false);
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    String title = "";
                                    if(page_info.equals("repg")){
                                        fragment = new Fragment_trouble_insert();
                                    }else{
                                        fragment = new Fragment_trouble_insert();
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

    private class app_jip_bus_care_insert extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.app_jip_bus_care_insert(filed_error_map,cooperate_list);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    boolean result = response.body();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    page_info = "list";
                    a_builder.setTitle("콜 처리");
                    a_builder.setCancelable(false);
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    String title = "";
                                    if(page_info.equals("repg")){
                                        fragment = new Fragment_trouble_insert();
                                    }else{
                                        fragment = new Fragment_trouble_insert();
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

    //조합 리스트
    private class get_app_history_office_group extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_infoVo>> call = erp.get_app_history_office_group();
            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    final List<Bus_infoVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("조합 선택");
                    for (Bus_infoVo i : list){
                        spinner_list.add(i.getOffice_group());
                    }
                    bus_area_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position >0){
                                String select_nms_group = spinner_list.get(position);
                                new Fragment_trouble_insert_bus.Get_app_error_Bus_Office().execute(select_nms_group);
                            }else{
                                bus_office_group.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {
                }
            });
            return null;
        }
    }

    // 운수사 리스트
    private class Get_app_error_Bus_Office extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_infoVo>> call = erp.get_app_error_Bus_Office(strings[0]);
            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    final List<Bus_infoVo> list = response.body();
                    List<String> spinner_list = new ArrayList<>();
                    for (Bus_infoVo i : list){
                        spinner_list.add(i.getBusoff_name());
                    }
                    bus_office_group.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_office_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            filed_error_map.put("transp_bizr_id",list.get(position).getTransp_bizr_id());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

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

    public void downKeyboard(EditText editText) {
//        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void upKeyboard(EditText editText){
//        imm.showSoftInput(editText,0);
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void move_scroll(View view){
        main_container.smoothScrollTo(0,400);
    }

    private void all_keyboard_down(){
        downKeyboard(find_bus_num);
        downKeyboard(field_error_garage);
        downKeyboard(field_error_route);
        downKeyboard(field_error_phone);
        downKeyboard(field_error_notice);
        downKeyboard(unit_before_id);
        downKeyboard(unit_after_id);

        find_bus_num.clearFocus();
        field_error_garage.clearFocus();
        field_error_route.clearFocus();
        field_error_phone.clearFocus();
        field_error_notice.clearFocus();
        unit_before_id.clearFocus();
        unit_after_id.clearFocus();
    }

    private void office_display_check(boolean check){
        if(check){
            field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
            field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
            field_trouble_care_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));

            field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
            field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
            field_trouble_care_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
            bus_area_spinner.setSelection(0);
            bus_num_9999.setVisibility(View.VISIBLE);
            bus_num_nomal.setVisibility(View.GONE);
        }else{
            bus_num_nomal.setVisibility(View.VISIBLE);
            bus_num_9999.setVisibility(View.GONE);
        }
    }

    private void start_spinner(){

        field_trouble_error_type_list.setSelection(0);

        bus_area_name.setText("");
        bus_office_name.setText("");

        field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
        field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
        field_trouble_care_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));

        field_trouble_high_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
        field_trouble_low_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
        field_trouble_care_code_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
    }



    // 현재날짜
    public void REG_DTTI(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        DTTI = sdf.format(date);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //String strResult = intentResult.getContents();
        Log.d("intentResult>", ""+intentResult);  //null
        Log.d("requestCode>", ""+requestCode);

        //TODO: 작업 전 사진
        /** 사진촬영 **/       //Note: 순번 1
        if (requestCode == REQUEST_BEFORE_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                // Uri -------> Bitmap (사이즈/용량의 문제로 bitmap 으로 변경해주기)
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                //path_list.add("교체 전: "+"사진선택 버튼 -> 사진촬영");
                PIC_POSITION = 1;
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 1 >>", path_list+"");
            }
        }  //NOTE: 첫번째 사진 클릭시 -> 사진촬영       //Note: 순번 1
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_1){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                     unitBeforeAddPic.setVisibility(View.VISIBLE);   //사진촬영 후 추가버튼
                }
                beforeUnitDeletelBtn1.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                //path_list.add("교체 전: "+"첫번째 사진 클릭시 -> 사진촬영");
                PIC_POSITION = 1;
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        }
        //NOTE: 두번째 사진 클릭시 -> 사진촬영          //Note: 순번 2
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_CAPTURE_2){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        }
        /** 사진앨범 **/            //Note: 순번 1
        else if (requestCode == REQUEST_BEFORE_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    // 이미지뷰에 세팅
                    unitBeforeImage1.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage1.setVisibility(View.VISIBLE);
                    unitBeforeAddPic.setVisibility(View.VISIBLE);  //사진앨범 선택 후 추가버튼
                    beforeUnitDeletelBtn1.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    selectUnitBeforeBtn.setVisibility(View.GONE);
                   // path_list.add("교체 전: "+"사진선택 버튼 -> 사진앨범");
                    PIC_POSITION = 1;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } //NOTE: 첫번째 사진 클릭시 -> 사진앨범                //Note: 순번 1
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_PICK_1){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    // 이미지뷰에 세팅
                    unitBeforeImage1.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage1.setVisibility(View.VISIBLE);
                    if (unitBeforeImage2.getVisibility() == View.VISIBLE){
                        unitBeforeAddPic.setVisibility(View.GONE);  //사진앨범 선택 후 추가버튼
                    }else {
                        unitBeforeAddPic.setVisibility(View.VISIBLE);  //사진앨범 선택 후 추가버튼
                    }

                    //beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                   // path_list.add("교체 전: "+"첫번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 1;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //NOTE: 두번째 사진 클릭시 -> 사진앨범              //Note: 순번 2
        else if (requestCode == BEFORE_PHOTO_CLICK_IMAGE_PICK_2){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    // 이미지뷰에 세팅
                    unitBeforeImage2.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage2.setVisibility(View.VISIBLE);
                    unitBeforeAddPic.setVisibility(View.GONE);  //사진앨범 선택 후 추가버튼
                    beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    //path_list.add("교체 전: "+"두번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 2;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /** 사진 촬영 후 --> 추가버튼1 **/       //Note: 순번 2
        else if (requestCode == REQUEST_BEFORE_ADD_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        } /** 사진앨범 선택 후 --> 추가버튼1 **/           //Note: 순번 2
        else if (requestCode == REQUEST_BEFORE_ADD_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    // 이미지뷰에 세팅
                    unitBeforeImage2.setImageBitmap(resizedPhotoBm);
                    unitBeforeImage2.setVisibility(View.VISIBLE);
                    unitBeforeAddPic.setVisibility(View.GONE);
                    selectUnitBeforeBtn.setVisibility(View.GONE);  //사진선택 버튼 삭제
                    beforeUnitDeleteBtn2.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    //path_list.add("교체 전: "+"추가버튼 -> 사진촬영");
                    PIC_POSITION = 2;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //TODO: 작업 후 사진
        /** 사진촬영 **/            //Note: 순번 3
        else if (requestCode == REQUEST_AFTER_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }

        }  //NOTE: 첫번째 사진 클릭시 -> 사진촬영           //Note: 순번 3
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_CAPTURE_1){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                if (unitAfterImage2.getVisibility() == View.VISIBLE){
                    unitAfterAddPic.setVisibility(View.GONE);
                }else {
                    unitAfterAddPic.setVisibility(View.VISIBLE);
                }
                selectUnitAfterBtn.setVisibility(View.GONE);
                afterUnitDeletelBtn1.setVisibility(View.VISIBLE);
                //path_list.add("교체 전: "+"첫번째 사진 클릭시 -> 사진촬영");
                PIC_POSITION = 3;
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        }//NOTE: 두번째 사진 클릭시 -> 사진촬영         //Note: 순번 4
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_CAPTURE_2){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        }
        /** 사진앨범 **/                //Note: 순번 3
        else if (requestCode == REQUEST_AFTER_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    unitAfterImage1.setImageBitmap(resizedPhotoBm);
                    unitAfterImage1.setVisibility(View.VISIBLE);
                    unitAfterAddPic.setVisibility(View.VISIBLE);
                    afterUnitDeletelBtn1.setVisibility(View.VISIBLE);
                    selectUnitAfterBtn.setVisibility(View.GONE);
                   // path_list.add("교체 후: "+"사진선택 버튼 -> 사진앨범");
                    PIC_POSITION = 3;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }//NOTE: 첫번째 사진 클릭시 -> 사진앨범         //Note: 순번 3
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_PICK_1){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    unitAfterImage1.setImageBitmap(resizedPhotoBm);
                    unitAfterImage1.setVisibility(View.VISIBLE);
                    if (unitAfterImage2.getVisibility() == View.VISIBLE){
                        unitAfterAddPic.setVisibility(View.GONE);
                    }else {
                        unitAfterAddPic.setVisibility(View.VISIBLE);
                    }
                    selectUnitAfterBtn.setVisibility(View.GONE);
                    afterUnitDeletelBtn1.setVisibility(View.VISIBLE);
                   // path_list.add("교체 후: "+"첫번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 3;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }//NOTE: 두번째 사진 클릭시 -> 사진앨범             //Note: 순번 4
        else if (requestCode == AFTER_PHOTO_CLICK_IMAGE_PICK_2){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    unitAfterImage2.setImageBitmap(resizedPhotoBm);
                    unitAfterImage2.setVisibility(View.VISIBLE);
                    unitAfterAddPic.setVisibility(View.GONE);
                    selectUnitAfterBtn.setVisibility(View.GONE);
                    afterUnitDeletelBtn2.setVisibility(View.VISIBLE);
                   // path_list.add("교체 후: "+"두번째 사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 4;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /** 사진 촬영 후 --> 추가버튼2 **/           //Note: 순번 4
        else if (requestCode == REQUEST_AFTER_ADD_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        }  /** 사진앨범 선택 후 --> 추가버튼2 **/          //Note: 순번 4
        else if (requestCode == REQUEST_AFTER_ADD_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    unitAfterImage2.setImageBitmap(resizedPhotoBm);
                    unitAfterImage2.setVisibility(View.VISIBLE);
                    unitAfterAddPic.setVisibility(View.GONE);
                    selectUnitAfterBtn.setVisibility(View.GONE);
                    afterUnitDeletelBtn2.setVisibility(View.VISIBLE);
                    //path_list.add("교체 후: "+"추가버튼 -> 사진앨범");
                    PIC_POSITION = 4;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //TODO:  차량 단말기 사진
        /** 사진촬영 **/                //Note: 순번 5
        else if (requestCode == REQUEST_BUS_UNIT_IMAGE_CAPTURED){
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                //ath_list.add("차량 단말기: "+"사진선택 버튼 -> 사진촬영");
                PIC_POSITION = 5;
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        }
        /** 사진앨범 **/                //Note: 순번 5
        else if (requestCode == REQUEST_BUS_UNIT_IMAGE_PICK){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    busUnitImage.setImageBitmap(resizedPhotoBm);
                    busUnitImage.setVisibility(View.VISIBLE);
                    busUnitCancelBtn.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    busUnitBtn.setVisibility(View.GONE);
                    //path_list.add("차량 단말기: "+"사진선택 버튼 -> 사진앨범");
                    PIC_POSITION = 5;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //차량단말기 이미지 클릭시  -> 사진촬영            //Note: 순번 5
        }else if (requestCode == 1000) {
            if (resultCode == RESULT_OK){
                bm = null;
                if (imageUri != null){
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);    //  Uri -----> Bitmap
                        ResizingBitmapPhoto();
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
                //path_list.add("차량 단말기: "+"사진 클릭시 -> 사진촬영");
                PIC_POSITION = 5;
                path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                Log.d("before_unit_path_list 2 >>", path_list+"");
            }
        }//차량단말기 이미지 클릭시  -> 사진앨범            //Note: 순번 5
        else if (requestCode == 2000){
            if (resultCode == RESULT_OK){
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(intent.getData());
                    bm = BitmapFactory.decodeStream(in);
                    in.close();
                    ResizingBitmapPhoto();
                    busUnitImage.setImageBitmap(resizedPhotoBm);
                    busUnitImage.setVisibility(View.VISIBLE);
                    busUnitCancelBtn.setVisibility(View.VISIBLE);  //삭제버튼 보이기
                    busUnitBtn.setVisibility(View.GONE);
                    //path_list.add("차량 단말기: "+"사진 클릭시 -> 사진앨범");
                    PIC_POSITION = 5;
                    path_list.add(imageFile + "&" + (folderName + "TABLE_NAME" + "/" + DTTI + "/" + "TABLE_NAME" + "_" + DTTI + "_" + transp_bizr_id_value + "_" + bus_id_value + "_" + PIC_POSITION + ".jpg").replaceAll("/", "%"));
                    Log.d("before_unit_path_list 2 >>", path_list+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {   /* 교체 시리얼 바코드 스캔.. */
            String strResult = intentResult.getContents();
            if (click_type.equals("stop")){

            }else if (click_type.equals("scan")){
                new getfield_error_busnum().execute(strResult);
            }else if(click_type.equals("before")){
                unit_before_id.setText(strResult);
            }else if(click_type.equals("after")){
                unit_after_id.setText(strResult);
            }
        }

        Log.d("<<<로그 path_list>>>", path_list+"");
        //TODO:  여기서 path_map 에 더해주기..

    }//onActivityforResult..



    // Bitmap 사진 리사이징하기
    public void ResizingBitmapPhoto() throws IOException {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        double Wratio = 0.0;
        double Hratio = 0.0;

        Matrix matrix = new Matrix();
        if (bmWidth > bmHeight){
            Wratio = ((double)bmWidth / (double)bmHeight) * 512;
            Hratio = 1 * 512;
        }else {
            Wratio = 1 * 512;
            Hratio = ((double)bmHeight / (double)bmWidth) * 512;
        }
        //matrix.postRotate(90);
        resizedPhotoBm = bm.createScaledBitmap(bm, (int) Wratio , (int) Hratio, false);
        resizedPhotoBm = resizedPhotoBm.createBitmap(resizedPhotoBm, 0, 0, (int) Wratio, (int) Hratio, matrix, true);
    }



    // 사진촬영/ 캡쳐된 이미지 저장경로 지정
    public void setImageUri(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/IERP");

        if (!storageDir.exists()){
            storageDir.mkdirs();
        }else {
            Log.d("storageDir~~", storageDir+"");
        }

        imageFile = new File(storageDir, imageFileName);
        Log.d("imageFile체크체크>>", imageFile+"");
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.d("imagePath~~", mCurrentPhotoPath+"");

        //File ------> Uri
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            imageUri = Uri.fromFile(imageFile);
        }else {
            imageUri = FileProvider.getUriForFile(context, context.getPackageName(), imageFile);
        }
    }

}