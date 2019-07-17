package app.erp.com.erp_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.vo.Bus_infoItemVo;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Reserve_ItemVO;

/**
 * Created by hsra on 2019-06-24.
 */

public class Bus_InfoAdapter extends BaseAdapter {

    private ArrayList<Bus_infoItemVo> listViewItem = new ArrayList<Bus_infoItemVo>();

    public Bus_InfoAdapter() {
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
            convertView = inflater.inflate(R.layout.dialog_listview_layout, parent, false);
        }

        TextView bus_unit_name = (TextView)convertView.findViewById(R.id.bus_unit_name);
        TextView bus_work_man = (TextView)convertView.findViewById(R.id.bus_work_man);
        TextView bus_unit_barcode = (TextView)convertView.findViewById(R.id.bus_unit_barcode);
        TextView bus_work_date = (TextView)convertView.findViewById(R.id.bus_work_date);

        Bus_infoItemVo bus_infoItemVo = listViewItem.get(position);

        bus_unit_name.setText(bus_infoItemVo.getBus_unit_name());
        bus_work_man.setText(bus_infoItemVo.getBus_work_man());
        bus_unit_barcode.setText(bus_infoItemVo.getBus_unit_barcode());
        bus_work_date.setText(bus_infoItemVo.getBus_work_date());

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

    public void addItem (String bus_unit_name, String bus_work_man, String bus_unit_barcode, String bus_work_date){
        Log.d("dfdf","dfdfdf " + bus_unit_barcode);
        Bus_infoItemVo busInfoItemVo = new Bus_infoItemVo();

        String t_bus_work_date = bus_work_date.substring(0,4)+"/"+bus_work_date.substring(4,6)+"/"+bus_work_date.substring(6,8)+"\n"+bus_work_date.substring(8,10)+":"+bus_work_date.substring(10,12);
        busInfoItemVo.setBus_unit_barcode(bus_unit_barcode);
        busInfoItemVo.setBus_unit_name(bus_unit_name);
        busInfoItemVo.setBus_work_date(t_bus_work_date);
        busInfoItemVo.setBus_work_man(bus_work_man);

        listViewItem.add(busInfoItemVo);
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
