package app.erp.com.erp_app.document_care.myfragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class CableAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<CableItems> items;
    ArrayList<InstallPhotoItems> photoItems;

    public CableAdapter() {
    }

    public CableAdapter(Context context, ArrayList<CableItems> items, ArrayList<InstallPhotoItems> photoItems ) {
        this.context = context;
        this.items = items;
        this.photoItems= photoItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_cable_item, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        CableItems item= items.get(position);
        vh.tvCableName.setText(item.cableName);
        vh.tvCableQuantity.setText(item.cableQuantity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class VH extends RecyclerView.ViewHolder{

        TextView tvCableName;
        TextView tvCableQuantity;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.tvCableName= itemView.findViewById(R.id.tv_cableName);
            this.tvCableQuantity= itemView.findViewById(R.id.tv_cableQuantity);
        }
    }

}
