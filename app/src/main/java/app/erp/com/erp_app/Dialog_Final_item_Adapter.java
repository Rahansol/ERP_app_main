package app.erp.com.erp_app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import app.erp.com.erp_app.ReleaseRequestActivity;

public class Dialog_Final_item_Adapter extends BaseAdapter {

    private ArrayList<String> final_list_items;
    private ReleaseRequestActivity releaseRequestActivity;




    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {      //position에 해당하는 데이터 값
        return null;
    }

    @Override
    public long getItemId(int position) {   //position의 id값을 리턴
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
