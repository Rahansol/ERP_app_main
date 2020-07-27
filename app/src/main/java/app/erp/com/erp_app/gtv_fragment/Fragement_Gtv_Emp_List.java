package app.erp.com.erp_app.gtv_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

public class Fragement_Gtv_Emp_List extends Fragment {

    TableLayout table_gtv_body_emp;
    Context context;
    String gtv_day, emp_name;
    ProgressDialog progressDialog;

    public Fragement_Gtv_Emp_List() {
    }
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gtv_emp_list, container ,false);
        context = getActivity();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("조회중..");
        progressDialog.show();

        table_gtv_body_emp = (TableLayout)view.findViewById(R.id.table_gtv_body_emp);
        Bundle args = getArguments() ;

        gtv_day = args.getString("gtv_day");
        emp_name = args.getString("emp_name");

        ((Gtv_Error_Install_Activity)getActivity()).getSupportActionBar().setTitle("GTV 담당자별 현황");

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Gtv_Report_Vo>> call_gtv = erp.app_gtv_oiffce_emp_list( gtv_day, emp_name);
        new Fragement_Gtv_Emp_List.App_Gtv_Error_List().execute(call_gtv);

        return view;
    }

    private class App_Gtv_Error_List extends AsyncTask<Call,Void, List<Gtv_Report_Vo>> {
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
            tableRow.setTag(list.get(i).getEmp_name()+","+list.get(i).getBusoff_name());
            tableRow.setBackgroundResource(R.drawable.table_border3);

            for(int j = 0 ; j < 4; j++){
                final TextView text = new TextView(context);
                switch (j){
                    case 0:
                        text.setText(list.get(i).getBusoff_name());
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0 , 80,3f);
                        lp.setMargins(0,10,0,10);
                        text.setLayoutParams(lp);
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        tableRow.addView(text);
                        break;
                    case 1 :
                        text.setTag(list.get(i).getBusoff_name());
                        text.setText(list.get(i).getTot_cnt());
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,2f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        if(!list.get(i).getBusoff_name().equals("계")){
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String busoff_name = v.getTag().toString();
                                    Fragment fr = new Fragement_Gtv_Install_Detail();
                                    Bundle bundle = new Bundle();

                                    bundle.putString("gtv_day",gtv_day);
                                    bundle.putString("emp_name",emp_name);
                                    bundle.putString("busoff_name",busoff_name);

                                    fr.setArguments(bundle);
                                    FragmentManager fm_all = getFragmentManager();
                                    FragmentTransaction fragmentTransaction_all = fm_all.beginTransaction();
                                    fragmentTransaction_all.replace(R.id.gtv_fragment,fr);
                                    fragmentTransaction_all.addToBackStack(null);
                                    fragmentTransaction_all.commit();
                                }
                            });
                        }
                        tableRow.addView(text);
                        break;
                    case 2 :
                        text.setTag(list.get(i).getBusoff_name());
                        text.setText(list.get(i).getErr_cnt());
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,2f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        if(!list.get(i).getBusoff_name().equals("계")){
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String busoff_name = v.getTag().toString();
                                    Fragment fr = new Fragement_Gtv_Error_Detail();
                                    Bundle bundle = new Bundle();

                                    bundle.putString("gtv_day",gtv_day);
                                    bundle.putString("emp_name",emp_name);
                                    bundle.putString("busoff_name",busoff_name);

                                    fr.setArguments(bundle);
                                    FragmentManager fm_all = getFragmentManager();
                                    FragmentTransaction fragmentTransaction_all = fm_all.beginTransaction();
                                    fragmentTransaction_all.replace(R.id.gtv_fragment,fr);
                                    fragmentTransaction_all.addToBackStack(null);
                                    fragmentTransaction_all.commit();
                                }
                            });
                        }
                        tableRow.addView(text);
                        break;
                    case 3 :
                        text.setText(list.get(i).getErr_rate());
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,2f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        tableRow.addView(text);
                        break;

                }
            }
            table_gtv_body_emp.addView(tableRow);
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("tag ",":"+ v.getTag());
                }
            });
        }
        progressDialog.dismiss();
    }
}
