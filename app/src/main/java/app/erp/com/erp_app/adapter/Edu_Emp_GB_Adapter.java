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
import app.erp.com.erp_app.vo.Edu_Emp_Vo;


/**
 * Created by hsra on 2019-06-24.
 */

public class Edu_Emp_GB_Adapter extends BaseAdapter {

    private ArrayList<Edu_Emp_Vo> listViewItem = new ArrayList<Edu_Emp_Vo>();

    public Edu_Emp_GB_Adapter() {
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
            convertView = inflater.inflate(R.layout.emp_list_gb_item , parent,false);

            viewHolder = new ViewHolder();
            viewHolder.emp_check = (CheckBox)convertView.findViewById(R.id.emp_check_box);

            convertView.setTag(viewHolder);
        }else{viewHolder = (ViewHolder)convertView.getTag();}

        Edu_Emp_Vo user_infoVo = listViewItem.get(position);

        TextView testText = (TextView)convertView.findViewById(R.id.emp_1);
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
        TextView emp_name;
        CheckBox emp_check;
    }

}
