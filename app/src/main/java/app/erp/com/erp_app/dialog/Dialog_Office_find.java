package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.error_history.Error_History_Activity;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

public class Dialog_Office_find extends Dialog {

    private View.OnClickListener ok_listener;
    private View.OnClickListener cancle_listener;

    private String busoff_name , trans_id;

    private Spinner busoff_name_spinner;
    private ERP_Spring_Controller erp;
    private Context mContext;
    private ProgressDialog progressDialog;

    public Dialog_Office_find(Context context , View.OnClickListener o_listener,View.OnClickListener c_listener) {
        super(context);
        this.ok_listener = o_listener;
        this.cancle_listener  = c_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_office_find);
        mContext = getContext();
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        //운수사 입력 edittext
        final EditText busoff_name_text = (EditText)findViewById(R.id.busoff_name_text);
        busoff_name_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    Call<List<ProJectVO>> call = erp.app_busoff_name_find(busoff_name_text.getText().toString());
                    new Dialog_Office_find.app_busoff_name_find().execute(call);
                    handled =true;
                    downKeyboard(busoff_name_text);
                    busoff_name_text.clearFocus();
                }
                return handled;
            }
        });

        //검색 btn
        Button office_find_btn = (Button)findViewById(R.id.office_find_btn);
        office_find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<ProJectVO>> call = erp.app_busoff_name_find(busoff_name_text.getText().toString());
                new Dialog_Office_find.app_busoff_name_find().execute(call);
                downKeyboard(busoff_name_text);
                busoff_name_text.clearFocus();
            }
        });
        //운수사 spinner
        busoff_name_spinner = (Spinner)findViewById(R.id.busoff_name_spinner);

        //확인버튼
        TextView ok_btn = (TextView)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(ok_listener);
        TextView cancle_btn = (TextView)findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(cancle_listener);
    }

    private class app_busoff_name_find extends AsyncTask<Call,Void,List<ProJectVO>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("운수사 조회 중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(proJectVOS.size() > 0){
                final List<String> spinner_list = new ArrayList<>();
                for(ProJectVO i : proJectVOS){
                    spinner_list.add(i.getBusoff_name());
                }
                busoff_name_spinner.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                busoff_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        busoff_name = proJectVOS.get(position).getBusoff_name();
                        trans_id = proJectVOS.get(position).getTransp_bizr_id();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }else{
                Toast.makeText(mContext,"검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void downKeyboard(EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public Map<String,Object> select_busoff (){

        Map<String,Object> return_map = new HashMap<>();
        return_map.put("busoff_name",busoff_name);
        return_map.put("trans_id",trans_id);

        return return_map;
    }
}
