package app.erp.com.erp_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.Bus_infoVo;
import retrofit2.Call;
import retrofit2.Response;

public class New_Bus_Activity extends AppCompatActivity implements View.OnClickListener{

    Button office_serch_btn, bus_info_insert_btn;

    EditText office_name, bus_num_edit_text;

    Spinner office_group_spinner;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String bus_num , trans_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bus);

        pref = getSharedPreferences("user_info" , MODE_PRIVATE);

        office_serch_btn = (Button)findViewById(R.id.office_serch_btn);
        bus_info_insert_btn = (Button)findViewById(R.id.bus_info_insert_btn);

        office_name = (EditText)findViewById(R.id.office_name);
        bus_num_edit_text =(EditText)findViewById(R.id.bus_num_edit_text);

        office_group_spinner = (Spinner)findViewById(R.id.office_group_spinner);

        office_serch_btn.setOnClickListener(this);
        bus_info_insert_btn.setOnClickListener(this);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.office_serch_btn :
                String busoff_name = office_name.getText().toString();
                ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<Bus_infoVo>> erp_call = erp.office_group_names(busoff_name);
                new Get_office_group().execute(erp_call);
                break;
            case R.id.bus_info_insert_btn :
                String emp_id = pref.getString("emp_id","hsra");
                bus_num = bus_num_edit_text.getText().toString();
                ERP_Spring_Controller erp2 = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<Void> insert_bus_info = erp2.app_insert_bus_info(bus_num,trans_id,emp_id);
                new Insert_bus_info().execute(insert_bus_info);
                break;
        }
    }

    private class Get_office_group extends AsyncTask<Call, Void, List<Bus_infoVo>>{
        @Override
        protected List<Bus_infoVo> doInBackground(Call... calls) {
            try {
                Call<List<Bus_infoVo>> call = calls[0];
                Response<List<Bus_infoVo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_infoVo> bus_infoVos) {
            if(bus_infoVos.size() > 0){
                final List<Bus_infoVo> list = bus_infoVos;
                List<String> empty_lsit = new ArrayList<>();
                for(int i = 0 ; i < list.size(); i++){
                    empty_lsit.add(list.get(i).getBusoff_name());
                }
                office_group_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,empty_lsit));

                office_group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        trans_id = list.get(position).getTransp_bizr_id();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }

    private class Insert_bus_info extends AsyncTask<Call , Void , Void>{
        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(New_Bus_Activity.this,"버스번호가 등록되었습니다.",Toast.LENGTH_LONG).show();
            Intent i = new Intent(New_Bus_Activity.this,New_Bus_Activity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
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
                                Intent i = new Intent(New_Bus_Activity.this , LoginActivity.class );
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
