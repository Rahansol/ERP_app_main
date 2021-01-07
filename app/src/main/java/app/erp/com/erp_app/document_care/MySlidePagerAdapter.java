package app.erp.com.erp_app.document_care;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;


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
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
