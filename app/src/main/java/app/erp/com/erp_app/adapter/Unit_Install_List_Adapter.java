package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import app.erp.com.erp_app.vo.Unit_InstallVO;

public class Unit_Install_List_Adapter extends RecyclerView.Adapter<Unit_Install_List_Adapter.ViewHolder>{

    private ArrayList<Unit_InstallVO> mData = new ArrayList<>();
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Unit_InstallVO data;
        private int postion;

        TextView unit_name;
        TextView count_text;
        Button count_minus_btn;
        Button count_plus_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            unit_name = (TextView)itemView.findViewById(R.id.unit_name_textview);
            count_text = (TextView)itemView.findViewById(R.id.count_text);

            count_minus_btn = (Button)itemView.findViewById(R.id.count_minus_btn);
            count_plus_btn = (Button)itemView.findViewById(R.id.count_plus_btn);
        }

        void onbind(Unit_InstallVO datas, final int pos){
            this.data =datas;
            this.postion = pos;
            unit_name.setText(data.getItem_each_name());

            count_minus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(count_text.getText().toString());
                    count--;
                    if(count == -1) count =0;
                    count_text.setText(""+count);
                }
            });

            count_plus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(count_text.getText().toString());
                    count++;
                    if(count == -1) count =0;
                    count_text.setText(""+count);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.recyclerview_unit_install_list, viewGroup , false);
        Unit_Install_List_Adapter.ViewHolder vh  = new Unit_Install_List_Adapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.onbind(mData.get(i) , i);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Unit_InstallVO list){
        mData.add(list);
        notifyDataSetChanged();
    }
}
