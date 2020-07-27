package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import app.erp.com.erp_app.vo.Over_Work_VO;


/**
 * Created by hsra on 2019-06-24.
 */

//연장근무 상세보기 다이알로그에서 신청한 업무내역 리스트
public class Over_Work_Insert_List_Adapter extends RecyclerView.Adapter<Over_Work_Insert_List_Adapter.ViewHolder> {

    private ArrayList<Over_Work_List_VO> mData = new ArrayList<>();
    private SparseBooleanArray selectItems = new SparseBooleanArray();
    private Context mContext;

    private HashMap<String , Object> return_map = new HashMap<>();
    private Map<String,Object> test_map = new HashMap<>();
    private int prePosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Over_Work_List_VO data;
        private int postion;

        TextView io_error_high_code;
        TextView io_error_unit;
        TextView io_error_office;
        TextView io_error_route;
        TextView io_error_office_group;
        TextView io_reg_date;

        ViewHolder(View itemView){
            super(itemView);

            io_error_high_code = (TextView)itemView.findViewById(R.id.io_error_high_code);
            io_error_unit = (TextView)itemView.findViewById(R.id.io_error_unit);
            io_error_office = (TextView)itemView.findViewById(R.id.io_error_office);
            io_error_route = (TextView)itemView.findViewById(R.id.io_error_route);
            io_error_office_group = (TextView)itemView.findViewById(R.id.io_error_office_group);
            io_reg_date = (TextView)itemView.findViewById(R.id.io_reg_date);

        }

        void onBind(Over_Work_List_VO datas , int pos){
            this.data = datas;
            this.postion = pos;

            io_error_unit.setText(data.getTrouble_high_name());
            io_reg_date.setText(substr_date(data.getT_reg_date()) + " "  + substr_time(data.getT_reg_time()));
            io_error_high_code.setText(data.getTrouble_low_name());
            io_error_office_group.setText(data.getOffice_group());
            io_error_office.setText(data.getBusoff_name());
            if(null != data.getRoute_id())io_error_route.setText("노선 : " +data.getRoute_id());
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int postion) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.my_over_work_insert_list_layout, viewGroup , false);
        Over_Work_Insert_List_Adapter.ViewHolder vh  = new ViewHolder(view);
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

    public void addItem(Over_Work_List_VO list){
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
