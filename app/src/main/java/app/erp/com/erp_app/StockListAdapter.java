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

public class StockListAdapter extends RecyclerView.Adapter {


    Context context;
    ArrayList<StockListItems> items;

    public int number = 0;



    public StockListAdapter() {
    }

    public StockListAdapter(Context context, ArrayList<StockListItems> items) {
        this.context = context;
        this.items = items;
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

            // 체크박스 ..
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(getLayoutPosition()).checkbox= isChecked;
                    String Rnum= items.get(getLayoutPosition()).rnum;
                    String UnitVer= items.get(getLayoutPosition()).unit_ver;
                    String UnitId= items.get(getLayoutPosition()).unit_id;
                    Boolean CheckBox= items.get(getLayoutPosition()).checkbox? true : false;   // Y/N이니까 string??

                    if(isChecked){
                        Toast.makeText(context, UnitVer+" -  "+UnitId+" 선택 ", Toast.LENGTH_SHORT).show();
                        // 1. DB에 update 하기..
                        /*if(isChecked){
                            db.execSQL(" INSERT INTO "+ tableName + "(foodImg, foodTitle, foodSub, fav) VALUES('"+foodImg+"','"+foodTitle+"','"+foodSub+"','"+fav+"')");
                        }else {
                            db.execSQL("DELETE FROM "+ tableName+" WHERE foodTitle='"+foodTitle+"'" );
                        }*/




                        // 2. 체크가를 선택하면 선택현황 아이템 숫자 증가++

                    }else {
                        Toast.makeText(context, UnitVer+" -  "+UnitId+" 선택취소 ", Toast.LENGTH_SHORT).show();
                        // 1. DB에 delete 하기..
                        // 2. 체크를 취소하면 선택현황 아이템 숫자 하감--
                    }
                }
            });



        }
    }
}
