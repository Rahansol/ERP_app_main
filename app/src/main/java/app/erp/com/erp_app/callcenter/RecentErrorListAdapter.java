package app.erp.com.erp_app.callcenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;

public class RecentErrorListAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<RecentErrorListItems> items;

    public RecentErrorListAdapter() {
    }

    public RecentErrorListAdapter(Context context, ArrayList<RecentErrorListItems> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recyclerview_recent_error_list_dialog, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VH vh= (VH)viewHolder;
        RecentErrorListItems item= items.get(position);
        vh.reg_date.setText(item.reg_date);
        vh.reg_emp_name.setText(item.reg_emp_name);
        vh.unit_before_id.setText(item.unit_before_id);
        vh.care_date.setText(item.care_date);
        vh.care_emp_name.setText(item.care_emp_name);
        vh.unit_after_id.setText(item.unit_after_id);
        vh.busoff_name.setText(item.busoff_name);
        vh.route_num.setText(item.route_num);
        vh.unit_name.setText(item.unit_name);
        vh.trouble_high_name.setText(item.trouble_high_name);
        vh.trouble_low_name.setText(item.trouble_low_name);
        vh.trouble_care_name.setText(item.trouble_care_name);
        vh.notice.setText(item.notice);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class VH extends RecyclerView.ViewHolder{
        TextView reg_date;
        TextView reg_emp_name;
        TextView unit_before_id;
        TextView care_date;
        TextView care_emp_name;
        TextView unit_after_id;
        TextView busoff_name;
        TextView garage_id;
        TextView route_num;
        TextView unit_name;
        TextView trouble_high_name;
        TextView trouble_low_name;
        TextView trouble_care_name;
        TextView notice;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.reg_date= itemView.findViewById(R.id.reg_date);
            this.reg_emp_name= itemView.findViewById(R.id.reg_emp_name);
            this.unit_before_id= itemView.findViewById(R.id.unit_before_id);
            this.care_date= itemView.findViewById(R.id.care_date);
            this.care_emp_name= itemView.findViewById(R.id.care_emp_name);
            this.unit_after_id= itemView.findViewById(R.id.unit_after_id);
            this.busoff_name= itemView.findViewById(R.id.busoff_name);
            this.garage_id= itemView.findViewById(R.id.garage_id);
            this.route_num= itemView.findViewById(R.id.route_num);
            this.unit_name= itemView.findViewById(R.id.unit_name);
            this.trouble_high_name= itemView.findViewById(R.id.trouble_high_name);
            this.trouble_low_name= itemView.findViewById(R.id.trouble_low_name);
            this.trouble_care_name= itemView.findViewById(R.id.trouble_care_name);
            this.notice= itemView.findViewById(R.id.notice);
        }
    }
}
