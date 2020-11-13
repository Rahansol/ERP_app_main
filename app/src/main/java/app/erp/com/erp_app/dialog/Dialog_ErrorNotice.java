package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;

public class Dialog_ErrorNotice extends Dialog {

    private String notice;

    public Dialog_ErrorNotice(@NonNull Context context, String pvo) {
        super(context);
        this.notice = pvo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_error_notice);

        TextView btn_ok = (TextView)findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        TextView error_notice = (TextView)findViewById(R.id.error_notice);
        error_notice.setText(notice + "");
    }
}
