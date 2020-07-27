package app.erp.com.erp_app.error_history;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.erp.com.erp_app.Barcode_input_list_Activity;
import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Trouble_History_Adapter;
import app.erp.com.erp_app.callcenter.Fragment_trouble_insert_bus;
import app.erp.com.erp_app.dialog.Dialog_Office_find;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Error_History_Activity extends AppCompatActivity {

    private HashMap<String,Object> request_map ;
    private ERP_Spring_Controller erp;
    private EditText prj_find_bus_num;
    private Context mcContext;
    private ProgressDialog progressDialog;
    private Spinner bus_num_list , trouble_unit_spinner;

    private LinearLayout office_find_layout ,busnum_find_layout;

    private Dialog_Office_find office_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_history_main);
        mcContext = this;

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Trouble_CodeVo>> trouble_unit_code_list_call = erp.trouble_unit_code_list();
        new Error_History_Activity.trouble_unit_code_list().execute(trouble_unit_code_list_call);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        request_map = new HashMap<>();
        request_map.put("st_date","");
        request_map.put("ed_date","");
        request_map.put("service_id","01");
        request_map.put("unit_code","");
        request_map.put("garage_id","");
        request_map.put("route_id","");
        request_map.put("bus_id","");
        request_map.put("transp_bizr_id","");
        request_map.put("serch_trans_id","");

        // 운수사 검색 리니어레이아웃
        office_find_layout = (LinearLayout)findViewById(R.id.office_find_layout);
        office_find_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                office_dialog = new Dialog_Office_find(mcContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        office_dialog.dismiss();
                        Map<String,Object> office_return_map = office_dialog.select_busoff();
                        String busoff_name = (String)office_return_map.get("busoff_name");
                        String trans_id = (String)office_return_map.get("trans_id");
                        request_map.put("serch_trans_id",trans_id);

                        TextView busoff_text = (TextView)findViewById(R.id.busoff_textview);
                        busoff_text.setText(busoff_name);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        office_dialog.dismiss();
                    }
                });
                office_dialog.setCancelable(false);
                office_dialog.show();

                DisplayMetrics dm = mcContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = dm.widthPixels; //디바이스 화면 너비
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(office_dialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = office_dialog.getWindow();
                window.setAttributes(lp);
                office_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });

        //버스 검색 리니어레이아웃
//        busnum_find_layout = (LinearLayout)findViewById(R.id.busnum_find_layout);
//        busnum_find_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        final TextView st_date_view = (TextView)findViewById(R.id.st_date);
        final TextView ed_date_view = (TextView)findViewById(R.id.ed_date);

        final Calendar cal = Calendar.getInstance();
        findViewById(R.id.st_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mcContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        st_date_view.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        findViewById(R.id.ed_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mcContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%02d-%02d", year, month+1, date);
                        ed_date_view.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        //spinner
        bus_num_list = (Spinner)findViewById(R.id.bus_num_list);
        trouble_unit_spinner = (Spinner)findViewById(R.id.trouble_unit_spinner);
        //spinner - end

        Button history_serch_btn = (Button)findViewById(R.id.history_serch_btn);
        history_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(st_date_view.getText().length() == 1){
                    request_map.put("st_date","");
                }else{
                    request_map.put("st_date",st_date_view.getText());
                }

                if(ed_date_view.getText().length() == 1){
                    request_map.put("ed_date","");
                }else{
                    request_map.put("ed_date",ed_date_view.getText());
                }

                ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<Trouble_HistoryListVO>> call = erp.app_trouble_history_serch(request_map);
                new Error_History_Activity.app_trouble_history_serch().execute(call);
            }
        });

        prj_find_bus_num = (EditText)findViewById(R.id.prj_find_bus_num);
        prj_find_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    Call<List<Bus_infoVo>> call = erp.getfield_error_busnum(prj_find_bus_num.getText().toString());
                    new Error_History_Activity.getfield_error_busnum().execute(call);
                    handled =true;
                    downKeyboard(prj_find_bus_num);
                    prj_find_bus_num.clearFocus();
                }
                return handled;
            }
        });

        Button prj_item_serch_btn = (Button)findViewById(R.id.prj_item_serch_btn);
        prj_item_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<Bus_infoVo>> call = erp.getfield_error_busnum(prj_find_bus_num.getText().toString());
                new Error_History_Activity.getfield_error_busnum().execute(call);
                downKeyboard(prj_find_bus_num);
                prj_find_bus_num.clearFocus();
            }
        });
    }

    private class getfield_error_busnum extends AsyncTask<Call , Void , List<Bus_infoVo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcContext);
            progressDialog.setMessage("차량 번호 조회 중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        @Override
        protected List<Bus_infoVo> doInBackground(Call... calls) {
            try {
                Call<List<Bus_infoVo>> call =calls[0];
                Response<List<Bus_infoVo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Bus_infoVo> bus_infoVos) {
            super.onPostExecute(bus_infoVos);
            if (progressDialog != null)
                progressDialog.dismiss();
            prj_find_bus_num.clearFocus();
            if(null != bus_infoVos){
                if(bus_infoVos.size() > 0){
                    final List<String> spinner_list = new ArrayList<>();
                    spinner_list.add("버스번호를 선택해주세요.");
                    for(Bus_infoVo i : bus_infoVos){
                        spinner_list.add(i.getBusoff_bus());
                    }
                    bus_num_list.setAdapter(new ArrayAdapter<String>(mcContext,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_num_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position > 0){
                                String select_bus_num = spinner_list.get(position);
                                for(int i = 0 ; i < bus_infoVos.size(); i++){
                                    if(bus_infoVos.get(i).getBusoff_bus() == select_bus_num){
                                        request_map.put("bus_id",bus_infoVos.get(i).getBus_id());
                                        request_map.put("transp_bizr_id",bus_infoVos.get(i).getTransp_bizr_id());
                                    }
                                }
                                bus_num_list.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                            }else{
                                request_map.put("bus_id","");
                                request_map.put("transp_bizr_id","");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }else{
                    request_map.put("bus_id","");
                    Toast.makeText(mcContext,"검색결과가 없습니다 다른 버스번호로 다시 검색해보세요",Toast.LENGTH_SHORT).show();
                }
            }else{
                request_map.put("bus_id","");
                Toast.makeText(mcContext,"검색결과가 없습니다 다른 버스번호로 다시 검색해보세요",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class app_trouble_history_serch extends AsyncTask<Call,Void, List<Trouble_HistoryListVO>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcContext);
            progressDialog.setMessage("장애 이력 조회 중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected List<Trouble_HistoryListVO> doInBackground(Call... calls) {
            try{
                Call<List<Trouble_HistoryListVO>> call = calls[0];
                Response<List<Trouble_HistoryListVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Trouble_HistoryListVO> trouble_historyListVOS) {
            super.onPostExecute(trouble_historyListVOS);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(trouble_historyListVOS != null){
                RecyclerView recyclerView = findViewById(R.id.trouble_history_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(mcContext));

                Trouble_History_Adapter adapter = new Trouble_History_Adapter();
                recyclerView.setAdapter(adapter);

                for(int i=0; i<trouble_historyListVOS.size(); i++){
                    adapter.addItem(trouble_historyListVOS.get(i));
                }
                adapter.notifyDataSetChanged();

                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        findViewById(R.id.t_notice).getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                });
            }else{
                Toast.makeText(mcContext,"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class trouble_unit_code_list extends AsyncTask<Call , Void , List<Trouble_CodeVo>>{
        @Override
        protected List<Trouble_CodeVo> doInBackground(Call... calls) {
            try{
                Call<List<Trouble_CodeVo>> call = calls[0];
                Response<List<Trouble_CodeVo>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Trouble_CodeVo> trouble_codeVos) {
            super.onPostExecute(trouble_codeVos);
            Log.d("ttt","ttttt1ttt"+trouble_codeVos.toString());
            if(trouble_codeVos != null){
                final List<String> spinner_list = new ArrayList<>();
                spinner_list.add("장비 선택");
                for(Trouble_CodeVo i : trouble_codeVos){
                    spinner_list.add(i.getUnit_name());
                }

                trouble_unit_spinner.setAdapter(new ArrayAdapter<String>(mcContext,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                trouble_unit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position > 0){
                            String select_bus_num = spinner_list.get(position);
                            for(int i = 0 ; i < trouble_codeVos.size(); i++){
                                if(trouble_codeVos.get(i).getUnit_name() == select_bus_num){
                                    request_map.put("unit_code",trouble_codeVos.get(i).getUnit_code());
                                }
                            }
                        }else{
                            request_map.put("unit_code","");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }
    }

    public void downKeyboard(EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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
                                Intent i = new Intent(Error_History_Activity.this , LoginActivity.class );
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
