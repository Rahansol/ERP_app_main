package app.erp.com.erp_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.vo.Reserve_ItemVO;
import app.erp.com.erp_app.vo.UnitList;

/**
 * Created by hsra on 2019-06-24.
 */

public class ReserveItemAdapter extends BaseAdapter {

    private ArrayList<Reserve_ItemVO> listViewItem = new ArrayList<Reserve_ItemVO>();

    public ReserveItemAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int post = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.reserve_list_layout, parent, false);
        }

        TextView index_text_name = (TextView)convertView.findViewById(R.id.index_text_name);
        TextView area_text_name = (TextView)convertView.findViewById(R.id.area_text_name);
        TextView unit_text_name = (TextView)convertView.findViewById(R.id.unit_text_name);
        TextView barcode_text_name = (TextView)convertView.findViewById(R.id.barcode_text_name);
        EditText notice = (EditText)convertView.findViewById(R.id.notice);

        AutoCompleteTextView bus_num_text = (AutoCompleteTextView)convertView.findViewById(R.id.bus_num_text);
        Spinner trouble_high_text = (Spinner)convertView.findViewById(R.id.trouble_high_text);
        Spinner trouble_low_text = (Spinner)convertView.findViewById(R.id.trouble_low_text);

        Reserve_ItemVO reserveItemVO = listViewItem.get(position);

        index_text_name.setText(reserveItemVO.getIndex_num());
        area_text_name.setText(reserveItemVO.getArea_name());
        unit_text_name.setText(reserveItemVO.getUnit_barcde());
        barcode_text_name.setText(reserveItemVO.getUnit_barcde());
        notice.setText(reserveItemVO.getNotice());

        ArrayAdapter spinnerAdapter, spinnerAdapter1, autotextAdapter;
        spinnerAdapter = new ArrayAdapter(context , R.layout.spinner_item,reserveItemVO.getTrouble_high_code());
        trouble_high_text.setAdapter(spinnerAdapter);

        spinnerAdapter1 = new ArrayAdapter(context , R.layout.spinner_item,reserveItemVO.getTrouble_low_code());
        trouble_low_text.setAdapter(spinnerAdapter1);

        autotextAdapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, reserveItemVO.getBus_num());
        bus_num_text.setAdapter(autotextAdapter);

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

    public void addItem (String index_text_name , String area_text_name , String unit_text_name, String barcode_text_name,  String notice ,
                           ArrayList<String> trouble_high_code, ArrayList<String> trouble_low_code, ArrayList<String> bus_num_text){

        Reserve_ItemVO reserveItemVO = new Reserve_ItemVO();
        reserveItemVO.setIndex_num(index_text_name);
        reserveItemVO.setArea_name(area_text_name);
        reserveItemVO.setUnit_name(unit_text_name);
        reserveItemVO.setUnit_barcde(barcode_text_name);
        reserveItemVO.setBus_num(bus_num_text);
        reserveItemVO.setNotice(notice);
        reserveItemVO.setTrouble_high_code(trouble_high_code);
        reserveItemVO.setTrouble_low_code(trouble_low_code);

        listViewItem.add(reserveItemVO);
    }

    public void clearItem(){
        Log.d(":",":"+listViewItem.size());
        listViewItem.clear();
    }

    public void removeItemAdp(int pos){
        Log.d("tt",":"+listViewItem.size());
        listViewItem.remove(pos);
    }
}
