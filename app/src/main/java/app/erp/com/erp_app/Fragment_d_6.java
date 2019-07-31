package app.erp.com.erp_app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_d_6 extends Fragment implements MainActivity.OnBackPressedListener{

    Button bus_num_find , bus_num_barcode_find;
    Context context;

    private Retrofit retrofit;

    String click_type ,bus_barcode, area_code;
    List<String> scan_unit_barcodes;
    EditText find_bus_num;
    TextView insert_start_day , insert_start_time, insert_reg_emp_id, insert_unit_code , insert_bus_num, insert_phone_num, insert_area_code,
            insert_office_code, insert_garage, insert_route_code, insert_ars_unit_code, insert_ars_trouble_high_code,insert_ars_trouble_low_code;

    SharedPreferences pref, barcode_type_pref;
    SharedPreferences.Editor editor;

    LinearLayout care_layout , old_new_layout , old_select , old_barcode , new_old_layout , new_selcet ,new_barcode;

    CheckBox bs_yn;

    Spinner insert_process_unit_code , insert_process_trouble_high_code, insert_process_trouble_low_code, insert_process_trouble_care_code ;

    RadioGroup today_group;
    RadioButton today_y , today_n;

    public Fragment_d_6(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d_6, container ,false);
        context = getActivity();

        Bundle bundle = getArguments();
        Trouble_HistoryListVO thlvo = (Trouble_HistoryListVO) bundle.getSerializable("Obj");

        insert_start_day = (TextView)view.findViewById(R.id.insert_start_day);
        insert_start_time = (TextView)view.findViewById(R.id.insert_start_time);
        insert_reg_emp_id = (TextView)view.findViewById(R.id.insert_reg_emp_id);
        insert_unit_code = (TextView)view.findViewById(R.id.insert_unit_code);
        insert_bus_num = (TextView)view.findViewById(R.id.insert_bus_num);
        insert_phone_num = (TextView)view.findViewById(R.id.insert_phone_num);
        insert_area_code = (TextView)view.findViewById(R.id.insert_area_code);
        insert_office_code = (TextView)view.findViewById(R.id.insert_office_code);
        insert_garage = (TextView)view.findViewById(R.id.insert_garage);
        insert_route_code = (TextView)view.findViewById(R.id.insert_route_code);
        insert_ars_unit_code = (TextView)view.findViewById(R.id.insert_ars_unit_code);
        insert_ars_trouble_high_code = (TextView)view.findViewById(R.id.insert_ars_trouble_high_code);
        insert_ars_trouble_low_code = (TextView)view.findViewById(R.id.insert_ars_trouble_low_code);

        insert_process_unit_code = (Spinner) view.findViewById(R.id.insert_process_unit_code);
        insert_process_trouble_high_code = (Spinner)view.findViewById(R.id.insert_process_trouble_high_code);
        insert_process_trouble_low_code = (Spinner)view.findViewById(R.id.insert_process_trouble_low_code);
        insert_process_trouble_care_code = (Spinner)view.findViewById(R.id.insert_process_trouble_care_code);

        new GetMyWork_Job().execute(thlvo.getReg_date(),thlvo.getJob_viewer(),thlvo.getReg_time());

        return view;
    }

    private class GetMyWork_Job extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.test_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<List<Trouble_HistoryListVO>> call = erp.getMyWork_Job(strings[0],strings[1],strings[2]);
            call.enqueue(new Callback<List<Trouble_HistoryListVO>>() {
                @Override
                public void onResponse(Call<List<Trouble_HistoryListVO>> call, Response<List<Trouble_HistoryListVO>> response) {
                    try {
                        List<Trouble_HistoryListVO> list  = response.body();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(context,"데이터를 가져오는데 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Trouble_HistoryListVO>> call, Throwable t) {

                }
            });

            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String barcode = result.getContents();
        if(click_type.equals("stop")){
        }else if(click_type.equals("scan")){
        }
    }

    @Override
    public void onBack() {
        Log.e("Other", "onBack()");
        // 리스너를 설정하기 위해 Activity 를 받아옵니다.
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        activity.setOnBackPressedListener(null);
        // MainFragment 로 교체
        Fragment_d fragment_d = new Fragment_d();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frage_change, fragment_d).commit();
        // Activity 에서도 뭔가 처리하고 싶은 내용이 있다면 하단 문장처럼 호출해주면 됩니다.
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("장애등록");
        // activity.onBackPressed();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Log.e("Other", "onAttach()");
        ((MainActivity)activity).setOnBackPressedListener(this);
    }
}
