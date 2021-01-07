package app.erp.com.erp_app.callcenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import app.erp.com.erp_app.R;
import retrofit2.Retrofit;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_trouble_insert extends Fragment {

    Button error_bus_btn , error_nms_btn, error_charger_btn, error_bit_btn, error_nomalWork_btn, education_btn, call_bus_btn;
    Context context;

    private Retrofit retrofit;

    LinearLayout call_layout;
    SharedPreferences pref, barcode_type_pref;
    public Fragment_trouble_insert(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_insert, container ,false);
        context = getActivity();
        ((Call_Center_Activity)getActivity()).switchFragment("insert");

        // 유저정보 가져옴
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        // 사원리스트 가져옴
        String dep_code = pref.getString("dep_code",null);
        String check_emp_id = pref.getString("emp_id",null);

//         콜 센터 레이아웃
        call_layout = (LinearLayout)view.findViewById(R.id.call_layout);
        call_layout.setVisibility(View.GONE);

        if("C30".equals(dep_code) || "E00".equals(dep_code) || "sbhan".equals(check_emp_id) || "jhhan".equals(check_emp_id)){
            call_layout.setVisibility(View.VISIBLE);
        }else{
            call_layout.setVisibility(View.GONE);
        }

        // 키보드 내려가는 부분
        try{
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    RelativeLayout main_layout = view.findViewById(R.id.main_layout);
                    InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(main_layout.getWindowToken(),0);
                }
            },250);
        }catch (Exception e){
            e.printStackTrace();
        }

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                String title = "";
                switch (v.getId()){
                    case R.id.error_bus_btn :
                        fragment = new Fragment_trouble_insert_bus();
                        title = "장애등록 (버스)";
                        break;
                    case R.id.error_nms_btn :
                        fragment = new Fragment_trouble_insert_jip();
                        title = "장애등록 (집계)";
                        break;
                    case R.id.error_charger_btn :
                        fragment = new Fragment_trouble_insert_charger();
                        title = "장애등록 (충전기)";
                        break;
                    case R.id.error_bit_btn :
                        fragment = new Fragment_trobule_insert_bit();
                        title = "장애등록 (BIT)";
                        break;
                    case R.id.error_nomalWork_btn :
                        fragment = new Fragment_trobule_insert_office();
                        title = "장애등록 (일반업무)";
                        break;
                    case R.id.education_btn :
                        fragment = new Fragment_trouble_insert_edu();
                        break;
                    case R.id.call_bus_insert:
                        fragment = new Fragment_trouble_insert_bus_call_center();
                        break;
                }
                if(fragment != null){

//                    getActivity().getSupportFragmentManager().popBackStack();

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frage_change,fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        };

        error_bus_btn = (Button)view.findViewById(R.id.error_bus_btn);
        error_nms_btn = (Button)view.findViewById(R.id.error_nms_btn);
        error_charger_btn = (Button)view.findViewById(R.id.error_charger_btn);
        error_bit_btn = (Button)view.findViewById(R.id.error_bit_btn);
        error_nomalWork_btn = (Button)view.findViewById(R.id.error_nomalWork_btn);
        education_btn = (Button)view.findViewById(R.id.education_btn);
        call_bus_btn = (Button)view.findViewById(R.id.call_bus_insert);

        error_bus_btn.setOnClickListener(onClickListener);
        error_nms_btn.setOnClickListener(onClickListener);
        error_charger_btn.setOnClickListener(onClickListener);
        error_bit_btn.setOnClickListener(onClickListener);
        error_nomalWork_btn.setOnClickListener(onClickListener);
        education_btn.setOnClickListener(onClickListener);
        call_bus_btn.setOnClickListener(onClickListener);

        return view;
    }

}
