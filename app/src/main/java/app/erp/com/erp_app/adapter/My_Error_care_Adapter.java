package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.AdapterViewHolder;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;

/**
 * Created by hsra on 2019-06-24.
 */

public class My_Error_care_Adapter extends BaseAdapter {

    private ArrayList<Trouble_HistoryListVO> listViewItem = new ArrayList<Trouble_HistoryListVO>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private View.OnClickListener error_equal_bus_btn;
    private View.OnClickListener error_call_driver_tel;



    AdapterViewHolder viewHolder;
    public My_Error_care_Adapter() {
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
            convertView = inflater.inflate(R.layout.my_error_care_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.btn_1 = convertView.findViewById(R.id.btn_4);
            viewHolder.btn_5 = convertView.findViewById(R.id.btn_5);

            viewHolder.error_office = convertView.findViewById(R.id.error_office);
            viewHolder.error_route = convertView.findViewById(R.id.error_route);
            viewHolder.error_bus_num = convertView.findViewById(R.id.error_bus_num);
            viewHolder.error_unit = convertView.findViewById(R.id.error_unit);
            viewHolder.error_high_code = convertView.findViewById(R.id.error_high_code);
            viewHolder.error_low_code = convertView.findViewById(R.id.error_low_code);
            viewHolder.error_office_group = convertView.findViewById(R.id.error_office_group);
            viewHolder.error_reg_date = convertView.findViewById(R.id.error_reg_date);
            convertView.setTag(viewHolder);
        }else viewHolder = (ViewHolder)convertView.getTag();

        Trouble_HistoryListVO trouble_historylistvo = listViewItem.get(position);

        viewHolder.btn_1.setTag(position);
        viewHolder.btn_1.setOnClickListener(defaultRequestBtnClickListener);

        viewHolder.btn_5.setTag(position);
        viewHolder.btn_5.setOnClickListener(error_equal_bus_btn);

        viewHolder.error_office.setText(trouble_historylistvo.getBusoff_name());
        if(trouble_historylistvo.getRoute_id() == null){
            viewHolder.error_route.setText("노선 : ");
        }else{
            viewHolder.error_route.setText("노선 : "+trouble_historylistvo.getRoute_id());
        }
        String error_high_code_unit_code = trouble_historylistvo.getUnit_name() +" " + trouble_historylistvo.getTrouble_name() ;
        viewHolder.error_bus_num.setText(trouble_historylistvo.getBus_num());
        viewHolder.error_unit.setText(error_high_code_unit_code);
        viewHolder.error_high_code.setText(trouble_historylistvo.getTrouble_low_name());
//        viewHolder.error_low_code.setText(trouble_historylistvo.getTrouble_low_name());
        viewHolder.error_office_group.setText(trouble_historylistvo.getOffice_group());
        viewHolder.error_reg_date.setText(substr_date(trouble_historylistvo.getReg_date()) + " "  + substr_time(trouble_historylistvo.getReg_time()));

        if(trouble_historylistvo.getDriver_tel_num() == null){
            viewHolder.error_low_code.setText("☎ : 미등록" );
        }else{
            viewHolder.error_low_code.setText("☎ : " + trouble_historylistvo.getDriver_tel_num());
            viewHolder.error_low_code.setTextColor(context.getResources().getColor(R.color.app_blue));
            viewHolder.error_low_code.setTag(trouble_historylistvo.getDriver_tel_num());
            viewHolder.error_low_code.setOnClickListener(error_call_driver_tel);
        }

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

    public View.OnClickListener getError_equal_bus_btn() {
        return error_equal_bus_btn;
    }

    public void setError_equal_bus_btn(View.OnClickListener error_equal_bus_btn) {
        this.error_equal_bus_btn = error_equal_bus_btn;
    }

    public View.OnClickListener getError_call_driver_tel() {
        return error_call_driver_tel;
    }

    public void setError_call_driver_tel(View.OnClickListener error_call_driver_tel) {
        this.error_call_driver_tel = error_call_driver_tel;
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
        Button btn_5;
        TextView error_office;
        TextView error_route;
        TextView error_bus_num;
        TextView error_unit;
        TextView error_high_code;
        TextView error_low_code;
        TextView error_office_group;
        TextView error_reg_date;
    }

    public Trouble_HistoryListVO  resultItem(int postion){
        return listViewItem.get(postion);
    }

    public String substr_time( String time){
        if(time != null){
            String result_time = time.substring(0,2) +":"+ time.substring(2,4);
            return result_time;
        }
        return "시간 미입력";
    }

    public String substr_date (String date ){
        if ( date != null){
            String result_date = date.substring(5,date.length());
            return result_date;
        }
        return "날짜 미입력";
    }

}
