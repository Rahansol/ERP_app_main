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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
        vh.etBusNum.setText(item.busNum); //직접입력으로 입력된 번호..
        vh.tv.setText(item.tv);
        vh.ivPreview.setImageURI(item.preview_uri);
        ////  PCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPCPC
        if (item.tv.equals("미리보기 (P,C 둘다)")){
            vh.tv.setText("미리보기");

            vh.etBusNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    final EditText et= new EditText(context);
                    builder.setTitle("일련번호를 입력하세요.");
                    builder.setView(et);
                    //키보드 올리기
                    InputMethodManager imm= (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);  //키보드 올리기 안됨..
                    imm.showSoftInput((View) et.getWindowToken(), 0);
                    et.setRawInputType(InputType.TYPE_CLASS_NUMBER);  //키보드 타입- 숫자
                    builder.setNeutralButton("바코드스캔", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            G.position= position;
                            IntentIntegrator integrator= new IntentIntegrator(myPageFragment1.getActivity());
                            integrator.forSupportFragment(myPageFragment1).initiateScan();
                            myPageFragment1.startActivityForResult(integrator.createScanIntent(), 600);
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

                                //입력 후 키보드 내리기
                                InputMethodManager imm= (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

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
            vh.ivTakePic.setText("사진선택");
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
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            setImageUri();
                            if (imgUri!=null) intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                            //((Activity)context).startActivityForResult(intent, 200);
                            myPageFragment1.startActivityForResult(intent, 200+position);   //intent 로 전달할때 position 값도 같이 전달..
                        }
                    });
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

                }
            });
////  CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
        }else if (item.tv.equals("해당없음  (C 만)")){
            vh.tv.setText("해당없음");

            //일련번호 다이얼로그
            vh.etBusNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    final EditText et= new EditText(context);
                    builder.setTitle("일련번호 입력").setMessage("일련번호를 입력하세요.");
                    builder.setView(et);
                    //키보드 올리기
                    InputMethodManager imm= (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);   //키보드 올리기 안됨..
                    imm.showSoftInput((View) et.getWindowToken(), 0);
                    et.setRawInputType(InputType.TYPE_CLASS_NUMBER);  //키보드 타입- 숫자
                    builder.setNeutralButton("바코드스캔", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            G.position= position;
                            IntentIntegrator integrator= new IntentIntegrator(myPageFragment1.getActivity());
                            integrator.forSupportFragment(myPageFragment1).initiateScan();
                            myPageFragment1.startActivityForResult(integrator.createScanIntent(), 600);
                        }
                    });
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String st_unit_num= et.getText().toString();

                            if (et.getText().length()!=0){
                                //et.setTextColor(Integer.parseInt("#E91E63"));  // 텍스트 색 변경
                                item.busNum= et.getText().toString();
                                vh.etBusNum.setText(item.busNum);

                                //입력 후 키보드 내리기
                                InputMethodManager imm= (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

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

            vh.ivTakePic.setText("해당없음");
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
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            setImageUri();
                            if (imgUri!=null) intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                            //((Activity)context).startActivityForResult(intent, 200);
                            myPageFragment1.startActivityForResult(intent, 200+position);
                        }
                    });
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
            //this.iv_barcode= itemView.findViewById(R.id.iv_barcode);
            this.bc= itemView.findViewById(R.id.bc);
        }
    }//VH..





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

            imgUri= FileProvider.getUriForFile(context, context.getPackageName(), imageFile);  // imgUri 작업 끝
            Log.d("PATH TEST : >>>> ", "["+imgUri+"]["+imageFile+"]");
        }
        new AlertDialog.Builder(context).setMessage(imgUri.toString()).create().show();  //작업확인
    }










}
