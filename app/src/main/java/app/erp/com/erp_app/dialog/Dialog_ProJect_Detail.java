package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;

public class Dialog_ProJect_Detail extends Dialog {

    private ProJectVO pvo;
    private View.OnClickListener ok_listener;

    public Dialog_ProJect_Detail(@NonNull Context context, ProJectVO pvo, View.OnClickListener ok_listener) {
        super(context);
        this.pvo = pvo;
        this.ok_listener = ok_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_project_detail);

        TextView btn_ok = (TextView)findViewById(R.id.btn_ok);
        TextView project_name = (TextView)findViewById(R.id.project_name);
        TextView area_name = (TextView)findViewById(R.id.area_name);
        TextView sub_area_name = (TextView)findViewById(R.id.sub_area_name);
        TextView status_name = (TextView)findViewById(R.id.status_name);
        TextView infra_name = (TextView)findViewById(R.id.infra_name);
        TextView base_infra_name = (TextView)findViewById(R.id.base_infra_name);
        TextView st_date = (TextView)findViewById(R.id.st_date);
        TextView ed_date = (TextView)findViewById(R.id.ed_date);
        TextView reg_date = (TextView)findViewById(R.id.reg_date);
        TextView emp_name = (TextView)findViewById(R.id.emp_name);
        TextView prj_notice = (TextView)findViewById(R.id.prj_notice);

        project_name.setText(pvo.getPrj_name());
        area_name.setText(pvo.getArea_name());
        sub_area_name.setText(pvo.getSub_area_name());
        status_name.setText(pvo.getStatus_name());
        infra_name.setText(pvo.getInfra_name());
        base_infra_name.setText(pvo.getBase_infra_name());
        st_date.setText(pvo.getSt_date());
        ed_date.setText(pvo.getEd_date());
        reg_date.setText(pvo.getReg_date());
        emp_name.setText(pvo.getReg_name());
        prj_notice.setText(pvo.getPrj_notice());

        btn_ok.setOnClickListener(ok_listener);
    }
}
