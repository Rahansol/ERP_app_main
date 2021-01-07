package app.erp.com.erp_app;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class InUnitListAdapter2 extends RecyclerView.Adapter {
    Context context;
    ArrayList<InUnitListItems> items2;
    ArrayList<InUnitListItems> items;
    InUnitListAdapter inUnitListAdapter;

    public InUnitListAdapter2() {
    }

    public InUnitListAdapter2(Context context, ArrayList<InUnitListItems> items2, ArrayList<InUnitListItems> items, InUnitListAdapter inUnitListAdapter) {
        this.context = context;
        this.items2 = items2;
        this.items = items;
        this.inUnitListAdapter = inUnitListAdapter;
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
        InUnitListItems item= items2.get(i);
        vh.tvRnum.setText(item.rnum);
        vh.tvUnitVer.setText(item.unit_ver);
        vh.tvUnitId.setText(item.unit_id);
        vh.checkBox.setChecked(item.checkbox);
    }

    @Override
    public int getItemCount() {
        return items2.size();
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
                    items2.get(getLayoutPosition()).checkbox= isChecked;

                    InUnitListItems item= items2.get(getLayoutPosition());

                    if (isChecked){

                    }else {
                        int i;
                        for (i=0; i<items.size(); i++){
                            InUnitListItems t= items.get(i);
                            if (t.rnum==item.rnum) break;
                        }
                        items.get(i).checkbox= false;

                        WarehousingActivity ac= (WarehousingActivity) context;
                        ac.inUnitListAdapter.notifyItemChanged(i);

                        items2.remove(item);
                        notifyItemRemoved(getLayoutPosition());
                    }
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
