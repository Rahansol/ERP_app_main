package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Over_Work_VO;


/**
 * Created by hsra on 2019-06-24.
 */

//연장근무 신청하는 화면에서 업무 선택하는 리스트 생성 어뎁터
public class Over_Work_List_Adapter extends RecyclerView.Adapter<Over_Work_List_Adapter.ViewHolder> {

    private ArrayList<Over_Work_VO> mData = new ArrayList<>();
    private SparseBooleanArray selectItems = new SparseBooleanArray();
    private Context mContext;

    private HashMap<String , Object> return_map = new HashMap<>();
    private Map<String,Object> test_map = new HashMap<>();
    private int prePosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Over_Work_VO data;
        private int postion;

        Button over_work_select_btn;
        TextView o_error_unit;

        TextView o_reg_date;
        TextView o_error_office_group;
        TextView o_error_route;
        TextView o_bus_num;
        TextView error_office;
        TextView o_error_high_code;

        RelativeLayout card_background;


        ViewHolder(View itemView){
            super(itemView);
            over_work_select_btn = (Button)itemView.findViewById(R.id.over_work_select_btn);
            o_error_unit = (TextView)itemView.findViewById(R.id.o_error_unit);
            o_error_high_code = (TextView)itemView.findViewById(R.id.o_error_high_code);

            o_reg_date = (TextView)itemView.findViewById(R.id.o_reg_date);
            o_error_office_group = (TextView)itemView.findViewById(R.id.o_error_office_group);

            error_office = (TextView)itemView.findViewById(R.id.error_office);
            o_bus_num = (TextView)itemView.findViewById(R.id.o_bus_num);
            o_error_route = (TextView)itemView.findViewById(R.id.o_error_route);

            card_background = (RelativeLayout)itemView.findViewById(R.id.card_background);

        }

        void onBind(Over_Work_VO datas , int pos){
            this.data = datas;
            this.postion = pos;

            o_error_unit.setText(data.getUnit_name() + " " + data.getTrouble_high_name());
            o_reg_date.setText(substr_date(data.getReg_date()) + " "  + substr_time(data.getReg_time()));
            o_error_high_code.setText(data.getTrouble_low_name());
            o_error_office_group.setText(data.getOffice_group());

            error_office.setText(data.getBusoff_name());
            o_bus_num.setText(data.getBus_num());
            if(null != data.getRoute_id())o_error_route.setText("노선 : " +data.getRoute_id());
            over_work_select_btn.setOnClickListener(this);
            if("Y".equals(data.getBef_yn())){
                Log.d("pos",""+postion);
                return_map.put(""+postion,data);
                selectItems.put(postion, true);
                over_work_select_btn.setText("선택취소");
                over_work_select_btn.setBackground(mContext.getDrawable(R.drawable.error_equles_bus_btn_cancle));
                card_background.setBackgroundColor(mContext.getResources().getColor(R.color.btn_color));
            }else if ( selectItems.get(postion, false) ){
                over_work_select_btn.setText("선택취소");
                over_work_select_btn.setBackground(mContext.getDrawable(R.drawable.error_equles_bus_btn_cancle));
                card_background.setBackgroundColor(mContext.getResources().getColor(R.color.btn_color));
            } else {
                over_work_select_btn.setText("선택");

                over_work_select_btn.setBackground(mContext.getDrawable(R.drawable.error_equles_bus_btn));
                card_background.setBackgroundColor(mContext.getResources().getColor(R.color.bgTitleLeft));
            }
        }

        @Override
        public void onClick(View view) {

            int pos = getAdapterPosition();

            switch (view.getId()){
                case R.id.over_work_select_btn :
                    if ( selectItems.get(pos, false) ){
                        selectItems.put(pos, false);
                        over_work_select_btn.setText("선택");
                        return_map.remove(""+pos,data);

                        over_work_select_btn.setBackground(mContext.getDrawable(R.drawable.error_equles_bus_btn));
                        card_background.setBackgroundColor(mContext.getResources().getColor(R.color.bgTitleLeft));
                    } else {
                        selectItems.put(pos, true);
                        over_work_select_btn.setText("선택취소");
                        return_map.put(""+pos,data);

                        over_work_select_btn.setBackground(mContext.getDrawable(R.drawable.error_equles_bus_btn_cancle));
                        card_background.setBackgroundColor(mContext.getResources().getColor(R.color.btn_color));
                    }
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int postion) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.my_over_work_list_layout, viewGroup , false);
        Over_Work_List_Adapter.ViewHolder vh  = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int postion) {
        viewHolder.onBind(mData.get(postion) , postion);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Over_Work_VO list){
        mData.add(list);
        notifyDataSetChanged();
    }

    public HashMap<String , Object> get_select_tiem(){
        return return_map;
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
