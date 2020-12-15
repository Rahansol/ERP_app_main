package app.erp.com.erp_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InUnitListAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<InUnitListItems> items;
    ArrayList<InUnitListItems> items2;
    InUnitListAdapter2 inUnitListAdapter2;

    public InUnitListAdapter() {
    }

    public InUnitListAdapter(Context context, ArrayList<InUnitListItems> items, ArrayList<InUnitListItems> items2, InUnitListAdapter2 inUnitListAdapter2) {
        this.context = context;
        this.items = items;
        this.items2 = items2;
        this.inUnitListAdapter2 = inUnitListAdapter2;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_stock_list, viewGroup, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH vh= (VH) viewHolder;
        InUnitListItems item= items.get(i);
        vh.tvRnum.setText(item.rnum);
        vh.tvUnitVer.setText(item.unit_ver);
        vh.tvUnitId.setText(item.unit_id);
        vh.checkBox.setChecked(item.checkbox);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class VH extends RecyclerView.ViewHolder{
        TextView tvRnum;
        TextView tvUnitVer;
        TextView tvUnitId;
        CheckBox checkBox;

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvRnum= itemView.findViewById(R.id.tv_rnum);
            this.tvUnitVer= itemView.findViewById(R.id.tv_unit_ver);
            this.tvUnitId= itemView.findViewById(R.id.tv_unit_id);
            this.checkBox= itemView.findViewById(R.id.stock_checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(getLayoutPosition()).checkbox= isChecked;
                    String Rnum= items.get(getLayoutPosition()).rnum;
                    String UnitVer= items.get(getLayoutPosition()).unit_ver;
                    String UnitId= items.get(getLayoutPosition()).unit_id;
                    Boolean CheckBox= items.get(getLayoutPosition()).checkbox? true : false;

                    String Un_yn= items.get(getLayoutPosition()).un_yn;
                    String In_yn= "In";
                    String Emp_id= items.get(getLayoutPosition()).emp_id;
                    String Unit_code= items.get(getLayoutPosition()).unit_code;
                    String Rep_unit_code= items.get(getLayoutPosition()).rep_unit_code;
                    String Barcode_dep_id= items.get(getLayoutPosition()).barcode_dep_id;

                    String Req_date= items.get(getLayoutPosition()).req_date;
                    String Notice= items.get(getLayoutPosition()).notice;
                    String Request_dep_id= items.get(getLayoutPosition()).request_dep_id;
                    String Response_dep_id= items.get(getLayoutPosition()).response_dep_id;

                    InUnitListItems item= items.get(getLayoutPosition());    //현재누른 항목 아이템

                    if (isChecked){
                        items2.add(items.get(getLayoutPosition()));
                        inUnitListAdapter2.notifyItemInserted(items2.size()-1);
                        Un_yn="";
                    }else {
                        int i;
                        for (i=0; i<items2.size(); i++){
                            InUnitListItems t= items2.get(i);
                            if (t.rnum==item.rnum) break;
                        }
                        items2.remove(items.get(getLayoutPosition()));
                        inUnitListAdapter2.notifyItemRemoved(i);

                        Un_yn="YY";   //선택 해제시 YY를 넣어라        //스프링쪽에서 선택해제시 YY가 들어가는데 in_yn 도 "In"
                    }


                    //예약/취소..통신 -하나로 관리 (예약,업데이트,인서트,삭제..)
                    ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                    Log.d("erp.AppInUnitList2(pram )확인================>>>>>>>",Barcode_dep_id+"/"+Unit_code+"/"+Rep_unit_code+"/"+UnitId+"/"+In_yn+"/"+Un_yn+"/"+Emp_id);
                    Call<Void> call= erp.AppInUnitList(Barcode_dep_id, Unit_code, Rep_unit_code,UnitId, In_yn, Un_yn, Emp_id, Req_date, Notice, Request_dep_id, Response_dep_id );
                    Log.d("emp_id 확인================>>>>>>>","Un_yn : ["+Un_yn+"]In_yn:["+In_yn+"] test");
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("emp_id 통신 fail  ================>>>>>>>",t+"");
                        }
                    });


                    //스프링쪽에서 barcode_dep_id 가 문자열 "김민수" 인서트 되고있음..
                    //request_dep_id=null, response_dep_id=null, req_date=null 로 인서트 되고있음...
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
