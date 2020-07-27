package app.erp.com.erp_app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.ERP_Spring_Controller;
import app.erp.com.erp_app.R;
import app.erp.com.erp_app.dialog.Dialog_Doc_Info_View;
import app.erp.com.erp_app.document_care.ProJect_Doc_Insert_Activity;
import app.erp.com.erp_app.document_care.ProJect_Doc_Write_Activity;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.vo.Unit_InstallVO;
import retrofit2.Call;
import retrofit2.Response;

import static app.erp.com.erp_app.document_care.ProJect_Doc_Insert_Activity.getMapFromJsonObject;

public class Prj_Doc_Sign_List_RecyclerAdapter extends RecyclerView.Adapter<Prj_Doc_Sign_List_RecyclerAdapter.ViewHolder>{

    private ArrayList<ProJectVO> mData = new ArrayList<>();
    private Context mContext;

    private ArrayList<ProJectVO> req_list = new ArrayList<>();
    private String prj_name;
    private Map<String,Object> req_map = new HashMap<>();
    private Intent intent;
    private ERP_Spring_Controller erp =ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
    private Dialog_Doc_Info_View dialog;
    private HashMap<String,Object> req_map2 = new HashMap<>();

    public Prj_Doc_Sign_List_RecyclerAdapter(String pn , Map<String,Object> rm) {
        this.prj_name = pn;
        this.req_map = rm;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ProJectVO data;
        private int postion;

        LinearLayout click_layout;
        TextView write_date_textview;
        TextView busoff_name_textview;
        TextView garage_name_textview;
        TextView emp_name_textview;
        TextView sign_date_textview;
        TextView sign_man_textview;
        TextView car_cnt_textview;


        ViewHolder(View itemview){
            super(itemview);
            click_layout = (LinearLayout)itemview.findViewById(R.id.click_layout);

            write_date_textview = (TextView)itemview.findViewById(R.id.write_date_textview);
            busoff_name_textview = (TextView)itemview.findViewById(R.id.busoff_name_textview);
            garage_name_textview = (TextView)itemview.findViewById(R.id.garage_name_textview);
            emp_name_textview = (TextView)itemview.findViewById(R.id.emp_name_textview);
            sign_date_textview = (TextView)itemview.findViewById(R.id.sign_date_textview);
            sign_man_textview = (TextView)itemview.findViewById(R.id.sign_man_textview);
            car_cnt_textview = (TextView)itemview.findViewById(R.id.car_cnt_textview);

        }

        void  onBind(ProJectVO datas , int pos){
            this.data = datas;
            this.postion = pos;

            write_date_textview.setText(data.getWrite_date());
            busoff_name_textview.setText(data.getBusoff_name());
            garage_name_textview.setText(data.getGarage_name());
            emp_name_textview.setText(data.getEmp_name());
            sign_date_textview.setText(data.getSign_date());
            sign_man_textview.setText(data.getSign_man());
            car_cnt_textview.setText(data.getCar_cnt());

            click_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    req_map2.put("prj_name",prj_name );
                    req_map2.put("write_dtti",data.getWrite_dtti());
                    req_map2.put("transp_bizr_id",data.getTransp_bizr_id());
                    req_map2.put("garage_id",data.getGarage_id());
                    req_map2.put("sub_area_code",data.getSub_area_code());
                    req_map2.put("prj_seq",data.getPrj_seq());

                    req_map.put("trans_id",data.getTransp_bizr_id());
                    req_map.put("garage_name",data.getGarage_name());
                    req_map.put("busoff_name",data.getBusoff_name());
                    req_map.put("view_type","v");

                    req_map.put("sign_name",data.getSign_man());
                    req_map.put("sign_tel_num",data.getSign_tel());
                    req_map.put("sign_img_path",data.getSign_image());

                    Call<List<ProJectVO>> call = erp.prj_doc_info(req_map2);
                    new Prj_Doc_Sign_List_RecyclerAdapter.prj_doc_info().execute(call);


                }
            });
        }
    }

    public void addItem(ProJectVO list){
        mData.add(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.recyclerview_prj_doc_sign_list, viewGroup , false);
        Prj_Doc_Sign_List_RecyclerAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int postion) {
        viewHolder.onBind(mData.get(postion) , postion);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String return_date(String date){
        String rs = date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8) + "\n" + date.substring(8,10) +":" + date.substring(10,12);
        return rs;
    }

    public ArrayList<ProJectVO> return_check_work (){
        HashSet<ProJectVO> listSet = new HashSet<ProJectVO>(req_list);
        ArrayList<ProJectVO> processedList = new ArrayList<ProJectVO>(listSet);
        return processedList;
    }
    //다이얼로그에 필요한 데이터 통신
    private class prj_doc_info extends AsyncTask<Call, Void , List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
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
            if(proJectVOS != null){
                dialog = new Dialog_Doc_Info_View(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                }, proJectVOS, req_map2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<List<ProJectVO>> call = erp.prj_doc_view_data(req_map2);
                        new Prj_Doc_Sign_List_RecyclerAdapter.prj_doc_view_data().execute(call);
                    }
                });
                dialog.show();
                dialog.setCancelable(false);

                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = dm.widthPixels; //디바이스 화면 너비
                width = (int)(width * 0.9);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = dialog.getWindow();
                window.setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    // 문서 선택했을때 문서에 작성한 버스 리스트 가져옴
    private class prj_doc_view_data extends AsyncTask<Call,Void, List<ProJectVO>>{
        @Override
        protected List<ProJectVO> doInBackground(Call... calls) {
            try{
                Call<List<ProJectVO>> call = calls[0];
                Response<List<ProJectVO>> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProJectVO> chekc_list) {
            super.onPostExecute(chekc_list);
            if(chekc_list != null){
                String sql_text = "";
                for(int i=0; i<chekc_list.size(); i++){
                    if(i==0){
                        sql_text +=  "(REG_DTTI=" + "re1" + chekc_list.get(i).getReg_dtti() + "re1"  + "AND REG_EMP_ID =" + "re1" + chekc_list.get(i).getReg_emp_id() + "re1" + "AND BUS_ID=" + "re1"+ chekc_list.get(i).getBus_id() + "re1" + ")";
                    }else{
                        sql_text += "OR" + "(REG_DTTI=" + "re1" + chekc_list.get(i).getReg_dtti() + "re1"  + "AND REG_EMP_ID =" + "re1" + chekc_list.get(i).getReg_emp_id() + "re1" + "AND BUS_ID=" + "re1"+ chekc_list.get(i).getBus_id() + "re1" + ")";
                    }
                }
                req_map.put("check_work_list_sql",sql_text.replaceAll("re1","'" + "'"));
                req_map.put("check_work_list_sql2",sql_text.replaceAll("re1","'"));
                Call<Object> call = erp.create_sql_check_work_list(req_map);
                new Prj_Doc_Sign_List_RecyclerAdapter.create_sql_check_work_list().execute(call);

            }
        }
    }

    // 버스리스트로 sql 생성
    private class create_sql_check_work_list extends AsyncTask<Call , Void , Object>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("문서 양식 생성중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Object doInBackground(Call... calls) {
            try{
                Call<Object> call = calls[0];
                Response<Object> response = call.execute();
                return response.body();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object prj_item_vos) {
            super.onPostExecute(prj_item_vos);
            if (progressDialog != null)
                progressDialog.dismiss();

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

            String res = prj_item_vos.toString();
            JSONObject test = null;
            try {
                test = new JSONObject(res);
            } catch (JSONException e) {
                test = null;
                e.printStackTrace();
            }
            JSONArray jsonArray = null;
            JSONArray jsonArray2 = null;

            try {
                jsonArray =  test.getJSONArray("data1");
                jsonArray2 = test.getJSONArray("data2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if( prj_item_vos != null )
            {
                int jsonSize = jsonArray.length();
                for( int i = 0; i < jsonSize; i++ )
                {
                    Map<String, Object> map = null;
                    try {
                        map = getMapFromJsonObject( (JSONObject) jsonArray.get(i) );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.add( map );
                }
            }

            if( prj_item_vos != null )
            {
                int jsonSize = jsonArray2.length();
                for( int i = 0; i < jsonSize; i++ )
                {
                    Map<String, Object> map = null;
                    try {
                        map = getMapFromJsonObject( (JSONObject) jsonArray2.get(i) );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list2.add( map );
                }
            }

            intent = new Intent(mContext , ProJect_Doc_Write_Activity.class);
            intent.putExtra("list1" , (Serializable) list);
            intent.putExtra("list2" , (Serializable) list2);
            intent.putExtra("request_map" , (Serializable)req_map);

            Call<List<Unit_InstallVO>> call = erp.prj_all_item_list(req_map);
            new Prj_Doc_Sign_List_RecyclerAdapter.prj_all_item_list().execute(call);
        }
    }

    // 생성한 sql로 결과값가져오고 view 생성
    private class prj_all_item_list extends AsyncTask<Call , Void , List<Unit_InstallVO>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("문서 양식 생성중...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected List<Unit_InstallVO> doInBackground(Call... calls) {
            try{
                Call<List<Unit_InstallVO>> call =calls[0];
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
            if (progressDialog != null)
                progressDialog.dismiss();
            if(unit_installVOS != null){
                intent.putExtra("item_vo", (Serializable) unit_installVOS);
                mContext.startActivity(intent);
            }
        }
    }
}
