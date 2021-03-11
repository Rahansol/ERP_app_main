package app.erp.com.erp_app.document_care;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.dialog.Dialog_Bus_find;
import app.erp.com.erp_app.vo.Image_Path_VO;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static app.erp.com.erp_app.document_care.CameraTestActivity.REQUEST_IMAGE_CAPTURE;
import static app.erp.com.erp_app.document_care.CameraTestActivity.REQUEST_VIDEO_CAPTURE;

public class Fragment_Project_Work_Insert_1 extends Fragment {

    public static Fragment_Project_Work_Insert_1 shareMyString(ArrayList<ProJectVO> value, ProJectVO vo) {

        Bundle args = new Bundle();
        args.putSerializable("list",value);
        args.putSerializable("pvo",vo);
        Fragment_Project_Work_Insert_1 f = new Fragment_Project_Work_Insert_1();
        f.setArguments(args);
        return f;
    }

    Spinner project_office_group, project_transp,project_garage_spinner,project_route_list,infra_job_spinner;
    String area_code;
    private String sub_area_code , office_group;
    private LinearLayout project_item_layout , route_num_layout;

    String mCurrentPhotoPath;
    List<Image_Path_VO> mCurrentPhotoPath_array;
    Uri imageUri;
    Uri photoURI, albumURI;
    Map<String, Object> request_map;
    Map<String, Object> image_path_map;
    Map<String, Object> radio_map;
    Map<String, Object> item_check_map;
    String p_btn_tag;
    String prj_code;
    String get_today, prj_seq;
    List<ProJectVO> list_size;

    LinearLayout vehicle_num_layout;
    private EditText vehicle_num, route_num;

    private View view;
    private List<ProJectVO> list = new ArrayList<>();
    private ERP_Spring_Controller erp;
    private Context mcontext;
    private Make_Project_Item mpi;
    private SharedPreferences pref;
    private TextView project_bus_list;

    private Dialog_Bus_find dbf;

    private boolean office_group_check = false;

    static final int REQUEST_IMAGE_GALLERY = 2;
    static final int REQUEST_VIDEO_GALLERY = 4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project_work_insert_1, container ,false);
        mcontext = getActivity();
        Bundle bundle = getArguments();

        //프로젝트 아이템 추가되는 레이아웃
        project_item_layout = (LinearLayout)view.findViewById(R.id.project_item_layout);

        // 공통 클랙스
        mpi = new Make_Project_Item();

        pref = mcontext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String emp_id = pref.getString("emp_id",null);

        // 어뎁터에서 넘어온 값
        ProJectVO pvo = (ProJectVO) bundle.getSerializable("pvo");
        ArrayList<ProJectVO> list = (ArrayList<ProJectVO>) bundle.getSerializable("list");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        get_today = sdf.format(date);

        // 전역 변수
        mCurrentPhotoPath_array = new ArrayList<>();
        request_map = new HashMap<>();
        image_path_map = new HashMap<>();
        radio_map = new HashMap<>();
        item_check_map = new HashMap<>();

        area_code = pvo.getArea_code();
        sub_area_code = pvo.getSub_area_code();
        office_group = pvo.getOffice_group();

        prj_code = pvo.getBase_infra_code() + pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq();
        prj_seq = pvo.getPrj_seq();
        request_map.put("reg_emp_id",emp_id);
        request_map.put("transp_bizr_id",null);
        request_map.put("busoff_name",null);
        request_map.put("garage_id",null);
        request_map.put("garage_name",null);
        request_map.put("route_id",null);
        request_map.put("route_num",null);
        request_map.put("bus_id",null);
        request_map.put("bus_num",null);

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        //전역변수 end

        //spinner
        project_office_group = (Spinner)view.findViewById(R.id.project_office_group);
        project_transp = (Spinner)view.findViewById(R.id.project_transp);
//        project_bus_list = (Spinner)view.findViewById(R.id.project_bus_list);
        project_garage_spinner = (Spinner)view.findViewById(R.id.project_garage_spinner);
        project_route_list = (Spinner)view.findViewById(R.id.project_route_list);
        infra_job_spinner = (Spinner)view.findViewById(R.id.infra_job_spinner);
        //spinner end

        project_bus_list = (TextView)view.findViewById(R.id.project_bus_list);

        //차대번호 레이아웃
        vehicle_num_layout = (LinearLayout)view.findViewById(R.id.vehicle_num_layout);
        request_map.put("vehicle_num","");
        vehicle_num = (EditText)view.findViewById(R.id.vehicle_num);
        vehicle_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    vehicle_num.clearFocus();
                    downKeyboard(vehicle_num);
                    request_map.put("vehicle_num" , v.getText().toString());
                    request_map.put("bus_id", "999999999");
                }
                return handled;
            }
        });

        vehicle_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                request_map.put("vehicle_num" , vehicle_num.getText().toString());
                request_map.put("bus_id", "999999999");
            }
        });

        // 노선번호 레이아웃
        route_num_layout = (LinearLayout)view.findViewById(R.id.route_num_layout);
        route_num = (EditText)view.findViewById(R.id.route_num);
        route_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    route_num.clearFocus();
                    downKeyboard(route_num);
                    request_map.put("route_id","");
                    request_map.put("route_num",v.getText().toString());
                }
                return handled;
            }
        });

        route_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                request_map.put("route_id","");
                request_map.put("route_num",route_num.getText().toString());
            }
        });

        // job_type spinner
        request_map.put("job_type","");
        infra_job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if(pos >0){
                    switch (pos){
                        case 1:
                            ((ProJect_Work_Insert_Activity) getActivity()).view_control_for_activity(false);
                            request_map.put("job_type","1");
                            break;
                        case 2:
                            ((ProJect_Work_Insert_Activity) getActivity()).view_control_for_activity(true);
                            request_map.put("job_type","2");
                            break;
                        case 3:
                            ((ProJect_Work_Insert_Activity) getActivity()).view_control_for_activity(false);
                            request_map.put("job_type","3");
                            break;
                        case 4:
                            ((ProJect_Work_Insert_Activity) getActivity()).view_control_for_activity(false);
                            request_map.put("job_type","4");
                            break;
                        case 5:
                            ((ProJect_Work_Insert_Activity) getActivity()).view_control_for_activity(false);
                            request_map.put("job_type","5");
                            break;
                    }
                }else{
                    request_map.put("job_type","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //선택한 조합리스트 가져옴
        if(null == office_group){
            office_group = "";
            office_group_check = true;
        }else{
            office_group_check = false;
        }

        RelativeLayout bus_num_find_alert = (RelativeLayout)view.findViewById(R.id.bus_num_find_alert);
        bus_num_find_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trnas_id = (String)request_map.get("transp_bizr_id");
                if(null == trnas_id){
                    Toast.makeText(mcontext,"운수사를 먼저 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                dbf = new Dialog_Bus_find(mcontext, trnas_id, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String bus_split[] = dbf.bus_num_id().split("&");
                        project_bus_list.setText(""+bus_split[1]);
                        if(bus_split[0] == "null"){
                            request_map.put("bus_id", null);
                        }else{
                            request_map.put("bus_id", bus_split[0]);
                        }
                        request_map.put("bus_num",bus_split[1]);

                        if(bus_split[2].equals("N")){
                            vehicle_num_layout.setVisibility(View.VISIBLE);
                        }else{
                            vehicle_num_layout.setVisibility(View.GONE);
                        }
                        dbf.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbf.dismiss();
                    }
                });
                dbf.show();

                DisplayMetrics dm = mcontext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = dm.widthPixels; //디바이스 화면 너비
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dbf.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = dbf.getWindow();
                window.setAttributes(lp);
                dbf.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });

        Call<List<ProJectVO>> call = erp.app_office_group_select(area_code , sub_area_code,office_group);
        new Fragment_Project_Work_Insert_1.get_office_group ().execute(call);

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        make_project_item_view(list);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String p_index  = "";
        String trnas_id = "";
        String bus_id   = "";
        String path     = "";
        String db_path   = "";
        String replace  = "";
        Button select_btn;
        Button select_btn2;

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_IMAGE_CAPTURE :
                    try{
                        String pic_path = galleryAddPic();
//                     이미지뷰에 세팅
                        p_index  = p_btn_tag.substring(5,p_btn_tag.length());
                        trnas_id = (String)request_map.get("transp_bizr_id");
                        bus_id   = (String)request_map.get("bus_id");
                        path     = "nas_image/image/IERP/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".jpg";
                        db_path   = "project_img/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".jpg";
                        replace  = path.replaceAll("/","%");

                        select_btn = (Button) radio_map.get("pic_button"+p_index );
                        select_btn.setBackground(mcontext.getResources().getDrawable(R.drawable.blue_square_btn));
                        select_btn.setTextColor(mcontext.getResources().getColor(R.color.white));

                        request_map.put(p_btn_tag,db_path);
                        image_path_map.put(p_btn_tag , pic_path+"&"+replace);

                        select_btn2 = (Button)radio_map.get("select_button"+p_index );
                        select_btn2.setBackground(mcontext.getResources().getDrawable(R.drawable.custom_btn));
                        select_btn2.setTextColor(mcontext.getResources().getColor(R.color.textBlack2));
                        select_btn2.setText("선택");

                        setImageView(p_index,pic_path);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_VIDEO_CAPTURE :
                    try{
                        String pic_path = galleryAddPic();
//                     이미지뷰에 세팅
                        p_index  = p_btn_tag.substring(5,p_btn_tag.length());
                        trnas_id = (String)request_map.get("transp_bizr_id");
                        bus_id   = (String)request_map.get("bus_id");
                        path     = "nas_image/image/IERP/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".mp4";
                        db_path   = "project_img/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".mp4";
                        replace  = path.replaceAll("/","%");

                        select_btn = (Button) radio_map.get("pic_button"+p_index );
                        select_btn.setBackground(mcontext.getResources().getDrawable(R.drawable.blue_square_btn));
                        select_btn.setTextColor(mcontext.getResources().getColor(R.color.white));

                        request_map.put(p_btn_tag,db_path);
                        image_path_map.put(p_btn_tag , pic_path+"&"+replace);

                        select_btn2 = (Button)radio_map.get("select_button"+p_index );
                        select_btn2.setBackground(mcontext.getResources().getDrawable(R.drawable.custom_btn));
                        select_btn2.setTextColor(mcontext.getResources().getColor(R.color.textBlack2));
                        select_btn2.setText("선택");

                        setVideoView(p_index,pic_path);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_IMAGE_GALLERY :
                    try {
                        // 선택한 이미지에서 비트맵 생성
                        InputStream in = mcontext.getContentResolver().openInputStream(data.getData());
                        Uri selectedImage = data.getData();
                        Log.d("selected Image :::::::::::: >>>>>>>>> ", selectedImage+"");
                        Bitmap img = BitmapFactory.decodeStream(in);

                        int column_index=0;
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = mcontext.getContentResolver().query(selectedImage, proj, null, null, null);
                        if(cursor.moveToFirst()){
                            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        }
                        String gallery_path =  cursor.getString(column_index);
                        Log.d("gallery_path ::::::::::::: >>>>>>>>>>> ", gallery_path+"");
                        in.close();
                        // 이미지뷰에 세팅
                        p_index  = p_btn_tag.substring(5,p_btn_tag.length());
                        trnas_id = (String)request_map.get("transp_bizr_id");
                        bus_id   = (String)request_map.get("bus_id");
                        path     = "nas_image/image/IERP/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".jpg";
                        db_path   = "project_img/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".jpg";
                        replace  = path.replaceAll("/","%");

                        select_btn = (Button) radio_map.get("select_button"+p_index );
                        select_btn.setText("선택완료");
                        select_btn.setBackground(mcontext.getResources().getDrawable(R.drawable.blue_square_btn));
                        select_btn.setTextColor(mcontext.getResources().getColor(R.color.white));

                        request_map.put(p_btn_tag,db_path);
                        image_path_map.put(p_btn_tag , gallery_path+"&"+replace);

                        select_btn2 = (Button)radio_map.get("pic_button"+p_index );
                        select_btn2.setBackground(mcontext.getResources().getDrawable(R.drawable.custom_btn));
                        select_btn2.setTextColor(mcontext.getResources().getColor(R.color.textBlack2));

                        setImageView(p_index,gallery_path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_VIDEO_GALLERY :
                    try {
                        // 선택한 이미지에서 비트맵 생성
                        InputStream in = mcontext.getContentResolver().openInputStream(data.getData());
                        Uri selectedImage = data.getData();
                        Bitmap img = BitmapFactory.decodeStream(in);

                        int column_index=0;
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = mcontext.getContentResolver().query(selectedImage, proj, null, null, null);
                        if(cursor.moveToFirst()){
                            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        }
                        String gallery_path =  cursor.getString(column_index);
                        in.close();
                        // 이미지뷰에 세팅
                        p_index = p_btn_tag.substring(5,p_btn_tag.length());
                        trnas_id = (String)request_map.get("transp_bizr_id");
                        bus_id = (String)request_map.get("bus_id");
                        path = "nas_image/image/IERP/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".mp4";
                        db_path = "project_img/"+prj_code+"/"+get_today+"/"+prj_code+"_"+get_today+"_"+trnas_id+"_"+bus_id+"_"+p_index+".mp4";
                        replace = path.replaceAll("/","%");

                        select_btn = (Button) radio_map.get("select_button"+p_index );
                        select_btn.setText("선택완료");
                        select_btn.setBackground(mcontext.getResources().getDrawable(R.drawable.blue_square_btn));
                        select_btn.setTextColor(mcontext.getResources().getColor(R.color.white));
                        request_map.put(p_btn_tag,db_path);
                        image_path_map.put(p_btn_tag , gallery_path+"&"+replace);

                        select_btn2 = (Button)radio_map.get("pic_button"+p_index );
                        select_btn2.setBackground(mcontext.getResources().getDrawable(R.drawable.custom_btn));
                        select_btn2.setTextColor(mcontext.getResources().getColor(R.color.textBlack2));

                        setVideoView(p_index,gallery_path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private class get_office_group extends AsyncTask<Call, Void , List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try {
                Call<List<ProJectVO>> call =calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(proJectVOS != null){
                List<String> office_group_names = new ArrayList<>();
                office_group_names.add("조합 선택");
                for(int i=0; i<proJectVOS.size(); i++){
                    office_group_names.add(proJectVOS.get(i).getOffice_group());
                }
                project_office_group.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,office_group_names));
                if(!office_group_check)project_office_group.setSelection(1);
                project_office_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i > 0){
                            proJectVOS.get(i-1).getOffice_group();
                            Call<List<ProJectVO>> call = erp.app_transp_bizr_select(proJectVOS.get(i-1).getOffice_group(), area_code, sub_area_code);
                            new Fragment_Project_Work_Insert_1.get_transp_bizr_select().execute(call);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

        }
    }

    private class get_transp_bizr_select extends AsyncTask<Call, Void , List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(proJectVOS != null){
                List<String> transp_id = new ArrayList<>();
                transp_id.add("운수사 선택");
                for(int i=0; i<proJectVOS.size(); i++){
                    transp_id.add(proJectVOS.get(i).getBusoff_name());
                }
                project_transp.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,transp_id));
                project_transp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i>0){
                            request_map.put("transp_bizr_id",proJectVOS.get(i-1).getTransp_bizr_id());
                            request_map.put("busoff_name",proJectVOS.get(i-1).getBusoff_name());

                            Call<List<ProJectVO>> call2 = erp.app_prj_garage_select(proJectVOS.get(i-1).getTransp_bizr_id());
                            new Fragment_Project_Work_Insert_1.app_prj_garage_select().execute(call2);

                            Call<List<ProJectVO>> call3 = erp.transp_route_num_list(proJectVOS.get(i-1).getTransp_bizr_id());
                            new Fragment_Project_Work_Insert_1.transp_route_num_list().execute(call3);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    private class app_prj_garage_select extends AsyncTask<Call,Void, List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(proJectVOS != null){
                List<String> garage_name = new ArrayList<>();
                garage_name.add("영업소 선택");
                for(int i=0; i<proJectVOS.size(); i++){
                    garage_name.add(proJectVOS.get(i).getGarage_name());
                }
                project_garage_spinner.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,garage_name));
                project_garage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i>0){

                            request_map.put("garage_id",proJectVOS.get(i-1).getGarage_id());
                            request_map.put("garage_name",proJectVOS.get(i-1).getGarage_name());
                        }else{
                            request_map.put("garage_id","");
                            request_map.put("garage_name","");
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    private class transp_route_num_list extends AsyncTask<Call,Void,List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call =calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if (proJectVOS != null){
                List<String> route_id = new ArrayList<>();
                route_id.add("노선 선택");
                route_id.add("번호 없음 (노선번호 입력)");
                for(int i=0; i<proJectVOS.size(); i++){
                    route_id.add(proJectVOS.get(i).getRoute_num());
                }
                project_route_list.setAdapter(new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_dropdown_item,route_id));
                project_route_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i>1){
                            route_num_layout.setVisibility(View.GONE);
                            request_map.put("route_id",proJectVOS.get(i-2).getRoute_id());
                            request_map.put("route_num",proJectVOS.get(i-2).getRoute_num());
                        }else if(i == 1){
                            route_num_layout.setVisibility(View.VISIBLE);
                            request_map.put("route_id","");
                            request_map.put("route_num","");
                        }else{
                            request_map.put("route_id","");
                            request_map.put("route_num","");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }








    void make_project_item_view (List<ProJectVO> list){
        if(null !=list){
            list_size = list;
            final int lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, lidth);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,10);
            Map<String,Object> rgmap = new HashMap<>();

            for(int i=0; i<list.size(); i++){
                request_map.put("item_"+list.get(i).getItem_seq() , "");
                item_check_map.put("item_"+list.get(i).getItem_seq() , list.get(i).getItem_type());
                if(list.get(i).getItem_type().equals("S")){
                    RadioGroup rg = new RadioGroup(mcontext);
                    String id = "item_" +list.get(i).getItem_seq();
                    rg.setTag(id);
                    rgmap.put("item_"+list.get(i).getItem_seq() ,rg);
                    request_map.put("item_"+list.get(i).getItem_seq(),"");
                }
            }

            for(int i=0; i<list.size(); i++){
                LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout main = new LinearLayout(mcontext);
                main.setLayoutParams(main_params);
                main.setOrientation(LinearLayout.VERTICAL);

                LinearLayout li = new LinearLayout(mcontext);
                if(list.get(i).getItem_type().equals("S") || list.get(i).getItem_type().equals("C")){
                    li.setOrientation(LinearLayout.VERTICAL);
                }else{
                    li.setOrientation(LinearLayout.HORIZONTAL);
                }
                main.addView(li);

                TextView job_text = new TextView(mcontext);
                String item_type = list.get(i).getItem_type();

                switch (item_type){
                    case "P" :
                        li.setLayoutParams(params);
                        li.setWeightSum(5);

                        int pheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams prview_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pheight);
                        final ImageView imageView = new ImageView(mcontext);
                        imageView.setLayoutParams(prview_params);
                        imageView.setVisibility(View.GONE);
                        radio_map.put("pic_viewer"+list.get(i).getItem_seq() , imageView);


                        int p_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        int p_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams p_params = new LinearLayout.LayoutParams(p_width, p_height);
                        p_params.setMargins(0,0,5,0);
//                        LinearLayout.LayoutParams prview_btn_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        final ToggleButton priveiew_btn = new ToggleButton(mcontext);
                        priveiew_btn.setTextOff("미리보기");
                        priveiew_btn.setTextOn("미리보기");
                        priveiew_btn.setText("미리보기");
                        priveiew_btn.setLayoutParams(p_params);
                        priveiew_btn.setBackgroundResource(R.drawable.custom_btn);
                        priveiew_btn.setPadding(3,3,3,3);
                        priveiew_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(priveiew_btn.isChecked()){
                                    imageView.setVisibility(View.VISIBLE);
                                }else{
                                    imageView.setVisibility(View.GONE);
                                }
                            }
                        });
                        main.addView(imageView);
//                        ------------------------------------

                        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams t_params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                        t_params.weight = 5f;

                        LinearLayout preview_layout = new LinearLayout(mcontext);
                        LinearLayout.LayoutParams prview_layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        prview_layout_params.weight = 5f;
                        preview_layout.setOrientation(LinearLayout.HORIZONTAL);
                        preview_layout.setLayoutParams(prview_layout_params);

                        job_text.setText(list.get(i).getJob_text());
//                        job_text.setLayoutParams(t_params);
                        job_text.setTextColor(mcontext.getResources().getColor(R.color.textBlack));

                        preview_layout.addView(job_text);
//                        preview_layout.addView(priveiew_btn);
                        li.addView(preview_layout);


                        final Button work_btn = new Button(mcontext);
                        work_btn.setText("선택");
                        work_btn.setTag("item_"+list.get(i).getItem_seq());
                        work_btn.setPadding(3,3,3,3);

                        radio_map.put("select_button"+list.get(i).getItem_seq() , work_btn);

                        work_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String bus_id = (String)request_map.get("bus_id");
                                if(null == bus_id || bus_id.equals("null")){
                                    Toast.makeText(mcontext,"조합 , 운수사 , 차량을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
                                }else{
                                    getAlbum(REQUEST_IMAGE_GALLERY);
                                    p_btn_tag = view.getTag().toString();
                                }
                            }
                        });


//                        ----------------- 선택 버튼 end

                        int bwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        int bheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams b_params = new LinearLayout.LayoutParams(bwidth, bheight);
                        b_params.setMargins(0,0,5,0);

                        work_btn.setLayoutParams(b_params);
                        work_btn.setBackgroundResource(R.drawable.custom_btn);
                        li.addView(work_btn);

                        final Button pic_btn = new Button(mcontext);
                        pic_btn.setText("촬영");
                        pic_btn.setTag("item_"+list.get(i).getItem_seq());
                        pic_btn.setPadding(3,3,3,3);

                        radio_map.put("pic_button"+list.get(i).getItem_seq() , pic_btn);

                        pic_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String bus_id = (String)request_map.get("bus_id");
                                if(null == bus_id || bus_id.equals("null")){
                                    Toast.makeText(mcontext,"조합 , 운수사 , 차량을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
                                }else{
                                    //                                ((Button) view).setText("선택완료");

                                    captureCamera(MediaStore.ACTION_IMAGE_CAPTURE,REQUEST_IMAGE_CAPTURE);
                                    p_btn_tag = view.getTag().toString();
                                }
                            }
                        });

                        bwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        bheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        b_params = new LinearLayout.LayoutParams(bwidth, bheight);
                        b_params.setMargins(0,0,5,0);

                        pic_btn.setLayoutParams(b_params);
                        pic_btn.setBackgroundResource(R.drawable.custom_btn);
                        li.addView(pic_btn);

                        li.addView(priveiew_btn);

//                        ------------------- 촬영 버튼 end


//                                    ---------------------- 미리보기 end
                        break;
                    case "V" :
                        li.setLayoutParams(params);
                        li.setWeightSum(5);

                        int Vheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams Vrview_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Vheight);
                        final ImageView VimageView = new ImageView(mcontext);
                        VimageView.setLayoutParams(Vrview_params);
                        VimageView.setVisibility(View.GONE);
                        radio_map.put("pic_viewer"+list.get(i).getItem_seq() , VimageView);


                        int Vp_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        int Vp_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams Vp_params = new LinearLayout.LayoutParams(Vp_width, Vp_height);
                        Vp_params.setMargins(0,0,5,0);
//                        LinearLayout.LayoutParams prview_btn_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        final ToggleButton Vpriveiew_btn = new ToggleButton(mcontext);
                        Vpriveiew_btn.setTextOff("미리보기");
                        Vpriveiew_btn.setTextOn("미리보기");
                        Vpriveiew_btn.setText("미리보기");
                        Vpriveiew_btn.setLayoutParams(Vp_params);
                        Vpriveiew_btn.setBackgroundResource(R.drawable.custom_btn);
                        Vpriveiew_btn.setPadding(3,3,3,3);
                        Vpriveiew_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(Vpriveiew_btn.isChecked()){
                                    VimageView.setVisibility(View.VISIBLE);
                                }else{
                                    VimageView.setVisibility(View.GONE);
                                }
                            }
                        });
                        main.addView(VimageView);
//                        ------------------------------------

                        final int Vwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams Vt_params = new LinearLayout.LayoutParams(Vwidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Vt_params.weight = 5f;

                        LinearLayout Vpreview_layout = new LinearLayout(mcontext);
                        LinearLayout.LayoutParams Vprview_layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        Vprview_layout_params.weight = 5f;
                        Vpreview_layout.setOrientation(LinearLayout.HORIZONTAL);
                        Vpreview_layout.setLayoutParams(Vprview_layout_params);

                        job_text.setText(list.get(i).getJob_text());
//                        job_text.setLayoutParams(t_params);
                        job_text.setTextColor(mcontext.getResources().getColor(R.color.textBlack));

                        Vpreview_layout.addView(job_text);
//                        preview_layout.addView(priveiew_btn);
                        li.addView(Vpreview_layout);


                        final Button Vwork_btn = new Button(mcontext);
                        Vwork_btn.setText("선택");
                        Vwork_btn.setTag("item_"+list.get(i).getItem_seq());
                        Vwork_btn.setPadding(3,3,3,3);

                        radio_map.put("select_button"+list.get(i).getItem_seq() , Vwork_btn);

                        Vwork_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String bus_id = (String)request_map.get("bus_id");
                                if(null == bus_id || bus_id.equals("null")){
                                    Toast.makeText(mcontext,"조합 , 운수사 , 차량을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
                                }else{
                                    getAlbum(REQUEST_VIDEO_GALLERY);
                                    p_btn_tag = view.getTag().toString();
                                }
                            }
                        });


//                        ----------------- 선택 버튼 end

                        int Vbwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        int Vbheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams Vb_params = new LinearLayout.LayoutParams(Vbwidth, Vbheight);
                        Vb_params.setMargins(0,0,5,0);

                        Vwork_btn.setLayoutParams(Vb_params);
                        Vwork_btn.setBackgroundResource(R.drawable.custom_btn);
                        li.addView(Vwork_btn);

                        final Button Vpic_btn = new Button(mcontext);
                        Vpic_btn.setText("촬영");
                        Vpic_btn.setTag("item_"+list.get(i).getItem_seq());
                        Vpic_btn.setPadding(3,3,3,3);

                        radio_map.put("pic_button"+list.get(i).getItem_seq() , Vpic_btn);

                        Vpic_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String bus_id = (String)request_map.get("bus_id");
                                if(null == bus_id || bus_id.equals("null")){
                                    Toast.makeText(mcontext,"조합 , 운수사 , 차량을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
                                }else{
                                    //                                ((Button) view).setText("선택완료");

                                    captureCamera(MediaStore.ACTION_IMAGE_CAPTURE,REQUEST_IMAGE_CAPTURE);
                                    p_btn_tag = view.getTag().toString();
                                }
                            }
                        });

                        bwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        bheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        b_params = new LinearLayout.LayoutParams(bwidth, bheight);
                        b_params.setMargins(0,0,5,0);

                        Vpic_btn.setLayoutParams(b_params);
                        Vpic_btn.setBackgroundResource(R.drawable.custom_btn);
                        li.addView(Vpic_btn);

                        li.addView(Vpriveiew_btn);
                        break;
                    case "C" :
                        // 확인버튼 꼭 눌러야함
                        job_text.setText(list.get(i).getJob_text());
                        li.addView(job_text);

                        final EditText editText = new EditText(mcontext);
                        editText.setTag("item_" + list.get(i).getItem_seq());
                        editText.setSingleLine();
                        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                boolean handled =false;
                                if (actionId == EditorInfo.IME_ACTION_DONE){
                                    handled =true;
                                    editText.clearFocus();
                                    downKeyboard(editText);
                                    request_map.put(v.getTag().toString() , v.getText().toString());
                                }
                                return handled;
                            }
                        });

                        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean b) {
                                request_map.put(v.getTag().toString() , editText.getText().toString());
                            }
                        });

                        li.addView(editText);
                        break;
                    case "S":
                        job_text.setText(list.get(i).getJob_text());
                        li.addView(job_text);

                        RadioGroup main_rg = (RadioGroup)rgmap.get("item_" + list.get(i).getItem_seq());
                        main_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                                request_map.put(radioGroup.getTag().toString() , checkedId);
                            }
                        });
                        li.addView(main_rg);
                        break;
                    case "Si" :
                        String inext = list.get(i).getItem_seq();
                        RadioGroup sub_rg = (RadioGroup)rgmap.get("item_" +inext);
                        RadioButton radioButton = new RadioButton(mcontext);
                        radioButton.setText(list.get(i).getJob_text());
                        radioButton.setId(Integer.parseInt(list.get(i).getSelect_seq()));
                        sub_rg.addView(radioButton);
                        break;
                }
                project_item_layout.addView(main);
            }
        }
    }

    private void getAlbum(int intentType){
        Intent intent = new Intent(Intent.ACTION_PICK);
        if(intentType == 2){
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        }else if(intentType == 4){
            intent.setType(MediaStore.Video.Media.CONTENT_TYPE);
        }
        startActivityForResult(intent, intentType);
    }

    private String galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath); // 지금 전역변수로 파일을 만들죠 ?네 그럼 저 전역변수가
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mcontext.sendBroadcast(mediaScanIntent);
        Toast.makeText(mcontext, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
        return mCurrentPhotoPath;
    }

    private void setImageView(String select_index, String img_path){
        Bitmap bitmap = BitmapFactory.decodeFile(img_path);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(img_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = mpi.exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        ImageView select_img_view = (ImageView) radio_map.get("pic_viewer"+select_index);
        select_img_view.setImageBitmap(mpi.rotate(bitmap, exifDegree));
    }

    private void setVideoView(String select_index, String video_path){

        try {
            // 썸네일 추출후 리사이즈해서 다시 비트맵 생성
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 360, 480);

            ImageView select_img_view = (ImageView) radio_map.get("pic_viewer"+select_index);
            select_img_view.setImageBitmap(thumbnail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downKeyboard(EditText editText) {
//        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void captureCamera(String cameraType , int intenType){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(cameraType);
            if (takePictureIntent.resolveActivity(mcontext.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile(intenType);
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    Uri providerURI = FileProvider.getUriForFile(mcontext, mcontext.getPackageName(), photoFile);
                    imageUri = providerURI;
                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(takePictureIntent, intenType);
                }
            }
        } else {
            Toast.makeText(mcontext, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile(int intenType) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "";
        if(intenType == 1){
            imageFileName = "JPEG_" + timeStamp + ".jpg";
        }else{
            imageFileName = "MP4_" + timeStamp + ".mp4";
        }
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP");
        Log.d("storageDir===================",storageDir+"");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    public Map<String, Object> return_reques_map (){
        return request_map;
    }

    public Map<String, Object> return_imgage_map (){
        return image_path_map;
    }
}
