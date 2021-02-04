package app.erp.com.erp_app.document_care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.LoginActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Prj_Doc_Write_PagerAdapter;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.vo.Unit_InstallVO;
import retrofit2.Call;
import retrofit2.Response;

public class ProJect_Doc_Write_Activity extends AppCompatActivity {

    private Context mcontext;
    private ViewPager doc_viewpager;
    private Prj_Doc_Write_PagerAdapter adapter = new Prj_Doc_Write_PagerAdapter(getSupportFragmentManager());
    private ERP_Spring_Controller erp;
    private List<ProJectVO> doc_seq_List = new ArrayList<>();
    private Map<String,Object> sign_map = new HashMap<>();
    private List<String> seq_list = new ArrayList<>();
    private Map<String,Object> request_map = new HashMap<>();
    private ProJectVO pvo;
    private ActionBar ab;
    private String view_tpye;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj_doc_write_activty);
        mcontext = this;
        Intent intent = getIntent();
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        //프래그먼트 뷰페이저
        doc_viewpager = (ViewPager)findViewById(R.id.doc_viewpager);

        List<Map<String, Object>> list = (List<Map<String, Object>>) intent.getSerializableExtra("list1");
        List<Map<String, Object>> list2 = (List<Map<String, Object>>) intent.getSerializableExtra("list2");
        List<Unit_InstallVO> item_list = (List<Unit_InstallVO>) intent.getSerializableExtra("item_vo");
        request_map = (Map<String, Object>)intent.getSerializableExtra("request_map");
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("설치 확인서");

        ArrayList<ProJectVO> list1= new ArrayList<>();
        pvo = (ProJectVO) request_map.get("pvo");

        adapter.addfrag(new Fragment_Project_Doc_Write_1().shareMyString(list,pvo,item_list,request_map));
        adapter.addfrag(new Fragment_Project_Doc_Write_2().shareMyString(list2,item_list, pvo));
        adapter.addfrag(new Fragment_Project_Doc_Write_3().shareMyString(list1,pvo,request_map));

        doc_viewpager.setAdapter(adapter);
        doc_viewpager.setOffscreenPageLimit(3);

        // 뷰페이지 컨트롤 버튼
        final Button viewpager_beforn = (Button)findViewById(R.id.viewpager_beforn);
        viewpager_beforn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = doc_viewpager.getCurrentItem()-1;
                if(pos==-1)pos=0;
                doc_viewpager.setCurrentItem(pos,true);
            }
        });
        final Button viewpager_next = (Button)findViewById(R.id.viewpager_next);
        viewpager_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = doc_viewpager.getCurrentItem()+1;
                if(pos==3)pos=2;
                doc_viewpager.setCurrentItem(pos,true);
            }
        });

        // 상단 textview
        TextView job_name =(TextView)findViewById(R.id.job_name);
        TextView garage_name = (TextView)findViewById(R.id.garage_name);
        TextView busoff_name = (TextView)findViewById(R.id.busoff_name);
        TextView office_group = (TextView)findViewById(R.id.office_group);

        job_name.setText((String)request_map.get("job_name"));
        garage_name.setText((String)request_map.get("garage_name"));
        busoff_name.setText((String)request_map.get("busoff_name"));
        office_group.setText((String)request_map.get("office_group"));

        //화면 타입 변수
        view_tpye = (String)request_map.get("view_type");
        Log.d("view type ::::::::::::::::::::" , view_tpye);

        // 등록 버튼
        final Button prj_request_btn = (Button)findViewById(R.id.prj_request_btn);
        prj_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0; i<doc_seq_List.size();i++){
                    seq_list.add(doc_seq_List.get(i).getDoc_seq());
                }

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String doc_dtti = sdf.format(date);
                String trans_bizr_id = (String)request_map.get("trans_id");
                String path_reg_date = pvo.getReg_date().replaceAll("-","");
                request_map.put("doc_dtti",doc_dtti);

                sign_map = adapter.sign_map_return();

                boolean sign_path_check = false;
                String real_path = "";
                real_path = (String)sign_map.get("real_path");

                Log.d("real_path::",""+ sign_path_check+"1111" + real_path);


                String sign_man_text = (String)sign_map.get("sign_man");
                String sign_man_tel  = (String)sign_map.get("sign_tel");

                if(sign_man_text.equals("") || sign_man_tel.equals("")){
                    Toast.makeText(mcontext,"담당자, 연락처을 입력해 주세요." ,Toast.LENGTH_SHORT).show();
                    return;
                }else if("N".equals(real_path)){
                    Toast.makeText(mcontext,"운수사 사인을 입력해 주세요." ,Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                Set<?> set = sign_map.keySet();
                Iterator<?> it = set.iterator();
                while(it.hasNext()){
                    String key = (String)it.next();
                    sb.append("------------------------------------------------------------\n");
                    sb.append("key = "+key+",\t\t\tvalue = "+sign_map.get(key)+"\n");
                }
                Log.d("sb",""+sb);

                String prj_code = (String)request_map.get("prj_code");
                String sign_save_path = "nas_image/image/IERP/"+prj_code+"/"+path_reg_date+"/"+prj_code+"_"+path_reg_date+"_"+trans_bizr_id+"_"+"sign"+".jpg";
                String replace  = sign_save_path.replaceAll("/","%");
//                String real_path = (String)sign_map.get("real_path");
                sign_map.put("sign",real_path+"&"+replace);

               // System.out.println("sign_map==========================>>>>>>>>>>>>>>>"+sign_map+"");

                request_map.putAll(sign_map);
                String db_path = "project_img/" + prj_code + "/" +path_reg_date+ "/" + prj_code+"_"+path_reg_date+"_"+trans_bizr_id+"_"+"sign"+".jpg";
                request_map.put("sign_path",db_path);

                final AlertDialog.Builder a_builder = new AlertDialog.Builder(mcontext);
                a_builder.setTitle("문서 작성");
                a_builder.setMessage("작성하신 업무 내역을 등록 하시겠습니까 ?");
                a_builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ProJect_Doc_Write_Activity.ImageUploadJson().execute();
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

        if(view_tpye.equals("v")){
            prj_request_btn.setVisibility(View.GONE);
        }else{
            prj_request_btn.setVisibility(View.VISIBLE);
        }

        Call<List<ProJectVO>> call = erp.doc_seq_list(request_map);
        new ProJect_Doc_Write_Activity.doc_seq_list().execute(call);


        doc_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int pos) {
                switch (pos){
                    case 0 :
                        ab.setTitle("설치 확인서");
                        break;
                    case 1 :
                        ab.setTitle("장비 인수증");
                        break;
                    case 2:
                        ab.setTitle("운수사 확인");
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
                Boolean jsonObject = JSONParser.sign_uploadImage(sign_map);
                if (jsonObject != null)
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
            if(aBoolean){
                Toast.makeText(mcontext,"이미지 업로드 완료" ,Toast.LENGTH_SHORT).show();
                Call<Boolean> call = erp.prj_doc_write(request_map,seq_list);

                String delete_file = (String)sign_map.get("real_path");    //이부분은 왜 파일을 삭제하는지?? 파일을 다시 지우고 생성하나???
                File f = new File(delete_file);
                if(f.delete()){
                    Log.d("sign_image","ok" );
                }
                new ProJect_Doc_Write_Activity.prj_doc_write().execute(call);
            }else{
                Toast.makeText(mcontext,"이미지 업로드 오류 발생" ,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class doc_seq_list extends AsyncTask<Call, Void, List<ProJectVO>>{
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
                doc_seq_List = proJectVOS;
            }
        }
    }

    private class prj_doc_write extends AsyncTask<Call,Void,Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("문서 생성중...");
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
                Toast.makeText(mcontext,"문서 생성 완료" ,Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(mcontext,"문서 생성 오류 발생" ,Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void change_title_name (String title_name){
        ab.setTitle(title_name);
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
                                Intent i = new Intent(ProJect_Doc_Write_Activity.this , LoginActivity.class );
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
}