package app.erp.com.erp_app;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.erp.com.erp_app.gtv_fragment.Fragement_Gtv_Emp_List;
import app.erp.com.erp_app.gtv_fragment.Fragement_Gtv_Total_List;

public class Gtv_Error_Install_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView gtv_day ;
    Button all_data_serch,emp_data_serch;
    TableLayout table_gtv_body;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gtv_error_install);

        pref = getSharedPreferences("user_info" , MODE_PRIVATE);

        gtv_day = (TextView)findViewById(R.id.gtv_day);
        all_data_serch = (Button)findViewById(R.id.all_data_serch);
        emp_data_serch = (Button)findViewById(R.id.emp_data_serch);
        table_gtv_body = (TableLayout)findViewById(R.id.table_gtv_body);

        getSupportActionBar().setTitle("GTV 가동율");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String get_today = sdf.format(date);
        gtv_day.setText(get_today);

        final Calendar cal = Calendar.getInstance();
        gtv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Gtv_Error_Install_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        gtv_day.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        all_data_serch.setOnClickListener(this);
        emp_data_serch.setOnClickListener(this);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Fragment fr = null;
        switch (v.getId()){
            case R.id.all_data_serch :      //전체조회 버튼
                String all_gtv_day = (String) gtv_day.getText();
                fr = new Fragement_Gtv_Total_List();
                Bundle args_all = new Bundle();

                args_all.putString("gtv_day",all_gtv_day.replaceAll("-",""));
                fr.setArguments(args_all);
                break;
            case R.id.emp_data_serch :      //담당지역 버튼
                String emp_gtv_day = (String) gtv_day.getText();
                fr = new Fragement_Gtv_Emp_List();

                Bundle args_emp = new Bundle();
                String emp_name = pref.getString("emp_name","hsra");

                args_emp.putString("gtv_day",emp_gtv_day.replaceAll("-",""));
                args_emp.putString("emp_name",emp_name);
                fr.setArguments(args_emp);
                break;
        }

        if(fr != null){
            FragmentManager fm_all = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction_all = fm_all.beginTransaction();
            fragmentTransaction_all.replace(R.id.gtv_fragment,fr);
            fragmentTransaction_all.commit();
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

                                Intent i = new Intent(Gtv_Error_Install_Activity.this , LoginActivity.class );
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
