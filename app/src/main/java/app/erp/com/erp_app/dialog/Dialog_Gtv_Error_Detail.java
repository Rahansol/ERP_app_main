package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.vo.Gtv_Report_Vo;

public class Dialog_Gtv_Error_Detail extends Dialog {

    private Context mcontext;
    private List<Gtv_Report_Vo> data_list;
    private Button ok_btn;
    TextView call_bus_num , call_office_name , call_route_id , call_error_type , call_tell_num ;
    private View.OnClickListener ok_Click;


    public Dialog_Gtv_Error_Detail(Context context, List<Gtv_Report_Vo> list, View.OnClickListener okClick){
        super(context);
        mcontext = context;
        data_list = list;
        ok_Click = okClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
