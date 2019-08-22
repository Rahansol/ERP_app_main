package app.erp.com.erp_app;

import java.util.List;
import java.util.Map;

import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.My_Work_ListVo;
import app.erp.com.erp_app.vo.Reserve_Work_Vo;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import app.erp.com.erp_app.vo.UnitList;
import app.erp.com.erp_app.vo.User_InfoVo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by hsra on 2019-06-21.
 */

public interface ERP_Spring_Controller {

    @GET("barcode/app_bus_num")
    Call<List<Bus_infoVo>> getBusList(@Query("bus_barcode") String bar_cdoe);
    @GET("barcode/app_select_all_unit")
    Call<List<UnitList>> getUnisList();
    @GET("barcode/app_select_all_unit_more")
    Call<List<UnitList>> getUnitListMore();
    @GET("barcode/app_barcode_install")
    Call<String> app_barcode_install(@Query("barcode_list[]") List<String> barcode_list , @Query("bus_barcode") String unit_barcode, @Query("emp_id") String emp_id , @Query("eb_barcode_list[]") List<String> eb_barcode_list);
    @GET("barcode/app_Login")
    Call<List<User_InfoVo>> app_login(@Query("emp_id") String emp_id , @Query("emp_pw") String emp_pw);
    @GET("barcode/app_office_name")
    Call<List<String>> get_office_name(@Query("office_barcode") String office_barcode);
    @GET("barcode/app_office_install")
    Call<String> app_office_install(@Query("unit_barcode[]") List<String> unit_barcode, @Query("location_code") String location_code,@Query("emp_id") String emp_id, @Query("eb_barcode_list[]") List<String> eb_barcode_list);
    @GET("barcode/office_bus_list")
    Call<List<Bus_infoVo>> office_bus_list(@Query("office_barcode") String office_barcode);
    @GET("barcode/app_reserve_high_code")
    Call<List<Trouble_CodeVo>> app_reserve_high_code (@Query("unit_barcode")String unit_barcode);
    @GET("barcode/app_reserve_low_code")
    Call<List<Trouble_CodeVo>> app_reserve_low_code (@Query("unit_barcode") String unit_barcode, @Query("trouble_high_cd") String trouble_high_cd);
    @GET("barcode/app_reserve_return_process")
    Call<String> app_reserve_return_process(@Query("location_code") String location_code , @Query("unit_barcode") String unit_barcode, @Query("trouble_high_cd") String trouble_high_cd , @Query("trouble_low_cd") String trouble_low_cd, @Query("notice") String notice ,
    @Query("emp_id")  String emp_id , @Query("bus_id") String bus_id );

    @GET("barcode/my_barcode_workList")
    Call<List<My_Work_ListVo>> my_barcode_workList (@Query("start_day") String start_day , @Query("end_day") String end_day , @Query("emp_id") String emp_id) ;

    @GET("barcode/my_barcode_workList_bus")
    Call<List<My_Work_ListVo>> my_barcode_workList_bus (@Query("start_day") String start_day , @Query("end_day") String end_day , @Query("emp_id") String emp_id, @Query("trans_id") String trans_id) ;

    @GET("barcode/my_barcode_workList_bus_unitbarcode")
    Call<List<My_Work_ListVo>> my_barcode_workList_bus_unitbarcode (@Query("start_day") String start_day , @Query("end_day") String end_day , @Query("bus_bar_code") String bus_bar_code, @Query("emp_id") String emp_id) ;

    @GET("barcode/reserve_itme_office_List")
    Call<List<Reserve_Work_Vo>> reserve_itme_office_List (@Query("start_day") String start_day , @Query("end_day") String end_day) ;

    @GET("barcode/reserve_item_unit_List")
    Call<List<Reserve_Work_Vo>> reserve_item_unit_List (@Query("start_day") String start_day , @Query("end_day") String end_day, @Query("location_code") String location_code) ;

    @GET("barcode/reserve_item_unit_List_detail")
    Call<List<Reserve_Work_Vo>> reserve_item_unit_List_detail (@Query("start_day") String start_day , @Query("end_day") String end_day, @Query("location_code") String location_code, @Query("unit_code") String unit_code) ;

    @GET("barcode/field_error_busnum")
    Call<List<Bus_infoVo>> getfield_error_busnum (@Query("bus_num") String bus_num);

    @GET("barcode/field_trouble_error_type")
    Call<List<Trouble_CodeVo>> getfield_trouble_error_type (@Query("service_id") String service_id, @Query("infra_code") String infra_code);

    @GET("barcode/field_trouble_high_code")
    Call<List<Trouble_CodeVo>> getfield_trouble_high_code (@Query("service_id") String service_id, @Query("infra_code") String infra_code, @Query("unit_code") String unit_code);

    @GET("barcode/field_trouble_low_code")
    Call<List<Trouble_CodeVo>> getfield_trouble_low_code (@Query("service_id") String service_id, @Query("infra_code") String infra_code, @Query("unit_code") String unit_code , @Query("tb_highcd")String tb_highcd);

    @GET("barcode/field_trouble_carecode")
    Call<List<Trouble_CodeVo>> getfield_trouble_carecode (@Query("service_id") String service_id, @Query("infra_code") String infra_code, @Query("unit_code") String unit_code , @Query("tb_highcd")String tb_highcd, @Query("tb_lowcd") String tb_lowcd);

//    @GET("barcode/field_my_error_list")
//    Call<List<Trouble_HistoryListVO>> getfield_my_error_list (@Query("emp_id") String emp_id);

    @GET("barcode/app_fieldError_not_care")
    Call<List<Trouble_HistoryListVO>> getfield_my_error_list (@Query("emp_id") String emp_id , @Query("service_id") String service_id);

    @GET("barcode/insert_filed_error_test")
    Call<Boolean> insert_filed_error_test (@QueryMap Map<String,Object> test);

    @GET("barcode/app_history_office_group")
    Call<List<Bus_infoVo>> get_app_history_office_group();

    @GET("barcode/app_error_Bus_Office")
    Call<List<Bus_infoVo>> get_app_error_Bus_Office (@Query("johap_name") String johap_name);

    @GET("barcode/getMyWork_Job")
    Call<List<Trouble_HistoryListVO>> getMyWork_Job (@Query("reg_date") String reg_date , @Query("job_viewer") String job_viewer, @Query("reg_time") String reg_time);

    @GET("barcode/app_trouble_history_update")
    Call<Boolean> update_trouble_history (@QueryMap Map<String,Object> update_map);

    @GET("barcode/app_fieldError_MyErrorList")
    Call<List<Trouble_HistoryListVO>> getMy_fieldError_care_list (@Query("emp_id") String emp_id);

    @GET("barcode/field_my_error_list_success")
    Call<Boolean> insert_my_success_error_list (@Query("emp_id") String emp_Id);

}
