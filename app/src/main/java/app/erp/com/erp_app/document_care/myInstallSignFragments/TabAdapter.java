package app.erp.com.erp_app.document_care.myInstallSignFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    Fragment[] fragments= new Fragment[3];
    String[] tabTitles= new String[]{"작성하기","작성현황","서명완료"};

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);

        fragments[0]= new Fragment1_write();
        fragments[1]= new Fragment2_my_status();
        fragments[2]= new Fragment3_my_status_completed();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
