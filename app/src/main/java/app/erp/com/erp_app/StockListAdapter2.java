package app.erp.com.erp_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StockListAdapter2 extends RecyclerView.Adapter {
    Context context;
    ArrayList<StockListItems> items2;
    ArrayList<StockListItems> items;
    StockListAdapter stockListAdapter;


    public StockListAdapter2() {
    }


    public StockListAdapter2(Context context, ArrayList<StockListItems> items2, ArrayList<StockListItems> items, StockListAdapter stockListAdapter) {
        this.context = context;
        this.items2 = items2;
        this.items = items;
        this.stockListAdapter = stockListAdapter;
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
        StockListItems item= items2.get(position);

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


        public VH(@NonNull final View itemView) {
            super(itemView);

            this.tvRnum= itemView.findViewById(R.id.tv_rnum);
            this.tvUnitVer= itemView.findViewById(R.id.tv_unit_ver);
            this.tvUnitId= itemView.findViewById(R.id.tv_unit_id);
            this.checkBox= itemView.findViewById(R.id.stock_checkbox);

            // 체크박스 ..
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items2.get(getLayoutPosition()).checkbox= isChecked;
                    String Rnum= items2.get(getLayoutPosition()).rnum;
                    String UnitVer= items2.get(getLayoutPosition()).unit_ver;
                    String UnitId= items2.get(getLayoutPosition()).unit_id;
                    Boolean CheckBox= items2.get(getLayoutPosition()).checkbox? true : false;

                    if(isChecked){

                    }else {  //체크박스를 취소하면 items2 가 지워지면서 items 도 지워짐.
                    }
                }
            });



        }
    }
}
