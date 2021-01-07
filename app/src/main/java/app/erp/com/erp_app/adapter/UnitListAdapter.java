package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.AdapterViewHolder;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.UnitList;

import static android.text.InputType.TYPE_CLASS_NUMBER;

/**
 * Created by hsra on 2019-06-24.
 */

public class UnitListAdapter extends BaseAdapter {

    private ArrayList<UnitList> listViewItem = new ArrayList<UnitList>();
    List<String>eb_barcode_list = new ArrayList<>();
    List<String> delete_list = new ArrayList<>();
    List<Boolean> checkBoxState = new ArrayList<>();

    AdapterViewHolder viewHolder;
    public UnitListAdapter() {
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
            convertView = inflater.inflate(R.layout.unit_list_layout, parent, false);

            viewHolder = new AdapterViewHolder();
            viewHolder.barcode_text_name = (TextView)convertView.findViewById(R.id.barcode_text_name);
            viewHolder.area_text_name = (TextView)convertView.findViewById(R.id.area_text_name) ;
//            viewHolder.delete_check = (CheckBox)convertView.findViewById(R.id.delete_check);
            viewHolder.index_text_name = (TextView)convertView.findViewById(R.id.index_text_name);
            viewHolder.text_input = (TextView)convertView.findViewById(R.id.text_input);
            viewHolder.unit_text_name = (TextView)convertView.findViewById(R.id.unit_text_name) ;
            convertView.setTag(viewHolder);
        }else viewHolder = (AdapterViewHolder) convertView.getTag();


//        TextView area_text_name   = (TextView)convertView.findViewById(R.id.area_text_name) ;
//        TextView index_text_name = (TextView)convertView.findViewById(R.id.index_text_name);
//        final TextView unit_text_name   = (TextView)convertView.findViewById(R.id.unit_text_name) ;
//        TextView barcde_text_name = (TextView)convertView.findViewById(R.id.barcode_text_name);
//        final TextView text_input = (TextView)convertView.findViewById(R.id.text_input);
        LinearLayout text_input_layout = (LinearLayout)convertView.findViewById(R.id.text_input_layout);
//        final CheckBox delete_check = (CheckBox)convertView.findViewById(R.id.delete_check);

        UnitList unitList = listViewItem.get(position);

        viewHolder.area_text_name.setText(unitList.getArea_name());
        viewHolder.unit_text_name.setText(unitList.getUnit_name());
        viewHolder.barcode_text_name.setText(unitList.getArea_code());
        viewHolder.index_text_name.setText(unitList.getIndex_num());
        viewHolder.text_input.setText(unitList.getEb_barcode());

        text_input_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_text = viewHolder.text_input.getText().toString();
                String case_input_text = "";
                if(t_text.equals("입력시 터치")){
                    case_input_text = "";
                }else if(t_text.equals("")){
                    case_input_text = "";
                }else{
                    case_input_text = listViewItem.get(position).getEb_barcode();
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(listViewItem.get(position).getUnit_name()+" EB 시리얼");
                alert.setCancelable(false);
                final int m_nMaxLengthOfDeviceName = 9;
                final EditText input = new EditText(context);
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(m_nMaxLengthOfDeviceName) });
                input.setInputType(TYPE_CLASS_NUMBER);
                input.setText(case_input_text);
                alert.setView(input);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        UnitList unitList = listViewItem.get(position);
                        if(input.getText().toString().equals("")){
                            viewHolder.text_input.setText("입력시 터치");
                            unitList.setEb_barcode(input.getText().toString());
                        }else{
                            viewHolder.text_input.setText(input.getText().toString());
                            unitList.setEb_barcode(input.getText().toString());
                        }
                        listViewItem.set(position,unitList);
                        InputMethodManager immhide = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                });
                alert.show();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

//        viewHolder.delete_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if( ((CheckBox)v).isChecked() ){
//                    delete_list.add(listViewItem.get(position).getArea_code());
//                    viewHolder.delete_check.setChecked(true);
//                    checkBoxState.add(true);
//                }else{
//                    viewHolder.delete_check.setChecked(false);
//                    for(Iterator<String> it = delete_list.iterator() ; it.hasNext() ; )
//                    {
//                        String value = it.next();
//
//                        if(value.equals(listViewItem.get(position).getArea_code()))
//                        {
//                            it.remove();
//                        }
//                    }
//                }
//                for(int i = 0 ; i < delete_list.size(); i ++){
//                    Log.d("objcet :" , "vlues :" + delete_list.get(i));
//                }
//            }
//
//        });
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

    public void addItem (String area_text_name , String unit_text_name , String barcode_text_name,String index_num){
        UnitList unitList = new UnitList();
        unitList.setArea_name(area_text_name);
        unitList.setUnit_name(unit_text_name);
        unitList.setArea_code(barcode_text_name);
        unitList.setIndex_num(""+(listViewItem.size()+1));

        listViewItem.add(unitList);
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

    public List<UnitList> resultItem(){
        return listViewItem;
    }

}
