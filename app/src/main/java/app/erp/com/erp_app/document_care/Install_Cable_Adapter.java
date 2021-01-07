package app.erp.com.erp_app.document_care;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class Install_Cable_Adapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<InstallCableItems> items;


    public Install_Cable_Adapter(Context context, ArrayList<InstallCableItems> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.recycler_pager2_items, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        InstallCableItems item= items.get(position);
        vh.tvNum.setText(item.num);
        vh.spItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vh.spItemDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vh.spQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vh.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vh.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class VH extends RecyclerView.ViewHolder{
        TextView tvNum;
        Spinner spItem;
        Spinner spItemDetail;
        Spinner spQuantity;
        ImageView ivMinus;
        ImageView ivPlus;


        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvNum= itemView.findViewById(R.id.num);
            this.spItem= itemView.findViewById(R.id.item);
            this.spItemDetail= itemView.findViewById(R.id.item_detail);
            this.spQuantity= itemView.findViewById(R.id.quantity);
            this.ivMinus= itemView.findViewById(R.id.minus);
            this.ivPlus= itemView.findViewById(R.id.plus);
        }
    }
}
