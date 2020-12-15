package app.erp.com.erp_app;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public DatePickerDialog onCreateDialog(@Nullable Bundle savedInstanceState) {   //달력 다이얼로그
        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        /*c.add(Calendar.DAY_OF_MONTH, -3);
        String threeDaysBefore= new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getTime());*/
        Locale.setDefault(Locale.KOREAN);

        return new DatePickerDialog(getContext(),(DatePickerDialog.OnDateSetListener) getContext(), year, month, day);
    }
}
