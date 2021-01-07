package app.erp.com.erp_app.document_care;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.play.core.install.model.ActivityResult;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.ActivityResultEvent;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment1;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment2;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment3;


// Fragment_ProJect_List.java 문서에서 등록버튼을 눌렀을 때 이 화면으로 이동시키기
// [등록화면]
public class MyProject_Work_Insert_Activity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project_work_insert);

        List<Fragment> list= new ArrayList<>();
        list.add(new MyPageFragment1());
        list.add(new MyPageFragment2());
        list.add(new MyPageFragment3());

        pager= findViewById(R.id.pager);
        pagerAdapter= new MySlidePagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(pagerAdapter);

    }


    /*리사이클러뷰 어댑터쪽에서 보낸 intent.. 카메라 앱 실행시킨 Result...*/
    //액티비티에 onActivityResult를 override 한 다음, 해당 fragment로 결과값을 넘겨준다.
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 20:
                //카메라 앱에서 캡처한 결과를 가져왔는지?
                if ( resultCode == RESULT_OK ){
                    Uri uri= data.getData();

                    if (uri != null){
                        Toast.makeText(this, "URI로 저장", Toast.LENGTH_SHORT).show();
                        Log.d("URI로 사진 저장", "URI로 사진 저장");
                        Glide.with(this).load(uri).into(iv);
                    }else {
                        Toast.makeText(this, "저장없이 Bitmap으로 ", Toast.LENGTH_SHORT).show();
                        // Bitmap 객체를 Intent 의 Extra 데이터로 전달
                        Bundle bundle= data.getExtras();
                        Bitmap bm= (Bitmap) bundle.get("data");
                        Glide.with(this).load(bm).into(iv);
                    }
                }
        }
    }//onActivityResult()........*/
}