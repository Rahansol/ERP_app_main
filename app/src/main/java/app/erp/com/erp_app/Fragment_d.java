package app.erp.com.erp_app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_d extends Fragment {

    Button bus_num_find , bus_num_barcode_find;
    Context context;

    private Retrofit retrofit;

    String click_type ,bus_barcode, area_code;
    List<String> scan_unit_barcodes;
    EditText find_bus_num;
    TextView reserve_area_name , reserve_unit_barcode;
    SharedPreferences pref, barcode_type_pref;
    SharedPreferences.Editor editor;

    LinearLayout care_layout , old_new_layout , old_select , old_barcode , new_old_layout , new_selcet ,new_barcode;

    CheckBox bs_yn;

    Spinner bus_num_list, field_trouble_error_type_list , field_trouble_high_code_list, field_trouble_low_code_list, field_trouble_care_code_list;

    public Fragment_d(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d, container ,false);
        context = getActivity();

        find_bus_num = (EditText)view.findViewById(R.id.find_bus_num);
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        EditText inputField = (EditText)view.findViewById(R.id.field_error_phone);
        inputField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        bus_num_list = (Spinner)view.findViewById(R.id.bus_num_list);
        field_trouble_error_type_list = (Spinner)view.findViewById(R.id.field_trouble_error_type_list);
        field_trouble_high_code_list = (Spinner)view.findViewById(R.id.field_trouble_high_code_list);
        field_trouble_low_code_list = (Spinner)view.findViewById(R.id.field_trouble_low_code_list);
        field_trouble_care_code_list = (Spinner)view.findViewById(R.id.field_trouble_care_code_list);

        barcode_type_pref = context.getSharedPreferences("barcode_type", Context.MODE_PRIVATE);
        editor = barcode_type_pref.edit();

        reserve_area_name = (TextView)view.findViewById(R.id.reserve_area_name);
        reserve_unit_barcode = (TextView)view.findViewById(R.id.reserve_unit_barcode);

        bus_num_barcode_find = (Button)view.findViewById(R.id.bus_num_barcode_find);
        bus_num_barcode_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "scan";
                editor.putString("camera_type" , "bus");
                editor.commit();
                new getfield_error_busnum().execute("02105671016854");
//                IntentIntegrator.forFragment(Fragment_d.this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
            }
        });

        bus_num_find = (Button)view.findViewById(R.id.bus_num_find);
        bus_num_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "text";
                new getfield_error_busnum().execute(find_bus_num.getText().toString());
            }
        });

        care_layout = (LinearLayout)view.findViewById(R.id.care_layout);
        old_new_layout = (LinearLayout)view.findViewById(R.id.old_new_layout);
        old_select = (LinearLayout)view.findViewById(R.id.old_select);
        old_barcode = (LinearLayout)view.findViewById(R.id.old_barcode);
        new_old_layout = (LinearLayout)view.findViewById(R.id.new_old_layout);
        new_selcet = (LinearLayout)view.findViewById(R.id.new_selcet);
        new_barcode = (LinearLayout)view.findViewById(R.id.new_barcode);

        bs_yn = (CheckBox)view.findViewById(R.id.bs_yn);
        bs_yn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {

                } else {

                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("scan")){
            new getfield_error_busnum().execute(barcode);
        }
    }

    private class getfield_error_busnum extends AsyncTask<String , Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_infoVo>> call = erp.getfield_error_busnum(strings[0]);
            call.enqueue(new Callback<List<Bus_infoVo>>() {
                @Override
                public void onResponse(Call<List<Bus_infoVo>> call, Response<List<Bus_infoVo>> response) {
                    final List<Bus_infoVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for(Bus_infoVo i : list){
                        spinner_list.add(i.getBusoff_bus());
                    }
                    bus_num_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    bus_num_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_bus_num = spinner_list.get(position);
                            for(int i = 0 ; i < list.size(); i++){
                                if(list.get(i).getBusoff_bus() == select_bus_num){
                                    reserve_area_name.setText(list.get(i).getOffice_group());
                                    reserve_unit_barcode.setText(list.get(i).getBusoff_name());
                                    break;
                                }
                            }
                            new getfield_trouble_error_type().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Bus_infoVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private class getfield_trouble_error_type extends AsyncTask<String , Integer , Long >{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_error_type("1","01");
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getUnit_name());
                    }
                    field_trouble_error_type_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_error_type_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_error_name = spinner_list.get(position);
                            String select_error_code = "";
                            for(int i = 0; i < list.size(); i++){
                                if(list.get(i).getUnit_name() == select_error_name){
                                    select_error_code = list.get(i).getUnit_code();
                                    break;
                                }
                            }
                            new getfield_trouble_high_code().execute(select_error_code);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private class getfield_trouble_high_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final String select_error_code = strings[0];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_high_code("1","01",select_error_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_high_name());
                    }
                    field_trouble_high_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_high_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_high_name = spinner_list.get(position);
                            String select_high_code = "";
                            for(int i = 0; i < list.size(); i++){
                                if(list.get(i).getTrouble_high_name() == select_high_name){
                                    select_high_code = list.get(i).getTrouble_high_cd();
                                    break;
                                }
                            }
                            new getfield_trouble_low_code().execute(select_error_code,select_high_code);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private class getfield_trouble_low_code extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final String select_error_code = strings[0];
            final String select_high_code = strings[1];
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_low_code("1","01",select_error_code,select_high_code);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_low_name());
                    }
                    field_trouble_low_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                    field_trouble_low_code_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String select_low_name = spinner_list.get(position);
                            String select_low_code = "";

                            for(int i = 0 ; i < list.size(); i++){
                                if(list.get(i).getTrouble_low_name() == select_low_name){
                                    select_low_code = list.get(i).getTrouble_low_cd();
                                    break;
                                }
                            }
                            new getfield_trouble_carecode().execute(select_error_code,select_high_code,select_low_code);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private class getfield_trouble_carecode extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_CodeVo>> call = erp.getfield_trouble_carecode("1","01",strings[0],strings[1],strings[2]);
            call.enqueue(new Callback<List<Trouble_CodeVo>>() {
                @Override
                public void onResponse(Call<List<Trouble_CodeVo>> call, Response<List<Trouble_CodeVo>> response) {
                    final List<Trouble_CodeVo> list = response.body();
                    final List<String> spinner_list = new ArrayList<>();
                    for (Trouble_CodeVo i : list){
                        spinner_list.add(i.getTrouble_care_name());
                    }
                    field_trouble_care_code_list.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                }

                @Override
                public void onFailure(Call<List<Trouble_CodeVo>> call, Throwable t) {

                }
            });
            return null;
        }
    }
}
