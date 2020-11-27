package app.erp.com.erp_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogFinalRequestList extends AppCompatActivity {
    public ArrayList<FinalStockListItem> finalStockListItems;
    public FinalListItemAdapter finalListItemAdapter;
    public RecyclerView recyclerview_final_list_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_final_request_list);

        //인텐트로 받은 값 가져와서 넣어주기..
        Intent intent= getIntent();
        String unit_code= intent.getStringExtra("unitCode");
        String rep_unit_code= intent.getStringExtra("repUnitCode");
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> call= erp.FinalBookingChk(unit_code, rep_unit_code, "IMSI-IM_SI","IMSIDT","msookim");
        new Final_booking_check().execute(call);


    }//onCreate....


    /*최종목록 확인하기 버튼*/
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

            Log.d("dialog 확인",testAllVOS.size()+" ;;;;;;;;;;;");

            finalStockListItems = new ArrayList<>();
            for (int i=0; i<testAllVOS.size(); i++){
                finalStockListItems.add(new FinalStockListItem(testAllVOS.get(i).getUnit_ver(), testAllVOS.get(i).getUnit_id(), testAllVOS.get(i).getUnit_code(), testAllVOS.get(i).getRep_unit_code()));
            }
            finalListItemAdapter= new FinalListItemAdapter(DialogFinalRequestList.this, finalStockListItems);
            recyclerview_final_list_dialog= findViewById(R.id.recyclerview_final_list_dialog);
            recyclerview_final_list_dialog.setAdapter(finalListItemAdapter);
            finalListItemAdapter.notifyDataSetChanged();
        }
    }

}



