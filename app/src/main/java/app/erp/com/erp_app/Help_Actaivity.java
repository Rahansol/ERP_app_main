package app.erp.com.erp_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.adapter.HelpPagerAdapter;

public class Help_Actaivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<String> numberList;

    private HelpPagerAdapter helpPagerAdapter;
    private CircleAnimIndicator circleAnimIndicator;

    private String help_key;
    TextView help_title;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        help_key = getIntent().getStringExtra("help_key");
        help_title = (TextView)findViewById(R.id.help_title);

        initLayout();
        init();

//        helpPagerAdapter = new HelpPagerAdapter(this);
//        viewPager.setAdapter(helpPagerAdapter);
    }

    private void initLayout(){

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circleAnimIndicator = (CircleAnimIndicator) findViewById(R.id.circleAnimIndicator);
    }


    /**
     * 데이터 초기화
     */
    private void init(){
        //데이터 초기화
        initData();
        //ViewPager 초기화
        initViewPager();
    }


    /**
     * 데이터 초기화
     */
    private void initData(){
        int return_int = 0;
        switch (help_key){
            case "insert" :
                return_int = 7;
                help_title.setText("장애 등록");
                break;
            case "list" :
                help_title.setText("장애 처리");
                return_int = 3;
                break;
            case "care":
                help_title.setText("장애 처리 완료");
                return_int = 1;
                break;
        }
        numberList = new ArrayList<>();
        for(int i = 0 ; i < return_int; i++){
            numberList.add(""+i);
        }
    }

    /**
     * ViewPager 초기화
     */
    private void initViewPager(){

        HelpPagerAdapter helpPagerAdapter = new HelpPagerAdapter(getApplicationContext(),help_key);
        viewPager.setAdapter(helpPagerAdapter);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        viewPager.setOffscreenPageLimit(3);

        //Indicator 초기화
        initIndicaotor();
    }


    /**
     * Indicator 초기화
     */
    private void initIndicaotor(){

        //원사이의 간격
        circleAnimIndicator.setItemMargin(15);
        //애니메이션 속도
        circleAnimIndicator.setAnimDuration(300);
        //indecator 생성
        circleAnimIndicator.createDotPanel(numberList.size(), R.drawable.non_circle , R.drawable.on_circle);
    }


    /**
     * ViewPager 전환시 호출되는 메서드
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            circleAnimIndicator.selectDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

}
