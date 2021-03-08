package app.erp.com.erp_app.document_care.myInstallSignFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.SelectedStatusAdapter;
import app.erp.com.erp_app.StockListAdapter;
import app.erp.com.erp_app.document_care.myfragments.Installation_List_Sginature_Activity2;
import app.erp.com.erp_app.document_care.myfragments.TranspBizrAdapter;
import app.erp.com.erp_app.document_care.myfragments.TranspBizrItems;


public class TranspBizrAdapter2 extends RecyclerView.Adapter {

    Context context;
    ArrayList<TranspBizrItems2> items;





    /*커스텀 리스너 객체*/
    public interface MyOnItemClickListener{
        void myOnItemClick(View v, int pos);
    }

    private MyOnItemClickListener myListener= null;

    public void setMyListener(MyOnItemClickListener myListener) {
        this.myListener= myListener;
    }




    public TranspBizrAdapter2() {
    }

    public TranspBizrAdapter2(Context context, ArrayList<TranspBizrItems2> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_transp_bizr_item2, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        TranspBizrItems2 item= items.get(position);
        vh.tvTableName.setText(item.tableName);
        vh.tvRegDtti.setText(item.tv_reg_dtti);
        vh.checkBox.setChecked(item.check);
        vh.tvjobName.setText(item.jobName);
        vh.tvBusOffName.setText(item.busOffName);
        vh.tvGarageName.setText(item.garageName);
        vh.tvRouteNum.setText(item.routeNum);
        vh.tvBusNum.setText(item.busNum);
        vh.tvBusId.setText(item.busId);
        vh.tvDocDtti.setText(item.docDtti);
        vh.checkBox.setVisibility(View.INVISIBLE);

        if (item.docDtti.equals("완료")){
            vh.tvDocDtti.setTextColor(Color.parseColor("#E91E63"));
        }else {
            vh.tvDocDtti.setTextColor(Color.parseColor("#5A3CC6"));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView tvjobName;
        TextView tvBusOffName;
        TextView tvGarageName;
        TextView tvRouteNum;
        TextView tvBusNum;
        TextView tvDocDtti;
        TextView tvBusId;
        TextView tvRegDtti;  //등록시간
        TextView tvTableName;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.checkBox= itemView.findViewById(R.id.checkbox);
            this.tvjobName= itemView.findViewById(R.id.tv_job_name);
            this.tvBusOffName= itemView.findViewById(R.id.tv_busoff_name);
            this.tvGarageName= itemView.findViewById(R.id.garage_name);
            this.tvRouteNum= itemView.findViewById(R.id.tv_route_num);
            this.tvBusNum= itemView.findViewById(R.id.tv_bus_num);
            this.tvDocDtti= itemView.findViewById(R.id.tv_doc_dtti);
            this.tvBusId = itemView.findViewById(R.id.tv_bus_id);
            this.tvRegDtti= itemView.findViewById(R.id.tv_reg_dtti);
            this.tvTableName= itemView.findViewById(R.id.tv_table_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //int pos= getAdapterPosition();
                    int pos= getLayoutPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (myListener != null){
                            myListener.myOnItemClick(v, pos);
                            TranspBizrItems2 item2= items.get(pos);
                        }
                    }

                }
            });
        }
    }//VH()..
}
