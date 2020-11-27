package app.erp.com.erp_app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class EmpListCheckableGNLinearLayout extends LinearLayout implements Checkable {

    public EmpListCheckableGNLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox)findViewById(R.id.emp_check_gn_box);
        return cb.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox)findViewById(R.id.emp_check_gn_box);
        setChecked(cb.isChecked() ? false : true);
    }

    @Override
    public void setChecked(boolean b) {
        CheckBox cb =(CheckBox) findViewById(R.id.emp_check_gn_box);
        if(cb.isChecked() != b){
            cb.setChecked(b);
        }
    }
}