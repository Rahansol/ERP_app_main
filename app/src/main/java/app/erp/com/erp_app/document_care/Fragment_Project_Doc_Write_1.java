package app.erp.com.erp_app.document_care;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
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
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Image_Path_VO;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.vo.Unit_InstallVO;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static app.erp.com.erp_app.document_care.CameraTestActivity.REQUEST_IMAGE_CAPTURE;

public class Fragment_Project_Doc_Write_1 extends Fragment {

    public static Fragment_Project_Doc_Write_1 shareMyString(List<Map<String, Object>> value, ProJectVO vo , List<Unit_InstallVO> i_list , Map<String,Object> req_map) {
        Bundle args = new Bundle();
        args.putSerializable("list", (Serializable) value);
        args.putSerializable("pvo",vo);
        args.putSerializable("item_list", (Serializable) i_list);
        args.putSerializable("req_map", (Serializable) req_map);
        Fragment_Project_Doc_Write_1 f = new Fragment_Project_Doc_Write_1();
        f.setArguments(args);
        return f;
    }
    private View view;
    private Context mcontext;
    private HorizontalScrollView h_scrollview;
    private ERP_Spring_Controller erp;
    private List<Map<String, Object>> main_list;
    private List<Unit_InstallVO> item_all_list;
    private LinearLayout out_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project_doc_write_1, container ,false);
        mcontext = getActivity();
        Bundle bundle = getArguments();

        // 어뎁터에서 넘어온 값
        // 프로젝트
        ProJectVO pvo = (ProJectVO) bundle.getSerializable("pvo");
        // 전체 아이템 리스트
        item_all_list = (List<Unit_InstallVO>) bundle.getSerializable("item_list");
        // 실제 데이터 리스트
        main_list = (List<Map<String, Object>>) bundle.getSerializable("list");
        // req 맵
        Map<String,Object> request_map = (Map<String, Object>) bundle.getSerializable("req_map");

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        // 호라이즌 스크롤 뷰
        h_scrollview = (HorizontalScrollView)view.findViewById(R.id.h_scrollview);

        List<String> head_list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < main_list.size(); i++){
            Map<String,Object> map = main_list.get(i);
            Set<?> set = map.keySet();
            Iterator<?> it = set.iterator();
            while(it.hasNext()){
                String key = (String)it.next();
                head_list.add(key);
                    sb.append("------------------------------------------------------------\n");
                    sb.append("key = "+key+",\t\t\tvalue = "+map.get(key)+"\n");
            }
        }

        HashSet<String> arr = new HashSet<String>(head_list);
        ArrayList<String> h_list = new ArrayList<String>(arr);

        Call<List<Unit_InstallVO>> call = erp.prj_header_list(request_map);
        new Fragment_Project_Doc_Write_1.prj_header_list().execute(call);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        out_layout = new LinearLayout(mcontext);
        out_layout.setOrientation(LinearLayout.VERTICAL);
        out_layout.setLayoutParams(params);

        return view;
    }

    private class prj_header_list extends AsyncTask<Call , Void , List<Unit_InstallVO>>{
        @Override
        protected List<Unit_InstallVO> doInBackground(Call... calls) {
            try{
                Call<List<Unit_InstallVO>> call = calls[0];
                Response<List<Unit_InstallVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Unit_InstallVO> unit_installVOS) {
            super.onPostExecute(unit_installVOS);
            if(unit_installVOS != null){
                make_table_header(unit_installVOS);
            }
        }
    }

    public void make_table_header (List<Unit_InstallVO> item_list){

        LinearLayout head_layout = new LinearLayout(mcontext);
        LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        head_layout.setOrientation(LinearLayout.HORIZONTAL);
        head_layout.setLayoutParams(main_params);

        for(int j=0; j<item_list.size(); j++){

            int lidth;
//            if(item_list.get(j).getItem_value().equals("NO")){
            if("NO".equals(item_list.get(j).getItem_value())){
                lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            }else{
                lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
            }
            LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams( lidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            text_params.gravity = Gravity.CENTER;

            TextView head = new TextView(mcontext);
            head.setLayoutParams(text_params);
            head.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            int text_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
            head.setTextSize(11);
            head.setPadding(5,5,5,5);
            head.setTextColor(mcontext.getResources().getColor(R.color.textBlack));

            head.setText(item_list.get(j).getItem_value());
            head_layout.addView(head);

            LinearLayout border_line = new LinearLayout(mcontext);
            int border_line_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams border_params = new LinearLayout.LayoutParams( border_line_dp, LinearLayout.LayoutParams.MATCH_PARENT);
            border_line.setLayoutParams(border_params);
            border_line.setBackgroundColor(mcontext.getResources().getColor(R.color.textBlack));
            head_layout.addView(border_line);
        }
//        head_layout.setBackground(mcontext.getResources().getDrawable(R.drawable.table_border));
        out_layout.addView(head_layout);

        make_table_body(item_list);
    }

    public void make_table_body(List<Unit_InstallVO> item_list){
        StringBuilder sb = new StringBuilder();

        for(int i=0; i < main_list.size(); i++){
            LinearLayout body_layout = new LinearLayout(mcontext);
            LinearLayout.LayoutParams b_params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            body_layout.setLayoutParams(b_params);
            body_layout.setOrientation(LinearLayout.HORIZONTAL);

            Map<String,Object> map3 = main_list.get(i);

            body_layout.addView(return_textview(""+ map3.get("rn"),40) );
            body_layout.addView(return_line());

            body_layout.addView(return_textview(""+ map3.get("route_num"),90));
            body_layout.addView(return_line());
            body_layout.addView(return_textview(""+ map3.get("bus_num"),90));
            body_layout.addView(return_line());

                for(int j=3; j < item_list.size(); j++){
                    Map<String,Object> map = main_list.get(i);
                    Set<?> set = map.keySet();
                    Iterator<?> it = set.iterator();

                    boolean write_chekc = false;
                    String write_text = "";

                    while(it.hasNext()){
                        String key = (String)it.next();
                        String replace_key = key.replaceAll("item_","");
                        String eq_item_name = "";

                        for(int a=0; a <item_all_list.size(); a++){
                            if(replace_key.equals(item_all_list.get(a).getItem_seq())){
                                eq_item_name = item_all_list.get(a).getItem_name().replaceAll(" " , "");
                            }
                        }

                        if(eq_item_name.equals("BTL_AMP")) eq_item_name = "BTL엠프";
                        if(eq_item_name.equals("일반AMP")) eq_item_name = "일반엠프";
                        if(item_list.get(j).getItem_value().replaceAll(" " , "").equals(eq_item_name)){
                            write_chekc = true;
                            write_text = ""+map.get(key);
                        }
                    }
                    int lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams( lidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                    text_params.gravity = Gravity.CENTER;
                    if(write_chekc){
                        LinearLayout line = new LinearLayout(mcontext);
                        int line_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams( line_width,LinearLayout.LayoutParams.MATCH_PARENT);
                        line.setLayoutParams(line_params);
                        line.setBackgroundColor(mcontext.getResources().getColor(R.color.textBlack));

                        TextView value_text = new TextView(mcontext);
                        value_text.setLayoutParams(text_params);
                        value_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        int text_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
                        value_text.setTextSize(11);
                        value_text.setPadding(5,5,5,5);
                        value_text.setTextColor(mcontext.getResources().getColor(R.color.textBlack));

                        value_text.setText(write_text);

                        body_layout.addView(value_text);
                        body_layout.addView(line);
                    }else{
                        LinearLayout line = new LinearLayout(mcontext);
                        int line_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams( line_width,LinearLayout.LayoutParams.MATCH_PARENT);
                        line.setLayoutParams(line_params);
                        line.setBackgroundColor(mcontext.getResources().getColor(R.color.textBlack));

                        TextView value_text = new TextView(mcontext);
                        value_text.setLayoutParams(text_params);
                        value_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        int text_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
                        value_text.setTextSize(11);
                        value_text.setPadding(5,5,5,5);
                        value_text.setTextColor(mcontext.getResources().getColor(R.color.textBlack));

                        value_text.setText("");

                        body_layout.addView(value_text);
                        body_layout.addView(line);
                    }

                }
            out_layout.addView(body_layout);
            out_layout.addView(return_vertical_line());
        }
        out_layout.setBackground(mcontext.getResources().getDrawable(R.drawable.table_bottom_left_border));
        h_scrollview.addView(out_layout);
    }

    public TextView return_textview (String settext, int width){
        if(settext.equals("null")) settext="";

        int textwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams( textwidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER;

        TextView value_text2 = new TextView(mcontext);
        value_text2.setLayoutParams(params2);
        value_text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        value_text2.setTextSize(11);
        value_text2.setPadding(5,5,5,5);
        value_text2.setTextColor(mcontext.getResources().getColor(R.color.textBlack));
        value_text2.setText(settext);

        return value_text2;

    }

    public LinearLayout return_line(){
        LinearLayout line = new LinearLayout(mcontext);
        int line_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams( line_width,LinearLayout.LayoutParams.MATCH_PARENT);
        line.setLayoutParams(line_params);
        line.setBackgroundColor(mcontext.getResources().getColor(R.color.textBlack));

        return line;
    }
    public LinearLayout return_vertical_line(){
        LinearLayout line = new LinearLayout(mcontext);
        int line_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,line_width);
        line.setLayoutParams(line_params);
        line.setBackgroundColor(mcontext.getResources().getColor(R.color.textBlack));

        return line;
    }

}