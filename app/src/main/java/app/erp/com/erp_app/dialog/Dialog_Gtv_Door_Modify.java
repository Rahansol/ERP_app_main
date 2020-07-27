package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Gtv_Report_Vo;

public class Dialog_Gtv_Door_Modify extends Dialog {

    private Context mcontext;
    private HashMap<String, Object> data_map, return_map;
    private Button gtv_modify_cancle_btn, gtv_modify_insert_btn;
    TextView dialog_office_name , dialog_route_num , dialog_bus_num , dialog_now_status;
    private View.OnClickListener cancle_listener , insert_listener;
    Spinner bus_num_spinner;


    public Dialog_Gtv_Door_Modify(Context context, HashMap<String,Object> list, View.OnClickListener cancle_Click, View.OnClickListener inset_Click){
        super(context);
        mcontext = context;
        data_map = list;
        cancle_listener = cancle_Click;
        insert_listener = inset_Click;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gtv_door_modify);

        return_map = new HashMap<>();

        dialog_office_name = (TextView)findViewById(R.id.dialog_office_name);
        dialog_route_num = (TextView)findViewById(R.id.dialog_route_num);
        dialog_bus_num = (TextView)findViewById(R.id.dialog_bus_num);
        dialog_now_status = (TextView)findViewById(R.id.dialog_now_status);

        dialog_office_name.setText(data_map.get("busoff_name").toString());
        dialog_route_num.setText(data_map.get("route_num").toString());
        dialog_bus_num.setText(data_map.get("bus_num").toString());
        dialog_now_status.setText(data_map.get("bus_door").toString());

        return_map.put("trans_id",data_map.get("trans_bizd_id").toString());
        return_map.put("busoff_name",data_map.get("busoff_name").toString());
        return_map.put("bus_id",data_map.get("bus_id").toString());
        return_map.put("bus_num",data_map.get("bus_num").toString());
        return_map.put("now_door",data_map.get("bus_door").toString());

        gtv_modify_cancle_btn = (Button)findViewById(R.id.gtv_modify_cancle_btn);
        gtv_modify_insert_btn = (Button)findViewById(R.id.gtv_modify_insert_btn);

        gtv_modify_cancle_btn.setOnClickListener(cancle_listener);
        gtv_modify_insert_btn.setOnClickListener(insert_listener);

        bus_num_spinner = (Spinner)findViewById(R.id.bus_num_spinner);
        bus_num_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test",":::"+position);
                switch (position){
                    case 1 :
                        return_map.put("change_door","1DOOR");
                        break;
                    case 2:
                        return_map.put("change_door","2DOOR");
                        break;
                    case 3:
                        return_map.put("change_door","미운행");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public HashMap<String,Object> return_change_info (){
        return return_map;
    }
}
