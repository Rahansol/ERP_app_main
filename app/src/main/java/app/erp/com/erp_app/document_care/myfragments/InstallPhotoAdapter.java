package app.erp.com.erp_app.document_care.myfragments;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class InstallPhotoAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<InstallPhotoItems> items;
    ArrayList<CableItems> cables;
    public InstallPhotoAdapter() {
    }

    public InstallPhotoAdapter(Context context, ArrayList<InstallPhotoItems> items, ArrayList<CableItems> cables) {
        this.context = context;
        this.items = items;
        this.cables = cables;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_install_photo_item, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        InstallPhotoItems item= items.get(position);
        vh.tvUnitName.setText(item.unitName);
        vh.ivUnitPhoto.setImageURI(Uri.parse(item.unitPhoto));
        //Glide.with(context).load(item.unitPhoto).into(vh.ivUnitPhoto);
        Picasso.get()
                .load(item.unitPhoto)
                .error(R.drawable.ic_error)
                .into(vh.ivUnitPhoto);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class VH extends RecyclerView.ViewHolder{

        TextView tvUnitName;
        ImageView ivUnitPhoto;
        TextView tvItemType;


        public VH(@NonNull View itemView) {
            super(itemView);
            this.tvUnitName= itemView.findViewById(R.id.tv_unitName);
            this.ivUnitPhoto= itemView.findViewById(R.id.iv_unitPhoto);
            this.tvItemType= itemView.findViewById(R.id.tv_item_type);
        }
    }

}
