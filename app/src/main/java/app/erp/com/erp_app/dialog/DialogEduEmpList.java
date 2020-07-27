package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import app.erp.com.erp_app.adapter.Edu_Emp_PC_Adapter;
import app.erp.com.erp_app.callcenter.Fragment_trouble_insert_edu;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import app.erp.com.erp_app.vo.User_InfoVo;
import retrofit2.Call;
import retrofit2.Response;

public class DialogEduEmpList extends Dialog  {

    private Context mContext;
    private List<Edu_Emp_Vo> gb_list = new ArrayList<>();
    private List<Edu_Emp_Vo> gn_list = new ArrayList<>();
    private List<Edu_Emp_Vo> ic_list = new ArrayList<>();
    private List<Edu_Emp_Vo> pc_list = new ArrayList<>();

    private TextView btn_ok;

    ListView listView,edu_emp_view2, edu_emp_view3, edu_emp_view4;
    Edu_Emp_GB_Adapter eea;
    Edu_Emp_GN_Adapter eena;
    Edu_Emp_IC_Adapter eeia;
    Edu_Emp_PC_Adapter eepca;
    private View.OnClickListener ok_btn_listener;

    public DialogEduEmpList(Context context, List<Edu_Emp_Vo> gb, List<Edu_Emp_Vo> gn, List<Edu_Emp_Vo> ic, List<Edu_Emp_Vo> pc , View.OnClickListener clickListener) {
        super(context);
        mContext = context;
        gb_list = gb;
        gn_list = gn;
        ic_list = ic;
        pc_list = pc;
        ok_btn_listener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edu_emp_list);

        btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(ok_btn_listener);

        //어뎁터
        eea = new Edu_Emp_GB_Adapter();
        eena = new Edu_Emp_GN_Adapter();
        eeia = new Edu_Emp_IC_Adapter();
        eepca = new Edu_Emp_PC_Adapter();

        listView = (ListView)findViewById(R.id.edu_emp_view);
        edu_emp_view2 = (ListView)findViewById(R.id.edu_emp_view2);
        edu_emp_view3 = (ListView)findViewById(R.id.edu_emp_view3);
        edu_emp_view4 = (ListView)findViewById(R.id.edu_emp_view4);

        listView.setAdapter(eea);
        edu_emp_view2.setAdapter(eena);
        edu_emp_view3.setAdapter(eeia);
        edu_emp_view4.setAdapter(eepca);

        for(int i = 0; i < gb_list.size(); i++){
            if(gb_list.get(i).isCheck_val()){
                listView.setItemChecked(i,true);
            }
            eea.addItem(gb_list.get(i));
        }
        for(int i = 0; i < gn_list.size(); i++){
            if(gn_list.get(i).isCheck_val()){
                edu_emp_view2.setItemChecked(i,true);
            }
            eena.addItem(gn_list.get(i));
        }
        for(int i = 0; i < ic_list.size(); i++){
            if(ic_list.get(i).isCheck_val()){
                edu_emp_view3.setItemChecked(i,true);
            }
            eeia.addItem(ic_list.get(i));
        }
        for(int i = 0; i < pc_list.size(); i++){
            if(pc_list.get(i).isCheck_val()){
                edu_emp_view4.setItemChecked(i,true);
            }
            eepca.addItem(pc_list.get(i));
        }
    }

    public Map<String, Object> return_list(){
        Map<String, Object> map = new HashMap<>();
        int check_count = 0;
        List<String> care_emp_id = new ArrayList<>();
        for(int i = 0 ; i < gb_list.size(); i++){
            gb_list.get(i).setCheck_val(listView.isItemChecked(i));
            if(listView.isItemChecked(i)){
                check_count++;
                care_emp_id.add(gb_list.get(i).getEmp_id());
            }
        }
        for(int i = 0 ; i < gn_list.size(); i++){
            gn_list.get(i).setCheck_val(edu_emp_view2.isItemChecked(i));
            if(edu_emp_view2.isItemChecked(i)){
                check_count++;
                care_emp_id.add(gn_list.get(i).getEmp_id());
            }
        }
        for(int i = 0 ; i < ic_list.size(); i++){
            ic_list.get(i).setCheck_val(edu_emp_view3.isItemChecked(i));
            if(edu_emp_view3.isItemChecked(i)){
                check_count++;
                care_emp_id.add(ic_list.get(i).getEmp_id());
            }
        }
        for(int i = 0 ; i < pc_list.size(); i++){
            pc_list.get(i).setCheck_val(edu_emp_view4.isItemChecked(i));
            if(edu_emp_view4.isItemChecked(i)){
                check_count++;
                care_emp_id.add(pc_list.get(i).getEmp_id());
            }
        }
        map.put("gb_list" , gb_list);
        map.put("gn_list" , gn_list);
        map.put("ic_list" , ic_list);
        map.put("pc_list" , pc_list);
        map.put("care_emp_id" , care_emp_id);
        map.put("check_count" , check_count);
        return map;
    }
}
