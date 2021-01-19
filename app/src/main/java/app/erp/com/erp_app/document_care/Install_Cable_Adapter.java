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
import android.widget.Toast;

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
        vh.tv_item_group_name.setText(item.item);
        vh.tv_item_each_name.setText(item.item_detail);
        vh.tv_quantity_recycler.setText(item.tv_quantity_recycler);
        vh.btn_delete.setText(item.btn_delete);

        /*삭제버튼 클릭- 리사이클러뷰 아이템 한줄씩 삭제*/
        vh.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, position+1+"번 선택항목을 삭제합니다.", Toast.LENGTH_SHORT).show();
                items.remove(item);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class VH extends RecyclerView.ViewHolder{
        TextView tv_item_group_name;
        TextView tv_item_each_name;
        TextView tv_quantity_recycler;
        TextView btn_delete;

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tv_item_group_name= itemView.findViewById(R.id.item_group_name);
            this.tv_item_each_name= itemView.findViewById(R.id.item_each_name);
            this.tv_quantity_recycler= itemView.findViewById(R.id.tv_quantity_recycler);
            this.btn_delete= itemView.findViewById(R.id.btn_delete);
        }
    }
}
