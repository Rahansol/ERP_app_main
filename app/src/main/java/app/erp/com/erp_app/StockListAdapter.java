package app.erp.com.erp_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.erp.com.erp_app.vo.TestAllVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.erp.com.erp_app.ReleaseRequestActivity.emp_id;
import static app.erp.com.erp_app.ReleaseRequestActivity.getIn_yn;
import static app.erp.com.erp_app.ReleaseRequestActivity.getUn_yn;
import static app.erp.com.erp_app.ReleaseRequestActivity.getUnitId;
import static app.erp.com.erp_app.ReleaseRequestActivity.barcode_dep_id;
import static app.erp.com.erp_app.ReleaseRequestActivity.unitCode_1;
import static app.erp.com.erp_app.ReleaseRequestActivity.repUnitCode_2;

public class StockListAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<StockListItems> items;
    ArrayList<StockListItems> items2;
    StockListAdapter2 stockListAdapter2;



    //커스텀 리스너 객체
    public interface MyOnItemClickListener{
        void myOnItemClick(View v, int post);
    }

    private MyOnItemClickListener myListener= null;

    public void setMyListener(MyOnItemClickListener myListener){
        this.myListener= myListener;
    }






    public StockListAdapter(Context context, ArrayList<StockListItems> items, ArrayList<StockListItems> items2, StockListAdapter2 stockListAdapter2) {
        this.context = context;
        this.items = items;
        this.items2 = items2;
        this.stockListAdapter2 = stockListAdapter2;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.recycler_stock_list, parent, false);
        VH holder= new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VH vh= (VH)viewHolder;
        StockListItems item= items.get(position);

        vh.tvRnum.setText(item.rnum);
        vh.tvUnitVer.setText(item.unit_ver);
        vh.tvUnitId.setText(item.unit_id);
        vh.checkBox.setChecked(item.checkbox);

        //vh.itemView.setTag(item.rnum+"&"+item.unit_ver+"&"+item.unit_id);
        vh.itemView.setTag(item.un_yn+"&"+item.in_yn+"&"+item.emp_id+"&"+item.unit_code+"&"+item.rep_unit_code+"&"+item.barcode_dep_id);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class VH extends RecyclerView.ViewHolder{
        TextView tvRnum;
        TextView tvUnitVer;
        TextView tvUnitId;
        CheckBox checkBox;


        public VH(@NonNull final View itemView) {
            super(itemView);

            this.tvRnum= itemView.findViewById(R.id.tv_rnum);
            this.tvUnitVer= itemView.findViewById(R.id.tv_unit_ver);
            this.tvUnitId= itemView.findViewById(R.id.tv_unit_id);
            this.checkBox= itemView.findViewById(R.id.stock_checkbox);


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(getLayoutPosition()).checkbox= isChecked;
                    String Rnum= items.get(getLayoutPosition()).rnum;
                    String UnitVer= items.get(getLayoutPosition()).unit_ver;
                    String UnitId= items.get(getLayoutPosition()).unit_id;
                    Boolean CheckBox= items.get(getLayoutPosition()).checkbox? true : false;

                    String Un_yn= items.get(getLayoutPosition()).un_yn;
                    String In_yn= items.get(getLayoutPosition()).in_yn;
                    String Emp_id= items.get(getLayoutPosition()).emp_id;
                    String Unit_code= items.get(getLayoutPosition()).unit_code;
                    String Rep_unit_code= items.get(getLayoutPosition()).rep_unit_code;
                    String Barcode_dep_id= items.get(getLayoutPosition()).barcode_dep_id;

                    String Req_date= items.get(getLayoutPosition()).req_date;
                    String Notice= items.get(getLayoutPosition()).notice;
                    String Request_dep_id= items.get(getLayoutPosition()).request_dep_id;
                    String Response_dep_id= items.get(getLayoutPosition()).response_dep_id;
                    StockListItems item= items.get(getLayoutPosition());    //현재 누른 아이템 항목 얻어오기

                    if(isChecked){
                        //Toast.makeText(context, UnitId+" 선택 ", Toast.LENGTH_SHORT).show();
                        items2.add(items.get(getLayoutPosition()));
                        stockListAdapter2.notifyItemInserted(items2.size()-1);
                        Log.d("선택: 바코드번호-> ", Barcode_dep_id+" test");
                        Log.d("선택: 유닛코드-> ", Unit_code+" test");
                        Log.d("선택: 버전별 유닛코드-> ", Rep_unit_code+" test");
                        Log.d("선택: 아이디-> ", Emp_id+" test");
                        Un_yn="";
                    }else {
                        //Toast.makeText(context, UnitId+" 선택취소 ", Toast.LENGTH_SHORT).show();
                        int i;
                        for ( i=0; i<items2.size(); i++){
                            StockListItems t= items2.get(i);
                            if (t.rnum==item.rnum)
                                break;
                        }
                        items2.remove(items.get(getLayoutPosition()));
                        //boolean un_yn= items2.remove(items.get(getLayoutPosition()));   //tag로 넘기기?
                        stockListAdapter2.notifyItemRemoved(i);
                        //stockListAdapter2.notifyItemRemoved(items2.size()-0);

                        //선택해제시 YY를 넣어라.
                        Un_yn="YY";
                    }

                    //하나로 관리  (예약, 업데이트, 인서트, 삭제...등)
                    ERP_Spring_Controller erp= ERP_Spring_Controller.retrofit.create(ERP_Spring_Controller.class);
                    Call<Void> call= erp.AppUnitBookingChk_map(Emp_id, Barcode_dep_id, Un_yn, Unit_code, Rep_unit_code, UnitId, In_yn, Req_date, Notice, Request_dep_id, Response_dep_id);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {      //보통 리턴타입이 다르면 FAIL로 빠지니까 꼭 확인하기./...
                            Log.d("emp_id 확인================>>>>>>>",t+"");
                        }
                    });
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checkBox.setChecked(true);
                    if (checkBox.isChecked()){
                        checkBox.setChecked(false);
                    }else {
                        checkBox.setChecked(true);
                    }
                }

            });



        }
    }
}
