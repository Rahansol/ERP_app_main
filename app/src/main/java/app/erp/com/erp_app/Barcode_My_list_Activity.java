package app.erp.com.erp_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.erp.com.erp_app.vo.My_Work_ListVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Barcode_My_list_Activity extends AppCompatActivity {

    private Retrofit retrofit;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView start_day, end_day;
    Button work_serch_btn;
    TableLayout my_work_list_table;

    TableRow mywork_head,mywork_bus_head,mywork_bus_unitbarcode_head;

    String start_day_text , end_day_text;

    Context context;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_barcode);
        context = this;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        String get_today = sdf.format(date);
        String get_month = sdf2.format(date)+"-01";

        mywork_head = findViewById(R.id.mywork_head);
        mywork_bus_head = findViewById(R.id.mywork_bus_head);
        mywork_bus_unitbarcode_head = findViewById(R.id.mywork_bus_unitbarcode_head);
        mywork_bus_head.setVisibility(View.GONE);
        mywork_bus_unitbarcode_head.setVisibility(View.GONE);

        start_day = (TextView)findViewById(R.id.start_day);
        end_day = (TextView)findViewById(R.id.end_day);

        start_day.setText(get_today);
        end_day.setText(get_today);

        final Calendar cal = Calendar.getInstance();
        findViewById(R.id.end_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        end_day.setText(msg);

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        findViewById(R.id.start_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        start_day.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        work_serch_btn = (Button)findViewById(R.id.work_serch_btn);
        work_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("조회중..");
                progressDialog.show();
                start_day_text = start_day.getText().toString();
                end_day_text = end_day.getText().toString();
                new My_work_List().execute(start_day.getText().toString() , end_day.getText().toString()    );
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private class My_work_List extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            String emp_id = pref.getString("emp_id",null);
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            final Call<List<My_Work_ListVo>> call = erp.my_barcode_workList(strings[0].replaceAll("-",""), strings[1].replaceAll("-",""),emp_id);
            call.enqueue(new Callback<List<My_Work_ListVo>>() {
                @Override
                public void onResponse(Call<List<My_Work_ListVo>> call, Response<List<My_Work_ListVo>> response) {
                    progressDialog.dismiss();
                    if(response.body() == null ){

                    }else{

                    }
                    List<My_Work_ListVo> list = response.body();
                    if(list.size() == 0 || list == null){

                    }else{
                        make_mylist_table(list);
                    }

                }

                @Override
                public void onFailure(Call<List<My_Work_ListVo>> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });

            return null;
        }
    }

        private void make_mylist_table(List<My_Work_ListVo> list) {
        my_work_list_table = (TableLayout)findViewById(R.id.my_work_list_table);
        my_work_list_table.removeAllViews();
        Button btn = null;
        mywork_head.setVisibility(View.VISIBLE);
        mywork_bus_head.setVisibility(View.GONE);
        mywork_bus_unitbarcode_head.setVisibility(View.GONE);

        if(list.size() == 0){
            Toast.makeText(context , "조회한 날짜의 운행기록이 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0 ; i < list.size(); i++){
                final TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT  ));
                tableRow.setBackgroundResource(R.drawable.cell_shape);
                btn = new Button(context);

                if(i == list.size()-1){
                    for(int j = 0 ; j < 6; j++){
                        final TextView text = new TextView(context);
                        switch (j){
                            case 0 :
                                text.setText("");
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                text.setText(list.get(i).getOffice_group());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
                                text.setText(list.get(i).getBusoff_name());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 3:
                                text.setText(list.get(i).getBar_cnt()+" 대");
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 4:
                                text.setText("");
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                        }
                    }
                }else{
                    for(int j = 0 ; j < 6; j++){
                        final TextView text = new TextView(context);
                        switch (j){
                            case 0 :
                                text.setText(list.get(i).getLast_update());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                text.setText(list.get(i).getOffice_group());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
                                text.setText(list.get(i).getBusoff_name());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 3:
                                text.setText(list.get(i).getBar_cnt()+" 대");
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 4:
                                btn.setText("조회");
                                final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                                btn.setLayoutParams(new TableRow.LayoutParams(0 , height,1f));
                                String select_item_info = list.get(i).getBar_cnt()+","+list.get(i).getBusoff_name()+","+list.get(i).getLast_update()+","+list.get(i).getOffice_group()+","+list.get(i).getTransp_bizr_id();
                                btn.setTag(select_item_info);
                                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                                tableRow.addView(btn);

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String my_work_values = (String)view.getTag();
                                        String [] arr = my_work_values.split(",");
                                        get_mylist_bus_data(arr);
                                    }
                                });
                                break;
                        }
                    }
                }
                my_work_list_table.addView(tableRow);
            }
        }
    }

    private void get_mylist_bus_data(String[] arr) {
        my_work_list_table.removeAllViews();
        String trans_id = arr[4];
        String emp_id = pref.getString("emp_id",null);
        new my_barcode_workList_bus().execute(start_day_text,end_day_text,emp_id,trans_id);
    }

    private class my_barcode_workList_bus extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);

            String start_day = strings[0].replaceAll("-","");
            String end_day = strings[1].replaceAll("-","");
            final Call<List<My_Work_ListVo>> call = erp.my_barcode_workList_bus(start_day,end_day,strings[2],strings[3]);
            call.enqueue(new Callback<List<My_Work_ListVo>>() {
                @Override
                public void onResponse(Call<List<My_Work_ListVo>> call, Response<List<My_Work_ListVo>> response) {
                    List<My_Work_ListVo> list = response.body();
                    make_mylist_table_bus(list);
                }

                @Override
                public void onFailure(Call<List<My_Work_ListVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private void make_mylist_table_bus(List<My_Work_ListVo> list) {
        Button btn = null;
        mywork_head.setVisibility(View.GONE);
        mywork_bus_head.setVisibility(View.VISIBLE);
        mywork_bus_unitbarcode_head.setVisibility(View.GONE);

        if(list.size() == 0){
            Toast.makeText(context , "조회한 날짜의 운행기록이 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0 ; i < list.size(); i++){
                final TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT  ));
                tableRow.setBackgroundResource(R.drawable.cell_shape);
                btn = new Button(context);

                if(i == list.size()-1){
                    for(int j = 0 ; j < 6; j++){
                        final TextView text = new TextView(context);
                        switch (j){
                            case 0 :
                                text.setText(list.get(i).getRnum());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                text.setText(list.get(i).getBus_num());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
                                text.setText(list.get(i).getBar_cnt());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 3:
                                text.setText("");
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                        }
                    }
                }else{
                    for(int j = 0 ; j < 6; j++){
                        final TextView text = new TextView(context);
                        switch (j){
                            case 0 :
                                text.setText(list.get(i).getRnum());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                text.setText(list.get(i).getBus_num());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
                                text.setText(list.get(i).getBar_cnt());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 3:
                                btn.setText("조회");
                                final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                                btn.setLayoutParams(new TableRow.LayoutParams(0 , height,1f));
                                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                                btn.setTag(list.get(i).getBus_bar_code());
                                tableRow.addView(btn);

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String my_work_values = (String)view.getTag();
                                        get_mylist_bus_unitbarcode_data(my_work_values);
                                    }
                                });
                                break;
                        }
                    }
                }
                my_work_list_table.addView(tableRow);
            }
        }
    }

    private void get_mylist_bus_unitbarcode_data(String my_work_values) {
        String emp_id = pref.getString("emp_id",null);
        new my_barcode_workList_bus_unitbarcode().execute(start_day_text,end_day_text,my_work_values,emp_id);
    }

    private class my_barcode_workList_bus_unitbarcode extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);

            String start_day = strings[0].replaceAll("-","");
            String end_day = strings[1].replaceAll("-","");
            final Call<List<My_Work_ListVo>> call = erp.my_barcode_workList_bus_unitbarcode(start_day,end_day,strings[2],strings[3]);
            call.enqueue(new Callback<List<My_Work_ListVo>>() {
                @Override
                public void onResponse(Call<List<My_Work_ListVo>> call, Response<List<My_Work_ListVo>> response) {
                    List<My_Work_ListVo> list = response.body();
                    make_mylist_table_bus_unitbarcode(list);
                }
                @Override
                public void onFailure(Call<List<My_Work_ListVo>> call, Throwable t) {
                }
            });
            return null;
        }
    }

    private void make_mylist_table_bus_unitbarcode(List<My_Work_ListVo> list) {
        mywork_head.setVisibility(View.GONE);
        mywork_bus_head.setVisibility(View.GONE);
        mywork_bus_unitbarcode_head.setVisibility(View.VISIBLE);

        my_work_list_table.removeAllViews();

        if(list.size() == 0){
            Toast.makeText(context , "조회한 날짜의 운행기록이 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0 ; i < list.size(); i++){
                final TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT  ));
                tableRow.setBackgroundResource(R.drawable.cell_shape);
                    for(int j = 0 ; j < 6; j++){
                        final TextView text = new TextView(context);
                        switch (j){
                            case 0 :
                                text.setText(list.get(i).getUnit_name());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                text.setText(list.get(i).getUnit_bar_code());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
                                text.setText(list.get(i).getEb_serial());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                        }
                    }
                my_work_list_table.addView(tableRow);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2 , menu);
        return true;
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

                                Intent i = new Intent(Barcode_My_list_Activity.this , LoginActivity.class );
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
