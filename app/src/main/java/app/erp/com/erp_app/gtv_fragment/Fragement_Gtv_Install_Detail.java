package app.erp.com.erp_app.gtv_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.Gtv_Error_Install_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.dialog.Dialog_Gtv_Door_Modify;
import app.erp.com.erp_app.vo.Gtv_Report_Vo;
import retrofit2.Call;
import retrofit2.Response;

public class Fragement_Gtv_Install_Detail extends Fragment {

    TableLayout table_gtv_body_emp;
    Context context;
    Dialog_Gtv_Door_Modify dgdm;
    ProgressDialog progressDialog;
    TextView title_text;
    HashMap<String , Object> dialog_map;

    public Fragement_Gtv_Install_Detail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gtv_detail, container ,false);
        table_gtv_body_emp = (TableLayout)view.findViewById(R.id.table_gtv_body_bus);
        context = getActivity();
        Bundle args = getArguments() ;
        dialog_map = new HashMap<>();
        dialog_map.put("busoff_name",args.getString("busoff_name"));

        title_text = (TextView)view.findViewById(R.id.title_text);
        title_text.setText(args.getString("busoff_name")+ " 설치 현황");

        ((Gtv_Error_Install_Activity)getActivity()).getSupportActionBar().setTitle("GTV 설치현황");

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Gtv_Report_Vo>> call_gtv = erp.app_gtv_detail_list(args.getString("gtv_day") , args.getString("busoff_name") , args.getString("emp_name"));
        new Fragement_Gtv_Install_Detail.App_Gtv_Detail_List().execute(call_gtv);

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("조회중..");
        progressDialog.show();

        return view;
    }

    private class App_Gtv_Detail_List extends AsyncTask<Call,Void, List<Gtv_Report_Vo>> {
        @Override
        protected List<Gtv_Report_Vo> doInBackground(Call... calls) {
            try {
                Call<List<Gtv_Report_Vo>> call = calls[0];
                Response<List<Gtv_Report_Vo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            progressDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(List<Gtv_Report_Vo> gtv_report_vos) {
            try {
                MakeGtv_Table(gtv_report_vos);
            }catch (Exception e){
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }
    }

    private class App_gtv_modify_insert extends AsyncTask<Call , Void , Boolean>{
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Toast.makeText(context,"GTV 도어 변경요청 완료 !",Toast.LENGTH_SHORT);
        }
    }

    private void MakeGtv_Table(List<Gtv_Report_Vo> list){
        table_gtv_body_emp.removeAllViews();
        int tableLength = list.size();
        for(int i = 0 ; i < tableLength; i++){
            final TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT  ));
            if(i%2 == 0){
                tableRow.setBackgroundResource(R.drawable.table_border3);
            }else{
                tableRow.setBackgroundResource(R.drawable.table_border2);
            }

            for(int j = 0 ; j < 4; j++){
                final TextView text = new TextView(context);
                switch (j){
                    case 0:
                        text.setText(list.get(i).getRoute_num());
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0 , 80,2f);
                        lp.setMargins(0,10,0,10);
                        text.setLayoutParams(lp);
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        tableRow.addView(text);
                        break;
                    case 1 :
                        text.setTag(list.get(i).getRoute_num()+","+list.get(i).getBus_num()+","+list.get(i).getDoor()+","+list.get(i).getTransp_bizr_id()+","+list.get(i).getBus_id());
                        Log.d("col2",list.get(i).getRoute_num()+","+list.get(i).getBus_num()+","+list.get(i).getDoor());
                        text.setText(list.get(i).getBus_num());
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,4f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
//                        text.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String bus_info[] = v.getTag().toString().split(",");
//                                dialog_map.put("route_num",bus_info[0]);
//                                dialog_map.put("bus_num",bus_info[1]);
//                                dialog_map.put("bus_door",bus_info[2]);
//                                dialog_map.put("trans_bizd_id",bus_info[3]);
//                                dialog_map.put("bus_id",bus_info[4]);
//                                dgdm = new Dialog_Gtv_Door_Modify(context,dialog_map,cancle_btn_listener,insert_btn_listener);
//
//                                dgdm.setCancelable(false);
//                                dgdm.getWindow().setGravity(Gravity.CENTER);
//                                dgdm.show();
//                            }
//                        });
                        tableRow.addView(text);
                        break;
                    case 2 :
                        text.setText(list.get(i).getDoor());
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,2f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        tableRow.addView(text);
                        break;
                }
            }
            table_gtv_body_emp.addView(tableRow);
        }
        progressDialog.dismiss();
    }

    private View.OnClickListener cancle_btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dgdm.dismiss();
        }
    };

    private View.OnClickListener insert_btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HashMap<String,Object> map = new HashMap<>();
            map = dgdm.return_change_info();
            ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call_gtv = erp.app_insert_gtv_modify_door(map.get("trans_id").toString(),map.get("busoff_name").toString(),map.get("bus_id").toString()
                    ,map.get("bus_num").toString(),map.get("now_door").toString(),map.get("change_door").toString());
            new Fragement_Gtv_Install_Detail.App_gtv_modify_insert().execute(call_gtv);
            dgdm.dismiss();
        }
    };
}
