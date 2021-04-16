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

import app.erp.com.erp_app.vo.Reserve_Work_Vo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Barcode_input_list_Activity extends AppCompatActivity {

    private Retrofit retrofit;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView start_day, end_day;
    Button work_serch_btn;
    TableLayout reserve_item_table;

    TableRow reserve_work_head,reserve_work_unit_head,reserve_work_unit_detail_head;

    String start_day_text , end_day_text;

    Context context;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_barcode_input_list);
        context = this;

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        reserve_item_table = findViewById(R.id.reserve_item_table);

        reserve_work_head = findViewById(R.id.reserve_work_head);
        reserve_work_unit_head = findViewById(R.id.reserve_work_unit_head);
        reserve_work_unit_detail_head = findViewById(R.id.reserve_work_unit_detail_head);

        reserve_work_head.setVisibility(View.VISIBLE);
        reserve_work_unit_head.setVisibility(View.GONE);
        reserve_work_unit_detail_head.setVisibility(View.GONE);

        String get_today = sdf.format(date);
        String get_month = sdf2.format(date)+"-01";

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
                new reserve_itme_office_List().execute(start_day.getText().toString() , end_day.getText().toString()  );
            }
        });
    }

    private class reserve_itme_office_List extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            final Call<List<Reserve_Work_Vo>> call = erp.reserve_itme_office_List(strings[0].replaceAll("-",""), strings[1].replaceAll("-",""));
            call.enqueue(new Callback<List<Reserve_Work_Vo>>() {
                @Override
                public void onResponse(Call<List<Reserve_Work_Vo>> call, Response<List<Reserve_Work_Vo>> response) {
                    progressDialog.dismiss();
                    if(response.body() == null ){

                    }else{

                    }
                    List<Reserve_Work_Vo> list = response.body();
                    if(list.size() == 0 || list == null){

                    }else{
                        Make_table_reserve_list(list);
                    }

                }

                @Override
                public void onFailure(Call<List<Reserve_Work_Vo>> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });

            return null;
        }
    }

    private void Make_table_reserve_list(List<Reserve_Work_Vo> list) {
        Button btn = null;
        reserve_work_head.setVisibility(View.VISIBLE);
        reserve_work_unit_head.setVisibility(View.GONE);
        reserve_work_unit_detail_head.setVisibility(View.GONE);
        reserve_item_table.removeAllViews();

        if(list.size() == 0){
            Toast.makeText(context , "조회한 날짜의 운행기록이 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0 ; i < list.size(); i++){
                final TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT  ));
                tableRow.setBackgroundResource(R.drawable.cell_shape);
                btn = new Button(context);
                    for(int j = 0 ; j < 6; j++){
                        final TextView text = new TextView(context);
                        switch (j){
                            case 0 :
                                String row_type_0 = list.get(i).getGarage_name();
                                if(row_type_0.equals("계")){
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                }else{
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                }
                                text.setText(list.get(i).getLast_update());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                String row_type_1 = list.get(i).getGarage_name();
                                if(row_type_1.equals("계")){
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                }else{
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                }
                                text.setText(list.get(i).getOffice_group());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
                                String row_type_2 = list.get(i).getGarage_name();
                                if(row_type_2.equals("계")){
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                }else{
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                }
                                text.setText(list.get(i).getGarage_name());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,2f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 3:
                                String row_type_3 = list.get(i).getGarage_name();
                                if(row_type_3.equals("계")){
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                }else{
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                }
                                text.setText(list.get(i).getPut_unit());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 4 :
                                String row_type_4 = list.get(i).getGarage_name();
                                if(row_type_4.equals("계")){
                                    text.setText("");
                                    text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                    text.setTextColor(getResources().getColor(R.color.textBlack));
                                    tableRow.addView(text);
                                    break;
                                }else{
                                    btn.setText("조회");
                                    final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                                    btn.setLayoutParams(new TableRow.LayoutParams(0 , height,1f));
                                    btn.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                                    tableRow.addView(btn);

                                    btn.setTag(list.get(i).getLocation_code());

                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String location_code = (String) view.getTag();
                                            new getReserve_work_unit_data().execute(start_day_text, end_day_text , location_code);
                                        }
                                    });
                                    break;
                                }
                        }
                    }
                reserve_item_table.addView(tableRow);
            }
        }
    }

    private class getReserve_work_unit_data extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final String location_code = strings[2];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            final Call<List<Reserve_Work_Vo>> call = erp.reserve_item_unit_List(strings[0].replaceAll("-",""), strings[1].replaceAll("-",""),strings[2]);
            call.enqueue(new Callback<List<Reserve_Work_Vo>>() {
                @Override
                public void onResponse(Call<List<Reserve_Work_Vo>> call, Response<List<Reserve_Work_Vo>> response) {
                    List<Reserve_Work_Vo> list = response.body(); 
                    Make_table_reserve_uniit(list,location_code);
                }

                @Override
                public void onFailure(Call<List<Reserve_Work_Vo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private void Make_table_reserve_uniit(List<Reserve_Work_Vo> list, String location_code) {
        Button btn = null;
        reserve_work_head.setVisibility(View.GONE);
        reserve_work_unit_head.setVisibility(View.VISIBLE);
        reserve_work_unit_detail_head.setVisibility(View.GONE);
        reserve_item_table.removeAllViews();

        final String l_code = location_code;

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
                                text.setText(list.get(i).getUnit_name());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,2f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                text.setText(list.get(i).getUnit_cnt());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
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
                                text.setText(list.get(i).getUnit_name());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,2f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 1 :
                                text.setText(list.get(i).getUnit_cnt());
                                text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,1f));
                                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                                text.setTextColor(getResources().getColor(R.color.textBlack));
                                tableRow.addView(text);
                                break;
                            case 2:
                                btn.setText("조회");
                                final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                                btn.setLayoutParams(new TableRow.LayoutParams(0 , height,1f));
                                String values = list.get(i).getUnit_code();
                                btn.setTag(values);
                                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                                tableRow.addView(btn);

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String my_work_values = (String)view.getTag();
                                        new getReserve_work_unit_detail().execute(start_day_text,end_day_text,l_code,my_work_values);
                                    }
                                });
                                break;
                        }
                    }
                }
                reserve_item_table.addView(tableRow);
            }
        }
    }

    private class getReserve_work_unit_detail extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            final Call<List<Reserve_Work_Vo>> call = erp.reserve_item_unit_List_detail(strings[0].replaceAll("-",""), strings[1].replaceAll("-",""),strings[2],strings[3]);
            call.enqueue(new Callback<List<Reserve_Work_Vo>>() {
                @Override
                public void onResponse(Call<List<Reserve_Work_Vo>> call, Response<List<Reserve_Work_Vo>> response) {
                    List<Reserve_Work_Vo> list = response.body();
                    Make_table_reserve_unit_detail(list);
                }

                @Override
                public void onFailure(Call<List<Reserve_Work_Vo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private void Make_table_reserve_unit_detail(List<Reserve_Work_Vo> list) {
        reserve_work_head.setVisibility(View.GONE);
        reserve_work_unit_head.setVisibility(View.GONE);
        reserve_work_unit_detail_head.setVisibility(View.VISIBLE);

        reserve_item_table.removeAllViews();

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
                            text.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.WRAP_CONTENT,2f));
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
                reserve_item_table.addView(tableRow);
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

                                Intent i = new Intent(Barcode_input_list_Activity.this , LoginActivity.class );
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
