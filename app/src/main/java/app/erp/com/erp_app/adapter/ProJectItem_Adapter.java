package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by hsra on 2019-06-24.
 * 사인 기능 삭제
 */

public class ProJectItem_Adapter extends BaseAdapter {

    private ArrayList<ProJectVO> listViewItem = new ArrayList<ProJectVO>();
    private View.OnClickListener prj_open_item;
//    private View.OnClickListener sign_input_click_listener;
//    private SharedPreferences sign_pref;

    public void setPrj_open_item(View.OnClickListener prj_open_item) {
        this.prj_open_item = prj_open_item;
    }

//    public void setSign_input_click_listener(View.OnClickListener sign_input_click_listener) {
//        this.sign_input_click_listener = sign_input_click_listener;
//    }

    @Override
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final ViewHolder viewHolder;
//        sign_pref = context.getSharedPreferences("sign_info" , MODE_PRIVATE);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.project_list_item_listview ,parent ,false);

            viewHolder = new ViewHolder();
            viewHolder.prj_reg_date = (TextView)convertView.findViewById(R.id.prj_reg_date);
            viewHolder.prj_emp_name = (TextView)convertView.findViewById(R.id.prj_emp_name);
            viewHolder.prj_bus_num = (TextView)convertView.findViewById(R.id.prj_bus_num);
            viewHolder.prj_busoff_name = (TextView)convertView.findViewById(R.id.prj_busoff_name);
            viewHolder.prj_item_serch_layout = (LinearLayout)convertView.findViewById(R.id.prj_item_serch_layout);
//            viewHolder.sign_input = (Button)convertView.findViewById(R.id.sign_input);
            convertView.setTag(viewHolder);
        }else{viewHolder = (ViewHolder)convertView.getTag();}
        ProJectVO proJectVO = listViewItem.get(position);

//        String prj_code = proJectVO.getBase_infra_code()+proJectVO.getArea_code()+proJectVO.getSub_area_code()+proJectVO.getPrj_seq();
//        String pref_string =prj_code+"_" + proJectVO.getReg_date()+"_" + proJectVO.getTransp_bizr_id()+"_" + proJectVO.getBus_id();
//        String btn_check = sign_pref.getString(pref_string,"N");

//        reg_dtti;
        viewHolder.prj_reg_date.setText(return_date(proJectVO.getReg_dtti()));
        viewHolder.prj_emp_name.setText(proJectVO.getEmp_name());
        viewHolder.prj_busoff_name.setText(proJectVO.getBusoff_name());
        viewHolder.prj_bus_num.setText(proJectVO.getBus_num());

        viewHolder.prj_item_serch_layout.setOnClickListener(prj_open_item);
        viewHolder.prj_item_serch_layout.setTag(position);

//        viewHolder.sign_input.setOnClickListener(sign_input_click_listener);
//        viewHolder.sign_input.setTag(position);

//        if("Y".equals(btn_check)){
//            viewHolder.sign_input.setVisibility(View.INVISIBLE);
//        }else{
//            viewHolder.sign_input.setVisibility(View.VISIBLE);
//        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItem.get(position);
    }

    public void addItem (ProJectVO list){
        ProJectVO data_list = list;
        listViewItem.add(data_list);
    }
    // View lookup cache
    private static class ViewHolder {
        LinearLayout prj_item_serch_layout;
        TextView prj_reg_date;
        TextView prj_emp_name;
        TextView prj_busoff_name;
        TextView prj_bus_num;
        Button sign_input;
    }

    public void clearItem (){
        listViewItem.clear();
    }

    public String return_date(String date){
        String rs = date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8) + "\n" + date.substring(8,10) +":" + date.substring(10,12);
        return rs;
    }

}
