package app.erp.com.erp_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.vo.UnitList;

/**
 * Created by hsra on 2019-06-24.
 */

public class UnitListAdapter_b extends BaseAdapter {

    private ArrayList<UnitList> listViewItem = new ArrayList<UnitList>();

    public UnitListAdapter_b() {
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
            convertView = inflater.inflate(R.layout.unit_list_layout_b, parent, false);
        }

        TextView index_text_name  = (TextView)convertView.findViewById(R.id.index_text_name);
        TextView area_text_name   = (TextView)convertView.findViewById(R.id.area_text_name) ;
        TextView unit_text_name   = (TextView)convertView.findViewById(R.id.unit_text_name) ;
        TextView barcde_text_name = (TextView)convertView.findViewById(R.id.barcode_text_name);

//        Button submit_btn = (Button)convertView.findViewById(R.id.submit_btn);
//        submit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment_a fa = new Fragment_a();
//                fa.removeItem(position);
//            }
//        });
        UnitList unitList = listViewItem.get(position);

//        submit_btn.setTag("btn");
        index_text_name.setText(unitList.getIndex_num());
        area_text_name.setText(unitList.getArea_name());
        unit_text_name.setText(unitList.getUnit_name());
        barcde_text_name.setText(unitList.getArea_code());

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

    public void addItem (String area_text_name , String unit_text_name , String barcode_text_name, String index_num){
        UnitList unitList = new UnitList();
        unitList.setArea_name(area_text_name);
        unitList.setUnit_name(unit_text_name);
        unitList.setArea_code(barcode_text_name);
        unitList.setIndex_num(index_num);

        listViewItem.add(unitList);
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
