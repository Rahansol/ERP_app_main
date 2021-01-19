package app.erp.com.erp_app.document_care.myfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.InstallCableItems;
import app.erp.com.erp_app.document_care.Install_Cable_Adapter;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class MyPageFragment2 extends Fragment {
    Context mContext;
    static String st_office_group;
    static Spinner spinner_item_group_name;
    static Spinner spinner_item_each_name;
    static String st_office_group_name;
    static String st_office_group_value;

    static String st_item_group_name;
    static String st_item_group_name_value;
    static String st_item_each_name;
    static String st_item_each_name_value;

    /* InstallCable 리사이클러뷰*/
    private RecyclerView recyclerView;
    private ArrayList<InstallCableItems> installCableItems;
    private Install_Cable_Adapter install_cable_adapter;

    ImageView iv_plus;
    ImageView iv_minus;
    TextView tv_quantity_value;
    Button btn_add;
    static String st_quantity_value;

    int cnt=1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args= this.getArguments();
        if (args != null){
            /*String stOfficeGroup= args.getString("st_office_group_value", st_office_group_value);
            String stJobName= args.getString("st_job_name_value", "");
            TextView office_group= rootView.findViewById(R.id.office_group);
            office_group.setText(stOfficeGroup);*/
            String stJobName= getArguments().getString("st_job_name_value");
            TextView office_group= getView().findViewById(R.id.office_group);
            office_group.setText(stJobName);
            Log.d("office_group==========================================>", office_group+"");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.pager2_my_project_work_insert_fragment, container, false);


        /*품목 스피너*/
        spinner_item_group_name= rootView.findViewById(R.id.spinner_item_group_name);
        ERP_Spring_Controller erp_item_group_name= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call_item_group_name= erp_item_group_name.ItemGroupNameSpinner("경기마을");  //st_office_group_value
        new ItemGroupNameSpinner().execute(call_item_group_name);

        /*상세품목 스피너*/
        spinner_item_each_name= rootView.findViewById(R.id.spinner_item_each_name);
        ERP_Spring_Controller erp_item_each_name= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call_item_each_name= erp_item_each_name.ItemEachNameSpinner("0100203");
        new ItemEachNameSpinner().execute(call_item_each_name);

        installCableItems= new ArrayList<>();
        install_cable_adapter= new Install_Cable_Adapter(getContext(), installCableItems);
        recyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerview_spinner);

        tv_quantity_value= rootView.findViewById(R.id.tv_quantity_value);
        iv_minus= rootView.findViewById(R.id.iv_minus);
        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt <=1) cnt=1;
                else cnt--;
                tv_quantity_value.setText(""+cnt);
                st_quantity_value= cnt+"";
            }
        });
        iv_plus= rootView.findViewById(R.id.iv_plus);
        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_quantity_value.setText(++cnt+"");
                //st_quantity_value = cnt+"";
            }
        });
        btn_add= rootView.findViewById(R.id.btn_add);
        /*추가버튼을 클릭- 선택한 상세목록 확인 리스트 실행*/
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1) "선택"이면 추가해주지 않게 조건문 처리
                if (st_item_group_name.equals("선택")){
                    Toast.makeText(getContext(), "품목과 상세품목을 선택하세요.", Toast.LENGTH_SHORT).show();
                }else if (st_item_each_name.equals("선택")){
                    Toast.makeText(getContext(), "품목과 상세품목을 선택하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    installCableItems.add(new InstallCableItems(st_item_group_name_value, st_item_each_name_value, /*(st_quantity_value == null ? "1" : st_quantity_value)*/ cnt+"", "삭제"));
                    recyclerView.setAdapter(install_cable_adapter);
                    install_cable_adapter.notifyDataSetChanged();

                    //2) 추가버튼을 클릭하면 품목, 상세품목, 수량 초기화
                    spinner_item_group_name.setSelection(0);
                    spinner_item_each_name.setSelection(0);
                    tv_quantity_value.setText(1+"");
                   /* st_quantity_value=1+"";*/
                    cnt=1;
                }



            }
        });

        return  rootView;
    }//onCreateView


    /*품목 스피너*/
    public class ItemGroupNameSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getItem_group_name());

                }

                spinner_item_group_name.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, spinner_array));
                spinner_item_group_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_item_group_name= spinner_item_group_name.getSelectedItem().toString();
                        if (!st_item_group_name.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_item_group_name == bus_officeVOS.get(j).getItem_group_name()){
                                    st_item_group_name_value= bus_officeVOS.get(j).getItem_group_name();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


    public class ItemEachNameSpinner extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS != null){
                List<String> spinner_array= new ArrayList<>();
                spinner_array.add("선택");

                for (int i=0; i<bus_officeVOS.size(); i++){
                    spinner_array.add(bus_officeVOS.get(i).getItem_each_name());
                    Log.d("1111 :::: ", i+">>"+bus_officeVOS.get(i).getItem_each_name());
                }
                spinner_item_each_name.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, spinner_array));
                spinner_item_each_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        st_item_each_name= spinner_item_each_name.getSelectedItem().toString();
                        if (!st_item_each_name.equals("선택")){
                            for (int j=0; j<bus_officeVOS.size(); j++){
                                if (st_item_each_name == bus_officeVOS.get(j).getItem_each_name()){
                                    st_item_each_name_value= bus_officeVOS.get(j).getItem_each_name();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


}//MyFragment2


