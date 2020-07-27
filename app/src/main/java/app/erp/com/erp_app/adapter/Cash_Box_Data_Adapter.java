package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Cash_Work_VO;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;


/**
 * Created by hsra on 2019-06-24.
 */

public class Cash_Box_Data_Adapter extends BaseAdapter {

    private ArrayList<Cash_Work_VO> listViewItem = new ArrayList<Cash_Work_VO>();

    public Cash_Box_Data_Adapter() {
    }

    @Override
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cash_box_list_layout , parent,false);

            viewHolder = new ViewHolder();
            viewHolder.reg_date = (TextView) convertView.findViewById(R.id.reg_date);
            viewHolder.office = (TextView) convertView.findViewById(R.id.office);
            viewHolder.bus_num = (TextView) convertView.findViewById(R.id.bus_num);
            viewHolder.my_work = (TextView) convertView.findViewById(R.id.my_work);

            convertView.setTag(viewHolder);
        }else{viewHolder = (ViewHolder)convertView.getTag();}

        Cash_Work_VO user_infoVo = listViewItem.get(position);

        TextView reg_date = (TextView)convertView.findViewById(R.id.reg_date);
        TextView office = (TextView)convertView.findViewById(R.id.office);
        TextView bus_num = (TextView)convertView.findViewById(R.id.bus_num);
        TextView my_work = (TextView)convertView.findViewById(R.id.my_work);

        reg_date.setText(user_infoVo.getReg_date());
        office.setText(user_infoVo.getBusoff_name());
        bus_num.setText(user_infoVo.getBus_num());
        my_work.setText(user_infoVo.getMy_work());

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

    public void addItem (Cash_Work_VO list){
        Cash_Work_VO data_list = list;
        listViewItem.add(data_list);
    }
    // View lookup cache
    private static class ViewHolder {
        TextView reg_date;
        TextView office;
        TextView bus_num;
        TextView my_work;
    }

    public void clearItem (){
        listViewItem.clear();
    }

}
