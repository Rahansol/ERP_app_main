package app.erp.com.erp_app.document_care.myfragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class UnitAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<UnitItems> items;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private UnitAdapter.OnItemClickListener mListener= null;

    public void setMyListener(UnitAdapter.OnItemClickListener mListener){
        this.mListener= mListener;
    }

    public UnitAdapter() {
    }

    public UnitAdapter(Context context, ArrayList<UnitItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_installation_unit_items, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        UnitItems item= items.get(position);

        vh.tvText.setText(item.text);
        vh.tvVal.setText(item.val);
        vh.tvText2.setText(item.text2);
        vh.tvVal2.setText(item.val2);
        vh.viewLine.setVisibility(View.VISIBLE);
        vh.ivOpen.setVisibility(View.VISIBLE);

        if (item.text.equals("작업일")){
            vh.viewLine.setVisibility(View.VISIBLE);
            vh.ivOpen.setVisibility(View.VISIBLE);
        }else {
            vh.viewLine.setVisibility(View.INVISIBLE);
            vh.ivOpen.setVisibility(View.INVISIBLE);
        }
        //notifyItemChanged(position);





        //텍스트 스타일- bold 설정
        if (item.text.equals("작업일")|| item.text.equals("차량번호") /*|| item.text.equals("노선") || item.text.equals("작업자")*/
                || item.text2.equals("작업일")|| item.text2.equals("차량번호") /*|| item.text2.equals("노선") || item.text2.equals("작업자")*/){
            /*vh.tvText.setTypeface(null, Typeface.BOLD);
            vh.tvVal.setTypeface(null, Typeface.BOLD);
            vh.tvText2.setTypeface(null, Typeface.BOLD);
            vh.tvVal2.setTypeface(null, Typeface.BOLD);*/
            vh.tvText.setBackgroundResource(R.drawable.transparent_circle_purple_outline);
            vh.tvText2.setBackgroundResource(R.drawable.transparent_circle_purple_outline);
            vh.layoutBox.setBackgroundColor(Color.parseColor("#CECEDA"));
        }else {
            /*vh.tvText.setTypeface(null);
            vh.tvVal.setTypeface(null);
            vh.tvText2.setTypeface(null);
            vh.tvVal2.setTypeface(null);*/
            vh.layoutBox.setBackgroundColor(Color.parseColor("#ffffff"));
            vh.tvText.setBackgroundResource(R.drawable.transparent_circle_grey_outline_bold);
            vh.tvText.setBackgroundResource(R.drawable.transparent_circle_grey_outline_bold);
        }






        if (item.text==null || item.text2==null){
            //vh.tvText.setBackground(R.drawable.box_in_out4);
        }



   }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView ivOpen;
        View viewLine;
        LinearLayout layoutBox;
        TextView tvText;
        TextView tvVal;
        TextView tvText2;
        TextView tvVal2;


        public VH(@NonNull View itemView) {
            super(itemView);

            this.ivOpen= itemView.findViewById(R.id.iv_open);
            this.viewLine= itemView.findViewById(R.id.line);
            this.layoutBox= itemView.findViewById(R.id.layout_box);
            this.tvText= itemView.findViewById(R.id.tv_text);
            this.tvVal= itemView.findViewById(R.id.tv_val);
            this.tvText2= itemView.findViewById(R.id.tv_text2);
            this.tvVal2= itemView.findViewById(R.id.tv_val2);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, getLayoutPosition()+" 번째 단말기 상세목록", Toast.LENGTH_SHORT).show();

                    //설치확인서 상세 화면으로 이동
                    /*Intent i= new Intent(context, Installation_List_Sginature_Activity2_1.class);
                    //프로젝트, 작업, 운수사, 영업소, 차량번호, 차대번호, 등록시간, 노선, 등록자, 확인자 데이터 값 전달..
                    context.startActivity(i);*/

                    int mPos= getAdapterPosition();
                    if (mPos != RecyclerView.NO_POSITION){
                        if (mListener != null){
                            mListener.onItemClick(viewLine, mPos);
                        }
                    }
                }
            });

        }
    }
}
