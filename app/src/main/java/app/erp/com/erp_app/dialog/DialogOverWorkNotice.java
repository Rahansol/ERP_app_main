package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Cash_Work_VO;

public class DialogOverWorkNotice extends Dialog  {

    private Context mContext;
    private TextView btn_ok;
    private List<Cash_Work_VO> list;
    private String input_notice_string;

    private View.OnClickListener ok_btn_listener;
    private View.OnClickListener cancel_btn_listener;
    private EditText over_work_notice_edittext;

    public DialogOverWorkNotice(Context context, View.OnClickListener clickListener , View.OnClickListener cancel_img_btn_listener, String notice_text) {
        super(context);
        mContext = context;
        ok_btn_listener = clickListener;
        cancel_btn_listener = cancel_img_btn_listener;
        input_notice_string = notice_text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_over_work_notice);

        over_work_notice_edittext = (EditText)findViewById(R.id.over_work_notice_edittext);
        over_work_notice_edittext.setText(input_notice_string);



        ImageView cancel_img_btn = (ImageView)findViewById(R.id.cancel_img_btn);
        cancel_img_btn.setOnClickListener(cancel_btn_listener);

        btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(ok_btn_listener);

    }

    public String return_notice_Text(){
        String input_text = over_work_notice_edittext.getText().toString();
        return input_text;
    }
}
