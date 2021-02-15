package app.erp.com.erp_app.document_care.myfragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class TranspBizrAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<TranspBizrItems> items;
    Boolean check;

    public TranspBizrAdapter() {
    }

    public TranspBizrAdapter(Context context, ArrayList<TranspBizrItems> items) {
        this.context = context;
        this.items = items;
    }


    /*커스텀 리스너 객체*/
    public interface MyOnItemClickListener{
        void myOnItemClick(View v, int pos);
    }

    private MyOnItemClickListener myListener= null;

    public void setMyListener(MyOnItemClickListener myListener){
        this.myListener= myListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_transp_bizr_item, parent, false);
        VH holder= new VH(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        TranspBizrItems item= items.get(position);
        vh.checkBox.setChecked(false);
        vh.tvjobName.setText(item.jobName);
        vh.tvBusOffName.setText(item.busOffName);
        vh.tvGarageName.setText(item.garageName);
        vh.tvRouteNum.setText(item.routeNum);
        vh.tvBusNum.setText(item.busNum);
        vh.tvSign.setText(item.sign);
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
        TextView tvSign;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.checkBox= itemView.findViewById(R.id.checkbox);
            this.tvjobName= itemView.findViewById(R.id.tv_job_name);
            this.tvBusOffName= itemView.findViewById(R.id.tv_busoff_name);
            this.tvGarageName= itemView.findViewById(R.id.garage_name);
            this.tvRouteNum= itemView.findViewById(R.id.tv_route_num);
            this.tvBusNum= itemView.findViewById(R.id.tv_bus_num);
            this.tvSign= itemView.findViewById(R.id.tv_sign);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos= getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (myListener != null){
                            myListener.myOnItemClick(v, pos);
                        }
                    }

                    if (checkBox.isChecked()){
                        checkBox.setChecked(false);
                    }else {
                        checkBox.setChecked(true);
                    }

                }
            });
        }

    }


}
