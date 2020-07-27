package app.erp.com.erp_app.document_care;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;

import static android.app.Activity.RESULT_OK;

public class Fragment_Project_Doc_Write_3 extends Fragment {

    public static Fragment_Project_Doc_Write_3 shareMyString(ArrayList<ProJectVO> value, ProJectVO vo, Map<String,Object> req_map) {
        Bundle args = new Bundle();
        args.putSerializable("list",value);
        args.putSerializable("pvo",vo);
        args.putSerializable("req_map", (Serializable) req_map);
        Fragment_Project_Doc_Write_3 f = new Fragment_Project_Doc_Write_3();
        f.setArguments(args);
        return f;
    }
    private View view;
    private Context mcontext;
    private String prj_code,save_path,pref_string;
    private Map<String,Object> req_map;
    private EditText sign_tel , sign_man;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project_doc_write_3, container ,false);
        mcontext = getActivity();
        Bundle bundle = getArguments();

        req_map= new HashMap<>();
        req_map.put("real_path","N");
        // 어뎁터에서 넘어온 값
        ProJectVO pvo = (ProJectVO) bundle.getSerializable("pvo");
        ArrayList<ProJectVO> list = (ArrayList<ProJectVO>) bundle.getSerializable("list");
        // req 맵
        Map<String,Object> request_map = (Map<String, Object>) bundle.getSerializable("req_map");

        EditText brand_phone = (EditText) view.findViewById(R.id.sign_tel);
        brand_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

//        ProJectVO select_item = (ProJectVO) pjia.getItem((Integer) view.getTag());
        String bus_id = pvo.getBus_id();
        String reg_date = pvo.getReg_date();
        String trans_id = pvo.getTransp_bizr_id();

        String view_type = (String)request_map.get("view_type");

        prj_code = pvo.getBase_infra_code() + pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq();
        save_path     = "nas_image/image/IERP/"+prj_code+"/"+reg_date+"/"+prj_code+"_"+reg_date+"_"+trans_id+"_"+bus_id+"_"+"sign"+".jpg";
        pref_string = prj_code+"_"+reg_date+"_"+trans_id+"_"+bus_id;

        sign_tel = (EditText)view.findViewById(R.id.sign_tel);
        sign_man = (EditText)view.findViewById(R.id.sign_man);

        String sub_area_code = pvo.getSub_area_code();

        TextView confirm_check_text1 = (TextView)view.findViewById(R.id.confirm_check_text1);
        confirm_check_text1.setText("1. 상기내용과 같이 차량별 단말기 설치 및 점검결과 이상 없음을 확인 합니다.");
        TextView confirm_check_text2 = (TextView)view.findViewById(R.id.confirm_check_text2);
        TextView confirm_check_text3 = (TextView)view.findViewById(R.id.confirm_check_text3);
        confirm_check_text3.setText("3. 상기 물품을 분실하거나 파손시 변상하겠슴.");
        if(sub_area_code.equals("01")){
            confirm_check_text2.setText("2. 단말기 점검내용 : 승차,하차 단말기 카드처리 및 무선 송수신 , 운전자 단말기 BMS 및 기본기능");
        }else{
            confirm_check_text2.setText("2. 단말기 점검내용 : 승차,하차 단말기 카드처리 및 무선 송수신 , 운전자 단말기 등 기본기능");
        }


        Button sign_input_btn = (Button)view.findViewById(R.id.sign_input_btn);
        sign_input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mcontext , CameraTestActivity.class);
                startActivityForResult(i,112);
            }
        });

        ImageView sign_img = (ImageView)view.findViewById(R.id.sign_img);
        if(view_type.equals("v")){
            String sign_name = (String)request_map.get("sign_name");
            sign_man.setText(sign_name);
            String sign_tel_num = (String)request_map.get("sign_tel_num");
            sign_tel.setText(sign_tel_num);

            sign_input_btn.setVisibility(View.GONE);
//            sign_img.setVisibility(View.VISIBLE);
            String image_url = "http://ierp.interpass.co.kr/"+(String)request_map.get("sign_img_path");
            Glide.with(mcontext).load(image_url)
                    .listener(requestListener)
                    .into(sign_img);
        }else{
            sign_man.setText("");
            sign_tel.setText("");
            sign_input_btn.setVisibility(View.VISIBLE);
            sign_img.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 112){
            if(resultCode == RESULT_OK){
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String get_today = sdf.format(date);
                String path = data.getStringExtra("path");
                req_map.put("real_path",path);
                req_map.put("sign_dtti",get_today);
                save_path = save_path.replaceAll("/","%");
//                new Fragment_ProJect_Work_List.ImageUploadJson().execute();
            }
        }
    }

    public Map<String,Object> req_return(){
        req_map.put("sign_man",sign_man.getText().toString());
        req_map.put("sign_tel",sign_tel.getText().toString());
        return req_map;
    }

    private RequestListener requestListener = new RequestListener() {
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Log.d("img e ", ""+e);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };
}
