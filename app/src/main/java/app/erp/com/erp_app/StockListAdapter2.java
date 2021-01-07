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

                    StockListItems item= items2.get(getLayoutPosition());

                    if(isChecked){

                    }else {  //체크박스를 취소하면 items2 가 지워지면서 items 의 체크박스 선택이 취소로 변경.
                        int i;
                        for (i=0; i< items.size(); i++){
                            StockListItems t= items.get(i);
                            if (t.rnum==item.rnum) break;
                        }
                        items.get(i).checkbox= false;

                        ReleaseRequestActivity ac= (ReleaseRequestActivity) context;    //context를 이용해 액티비티에 접근..
                        ac.adapter_stock.notifyItemChanged(i);                          //액티비티에서 adapter_stock 멤버변수를 static으로 만들어, 생성자 전달없이 클래스명으로 접근하여 notify..
                        

                        items2.remove(item);
                        notifyItemRemoved(getLayoutPosition());

                        /*items2.remove(items2.get(getLayoutPosition()));
                        notifyItemRemoved(getAdapterPosition());
                        //item.checkbox.*/
                    }
                }
            });



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(false);

                }
            });


        }
    }
}
