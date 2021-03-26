package app.erp.com.erp_app.document_care.myfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.MainActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.callcenter.Fragment_trouble_list;
import app.erp.com.erp_app.document_care.InstallCableItems;
import app.erp.com.erp_app.document_care.Install_Cable_Adapter;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import app.erp.com.erp_app.vo.Bus_infoVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment2 extends Fragment implements View.OnClickListener {

    public static MyPageFragment1 myPageFragment1;
    public static FragmentManager fragmentManager;

    Context mContext= getActivity();
    static Spinner spinner_item_group_name;
    static Spinner spinner_item_each_name;

    static String st_item_group_name;
    static String st_item_group_name_value;
    static String st_item_each_name;
    static String st_item_each_name_value;

    /* InstallCable 리사이클러뷰*/
    private RecyclerView recyclerView;
    private ArrayList<InstallCableItems> installCableItems;
    private Install_Cable_Adapter install_cable_adapter;


    /*CABLE INSERT RECYCLERVIEW*/
    private RecyclerView recyclerView_cable;
    private ArrayList<CableInsertItems> cableInsertItems;
    private CableInsertAdapter cableInsertAdapter;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    TextView btnSave;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.pager2_my_project_work_insert_fragment, container, false);

       /* Log.d("prj_name :  ", G.prjName+"");
        Log.d("transpBizrId :  ", G.transpBizrId+"");
        Log.d("busoffName :  ", G.busoffName+"");
        Log.d("TempBusId :  ", G.TempBusId+"");
        Log.d("TempBusNum :  ", G.TempBusNum+"");
        Log.d("regEmpId :  ", G.regEmpId+"");
        Log.d("garageId :  ", G.garageId+"");
        Log.d("garageName :  ", G.garageName+"");
        Log.d("routeId :  ", G.routeId+"");
        Log.d("routeNum :  ", G.routeNum+"");
        Log.d("vehicleNum :  ", G.vehicleNum+"");
        Log.d("jopType :  ", G.jopType+"");
        Log.d("Last_seq :  ", G.Last_seq+"");*/

        fragmentManager= ((MyProject_Work_Insert_Activity)getActivity()).getSupportFragmentManager();

        //민혁 - 변수 초기화 추가
        for(int i=0; i < Garray.value2.length;i++) {
            Garray.value2[i]="";
        }


        if (getArguments() != null){
            String result= getArguments().getString("fromfragment1");
            Log.d("전달받은 데이터: ", result);

            ERP_Spring_Controller erp_cable= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
            Call<List<Bus_OfficeVO>> call_cable= erp_cable.cableItemLists(result);
            new cableItemLists().execute(call_cable);
        }else {
            Toast.makeText(getContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
        }



        /*TextView go_back= rootView.findViewById(R.id.go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new MyPageFragment1())
                        .addToBackStack(null)
                        .commit();
            }
        });*/

        recyclerView_cable= rootView.findViewById(R.id.recyclerview_cable);

        btnSave= rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        return  rootView;
    }//onCreateView


    // NOTE: 안전하게 context 를 사용하는 법 - onAttach 사용.
    // NOTE: 프래그먼트 이곳 저곳에서 getContext(), getActivity()를 부르다가 Null 이 발생할 수 있기때문에
    // NOTE: 더 안전하게 하려면 부를 때마다 체크 .. ===>>  ( isAdd() && mContext != null )
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext= context;
    }



    public class cableItemLists extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{
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
                cableInsertItems= new ArrayList<>();
                for (int i=0; i<bus_officeVOS.size(); i++){
                    cableInsertItems.add(new CableInsertItems(bus_officeVOS.get(i).getItem_each_seq(), bus_officeVOS.get(i).getItem_group_name(), "0"));
                }
                cableInsertAdapter= new CableInsertAdapter(mContext, cableInsertItems);
                recyclerView_cable.setAdapter(cableInsertAdapter);

                //item.quantity.notify();
                //cableInsertAdapter.notifyItemChanged(2);

               // cableInsertAdapter.notifyDataSetChanged();

            }
        }
    }



    //btnSave [저장]버튼
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                builder.setTitle("요청한 상세품목과 수량을 등록 하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0 ; i < Garray.value2.length ; i++) {
                            Log.d(" Garray.value2[", i+"]="+Garray.value2[i]  );
                        }
                        Log.d("차량번호 확인 : st_bus_list====>", G.st_bus_list+"");
                        Log.d("차량번호 확인 : st_bus_list_id====>", G.st_bus_list_id+"");
                        // 수량 업데이트
                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<String> call= erp.update_prj_def_val2(G.prjName+""
                                ,G.dtti+""
                                ,G.regEmpId+""
                                ,G.transpBizrId+""
                                ,G.busoffName+""
                                ,G.garageId+""
                                ,G.garageName+""
                                ,G.routeId+""
                                ,G.routeNum+""
                                ,G.st_bus_list_id+""
                                ,G.st_bus_list+""
                                ,G.vehicleNum+""
                                ,G.jopType+""
                                ,Garray.value2[0]+""
                                ,Garray.value2[1]+""
                                ,Garray.value2[2]+""
                                ,Garray.value2[3]+""
                                ,Garray.value2[4]+""
                                ,Garray.value2[5]+""
                                ,Garray.value2[6]+""
                                ,Garray.value2[7]+""
                                ,Garray.value2[8]+""
                                ,Garray.value2[9]+""
                                ,Garray.value2[10]+""
                                ,Garray.value2[11]+""
                                ,Garray.value2[12]+""
                                ,Garray.value2[13]+""
                                ,Garray.value2[14]+""
                                ,Garray.value2[15]+""
                                ,Garray.value2[16]+""
                                ,Garray.value2[17]+""
                                ,Garray.value2[18]+""
                                ,Garray.value2[19]+""
                                ,Garray.value2[20]+""
                                ,Garray.value2[21]+""
                                ,Garray.value2[22]+""
                                ,Garray.value2[23]+""
                                ,Garray.value2[24]+""
                                ,Garray.value2[25]+""
                                ,Garray.value2[26]+""
                                ,Garray.value2[27]+""
                                ,Garray.value2[28]+""
                                ,Garray.value2[29]+""
                                ,Garray.value2[30]+""
                                ,Garray.value2[31]+""
                                ,Garray.value2[32]+""
                                ,Garray.value2[33]+""
                                ,Garray.value2[34]+""
                                ,Garray.value2[35]+""
                                ,Garray.value2[36]+""
                                ,Garray.value2[37]+""
                                ,Garray.value2[38]+""
                                ,Garray.value2[39]+""
                                ,Garray.value2[40]+""
                                ,Garray.value2[41]+""
                                ,Garray.value2[42]+""
                                ,Garray.value2[43]+""
                                ,Garray.value2[44]+""
                                ,Garray.value2[45]+""
                                ,Garray.value2[46]+""
                                ,Garray.value2[47]+""
                                ,Garray.value2[48]+""
                                ,Garray.value2[49]+""
                                ,Garray.value2[50]+""
                                ,Garray.value2[61]+""
                                ,Garray.value2[62]+""
                                ,Garray.value2[63]+""
                                ,Garray.value2[64]+""
                                ,Garray.value2[65]+""
                                ,Garray.value2[66]+""
                                ,Garray.value2[67]+""
                                ,Garray.value2[68]+""
                                ,Garray.value2[69]+""
                                ,Garray.value2[70]+""
                                ,Garray.value2[71]+""
                                ,Garray.value2[72]+""
                                ,Garray.value2[73]+""
                                ,Garray.value2[74]+""
                                ,Garray.value2[75]+""
                                ,Garray.value2[76]+""
                                ,Garray.value2[77]+""
                                ,Garray.value2[78]+""
                                ,Garray.value2[79]+""
                                ,Garray.value2[80]+""
                                ,Garray.value2[81]+""
                                ,Garray.value2[82]+""
                                ,Garray.value2[83]+""
                                ,Garray.value2[84]+""
                                ,Garray.value2[85]+""
                                ,Garray.value2[86]+""
                                ,Garray.value2[87]+""
                                ,Garray.value2[88]+""
                                ,Garray.value2[89]+""
                                ,Garray.value2[90]+""
                                ,Garray.value2[91]+""
                                ,Garray.value2[92]+""
                                ,Garray.value2[93]+""
                                ,Garray.value2[94]+""
                                ,Garray.value2[95]+""
                                ,Garray.value2[96]+""
                                ,Garray.value2[97]+""
                                ,Garray.value2[98]+""
                                ,Garray.value2[99]+"");

                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(mContext, "저장완료!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });


                         /*getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout, new MyPageFragment1())    //첫번째 fragment 화면으로 돌아감
                                .addToBackStack(null)
                                .commit(); */

                        // STATUS: [완료]버튼을 클릭 -> 두번째 화면(2단계)으로 돌아감 (지금 현재화면)
                        // FIXME: 1) BackStack 에 있는 모든 fragments 들 소멸시키고 첫밴째 화면(1단계)으로 '이동'시키기
                        // FIXME: 2) 1단계 화면에서 뒤로가기 버튼 클릭 -> 메인화면으로 이동시키기


                        // NOTE: BackStack 에 있는 모든 fragments 들을 소멸시킴
                        getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                        // EDIT: [완료]버튼 클릭하면 1단계 화면으로 이동
                        //       1) 1단계 화면에서 뒤로가기 버튼 클릭  ->  첫번째로 [프로젝트 업무] 메뉴를 선택하면 나오는 [작성하기] 화면으로 이동
                        //       3) [작성하기] 화면에서 뒤로가기 버튼클릭  ->  메인화면으로 이동


                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.create();
                builder.show();



        }
    }



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

    //상세품목 스피너
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


