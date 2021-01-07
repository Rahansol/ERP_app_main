package app.erp.com.erp_app.document_care;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.vo.Unit_InstallVO;

public class Fragment_Project_Doc_Write_2 extends Fragment {

    public static Fragment_Project_Doc_Write_2 shareMyString(List<Map<String, Object>> value , List<Unit_InstallVO> i_list , ProJectVO vo) {
        Bundle args = new Bundle();
        args.putSerializable("list", (Serializable) value);
        args.putSerializable("item_list", (Serializable) i_list);
        args.putSerializable("pvo",vo);
        Fragment_Project_Doc_Write_2 f = new Fragment_Project_Doc_Write_2();
        f.setArguments(args);
        return f;
    }
    private View view;
    private Context mcontext;
    private List<Map<String, Object>> main_list;
    private List<Unit_InstallVO> item_all_list;
    private ScrollView install_unit_scrollview;
    private LinearLayout main_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project_doc_write_2, container ,false);
        mcontext = getActivity();
        Bundle bundle = getArguments();

        // 어뎁터에서 넘어온 값
        // 프로젝트
        ProJectVO pvo = (ProJectVO) bundle.getSerializable("pvo");
        // 실제 데이터 리스트
        main_list = (List<Map<String, Object>>) bundle.getSerializable("list");
        // 전체 아이템 리스트
        item_all_list = (List<Unit_InstallVO>) bundle.getSerializable("item_list");

        install_unit_scrollview = (ScrollView)view.findViewById(R.id.install_unit_scrollview);
        main_layout = new LinearLayout(mcontext);
        LinearLayout.LayoutParams b_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        main_layout.setOrientation(LinearLayout.VERTICAL);
        main_layout.setLayoutParams(b_params);

        make_body_layout();

        return view;
    }

    public void make_body_layout(){
        for(int i=0; i<main_list.size(); i++){
            Map<String,Object> map = main_list.get(i);
            Set<?> set = map.keySet();
            Iterator<?> it = set.iterator();

            int unit_count = 0;

            while(it.hasNext()){
                String key = (String)it.next();
                String unit_value = ""+map.get(key);
                unit_count += Integer.parseInt(unit_value);
                if(!unit_value.equals("0")){

                    LinearLayout.LayoutParams b_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout cover_layout = new LinearLayout(mcontext);
                    cover_layout.setLayoutParams(b_params);
                    cover_layout.setOrientation(LinearLayout.HORIZONTAL);
                    cover_layout.setBackground(mcontext.getResources().getDrawable(R.drawable.table_bottom_border));

                    //  껍데기 레이아웃 end

                    // 텍스트 레이아웃 start

                    TextView item_text = new TextView(mcontext);
                    int lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                    b_params = new LinearLayout.LayoutParams( lidth,LinearLayout.LayoutParams.WRAP_CONTENT);
                    b_params.weight = 1f;
                    item_text.setLayoutParams(b_params);
                    item_text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    item_text.setTextSize(11);
                    item_text.setTextColor(mcontext.getResources().getColor(R.color.textBlack));
                    item_text.setPadding(15,0,0,0);
                    String replace_key = key.replaceAll("item_","");
                    String item_name_text = "";
                    for(int j=0; j<item_all_list.size(); j++){
                        if(replace_key.equals(item_all_list.get(j).getItem_seq())){
                            item_name_text = item_all_list.get(j).getItem_name();
                        }
                    }
                    item_text.setText(item_name_text);

                    LinearLayout line = new LinearLayout(mcontext);
                    lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                    b_params = new LinearLayout.LayoutParams( lidth,LinearLayout.LayoutParams.MATCH_PARENT);
                    line.setLayoutParams(b_params);
                    line.setBackgroundColor(mcontext.getResources().getColor(R.color.textBlack));

                    TextView count_text = new TextView(mcontext);
                    lidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                    b_params = new LinearLayout.LayoutParams( lidth,LinearLayout.LayoutParams.WRAP_CONTENT);
                    count_text.setLayoutParams(b_params);
                    count_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    count_text.setText(""+map.get(key));
                    count_text.setTextSize(11);
                    count_text.setTextColor(mcontext.getResources().getColor(R.color.textBlack));

//                    cover_layout.addView(line);
                    cover_layout.addView(item_text);
                    cover_layout.addView(line);
                    cover_layout.addView(count_text);
//                    cover_layout.addView(line);
                    main_layout.addView(cover_layout);
                }
            }

            if(unit_count == 0){
                LinearLayout.LayoutParams e_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView empty_text = new TextView(mcontext);
                empty_text.setLayoutParams(e_params);
                empty_text.setText("등록된 장비가 없습니다.");
                main_layout.addView(empty_text);
            }
        }
        install_unit_scrollview.addView(main_layout);
    }
}
