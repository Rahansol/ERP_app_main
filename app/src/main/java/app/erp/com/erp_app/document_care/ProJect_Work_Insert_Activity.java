package app.erp.com.erp_app.document_care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Prj_Work_Insert_PagerAdapter;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;


//작업 [등록] 버튼을 누르면 나오는 화면 -> 등록화면
public class ProJect_Work_Insert_Activity extends AppCompatActivity {

    private ViewPager insert_view_pager;
    private SharedPreferences pref;
    private Prj_Work_Insert_PagerAdapter adapter = new Prj_Work_Insert_PagerAdapter(getSupportFragmentManager());
    private Context mcontext;
    private ProJectVO pvo;

    private  String prj_code, get_today ,prj_seq,area_code,sub_area_code,office_group;

    private ERP_Spring_Controller erp;
    private List<ProJectVO> list_size;

    private Map<String, Object> request_map;
    private Map<String, Object> image_path_map;
//    private Map<String, Object> radio_map;
//    private Map<String, Object> item_check_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_work_insert);
        mcontext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        pvo = (ProJectVO) intent.getExtras().get("pvo");
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        //액션 바 설정
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(pvo.getPrj_name());

        pref = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String emp_id = pref.getString("emp_id",null);

//        전역변수 설정
        prj_code = pvo.getBase_infra_code() + pvo.getArea_code() + pvo.getSub_area_code() + pvo.getPrj_seq();
        prj_seq = pvo.getPrj_seq();
        area_code = pvo.getArea_code();
        sub_area_code = pvo.getSub_area_code();

        String unit_version = "";
        if(pvo.getArea_code().equals("01") && pvo.getPrj_seq().equals("1")){
            unit_version = "1.5";
        }else if(pvo.getArea_code().equals("01") && pvo.getPrj_seq().equals("2")){
            unit_version = "2.0";
        }else if(pvo.getArea_code().equals("01") && pvo.getPrj_seq().equals("3")){
            unit_version = "3.0";
        }else if(pvo.getArea_code().equals("16") && pvo.getPrj_seq().equals("1")){
            unit_version = "2.0";
        }

        Log.d("version :::::::::",unit_version);

        Call<List<ProJectVO>> call2 = erp.app_project_item_list(area_code , sub_area_code, pvo.getPrj_seq(),unit_version);
        new ProJect_Work_Insert_Activity.get_project_item_list().execute(call2);

        // 뷰페이지 컨트롤 버튼
        final Button viewpager_beforn = (Button)findViewById(R.id.viewpager_beforn);
        viewpager_beforn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = insert_view_pager.getCurrentItem()-1;
                if(pos==-1)pos=0;
                insert_view_pager.setCurrentItem(pos,true);
            }
        });
        final Button viewpager_next = (Button)findViewById(R.id.viewpager_next);
        viewpager_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = insert_view_pager.getCurrentItem()+1;
                if(pos==3)pos=2;
                insert_view_pager.setCurrentItem(pos,true);
            }
        });

        // 데이터 등록 버튼
        final Button prj_request_btn = (Button)findViewById(R.id.prj_request_btn);
        prj_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_map = adapter.return_all_request_map();
                request_map.put("area_code",area_code);
                request_map.put("sub_area_code",sub_area_code);
                request_map.put("prj_seq",prj_seq);
                image_path_map = adapter.return_imgage_map();

                String transp_bizr_id = (String) request_map.get("transp_bizr_id");
                String bus_id = (String) request_map.get("bus_id");
                if(null == transp_bizr_id){
                    Toast.makeText(mcontext,"조합 , 운수사을 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if( null == bus_id){
                    Toast.makeText(mcontext,"차량을 선택해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                final AlertDialog.Builder a_builder = new AlertDialog.Builder(mcontext);
                a_builder.setTitle("프로젝트 업무 등록");
                a_builder.setMessage("작성하신 업무 내역을 등록 하시겠습니까 ?");
                a_builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ProJect_Work_Insert_Activity.ImageUploadJson().execute();
                            }
                        });
                a_builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                a_builder.setCancelable(false);
                a_builder.show();
            }
        });
        viewpager_beforn.setVisibility(View.GONE);
        prj_request_btn.setVisibility(View.GONE);

        // 프래그먼트 뷰페이저
        insert_view_pager = (ViewPager)findViewById(R.id.insert_view_pager);
        insert_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int pos) {
                switch (pos){
                    case 0 :
                        viewpager_beforn.setVisibility(View.GONE);
                        viewpager_next.setVisibility(View.VISIBLE);
                        prj_request_btn.setVisibility(View.GONE);
                        break;
                    case 1 :
                        viewpager_beforn.setVisibility(View.VISIBLE);
                        viewpager_next.setVisibility(View.VISIBLE);
                        prj_request_btn.setVisibility(View.GONE);
                        break;
                    case 2:
                        viewpager_beforn.setVisibility(View.VISIBLE);
                        viewpager_next.setVisibility(View.GONE);
                        prj_request_btn.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private class ImageUploadJson extends AsyncTask<Void , Void , Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("이미지 업로드 중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Boolean jsonObject = JSONParser.uploadImage(image_path_map);
                if (jsonObject != null)
//                    Log.d(" result ::" , "" + jsonObject.getString("result").equals("success"));
                    return jsonObject;
            } catch (Exception e) {
                Log.i("frag_TAG", "Error : " + e.getLocalizedMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();
            Log.d(" aBoolean::" , aBoolean.toString());
            if(aBoolean){
                insert_data_project_work();
                Toast.makeText(mcontext,"이미지 업로드 완료" ,Toast.LENGTH_SHORT).show();
            }else{
//                insert_data_project_work();
                Toast.makeText(mcontext,"이미지 업로드 오류 발생" ,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class get_project_item_list extends AsyncTask<Call ,Void, List<ProJectVO>> {
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if(proJectVOS != null){
                ArrayList<ProJectVO> insert_1 = new ArrayList<>();
                ArrayList<ProJectVO> insert_2 = new ArrayList<>();
                ArrayList<ProJectVO> insert_3 = new ArrayList<>();

                for(int i=0 ;i<proJectVOS.size();i++){
                    switch (proJectVOS.get(i).getPage()){
                        case "1" :
                            insert_1.add(proJectVOS.get(i));
                            break;
                        case "2" :
                            insert_2.add(proJectVOS.get(i));
                            break;
                        case "3" :
                            insert_3.add(proJectVOS.get(i));
                            break;
                    }
                }
                adapter.addfrag(new Fragment_Project_Work_Insert_1().shareMyString(insert_1,pvo));
                adapter.addfrag(new Fragment_Project_Work_Insert_2().shareMyString(insert_2,pvo));
                adapter.addfrag(new Fragment_Project_Work_Insert_3().shareMyString(insert_3,pvo));

                insert_view_pager.setAdapter(adapter);
                insert_view_pager.setOffscreenPageLimit(3);
            }
        }
    }

    private class insert_project_work_data extends AsyncTask<Call , Void , Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("데이터 업로드 중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
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
            if (progressDialog != null)
                progressDialog.dismiss();

            if(aBoolean){
                Toast.makeText(mcontext , "데이터 업로드 완료",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(mcontext , "데이터 업로드 오류",Toast.LENGTH_SHORT).show();
                finish();
            }
//            frage_chagne();
        }
    }

    public void insert_data_project_work(){
        Call<Boolean> insert_call = erp.insert_project_work_data(request_map);
        new ProJect_Work_Insert_Activity.insert_project_work_data().execute(insert_call);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2 , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_btn :
                new AlertDialog.Builder(this)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(ProJect_Work_Insert_Activity.this , LoginActivity.class );
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void view_control_for_activity(boolean view_fag){
        adapter.view_control(view_fag);
    }
}
