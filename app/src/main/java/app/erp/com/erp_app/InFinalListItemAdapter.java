package app.erp.com.erp_app;

import android.content.Context;
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
import java.util.List;

import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InFinalListItemAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<FinalStockListItem> items;

    public InFinalListItemAdapter() {
    }

    public InFinalListItemAdapter(Context context, ArrayList<FinalStockListItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.dialog_recyclerview_final_list_check, viewGroup, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH vh= (VH) viewHolder;
        FinalStockListItem item= items.get(i);

        vh.tvUnitVerFinal.setText(item.unit_ver);
        vh.tvUnitIdFinal.setText(item.unit_id);
        vh.tvUnitCodeFinal.setText(item.unit_code);
        vh.tvRepUnitCodeFinal.setText(item.rep_unit_code);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class VH extends RecyclerView.ViewHolder{
        TextView tvUnitVerFinal;
        TextView tvUnitIdFinal;
        TextView tvUnitCodeFinal;
        TextView tvRepUnitCodeFinal;
        CheckBox checkBoxIn;

        public VH(@NonNull final View itemView) {
            super(itemView);

            this.tvUnitVerFinal= itemView.findViewById(R.id.tv_final_unit_ver);
            this.tvUnitIdFinal= itemView.findViewById(R.id.tv_final_unit_id);
            this.tvUnitCodeFinal= itemView.findViewById(R.id.tv_final_unit_code);
            this.tvRepUnitCodeFinal= itemView.findViewById(R.id.tv_final_rep_unit_code);
            this.checkBoxIn= itemView.findViewById(R.id.final_checkbox);
            checkBoxIn.setChecked(true);

            checkBoxIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(getLayoutPosition()).checkBox= isChecked;
                    String In_yn= "In";
                    String Un_yn= items.get(getLayoutPosition()).un_yn;
                    String Emp_id= items.get(getLayoutPosition()).req_emp_id;
                    String Barcode_dep_id= items.get(getLayoutPosition()).req_location;

                    String Req_date= items.get(getLayoutPosition()).req_date;
                    String Notice= items.get(getLayoutPosition()).notice;
                    String Request_dep_id= items.get(getLayoutPosition()).request_dep_id;
                    String Response_dep_id= items.get(getLayoutPosition()).response_dep_id;

                    String Unit_ver= items.get(getLayoutPosition()).unit_ver;
                    String Unit_id= items.get(getLayoutPosition()).unit_id;
                    String Unit_code= items.get(getLayoutPosition()).unit_code;
                    String Rep_unit_code= items.get(getLayoutPosition()).rep_unit_code;
                    Boolean CheckBox= items.get(getLayoutPosition()).checkBox;

                    FinalStockListItem item= items.get(getLayoutPosition());

                    if (isChecked){
                        Un_yn="";
                    }else {
                        Un_yn="YY";

                    }

                    /*DialogFinalCheckInActivity 에 접근해 바코드값 가져오기..*/
                    DialogFinalCheckInActivity ac= (DialogFinalCheckInActivity) context;
                    String barcodeValue= ac.barcodeDepIdValue;
                    String responseValue= ac.responseDepId;
                    String reqDateValue= ac.currentDateValue;


                    /*입고- 여기서 업데이트/삭제하는 쿼리문 돌리기..*/
                    ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                    Call<Void> call= erp.AppInUnitList(barcodeValue, Unit_code, Rep_unit_code, Unit_id,In_yn, Un_yn, Emp_id, reqDateValue, Notice, barcodeValue, responseValue);
                    Log.d("param list ======> ", barcodeValue+"/"+Unit_code+"/"+Rep_unit_code+"/"+In_yn+"/"+ Unit_id+"/"+Un_yn);    //여기서 그냥 Barcode_dep_id 를 쓰면 문자열"김민수"가 나옴 그래서 barcodeValue로...
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });


                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxIn.isChecked()){
                        checkBoxIn.setChecked(false);
                    }else {
                        checkBoxIn.setChecked(true);
                    }
                }
            });
        }
    }
}
