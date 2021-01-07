package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Cash_Work_VO;
import app.erp.com.erp_app.vo.ProJectVO;


/**
 * Created by hsra on 2019-06-24.
 */

public class ProJectList_Adapter extends BaseAdapter {

    private ArrayList<ProJectVO> listViewItem = new ArrayList<ProJectVO>();
    /* myButtonInsert 등록 test */
    private View.OnClickListener mysetDetail_btn_listener;
    private View.OnClickListener detail_btn_listener;
    private View.OnClickListener work_insert_btn_listener;
    private View.OnClickListener project_work_serch_btn_listener;
    private View.OnClickListener prj_doc_insert_listener;
    private View.OnClickListener prj_doc_view_btn_listener;
    private String display_check;


    /* myButtonInsert 등록 test */
    public void mysetDetail_btn_listener(View.OnClickListener myDetail_btn_listener){
        this.mysetDetail_btn_listener= myDetail_btn_listener;
    }


    public void setDetail_btn_listener(View.OnClickListener detail_btn_listener) {
        this.detail_btn_listener = detail_btn_listener;
    }

    public void setWork_insert_btn_listener(View.OnClickListener work_insert_btn_listener) {
        this.work_insert_btn_listener = work_insert_btn_listener;
    }

    public void setProject_work_serch_btn_listener(View.OnClickListener project_work_serch_btn_listener) {
        this.project_work_serch_btn_listener = project_work_serch_btn_listener;
    }

    public ProJectList_Adapter(String dc ) {
        this.display_check = dc;
    }

    public void setPrj_doc_insert_listener(View.OnClickListener prj_doc_insert_listener) {
        this.prj_doc_insert_listener = prj_doc_insert_listener;
    }

    public void setPrj_doc_view_btn_listener(View.OnClickListener prj_doc_view_btn_listener) {
        this.prj_doc_view_btn_listener = prj_doc_view_btn_listener;
    }

    @Override
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.project_list_listview ,parent ,false);
            viewHolder = new ViewHolder();

            /*my 등록버튼*/
            viewHolder.myButtonInsert= (Button) convertView.findViewById(R.id.myButtonInsert);

            viewHolder.project_name = (TextView) convertView.findViewById(R.id.project_name);
            viewHolder.project_detail_btn = (Button)convertView.findViewById(R.id.project_detail_btn);
            viewHolder.project_work_insert_btn = (Button)convertView.findViewById(R.id.project_work_insert_btn);
            viewHolder.project_work_serch_btn = (Button)convertView.findViewById(R.id.project_work_serch_btn);
            viewHolder.prj_doc_insert_btn = (Button)convertView.findViewById(R.id.prj_doc_insert_btn);
            viewHolder.prj_doc_view_btn = (Button)convertView.findViewById(R.id.prj_doc_view_btn);

            viewHolder.s_v_i_btn_layout = (LinearLayout)convertView.findViewById(R.id.s_v_i_btn_layout);
            viewHolder.prj_doc_btn_layout = (RelativeLayout)convertView.findViewById(R.id.prj_doc_btn_layout);

            convertView.setTag(viewHolder);
        }else{viewHolder = (ViewHolder)convertView.getTag();}

        ProJectVO proJectVO = listViewItem.get(position);

        viewHolder.project_detail_btn.setTag(position);
        viewHolder.project_detail_btn.setOnClickListener(detail_btn_listener);

        viewHolder.prj_doc_view_btn.setTag(position);
        viewHolder.prj_doc_view_btn.setOnClickListener(prj_doc_view_btn_listener);

        if(display_check.equals("Y")){
            viewHolder.s_v_i_btn_layout.setVisibility(View.VISIBLE);
            viewHolder.prj_doc_btn_layout.setVisibility(View.GONE);
        }else{
            viewHolder.s_v_i_btn_layout.setVisibility(View.GONE);
            viewHolder.prj_doc_btn_layout.setVisibility(View.VISIBLE);
        }

        viewHolder.prj_doc_insert_btn.setTag(position);
        viewHolder.prj_doc_insert_btn.setOnClickListener(prj_doc_insert_listener);

        viewHolder.project_work_insert_btn.setTag(position);
        viewHolder.project_work_insert_btn.setOnClickListener(work_insert_btn_listener);

        /*my 등록버튼*/
        viewHolder.myButtonInsert.setTag(position);
        viewHolder.myButtonInsert.setOnClickListener(mysetDetail_btn_listener);

        viewHolder.project_work_serch_btn.setTag(position);
        viewHolder.project_work_serch_btn.setOnClickListener(project_work_serch_btn_listener);

        TextView reg_date = (TextView)convertView.findViewById(R.id.project_name);
        reg_date.setText(proJectVO.getPrj_name());

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

    public void addItem (ProJectVO list){
        ProJectVO data_list = list;
        listViewItem.add(data_list);
    }
    // View lookup cache
    private static class ViewHolder {

        /*my 등록버튼*/
        Button myButtonInsert;

        TextView project_name;
        Button project_detail_btn;
        Button project_work_insert_btn;
        Button project_work_serch_btn;
        Button prj_doc_insert_btn;
        Button prj_doc_view_btn;

        LinearLayout s_v_i_btn_layout;
        RelativeLayout prj_doc_btn_layout;
    }

    public void clearItem (){
        listViewItem.clear();
    }

    public void set_display(String dp){
        this.display_check = dp;
        notifyDataSetChanged();
    }

}
