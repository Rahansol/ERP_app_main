package app.erp.com.erp_app.callcenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.adapter.My_Error_Adapter;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.dialog.Dialog_ErrorNotice;
import app.erp.com.erp_app.dialog.Dialog_Unpro_Notice;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_trouble_list extends Fragment {

    Context context;

    My_Error_Adapter adapter;
    ListView listView;

    RadioGroup trouble_history_list;
    RadioButton trouble_bus , trouble_nms, trouble_chager , trouble_bit , trouble_nomal;

    TextView nomal_count , bus_count, nms_count,chager_count , bit_count;

    /*최근 6개월 장애건수 다이얼로그*/
    public static RecentErrorListAdapter recentAdapter1;
    public static RecyclerView recycler_RecentError;
    public static ArrayList<RecentErrorListItems> recentErrorListItems;

    private Map<String, Object> updateMap = new HashMap<>();

    public Dialog_Unpro_Notice dialog;
    public  String get_et_unpro_notice;

    private Retrofit retrofit;
    SharedPreferences pref,page_check_info;
    SharedPreferences.Editor editor;

    String service_id;
    String msg_check3;
    Trouble_HistoryListVO deleteVo;

    public Fragment_trouble_list(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trouble_list, container ,false);

        context = getActivity();
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        ((Call_Center_Activity)getActivity()).switchFragment("care");

        page_check_info = context.getSharedPreferences("page_check_info" ,  Context.MODE_PRIVATE);
        editor = page_check_info.edit();
        editor.putString("page_check","trouble_care");
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

        //상단 라디오 버튼
        trouble_bus = (RadioButton) view.findViewById(R.id.trouble_bus);
        trouble_nms = (RadioButton) view.findViewById(R.id.trouble_nms);
        trouble_chager = (RadioButton) view.findViewById(R.id.trouble_chager);
        trouble_bit = (RadioButton) view.findViewById(R.id.trouble_bit);
        trouble_nomal = (RadioButton)view.findViewById(R.id.trouble_nomal);

        // 상단 라디오 버튼 옆 카운트 text
        nomal_count = (TextView)view.findViewById(R.id.nomal_count);
        bus_count = (TextView)view.findViewById(R.id.bus_count);
        nms_count = (TextView)view.findViewById(R.id.nms_count);
        chager_count = (TextView)view.findViewById(R.id.chager_count);
        bit_count = (TextView)view.findViewById(R.id.bit_count);

        trouble_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trouble_bus.setChecked(true);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(false);

                service_id = "01";
                new Filed_MyErrorList().execute();
            }
        });

        trouble_nms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(true);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(false);

                service_id = "02";
                new Filed_MyErrorList().execute();
            }
        });

        trouble_chager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(true);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(false);

                service_id = "04";
                new Filed_MyErrorList().execute();
            }
        });

        trouble_bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(true);
                trouble_nomal.setChecked(false);

                service_id = "13";
                new Filed_MyErrorList().execute();
            }
        });

        trouble_nomal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(true);

                service_id = "09";
                new Filed_MyErrorList().execute();
            }
        });

        String emp_id = pref.getString("emp_id","inter");
        adapter = new My_Error_Adapter(emp_id);

        adapter.setNotice_btn_listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = (int) view.getTag();
                Trouble_HistoryListVO thlvo = adapter.resultItem(postion);

                Dialog_ErrorNotice den = new Dialog_ErrorNotice(context, thlvo.getArs_notice());
                den.setCancelable(true);
                den.show();

                DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = dm.widthPixels; //디바이스 화면 너비
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(den.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = den.getWindow();
                window.setAttributes(lp);
                den.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });


        /*최근 3~6개월 장애목록*/
        adapter.setErrorEventBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "최근 6개월간 장애목록 불러오기..", Toast.LENGTH_SHORT).show();

                int pos= (int)v.getTag();
                final Trouble_HistoryListVO thlvo= adapter.resultItem(pos);
                deleteVo= adapter.resultItem(pos);
                ArrayList<Trouble_HistoryListVO> list= adapter.resultList();
                final HashMap<String, Object> map= new HashMap<>();
                map.put("transp_bizr_id", thlvo.getTransp_bizr_id());
                map.put("bus_id", thlvo.getBus_id());
                String sp_transp_bizr_id= thlvo.getTransp_bizr_id();
                String sp_bus_id= thlvo.getBus_id();

                Log.d("transp_bizr_id 확인 ===== > ", sp_transp_bizr_id+"");
                Log.d("bus_id 확인 ==== > ", sp_bus_id+"");

                ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<Trouble_HistoryListVO>> call= erp.app_fieldError_not_care_history(sp_transp_bizr_id, sp_bus_id);  //My_Error_Adapter 에서 bundle 로 받아온 데이터 전달하기..
                new app_fieldError_not_care_history().execute(call);
            }
        });



        /* 미처리 버튼 */
        adapter.setUndisposed_btn_listener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int)v.getTag();
                final Trouble_HistoryListVO thlvo = adapter.resultItem(pos);
                String up = null;

                dialog = new Dialog_Unpro_Notice(context
                        , thlvo.getUnpro_notice()
                        , thlvo.getReg_date()
                        , thlvo.getReg_time()
                        , thlvo.getReg_emp_id()
                        , thlvo.getUnit_before_id()
                        , thlvo.getTrouble_high_cd()
                        , thlvo.getTrouble_low_cd()
                        , thlvo.getTransp_bizr_id()
                        , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.return_msg().length() == 0){
                            Toast.makeText(getContext(), "메세지를 입력하세요.", Toast.LENGTH_SHORT).show();
                        }else {
                            String msgg = dialog.return_msg();
                            Log.d("msg", msgg);
                            updateMap.put("unpro_notice", msgg);
                            updateMap.put("reg_date", thlvo.getReg_date());
                            updateMap.put("reg_time", thlvo.getReg_time());
                            updateMap.put("reg_emp_id", thlvo.getReg_emp_id());
                            updateMap.put("unit_code_before", thlvo.getUnit_before_id() == null ? "" : thlvo.getUnit_before_id());
                            updateMap.put("trouble_high_cd_before", thlvo.getTrouble_high_cd());
                            updateMap.put("trouble_low_cd_before", thlvo.getTrouble_low_cd());
                            updateMap.put("transp_bizr_id", thlvo.getTransp_bizr_id());

                            Log.d("updateMap>",updateMap+"");

                            new app_trouble_history_unpro_notice_editText_update().execute();
                            dialog.dismiss();
                            RestartFragment();
                        }
                    }
                },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                dialog.setCancelable(true);
                dialog.show();

                DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = dialog.getWindow();
                window.setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //edit: 다이얼로그가 사라지면 프래그먼트 리스타트됌
              /*  dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        restartFragment();
                    }
                });*/
            }
        });







        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int postion = (int) v.getTag();
                Trouble_HistoryListVO thlvo = adapter.resultItem(postion);

                Fragment_trobule_care fragment = new Fragment_trobule_care();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Obj",thlvo);
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frage_change,fragment)
                        .commit();

            }
        });

        adapter.setEqual_bus_btn_Listener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = (int) v.getTag();
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

        adapter.setCall_text_listener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tel_num = view.getTag().toString().replaceAll("-","");
                String tel = "tel:"+tel_num;
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });

        adapter.setMove_info_btn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = (int) view.getTag();
                final Trouble_HistoryListVO thlvo = adapter.resultItem(postion);
                deleteVo = adapter.resultItem(postion);

                ArrayList<Trouble_HistoryListVO> list = adapter.resultList();
                String emp_id = pref.getString("emp_id","inter");

                final HashMap<String,Object> map = new HashMap<>();
                map.put("move_emp_id",emp_id);
                map.put("reg_date",thlvo.getReg_date());
                map.put("reg_time",thlvo.getReg_time());
                map.put("reg_emp_id",thlvo.getReg_emp_id());
                // N = 이동 시작하는 경우
                String msg_check = "N";
                // 내가 작업중인지 아닌지
                String msg_check2 = "N";
                // 내가 다른곳 지원 나갔는데 또 다른운수사로 그냥 이동하는거 체크
                msg_check3 = "Y";

                // M = 다른 운수사로 이동중인데 선택한 운수사로 이동하는 경우
                for(int i=0; i<list.size();i++){

                    // move_emp_id 가 있고 내 아이디랑 동일하면서 지원중인건
                    if(list.get(i).getMove_emp_id() != null && list.get(i).getMove_emp_id().equals(emp_id) && list.get(i).getFirst_yn().equals("N")){
                        msg_check3 = "N";
                        deleteVo = list.get(i);
                    }

                    // move_emp_id 가 있고 내 아이디랑 동일하면서 최초등록건
                    if(list.get(i).getMove_emp_id() != null && list.get(i).getMove_emp_id().equals(emp_id) && list.get(i).getFirst_yn().equals("Y")){
                        map.put("m_move_emp_id",list.get(i).getMove_emp_id());
                        map.put("m_reg_date",list.get(i).getReg_date());
                        map.put("m_reg_time",list.get(i).getReg_time());
                        map.put("m_reg_emp_id",list.get(i).getReg_emp_id());
                        msg_check = "M";
                        msg_check2 = "Y";
                    }
                };

                // C = 다른 운수사로 이동중에서 선택한 운수사를 이미 다른작업자가 이동중인데 자신이 이동하는경우
                if(msg_check2.equals("Y") && thlvo.getMove_emp_id() !=null && !thlvo.getMove_emp_id().equals(emp_id)){
                    msg_check = "C";
                }

                // A = 선택한 운수사를 이미 다른작업자가 이동중인데 자신이 이동하는경우
                if(!msg_check2.equals("Y") && thlvo.getMove_emp_id() !=null && !thlvo.getMove_emp_id().equals(emp_id)){
                    msg_check = "A";
                }

                // S = 선택한 운수사를 이미 이동중이여서 이동취소하는경우
                if(thlvo.getMove_emp_id() !=null && thlvo.getMove_emp_id().equals(emp_id)){
                    map.put("move_emp_id","");
                    msg_check = "S";
                }

                map.put("msg_check",msg_check);
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                alertdialog.setTitle("위치 이동하기");

                String p_btn_text = "지원하기";
                String n_btn_text = "이동하기";

                switch (msg_check){
                    case "N":
                        alertdialog.setMessage("해당 운수사로 이동하시겠습니까?");
                        break;
                    case "M" :
                        alertdialog.setMessage("이미 이동중입니다. 다른곳으로 이동하시겠습니까?");
                        break;
                    case "A":
                        alertdialog.setMessage("다른 작업자가 이미 이동중입니다. 이동하시겠습니까?");
                        break;
                    case "S":
                        alertdialog.setMessage("해당 운수사로 이미 이동중입니다. 이동 취소하시겠습니까?");
                        n_btn_text = "취소하기";
                        break;
                    case "C":
                        alertdialog.setMessage("다른 작업자가 이미 이동중입니다. 이동하시겠습니까?");
                        break;
                }



                alertdialog
                        .setCancelable(false)
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                if(!msg_check.equals("S") && !msg_check.equals("N") && !msg_check.equals("M") ){
                    alertdialog.setPositiveButton(p_btn_text,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String emp_id = pref.getString("emp_id","inter");
                                    ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                    Call<Boolean> call = erp.trouble_move_together_insert(map,thlvo, emp_id );
                                    new Fragment_trouble_list.trouble_move_together().execute(call);
                                    dialog.cancel();
                                }
                            });
                }
                alertdialog.setNegativeButton(n_btn_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                                Call<Boolean> call = erp.trouble_move_change(map,deleteVo,msg_check3);
                                new Fragment_trouble_list.Update_Move_Emp_id().execute(call);
                                dialog.cancel();
                            }
                        });
                AlertDialog adialog = alertdialog.create();
                adialog.show();
            }
        });

        listView = (ListView)view.findViewById(R.id.my_error_list);

        trouble_bus.performClick();

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Trouble_HistoryListVO>> call = erp.get_trouble_count(emp_id);
        new Fragment_trouble_list.Get_Trouble_Count().execute(call);

        return view;
    }


    /*최근 장애건수 버튼을 누르면 실행되는 메소드*/
    private class app_fieldError_not_care_history extends AsyncTask<Call, Void, List<Trouble_HistoryListVO>>{

        @Override
        protected List<Trouble_HistoryListVO> doInBackground(Call... calls) {
            Call<List<Trouble_HistoryListVO>> call= calls[0];
            try {
                Response<List<Trouble_HistoryListVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trouble_HistoryListVO> trouble_historyListVOS) {
            super.onPostExecute(trouble_historyListVOS);

            if (trouble_historyListVOS == null){
                Toast.makeText(context, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
                //Toast.makeText(context, "데이터를 확인합니다.", Toast.LENGTH_SHORT).show();
            }

            View view1= getLayoutInflater().inflate(R.layout.recent_error_list_dialog_layout, null);

            Log.d("List Size :::::: ", trouble_historyListVOS.size()+"");
            Log.d("List String :::::: ", trouble_historyListVOS+"");

            recentErrorListItems= new ArrayList<>();
            for (int i=0; i<trouble_historyListVOS.size(); i++){
                recentErrorListItems.add(new RecentErrorListItems(trouble_historyListVOS.get(i).getReg_date()
                                                                 ,trouble_historyListVOS.get(i).getReg_emp_name()
                                                                 ,trouble_historyListVOS.get(i).getUnit_before_id()
                                                                 ,trouble_historyListVOS.get(i).getCare_date()
                                                                 ,trouble_historyListVOS.get(i).getCare_emp_name()
                                                                 ,trouble_historyListVOS.get(i).getUnit_after_id()
                                                                 ,trouble_historyListVOS.get(i).getBusoff_name()
                                                                 ,trouble_historyListVOS.get(i).getGarage_id()
                                                                 ,trouble_historyListVOS.get(i).getRoute_num()
                                                                 ,trouble_historyListVOS.get(i).getUnit_name()
                                                                 ,trouble_historyListVOS.get(i).getTrouble_high_name()
                                                                 ,trouble_historyListVOS.get(i).getTrouble_low_name()
                                                                 ,trouble_historyListVOS.get(i).getTrouble_care_name()
                                                                 ,trouble_historyListVOS.get(i).getNotice()));

            }
            recycler_RecentError= (RecyclerView) view1.findViewById(R.id.recent_error_recyclerview_dialog);
            recentAdapter1= new RecentErrorListAdapter(context, recentErrorListItems);
            recycler_RecentError.setAdapter(recentAdapter1);

            AlertDialog.Builder recentDialog= new AlertDialog.Builder(context);
            recentDialog.setView(view1);
            recentDialog.setIcon(R.drawable.ic_error);
            recentDialog.setTitle("최근 6개월 장애건수");
            recentDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "데이터를 확인하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            recentDialog.show();

        }
    }


    private class app_trouble_history_unpro_notice_editText_update extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getContext().getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            Call<Boolean> call = erp.trouble_history_undisposed_msg_update(updateMap);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Log.d("call>", "success");
                    Log.d("call>", response+"");
                    Log.d("call>", updateMap+"");
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("call>", t.getMessage());
                }
            });

            return null;
        }
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
            Call<List<Trouble_HistoryListVO>> call = erp.getfield_my_error_list(emp_id , service_id);
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

    private void MakeMyErrorList(List<Trouble_HistoryListVO> list) {
        adapter.clearItem();
        listView.setAdapter(adapter);

        if(null == list || 0 == list.size()){
            Toast.makeText(context,"데이터가 없습니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        for(Trouble_HistoryListVO i : list){
            adapter.addItem(i);
        }
    }

    private class Get_Trouble_Count extends AsyncTask<Call , Void , List<Trouble_HistoryListVO>>{
        @Override
        protected List<Trouble_HistoryListVO> doInBackground(Call... calls) {
            try{
                Call<List<Trouble_HistoryListVO>> call = calls[0];
                Response<List<Trouble_HistoryListVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trouble_HistoryListVO> trouble_historyListVOS) {
            if (null != trouble_historyListVOS) {
                Trouble_HistoryListVO vo = trouble_historyListVOS.get(0);

                bus_count.setText(text_plus(vo.getBus()));
                nms_count.setText(text_plus(vo.getJib()));
                chager_count.setText(text_plus(vo.getCharge()));
                bit_count.setText(text_plus(vo.getBit()));
                nomal_count.setText(text_plus(vo.getIb()));
            }
        }
    }

    private class Update_Move_Emp_id extends AsyncTask<Call, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            Fragment fragment = new Fragment_trouble_list();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frage_change,fragment);
            ft.commit();
        }
    }

    private class trouble_move_together extends AsyncTask<Call , Void , Boolean>{
        @Override
        protected Boolean doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            Fragment fragment = new Fragment_trouble_list();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frage_change,fragment);
            ft.commit();
        }
    }

    String text_plus (String text){
        return "(" + text + ")";
    }


    //note: 미처리 메세지 입력 후, 해당 fragment 로 이동 -> 업데이트된 데이터 표출됨.
    public void RestartFragment(){
       // service_id = null;
        switch (service_id){
            case "01": //버스
                trouble_bus.setChecked(true);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(false);
                new Filed_MyErrorList().execute();
                break;
            case "02": //집계
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(true);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(false);
                new Filed_MyErrorList().execute();
                break;
            case "04": //충전기
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(true);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(false);
                new Filed_MyErrorList().execute();
                break;
            case "13": //bit
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(true);
                trouble_nomal.setChecked(false);
                new Filed_MyErrorList().execute();
                break;
            case "09": //일반업무
                trouble_bus.setChecked(false);
                trouble_nms.setChecked(false);
                trouble_chager.setChecked(false);
                trouble_bit.setChecked(false);
                trouble_nomal.setChecked(true);
                new Filed_MyErrorList().execute();
                break;
            case "edu": //교육 //todo:교육은 service_id 가 뭐임??
                Toast.makeText(context, "교육으로 이동??", Toast.LENGTH_SHORT).show();
                break;
        }
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();
    }

}
