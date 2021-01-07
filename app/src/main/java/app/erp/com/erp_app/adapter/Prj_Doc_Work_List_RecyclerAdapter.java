package app.erp.com.erp_app.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;

public class Prj_Doc_Work_List_RecyclerAdapter extends RecyclerView.Adapter<Prj_Doc_Work_List_RecyclerAdapter.ViewHolder>{

    private ArrayList<ProJectVO> mData = new ArrayList<>();
    private Context mContext;

    private ArrayList<ProJectVO> req_list = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ProJectVO data;
        private int postion;

        TextView work_reg_dtti;
        TextView work_reg_man;
        TextView work_route_num;
        TextView work_bus_num;

        CheckBox work_check;

        ViewHolder(View itemview){
            super(itemview);

            work_reg_dtti = (TextView)itemview.findViewById(R.id.work_reg_dtti);
            work_reg_man = (TextView)itemview.findViewById(R.id.work_reg_man);
            work_route_num = (TextView)itemview.findViewById(R.id.work_route_num);
            work_bus_num = (TextView)itemview.findViewById(R.id.work_bus_num);

            work_check = (CheckBox)itemview.findViewById(R.id.work_check);
        }

        void  onBind(ProJectVO datas , int pos){
            this.data = datas;
            this.postion = pos;

            work_reg_dtti.setText(return_date(data.getReg_dtti()));
            work_reg_man.setText(data.getEmp_name());
            work_route_num.setText(data.getRoute_num());
            work_bus_num.setText(data.getBus_num());

            work_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(work_check.isChecked()){
                        req_list.add(data);
                    }else{
                        ProJectVO remove_p = null;
                        boolean remove_flag = false;
                        for(ProJectVO p : req_list){
                            if(p.equals(data)){
                                remove_flag = true;
                                remove_p=p;
                            }
                        }
                        if(remove_flag){
                            req_list.remove(remove_p);
                        }
                    }
                }
            });
        }
    }

    public void addItem(ProJectVO list){
        mData.add(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.recyclerview_prj_doc_work_list, viewGroup , false);
        Prj_Doc_Work_List_RecyclerAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int postion) {
        viewHolder.onBind(mData.get(postion) , postion);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String return_date(String date){
        String rs = date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8) + "\n" + date.substring(8,10) +":" + date.substring(10,12);
        return rs;
    }

    public ArrayList<ProJectVO> return_check_work (){
        HashSet<ProJectVO> listSet = new HashSet<ProJectVO>(req_list);
        ArrayList<ProJectVO> processedList = new ArrayList<ProJectVO>(listSet);
        return processedList;
    }
}
