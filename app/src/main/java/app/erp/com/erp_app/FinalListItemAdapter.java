package app.erp.com.erp_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
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

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvUnitVerFinal= itemView.findViewById(R.id.tv_final_unit_ver);
            this.tvUnitIdFinal= itemView.findViewById(R.id.tv_final_unit_id);
            this.tvUnitCodeFinal= itemView.findViewById(R.id.tv_final_unit_code);
            this.tvRepUnitCodeFinal= itemView.findViewById(R.id.tv_final_rep_unit_code);
        }
    }
    
}
