package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

public class Dialog_Doc_Info_View extends Dialog {

    private View.OnClickListener ok_listener;
    private ERP_Spring_Controller erp;
    private Context mContext;
    private ProgressDialog progressDialog;
    private List<ProJectVO> mdata;
    private HashMap<String,Object> mMap;
    private View.OnClickListener dov_listener;


    public Dialog_Doc_Info_View(Context context , View.OnClickListener o_listener, List<ProJectVO> list, HashMap<String,Object> map, View.OnClickListener dl) {
        super(context);
        this.ok_listener = o_listener;
        this.mdata = list;
        this.mMap = map;
        this.dov_listener = dl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_doc_info_view);
        mContext = getContext();
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        //텍스트

        TextView doc_busoff_name = (TextView)findViewById(R.id.doc_busoff_name);
        TextView doc_garage_name = (TextView)findViewById(R.id.doc_garage_name);
        TextView doc_reg_dtti = (TextView)findViewById(R.id.doc_reg_dtti);
        TextView doc_reg_name = (TextView)findViewById(R.id.doc_reg_name);
        TextView doc_sign_dtti = (TextView)findViewById(R.id.doc_sign_dtti);
        TextView doc_sign_name = (TextView)findViewById(R.id.doc_sign_name);
        TextView doc_sign_tel = (TextView)findViewById(R.id.doc_sign_tel);
        TextView doc_type = (TextView)findViewById(R.id.doc_type);

        doc_busoff_name.setText(mdata.get(0).getBusoff_name());
        doc_garage_name.setText(mdata.get(0).getGarage_name());
        doc_reg_dtti.setText(mdata.get(0).getWrite_dtti());
        doc_reg_name.setText(mdata.get(0).getEmp_name());
        doc_sign_dtti.setText(mdata.get(0).getSign_dtti());
        doc_sign_name.setText(mdata.get(0).getSign_man());
        doc_sign_tel.setText(mdata.get(0).getSign_tel());
        doc_type.setText(mdata.get(0).getOfficial_type());

        Call<List<ProJectVO>> call = erp.prj_doc_btn_name_list(mMap);
        new Dialog_Doc_Info_View.prj_doc_btn_name_list().execute(call);

        //확인버튼
        TextView ok_btn = (TextView)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(ok_listener);

        TextView doc_view_btn = (TextView)findViewById(R.id.doc_view_btn);
        doc_view_btn.setOnClickListener(dov_listener);
    }

    //다이얼로그 확인서 , 인수증 버튼 여부 데이터 통신
    private class prj_doc_btn_name_list extends AsyncTask<Call,Void,List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call =calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(proJectVOS != null){

                LinearLayout btn_text_layout = (LinearLayout)findViewById(R.id.btn_text_layout);
                for(int i=0; i<proJectVOS.size(); i++){
                    TextView btn_text = new TextView(mContext);
                    btn_text.setText(proJectVOS.get(i).getDoc_name());

                    btn_text_layout.addView(btn_text);
                }
            }
        }
    }
}
