package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import app.erp.com.erp_app.vo.User_InfoVo;


/**
 * Created by hsra on 2019-06-24.
 */

public class Edu_Emp_GN_Adapter extends BaseAdapter {

    private ArrayList<Edu_Emp_Vo> listViewItem = new ArrayList<Edu_Emp_Vo>();

    public Edu_Emp_GN_Adapter() {
    }

    @Override
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.emp_list_gn_item,parent,false);
        }

        TextView testText = (TextView)convertView.findViewById(R.id.emp_gn);

        Edu_Emp_Vo user_infoVo = listViewItem.get(position);
        testText.setText(user_infoVo.getEmp_name());

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

    public void addItem (Edu_Emp_Vo list){
        Edu_Emp_Vo user_list = list;
        listViewItem.add(user_list);
    }
    // View lookup cache
    private static class ViewHolder {
        Button btn_1;
        Button btn_2;
//        Button btn_3;
        TextView error_office;
        TextView error_route;
        TextView error_bus_num;
        TextView error_unit;
        TextView error_high_code;
        TextView error_low_code;
        TextView error_office_group;
        TextView reg_date;
        RelativeLayout card_background;
    }

}
