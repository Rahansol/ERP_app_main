package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

public class Dialog_Bus_find extends Dialog {

    private String trans_id;
    private View.OnClickListener ok_listener;
    private View.OnClickListener cancle_listener;

    private String bus_id , bus_num , bus_find_check;

    private Spinner bus_num_spinner;
    private ERP_Spring_Controller erp;
    private Context mContext;

    private ProgressDialog progressDialog;

    public Dialog_Bus_find(Context context , String t_id , View.OnClickListener ol , View.OnClickListener cl) {
        super(context);
        this.trans_id = t_id;
        this.ok_listener = ol;
        this.cancle_listener = cl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bus_num_find);
        mContext = getContext();

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        TextView ok_btn = (TextView)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(ok_listener);
        TextView cancle_btn = (TextView)findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(cancle_listener);

        final EditText bus_num_text = (EditText)findViewById(R.id.bus_num_text);
        bus_num_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    handled =true;
                    downKeyboard(bus_num_text);
                    bus_num_text.clearFocus();
                    Call<List<ProJectVO>> call = erp.app_bus_select(trans_id,bus_num_text.getText().toString());
                    new Dialog_Bus_find.get_bus_num_list().execute(call);
                }
                return handled;
            }
        });

        Button bus_num_find = (Button)findViewById(R.id.bus_num_find);
        bus_num_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downKeyboard(bus_num_text);
                Call<List<ProJectVO>> call = erp.app_bus_select(trans_id,bus_num_text.getText().toString());
                new Dialog_Bus_find.get_bus_num_list().execute(call);
            }
        });
        bus_num_spinner = (Spinner)findViewById(R.id.bus_num_spinner);

    }

    private class get_bus_num_list extends AsyncTask<Call , Void , List<ProJectVO>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("차량 번호 조회 중...");
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
        protected void onPostExecute(final List<ProJectVO> bus_infoVos) {
            super.onPostExecute(bus_infoVos);
            if(progressDialog != null)
                progressDialog.dismiss();
            if(bus_infoVos != null){
                if(bus_infoVos.size() > 0){
                    Toast.makeText(mContext,"버스를 선택해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"검색결과가 없습니다 다른 버스번호로 다시 검색해보세요",Toast.LENGTH_SHORT).show();
                }
            }
            final List<String> spinner_list = new ArrayList<>();
            spinner_list.add("버스번호를 선택해주세요.");
            spinner_list.add("번호 없음 (차대번호 입력)");
            for(ProJectVO i : bus_infoVos){
                spinner_list.add(i.getBus_num());
            }
            bus_num_spinner.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item,spinner_list));
            bus_num_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    if(pos >1){
                        bus_id = bus_infoVos.get(pos-2).getBus_id();
                        bus_num = bus_infoVos.get(pos-2).getBus_num();
                        bus_find_check = "Y";
                    }else if(pos == 1){
                        bus_id = "999999999";
                        bus_num = "번호없음9999";
                        bus_find_check = "N";
                    }else{
                        bus_id = null;
                        bus_num = "";
                        bus_find_check = "Y";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    public void downKeyboard(EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public String bus_num_id (){
        return bus_id + "&" + bus_num + "&" + bus_find_check;
    }
}
