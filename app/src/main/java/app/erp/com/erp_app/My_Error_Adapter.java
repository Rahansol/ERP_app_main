package app.erp.com.erp_app;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import app.erp.com.erp_app.vo.UnitList;

import static android.text.InputType.TYPE_CLASS_NUMBER;

/**
 * Created by hsra on 2019-06-24.
 */

public class My_Error_Adapter extends BaseAdapter {

    private ArrayList<Trouble_HistoryListVO> listViewItem = new ArrayList<Trouble_HistoryListVO>();
    private View.OnClickListener defaultRequestBtnClickListener;

    AdapterViewHolder viewHolder;
    public My_Error_Adapter() {
    }

    @Override
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int post = position;
        final Context context = parent.getContext();
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_error_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.btn_1 = convertView.findViewById(R.id.btn_1);
            viewHolder.error_office = convertView.findViewById(R.id.error_office);
            viewHolder.error_route = convertView.findViewById(R.id.error_route);
            viewHolder.error_bus_num = convertView.findViewById(R.id.error_bus_num);
            viewHolder.error_unit = convertView.findViewById(R.id.error_unit);
            viewHolder.error_high_code = convertView.findViewById(R.id.error_high_code);
            viewHolder.error_low_code = convertView.findViewById(R.id.error_low_code);
            viewHolder.error_office_group = convertView.findViewById(R.id.error_office_group);
            convertView.setTag(viewHolder);
        }else viewHolder = (ViewHolder)convertView.getTag();

        Trouble_HistoryListVO trouble_historylistvo = listViewItem.get(position);

        viewHolder.btn_1.setTag(position);
        viewHolder.btn_1.setOnClickListener(defaultRequestBtnClickListener);

        viewHolder.error_office.setText(trouble_historylistvo.getBusoff_name());
        if(trouble_historylistvo.getRoute_id() == null){
            viewHolder.error_route.setText("노선 : ");
        }else{
            viewHolder.error_route.setText("노선 : "+trouble_historylistvo.getRoute_id());
        }

        viewHolder.error_bus_num.setText(trouble_historylistvo.getBus_num());
        viewHolder.error_unit.setText(trouble_historylistvo.getUnit_name());
        viewHolder.error_high_code.setText(trouble_historylistvo.getTrouble_name());
        viewHolder.error_low_code.setText(trouble_historylistvo.getTrouble_low_name());
        viewHolder.error_office_group.setText(trouble_historylistvo.getOffice_group());

        return convertView;
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItem.get(position);
    }

    public void addItem (Trouble_HistoryListVO list){

        Trouble_HistoryListVO trouble_historylistvo = list;
//        trouble_historylistvo.setBusoff_name(list.getBusoff_name());
//        trouble_historylistvo.setRoute_id(list.getRoute_id());
//        trouble_historylistvo.setBus_id(list.getBus_num());
//        trouble_historylistvo.setUnit_name(list.getUnit_name());
//        trouble_historylistvo.setTrouble_name(list.getTrouble_name());
//        trouble_historylistvo.setTrouble_low_name(list.getTrouble_low_name());
//        trouble_historylistvo.setOffice_group(list.getOffice_group());

        listViewItem.add(trouble_historylistvo);
    }

    public void clearItem(){
        listViewItem.clear();
    }

    public void removeItemAdp(){
        int index_num = listViewItem.size();
        if(index_num == 0){
            index_num = 0;
        }else{
            index_num = listViewItem.size()-1;
        }
        if(listViewItem.size() == 0){
        }else{
            listViewItem.remove(index_num);
        }
    }

    // View lookup cache
    private static class ViewHolder {
        Button btn_1;
        TextView error_office;
        TextView error_route;
        TextView error_bus_num;
        TextView error_unit;
        TextView error_high_code;
        TextView error_low_code;
        TextView error_office_group;
    }

    public Trouble_HistoryListVO  resultItem(int postion){
        return listViewItem.get(postion);
    }

}
