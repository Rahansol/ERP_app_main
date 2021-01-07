package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import app.erp.com.erp_app.AdapterViewHolder;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.callcenter.Fragment_trouble_list;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;


/**
 * Created by hsra on 2019-06-24.
 */

public class My_Error_Adapter extends BaseAdapter {

    private ArrayList<Trouble_HistoryListVO> listViewItem = new ArrayList<Trouble_HistoryListVO>();
    private View.OnClickListener ErrorEventBtnClickListener;  //최근 3-6개월 장래목록 클릭리스너
    private View.OnClickListener defaultRequestBtnClickListener;
    private View.OnClickListener equal_bus_btn;
    private View.OnClickListener equal_trouble_btn;
    private View.OnClickListener call_text_listener;
    private View.OnClickListener move_info_btn;
    private View.OnClickListener notice_btn_listener;
    private String emp_id;

    AdapterViewHolder viewHolder;
    public My_Error_Adapter(String p_emp_id) {
        this.emp_id = p_emp_id;
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
            /*최근 3~6개월 장애*/
            viewHolder.btn_error_event_num= (Button) convertView.findViewById(R.id.btn_error_event_num);

            viewHolder.btn_1 = (Button)convertView.findViewById(R.id.btn_1);
            viewHolder.btn_2 = (Button)convertView.findViewById(R.id.btn_2);
            viewHolder.move_btn = (Button)convertView.findViewById(R.id.move_btn);
            viewHolder.notice_btn = (Button)convertView.findViewById(R.id.notice_btn);
//            viewHolder.btn_3 = (Button)convertView.findViewById(R.id.btn_3);

            viewHolder.error_office = convertView.findViewById(R.id.error_office);
            viewHolder.error_route = convertView.findViewById(R.id.error_route);
            viewHolder.error_bus_num = convertView.findViewById(R.id.error_bus_num);
            viewHolder.error_unit = convertView.findViewById(R.id.error_unit);
            viewHolder.error_high_code = convertView.findViewById(R.id.error_high_code);
            viewHolder.error_low_code = convertView.findViewById(R.id.error_low_code);
            viewHolder.error_office_group = convertView.findViewById(R.id.error_office_group);
            viewHolder.reg_date = convertView.findViewById(R.id.reg_date);
            viewHolder.card_background = convertView.findViewById(R.id.card_background);

            viewHolder.reg_emp_name = convertView.findViewById(R.id.reg_name);
            Trouble_HistoryListVO trouble_historylistvo = listViewItem.get(position);
//            Log.d("gubun " ,":"+trouble_historylistvo.getGubun() + " / " + trouble_historylistvo.getBusoff_name() + "/" + trouble_historylistvo.getBus_num());


            convertView.setTag(viewHolder);
        }else viewHolder = (ViewHolder)convertView.getTag();

        Trouble_HistoryListVO trouble_historylistvo = listViewItem.get(position);

        viewHolder.reg_emp_name.setText("등록자 : " + trouble_historylistvo.getReg_emp_name());

        /*최근 3~6개월 장애건수*/
        viewHolder.btn_error_event_num.setTag(position);
        viewHolder.btn_error_event_num.setOnClickListener(ErrorEventBtnClickListener);

        viewHolder.btn_1.setTag(position);
        viewHolder.btn_1.setOnClickListener(defaultRequestBtnClickListener);

        viewHolder.btn_2.setTag(position);
        viewHolder.btn_2.setOnClickListener(equal_bus_btn);

        viewHolder.move_btn.setTag(position);
        viewHolder.move_btn.setOnClickListener(move_info_btn);

        if(emp_id.equals(trouble_historylistvo.getMove_emp_id()) && "N".equals(trouble_historylistvo.getFirst_yn())){
            viewHolder.move_btn.setText("지원중");
            viewHolder.move_btn.setBackgroundColor(context.getResources().getColor(R.color.app_blue));
        }else if(trouble_historylistvo.getMove_emp_id() == null){
            viewHolder.move_btn.setText("이동하기");
            viewHolder.move_btn.setBackgroundColor(context.getResources().getColor(R.color.green_btn));
        }else if(trouble_historylistvo.getMove_emp_id().equals(emp_id)){
            viewHolder.move_btn.setText("이동중");
            viewHolder.move_btn.setBackgroundColor(context.getResources().getColor(R.color.app_blue));
        }else{
            viewHolder.move_btn.setText(trouble_historylistvo.getMove_emp_name());
            viewHolder.move_btn.setBackgroundColor(context.getResources().getColor(R.color.red_btn));
        }

        viewHolder.error_office.setText(trouble_historylistvo.getBusoff_name());
        if(trouble_historylistvo.getRoute_id() == null){
            viewHolder.error_route.setText("노선 : 정보 없음 ");
        }else{
            viewHolder.error_route.setText("노선 : "+trouble_historylistvo.getRoute_id());
        }

        /*최근 3개월 장애건수*/
        viewHolder.btn_error_event_num.setText(trouble_historylistvo.getBef_err_cnt());
        if(trouble_historylistvo.getBef_err_cnt().equals('0')){
            viewHolder.btn_error_event_num.setText("");
        }else{
            viewHolder.btn_error_event_num.setText("발생 : "+trouble_historylistvo.getBef_err_cnt()+"건");
        }


        Bundle bundle= new Bundle();
        bundle.putString("transp_bizr_id",trouble_historylistvo.getTransp_bizr_id());
        bundle.putString("bus_id",trouble_historylistvo.getBus_id());


        Log.d("bundle  ==================>  ", bundle.toString()+"");
        Fragment fr= new Fragment_trouble_list();
        fr.setArguments(bundle);



        viewHolder.error_bus_num.setText(trouble_historylistvo.getBus_num());
        viewHolder.reg_date.setText(substr_date(trouble_historylistvo.getReg_date()) + " "  + substr_time(trouble_historylistvo.getReg_time()));
        String error_date_time = trouble_historylistvo.getUnit_name() ;
        String error_high_code_unit_code = trouble_historylistvo.getUnit_name() +" " + trouble_historylistvo.getTrouble_name() ;

        viewHolder.error_unit.setText(error_high_code_unit_code);
        viewHolder.error_high_code.setText(trouble_historylistvo.getTrouble_low_name());
        if(trouble_historylistvo.getDriver_tel_num() == null){
            viewHolder.error_low_code.setText("☎ : 미등록" );
        }else{
            viewHolder.error_low_code.setText("☎ : " + trouble_historylistvo.getDriver_tel_num());
            viewHolder.error_low_code.setTextColor(context.getResources().getColor(R.color.app_blue));
            viewHolder.error_low_code.setTag(trouble_historylistvo.getDriver_tel_num());
            viewHolder.error_low_code.setOnClickListener(call_text_listener);
        }

//        viewHolder.error_low_code.setTextColor(R.color.origin_color);
        viewHolder.error_office_group.setText(trouble_historylistvo.getOffice_group());
        if("X".equals(trouble_historylistvo.getGubun())){
            viewHolder.card_background.setBackgroundColor(context.getResources().getColor(R.color.green_text_color2));
            viewHolder.error_office.setTextColor(context.getResources().getColor(R.color.app_blue));
        }else{
            viewHolder.card_background.setBackgroundColor(context.getResources().getColor(R.color.bgTitleLeft));
            viewHolder.error_office.setTextColor(context.getResources().getColor(R.color.history_text_color));
        }

        if(null != trouble_historylistvo.getArs_notice()){
            viewHolder.notice_btn.setTag(position);
            viewHolder.notice_btn.setVisibility(View.VISIBLE);
            viewHolder.notice_btn.setOnClickListener(notice_btn_listener);
        }else{
            viewHolder.notice_btn.setVisibility(View.GONE);
        }

        return convertView;
    }

    /*최근 3~-6개월 장애목록*/
    public void setErrorEventBtnClickListener(View.OnClickListener ErrorEventBtnClickListener){
        this.ErrorEventBtnClickListener = ErrorEventBtnClickListener;
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    public void setEqual_bus_btn_Listener(View.OnClickListener equal_bus_btn_listener){
        this.equal_bus_btn = equal_bus_btn_listener;
    }

    public void setEqual_trouble_btn(View.OnClickListener equal_trouble_btn){
        this.equal_trouble_btn = equal_trouble_btn;
    }

    public View.OnClickListener getCall_text_listener() {
        return call_text_listener;
    }

    public void setCall_text_listener(View.OnClickListener call_text_listener) {
        this.call_text_listener = call_text_listener;
    }

    public View.OnClickListener getMove_info_btn() {
        return move_info_btn;
    }

    public void setMove_info_btn(View.OnClickListener move_info_btn) {
        this.move_info_btn = move_info_btn;
    }

    public void setNotice_btn_listener(View.OnClickListener notice_btn_listener) {
        this.notice_btn_listener = notice_btn_listener;
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
        Button btn_error_event_num;     /*최근 3~6개월 장애건*/
        Button btn_1;
        Button btn_2;
        Button move_btn;
        Button notice_btn;
//        Button btn_3;
        TextView error_office;
        TextView error_route;
        TextView error_bus_num;
        TextView error_unit;
        TextView error_high_code;
        TextView error_low_code;
        TextView error_office_group;
        TextView reg_date;
        TextView reg_emp_name;
        RelativeLayout card_background;
    }

    public Trouble_HistoryListVO  resultItem(int postion){
        return listViewItem.get(postion);
    }

    public ArrayList<Trouble_HistoryListVO> resultList(){
        return listViewItem;
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
