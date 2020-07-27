package app.erp.com.erp_app.ic_check_menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Cash_Box_Data_Adapter;
import app.erp.com.erp_app.dialog.DialogOverLapWork;
import app.erp.com.erp_app.vo.Cash_Work_VO;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_cash_check_list extends Fragment {

    Spinner first_address_spinner , add_no_spinner ,cash_bus_num_spinner,all_office_spinner;
    Context context;

    SharedPreferences pref;

    ERP_Spring_Controller erp;

    Button check_list_fin_btn ,all_data_serch ,bus_num_find, bus_num_list_find;

    HashMap<String, Object> cash_box_check_map;

    RadioGroup all_or_my_radio;

    TextView cash_chec_head;

    EditText find_cash_bus_num;

    DialogOverLapWork mdialog;

    String b_trans_id , b_bus_id;

    Cash_Box_Data_Adapter  cbda;

    ListView cash_data_listview;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cash_check_list, container, false);
        context = getActivity();

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("로딩중...");
        progressDialog.show();

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final String emp_id = pref.getString("emp_id","not_user");

        cash_data_listview = (ListView)view.findViewById(R.id.cash_data_listview);
        cbda = new Cash_Box_Data_Adapter();

        b_trans_id = "";
        b_bus_id = "";

        cash_box_check_map = new HashMap<>();
        cash_box_check_map.put("emp_id",emp_id);
        cash_box_check_map.put("emp_id_check","n_all");
        cash_box_check_map.put("add_no","n_all");

        find_cash_bus_num = (EditText)view.findViewById(R.id.find_cash_bus_num);
        find_cash_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    Call<List<Cash_Work_VO>> call = erp.find_bus_num(find_cash_bus_num.getText().toString());
                    new Fragment_cash_check_list.GetFindBusNum().execute(call);
                    find_cash_bus_num.clearFocus();
                    handled =true;
                    downKeyboard(find_cash_bus_num);
//                    find_bus_num.clearFocus();
                }
                return handled;
            }
        });

        // spinner
        first_address_spinner = (Spinner)view.findViewById(R.id.first_address_spinner);
        add_no_spinner = (Spinner)view.findViewById(R.id.add_no_spinner);
        cash_bus_num_spinner = (Spinner)view.findViewById(R.id.cash_bus_num_spinner);
        all_office_spinner = (Spinner)view.findViewById(R.id.all_office_spinner);

        // button
        bus_num_list_find = (Button)view.findViewById(R.id.bus_num_list_find);
        bus_num_list_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(b_bus_id) && "".equals(b_trans_id)){
                    Toast.makeText(context,"버스를 먼저 선택해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    Call<List<Cash_Work_VO>> call = erp.find_overlap_work(b_trans_id,b_bus_id);
                    new Fragment_cash_check_list.FindOverLapWork().execute(call);
                }
            }
        });

        all_data_serch = (Button)view.findViewById(R.id.all_data_serch);
        all_data_serch.performClick();

        // button
        bus_num_find = (Button)view.findViewById(R.id.bus_num_find);
        bus_num_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_cash_bus_num.clearFocus();
                downKeyboard(find_cash_bus_num);

                Call<List<Cash_Work_VO>> call = erp.find_bus_num(find_cash_bus_num.getText().toString());
                new Fragment_cash_check_list.GetFindBusNum().execute(call);
            }
        });

        //TextView
        cash_chec_head = (TextView)view.findViewById(R.id.cash_chec_head);

        check_list_fin_btn = (Button)view.findViewById(R.id.check_list_fin_btn);
        check_list_fin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cbda.clearItem();
                cbda.notifyDataSetChanged();

                Call<List<Cash_Work_VO>> call = erp.cash_table_head_text(cash_box_check_map);
                Call<List<Cash_Work_VO>> call2 = erp.cash_table_data(cash_box_check_map);
                new Fragment_cash_check_list.Find_Cash_Table_Head_Data().execute(call);
                new Fragment_cash_check_list.Find_Cash_Table_Data().execute(call2);

            }
        });

        //
        all_or_my_radio = (RadioGroup)view.findViewById(R.id.all_or_my_radio);
        all_or_my_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.all_data_serch :
                        cash_box_check_map.put("emp_id_check","n_all");
                        break;
                    case R.id.my_data_serch :
                        cash_box_check_map.put("emp_id_check","check");
                        break;
                }
            }
        });

        Call<List<Cash_Work_VO>> call = erp.first_address_list();
        Call<List<Cash_Work_VO>> call2 = erp.find_all_office();
        new Fragment_cash_check_list.GetFirstAddress().execute(call);
        new Fragment_cash_check_list.GetFindAllOffice().execute(call2);

        return view;
    }

    // 영업소
    private class GetFirstAddress extends AsyncTask<Call, Void , List<Cash_Work_VO>> {
        @Override
        protected List<Cash_Work_VO> doInBackground(Call... calls) {
            try{
                Call<List<Cash_Work_VO>> call = calls[0];
                Response<List<Cash_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Cash_Work_VO> cash_work_vos) {
            super.onPostExecute(cash_work_vos);
            progressDialog.dismiss();
            if(null != cash_work_vos){
                final List<String> spinner_list = new ArrayList<>();
                spinner_list.add("시 / 군 / 구");
                for(Cash_Work_VO i : cash_work_vos){
                    spinner_list.add(i.getFirst_address());
                }
                first_address_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                first_address_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long ld) {
//                        cash_box_check_map.put("add_no","N");
                        if(position > 0){
                            String select_first_address = spinner_list.get(position);
                            Call<List<Cash_Work_VO>> call = erp.find_last_address(select_first_address);
                            new Fragment_cash_check_list.GetFindLastAddress().execute(call);
                        }else{
                            add_no_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    //영업소 하위 주소
    private class GetFindLastAddress extends AsyncTask<Call, Void , List<Cash_Work_VO>>{
        @Override
        protected List<Cash_Work_VO> doInBackground(Call... calls) {
            try{
                Call<List<Cash_Work_VO>> call = calls[0];
                Response<List<Cash_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Cash_Work_VO> cash_work_vos) {
            super.onPostExecute(cash_work_vos);
            progressDialog.dismiss();
            if(null != cash_work_vos){
                final List<String> spinner_list = new ArrayList<>();
                spinner_list.add("이하 주소");
                for(Cash_Work_VO i : cash_work_vos){
                    spinner_list.add(i.getLast_address());
                }
                add_no_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                add_no_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                        if(p > 0){
                            String select_last_address = spinner_list.get(p);
                            for(int i =0; i<cash_work_vos.size(); i++){
                                if(cash_work_vos.get(i).getLast_address() == select_last_address){
                                    cash_box_check_map.put("add_no",cash_work_vos.get(i).getAdd_no());
                                    break;
                                }
                            }
                        }else{
                            cash_box_check_map.put("add_no","n_all");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    //전체 운수사 리스트 조회
    private class GetFindAllOffice extends AsyncTask<Call , Void, List<Cash_Work_VO>>{
        @Override
        protected List<Cash_Work_VO> doInBackground(Call... calls) {
            try {
                Call<List<Cash_Work_VO>> call = calls[0];
                Response<List<Cash_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Cash_Work_VO> cash_work_vos) {
            super.onPostExecute(cash_work_vos);
            if(null != cash_work_vos){
                final List<String> spinner_list = new ArrayList<>();
                spinner_list.add("운수사로 검색 시 선택");
                for(Cash_Work_VO i : cash_work_vos){
                    spinner_list.add(i.getBusoff_name());
                }
                all_office_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                all_office_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long l) {
                        if(postion > 0){
                            String select_bus_off_name = spinner_list.get(postion);
                            for(int i=0; i < cash_work_vos.size(); i++){
                                if(cash_work_vos.get(i).getBusoff_name() == select_bus_off_name){
                                    cash_box_check_map.put("transp_bizr_id" , cash_work_vos.get(i).getTransp_bizr_id());
                                    break;
                                }
                            }
                        }else{
                            cash_box_check_map.put("transp_bizr_id" , "n_all");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    //조건별 상단 헤드 검색
    private class Find_Cash_Table_Head_Data extends AsyncTask<Call ,  Void , List<Cash_Work_VO>>{
        @Override
        protected List<Cash_Work_VO> doInBackground(Call... calls) {
            try{
                Call<List<Cash_Work_VO>> call = calls[0];
                Response<List<Cash_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Cash_Work_VO> cash_work_voList) {
            super.onPostExecute(cash_work_voList);
            Log.d("size",":"+cash_work_voList.size());
            cash_chec_head.setText(cash_work_voList.get(0).getTable_head());
        }
    }

    //버스번호 조회
    private class GetFindBusNum extends AsyncTask<Call , Void , List<Cash_Work_VO>>{
        @Override
        protected List<Cash_Work_VO> doInBackground(Call... calls) {
            try{
                Call<List<Cash_Work_VO>> call = calls[0];
                Response<List<Cash_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Cash_Work_VO> cash_work_vos) {
            super.onPostExecute(cash_work_vos);
            if(null != cash_work_vos){
                if(cash_work_vos.size() > 1){
                    Toast.makeText(context,"버스를 선택해주세요",Toast.LENGTH_SHORT).show();
                }else if(cash_work_vos.size() ==0){
                    Toast.makeText(context,"검색결과가 없습니다 \n다른 버스번호로 다시 검색해보세요",Toast.LENGTH_SHORT).show();
                }

                final List<String> spinner_list = new ArrayList<>();
                if(cash_work_vos.size() > 1){
                    spinner_list.add("버스 선택");
                }
                for(Cash_Work_VO i : cash_work_vos){
                    spinner_list.add(i.getBus_num());
                }
                cash_bus_num_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                cash_bus_num_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long ld) {
                        String select_bus_num = spinner_list.get(position);
                        if("버스 선택".equals(select_bus_num)){
                            b_trans_id = "";
                            b_bus_id = "";
                        }else{
                            for(int i=0;i<cash_work_vos.size(); i++ ){
                                if(cash_work_vos.get(i).getBus_num() == select_bus_num){
                                    b_trans_id = cash_work_vos.get(i).getTransp_bizr_id();
                                    b_bus_id = select_bus_num;

                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }else{
                Toast.makeText(context,"검색결과가 없습니다 다른 버스번호로 다시 검색해보세요",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //중복작업 조회
    private class FindOverLapWork extends AsyncTask<Call ,Void ,List<Cash_Work_VO>>{
        @Override
        protected List<Cash_Work_VO> doInBackground(Call... calls) {
            try {
                Call<List<Cash_Work_VO>> call = calls[0];
                Response<List<Cash_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Cash_Work_VO> cash_work_vos) {
            super.onPostExecute(cash_work_vos);
            if(null != cash_work_vos){
                if(cash_work_vos.size() > 0){
                    mdialog = new DialogOverLapWork(context,new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mdialog.dismiss();
                        }
                    },cash_work_vos);
                    mdialog.setCancelable(false);
                    mdialog.show();
                }
            }
        }
    }

    //조건별 데이터 검색
    private class Find_Cash_Table_Data extends AsyncTask<Call ,  Void , List<Cash_Work_VO>>{
        @Override
        protected List<Cash_Work_VO> doInBackground(Call... calls) {
            try{
                Call<List<Cash_Work_VO>> call = calls[0];
                Response<List<Cash_Work_VO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Cash_Work_VO> cash_work_voList) {
            super.onPostExecute(cash_work_voList);
            if(null != cash_work_voList){
                if(cash_work_voList.size() > 0){
                    for(int i = 0; i < cash_work_voList.size(); i++){
                        cbda.addItem(cash_work_voList.get(i));
                    }
                }
                cash_data_listview.setAdapter(cbda);
            }
        }
    }

    public void downKeyboard(EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}