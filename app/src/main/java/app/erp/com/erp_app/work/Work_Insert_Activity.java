package app.erp.com.erp_app.work;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.Work_Report_Activity;

public class Work_Insert_Activity extends AppCompatActivity {

    TimePicker time_picker;
    Vibrator vibrator;
    TextView start_time, end_time, start_day , date_text;
    TimePickerDialog start_timePickerDialog;
    TimePickerDialog end_timePickerDialog;
    Boolean text_check;
    ImageView before_serch_btn , after_serch_btn;
    boolean isOkayClicked;
    int  yearStr , monthStr , dayStr ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_insert);

        text_check = false;

        start_timePickerDialog = new TimePickerDialog(Work_Insert_Activity.this, android.R.style.Theme_Holo_Light_Dialog, s_listener, 15, 24, false);
        start_timePickerDialog.updateTime(00,00);

        end_timePickerDialog = new TimePickerDialog(Work_Insert_Activity.this, android.R.style.Theme_Holo_Light_Dialog, e_listener, 15, 24, false);
        end_timePickerDialog.updateTime(00,00);

        start_time = (TextView)findViewById(R.id.start_time);
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_timePickerDialog.show();
            }
        });
        end_time = (TextView)findViewById(R.id.end_time);
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_timePickerDialog.show();
            }
        });

        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(cal.getTime());

        yearStr = cal.get(cal.YEAR);
        monthStr = cal.get(cal.MONTH);
        dayStr  = cal.get(cal.DAY_OF_MONTH);

        start_day = (TextView)findViewById(R.id.start_day);
        start_day.setText(today);

        isOkayClicked = true;
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                if (isOkayClicked) {
                    String msg = String.format("%d-%02d-%02d", selectedYear, selectedMonth+1, selectedDay);
                    start_day.setText(msg);
                    yearStr = selectedYear;
                    monthStr = selectedMonth;
                    dayStr = selectedDay;
                }
                isOkayClicked = false;
            }
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog( Work_Insert_Activity.this, datePickerListener, yearStr, monthStr, dayStr);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.cancel();
                            isOkayClicked = false;
                        }
                    }
                });

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    isOkayClicked = true;
                    DatePicker datePicker = datePickerDialog .getDatePicker();
                    datePickerListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                }
            }
        });
        datePickerDialog.setCancelable(false);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        findViewById(R.id.start_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        date_text = (TextView)findViewById(R.id.date_text);
        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        before_serch_btn = (ImageView)findViewById(R.id.before_serch_btn);
        before_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String msg = String.format("%d-%02d-%02d", yearStr, monthStr+1, dayStr);
                Date select_date;
                try{
                    select_date = sdf.parse(msg);
                }catch (Exception e){
                    e.printStackTrace();
                    select_date = new Date();
                }

                Calendar before_cal = Calendar.getInstance();
                before_cal.setTime(select_date);
                before_cal.add(Calendar.DATE,-1);

                yearStr = before_cal.get(before_cal.YEAR);
                monthStr = before_cal.get(before_cal.MONTH);
                dayStr  = before_cal.get(before_cal.DAY_OF_MONTH);

                String today = sdf.format(before_cal.getTime());
                start_day.setText(today);
            }
        });

        after_serch_btn = (ImageView)findViewById(R.id.after_serch_btn);
        after_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String msg = String.format("%d-%02d-%02d", yearStr, monthStr+1, dayStr);
                Date select_date;
                try{
                    select_date = sdf.parse(msg);
                }catch (Exception e){
                    e.printStackTrace();
                    select_date = new Date();
                }

                Calendar before_cal = Calendar.getInstance();
                before_cal.setTime(select_date);
                before_cal.add(Calendar.DATE,1);

                yearStr = before_cal.get(before_cal.YEAR);
                monthStr = before_cal.get(before_cal.MONTH);
                dayStr  = before_cal.get(before_cal.DAY_OF_MONTH);

                String today = sdf.format(before_cal.getTime());
                start_day.setText(today);
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private TimePickerDialog.OnTimeSetListener s_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int h, int m) {
            start_time.setText(time_chagne(h) + ":" + time_chagne(m) );
        }
    };

    private TimePickerDialog.OnTimeSetListener e_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int h, int m) {
            end_time.setText(time_chagne(h) + ":" + time_chagne(m) );
        }
    };

    public String time_chagne( int t){
        String time = "";
        if(t < 10){
            time = "0" + t;
        }else{
            time = ""+t;
        }
        return time;
    }
}