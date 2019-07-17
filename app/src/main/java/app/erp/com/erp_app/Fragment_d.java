package app.erp.com.erp_app;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    Button error_bus_btn , error_nms_btn, error_charger_btn, error_bit_btn, error_nomalWork_btn;
    Context context;

    private Retrofit retrofit;

    public Fragment_d(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d, container ,false);
        context = getActivity();

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                String title = "";
                switch (v.getId()){
                    case R.id.error_bus_btn :
                        fragment = new Fragment_d_1();
                        title = "장애등록 (버스)";
                        break;
                    case R.id.error_nms_btn :
                        fragment = new Fragment_d_2();
                        title = "장애등록 (집계)";
                        break;
                    case R.id.error_charger_btn :
                        fragment = new Fragment_d_3();
                        title = "장애등록 (충전기)";
                        break;
                    case R.id.error_bit_btn :
                        fragment = new Fragment_d_4();
                        title = "장애등록 (BIT)";
                        break;
                    case R.id.error_nomalWork_btn :
                        fragment = new Fragment_d_5();
                        title = "장애등록 (일반업무)";
                        break;
                }
                if(fragment != null){
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frage_change,fragment);
                    ft.commit();
                }

                if (((MainActivity)getActivity()).getSupportActionBar() != null) {
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle(title);
                }
            }
        };

        error_bus_btn = (Button)view.findViewById(R.id.error_bus_btn);
        error_nms_btn = (Button)view.findViewById(R.id.error_nms_btn);
        error_charger_btn = (Button)view.findViewById(R.id.error_charger_btn);
        error_bit_btn = (Button)view.findViewById(R.id.error_bit_btn);
        error_nomalWork_btn = (Button)view.findViewById(R.id.error_nomalWork_btn);

        error_bus_btn.setOnClickListener(onClickListener);
        error_nms_btn.setOnClickListener(onClickListener);
        error_charger_btn.setOnClickListener(onClickListener);
        error_bit_btn.setOnClickListener(onClickListener);
        error_nomalWork_btn.setOnClickListener(onClickListener);


        return view;
    }

}
