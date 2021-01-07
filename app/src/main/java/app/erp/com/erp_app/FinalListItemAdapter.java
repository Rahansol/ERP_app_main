package app.erp.com.erp_app;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

public class FinalListItemAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<FinalStockListItem> items;




    public FinalListItemAdapter() {
    }

    public FinalListItemAdapter(Context context, ArrayList<FinalStockListItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
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
        CheckBox checkBox;

        public VH(@NonNull final View itemView) {
            super(itemView);

            this.tvUnitVerFinal= itemView.findViewById(R.id.tv_final_unit_ver);
            this.tvUnitIdFinal= itemView.findViewById(R.id.tv_final_unit_id);
            this.tvUnitCodeFinal= itemView.findViewById(R.id.tv_final_unit_code);
            this.tvRepUnitCodeFinal= itemView.findViewById(R.id.tv_final_rep_unit_code);
            this.checkBox= itemView.findViewById(R.id.final_checkbox);
            checkBox.setChecked(true);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(getLayoutPosition()).checkBox= isChecked;
                    Boolean Checkbox= items.get(getLayoutPosition()).checkBox? true : false;
                    String empId= items.get(getLayoutPosition()).req_emp_id;
                    String barcodeDepId= items.get(getLayoutPosition()).barcode_dep_id;
                    String unYn= items.get(getLayoutPosition()).un_yn;
                    String unitCode= items.get(getLayoutPosition()).unit_code;
                    String repUnitCode= items.get(getLayoutPosition()).rep_unit_code;
                    String unitId= items.get(getLayoutPosition()).unit_id;
                    String inYn= items.get(getLayoutPosition()).in_yn;
                    String reqDate= items.get(getLayoutPosition()).req_date;
                    String notice= items.get(getLayoutPosition()).notice;
                    String requestDepId= items.get(getLayoutPosition()).request_dep_id;
                    String responseDepId= items.get(getLayoutPosition()).response_dep_id;


                    FinalStockListItem item= items.get(getLayoutPosition());  //현재 누른 아이템 항목 얻어오기

                    if (isChecked){
                        unYn= "";
                    }else {
                        unYn= "YY";
                    }

                    /*DialogFinalCheckActivity 에 접근해 바코드값 가져오기..*/
                    DialogFinalRequestListActivity ac= (DialogFinalRequestListActivity) context;
                    String barcodeValueOut= ac.barcodeDepIdValueOut;
                    String requestValueOut= ac.requestDepIdOut;
                    String currentDateValueOut= ac.currentDateValueOut;

                    /*출고- 여기서 업데이트/삭제하는 쿼리문 돌리기..*/
                    ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                    Call<Void> call= erp.AppUnitBookingChk_map(empId, barcodeValueOut, unYn, unitCode, repUnitCode, unitId, inYn, currentDateValueOut, notice, requestValueOut, barcodeValueOut );
                    Log.d("param list ======> ",empId+"/"+barcodeValueOut+"/"+unYn+"/"+unitCode+"/"+repUnitCode+"/"+unitId+"/"+inYn+"/"+currentDateValueOut+"/"+notice+"/"+requestValueOut+"/"+responseDepId);
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
                    if (checkBox.isChecked()){checkBox.setChecked(false);}
                    else{checkBox.setChecked(true);}
                }
            });
        }
    }
    
}
