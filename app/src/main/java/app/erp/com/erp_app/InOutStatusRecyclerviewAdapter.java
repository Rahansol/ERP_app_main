package app.erp.com.erp_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;





public class InOutStatusRecyclerviewAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<InOutStatusItem> items;

    public InOutStatusRecyclerviewAdapter() {
    }

    public InOutStatusRecyclerviewAdapter(Context context, ArrayList<InOutStatusItem> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recyclerview_in_out_status, viewGroup, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH vh= (VH) viewHolder;
        InOutStatusItem item= items.get(i);

        vh.tvReqType.setText(item.req_type);
        vh.tvRegDate.setText(item.reg_date);
        vh.tvRegTime.setText(item.reg_time);
        vh.tvReqDate.setText(item.req_date);
        vh.tvResDate.setText(item.res_date);
        vh.tvResName.setText(item.res_name);
        vh.tvReqName.setText(item.req_name);
        vh.tvUnitCnt.setText(item.unit_cnt);

        vh.tvScheduleCnt.setText(item.schedule_cnt);
        vh.tvCancelCnt.setText(item.cancel_cnt);
        vh.tvReceiptCnt.setText(item.receipt_cnt);
        vh.tvUnRequestCnt.setText(item.unrequest_cnt);

        if (item.req_type.equals("출고")){
            vh.tvReqType.setTextColor(Color.parseColor("#ffffff"));         //글씨색 변경
            vh.tvReqType.setBackground(context.getResources().getDrawable(R.drawable.box_border_thin_green));   //배경색 변경
        }else {   //입고
            vh.tvReqType.setTextColor(Color.parseColor("#ffffff"));
            vh.tvReqType.setBackground(context.getResources().getDrawable(R.drawable.box_border_thin_blue));
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }




    class VH extends RecyclerView.ViewHolder{
        TextView tvReqType;
        TextView tvRegDate;
        TextView tvRegTime;
        TextView tvReqDate;
        TextView tvResDate;
        TextView tvResName;
        TextView tvReqName;
        TextView tvUnitCnt;
        TextView tvScheduleCnt;
        TextView tvCancelCnt;
        TextView tvReceiptCnt;
        TextView tvUnRequestCnt;


        public VH(final View itemView) {
            super(itemView);

            this.tvReqType = itemView.findViewById(R.id.tv_req_type_value);
            this.tvRegDate = itemView.findViewById(R.id.tv_reg_date_value);
            this.tvRegTime = itemView.findViewById(R.id.tv_reg_time_value);
            this.tvReqDate = itemView.findViewById(R.id.tv_req_date_value);
            this.tvResDate = itemView.findViewById(R.id.tv_res_date_value);
            this.tvResName = itemView.findViewById(R.id.tv_res_name_value);
            this.tvReqName = itemView.findViewById(R.id.tv_req_name_value);
            this.tvUnitCnt = itemView.findViewById(R.id.tv_unit_cnt_value);
            this.tvScheduleCnt = itemView.findViewById(R.id.tv_schedule_cnt_value);
            this.tvCancelCnt = itemView.findViewById(R.id.tv_cancel_cnt_value);
            this.tvReceiptCnt = itemView.findViewById(R.id.tv_receipt_cnt_value);
            this.tvUnRequestCnt = itemView.findViewById(R.id.tv_unrequest_cnt_value);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //인텐트로 데이터값 다른액티비티로 전달
                    Intent intent= new Intent(context, InOutDetailActivity.class);
                    intent.putExtra("reqType", items.get(getLayoutPosition()).req_type);   //요청타입
                    intent.putExtra("regDate", items.get(getLayoutPosition()).reg_date);   //요청날짜
                    intent.putExtra("regTime", items.get(getLayoutPosition()).reg_time);   //요청시간

                    intent.putExtra("reqDate", items.get(getLayoutPosition()).req_date);  //요청일
                    intent.putExtra("resDate", items.get(getLayoutPosition()).res_date);  //물류일

                    intent.putExtra("resName", items.get(getLayoutPosition()).res_name);
                    intent.putExtra("reqName", items.get(getLayoutPosition()).req_name);

                    intent.putExtra("unitCnt", items.get(getLayoutPosition()).unit_cnt);
                    intent.putExtra("scheduleCnt", items.get(getLayoutPosition()).schedule_cnt);
                    intent.putExtra("cancelCnt", items.get(getLayoutPosition()).cancel_cnt);
                    intent.putExtra("receiptCnt", items.get(getLayoutPosition()).receipt_cnt);
                    intent.putExtra("unrequestCnt", items.get(getLayoutPosition()).unrequest_cnt);

                    context.startActivity(intent);

                }
            });
        }
    }
}
