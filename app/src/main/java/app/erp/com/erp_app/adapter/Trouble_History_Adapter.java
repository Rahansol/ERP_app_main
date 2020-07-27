package app.erp.com.erp_app.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;


/**
 * Created by hsra on 2019-06-24.
 */

public class Trouble_History_Adapter extends RecyclerView.Adapter<Trouble_History_Adapter.ViewHolder> {

    private ArrayList<Trouble_HistoryListVO> mData = new ArrayList<>();
    private Context mContext;

    private SparseBooleanArray selectItems = new SparseBooleanArray();
    private int prePosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout trouble_simple_layout;
        LinearLayout trouble_detail_layout;
        LinearLayout history_full_layout;

        TextView t_unit_name;
        TextView t_bus_num;
        TextView t_care_emp;
        TextView t_busoff_name;
        TextView t_area_name;
        TextView t_reg_date;
        TextView t_reg_date2;
        TextView t_care_date;
        TextView t_unit_name2;
        TextView t_high_code_name;
        TextView t_low_code_name;
        TextView t_t_care_name;
        TextView t_notice;
        TextView t_bf_id;
        TextView b_af_id;
        TextView t_care_reg_man;

        private Trouble_HistoryListVO data;
        private int postion;


        ViewHolder(View itemView){
            super(itemView);
            trouble_simple_layout = itemView.findViewById(R.id.trouble_simple_layout);
            trouble_detail_layout = itemView.findViewById(R.id.trouble_detail_layout);
            history_full_layout = itemView.findViewById(R.id.history_full_layout);

            t_unit_name = itemView.findViewById(R.id.t_unit_name);
            t_bus_num = itemView.findViewById(R.id.t_bus_num);
            t_care_emp = itemView.findViewById(R.id.t_care_emp);
            t_busoff_name = itemView.findViewById(R.id.t_busoff_name);
            t_area_name = itemView.findViewById(R.id.t_area_name);
            t_reg_date = itemView.findViewById(R.id.t_reg_date);
            t_reg_date2 = itemView.findViewById(R.id.t_reg_date2);
            t_care_date = itemView.findViewById(R.id.t_care_date);
            t_unit_name2 = itemView.findViewById(R.id.t_unit_name2);
            t_high_code_name = itemView.findViewById(R.id.t_high_code_name);
            t_low_code_name = itemView.findViewById(R.id.t_low_code_name);
            t_t_care_name = itemView.findViewById(R.id.t_t_care_name);
            t_notice = itemView.findViewById(R.id.t_notice);
            t_bf_id = itemView.findViewById(R.id.t_bf_id);
            b_af_id = itemView.findViewById(R.id.b_af_id);
            t_care_reg_man = itemView.findViewById(R.id.t_care_reg_man);

        }

        void onBind(Trouble_HistoryListVO datas , int pos){
            this.data = datas;
            this.postion = pos;

            t_unit_name.setText(data.getUnit_name());
            t_bus_num.setText(data.getBus_num());
            t_care_emp.setText(data.getEmp_name());
            t_busoff_name.setText(data.getBusoff_name());
            t_area_name.setText(data.getOffice_group());
            t_reg_date.setText(data.getReg_date().substring(0,10));
            t_reg_date2.setText(data.getReg_date());
            t_care_date.setText(data.getCare_date());
            t_unit_name2.setText(data.getUnit_name());
            t_high_code_name.setText(data.getTrouble_high_name());
            t_low_code_name.setText(data.getTrouble_low_name());
            t_t_care_name.setText(data.getTrouble_care_name());
            t_notice.setText(data.getNotice());
            t_bf_id.setText(data.getUnit_before_id());
            b_af_id.setText(data.getUnit_after_id());
            t_care_reg_man.setText(data.getEmp_name());

            t_unit_name.setIncludeFontPadding(false);
            t_bus_num.setIncludeFontPadding(false);
            t_care_emp.setIncludeFontPadding(false);
            t_busoff_name.setIncludeFontPadding(false);
            t_area_name.setIncludeFontPadding(false);
            t_reg_date.setIncludeFontPadding(false);
            t_reg_date2.setIncludeFontPadding(false);
            t_care_date.setIncludeFontPadding(false);
            t_unit_name2.setIncludeFontPadding(false);
            t_high_code_name.setIncludeFontPadding(false);
            t_low_code_name.setIncludeFontPadding(false);
            t_t_care_name.setIncludeFontPadding(false);
            t_notice.setIncludeFontPadding(false);
            t_notice.setMovementMethod(new ScrollingMovementMethod());
            t_bf_id.setIncludeFontPadding(false);
            b_af_id.setIncludeFontPadding(false);
            t_care_reg_man.setIncludeFontPadding(false);

            changeVisibility(selectItems.get(postion));
            trouble_simple_layout.setOnClickListener(this);

            t_notice.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true); return false; } });

            if(pos%2 ==0){
                history_full_layout.setBackground(mContext.getResources().getDrawable(R.color.contentDividerLine));
            }else{
                history_full_layout.setBackground(mContext.getResources().getDrawable(R.color.white));
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.trouble_simple_layout :
                    if (selectItems.get(postion)) {
                        // 펼쳐진 Item을 클릭 시
                        selectItems.delete(postion);
                    } else {
                        // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectItems.delete(prePosition);
                        // 클릭한 Item의 position을 저장
                        selectItems.put(postion, true);
                    }
                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(postion);
                    // 클릭된 position 저장
                    prePosition = postion;
                    break;
            }

        }

        private void changeVisibility(final boolean isExpanded) {

            trouble_detail_layout.measure(View.MeasureSpec.UNSPECIFIED , View.MeasureSpec.UNSPECIFIED );
            int height = trouble_detail_layout.getMeasuredHeight();

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    trouble_detail_layout.getLayoutParams().height = value;
                    trouble_detail_layout.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    trouble_detail_layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int postion) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.trouble_history_listview, viewGroup , false);
        Trouble_History_Adapter.ViewHolder vh  = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int postion) {
        viewHolder.onBind(mData.get(postion) , postion);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Trouble_HistoryListVO list){
        mData.add(list);
    }


}
