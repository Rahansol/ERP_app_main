package app.erp.com.erp_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.erp.com.erp_app.vo.MainReportVo;
import retrofit2.Call;
import retrofit2.Response;

public class Work_Report_Activity extends AppCompatActivity {

    TextView start_day , date_text , text_box_length;
    Context context;
    Button serch_btn , submit_btn;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText text_box;

    ProgressDialog progressDialog;

    ImageView before_serch_btn , after_serch_btn;

    boolean isOkayClicked;
    int  yearStr , monthStr , dayStr ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);
        context = this;

        pref = getSharedPreferences("user_info" , MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("조회중..");
        progressDialog.setCancelable(false);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(cal.getTime());

        yearStr = cal.get(cal.YEAR);
        monthStr = cal.get(cal.MONTH);
        dayStr  = cal.get(cal.DAY_OF_MONTH);

        text_box_length = (TextView)findViewById(R.id.text_box_length);

        text_box = (EditText)findViewById(R.id.text_box);
        text_box.setRawInputType(InputType.TYPE_CLASS_TEXT);
        text_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = text_box.getText().toString();
                text_box_length.setText(input.length() + " / 2000 자");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        start_day = (TextView)findViewById(R.id.start_day);
        start_day.setText(today);

        isOkayClicked = true;
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
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

        final DatePickerDialog datePickerDialog = new DatePickerDialog( Work_Report_Activity.this, datePickerListener, yearStr, monthStr, dayStr);

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

                    String plan_date = start_day.getText().toString().replaceAll("-","");
                    function_call(plan_date);
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

        submit_btn = (Button)findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("등록중..");

                String emp_id = pref.getString("emp_id","interpass");
                String plan_date = start_day.getText().toString().replaceAll("-","");
                String plan_notice = text_box.getText().toString();
                ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<Boolean> call = erp.merge_plan_notice(emp_id,plan_date,plan_notice);
                new Work_Report_Activity.Update_Report_notice().execute(call);
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
                String plan_date = start_day.getText().toString().replaceAll("-","");
                function_call(plan_date);
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
                String plan_date = start_day.getText().toString().replaceAll("-","");
                function_call(plan_date);
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        String plan_date = start_day.getText().toString().replaceAll("-","");
        function_call(plan_date);
    }

    public void function_call (String plan_date){
        String emp_id = pref.getString("emp_id","interpass");
        progressDialog.show();

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<MainReportVo>> call = erp.get_work_report_data(emp_id,plan_date);
        new Work_Report_Activity.Work_Report_Data().execute(call);
    }

    private class Work_Report_Data extends AsyncTask<Call , Void , List<MainReportVo>>{
        @Override
        protected List<MainReportVo> doInBackground(Call... calls) {
            try{
                Call<List<MainReportVo>> call = calls[0];
                Response<List<MainReportVo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MainReportVo> mainReportVos) {
            super.onPostExecute(mainReportVos);
            progressDialog.dismiss();

            if(null !=mainReportVos){
                if(mainReportVos.size() > 0){
                    text_box.setText(mainReportVos.get(0).getPlan_notice());
                }else{
                    text_box.setText("");
                }
            }else{
                Log.d("error","error");
            }
        }
    }

    private class Update_Report_notice extends AsyncTask<Call , Void , Boolean>{
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            String toast_text = "";
            if(aBoolean){
                toast_text = "업무계획 저장 완료";
            }else{
                toast_text = "업무계획을 저장하는데 오류가 발생했습니다.";
            }
            Toast.makeText(context ,toast_text , Toast.LENGTH_SHORT).show();

            Intent i = new Intent(Work_Report_Activity.this , Work_Report_Activity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_btn :
                new AlertDialog.Builder(this)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                pref = getSharedPreferences("user_info" , MODE_PRIVATE);
                                editor = pref.edit();
                                editor.putString("auto_login" , "Nauto");
                                editor.commit();

                                Intent i = new Intent(Work_Report_Activity.this , LoginActivity.class );
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
