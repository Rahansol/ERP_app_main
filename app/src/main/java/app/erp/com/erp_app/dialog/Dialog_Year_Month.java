package app.erp.com.erp_app.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;

import app.erp.com.erp_app.R;

public class Dialog_Year_Month extends DialogFragment {

    private static final int MAX_YEAR = 2030;
    private static final int MIN_YEAR = 2019;

    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    Button btn_ok;
    Button btn_cancle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_year_month,null);

        btn_ok = view.findViewById(R.id.btn_confirm);
        btn_cancle = view.findViewById(R.id.btn_cancel);

        final NumberPicker monthPicker = (NumberPicker)view.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker)view.findViewById(R.id.picker_year);

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Year_Month.this.getDialog().cancel();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDateSet(null , yearPicker.getValue() , monthPicker.getValue() , 0);
                Dialog_Year_Month.this.getDialog().cancel();
            }
        });

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH)+1);

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);

        builder.setView(view);

        return builder.create();
    }
}
