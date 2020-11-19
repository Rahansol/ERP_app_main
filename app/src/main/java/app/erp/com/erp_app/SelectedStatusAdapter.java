package app.erp.com.erp_app;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectedStatusAdapter extends RecyclerView.Adapter {

    private boolean isChecked=true;
    private boolean itemClicked=true;
    private int selectedPosition= -1;

    Context context;
    ArrayList<selectedStatusItems> items;
    //리사이클러뷰 외부(액티비티 or 프래그먼트)에서 아이템 클릭 이벤트 처리하기
    // - 어댑터에 직접 리스너 인터페이스를 정의한다.
    // - 액티비티에서 해당 리스너 객체를 생성하고 어댑터에 전달하여 호출되도록 만들기.  = 커스텀 리스너 Custom Listener
    // 즉, 자식(여기서는 어댑터가 해당)이 부모(액티비티)의 이벤트 핸들러를 호출할 필요가 있을 때 사용하는 방법임.


    // 1. 커스텀 리스너 인터페이스(OnItemClickListener)정의.
    public interface OnitemClickListener{            //1.1 자식(어댑터)안에서 새로운 리스너 인터페이스 정의
        void onItemClick(View v , int post);
    }

    // 2. 리스너 객체를 전달하는 메소드(setOnItemClickListener())와 전달된 객체를 저장할 변수(mListener)추가
    private OnitemClickListener mListener = null;     //2.1 리스너 객체 참조를 저장하는 변수

    public void setmListener(OnitemClickListener mListener) {   //2.2 OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메소드
        this.mListener = mListener;
    }





    public SelectedStatusAdapter() {
    }

    public SelectedStatusAdapter(Context context, ArrayList<selectedStatusItems> items) {
        // 넘어온 아이템 리스트를 SelectedStatusAdapter class 전역변수인  items 에  this.items = items; 넣어주고
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int vewType) {           // 여기 왜 이렇게 뜨는지 모르겠어요
        LayoutInflater inflater= LayoutInflater.from(context);
//        View itemView= inflater.inflate(R.layout.activity_release_request, viewGroup, false);
        View itemView= inflater.inflate(R.layout.recycler_selected_status_items, viewGroup, false);
        VH holder= new VH(itemView);
        return holder;
    }



    // 텍스트 set set set
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        VH vh= (VH)viewHolder;
        selectedStatusItems item= items.get(position);   //원래느 여기에 position 이건데 int i로 되어있더라구요 이것도 마찬가지로 변수명 개념으로 보시면 되영 아하하
        vh.tvUnitName.setText(item.unitName);
        vh.tvUnitTotalNum.setText(item.unitTotalNum);
        /*vh.tvUnitSelectedNum.setText(""+item.unitSelectedNum);*/

        // 여기서 settag 할때 스트링이 들어가는데 만약 T1022 이렇게 들어가면 어떤게 unitCode 인지 repUnitCode 인지 모르겠져 ?
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // setTag 하기..
        vh.itemView.setTag(item.unitCode+"&"+item.repUnitCode+"&"+item.unitName+"&"+item.unitTotalNum);  //제가 더 추가해서 넘겨줬어요 어디에서 사용하실려고 넘긴거에여 ? 액티비티에서 tag로 uni_code랑 넘겨준거처럼 tag로 넘겨서 사용하려구요
        // 받아서 어디에서요 ?
        /*vh.itemView.setTag(item.unitCode);
        vh.itemView.setTag(item.repUnitCode);//이렇게 해도 되죠? ㄴㄴ 이렇게 하면 마지막꺼만 들어가요*/



        //아이템 선택항목 강조
        /*if (selectedPosition == position){
            vh.itemView.setBackgroundResource(R.drawable.selected_box);
            vh.tvUnitName.getResources().getColor(R.color.white);
            vh.tvUnitTotalNum.getResources().getColor(R.color.white);
        }else {
            vh.itemView.setBackgroundResource(R.drawable.unselected_box);
            vh.tvUnitName.getResources().getColor(R.color.textBlack2);
            vh.tvUnitTotalNum.getResources().getColor(R.color.textBlack2);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class VH extends RecyclerView.ViewHolder{

        TextView tvUnitName;
        TextView tvUnitTotalNum;
        /*TextView tvUnitSelectedNum;*/

        public VH(@NonNull final View itemView) {
            super(itemView);


            this.tvUnitName= itemView.findViewById(R.id.unit_name);
            this.tvUnitTotalNum= itemView.findViewById(R.id.unit_total_num);
            /*this.tvUnitSelectedNum= itemView.findViewById(R.id.unit_selected_num);*/



            // 3. 아이템 클릭 이벤트 핸들러 메소드에서 리스너 객체 메소드(onItemClick)호출 = 뷰홀더에서 아이템 클릭 시, 커스텀 이벤트 메소드를 호출하는 코드를 작성..
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


     // 4. 마지막으로, 액티비티(or 프래그먼트)에서 커스텀 리스너 객체를 생성하여 어댑터에 전달



}
