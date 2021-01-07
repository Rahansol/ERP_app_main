package app.erp.com.erp_app.gtv_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.Gtv_Error_Install_Activity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Gtv_Report_Vo;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragement_Gtv_Total_List extends Fragment {

    TableLayout table_gtv_body;
    private Retrofit retrofit;
    Context context;
    String gtv_day ;
    ProgressDialog progressDialog;

    public Fragement_Gtv_Total_List() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gtv_total_list, container ,false);
        table_gtv_body = (TableLayout)view.findViewById(R.id.table_gtv_body);
        context = getActivity();
        Bundle args = getArguments() ;
        gtv_day = args.getString("gtv_day");

        ((Gtv_Error_Install_Activity)getActivity()).getSupportActionBar().setTitle("GTV 가동율");

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5,TimeUnit.MINUTES)
                .writeTimeout(5,TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.test_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
        Call<List<Gtv_Report_Vo>> call_gtv = erp.app_gtv_error_list(gtv_day);
        new Fragement_Gtv_Total_List.App_Gtv_Error_List().execute(call_gtv);

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("조회중..");
        progressDialog.show();

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
        table_gtv_body.removeAllViews();
        int tableLength = list.size();
        for(int i = 0 ; i < tableLength; i++){
            final TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0,2f  ));
            if(list.get(i).getRnum().equals("3")){
                String tag = list.get(i).getEmp_name()+","+list.get(i).getBusoff_name();
                tableRow.setTag(tag);
                tableRow.setBackgroundResource(R.drawable.table_border3);
            }else if(list.get(i).getRnum().equals("2")){
                String tag = "trp"+list.get(i).getEmp_name();
                tableRow.setId(i);
                tableRow.setTag(tag);
                tableRow.setBackgroundResource(R.drawable.table_border2);
            }else{
                tableRow.setTag("fix");
            }

            for(int j = 0 ; j < 6; j++){
                final TextView text = new TextView(context);
                switch (j){
                    case 0:
                        text.setTag("trp"+list.get(i).getEmp_name());
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0 , 80,1f);
                        lp.setMargins(0,10,0,10);
                        text.setLayoutParams(lp);
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        if(list.get(i).getRnum().equals("2")){
                            text.setText("+");
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String tr_tag = v.getTag().toString();
                                    TableRow tr = null;
                                    String emp_name = v.getTag().toString().substring(3,v.getTag().toString().length());
                                    if(tr_tag.substring(2,3).equals("p")){
                                        for(int i = 0 ; i < table_gtv_body.getChildCount(); i++){
                                            tr = (TableRow)table_gtv_body.getChildAt(i);
                                            String tr_tag_split [] = tr.getTag().toString().split(",");
                                            if(emp_name.equals(tr_tag_split[0])){
                                                tr.setVisibility(View.VISIBLE);
                                                table_gtv_body.invalidate();
                                                table_gtv_body.refreshDrawableState();
                                            }
                                        }
                                        v.setTag("trm"+emp_name);
                                    }else{
                                        for(int i = 0 ; i < table_gtv_body.getChildCount(); i++){
                                            tr = (TableRow)table_gtv_body.getChildAt(i);
                                            String tr_tag_split [] = tr.getTag().toString().split(",");
                                            if(emp_name.equals(tr_tag_split[0])){
                                                tr.setVisibility(View.GONE);
                                                table_gtv_body.invalidate();
                                                table_gtv_body.refreshDrawableState();
                                            }
                                        }
                                        v.setTag("trp"+emp_name);
                                    }
                                }
                            });
                        }
                        tableRow.addView(text);
                        break;
                    case 1 :
                        text.setText(list.get(i).getEmp_name());                                                                   // 담당자 이름???
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,2f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        tableRow.addView(text);
                        break;
                    case 2 :
                        text.setText(list.get(i).getBusoff_name());                                                               // 운수사명??
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,3f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        tableRow.addView(text);
                        break;
                    case 3 :
                        String tag = list.get(i).getEmp_name()+","+list.get(i).getBusoff_name()+","+list.get(i).getRoute_num();  //설치차량수 ???
                        text.setTag(tag);

                        text.setText(list.get(i).getTot_cnt());     // total 설치차량수
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,2f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        if(list.get(i).getRnum().equals("3")){
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String emp_office_split [] = v.getTag().toString().split(",");
                                    Fragment fr = new Fragement_Gtv_Error_Detail();                      //설치차량수를 클릭하면 노선, 차량번호, 등록 상태가 나오는데.. 이것 바꿔주기.
                                    Bundle bundle = new Bundle();                                           //Fragement_Gtv_Install_Detail() 를 ===>   Fragement_Gtv_Install_Detail() 로...

                                    bundle.putString("gtv_day",gtv_day);
                                    bundle.putString("emp_name",emp_office_split[0]);
                                    bundle.putString("busoff_name",emp_office_split[1]);

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
                    case 4 :
                        String tag_4 = list.get(i).getEmp_name()+","+list.get(i).getBusoff_name();
                        text.setTag(tag_4);

                        text.setText(list.get(i).getErr_cnt());                                          //점검대상???
                        text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT,2f));
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                        text.setTextColor(getResources().getColor(R.color.textBlack));
                        text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        if(list.get(i).getRnum().equals("3")){
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String emp_office_split [] = v.getTag().toString().split(",");
                                    Fragment fr = new Fragement_Gtv_Error_Detail();                       // [노선, 차량번호, DRV, LTE, MST, SLV 프래그먼트]로 이동....
                                    Bundle bundle = new Bundle();

                                    bundle.putString("gtv_day",gtv_day);
                                    bundle.putString("emp_name",emp_office_split[0]);
                                    bundle.putString("busoff_name",emp_office_split[1]);

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
                    case 5 :
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
            if(list.get(i).getRnum().equals("3")){
                tableRow.setVisibility(View.GONE);
            }
            table_gtv_body.addView(tableRow);
        }
        progressDialog.dismiss();
    }
}
