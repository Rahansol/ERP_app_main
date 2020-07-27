package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import app.erp.com.erp_app.R;

public class Dialog_Reserve_Item extends Dialog {

    private Context mcontext;
    private String m_dialog_msg;
    private View.OnClickListener cancle_listener , insert_listener;

    Button cancle_btn, insert_btn;
    TextView check_item_list;

    Spinner reserve_insert_gubun;

    String input_gubun_result ;

    public Dialog_Reserve_Item(Context context , String dialog_msg , View.OnClickListener p_cancle_listener , View.OnClickListener p_insert_listener) {
        super(context);
        m_dialog_msg = dialog_msg;
        cancle_listener = p_cancle_listener;
        insert_listener = p_insert_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_item_insert_dialog);

        check_item_list = (TextView)findViewById(R.id.check_item_list);
        check_item_list.setMovementMethod(new ScrollingMovementMethod());
        check_item_list.setText(m_dialog_msg);

        cancle_btn = (Button)findViewById(R.id.cancle_btn);
        insert_btn = (Button)findViewById(R.id.insert_btn);

        cancle_btn.setOnClickListener(cancle_listener);
        insert_btn.setOnClickListener(insert_listener);

        reserve_insert_gubun = (Spinner)findViewById(R.id.reserve_insert_gubun);
        reserve_insert_gubun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String select_gubun = (String)reserve_insert_gubun.getSelectedItem();
                Log.d("itme",select_gubun);
                switch (select_gubun){
                    case "- 입고구분 -" :
                        input_gubun_result = "0";
                        break;
                    case "원인분석" :
                        input_gubun_result = "1";
                        break;
                    case "철수" :
                        input_gubun_result = "2";
                        break;
                    case "불량":
                        input_gubun_result = "3";
                        break;
                    case "반납" :
                        input_gubun_result = "4";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                input_gubun_result = "0";
            }
        });
    }

    public String result_input_gubun (){
        return input_gubun_result;
    }

}
