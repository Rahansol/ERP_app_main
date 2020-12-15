package app.erp.com.erp_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Response;

public class DialogFinalRequestListActivity extends AppCompatActivity {
    public ArrayList<FinalStockListItem> finalStockListItems;
    public FinalListItemAdapter finalListItemAdapter;
    public RecyclerView recyclerview_final_list_dialog;

    public TextView tv_ok_dialog;
    static String req_emp_id;
    public Context context;
    SharedPreferences pref;
    private String emp_id;

    /*재고리스 어댑터에서 intent를 통해 보낸 데이터*/
    private String empId;
    private String barcodeDepId;

    static String barcodeDepIdValueOut;
    static String requestDepIdOut;
    static String currentDateValueOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_final_request_list);

        pref= getSharedPreferences("sp_barcode_dep_id_out", MODE_PRIVATE);
        barcodeDepIdValueOut= pref.getString("barcodeDepIdValueOut", "");
        Log.d("barcodeDepIdValueOut ========>>>>>" ,barcodeDepIdValueOut+"  0_0");

        pref= getSharedPreferences("sp_request_dep_id_out", MODE_PRIVATE);
        requestDepIdOut= pref.getString("requestDepIdOut", "");
        Log.d("barcodeDepIdValueOut ========>>>>>" ,barcodeDepIdValueOut+"  0_0");

        pref= getSharedPreferences("sp_current_date_out", MODE_PRIVATE);
        currentDateValueOut= pref.getString("currentDateValueOut", "");
        Log.d("barcodeDepIdValueOut ========>>>>>" ,barcodeDepIdValueOut+"  0_0");


        req_emp_id= "msookim";
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> call= erp.FinalBookingChk(req_emp_id, "");  // 출고는??
        new Final_booking_check().execute(call);

        tv_ok_dialog= findViewById(R.id.tv_ok_dialog);
        tv_ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogFinalRequestListActivity.this, "출고대상을 확인하였습니다.", Toast.LENGTH_SHORT).show();
                DialogFinalRequestListActivity.this.finish();
            }
        });
    }//onCreate....


    /*출고대상 최종목록 확인하기 버튼*/
    public class Final_booking_check extends AsyncTask<Call, Void, List<TestAllVO>> {

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);


            finalStockListItems = new ArrayList<>();
            for (int i=0; i<testAllVOS.size(); i++){
                finalStockListItems.add(new FinalStockListItem(testAllVOS.get(i).getUn_yn()
                                                               ,testAllVOS.get(i).getBarcode_dep_id()
                                                               ,req_emp_id
                                                               ,barcodeDepIdValueOut
                                                                ,currentDateValueOut
                                                                ,testAllVOS.get(i).getNotice()
                                                                ,testAllVOS.get(i).getRequest_dep_id()
                                                                ,testAllVOS.get(i).getResponse_dep_id()
                                                               ,testAllVOS.get(i).getUnit_ver()
                                                               ,testAllVOS.get(i).getUnit_id()
                                                               ,testAllVOS.get(i).getUnit_code()
                                                               ,testAllVOS.get(i).getRep_unit_code()
                                                               ,true));
            }
            finalListItemAdapter= new FinalListItemAdapter(DialogFinalRequestListActivity.this, finalStockListItems);
            recyclerview_final_list_dialog= findViewById(R.id.recyclerview_final_list_dialog);
            recyclerview_final_list_dialog.setAdapter(finalListItemAdapter);
            finalListItemAdapter.notifyDataSetChanged();
        }
    }

}



