package app.erp.com.erp_app.gtv_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.Gtv_Error_Install_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Gtv_Report_Vo;
import retrofit2.Call;
import retrofit2.Response;

public class Fragement_Gtv_Error_Detail extends Fragment {

    TableLayout table_gtv_body_emp;
    Context context;
    ProgressDialog progressDialog;

    public Fragement_Gtv_Error_Detail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gtv_error_detail, container ,false);
        context = getActivity();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("조회중..");
        progressDialog.show();

        table_gtv_body_emp = (TableLayout)view.findViewById(R.id.table_gtv_body_error);
        Bundle args = getArguments() ;

        ((Gtv_Error_Install_Activity)getActivity()).getSupportActionBar().setTitle("GTV 조치대상");

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Gtv_Report_Vo>> call_gtv = erp.app_gtv_detail_error_list(args.getString("gtv_day") , args.getString("busoff_name") , args.getString("emp_name"));
        new Fragement_Gtv_Error_Detail.App_Gtv_Error_Detail_List().execute(call_gtv);

        TextView tv_busoff_name= view.findViewById(R.id.tv_busoff_name);
        tv_busoff_name.setText(args.getString("busoff_name"));

        return view;
    }

    private class App_Gtv_Error_Detail_List extends AsyncTask<Call,Void, List<Gtv_Report_Vo>> {
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

            for(int j = 0 ; j < 6; j++){
                final TextView text = new TextView(context);
                switch (j){
                    case 0:
                        text.setText(list.get(i).getRoute_num());                                       //노선번호
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0 , 80,2f);
                        lp.setMargins(0,10,0,10);
                        text.setLayoutParams(lp);
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        tableRow.addView(text);
                        break;
                    case 1 :
                        text.setText(list.get(i).getBus_num());                                        //차량번호
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,3f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        tableRow.addView(text);
                        break;
                    case 2 :
                        text.setText(list.get(i).getDoor_cnt());                                          // DOOR_CNT (도어)
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,1f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        if(i%2 == 0){
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder));
                        }else{
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder2));
                        }
                        tableRow.addView(text);
                        break;
                    case 3 :
                        text.setText(list.get(i).getSettop_yn());                                          // SETTOP_YN (셋탑설치)
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,1f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        if(i%2 == 0){
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder));
                        }else{
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder2));
                        }
                        tableRow.addView(text);
                        break;
                    case 4 :
                        text.setText(list.get(i).getDrive());                                          // DRV (drive)
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,1f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        if(i%2 == 0){
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder));
                        }else{
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder2));
                        }
                        tableRow.addView(text);
                        break;
                    /*case 3 :
                        text.setText(list.get(i).getLte());                                           // LTE
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,1f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        text.setBackground(getResources().getDrawable(R.drawable.table_text_boder));
                        if(i%2 == 0){
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder));
                        }else{
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder2));
                        }
                        tableRow.addView(text);
                        break;
                    case 4 :                                                                        //  MST (Master)
                        text.setText(list.get(i).getMaster());
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,1f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        if(i%2 == 0){
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder));
                        }else{
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder2));
                        }
                        tableRow.addView(text);
                        break;*/
                    case 5 :
                        text.setText(list.get(i).getSlave());                                       // SLV (Slave)
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,1f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        if(i%2 == 0){
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder));
                        }else{
                            text.setBackground(getResources().getDrawable(R.drawable.table_text_boder2));
                        }
                        tableRow.addView(text);
                        break;
                }
            }
            table_gtv_body_emp.addView(tableRow);
        }
        progressDialog.dismiss();
    }
}
