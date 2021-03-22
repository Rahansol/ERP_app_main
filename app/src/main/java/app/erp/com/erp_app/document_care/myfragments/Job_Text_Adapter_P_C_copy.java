package app.erp.com.erp_app.document_care.myfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.erp.com.erp_app.CustomScannerActivity;
import app.erp.com.erp_app.R;

public class Job_Text_Adapter_P_C_copy extends RecyclerView.Adapter {
    Context context;
    ArrayList<JobTextItems> items;
    MyPageFragment1_copy myPageFragment1_copy;

    SharedPreferences img_pref, barcode_type_pref;
    SharedPreferences.Editor editor;

    ArrayList[] arrayLists={};

    Uri imgUri;  //캡쳐한 이미지 경로 Uri

    Uri imageUri;  //캡쳐한 이미지 경로 Uri
    String mCurrentPhotoPath;

    EditText et;


    public Job_Text_Adapter_P_C_copy() {
    }


    public Job_Text_Adapter_P_C_copy(Context context, ArrayList<JobTextItems> jobTextItems, MyPageFragment1_copy myPageFragment1_copy) {
        this.context= context;
        this.items= jobTextItems;
        this.myPageFragment1_copy= myPageFragment1_copy;
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
        //Boolean pc= true;
        Boolean p_c= item.tv.equals("미리보기 (P,C 둘다)");
        Boolean c= item.tv.equals("해당없음  (C 만)");
        vh.tvJobText.setText(item.jobText);
        vh.etBusNum.setText(item.busNum); //직접입력으로 입력된 번호..
        //vh.etBusNum.setText(G.BARCODE);  //바코드스캔으로 입력된 번호..
        vh.tv.setText(item.tv);
       // vh.itemView.setTag(item.takePic);

        /*switch (pc){
            case item.tv.equals("미리보기 (P,C 둘다)")==true:
        }
*/



////  PCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPC
        if (item.tv.equals("미리보기 (P,C 둘다)")){
            vh.tv.setText("미리보기");
            //[바코드체크]
            if (G.BARCODE!=null){
                vh.etBusNum.setText(item.busNum);
            }else {
                vh.etBusNum.setText(item.busNum);
            }
            vh.etBusNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    et= new EditText(context);
                    builder.setTitle("일련번호를 입력하세요.");
                    builder.setView(et);
                    builder.setNeutralButton("스캐너", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            G.position= position;
                            IntentIntegrator integrator= new IntentIntegrator(myPageFragment1_copy.getActivity());
                            integrator.forSupportFragment(myPageFragment1_copy).initiateScan();
                            myPageFragment1_copy.startActivityForResult(integrator.createScanIntent(), 600);
                        }
                    });
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String st_unit_num= et.getText().toString();
                            Log.d("다이얼로그 입력번호값 ==>   ",st_unit_num+"/"+(position));
                            if (et.getText().length()!=0){
                                item.busNum= et.getText().toString();
                                vh.etBusNum.setText(item.busNum);
                                notifyItemChanged(position);
                                Garray.value[Garray.PositionInfo[position][0]]= st_unit_num;
                            }else {

                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
            });
            vh.ivTakePic.setText("사진앨범");   //item.jobText?
            if (G.CAPTURED_IMAGE_BITMAP!=null){
                //Glide.with(context).load(item.preview_uri).into(vh.ivPreview);
                vh.ivPreview.setImageBitmap(G.CAPTURED_IMAGE_BITMAP);
            }else {
                vh.ivPreview.setImageBitmap(G.CAPTURED_IMAGE_BITMAP);
            }
            vh.ivTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.position= vh.getAdapterPosition();
                    //G.position= position+1;             //전역변수 G 클래스에 포지션값 저장
                    Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_menu_camera);
                    builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요");
                    builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진촬영

                            //방법 1)
                            //captureCamera(MediaStore.ACTION_IMAGE_CAPTURE, 200, myPageFragment1_copy);

                            //방법 2)
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //createImageUri();
                            setImageUri();
                            if (imgUri!=null) intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                            //((Activity)context).startActivityForResult(intent, 200);
                            myPageFragment1_copy.startActivityForResult(intent, 200);


                            /*String[] IMAGE_PROJECTION={
                                    MediaStore.Images.ImageColumns.DATA,
                                    MediaStore.Images.ImageColumns._ID
                            };
                            try{
                                Log.d("try체크-1", "try체크");
                                Cursor cursorImages= context.getContentResolver().query(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                        ,IMAGE_PROJECTION
                                        , null
                                        ,null
                                        ,null);
                                if (cursorImages != null && cursorImages.moveToLast()){
                                    imgUri= Uri.parse(cursorImages.getString(0));  //경로
                                    int id= cursorImages.getInt(1);
                                    Log.d("imgUri체크", imgUri+", 아이디: "+id);

                                    int columnIndex= cursorImages.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                    String fileName= cursorImages.getString(columnIndex);
                                    Log.d("fileName--->", fileName+"");
                                    Bitmap bitmap= BitmapFactory.decodeFile(fileName);
                                    Glide.with(context).load(bitmap).into(vh.ivPreview);
                                    cursorImages.close();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/

                        }
                    });

                    builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진앨범 불러오기 실행코드..
                            //Toast.makeText(context, "P22222 ~~~~~~~~~~~~~"+position, Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Intent.ACTION_PICK);
                            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            myPageFragment1_copy.startActivityForResult(intent, 300+position );
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
                }
            });
////  CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
        }else if (item.tv.equals("해당없음  (C 만)")){
            vh.tv.setText("해당없음");
            //[바코드체크]
            if (G.BARCODE!=null){
                vh.etBusNum.setText(item.busNum);
            }else {
                vh.etBusNum.setText(item.busNum);
            }
            //일련번호 다이얼로그
            vh.etBusNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    final EditText et= new EditText(context);
                    builder.setTitle("일련번호 입력").setMessage("일련번호를 입력하세요.");
                    builder.setView(et);
                    builder.setNeutralButton("바코드스캔", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            G.position= position;
                            IntentIntegrator integrator= new IntentIntegrator(myPageFragment1_copy.getActivity());
                            integrator.forSupportFragment(myPageFragment1_copy).initiateScan();
                            myPageFragment1_copy.startActivityForResult(integrator.createScanIntent(), 600);
                        }
                    });
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String st_unit_num= et.getText().toString();

                            if (et.getText().length()!=0){
                                item.busNum= et.getText().toString();
                                vh.etBusNum.setText(item.busNum);
                                notifyItemChanged(position);
                                Garray.value[Garray.PositionInfo[position][0]]= st_unit_num;
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
                    G.position= position;
                    //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "해당없음", Toast.LENGTH_SHORT).show();
                }
            });
            ////////미리보기  (P 만)  // pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp
        }else{
            vh.tv.setText("미리보기");
            vh.etBusNum.setFocusableInTouchMode(false);
            if (G.CAPTURED_IMAGE_BITMAP!=null){
                //Glide.with(context).load(item.preview_uri).into(vh.ivPreview);
                vh.ivPreview.setImageBitmap(G.CAPTURED_IMAGE_BITMAP);
            }else {
                vh.ivPreview.setImageBitmap(G.CAPTURED_IMAGE_BITMAP);
            }
            vh.ivTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.position= position;
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_menu_camera);
                    builder.setTitle("사진선택").setMessage("업로드할 이미지를 선택하세요");
                    builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진촬영
                            //captureCamera(MediaStore.ACTION_IMAGE_CAPTURE, 200, myPageFragment1_copy);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            setImageUri();
                            if (imgUri!=null) intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                            //((Activity)context).startActivityForResult(intent, 200);
                            myPageFragment1_copy.startActivityForResult(intent, 200);




                           /* String[] IMAGE_PROJECTION={
                                    MediaStore.Images.ImageColumns.DATA,
                                    MediaStore.Images.ImageColumns._ID
                            };
                            try{
                                Log.d("try체크-2", "try체크");
                                Cursor cursorImages= context.getContentResolver().query(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                        ,IMAGE_PROJECTION
                                        , null
                                        ,null
                                        ,null);
                                if (cursorImages != null && cursorImages.moveToLast()){
                                    imgUri= Uri.parse(cursorImages.getString(0));  //경로
                                    int id= cursorImages.getInt(1);
                                    Log.d("imgUri체크", imgUri+", 아이디: "+id);

                                    int columnIndex= cursorImages.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                    String fileName= cursorImages.getString(columnIndex);
                                    Log.d("fileName--->", fileName+"");
                                    Bitmap bitmap= BitmapFactory.decodeFile(fileName);
                                    Glide.with(context).load(bitmap).into(vh.ivPreview);
                                    cursorImages.close();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        }
                    });
                    builder.setNegativeButton("사진앨범", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진앨범 불러오기 실행코드..
                            Toast.makeText(context, "사진앨범에서 불러오기: "+position, Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Intent.ACTION_PICK);
                            intent.putExtra("position",position);
                            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            myPageFragment1_copy.startActivityForResult(intent, 300+position );
                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
            });
            vh.ivPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Context context = v.getContext();*/
                }
            });
        }
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return items.size();
    }


    public JobTextItems getItem(int position){
        return items.get(position);
    }


    class VH extends RecyclerView.ViewHolder{

        TextView tvJobText;
        TextView etBusNum;
        TextView ivTakePic;
        TextView tv;
        TextView tv_p;
        ImageView ivPreview;
        //ImageView iv_barcode;
        RelativeLayout bc;

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvJobText= itemView.findViewById(R.id.tv_job_text);
            this.etBusNum= itemView.findViewById(R.id.et_bus_num);
            this.ivTakePic= itemView.findViewById(R.id.iv_take_pic);
            this.tv= itemView.findViewById(R.id.tv);
            this.tv_p= itemView.findViewById(R.id.tv_p);
            this.ivPreview= itemView.findViewById(R.id.iv_preview);
            //this.iv_barcode= itemView.findViewById(R.id.iv__barcode);
            this.bc= itemView.findViewById(R.id.bc);
        }
    }//VH..



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //사진촬영 후 이미지 가져옴
    private void captureCamera(String cameraType , int intenType, MyPageFragment1_copy f1){
        Log.d("itentType", intenType+"");   //200
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(cameraType);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                File photoFile = null;
                try {
  //                  photoFile = createImageFile(intenType);
                    photoFile = createImageFile(intenType);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    Log.d("ininininin","ininin"+photoFile);          //  /storage/emulated/0/IERP/JPEG_20210315_143634.jpg
                    // getUriForFile 의 두 번째 인자는 Manifest provider 의 authorities 와 일치해야 함
                    Uri providerURI = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);
                    imgUri = providerURI;
                    Log.d("image file name :::::: ", ""+imgUri);     //  content://app.erp.com.erp_app/hidden/JPEG_20210315_133420.jpg
                    // 인텐트에 전달할 때는 FileProvider 의 Return 값인 content://로만!!, providerURI 의 값에 카메라 데이터를 넣어 보냄


                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    myPageFragment1_copy.startActivityForResult(takePictureIntent, intenType);
                }
            }
        } else {
            Toast.makeText(context, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile(int intenType) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "";

        imageFileName = "JPEG_" + timeStamp + ".jpg";

        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP", "");   //storageDir= 사진경로

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        Log.d("절대경로",mCurrentPhotoPath+"/////"+storageDir.toString());  //  /storage/emulated/0/IERP/JPEG_20210315_140137.jpg
        Log.d("이미지파일", imageFile+"");       //  /storage/emulated/0/IERP/JPEG_20210315_140137.jpg

        return imageFile;
    }


    private void galleryAddPic(){
        Log.i("galleryAddPic", "galleryAddPic 실행");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File file = new File(mCurrentPhotoPath);
        mediaScanIntent.setData(Uri.fromFile(file));
        Log.d("contentUri>>", Uri.fromFile(file)+"");    //  file:///storage/emulated/0/IERP/JPEG_20210315_133420.jpg
        context.sendBroadcast(mediaScanIntent);                     // 사진을 찍어서 이미지를 저장했으면 -> 이미지 파일이 생겼다고 알림- sendBroadCast
        Toast.makeText(context, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }







    public void createImageUri(){
        File path= Environment.getExternalStorageDirectory();
        path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddmmss");
        String fileName= "IMG_"+sdf.format(new Date())+".jpg";
        File file= new File(path, fileName);

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
            imgUri= Uri.fromFile(file);
        }else {
            imgUri= FileProvider.getUriForFile(context, context.getPackageName(), file);
        }new AlertDialog.Builder(context).setMessage(imgUri.toString()).create().show();
    }



    public void setImageUri(){

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            imgUri = Uri.fromFile(imageFile);
            Log.d("PATH TEST : >->->-> ", "["+imgUri+"]["+imageFile+"]");
        }else {
            imgUri= FileProvider.getUriForFile(context, context.getPackageName(), imageFile);  // imgUri 작업 끝
            Log.d("PATH TEST : >>>> ", "["+imgUri+"]["+imageFile+"]");
        }

        new AlertDialog.Builder(context).setMessage(imgUri.toString()).create().show();  //작업확인
    }










}
