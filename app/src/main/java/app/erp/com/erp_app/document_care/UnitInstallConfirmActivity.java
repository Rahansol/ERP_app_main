package app.erp.com.erp_app.document_care;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.Unit_Install_List_Adapter;
import app.erp.com.erp_app.vo.Unit_InstallVO;
import retrofit2.Call;
import retrofit2.Response;

public class UnitInstallConfirmActivity extends AppCompatActivity {

    private int count = 0;
    private TextView count_text;
    private ERP_Spring_Controller erp;
    private Unit_Install_List_Adapter unit_install_list_adapter;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_install_confirm);
        mContext = this;
        erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);

        Call<List<Unit_InstallVO>> call = erp.app_unit_install_list("01","00","2");
        new UnitInstallConfirmActivity.app_unit_install_list().execute(call);

    }

    private class app_unit_install_list extends AsyncTask<Call, Void, List<Unit_InstallVO>>{

        @Override
        protected List<Unit_InstallVO> doInBackground(Call... calls) {
            try{
                Call<List<Unit_InstallVO>> call = calls[0];
                Response<List<Unit_InstallVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Unit_InstallVO> unit_installVOS) {
            super.onPostExecute(unit_installVOS);
            if(unit_installVOS != null){
                RecyclerView recyclerView = findViewById(R.id.unit_install_recycleview);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                unit_install_list_adapter = new Unit_Install_List_Adapter();
                recyclerView.setAdapter(unit_install_list_adapter);
                for(int i=0; i<unit_installVOS.size(); i++){
                    unit_install_list_adapter.addItem(unit_installVOS.get(i));
                }

                unit_install_list_adapter.notifyDataSetChanged();
            }
        }
    }
}
