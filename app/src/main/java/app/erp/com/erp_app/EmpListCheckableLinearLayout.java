package app.erp.com.erp_app;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class EmpListCheckableLinearLayout extends LinearLayout implements Checkable {

    public EmpListCheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox)findViewById(R.id.emp_check_box);
        return cb.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox)findViewById(R.id.emp_check_box);
        setChecked(cb.isChecked() ? false : true);
    }

    @Override
    public void setChecked(boolean b) {
        CheckBox cb =(CheckBox) findViewById(R.id.emp_check_box);
        if(cb.isChecked() != b){
            cb.setChecked(b);
        }
    }
}
