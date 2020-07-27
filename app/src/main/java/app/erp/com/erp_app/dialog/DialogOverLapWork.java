package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Edu_Emp_GB_Adapter;
import app.erp.com.erp_app.adapter.Edu_Emp_GN_Adapter;
import app.erp.com.erp_app.adapter.Edu_Emp_IC_Adapter;
import app.erp.com.erp_app.vo.Cash_Work_VO;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;

public class DialogOverLapWork extends Dialog  {

    private Context mContext;
    private TextView btn_ok;
    private List<Cash_Work_VO> list;

    TextView reg_data , over_lap_office_name , over_lap_route_num , over_lap_garage;
    private View.OnClickListener ok_btn_listener;

    public DialogOverLapWork(Context context, View.OnClickListener clickListener , List<Cash_Work_VO> cash_work_voList) {
        super(context);
        mContext = context;
        ok_btn_listener = clickListener;
        list = cash_work_voList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_over_lap_work);

        reg_data = (TextView)findViewById(R.id.reg_data);
        over_lap_office_name = (TextView)findViewById(R.id.over_lap_office_name);
        over_lap_route_num = (TextView)findViewById(R.id.over_lap_route_num);
        over_lap_garage = (TextView)findViewById(R.id.over_lap_garage);

        reg_data.setText(list.get(0).getReg_date()+" " +list.get(0).getEmp_name());
        over_lap_office_name.setText(list.get(0).getBusoff_name());
        over_lap_route_num.setText(list.get(0).getRoute_num());
        over_lap_garage.setText(list.get(0).getAddress());

        btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(ok_btn_listener);

    }
}
