package app.erp.com.erp_app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.document_care.Fragment_Project_Work_Insert_1;
import app.erp.com.erp_app.document_care.Fragment_Project_Work_Insert_2;
import app.erp.com.erp_app.document_care.Fragment_Project_Work_Insert_3;
import app.erp.com.erp_app.vo.ProJectVO;

public class Prj_Work_Insert_PagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    private ArrayList<ProJectVO> minsert_1 = new ArrayList<>();
    private ArrayList<ProJectVO> minsert_2 = new ArrayList<>();
    private ArrayList<ProJectVO> minsert_3 = new ArrayList<>();
    private ProJectVO pvo;

    public Prj_Work_Insert_PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addlist(ArrayList<ProJectVO> insert_1, ArrayList<ProJectVO> insert_2 ,ArrayList<ProJectVO> insert_3, ProJectVO vo){
        minsert_1 = insert_1;
        minsert_2 = insert_2;
        minsert_3 = insert_3;
        pvo = vo;
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

    public Map<String,Object> return_all_request_map (){
        Fragment_Project_Work_Insert_1 f1 = (Fragment_Project_Work_Insert_1) mFragmentList.get(0);
        Fragment_Project_Work_Insert_2 f2 = (Fragment_Project_Work_Insert_2) mFragmentList.get(1);
        Fragment_Project_Work_Insert_3 f3 = (Fragment_Project_Work_Insert_3) mFragmentList.get(2);
        Map<String, Object> return_all_map = new HashMap<>();
        return_all_map.putAll(f1.return_reques_map());
        return_all_map.putAll(f2.return_reques_map());
        return_all_map.putAll(f3.return_reques_map());
        return return_all_map;
    }

    public Map<String,Object> return_imgage_map (){
        Fragment_Project_Work_Insert_1 f1 = (Fragment_Project_Work_Insert_1) mFragmentList.get(0);
        return f1.return_imgage_map();
    }


    public void view_control(boolean view_frg){
        Fragment_Project_Work_Insert_2 f2 = (Fragment_Project_Work_Insert_2) mFragmentList.get(1);
        f2.view_controler(view_frg);
    }

}
