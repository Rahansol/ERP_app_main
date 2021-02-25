package app.erp.com.erp_app.document_care.myfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.callcenter.Call_Center_Activity;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;
import app.erp.com.erp_app.jsonparser.JSONParser;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstallationSignActivity2 extends AppCompatActivity {

    private SignaturePad mSignaturePad;
    private Button mClearBtn, mSaveBtn;
    private ArrayList<String> path_list= new ArrayList<String>();
    private Map<String, Object> sign_map= new HashMap<>();

    private TextView tv_prj, tv_dtti, tv_busoff_name, tv_garage_name;
    private EditText etName, etPhoneNum;
    private ImageView ivInterpassSign;

    //단말기, 케이블 목록 리사이클러뷰
    private ArrayList<CableItems> cableItems;
    private RecyclerView recycler;
    private CableAdapter cableAdapter;

    private static String result_path;

    Context mContext;
    Context testContext;
    SharedPreferences pref;


    private static String st_bus_id, st_table_name, st_reg_dtti,st_today,today_without_time, st_transo_bizr_id, st_job_type, st_emp_id, st_bus_num, johab_name, st_busoffName, st_garageID, st_garageName, signImagePath, signEmpName, signEmpPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_sign2);
        PermissionCheck();

        getSupportActionBar().setTitle("2단계");

        InstallationSignActivity step1Activity= (InstallationSignActivity)InstallationSignActivity.step1Activity;
        Installation_List_Signature_Activity AActivity= (Installation_List_Signature_Activity) Installation_List_Signature_Activity.AActivity;


        mContext= this;

        pref= getSharedPreferences("user_info", MODE_PRIVATE);
        st_emp_id= pref.getString("emp_id","");

        ivInterpassSign= findViewById(R.id.iv_interpass_sign);
        Picasso.get()
                .load(R.drawable.interpass_sign)
                .into(ivInterpassSign);

        Intent i= getIntent();
        String st_prj= i.getStringExtra("prj");
        st_table_name= i.getStringExtra("table_name");
        st_busoffName= i.getStringExtra("busoffName");
        st_transo_bizr_id= i.getStringExtra("transp_bizr_id");
        //st_garageID= i.getStringExtra("garageId");
        st_garageName= i.getStringExtra("garageName");
        String st_job_name= i.getStringExtra("job_name");
        st_job_type= i.getStringExtra("job_type");
        st_today= i.getStringExtra("today");
        st_reg_dtti= i.getStringExtra("reg_dtti");
        st_bus_id= i.getStringExtra("bus_id");
        String mPrj_JobName= i.getStringExtra("mPrj_JobName");
        String mPrj_JobName2= mPrj_JobName+" "+"장비인수증";
        st_bus_num=  i.getStringExtra("bus_num");

        tv_prj= findViewById(R.id.tv_Prj);
        tv_prj.setText(mPrj_JobName2);

        tv_dtti= findViewById(R.id.tv_dtti);
        tv_dtti.setText(st_today.substring(0,8)+"  "+st_today.substring(8,10)+":"+st_today.substring(10,12)+":"+st_today.substring(12,14));
        today_without_time= st_today.substring(0,8);

        tv_busoff_name= findViewById(R.id.tv_busoff_name);
        tv_busoff_name.setText(st_busoffName);

        tv_garage_name= findViewById(R.id.tv_garage_name);
        tv_garage_name.setText(st_garageName);

        /*Log.d("세번째 ===============", "");
        Log.d("프로젝트명 ::   ", st_prj);
        Log.d("테이블명 ::     ", st_table_name);
        Log.d("날짜 ::         ", st_reg_dtti);
        Log.d("운수사명 ::     ", st_busoffName);
        Log.d("운수사 아이디:: ", st_transo_bizr_id);
        Log.d("영업소명 ::     ", st_garageName);

        Log.d("작업 ::         ", st_job_name);
        Log.d("작업타입 ::     ", st_job_type);
        Log.d("버스넘- 날짜 ::  ", st_reg_dtti);
        Log.d("버스 아이디 ::  ", st_bus_id);
        Log.d("BUS NUM ::  ", st_bus_num);
        Log.d("emp_id ::  ", st_emp_id);   //msookim*/




        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.step2_unitList(st_table_name, st_transo_bizr_id, st_job_type, st_bus_num, st_garageName);
        new step_unitList().execute(call);





        etName= findViewById(R.id.et_name);            //성명
        etPhoneNum= findViewById(R.id.et_phone_num);   //연락처

        mClearBtn= findViewById(R.id.btn_clear);
        mSaveBtn= findViewById(R.id.btn_save);   //완료버튼..
        mSignaturePad= findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                mSaveBtn.setEnabled(true);
                mClearBtn.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveBtn.setEnabled(false);
                mClearBtn.setEnabled(false);
            }
        });

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });


        // [완료]버튼을 클릭하면
        // 1) 서명.png 저장
        // 2) INSERT & UPDATE....
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EDIT TEXT =>  ","성명: "+etName.getText().toString()+",  연락처: "+etPhoneNum.getText().toString());

                if (etName.getText().toString().length()==0 || etPhoneNum.getText().toString().length()==0){
                    Toast.makeText(InstallationSignActivity2.this, "성명과 연락처를 기재하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    //사인 저장..
                    Bitmap signatureBitmap= mSignaturePad.getTransparentSignatureBitmap();
                    if (!addJpgSignatureGallery(signatureBitmap).equals("false")){
                        Toast.makeText(InstallationSignActivity2.this, "작성하신 사인이 정상적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent result= new Intent();
                        result.putExtra("path", addJpgSignatureGallery(signatureBitmap));
                        setResult(RESULT_OK, result);

                        // UPDATE 문 실행..
                        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                        Call<List<Bus_OfficeVO>> call= erp.insert_installation_lists(st_table_name
                                                                                   , st_today
                                                                                   , st_transo_bizr_id
                                                                                   , st_job_type
                                                                                   , st_emp_id, st_bus_num
                                                                                   , st_busoffName
                                                                                   , st_garageName
                                                                                   , etName.getText().toString()
                                                                                   , etPhoneNum.getText().toString()
                                                                                   , result_path );   // 디비에 사진이미지 이름 저장---- 서명.png 값 파라미터에 추가.... #####################################################  ??????????
                        Log.d("result_path ======>  ", result_path+"");
                        call.enqueue(new Callback<List<Bus_OfficeVO>>() {
                            @Override
                            public void onResponse(Call<List<Bus_OfficeVO>> call, Response<List<Bus_OfficeVO>> response) {
                                Toast.makeText(InstallationSignActivity2.this, "업데이트 완료!", Toast.LENGTH_SHORT).show();

                                new ImageUploadJson().execute();
                                step1Activity.finish();
                                AActivity.finish();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<List<Bus_OfficeVO>> call, Throwable t) {
                                Toast.makeText(InstallationSignActivity2.this, "업데이트 실패 :(", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //finish();


                    }else {
                        Toast.makeText(InstallationSignActivity2.this, "사인 저장 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });

    }//onCreate....



    //2단계 단말기, 케이블 목록
    public class step_unitList extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("예외: ", e+"");
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS==null){
                Toast.makeText(mContext, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
                cableItems= new ArrayList<>();
                for (int i=0; i<bus_officeVOS.size(); i++){

                    cableItems.add(new CableItems(bus_officeVOS.get(i).getText(), bus_officeVOS.get(i).getVal()+" 개"));
                }
                cableAdapter= new CableAdapter(mContext, cableItems);
                recycler= findViewById(R.id.recycler_unit);
                recycler.setAdapter(cableAdapter);

            }
        }
    }






    public File getAlbumStorageDir(String albumName){
        File file= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()){
            Log.d("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException{
        Bitmap newBitmap= Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(newBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream= new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);   //bitmap.compress= bitmap 압축
        stream.close();
    }


    public String addJpgSignatureGallery(Bitmap signture){
        result_path="";
        try {
            File photo= new File(getAlbumStorageDir("SignaturedPad"), String.format("Signature_%d.png", System.currentTimeMillis()));
            Log.d("사인파일 이름=> ", photo+"");        //   /storage/emulated/0/Pictures/SignaturedPad/Signature_1613953381718.png
            saveBitmapToJPG(signture, photo);
            scanMediaFile(photo);
            result_path= photo.getAbsolutePath()+ "&" + ("nas_image/image/IERP/sign/" +  st_table_name  + "/" + today_without_time + "/" + st_table_name + "_" + today_without_time + "_" + st_transo_bizr_id  + "_sign.png").replaceAll("/","%");
            path_list.add(result_path);
            int cnt= 0;
            for (String str :  path_list){
                //cnt++;
                System.out.println(cnt+" :  "+str);
                sign_map.put("sign", str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result_path= "false";
        }
        return  result_path;
    }




    public void scanMediaFile(File photo){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri= Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        InstallationSignActivity2.this.sendBroadcast(mediaScanIntent);
    }

    void PermissionCheck(){
        PermissionListener permissionListener= new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String savePath= Environment.getExternalStoragePublicDirectory("SignaturePad").toString();
                File dir= new File(savePath);
                if (!dir.exists()){
                    dir.mkdirs();   //폴더생성
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("프로젝트 업무를 위해 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한]에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }



    //이미지 업로드
    private class ImageUploadJson extends AsyncTask<Void, Void, Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog= new ProgressDialog(mContext);
            progressDialog.setMessage("사인 업로드 중....");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                int sign_map_size= sign_map.size();
                Log.d("[sign_map_size] =====> ", sign_map_size+"");
                Log.d("[sign_map] ======>  ",  sign_map+"");
                Boolean jsonObject= JSONParser.sign_uploadImage(sign_map);
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
            if (aBoolean){

                Toast.makeText(mContext,"이미지 업로드 완료" ,Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(mContext,"이미지 업로드 오류 발생 !!" ,Toast.LENGTH_SHORT).show();
                //Log.d("")
            }
        }
    }//ImageUploadJson()...



}//Activity..