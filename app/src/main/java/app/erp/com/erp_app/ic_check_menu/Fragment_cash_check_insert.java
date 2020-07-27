package app.erp.com.erp_app.ic_check_menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.dialog.DialogOverLapWork;
import app.erp.com.erp_app.vo.Cash_Work_VO;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_cash_check_insert extends Fragment implements View.OnClickListener{

    Spinner first_address_spinner , add_no_spinner ,cash_bus_num_spinner,all_office_spinner;
    private Retrofit retrofit;
    ERP_Spring_Controller erp;
    Context context;
    EditText find_cash_bus_num ,cash_bus_route_num,cash_box_notice , change_bus_num;
    TextView cash_office_name;
    Button bus_num_find ,cash_box_check_insert_btn;
    SharedPreferences pref , area_pref;
    SharedPreferences.Editor editor;
    DialogOverLapWork mdialog;
    ProgressDialog progressDialog;

    HashMap<String, Object> cash_box_check_map;

    RadioGroup check1_group , check2_group, check3_group , check4_group , check5_group , check6_group , check7_group;

    CheckBox check3_1 , check3_2 , check3_3 ,check3_4
    , check4_1 ,check4_2 , check4_3
    , check6_1 , check6_2, check6_3, check6_4
    , check7_1 , check7_2 , check7_3 , check7_4;

    int check3_val , check4_val , check6_val, check7_val;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cash_check_insert, container, false);
        context = getActivity();

        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        area_pref = context.getSharedPreferences("area_data", Context.MODE_PRIVATE);

        editor = area_pref.edit();

        cash_box_check_map = new HashMap<>();
        cash_box_check_map.put("check_1","N");
        cash_box_check_map.put("check_2","N");
        cash_box_check_map.put("check_3",0);
        cash_box_check_map.put("check_4",0);
        cash_box_check_map.put("check_5","N");
        cash_box_check_map.put("check_6",0);
        cash_box_check_map.put("check_7",0);
        cash_box_check_map.put("bus_num","N");
        cash_box_check_map.put("transp_bizr_id","N");

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("등록중...");

        // spinner
        first_address_spinner = (Spinner)view.findViewById(R.id.first_address_spinner);
        add_no_spinner = (Spinner)view.findViewById(R.id.add_no_spinner);
        cash_bus_num_spinner = (Spinner)view.findViewById(R.id.cash_bus_num_spinner);
        all_office_spinner = (Spinner)view.findViewById(R.id.all_office_spinner);

        //edittext
        cash_bus_route_num = (EditText)view.findViewById(R.id.cash_bus_route_num);
//        cash_bus_route_num.setPrivateImeOptions("defaultInputmode=numberic;");
        find_cash_bus_num = (EditText)view.findViewById(R.id.find_cash_bus_num);
        cash_box_notice = (EditText)view.findViewById(R.id.cash_box_notice);
        change_bus_num = (EditText)view.findViewById(R.id.change_bus_num);

        change_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    find_cash_bus_num.clearFocus();
                    handled =true;
                    downKeyboard(change_bus_num);
                }
                return handled;
            }
        });

        find_cash_bus_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    Call<List<Cash_Work_VO>> call = erp.find_bus_num(find_cash_bus_num.getText().toString());
                    new Fragment_cash_check_insert.GetFindBusNum().execute(call);
                    find_cash_bus_num.clearFocus();
                    handled =true;
                    downKeyboard(find_cash_bus_num);
//                    find_bus_num.clearFocus();
                }
                return handled;
            }
        });
        cash_bus_route_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled =false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    cash_bus_route_num.clearFocus();
                    handled =true;
                    downKeyboard(cash_bus_route_num);
                }
                return handled;
            }
        });

        // textview
        cash_office_name = (TextView)view.findViewById(R.id.cash_office_name);

        // button
        bus_num_find = (Button)view.findViewById(R.id.bus_num_find);
        bus_num_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_cash_bus_num.clearFocus();
                downKeyboard(find_cash_bus_num);

                Call<List<Cash_Work_VO>> call = erp.find_bus_num(find_cash_bus_num.getText().toString());
                new Fragment_cash_check_insert.GetFindBusNum().execute(call);
            }
        });
        cash_box_check_insert_btn = (Button)view.findViewById(R.id.cash_box_check_insert_btn);
        cash_box_check_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cash_box_check_map.put("route_num",cash_bus_route_num.getText().toString());
                cash_box_check_map.put("notice",cash_box_notice.getText().toString());

                String check_bus_num = change_bus_num.getText().toString();
                if(!"".equals(check_bus_num)){
                    cash_box_check_map.put("bus_num",change_bus_num.getText().toString());
                }

                Log.d("check val 3 = " , ":::"+ check3_val);
                Log.d("check val 4 = " , ":::"+ check4_val);
                Log.d("check val 6 = " , ":::"+ check6_val);
                Log.d("check val 7 = " , ":::"+ check7_val);

                if("N".equals(cash_box_check_map.get("add_no"))){
                    Toast.makeText(context,"영업소를 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if("N".equals(cash_box_check_map.get("transp_bizr_id")) && "".equals(cash_box_check_map.get("check_bizr_id")) ){
                    Toast.makeText(context,"운수사 (수정 운수사) 를 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if("N".equals(cash_box_check_map.get("bus_num"))){
                    Toast.makeText(context,"차량번호를 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if("".equals(cash_box_check_map.get("route_num"))){
                    Toast.makeText(context,"노선번호를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if("N".equals(cash_box_check_map.get("check_1"))){
                    Toast.makeText(context,"1번 항목을 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if("N".equals(cash_box_check_map.get("check_2"))) {
                    Toast.makeText(context, "2번 항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(0 == (int)cash_box_check_map.get("check_3")) {
                    Toast.makeText(context, "3번 항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(0 == (int)cash_box_check_map.get("check_4")) {
                    Toast.makeText(context, "4번 항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if("N".equals(cash_box_check_map.get("check_5"))) {
                    Toast.makeText(context, "5번 항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(0 == (int)cash_box_check_map.get("check_6")) {
                    Toast.makeText(context, "6번 항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(0 == (int)cash_box_check_map.get("check_7")) {
                    Toast.makeText(context, "7번 항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                StringBuilder sb = new StringBuilder();
//                Set<?> set = cash_box_check_map.keySet();
//                Iterator<?> it = set.iterator();
//                while(it.hasNext()){
//                    String key = (String)it.next();
//                    if(key != null){
//                        sb.append("------------------------------------------------------------\n");
//                        sb.append("key = "+key+",\t\t\tvalue = "+cash_box_check_map.get(key)+"\n");
//                    }
//                }
//                Log.i("values info : " , ""+sb);

                String emp_id = pref.getString("emp_id","empty_user");
                cash_box_check_map.put("emp_id",emp_id);

                progressDialog.show();
                Call<Boolean> call = erp.insert_cash_check_data(cash_box_check_map);
                new Fragment_cash_check_insert.Insert_Cash_Check_Data().execute(call);
            }
        });

        // checkbox
        check3_1 = (CheckBox)view.findViewById(R.id.check3_1);
        check3_2 = (CheckBox)view.findViewById(R.id.check3_2);
        check3_3 = (CheckBox)view.findViewById(R.id.check3_3);
        check3_4 = (CheckBox)view.findViewById(R.id.check3_4);

        check4_1 = (CheckBox)view.findViewById(R.id.check4_1);
        check4_2 = (CheckBox)view.findViewById(R.id.check4_2);
        check4_3 = (CheckBox)view.findViewById(R.id.check4_3);

        check6_1 = (CheckBox)view.findViewById(R.id.check6_1);
        check6_2 = (CheckBox)view.findViewById(R.id.check6_2);
        check6_3 = (CheckBox)view.findViewById(R.id.check6_3);
        check6_4 = (CheckBox)view.findViewById(R.id.check6_4);

        check7_1 = (CheckBox)view.findViewById(R.id.check7_1);
        check7_2= (CheckBox)view.findViewById(R.id.check7_2);
        check7_3= (CheckBox)view.findViewById(R.id.check7_3);
        check7_4= (CheckBox)view.findViewById(R.id.check7_4);

        check3_1.setOnClickListener(this);
        check3_2.setOnClickListener(this);
        check3_3.setOnClickListener(this);
        check3_4.setOnClickListener(this);

        check4_1.setOnClickListener(this);
        check4_2.setOnClickListener(this);
        check4_3.setOnClickListener(this);

        check6_1.setOnClickListener(this);
        check6_2.setOnClickListener(this);
        check6_3.setOnClickListener(this);
        check6_4.setOnClickListener(this);

        check7_1.setOnClickListener(this);
        check7_2.setOnClickListener(this);
        check7_3.setOnClickListener(this);
        check7_4.setOnClickListener(this);

        // radiogroup
        check1_group = (RadioGroup)view.findViewById(R.id.check1_group);
        check2_group = (RadioGroup)view.findViewById(R.id.check2_group);


        check5_group = (RadioGroup)view.findViewById(R.id.check5_group);


        check1_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.check1_1 :
                        cash_box_check_map.put("check_1","1");
                        break;
                    case R.id.check1_2 :
                        cash_box_check_map.put("check_1","2");
                        break;
                    case R.id.check1_3 :
                        cash_box_check_map.put("check_1","3");
                        break;
                }
            }
        });
        check2_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.check2_1 :
                        cash_box_check_map.put("check_2","1");
                        break;
                    case R.id.check2_2 :
                        cash_box_check_map.put("check_2","2");
                        break;
                    case R.id.check2_3 :
                        cash_box_check_map.put("check_2","3");
                        break;
                    case R.id.check2_4 :
                        cash_box_check_map.put("check_2","4");
                        break;
                }
            }
        });

        check5_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.check5_1 :
                        cash_box_check_map.put("check_5","1");
                        break;
                    case R.id.check5_2 :
                        cash_box_check_map.put("check_5","2");
                        break;
                }
            }
        });

        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Cash_Work_VO>> call = erp.first_address_list();
        Call<List<Cash_Work_VO>> call2 = erp.find_all_office();
        new Fragment_cash_check_insert.GetFirstAddress().execute(call);
        new Fragment_cash_check_insert.GetFindAllOffice().execute(call2);

        return view;
    }

    // 영업소
    private class GetFirstAddress extends AsyncTask<Call , Void , List<Cash_Work_VO>>{
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
                final List<String> spinner_list = new ArrayList<>();
                spinner_list.add("시 / 군 / 구");
                for(Cash_Work_VO i : cash_work_vos){
                    spinner_list.add(i.getFirst_address());
                }

                String scacs = area_pref.getString("first_position","");
                int get_pos = spinner_list.indexOf(scacs);

                first_address_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));
                if(-1 != get_pos){

                    first_address_spinner.setSelection(get_pos);
                }
                first_address_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long ld) {
                        cash_box_check_map.put("add_no","N");

                        String save_address = spinner_list.get(position);
                        editor.putString("first_position",save_address);
                        editor.commit();

                        if(position > 0){
                            String select_first_address = spinner_list.get(position);
                            Call<List<Cash_Work_VO>> call = erp.find_last_address(select_first_address);
                            new Fragment_cash_check_insert.GetFindLastAddress().execute(call);
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
            if(null != cash_work_vos){
                final List<String> spinner_list = new ArrayList<>();
                spinner_list.add("이하 주소");
                for(Cash_Work_VO i : cash_work_vos){
                    spinner_list.add(i.getLast_address());
                }
                add_no_spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinner_list));

                String pos = area_pref.getString("last_position","");
                Log.d("pos",pos);
                int find_pos = spinner_list.indexOf(pos);
                if(-1 != find_pos){
                    add_no_spinner.setSelection(find_pos);
                }
                add_no_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                        String save_last_address = spinner_list.get(p);
                        editor.putString("last_position",save_last_address);
                        editor.commit();

                        if(p > 0){
                            String select_last_address = spinner_list.get(p);
                            for(int i =0; i<cash_work_vos.size(); i++){
                                if(cash_work_vos.get(i).getLast_address() == select_last_address){
                                    cash_box_check_map.put("add_no",cash_work_vos.get(i).getAdd_no());
                                    break;
                                }
                            }
                        }else{
                            cash_box_check_map.put("add_no","N");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
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

                cash_office_name.setText("");
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
                            cash_office_name.setText("");
                            cash_box_check_map.put("bus_num","N");
                            cash_box_check_map.put("transp_bizr_id","N");
                        }else{
                            for(int i=0;i<cash_work_vos.size(); i++ ){
                                if(cash_work_vos.get(i).getBus_num() == select_bus_num){
                                    Call<List<Cash_Work_VO>> call = erp.find_overlap_work(cash_work_vos.get(i).getTransp_bizr_id(),select_bus_num);
                                    new Fragment_cash_check_insert.FindOverLapWork().execute(call);

                                    cash_office_name.setText(Html.fromHtml("<u>" + cash_work_vos.get(i).getBusoff_name() + "</u>")); // 밑줄

                                    cash_box_check_map.put("bus_num",cash_work_vos.get(i).getBus_num());
                                    cash_box_check_map.put("transp_bizr_id",cash_work_vos.get(i).getTransp_bizr_id());
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

                            Fragment fragment ;
                            fragment = new Fragment_cash_check_insert();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.cash_frag_change,fragment);
                            ft.commit();
                        }
                    },cash_work_vos);
                    mdialog.setCancelable(false);
                    mdialog.show();
                }
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
                spinner_list.add("운수사 미일치 시 선택");
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
                                    cash_box_check_map.put("check_bizr_id" , cash_work_vos.get(i).getTransp_bizr_id());
                                    cash_box_check_map.put("af_transp_bizr_id" , cash_work_vos.get(i).getTransp_bizr_id());
                                    break;
                                }
                            }
                        }else{
                            cash_box_check_map.put("check_bizr_id" , "");
                            cash_box_check_map.put("af_transp_bizr_id" , "");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    //데이터 입력
    private class Insert_Cash_Check_Data extends AsyncTask<Call , Void, Boolean>{
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
            progressDialog.dismiss();

            final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
            a_builder.setTitle("인천통합징수기 장애 등록");
            a_builder.setCancelable(false);
            a_builder.setPositiveButton("동일 영업소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment fragment ;
                                fragment = new Fragment_cash_check_insert();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.cash_frag_change,fragment);
                            ft.commit();
                        }
                    });
            a_builder.setNeutralButton("새로하기",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            editor.putString("last_position","");
                            editor.putString("first_position","");
                            editor.commit();

                            Fragment fragment ;
                            fragment = new Fragment_cash_check_insert();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.cash_frag_change,fragment);
                            ft.commit();
                        }
                    });
            if(aBoolean){
                a_builder.setMessage(" 등록 완료.");
                a_builder.show();
            }else{
                a_builder.setMessage("오류 발생 다시 시도 해주세요 .");
                a_builder.show();
            }

        }
    }

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            // check box 3 group -------------
            case R.id.check3_1:
                if(check3_val == 9){
                    check3_4.setChecked(false);
                    check3_val = 0;
                }
                if (checked){
                    check3_val += 1;
                }else{
                    check3_val -= 1;
                }
                break;
            case R.id.check3_2:
                if(check3_val == 9){
                    check3_4.setChecked(false);
                    check3_val = 0;
                }
                if (checked){
                    check3_val += 2;
                }else{
                    check3_val -= 2;
                }
                break;
            case R.id.check3_3:
                if(check3_val == 9){
                    check3_4.setChecked(false);
                    check3_val = 0;
                }
                if (checked){
                    check3_val += 4;
                }else{
                    check3_val -= 4;
                }
                break;
            case R.id.check3_4:

                if (checked){
                    check3_1.setChecked(false);
                    check3_2.setChecked(false);
                    check3_3.setChecked(false);
                    check3_val = 9;
                }else{
                    check3_1.setChecked(false);
                    check3_2.setChecked(false);
                    check3_3.setChecked(false);
                    check3_val = 0;
                }
                break;

                // check box 4 group -------------

            case R.id.check4_1 :
                if(check4_val == 9){
                    check4_3.setChecked(false);
                    check4_val = 0;
                }
                if (checked){
                    check4_val += 1;
                }else{
                    check4_val -= 1;
                }
                break;
            case R.id.check4_2 :
                if(check4_val == 9){
                    check4_3.setChecked(false);
                    check4_val = 0;
                }
                if (checked){
                    check4_val += 2;
                }else{
                    check4_val -= 2;
                }
                break;
            case R.id.check4_3 :
                if (checked){
                    check4_1.setChecked(false);
                    check4_2.setChecked(false);
                    check4_val = 9;
                }else{
                    check4_1.setChecked(false);
                    check4_2.setChecked(false);
                    check4_val = 0;
                }

                break;

            // check box 6 group -------------

            case R.id.check6_1 :
                if(check6_val == 9){
                    check6_4.setChecked(false);
                    check6_val = 0;
                }
                if (checked){
                    check6_val += 1;
                }else{
                    check6_val -= 1;
                }
                break;
            case R.id.check6_2 :
                if(check6_val == 9){
                    check6_4.setChecked(false);
                    check6_val = 0;
                }
                if (checked){
                    check6_val += 2;
                }else{
                    check6_val -= 2;
                }
                break;
            case R.id.check6_3 :
                if(check6_val == 9){
                    check6_4.setChecked(false);
                    check6_val = 0;
                }
                if (checked){
                    check6_val += 4;
                }else{
                    check6_val -= 4;
                }
                break;
            case R.id.check6_4 :

                if (checked){
                    check6_1.setChecked(false);
                    check6_2.setChecked(false);
                    check6_3.setChecked(false);
                    check6_val = 9;
                }else{
                    check6_1.setChecked(false);
                    check6_2.setChecked(false);
                    check6_3.setChecked(false);
                    check6_val = 0;
                }

                break;

            // check box 6 group -------------

            case R.id.check7_1 :
                if(check7_val == 9){
                    check7_4.setChecked(false);
                    check7_val = 0;
                }
                if (checked){
                    check7_val += 1;
                }else{
                    check7_val -= 1;
                }
                break;
            case R.id.check7_2 :
                if(check7_val == 9){
                    check7_4.setChecked(false);
                    check7_val = 0;
                }
                if (checked){
                    check7_val += 2;
                }else{
                    check7_val -= 2;
                }
                break;
            case R.id.check7_3 :
                if(check7_val == 9){
                    check7_4.setChecked(false);
                    check7_val = 0;
                }
                if (checked){
                    check7_val += 4;
                }else{
                    check7_val -= 4;
                }
                break;
            case R.id.check7_4 :
                if (checked){
                    check7_1.setChecked(false);
                    check7_2.setChecked(false);
                    check7_3.setChecked(false);
                    check7_val = 9;
                }else{
                    check7_1.setChecked(false);
                    check7_2.setChecked(false);
                    check7_3.setChecked(false);
                    check7_val = 0;
                }

                break;
        }
        cash_box_check_map.put("check_3",check3_val);
        cash_box_check_map.put("check_4",check4_val);
        cash_box_check_map.put("check_6",check6_val);
        cash_box_check_map.put("check_7",check7_val);
    }

    public void downKeyboard(EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}