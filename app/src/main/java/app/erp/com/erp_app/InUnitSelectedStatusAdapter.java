package app.erp.com.erp_app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InUnitSelectedStatusAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<selectedStatusItems2> items;


    /*리사이클러뷰 외부(액티비티/ 프래그먼트)에서 아이템 클릭 이벤트 처리하기*/
    // 1. 커스텀 리스너 인터페이스 정의     (자식(어댑터)안에서 새로운 리스너 인터페이스 정의)
    public interface OnitemClickListener{
        void onItemClick(View v, int post);
    }

    // 2. 리스너 객체를 전달하는 메소드 (setOnItemClickListener()와 전달할 객체를 저장할 변수 mListener 추가)
    // 2.1 리스너 객체 참조를 저장하는 변수
    private OnitemClickListener mListener= null;

    // 2.2 OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메소드
    public void setmListener(OnitemClickListener mListener){
        this.mListener= mListener;
    }

    public InUnitSelectedStatusAdapter() {
    }

    public InUnitSelectedStatusAdapter(Context context, ArrayList<selectedStatusItems2> items) {
        this.context = context;
        this.items = items;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_selected_status_items, viewGroup, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH vh= (VH) viewHolder;
        selectedStatusItems2 item= items.get(i);
        vh.tvUnitName.setText(item.unitName);
        vh.tvUnitTotalNum.setText(item.unitTotalNum);

        vh.itemView.setTag(item.unitCode+"&"+item.repUnitCode+"&"+item.unitId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class VH extends RecyclerView.ViewHolder{

        TextView tvUnitName;
        TextView tvUnitTotalNum;

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvUnitName= itemView.findViewById(R.id.unit_name);
            this.tvUnitTotalNum= itemView.findViewById(R.id.unit_total_num);

            // 3. 아이템 클릭 이벤트 핸들러 메소드에서 리스너 객체 메소드(onItemClick) 호출
            //    뷰 홀더에서 아이템 클릭 시, 커스텀 이벤트 메소드를 호출하는 코드를 작성..
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos= getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }

                    /*선택현황 아이템을 클릭하고 스피너와 날짜값이 있는지 확인*/
                   /* WarehousingActivity ac= (WarehousingActivity) context;    //어댑터에서 액티비티 컨트롤하기

                    if (ac.tvShowDate==null){
                        ac.builder = new AlertDialog.Builder(context);
                        ac.builder.setTitle("요청날짜 정보부족").setMessage("출고신청 날짜선택을 완료하세요");
                        ac.builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = ac.builder.create();
                        alertDialog.show();
                    }else if (ac.selectedSpinnerAreaItems==null){
                        ac.builder= new AlertDialog.Builder(context);
                        ac.builder.setTitle("지부 정보부족").setMessage("지부 선택을 완료하세요");
                        ac.builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog= ac.builder.create();
                        alertDialog.show();
                    }else if (ac.selectedUnitGroup==null){
                        ac.builder = new AlertDialog.Builder(context);
                        ac.builder.setTitle("입고 정보부족").setMessage("입고 선택을 완료하세요");
                        ac.builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = ac.builder.create();
                        alertDialog.show();
                    }else {

                    }*/

                }
            });

        }

    }
}
