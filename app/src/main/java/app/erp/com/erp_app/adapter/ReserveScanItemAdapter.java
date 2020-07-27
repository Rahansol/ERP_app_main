package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import app.erp.com.erp_app.vo.Reserve_ItemVO;

public class ReserveScanItemAdapter extends BaseAdapter {

    private ArrayList<Reserve_ItemVO> listviewitem = new ArrayList<Reserve_ItemVO>();
    private View.OnClickListener delete_btn_click;

    public ReserveScanItemAdapter(View.OnClickListener listener) {
        delete_btn_click = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.reserve_scan_item_layout,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.barcode_text = (TextView)convertView.findViewById(R.id.barcode_text);
            viewHolder.unit_name_text = (TextView)convertView.findViewById(R.id.unit_name_text);
            viewHolder.unit_area_text =(TextView)convertView.findViewById(R.id.unit_area_text);
            viewHolder.delete_btn = (Button)convertView.findViewById(R.id.delete_btn);
            viewHolder.total_layout = (LinearLayout)convertView.findViewById(R.id.total_layout);
            viewHolder.overlap_scan = (TextView)convertView.findViewById(R.id.overlap_scan);

            convertView.setTag(viewHolder);
        }else{viewHolder = (ViewHolder)convertView.getTag();}

        Reserve_ItemVO reserveItemVO = listviewitem.get(position);
        viewHolder.barcode_text.setText(reserveItemVO.getUnit_barcde());
        viewHolder.unit_name_text.setText(reserveItemVO.getUnit_name());
        viewHolder.unit_area_text.setText(reserveItemVO.getArea_name());
        viewHolder.delete_btn.setOnClickListener(delete_btn_click
//                new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("p",""+position);
//                listviewitem.remove(position);
//                notifyDataSetChanged();
//            }
//        }
        );
        if("Y".equals(reserveItemVO.getIndex_num())){
            viewHolder.total_layout.setBackgroundColor(context.getResources().getColor(R.color.contentDividerLine2));
            viewHolder.delete_btn.setVisibility(View.GONE);
            viewHolder.overlap_scan.setVisibility(View.VISIBLE);
        }else{
            viewHolder.delete_btn.setVisibility(View.VISIBLE);
            viewHolder.overlap_scan.setVisibility(View.GONE);
            viewHolder.total_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            viewHolder.delete_btn.setTag(position+","+reserveItemVO.getUnit_barcde());
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return listviewitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem (Reserve_ItemVO list){
        listviewitem.add(list);
    }

    public void removeItem (int index){
        listviewitem.remove(index);
    }

    private static class ViewHolder{
        LinearLayout total_layout;
        TextView barcode_text;
        TextView unit_name_text;
        TextView unit_area_text;
        TextView overlap_scan;
        Button delete_btn;
    }

    public ArrayList<Reserve_ItemVO> return_list(){
        return listviewitem;
    }

    public void remove_all(){
        listviewitem.clear();
    }
}
