package app.erp.com.erp_app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment2 extends DialogFragment {
    @NonNull
    @Override
    public DatePickerDialog onCreateDialog(@Nullable Bundle savedInstanceState) {   //달력 다이얼로그
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        Locale.setDefault(Locale.KOREAN);

        return new DatePickerDialog(getContext(),(DatePickerDialog.OnDateSetListener) getContext(), year, month, day);
    }
}
