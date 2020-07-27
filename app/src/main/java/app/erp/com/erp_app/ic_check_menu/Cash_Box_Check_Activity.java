package app.erp.com.erp_app.ic_check_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.callcenter.Fragment_trobule_care_insert;
import app.erp.com.erp_app.callcenter.Fragment_trouble_insert;
import app.erp.com.erp_app.callcenter.Fragment_trouble_list;

public class Cash_Box_Check_Activity  extends AppCompatActivity implements View.OnClickListener{

    FragmentManager fm;
    FragmentTransaction ft;

    TextView cash_list, cash_insert;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_box_check);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        cash_list = (TextView)findViewById(R.id.cash_list);
        cash_insert = (TextView)findViewById(R.id.cash_insert);

        cash_list.setOnClickListener(this);
        cash_insert.setOnClickListener(this);

        Fragment fragment = new Fragment_cash_check_insert();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
//        ft.addToBackStack(null);
        ft.add(R.id.cash_frag_change,fragment);
        ft.commit();

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;

        switch (v.getId()){
            case R.id.cash_list :
                fragment = new Fragment_cash_check_list();
                cash_list.setBackground(getResources().getDrawable(R.drawable.text_btn));
                cash_insert.setBackground(null);

                cash_list.setTextColor(getResources().getColor(R.color.green_text_color));
                cash_insert.setTextColor(getResources().getColor(R.color.origin_color));
                break;
            case R.id.cash_insert :
                fragment = new Fragment_cash_check_insert();

                cash_list.setBackground(null);
                cash_insert.setBackground(getResources().getDrawable(R.drawable.text_btn));

                cash_list.setTextColor(getResources().getColor(R.color.origin_color));
                cash_insert.setTextColor(getResources().getColor(R.color.green_text_color));
                break;

        }
        if(fragment != null){
            FragmentManager fmanager = getSupportFragmentManager();
            FragmentTransaction ftrans = fmanager.beginTransaction();
            ftrans.replace(R.id.cash_frag_change,fragment);
            ftrans.commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
