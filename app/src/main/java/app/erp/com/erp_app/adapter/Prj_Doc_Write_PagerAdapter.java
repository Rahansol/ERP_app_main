package app.erp.com.erp_app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.document_care.Fragment_Project_Doc_Write_3;
import app.erp.com.erp_app.vo.Prj_Item_VO;

public class Prj_Doc_Write_PagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    private ArrayList<Prj_Item_VO> minsert_1 = new ArrayList<>();
    private ArrayList<Prj_Item_VO> minsert_2 = new ArrayList<>();

    public Prj_Doc_Write_PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addlist(ArrayList<Prj_Item_VO> insert_1, ArrayList<Prj_Item_VO> insert_2){
        minsert_1 = insert_1;
        minsert_2 = insert_2;
    }

    public void addfrag(Fragment fragment){
        mFragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int postion) {
        Fragment f;
        switch (postion){
            case 0 :
                f = mFragmentList.get(0);
                break;
            case 1 :
                f = mFragmentList.get(1);
                break;
            case 2 :
                f = mFragmentList.get(2);
                break;
            default:
                f = null;
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public Map<String,Object> sign_map_return (){
        Fragment_Project_Doc_Write_3 f= (Fragment_Project_Doc_Write_3) mFragmentList.get(2);
        return f.req_return();
    }
}
