package app.erp.com.erp_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
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

public class DialogFinalCheckInActivity extends AppCompatActivity {
    /*다이얼로그 액티비티 리사이클러뷰*/
    private ArrayList<FinalStockListItem> finalStockListItems;
    private RecyclerView recyclerView;
    private InFinalListItemAdapter inFinalListItemAdapter;

    private TextView tv_ok_dialog_in;
    static String req_emp_id;
    Context mContext;

    static SharedPreferences pref;
    static String barcodeDepIdValue;
    static String responseDepId;
    static String currentDateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_final_check_in);

        /*WarehousingActivity 에서 SharedPreference 를 통해 저장된 값 불어오기*/
        pref= getSharedPreferences("sp_barcode_dep_id", MODE_PRIVATE);
        barcodeDepIdValue= pref.getString("barcodeDepIdValue", "");        //입고신청 화면에서 상단 스피너 지부값
        Log.d("barcodeDepIdValue", barcodeDepIdValue+"  0_0");   //  99000201  0_0

        pref= getSharedPreferences("sp_response_dep_id", MODE_PRIVATE);
        responseDepId= pref.getString("responseDepId","");
        Log.d("responseDepId", responseDepId+"  *_*");

        pref= getSharedPreferences("sp_current_date", MODE_PRIVATE);
        currentDateValue= pref.getString("currentDateValue","");
        Log.d("currentDateValue", currentDateValue+"  ! _ !");




        //여기가 다이얼로그 액티비티가 뜨는 곳
        req_emp_id="msookim";
        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<TestAllVO>> call= erp.In_FinalBookingChk(req_emp_id,"In");   //un_yn은 YY로??
        new In_Final_booking_check().execute(call);

        tv_ok_dialog_in= findViewById(R.id.tv_ok_dialog_in);
        tv_ok_dialog_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogFinalCheckInActivity.this, "확인", Toast.LENGTH_SHORT).show();
                DialogFinalCheckInActivity.this.finish();
            }
        });



    }//onCreate()........

    //입고대상 최종목록 확인하기 버튼
    public class In_Final_booking_check extends AsyncTask<Call, Void, List<TestAllVO>>{

        @Override
        protected List<TestAllVO> doInBackground(Call... calls) {
            Call<List<TestAllVO>> call= calls[0];
            try{
                Response<List<TestAllVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TestAllVO> testAllVOS) {
            super.onPostExecute(testAllVOS);


            finalStockListItems= new ArrayList<>();      //실행안됌
            for (int i=0; i<testAllVOS.size(); i++){
                finalStockListItems.add(new FinalStockListItem(testAllVOS.get(i).getUn_yn()
                                                            ,testAllVOS.get(i).getBarcode_dep_id()   //안씀??
                                                            ,req_emp_id
                                                            ,barcodeDepIdValue     //req_location (김민수) 대신
                                                            ,currentDateValue
                                                            ,testAllVOS.get(i).getNotice()
                                                            ,testAllVOS.get(i).getRequest_dep_id()
                                                            ,testAllVOS.get(i).getResponse_dep_id()
                                                            ,testAllVOS.get(i).getUnit_ver()
                                                            ,testAllVOS.get(i).getUnit_id()
                                                            ,testAllVOS.get(i).getUnit_code()
                                                            ,testAllVOS.get(i).getRep_unit_code()
                                                            ,true));

                Log.d("testAllVOS 사이즈 ", testAllVOS.size()+"");    //2
                Log.d("Req location 요청 위치 ", barcodeDepIdValue+"");    //김민수로나옴
                Log.d("Barcode dep id ############################## ", testAllVOS.get(i).getBarcode_dep_id()+"");
                Log.d("Request dep id ############################## ", testAllVOS.get(i).getRequest_dep_id()+"");    //null
                Log.d("Response dep id ############################## ", testAllVOS.get(i).getResponse_dep_id()+"");

            }
            inFinalListItemAdapter= new InFinalListItemAdapter(DialogFinalCheckInActivity.this, finalStockListItems);
            recyclerView= findViewById(R.id.recyclerview_final_list_dialog_in);
            recyclerView.setAdapter(inFinalListItemAdapter);
            inFinalListItemAdapter.notifyDataSetChanged();
        }


    }
}