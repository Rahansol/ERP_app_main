package app.erp.com.erp_app.document_care.myfragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Struct;
import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class TranspBizrAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<TranspBizrItems> items;

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
        View itemView= inflater.inflate(R.layout.recycler_transp_bizr_item2, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        TranspBizrItems item= items.get(position);

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
        vh.checkBox.setVisibility(View.VISIBLE);
        Log.d("item ::::: ", "["+item.check+"]["+item.jobName+"]["+item.busOffName+"]["+item.garageName+"]["+item.routeNum+"]["+item.busNum+"]["+item.docDtti+"]");


        if (item.docDtti.equals("완료")){
             vh.checkBox.setVisibility(View.INVISIBLE);
             vh.tvDocDtti.setTextColor(Color.parseColor("#E91E63"));
        }else {
            vh.checkBox.setVisibility(View.VISIBLE);
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


            this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (checkBox.isChecked()){
                        checkBox.setChecked(true);
                        items.get(getLayoutPosition()).check= true;
                    }else {
                        checkBox.setChecked(false);
                        items.get(getLayoutPosition()).check= false;
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int pos= getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (myListener != null){
                            myListener.myOnItemClick(v, pos);
                        }
                    }*/

                    //Toast.makeText(context, getLayoutPosition()+" 번째", Toast.LENGTH_SHORT).show();
                    Intent i= new Intent(context, Installation_List_Sginature_Activity2.class);
                    //i.putExtra("prj", items.get(getLayoutPosition()).);   //프로젝트 - 액티비티에서 보내기
                    //i.putExtra("table_name", pos+st_table_name_value);    //테이블명 - 액티비티에서 보내기
                    //i.putExtra("reg_dtti", pos+st_reg_dtti);              //등록시간 - 액티비티에서 보내기

                    //작업, 운수사, 영업소, 차량번호, 확인서, 노선
                    i.putExtra("item_job_name", items.get(getLayoutPosition()).jobName);
                    i.putExtra("item_busoff_name", items.get(getLayoutPosition()).busOffName);
                    i.putExtra("item_garage_name", items.get(getLayoutPosition()).garageName);
                    i.putExtra("item_bus_num", items.get(getLayoutPosition()).busNum);   //차량번호
                    i.putExtra("item_bus_id", items.get(getLayoutPosition()).busId);
                    i.putExtra("item_reg_dtti", items.get(getLayoutPosition()).tv_reg_dtti);
                    i.putExtra("item_sign", items.get(getLayoutPosition()).docDtti);
                    i.putExtra("item_route_num", items.get(getLayoutPosition()).routeNum);
                    i.putExtra("item_table_name", items.get(getLayoutPosition()).tableName);
                    context.startActivity(i);

                }
            });
        }//VH()..

    }


}
