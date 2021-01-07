package app.erp.com.erp_app.callcenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.adapter.My_Error_care_Adapter;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_trobule_care_insert extends Fragment {

    Context context;

    My_Error_care_Adapter adapter;
    ListView listView;

    private Retrofit retrofit;
    SharedPreferences pref , page_check_info;
    SharedPreferences.Editor editor;

    Button my_error_list_insert;
    ProgressDialog progressDialog;

    public Fragment_trobule_care_insert(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trobule_care_insert, container ,false);
        context = getActivity();
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        ((Call_Center_Activity)getActivity()).switchFragment("claer");

        page_check_info = context.getSharedPreferences("page_check_info" ,  Context.MODE_PRIVATE);
        editor = page_check_info.edit();
        editor.putString("page_check","care_insert");
        editor.commit();

        // 키보드 내려가는 부분
        try{
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    LinearLayout main_layout = view.findViewById(R.id.main_layout);
                    InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(main_layout.getWindowToken(),0);
                }
            },250);
        }catch (Exception e){
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        adapter = new My_Error_care_Adapter();
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = (int) v.getTag();
                Trouble_HistoryListVO thlvo = adapter.resultItem(postion);

                Fragment_trobule_care fragment = new Fragment_trobule_care();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Obj",thlvo);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frage_change,fragment);
                ft.commit();
            }
        });
        adapter.setError_call_driver_tel(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel_num = view.getTag().toString().replaceAll("-","");
                String tel = "tel:"+tel_num;
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });

        adapter.setError_equal_bus_btn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Call_Center_Activity)getActivity()).switchFragment("care");
                int postion = (int) view.getTag();
                Trouble_HistoryListVO thlvo = adapter.resultItem(postion);

                Fragment_trobule_equal_infra_insert fragment = new Fragment_trobule_equal_infra_insert();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Obj",thlvo);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frage_change,fragment);
                ft.commit();
            }
        });

        listView = (ListView)view.findViewById(R.id.my_error_list);
        new Filed_MyErrorList().execute();

        my_error_list_insert = (Button)view.findViewById(R.id.my_error_list_insert);
        my_error_list_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("등록중...");
                progressDialog.show();
                new insert_my_success_error_list().execute();
            }
        });

        return view;
    }

    private class Filed_MyErrorList extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            String emp_id = pref.getString("emp_id","inter");
            Call<List<Trouble_HistoryListVO>> call = erp.getMy_fieldError_care_list(emp_id);
            call.enqueue(new Callback<List<Trouble_HistoryListVO>>() {
                @Override
                public void onResponse(Call<List<Trouble_HistoryListVO>> call, Response<List<Trouble_HistoryListVO>> response) {
                    try{
                        List<Trouble_HistoryListVO> list = response.body();
                        MakeMyErrorList(list);
                    }catch (Exception e){
                        Toast.makeText(context,"데이터가 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Trouble_HistoryListVO>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private class insert_my_success_error_list extends AsyncTask<String, Integer, Long>{

        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            String emp_id = pref.getString("emp_id","inter");
            Call<Boolean> call = erp.insert_my_success_error_list(emp_id);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    boolean reulst = response.body();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context);
                    a_builder.setTitle("콜 처리");
                    a_builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment ;
                                    fragment = new Fragment_trobule_care_insert();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.frage_change,fragment);
                                    ft.commit();

                                }
                            });
                    if(reulst){
                        a_builder.setMessage(" 등록 완료.");
                        a_builder.show();
                    }else{
                        a_builder.setMessage("오류 발생 다시 시도 해주세요 .");
                        a_builder.show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private void MakeMyErrorList(List<Trouble_HistoryListVO> list) {
        listView.setAdapter(adapter);
        for(Trouble_HistoryListVO i : list){
            adapter.addItem(i);
        }
    }
}
