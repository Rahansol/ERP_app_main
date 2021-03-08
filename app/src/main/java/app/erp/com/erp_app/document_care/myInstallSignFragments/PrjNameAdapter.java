package app.erp.com.erp_app.document_care.myInstallSignFragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;

public class PrjNameAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<PrjNameItems> items;

    public PrjNameAdapter() {
    }

    public PrjNameAdapter(Context context, ArrayList<PrjNameItems> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_prj_name_item, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        PrjNameItems item= items.get(position);

        vh.tvPrjName.setText(item.prjName);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvPrjName;

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvPrjName= itemView.findViewById(R.id.tv_prj_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(context, MyProject_Work_Insert_Activity.class);
                    i.putExtra("SelectedNum", getAdapterPosition()+1+"");    //아이템 선택값 전달하기
                    context.startActivity(i);
                }
            });
        }

    }

}
