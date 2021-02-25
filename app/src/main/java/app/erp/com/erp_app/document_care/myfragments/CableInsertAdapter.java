package app.erp.com.erp_app.document_care.myfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class CableInsertAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<CableInsertItems> items;
    int cnt= 0;
    int tmp=0;

    ArrayList<String> gg = new ArrayList<String>();


    public CableInsertAdapter(Context context, ArrayList<CableInsertItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.recycler_cable, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        CableInsertItems item= items.get(position);
        vh.tvItemEachSeq.setText(item.itemEachSeq);
        //vh.tvItemGroupName.setText(item.itemGroupName);
        vh.tvQuantity.setText(item.quantity);




        if (item.itemEachSeq.equals("0")){
            vh.quantityLayout.setVisibility(View.INVISIBLE);
            vh.tvItemEachSeq.setVisibility(View.INVISIBLE);
            vh.box.setBackground(context.getDrawable(R.drawable.box_without_line));
            vh.tvItemGroupName.setTextSize(16);
            vh.tvItemGroupName.setTextColor(Color.parseColor("#3F51B5"));
            vh.tvItemGroupName.setText("■ "+item.itemGroupName+" ■");
        }else {
            vh.quantityLayout.setVisibility(View.VISIBLE);
            vh.tvItemEachSeq.setVisibility(View.INVISIBLE);
            vh.box.setBackground(context.getResources().getDrawable(R.drawable.box_line));
            vh.tvItemGroupName.setTextSize(13);
            vh.tvItemGroupName.setTextColor(Color.parseColor("#000000"));
            vh.tvItemGroupName.setText("▷ "+item.itemGroupName);
        }

        vh.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt <=1){
                    cnt=1;
                }else {
                    cnt--;
                }
                tmp = Integer.parseInt(vh.tvQuantity.getText()+"") ;

                if (tmp<=1){
                    //vh.tvQuantity.setText("0");
                    item.quantity= "0";
                    vh.tvQuantity.setText(item.quantity);
                   //vh.tvQuantity.setTextColor(Color.parseColor("#E91E63"));
                    notifyItemChanged(position);
                    Garray.value2[position]="0";
                }else{
                    item.quantity= --tmp+"";
                    //vh.tvQuantity.setText(--tmp+"");
                    vh.tvQuantity.setText(item.quantity);
                    //vh.tvQuantity.setTextColor(Color.parseColor("#E91E63"));
                    notifyItemChanged(position);
                    Garray.value2[position]=tmp+"";
                }

                Log.d("asdfasdfasdf;;;;;", vh.tvQuantity.getText()+"");
                Log.d("Change Value :::::: ", "position :"+position+"////Garray.value2"+Garray.value2[position]);
            }
        });
        vh.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmp = Integer.parseInt(vh.tvQuantity.getText()+"") ;
                item.quantity= ++tmp+"";
                vh.tvQuantity.setText(item.quantity);
                //vh.tvQuantity.setTextColor(Color.parseColor("#E91E63"));
                notifyItemChanged(position);
                //vh.tvQuantity.setText(++tmp+"");
                Garray.value2[position]=tmp+"";

            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends  RecyclerView.ViewHolder{

        TextView tvItemEachSeq;
        TextView tvItemGroupName;
        LinearLayout quantityLayout;
        TextView tvQuantity;
        ImageView ivPlus;
        ImageView ivMinus;
        LinearLayout box;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.tvItemEachSeq= itemView.findViewById(R.id.tv_item_each_seq);
            this.tvItemGroupName= itemView.findViewById(R.id.tv_item_group_name);
            this.quantityLayout= itemView.findViewById(R.id.quantity_layout);
            this.tvQuantity= itemView.findViewById(R.id.tv_quantity);
            this.ivPlus= itemView.findViewById(R.id.ic_plus);
            this.ivMinus= itemView.findViewById(R.id.ic_minus);
            this.box= itemView.findViewById(R.id.box);

        }
    }

    public ArrayList<String> getarr(){
        return gg;
    }
}
