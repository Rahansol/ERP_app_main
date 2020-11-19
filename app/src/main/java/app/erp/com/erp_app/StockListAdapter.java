package app.erp.com.erp_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StockListAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<StockListItems> items;
    ArrayList<StockListItems> items2;
    StockListAdapter2 stockListAdapter2;



    public StockListAdapter(Context context, ArrayList<StockListItems> items, ArrayList<StockListItems> items2, StockListAdapter2 stockListAdapter2) {
        this.context = context;
        this.items = items;
        this.items2 = items2;
        this.stockListAdapter2 = stockListAdapter2;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_stock_list, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VH vh= (VH)viewHolder;
        StockListItems item= items.get(position);

        vh.tvRnum.setText(item.rnum);
        vh.tvUnitVer.setText(item.unit_ver);
        vh.tvUnitId.setText(item.unit_id);
        vh.checkBox.setChecked(item.checkbox);

        vh.itemView.setTag(item.rnum+"&"+item.unit_ver+"&"+item.unit_id);

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
        public VH(@NonNull final View itemView) {
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

                    StockListItems item= items.get(getLayoutPosition());

                    if(isChecked){
                        Toast.makeText(context, UnitId+" 선택 ", Toast.LENGTH_SHORT).show();
                        items2.add(items.get(getLayoutPosition()));
                        stockListAdapter2.notifyItemInserted(items2.size()-1);

                    }else {
                        Toast.makeText(context, UnitId+" 선택취소 ", Toast.LENGTH_SHORT).show();
                        int i;
                        for ( i=0; i<items2.size(); i++){
                            StockListItems t= items2.get(i);
                            if (t.rnum==item.rnum)
                                break;
                        }
                        items2.remove(items.get(getLayoutPosition()));
                        stockListAdapter2.notifyItemRemoved(i);
                        //stockListAdapter2.notifyItemRemoved(items2.size()-0);


                    }
                }
            });




        }
    }
}
