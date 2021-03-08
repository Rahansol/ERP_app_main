package app.erp.com.erp_app.document_care.myfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.erp.com.erp_app.CustomScannerActivity;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.PreviewDialogActivity;

public class Job_Text_Adapter_P_C extends RecyclerView.Adapter {
    Context context;
    ArrayList<JobTextItems> items;
    MyPageFragment1 myPageFragment1;

    ArrayList[] arrayLists={};

    Uri imgUri;  //캡쳐한 이미지 경로 Uri
    String mCurrentPhotoPath;


    public Job_Text_Adapter_P_C() {
    }

    public Job_Text_Adapter_P_C(Context context, ArrayList<JobTextItems> items, MyPageFragment1 myPageFragment1) {
        this.context = context;
        this.items = items;
        this.myPageFragment1= myPageFragment1;
    }


    /*리사이클러 외부(액티비티/프래그먼트)에서 아이템 클릭 이벤트 처리하기*/
    public interface mOnItemClickListener{          //1.커스텀 리스너 인터페이스 정의: 자식(어댑터)안에서 새로운 리스너 인터페이스 정의
        void mOnItemclick(View v, int post);
    }

    private mOnItemClickListener mListener= null;   //2.1 리스너 객체 참조를 저장하는 변수

    public void setmListener(mOnItemClickListener mListener){  //2.2 mOnItemClickListener 리스터 객체 참조를 어댑터에 전달하는 메소드
        this.mListener= mListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.recycler_job_text_item_p_c, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        JobTextItems item= items.get(position);
        vh.tvJobText.setText(item.jobText);
        vh.etBusNum.setText(item.busNum);
        vh.tv.setText(item.tv);

       // vh.itemView.setTag(item.takePic);
        vh.ivPreview.setImageURI(item.preview_uri);


////  PCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPC
        if (item.tv.equals("미리보기 (P,C 둘다)")){
            vh.tv.setText("미리보기");
            //일련번호 다이얼로그
            //vh.etBusNum.setText(Garray.value[G.position]);

            vh.etBusNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    final EditText et= new EditText(context);
                    builder.setTitle("일련번호를 입력하세요.");
                    builder.setView(et);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String st_unit_num= et.getText().toString();
                            Log.d("다이얼로그 입력번호값 ==>   ",st_unit_num+"/"+(position));
                            if (et.getText().length()!=0){
                                //vh.etBusNum.setText(et.getText().toString());
                                item.busNum= et.getText().toString();
                                vh.etBusNum.setText(item.busNum);
                                //vh.etBusNum.setTextColor(Color.parseColor("#E91E63"));
                                notifyItemChanged(position);
                                Garray.value[Garray.PositionInfo[position][0]]= st_unit_num;
                                //notifyItemChanged(Integer.parseInt(st_unit_num));
                                //notifyItemChanged(prePosition);
                            }else {

                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
            });

            vh.ivTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.position= vh.getAdapterPosition();

                    //G.position= position+1;             //전역변수 G 클래스에 포지션값 저장
                    Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_menu_camera);
                    builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요");
                    /*builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진촬영 불러오기 실행코드..
                            Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //카메라앱에에 캡쳐한 이미지를 저장하게 하려면
                            //저장할 이미지의 파일경로 Uri를 미리 지정해야함- why? 카메라앱 촬영이 끝난 다음에 경로를 만들면 저장을 못함.!!!!
                            //사진 저장경로 설정
                            //galleryAddPic();
                            setImageUri();
                            Log.d("imgUri ===========> ", mCurrentPhotoPath+"   tt");
                            if(mCurrentPhotoPath!=null) intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                            Log.d("imgUri ===========> ", imgUri+ "  ss");
                            Log.d("imgUri ===========> ", mCurrentPhotoPath+"   tt");

                            //이미지 저장
                            img_pref= context.getSharedPreferences("img_pref", Context.MODE_PRIVATE);
                            editor= img_pref.edit();
                            editor.putString("camera_type", mCurrentPhotoPath);
                            Log.d("imgUri ===========> ", mCurrentPhotoPath+ "  ss");
                            Log.d("imgUri ===========> ", imgUri+ "  ss");
                            editor.apply();

                            myPageFragment1.startActivityForResult(intent, 200+position);   //결과값 전달
                        }
                    });*/
                    builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진앨범 불러오기 실행코드..
                            //Toast.makeText(context, "P22222 ~~~~~~~~~~~~~"+position, Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Intent.ACTION_PICK);
                            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                            myPageFragment1.startActivityForResult(intent, 300+position );
                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
            });
            /////////  사진 미리보기 클릭
            vh.ivPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    /*SharedPreferences pref= context.getSharedPreferences("imgUri_data", Context.MODE_PRIVATE);
                    String st_imgUri= pref.getString("imgUri", null);
                    Uri imgUri= Uri.parse(st_imgUri);*/

                    /*Intent i = new Intent(context, PreviewDialogActivity.class);   //미리보기 액티비티로 이동
                    context.startActivity(i);*/

                    /*if (imgUri != null){
                        Intent i = new Intent(context, PreviewDialogActivity.class);   //미리보기 액티비티로 이동
                        context.startActivity(i);
                    }else {
                        Toast.makeText(context, "사진을 선택하세요.", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });   ///// 바코드 스캔
            /*vh.iv_barcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "바코드 스캔................!!!!!!!!", Toast.LENGTH_SHORT).show();

                    IntentIntegrator integrator= new IntentIntegrator((Activity) context);
                    integrator.setOrientationLocked(false);        //가로, 세로모드 전환
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);   // ALL 타입지정
                    integrator.setPrompt("Scanning Code");
                    barcode_type_pref= context.getSharedPreferences("barcode_type", context.MODE_PRIVATE);
                    editor= barcode_type_pref.edit();
                    editor.putString("camera_type","bus_num");
                    editor.commit();
                    integrator.setCaptureActivity(CustomScannerActivity.class).initiateScan();
                    Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show();
                    Log.d("dd","fffffffff");
                    myPageFragment1.startActivityForResult(integrator.createScanIntent(), 600);
                    //((Activity)context).startActivityForResult(integrator.createScanIntent(), 600);

                }
            });*/
////  CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
        }else if (item.tv.equals("해당없음  (C 만)")){
            vh.tv.setText("해당없음");
           // vh.tv.setTextColor(Color.parseColor("#000000"));

            //일련번호 다이얼로그
            vh.etBusNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    final EditText et= new EditText(context);
                    builder.setTitle("일련번호 입력").setMessage("일련번호를 입력하세요.");
                    builder.setView(et);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String st_unit_num= et.getText().toString();

                            if (et.getText().length()!=0){
                                //et.setTextColor(Integer.parseInt("#E91E63"));  // 텍스트 색 변경
                                item.busNum= et.getText().toString();
                                vh.etBusNum.setText(item.busNum);
                                //int color= context.getColor(Integer.parseInt("#E91E63"));
                                //vh.etBusNum.setTextColor(Color.parseColor("#E91E63"));
                                notifyItemChanged(position);
                                Garray.value[Garray.PositionInfo[position][0]]= st_unit_num;
                               // notifyItemChanged(Integer.parseInt(st_unit_num));
                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
            });

            //vh.tv.setTextColor(Color.parseColor("#000000"));
           // vh.bc.setBackgroundColor(Color.parseColor("#000000"));
            vh.ivTakePic.setText("해당없음");
            //vh.ivTakePic.setTextColor(Color.parseColor("#000000"));
            vh.ivTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.position= position;
                    //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "해당없음", Toast.LENGTH_SHORT).show();
                }
            });
            vh.iv_barcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "바코드 스캔................!!!!!!!!", Toast.LENGTH_SHORT).show();

                    IntentIntegrator integrator= new IntentIntegrator((Activity) context);
                    //integrator.setRequestCode(600);
                    integrator.setCaptureActivity(CustomScannerActivity.class).initiateScan();
                    myPageFragment1.startActivityForResult(integrator.createScanIntent(), 600);
                    //((Activity)context).startActivityForResult(integrator.createScanIntent(), 600);
                }
            });
            ////////미리보기  (P 만)  // pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp
        }else{
            vh.tv.setText("미리보기");

            vh.etBusNum.setFocusableInTouchMode(false);
            //vh.etBusNum.setTextColor(Color.parseColor("#000000"));
            //notifyItemChanged(position);
            //vh.iv_barcode.setVisibility(View.INVISIBLE);
            vh.ivTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.position= position;
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_menu_camera);
                    builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요");
                    /*builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진촬영 불러오기 실행코드..
                            Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //카메라앱에에 캡쳐한 이미지를 저장하게 하려면
                            //저장할 이미지의 파일경로 Uri를 미리 지정해야함- why? 카메라앱 촬영이 끝난 다음에 경로를 만들면 저장을 못함.!!!!
                            //사진 저장경로 설정
                            setImageUri();
                            Log.d("imgUri ===========> ", mCurrentPhotoPath+"   tt");
                            if(imgUri!=null) intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                            Log.d("imgUri ===========> ", mCurrentPhotoPath+"   tt");
                            Log.d("imgUri ===========> ", imgUri+"   tt");
                            //이미지 저장..
                            img_pref= context.getSharedPreferences("img_pref", Context.MODE_PRIVATE);
                            editor= img_pref.edit();
                            editor.putString("camera_type", imgUri+"");
                            Log.d("imgUri ===========> ", mCurrentPhotoPath+ "  ss");
                            editor.apply();
                            myPageFragment1.startActivityForResult(intent, 200+position);   //결과값 전달
                        }
                    });*/
                    builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진앨범 불러오기 실행코드..
                            Toast.makeText(context, "사진앨범에서 불러오기: "+position, Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Intent.ACTION_PICK);
                            intent.putExtra("position",position);
                            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                            myPageFragment1.startActivityForResult(intent, 300+position );
                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
            });
            vh.ivPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Context context = v.getContext();

                    SharedPreferences pref= context.getSharedPreferences("imgUri_data", Context.MODE_PRIVATE);
                    String st_imgUri= pref.getString("imgUri", null);
                    Uri imgUri= Uri.parse(st_imgUri);


                    if (imgUri != null){
                        Intent i = new Intent(context, PreviewDialogActivity.class);   //미리보기 액티비티로 이동
                        context.startActivity(i);
                    }else {
                        Toast.makeText(context, "사진을 선택하세요.", Toast.LENGTH_SHORT).show();
                    }*/

                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class VH extends RecyclerView.ViewHolder{

        TextView tvJobText;
        TextView etBusNum;
        TextView ivTakePic;
        TextView tv;
        TextView tv_p;
        ImageView ivPreview;
        ImageView iv_barcode;
        RelativeLayout bc;

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvJobText= itemView.findViewById(R.id.tv_job_text);
            this.etBusNum= itemView.findViewById(R.id.et_bus_num);
            this.ivTakePic= itemView.findViewById(R.id.iv_take_pic);
            this.tv= itemView.findViewById(R.id.tv);
            this.tv_p= itemView.findViewById(R.id.tv_p);
            this.ivPreview= itemView.findViewById(R.id.iv_preview);
            this.iv_barcode= itemView.findViewById(R.id.iv_barcode);
            this.bc= itemView.findViewById(R.id.bc);
        }
    }//VH..


    private void scanCode(){
        IntentIntegrator integrator= new IntentIntegrator((Activity) context);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        //integrator.setRequestCode(600);
        integrator.initiateScan();

    }




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 한솔씨코드

    /*private void captureCamera(String cameraType , int intenType, MyPageFragment1 f1){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(cameraType);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile(intenType);
                } catch (IOException ex) {
                    Log.d("captureCamera Error", ex.toString());
                }
                Log.d("ininininin","xxxxxxxxxxxxxxxxx"+photoFile);
                if (photoFile != null) {
                    Log.d("ininininin","ininin"+photoFile);
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    Uri providerURI = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);
                    imgUri = providerURI;
                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    //f1.startActivityForResult(takePictureIntent, intenType);
                    myPageFragment1.startActivityForResult(takePictureIntent, intenType);
                }
            }
        } else {
            Toast.makeText(context, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }*/

    /*public File createImageFile(int intenType) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "";
        if(intenType == 1){
            imageFileName = "JPEG_" + timeStamp + ".jpg";
        }else{
            imageFileName = "MP4_" + timeStamp + ".mp4";
        }
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP");   //storageDir= 사진경로

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);

        return imageFile;
    }*/


    private String galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath); // 지금 전역변수로 파일을 만들죠 ?네 그럼 저 전역변수가
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
        Toast.makeText(context, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
        return mCurrentPhotoPath;
    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 사진촬영 MY 코드
    // 캡쳐된 이미지 uri를 정하는 메소드
    /*public void setImageUri(){

        Toast.makeText(context, "setIamgeUri 실행 토스트!!!", Toast.LENGTH_SHORT).show();

        File path= Environment.getExternalStorageDirectory();
        path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName= "IMG_"+sdf.format(new Date())+".jpg";
        File file= new File(path, fileName);


        // 카메라에 전달해줄 저장 파일경로 = File 객체가 아니라 Uri 객체여야 함
        // File -----> Uri 변환
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N) imgUri= Uri.fromFile(file);
        else {
            imgUri= FileProvider.getUriForFile(context, "app.erp.com.erp_app.provider", file);
        }
        new AlertDialog.Builder(context).setMessage(imgUri.toString()).create().show();
    }*/







    public void setImageUri(){

        Toast.makeText(context, "setIamgeUri 실행 토스트!!!", Toast.LENGTH_SHORT).show();

        String timeStamp= new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
        String imageFileName= "JPEG_"+timeStamp+".jpg";
        File imageFile= null;
        File storageDir= new File(Environment.getExternalStorageDirectory()+"/IERP");   //외부메모리 최상위(root) 경로  //경로: [storage/emulated]

        if (!storageDir.exists()){
            Log.d(" ::: ", storageDir+" 가 존재안함!!");
            storageDir.mkdirs();
        }else {
            Log.d("storageDir========저장경로 =====::: ", storageDir+"");    //storage/emulated/0/IERP  //저장경로 있음
        }
        imageFile= new File(storageDir, imageFileName);
        mCurrentPhotoPath= imageFile.getAbsolutePath();  //이미지 파일의 절대경로?

        //카메라앱에 전달해줄 저장 파일경로= File 객체가 아니라 Uri 객체여야함!!
        // File -----> Uri 변환
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N)  imgUri = Uri.fromFile(imageFile);
        else {

            imgUri= FileProvider.getUriForFile(context, "app.erp.com.erp_app.provider", imageFile);  // imgUri 작업 끝
            Log.d("PATH TEST : >>>> ", "["+imgUri+"]["+imageFile+"]");
        }
        new AlertDialog.Builder(context).setMessage(imgUri.toString()).create().show();  //작업확인
    }










}
