package app.erp.com.erp_app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hsra on 2019-06-21.
 */

public class Fragment_d_0 extends Fragment {

    Context context;

    My_Error_Adapter adapter;
    ListView listView;

    private Retrofit retrofit;
    SharedPreferences pref;
    public Fragment_d_0(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d_0, container ,false);
        context = getActivity();
        pref = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        adapter = new My_Error_Adapter();
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = (int) v.getTag();
                Trouble_HistoryListVO thlvo = adapter.resultItem(postion);

                Fragment_d_6 fragment = new Fragment_d_6();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Obj",thlvo);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frage_change,fragment);
                ft.commit();
            }
        });

        listView = (ListView)view.findViewById(R.id.my_error_list);
        new Filed_MyErrorList().execute();

        return view;
    }

    private class Filed_MyErrorList extends AsyncTask<String , Integer , Long>{
        @Override
        protected Long doInBackground(String... strings) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.test_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ERP_Spring_Controller erp = retrofit.create(ERP_Spring_Controller.class);
            String emp_id = pref.getString("emp_id","inter");
            Call<List<Trouble_HistoryListVO>> call = erp.getfield_my_error_list(emp_id);
            call.enqueue(new Callback<List<Trouble_HistoryListVO>>() {
                @Override
                public void onResponse(Call<List<Trouble_HistoryListVO>> call, Response<List<Trouble_HistoryListVO>> response) {
                    try{
                        List<Trouble_HistoryListVO> list = response.body();
                        MakeMyErrorList(list);
                    }catch (Exception e){
                        Toast.makeText(context,"데이터가 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Trouble_HistoryListVO>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private void MakeMyErrorList(List<Trouble_HistoryListVO> list) {
        listView.setAdapter(adapter);
        Log.d("d","list_"+list.size());
        for(Trouble_HistoryListVO i : list){
            adapter.addItem(i);
        }
    }

}
