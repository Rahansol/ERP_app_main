package app.erp.com.erp_app.document_care.myfragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.erp.com.erp_app.DatePickerFragment;
import app.erp.com.erp_app.R;

public class MyPageFragment3 extends Fragment {

    private RelativeLayout payment_date_layout;
    private TextView tv_payment_date;
    static String st_payment_date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.pager3_my_project_work_insert_fragment, container, false);

        /*달력 날짜 선택 전 현재날짜로 먼저 설정*/
        long now= System.currentTimeMillis();
        Date date= new Date(now);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        String stCurrentDate= sdf.format(date);
        tv_payment_date= rootView.findViewById(R.id.payment_date);   //지급일
        tv_payment_date.setText(stCurrentDate);

        st_payment_date= stCurrentDate;  //지급일 전역변수에 현재날째 값 전달

        payment_date_layout= rootView.findViewById(R.id.payment_date_layout);
        payment_date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHandler(v);
            }
        });


        
        return  rootView;
    }


    public void OnClickHandler(View v){
        callbackMethod= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_payment_date= v.findViewById(R.id.payment_date);
                st_payment_date= String.format("%4d-%02d-%02d", year, (month+1), dayOfMonth);
                tv_payment_date.setText(st_payment_date);
            }
        };

        Calendar c= Calendar.getInstance(Locale.KOREAN);
        int year= c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        Locale.setDefault(Locale.KOREAN);

        DatePickerDialog dialog= new DatePickerDialog(getContext(), callbackMethod, year, month, day);
        dialog.show();
    }


}





