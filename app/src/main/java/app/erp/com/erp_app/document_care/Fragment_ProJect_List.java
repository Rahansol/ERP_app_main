package app.erp.com.erp_app.document_care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.adapter.ProJectList_Adapter;
import app.erp.com.erp_app.dialog.Dialog_ProJect_Detail;
import app.erp.com.erp_app.vo.ProJectVO;
import retrofit2.Call;
import retrofit2.Response;

public class Fragment_ProJect_List extends Fragment {

    ProJectList_Adapter cjad;
    ListView prj_list_view;
    Context mcontext;
    Dialog_ProJect_Detail dialog_proJect_detail;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_project_list, container ,false);
        mcontext = getActivity();

        cjad = new ProJectList_Adapter("Y");
        prj_list_view = (ListView)view.findViewById(R.id.project_list);

        final Button project_serch_btn = (Button)view.findViewById(R.id.project_serch_btn);     //전체 프로젝트 버튼
        project_serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_name = (String) project_serch_btn.getText();
                String serch_type = "R";
                if("전체 프로젝트".equals(btn_name)){
                    project_serch_btn.setText("진행 프로젝트");
                    project_serch_btn.setTextColor(mcontext.getResources().getColor(R.color.white));
                    project_serch_btn.setBackground(mcontext.getResources().getDrawable(R.drawable.blue_square_btn));
                    serch_type = "A";
                }else{
                    project_serch_btn.setText("전체 프로젝트");
                    project_serch_btn.setTextColor(mcontext.getResources().getColor(R.color.textBlack));
                    project_serch_btn.setBackground(mcontext.getResources().getDrawable(R.drawable.custom_btn));
                    serch_type = "R";
                }
                ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                Call<List<ProJectVO>> call = erp.project_list(serch_type);
                new Fragment_ProJect_List.get_project_list().execute(call);
            }
        });

        //프로젝트 정보 조회버튼
        cjad.setDetail_btn_listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProJectVO select_item = (ProJectVO) cjad.getItem((Integer) view.getTag());
                dialog_proJect_detail = new Dialog_ProJect_Detail(mcontext, select_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_proJect_detail.dismiss();
                    }
                });
                dialog_proJect_detail.setCancelable(false);
                dialog_proJect_detail.show();

                DisplayMetrics dm = mcontext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = dm.widthPixels; //디바이스 화면 너비
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog_proJect_detail.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = dialog_proJect_detail.getWindow();
                window.setAttributes(lp);
                dialog_proJect_detail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        //작업 등록 버튼
        cjad.setWork_insert_btn_listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mcontext, ProJect_Work_Insert_Activity.class);      //등록버튼을 누르면 등록화면으로 이동..여기서 내가 만든 화면으로 바꿔주기.
                ProJectVO select_item = (ProJectVO) cjad.getItem((Integer) view.getTag());
                i.putExtra("pvo",select_item);
                startActivity(i);
            }
        });

        /* myButtonInsert 등록 test */
        /*cjad.mysetDetail_btn_listener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mcontext, MyProject_Work_Insert_Activity.class);
                startActivity(i);
            }
        });
*/


        //작업 조회 버튼
        cjad.setProject_work_serch_btn_listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProJectVO pvo = (ProJectVO) cjad.getItem((int)view.getTag());

                Fragment fragment = new Fragment_ProJect_Work_List();

                Bundle bundle = new Bundle();
                bundle.putSerializable("Obj",pvo);
                fragment.setArguments(bundle);

                FragmentManager fmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ftrans = fmanager.beginTransaction();
                ftrans.replace(R.id.project_frage_change,fragment);
                ftrans.addToBackStack(null);
                ftrans.commit();
            }
        });

        //문서작성 버튼
        cjad.setPrj_doc_insert_listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mcontext, ProJect_Doc_Insert_Activity.class);
                ProJectVO select_item = (ProJectVO) cjad.getItem((Integer) view.getTag());
                i.putExtra("pvo",select_item);
                startActivity(i);
            }
        });

        //문서 작성물 조회 버튼
        cjad.setPrj_doc_view_btn_listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mcontext, ProJect_Doc_View_Activity.class);
                ProJectVO select_item = (ProJectVO) cjad.getItem((Integer) view.getTag());
                i.putExtra("pvo",select_item);
                startActivity(i);
            }
        });

        final LinearLayout s_v_i_layout = (LinearLayout)view.findViewById(R.id.s_v_i_layout);
        final TextView doc_insert_text = (TextView)view.findViewById(R.id.doc_insert_text);
        final ToggleButton write_doc_btn = (ToggleButton) view.findViewById(R.id.write_doc_btn);
        write_doc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(write_doc_btn.isChecked()){
                    cjad.set_display("N");
                    s_v_i_layout.setVisibility(View.GONE);
                    doc_insert_text.setVisibility(View.VISIBLE);
                }else{
                    cjad.set_display("Y");
                    s_v_i_layout.setVisibility(View.VISIBLE);
                    doc_insert_text.setVisibility(View.GONE);
                }

            }
        });

        ERP_Spring_Controller erp = ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
        Call<List<ProJectVO>> call = erp.project_list("R");
        new Fragment_ProJect_List.get_project_list().execute(call);

        return view;
    }

    private class get_project_list extends AsyncTask<Call, Void , List<ProJectVO>> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("프로젝트 리스트 조회 중");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try {
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProJectVO> proJectVOS) {
            super.onPostExecute(proJectVOS);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(proJectVOS != null){
                cjad.clearItem();
                prj_list_view.setAdapter(cjad);
                for(int i=0; i<proJectVOS.size(); i++){
                    cjad.addItem(proJectVOS.get(i));
                }
                prj_list_view.setAdapter(cjad);
            }
        }
    }
}
