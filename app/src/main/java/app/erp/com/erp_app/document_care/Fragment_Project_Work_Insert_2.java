package app.erp.com.erp_app.document_care;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Image_Path_VO;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

import static app.erp.com.erp_app.document_care.CameraTestActivity.REQUEST_IMAGE_CAPTURE;

public class Fragment_Project_Work_Insert_2 extends Fragment {

    public static Fragment_Project_Work_Insert_2 shareMyString(ArrayList<ProJectVO> value, ProJectVO vo) {

        Bundle args = new Bundle();
        args.putSerializable("list",value);
        args.putSerializable("pvo",vo);
        Fragment_Project_Work_Insert_2 f = new Fragment_Project_Work_Insert_2();
        f.setArguments(args);
        return f;
    }

    Spinner project_office_group, project_transp, project_bus_list ,project_garage_spinner;
    String area_code;
    private String sub_area_code , office_group;
    LinearLayout project_item_layout;

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
    TextView vehicle_num;

    private View view;
    private List<ProJectVO> list = new ArrayList<>();
    private ERP_Spring_Controller erp;
    private Context mcontext;
    private Make_Project_Item mpi;
    private SharedPreferences pref;
    private List<LinearLayout> view_layout_list = new ArrayList<>();


    static final int REQUEST_IMAGE_GALLERY = 2;
    static final int REQUEST_VIDEO_GALLERY = 4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project_work_insert_2, container ,false);
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

        // 전역 변수
        mCurrentPhotoPath_array = new ArrayList<>();
        request_map = new HashMap<>();
        image_path_map = new HashMap<>();
        radio_map = new HashMap<>();
        item_check_map = new HashMap<>();

        prj_code = pvo.getBase_infra_code() + pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq();
        prj_seq = pvo.getPrj_seq();
        request_map.put("reg_emp_id",emp_id);

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        make_project_item_view(list);

        return view;
    }

    void make_project_item_view (List<ProJectVO> list){
        if(null !=list){
            list_size = list;
            final int lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, lidth);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Map<String,Object> map = new HashMap<>();

            for(int i=0; i<list.size(); i++){
                request_map.put("item_"+list.get(i).getItem_seq() , "");
                item_check_map.put("item_"+list.get(i).getItem_seq() , list.get(i).getItem_type());
                if(list.get(i).getItem_type().equals("S")){
                    RadioGroup rg = new RadioGroup(mcontext);
                    String id = "item_" +list.get(i).getItem_seq();
                    rg.setTag(id);
                    map.put("item_"+list.get(i).getItem_seq() ,rg);
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

                        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams t_params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                        t_params.weight = 5f;

                        job_text.setText(list.get(i).getJob_text());
                        job_text.setLayoutParams(t_params);
                        job_text.setTextColor(mcontext.getResources().getColor(R.color.textBlack));
                        li.addView(job_text);

                        final Button work_btn = new Button(mcontext);
                        work_btn.setText("선택");
                        work_btn.setTag("item_"+list.get(i).getItem_seq());
                        work_btn.setPadding(3,3,3,3);

                        radio_map.put("select_button"+list.get(i).getItem_seq() , work_btn);

                        work_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String bus_id = (String)request_map.get("bus_id");
                                if(null == bus_id){
                                    Toast.makeText(mcontext,"조합 , 운수사 , 차량을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
                                }else{
                                    //                                ((Button) view).setText("선택완료");

                                    getAlbum(REQUEST_IMAGE_GALLERY);
                                    p_btn_tag = view.getTag().toString();
                                }
                            }
                        });

                        int bwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        int bheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams b_params = new LinearLayout.LayoutParams(bwidth, bheight);
                        //                    b_params.weight = 0.8f;
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
                                if(null == bus_id){
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


                        pic_btn.setLayoutParams(b_params);
                        pic_btn.setBackgroundResource(R.drawable.custom_btn);
                        li.addView(pic_btn);

                        bheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams prview_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bheight);
                        final ImageView imageView = new ImageView(mcontext);
                        imageView.setLayoutParams(prview_params);
                        imageView.setVisibility(View.GONE);
                        radio_map.put("pic_viewer"+list.get(i).getItem_seq() , imageView);

                        LinearLayout preview_layout = new LinearLayout(mcontext);
                        LinearLayout.LayoutParams prview_layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        preview_layout.setOrientation(LinearLayout.HORIZONTAL);
                        preview_layout.setLayoutParams(prview_layout_params);

                        LinearLayout.LayoutParams prview_btn_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        TextView preview_text = new TextView(mcontext);
                        preview_text.setText("미리 보기");
                        preview_text.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        preview_text.setLayoutParams(prview_btn_params);
                        preview_layout.addView(preview_text);

                        final ToggleButton priveiew_btn = new ToggleButton(mcontext);
                        priveiew_btn.setTextOff("▼");
                        priveiew_btn.setTextOn("▲");
                        priveiew_btn.setText("▼");
                        priveiew_btn.setBackground(null);
                        priveiew_btn.setLayoutParams(prview_btn_params);
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
                        preview_layout.addView(priveiew_btn);

                        main.addView(preview_layout);
                        main.addView(imageView);

                        break;
                    case "V" :
                        li.setLayoutParams(params);
                        li.setWeightSum(5);

                        final int vwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams v_params = new LinearLayout.LayoutParams(vwidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                        v_params.weight = 5f;

                        job_text.setText(list.get(i).getJob_text());
                        job_text.setLayoutParams(v_params);
                        li.addView(job_text);

                        final Button vwork_btn = new Button(mcontext);
                        vwork_btn.setText("btn_" + i+1);
                        vwork_btn.setText("선택");
                        vwork_btn.setTag("item_"+list.get(i).getItem_seq());

                        radio_map.put("select_button"+list.get(i).getItem_seq() , vwork_btn);

                        vwork_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String bus_id = (String)request_map.get("bus_id");
                                if(null == bus_id){
                                    Toast.makeText(mcontext,"조합 , 운수사 , 차량을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
                                }else{
                                    //                                ((Button) view).setText("선택완료");
                                    getAlbum(REQUEST_VIDEO_GALLERY);
                                    p_btn_tag = view.getTag().toString();
                                }
                            }
                        });

                        final int vbwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                        final int vbheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams vb_params = new LinearLayout.LayoutParams(vbwidth, vbheight);
                        //                    b_params.weight = 0.8f;

                        vwork_btn.setLayoutParams(vb_params);
                        vwork_btn.setBackgroundResource(R.drawable.custom_btn);
                        li.addView(vwork_btn);
                        break;
                    case "C" :
                        // 확인버튼 꼭 눌러야함
                        job_text.setText(list.get(i).getJob_text());
                        li.addView(job_text);

                        if(!list.get(i).getJob_text().contains("대폐차")){
                            view_layout_list.add(li);
                        }

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

                        if(!list.get(i).getJob_text().contains("대폐차")){
                            view_layout_list.add(li);
                        }

                        RadioGroup main_rg = (RadioGroup)map.get("item_" + list.get(i).getItem_seq());
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
                        RadioGroup sub_rg = (RadioGroup)map.get("item_" +inext);

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

    public void view_controler (boolean view_frg){
        if(view_layout_list.size() >0 ){

            if(view_frg){
                for(int i=0; i <view_layout_list.size(); i++){
                    view_layout_list.get(i).setVisibility(View.GONE);
                }
            }else{
                for(int i=0; i <view_layout_list.size(); i++){
                    view_layout_list.get(i).setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
