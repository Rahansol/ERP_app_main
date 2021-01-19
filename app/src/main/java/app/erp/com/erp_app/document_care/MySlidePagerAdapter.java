package app.erp.com.erp_app.document_care;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import app.erp.com.erp_app.document_care.myfragments.MyPageFragment1;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment2;
import app.erp.com.erp_app.document_care.myfragments.MyPageFragment3;


//MyProject_Work_insert.java에서 MySlidePagerAdapter로 Pager1,2,3,4 연결..
public class MySlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;


    public MySlidePagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList= fragmentList;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyPageFragment1.newInstance("jobName", "officeGroup");
            case 1:
                return new MyPageFragment2();
            case 2:
                return new MyPageFragment3();
        }
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }




}
