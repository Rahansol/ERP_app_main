package app.erp.com.erp_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.vo.ProJectVO;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class DialogPrj_ItemView extends Dialog  {

    private Context mContext;
    private List<ProJectVO> item_list = new ArrayList<>();
    private ProJectVO pvo;

    private TextView btn_ok , busoff_text , prj_reg_date_text, prj_route_txt, prj_bus_num_text, prj_name_text;
    private View.OnClickListener ok_btn_listener;
    private LinearLayout item_layout;

    private String bus_num , busoff_name,pref_string,trans_id,img_bus_id;

    public DialogPrj_ItemView(Context context, List<ProJectVO> iv, View.OnClickListener clickListener, ProJectVO ppvo, String bs , String bof, String ps,String tr_id , String bs_id) {
        super(context);
        pvo = ppvo;
        mContext = context;
        item_list = iv;
        ok_btn_listener = clickListener;
        bus_num = bs;
        busoff_name = bof;
        pref_string=ps;
        trans_id= tr_id;
        img_bus_id = bs_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_project_item_view);

        btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(ok_btn_listener);

        item_layout = (LinearLayout)findViewById(R.id.item_layout);

        prj_name_text = (TextView)findViewById(R.id.prj_name_text);
        busoff_text = (TextView)findViewById(R.id.busoff_text);
        prj_reg_date_text = (TextView)findViewById(R.id.prj_reg_date_text);
        prj_bus_num_text = (TextView)findViewById(R.id.prj_bus_num_text);

        prj_name_text.setText(pvo.getPrj_name());
        busoff_text.setText(busoff_name);
        prj_reg_date_text.setText(pvo.getReg_date());
        prj_bus_num_text.setText(bus_num);
        make_item_view (item_list);

    }

    void make_item_view (List<ProJectVO> list) {
        if(null != list){

            final int textwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(textwidth, LinearLayout.LayoutParams.MATCH_PARENT);
            text_params.weight = 1f;

            final int objectwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams object_params = new LinearLayout.LayoutParams(objectwidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            object_params.weight = 5f;

            final int cswidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams cs_params = new LinearLayout.LayoutParams(cswidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            cs_params.weight =5f;

            for(int i=0; i<list.size(); i++){
                LinearLayout li = new LinearLayout(mContext);
                li.setOrientation(LinearLayout.VERTICAL);

                LinearLayout title_layout = new LinearLayout(mContext);
                title_layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView title = new TextView(mContext);
                title.setText("내용");
                title.setLayoutParams(text_params);
                title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                title.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                title.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_view_boder));

                TextView title_text = new TextView(mContext);
                title_text.setText(list.get(i).getJob_text());
                title_text.setLayoutParams(object_params);
//                title_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                title_text.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                title_text.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_view_boder));

                title_layout.addView(title);
                title_layout.addView(title_text);

                li.addView(title_layout);

                LinearLayout image_layout = new LinearLayout(mContext);
                image_layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView image_title = new TextView(mContext);

                String item_type = list.get(i).getItem_type();
                switch (item_type){
                    case "P" :

                        image_title.setText("사진");
                        image_title.setLayoutParams(text_params);
                        image_title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        image_title.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                        image_title.setGravity(Gravity.CENTER_VERTICAL);
                        image_title.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_view_boder));

                        final int vbwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
                        final int vbheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, mContext.getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams vb_params = new LinearLayout.LayoutParams(vbwidth, vbheight);
                        vb_params.weight =5f;
                        image_layout.addView(image_title);

                        //String image_url = "http://ierp.interpass.co.kr/" + "" + list.get(i).getContents() + "" ;
                        String image_url = "192.168.0.122/" + "" + list.get(i).getContents() + "" ;
                        if(null == list.get(i).getContents()){
                            TextView no_image_text = new TextView(mContext);
                            no_image_text.setText("사진 없음");
                            no_image_text.setLayoutParams(vb_params);
                            no_image_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            no_image_text.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                            no_image_text.setGravity(Gravity.CENTER_VERTICAL);
                            no_image_text.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_view_boder));
                            image_layout.addView(no_image_text);
                        }else{
                            ImageView imageView = new ImageView(mContext);
                            imageView.setLayoutParams(vb_params);
                            imageView.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_view_boder));;
                            Glide.with(mContext).load(image_url)
                                    .listener(requestListener)
                                    .into(imageView);
                            image_layout.addView(imageView);
                        }
                        li.addView(image_layout);

                        break;
                    case "V" :

                        image_title.setText("동영상");
                        image_title.setLayoutParams(text_params);
                        image_title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        image_title.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                        image_title.setGravity(Gravity.CENTER_VERTICAL);
                        image_title.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_view_boder));

                        final int vvwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
                        final int vvheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, mContext.getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams vv_params = new LinearLayout.LayoutParams(vvwidth, vvheight);
                        vv_params.weight =5f;

                        //String video_url = "http://ierp.interpass.co.kr/" +  list.get(i).getContents() + "" ;
                        String video_url = "192.168.0.122/" +  list.get(i).getContents() + "" ;
//                        String url = "http://sites.google.com/site/ubiaccessmobile/sample_video.mp4";

                        ImageView videoView = new ImageView(mContext);
                        try {
                            // 썸네일 추출후 리사이즈해서 다시 비트맵 생성
                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video_url, MediaStore.Video.Thumbnails.MINI_KIND);
                            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 360, 480);
                            videoView.setImageBitmap(thumbnail);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        VideoView videoView = new VideoView(mContext);
//                        MediaController mc = new MediaController(mContext); // 컨트롤러(재생, 정지 등) 객체
//                        videoView.setMediaController(mc);   // 컨트롤러 설정
//                        videoView.setVideoURI(Uri.parse(video_url)); // 동영상 파일 위치 설정
//                        videoView.requestFocus();   // 파일 정보의 일부를 가짐
//                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            @Override
//                            public void onPrepared(MediaPlayer mp) {
                                // 파일을 재생할 준비과정이 끝났는지 확인
//                                Toast.makeText(mContext.getApplicationContext(), "동영상 준비됨", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        MediaController controller = new MediaController(mContext);
//                        controller.setAnchorView(videoView);
//                        videoView.setMediaController(controller);
//                        Uri testurl = Uri.parse(video_url);
                        Log.d("uri", video_url.toString());
//                        videoView.setVideoURI(testurl);
                        videoView.setLayoutParams(vv_params);
                        videoView.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_view_boder));;
//                        videoView.requestFocus();

                        image_layout.addView(image_title);
                        image_layout.addView(videoView);

                        li.addView(image_layout);

                        break;
                    case "C" :
                        image_title.setText("문자");
                        image_title.setLayoutParams(text_params);
                        image_title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        image_title.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                        image_title.setGravity(Gravity.CENTER_VERTICAL);
                        image_title.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_text_view_boder));

                        final int ccwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams cc_params = new LinearLayout.LayoutParams(ccwidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                        cc_params.weight =5f;

                        TextView cctext = new TextView(mContext);
                        cctext.setLayoutParams(cs_params);
                        cctext.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_text_view_boder));;
                        cctext.setText(list.get(i).getContents());
                        cctext.setTextColor(mContext.getResources().getColor(R.color.textBlack));

                        image_layout.addView(image_title);
                        image_layout.addView(cctext);

                        li.addView(image_layout);


                        break;
                    case "S" :
                        image_title.setText("선택");
                        image_title.setLayoutParams(text_params);
                        image_title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        image_title.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                        image_title.setGravity(Gravity.CENTER_VERTICAL);
                        image_title.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_text_view_boder));

                        TextView sstext = new TextView(mContext);
                        sstext.setLayoutParams(cs_params);
                        sstext.setBackground(mContext.getResources().getDrawable(R.drawable.prj_item_text_view_boder));;
                        sstext.setText(list.get(i).getContents());
                        sstext.setTextColor(mContext.getResources().getColor(R.color.textBlack));

                        image_layout.addView(image_title);
                        image_layout.addView(sstext);

                        li.addView(image_layout);


                        break;
                }

                item_layout.addView(li);
            }
        }
    }

    private RequestListener requestListener = new RequestListener() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }


        /*28버전 한솔씨 코드*/
       /* @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }*/
    };
}
