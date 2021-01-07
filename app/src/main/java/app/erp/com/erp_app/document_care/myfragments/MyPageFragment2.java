package app.erp.com.erp_app.document_care.myfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.InstallCableItems;
import app.erp.com.erp_app.document_care.Install_Cable_Adapter;

public class MyPageFragment2 extends Fragment {
    Context mContext;
    TextView tv_busoff_name;
    TextView tv_transp_bizr_id;
    SharedPreferences pref;

    /* InstallCable 리사이클러뷰*/
    private RecyclerView recyclerView;
    private ArrayList<InstallCableItems> installCableItems;
    private Install_Cable_Adapter install_cable_adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.pager2_my_project_work_insert_fragment, container, false);

        pref= getContext().getSharedPreferences("office_group_info", Context.MODE_PRIVATE);
        String st_office_group= pref.getString("office_group","");
        TextView office_group= rootView.findViewById(R.id.office_group);
        office_group.setText(st_office_group);   // 전에 선택된 데이터가 나옴

        pref= getContext().getSharedPreferences("busoff_info2", Context.MODE_PRIVATE);
        String st_busoff_name= pref.getString("busoff_name","");
        tv_busoff_name= rootView.findViewById(R.id.busoff_name);
        tv_busoff_name.setText(st_busoff_name);
        Log.d("운수회사명: ", st_busoff_name);   // 전에 선택된 데이터가 나옴

        pref= getContext().getSharedPreferences("busoff_info", Context.MODE_PRIVATE);
        String st_transp_bizr_id= pref.getString("transp_bizr_id", "");
        tv_transp_bizr_id= rootView.findViewById(R.id.transp_bizr_id);
        tv_transp_bizr_id.setText(st_transp_bizr_id);
        Log.d("운수회사 고정번호: ", st_transp_bizr_id);  //전에 선택된 데이터가 나옴



        installCableItems= new ArrayList<>();
        installCableItems.add(new InstallCableItems("1", "품목", "상세품목", "수량", 1, 1));
        installCableItems.add(new InstallCableItems("1", "품목", "상세품목", "수량", 1, 1));
        installCableItems.add(new InstallCableItems("1", "품목", "상세품목", "수량", 1, 1));
        installCableItems.add(new InstallCableItems("1", "품목", "상세품목", "수량", 1, 1));
        installCableItems.add(new InstallCableItems("1", "품목", "상세품목", "수량", 1, 1));
        installCableItems.add(new InstallCableItems("1", "품목", "상세품목", "수량", 1, 1));

        install_cable_adapter= new Install_Cable_Adapter(getContext(), installCableItems);
        recyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerview_spinner);
        recyclerView.setAdapter(install_cable_adapter);

        
        return  rootView;
    }
}


