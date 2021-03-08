package app.erp.com.erp_app.document_care.myInstallSignFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.document_care.MyProject_Work_Insert_Activity;
import app.erp.com.erp_app.vo.Bus_OfficeVO;
import retrofit2.Call;
import retrofit2.Response;

public class fragment1 extends Fragment {

    RecyclerView recyclerView;
    PrjNameAdapter prjNameAdapter;
    ArrayList<PrjNameItems> prjNameItems= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment1, container, false);

/*        btnMove= view.findViewById(R.id.btn_move);
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), MyProject_Work_Insert_Activity.class);
                i.putExtra("SelectedNum", 0+1+"");
                startActivity(i);
            }
        });*/
        prjNameItems.clear();

        ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<Bus_OfficeVO>> call= erp.JobNameSpinner();
        new PrjNameList().execute(call);

        recyclerView= view.findViewById(R.id.recycler);


        return view;
    }


    public class PrjNameList extends AsyncTask<Call, Void, List<Bus_OfficeVO>>{

        @Override
        protected List<Bus_OfficeVO> doInBackground(Call... calls) {
            Call<List<Bus_OfficeVO>> call= calls[0];
            try {
                Response<List<Bus_OfficeVO>> response= call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus_OfficeVO> bus_officeVOS) {
            super.onPostExecute(bus_officeVOS);

            if (bus_officeVOS == null){
                Toast.makeText(getContext(), "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }

            for (int i=0; i<bus_officeVOS.size(); i++){
                prjNameItems.add(new PrjNameItems(bus_officeVOS.get(i).getPrj_name()));
            }

            prjNameAdapter= new PrjNameAdapter(getContext(), prjNameItems);
            recyclerView.setAdapter(prjNameAdapter);
        }
    }


}
