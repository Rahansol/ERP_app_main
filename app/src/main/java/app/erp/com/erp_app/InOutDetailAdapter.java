package app.erp.com.erp_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InOutDetailAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<InOutDetailItems> items;


    public interface OnitemClickListener{
        void onItemClick(View v , int post);
    }

    private InOutDetailAdapter.OnitemClickListener mListener = null;

    public void setmListener(InOutDetailAdapter.OnitemClickListener mListener) {
        this.mListener = mListener;
    }




    public InOutDetailAdapter() {
    }

    public InOutDetailAdapter(Context context, ArrayList<InOutDetailItems> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(viewGroup.getContext());
        View itemView= inflater.inflate(R.layout.recyclerview_inout_detail, viewGroup, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH vh= (VH) viewHolder;
        InOutDetailItems item= items.get(i);

        vh.tvRnumDetail.setText(item.num);
        vh.tvUnitVerDetail.setText(item.unit_ver);
        vh.tvUnitIdDetail.setText(item.unit_id);
        vh.tvExeType.setText(item.exe_type);

        /*vh.tvUnitCodeDetail.setText(item.unit_code);
        vh.tvRepUnitCodeDetail.setText(item.rep_unit_code);*/

        vh.itemView.setTag(item.exe_type);

        if (item.exe_type.equals("수령완료")){
            vh.tvExeType.setTextColor(Color.parseColor("#00ac00"));
        }else if (item.exe_type.equals("물류예정")){
            vh.tvExeType.setTextColor(Color.parseColor("#1531EA"));
        }else if (item.exe_type.equals("출고취소")){
            vh.tvExeType.setTextColor(Color.parseColor("#AC004B"));
        }else {
            vh.tvExeType.setTextColor(Color.parseColor("#4200AC"));
        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tvRnumDetail;
        TextView tvUnitVerDetail;
        TextView tvUnitIdDetail;
        TextView tvExeType;

        /*TextView tvUnitCodeDetail;
        TextView tvRepUnitCodeDetail;*/


        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvRnumDetail= itemView.findViewById(R.id.tv_rnum);
            this.tvUnitVerDetail= itemView.findViewById(R.id.tv_detail_unit_ver);
            this.tvUnitIdDetail= itemView.findViewById(R.id.tv_detail_unit_id);
            this.tvExeType= itemView.findViewById(R.id.tv_exe_type);

            /*this.tvUnitCodeDetail= itemView.findViewById(R.id.tv_detail_unit_code);
            this.tvRepUnitCodeDetail= itemView.findViewById(R.id.tv_detail_rep_unit_code);*/


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(view , pos);
                        }
                    }
                }
            });





        }
    }



}
