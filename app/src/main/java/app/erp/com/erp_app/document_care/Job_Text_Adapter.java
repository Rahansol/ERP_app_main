package app.erp.com.erp_app.document_care;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment1;

public class Job_Text_Adapter extends RecyclerView.Adapter {

    AlertDialog.Builder builder;

    Context context;
    ArrayList<JobTextItems> items;
    MyPageFragment1 myPageFragment1;   //?

    public Job_Text_Adapter() {
    }

    public Job_Text_Adapter(Context context, ArrayList<JobTextItems> items) {
        this.context = context;
        this.items = items;
    }



    /*리사이클러 외부(액티비티/프래그먼트)에서 아이템 클릭 이벤트 처리하기*/
    public interface mOnItemClickListener{      //1.커스텀 리스너 인터페이스 정의: 자식(어댑터)안에서 새로운 리스너 인터페이스 정의
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
        View itemView= inflater.inflate(R.layout.recycler_job_text_item, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        JobTextItems item= items.get(position);
        vh.tvJobText.setText(item.jobText);
        vh.etBusNum.setText(item.busNum);
        /*vh.ivTakePic.setOnClickListener(new View.OnClickListener() {
        });*/
        vh.tvPreview.setOnClickListener(new View.OnClickListener() {   //사진 미리보기 클릭
            @Override
            public void onClick(View v) {
                Context context= v.getContext();
                Intent i= new Intent(context, PreviewDialogActivity.class);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class VH extends RecyclerView.ViewHolder{

        TextView tvJobText;
        EditText etBusNum;
        LinearLayout ivTakePic;
        TextView tvPreview;

        public VH(@NonNull View itemView) {
            super(itemView);

            this.tvJobText= itemView.findViewById(R.id.tv_job_text);
            this.etBusNum= itemView.findViewById(R.id.et_bus_num);
            this.ivTakePic= itemView.findViewById(R.id.iv_take_pic);
            this.tvPreview= itemView.findViewById(R.id.tv_preview);

            ivTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (mListener != null){
                            mListener.mOnItemclick(v, pos);
                        }
                    }
                }
            });
        }
    }



}
