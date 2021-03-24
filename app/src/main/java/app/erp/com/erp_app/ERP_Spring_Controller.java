package app.erp.com.erp_app;

import android.widget.EditText;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.erp.com.erp_app.vo.Bus_OfficeVO;
import app.erp.com.erp_app.vo.Bus_infoVo;
import app.erp.com.erp_app.vo.Cash_Work_VO;
import app.erp.com.erp_app.vo.Edu_Emp_Vo;
import app.erp.com.erp_app.vo.Gtv_Report_Vo;
import app.erp.com.erp_app.vo.MainReportVo;
import app.erp.com.erp_app.vo.My_Work_ListVo;
import app.erp.com.erp_app.vo.Over_Work_List_VO;
import app.erp.com.erp_app.vo.Over_Work_VO;
import app.erp.com.erp_app.vo.Prj_Item_VO;
import app.erp.com.erp_app.vo.ProJectVO;
import app.erp.com.erp_app.vo.ReleaseRequestVO;
import app.erp.com.erp_app.vo.Reserve_Work_Vo;
import app.erp.com.erp_app.vo.TestAllVO;
import app.erp.com.erp_app.vo.Trouble_CodeVo;
import app.erp.com.erp_app.vo.Trouble_HistoryListVO;
import app.erp.com.erp_app.vo.UnitList;
import app.erp.com.erp_app.vo.Unit_InstallVO;
import app.erp.com.erp_app.vo.User_InfoVo;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    /*차량검색*/
    @GET("barcode/getBusNum")
    Call<List<Bus_infoVo>> getBusNumList (@Query("bus_num") String bus_num, @Query("office_group") String office_group);

    //스프링프로젝트에서 barcode 컨트롤러에 field_trouble_error_type
    @GET("barcode/field_trouble_error_type")
    Call<List<Trouble_CodeVo>> getfield_trouble_error_type (@Query("service_id") String service_id, @Query("infra_code") String infra_code);

    @GET("barcode/field_trouble_high_code")
    Call<List<Trouble_CodeVo>> getfield_trouble_high_code (@Query("service_id") String service_id, @Query("infra_code") String infra_code, @Query("unit_code") String unit_code);

    @GET("barcode/field_trouble_low_code")
    Call<List<Trouble_CodeVo>> getfield_trouble_low_code (@Query("service_id") String service_id, @Query("infra_code") String infra_code, @Query("unit_code") String unit_code , @Query("tb_highcd")String tb_highcd);

    @GET("barcode/field_trouble_carecode")
    Call<List<Trouble_CodeVo>> getfield_trouble_carecode (@Query("service_id") String service_id, @Query("infra_code") String infra_code, @Query("unit_code") String unit_code , @Query("tb_highcd")String tb_highcd, @Query("tb_lowcd") String tb_lowcd);

    @GET("barcode/app_fieldError_not_care")
    Call<List<Trouble_HistoryListVO>> getfield_my_error_list (@Query("emp_id") String emp_id , @Query("service_id") String service_id);

    @GET("barcode/app_fieldError_not_care")
    Call<List<Trouble_HistoryListVO>> app_fieldError_6Month_Cnt (@Query("transp_bizr_id") String transp_bizr_id, @Query("bus_id") String bus_id);

    @GET("barcode/app_fieldError_not_care_history")
    Call<List<Trouble_HistoryListVO>> app_fieldError_not_care_history (@Query("transp_bizr_id") String transp_bizr_id, @Query("bus_id") String bus_id);

    @GET("barcode/insert_filed_error_test")
    Call<Boolean> insert_filed_error_test (@QueryMap Map<String,Object> test);

    @GET("barcode/app_fieldError_care_insert")
    Call<Boolean> app_fieldError_care_insert (@QueryMap Map<String,Object> test);

    @GET("barcode/app_history_office_group")
    Call<List<Bus_infoVo>> get_app_history_office_group();

    @GET("barcode/app_error_Bus_Office")
    Call<List<Bus_infoVo>> get_app_error_Bus_Office (@Query("johap_name") String johap_name);

    @GET("barcode/getMyWork_Job")
    Call<List<Trouble_HistoryListVO>> getMyWork_Job (@Query("reg_date") String reg_date , @Query("job_viewer") String job_viewer, @Query("reg_time") String reg_time);

    @GET("barcode/app_trouble_history_update")
    Call<Boolean> update_trouble_history (@QueryMap Map<String,Object> update_map, @Query("cooperate_list[]") List<String> cooperate_list);

    @POST("barcode/app_trouble_equal_infra_insert")
    Call<Boolean> app_trouble_equal_infra_insert (@Body Trouble_HistoryListVO test , @Query("cooperate_list[]") List<String> cooperate_list);

    @GET("barcode/app_fieldError_MyErrorList")
    Call<List<Trouble_HistoryListVO>> getMy_fieldError_care_list (@Query("emp_id") String emp_id);

    @GET("barcode/field_my_error_list_success")
    Call<Boolean> insert_my_success_error_list (@Query("emp_id") String emp_Id);

    @GET("barcode/app_office_group_names")
    Call<List<Bus_infoVo>> office_group_names (@Query("busoff_name") String busoff_name);

    @GET("barcode/app_insert_bus_info")
    Call<Void> app_insert_bus_info (@Query("bus_num") String bus_num , @Query("trans_id") String trans_id , @Query("emp_id") String emp_id);

    @GET("barcode/app_gtv_error_list")
    Call<List<Gtv_Report_Vo>> app_gtv_error_list(@Query("gtv_day") String gtv_day);

    @GET("barcode/app_gtv_oiffce_emp_list")
    Call<List<Gtv_Report_Vo>> app_gtv_oiffce_emp_list(@Query("gtv_day") String gtv_day, @Query("emp_name") String emp_name);

    @GET("barcode/app_gtv_detail_list")
    Call<List<Gtv_Report_Vo>> app_gtv_detail_list(@Query("gtv_day") String gtv_day ,@Query("busoff_name") String busoff_name , @Query("emp_name") String emp_name);

    @GET("barcode/app_gtv_detail_error_list")
    Call<List<Gtv_Report_Vo>> app_gtv_detail_error_list(@Query("gtv_day") String gtv_day ,@Query("busoff_name") String busoff_name , @Query("emp_name") String emp_name);

    @GET("barcode/app_gtv_bus_error_detail")
    Call<List<Gtv_Report_Vo>> app_gtv_bus_error_detail(@Query("gtv_day") String gtv_day ,@Query("busoff_name") String busoff_name , @Query("emp_name") String emp_name);

    @GET("barcode/app_insert_gtv_modify_door")
    Call<Boolean> app_insert_gtv_modify_door(@Query("trans_id") String trans_id ,@Query("busoff_name") String busoff_name , @Query("bus_id") String bus_id
            ,@Query("bus_num") String bus_num,@Query("now_door") String now_door,@Query("change_door") String change_door);

    @GET("barcode/trouble_count")
    Call<List<Trouble_HistoryListVO>> get_trouble_count (@Query("emp_id") String emp_id);

    @GET("barcode/edu_care_emp_list")
    Call<List<Edu_Emp_Vo>> Edu_care_emp_list (@Query("emp_id") String emp_id);

    @GET("barcode/insert_edu_history")
    Call<Boolean> insert_edu_history(@Query("care_emp_list[]") List<String> care_emp_list , @QueryMap Map<String,Object> map);

    @GET("barcode/insert_filed_error_jip_bus")
    Call<Boolean> insert_filed_error_jip_bus(@QueryMap Map<String,Object> map, @Query("cooperate_list[]") List<String> cooperate_list);

    @GET("barcode/app_jip_bus_care_insert")
    Call<Boolean> app_jip_bus_care_insert (@QueryMap Map<String,Object> map, @Query("cooperate_list[]") List<String> cooperate_list);

    @GET("barcode/reserve_item_process")
    Call<Boolean> reserve_item_process (@Query("barcode_list[]") List<String> barcode_list, @Query("emp_id") String emp_id, @Query("in_status") String in_status);

    @POST("barcode/trouble_move_change")
    Call<Boolean> trouble_move_change (@QueryMap Map<String,Object> update_map,@Body Trouble_HistoryListVO body, @Query("msg_check3") String msg_check3);

    @POST("barcode/trouble_move_together_insert")
    Call<Boolean> trouble_move_together_insert (@QueryMap Map<String,Object> update_map,@Body Trouble_HistoryListVO test, @Query("login_emp_id") String emp_id);

    @GET("barcode/app_work_report_data")
    Call<List<MainReportVo>> get_work_report_data (@Query("emp_id") String emp_id , @Query("plan_date") String plan_date);

    @GET("barcode/merge_plan_notice")
    Call<Boolean> merge_plan_notice (@Query("emp_id") String emp_id , @Query("plan_date") String plan_date, @Query("plan_notice") String plan_notice);

    @GET("barcode/app_fieldError_insert_call")
    Call<Boolean> app_fieldError_insert_call (@QueryMap Map<String,Object> map);

    @GET("barcode/app_changeTokenId")
    Call<Boolean> app_changeTokenId (@Query("emp_id") String emp_id , @Query("tokenid") String token_id);

    @GET("barcode/first_address_list")
    Call<List<Cash_Work_VO>> first_address_list ();

    @GET("barcode/find_last_address")
    Call<List<Cash_Work_VO>> find_last_address (@Query("first_address") String first_address);

    @GET("barcode/find_bus_num")
    Call<List<Cash_Work_VO>> find_bus_num (@Query("bus_num") String bus_num);

    @GET("barcode/find_overlap_work")
    Call<List<Cash_Work_VO>> find_overlap_work (@Query("transp_bizr_id") String transp_bizr_id, @Query("bus_num") String bus_num);

    @GET("barcode/find_all_office")
    Call<List<Cash_Work_VO>> find_all_office ();

    @GET("barcode/insert_cash_check_data")
    Call<Boolean> insert_cash_check_data (@QueryMap Map<String,Object> map);

    @GET("barcode/cash_table_head_text")
    Call<List<Cash_Work_VO>> cash_table_head_text (@QueryMap Map<String,Object> map);

    @GET("barcode/cash_table_data")
    Call<List<Cash_Work_VO>> cash_table_data (@QueryMap Map<String,Object> map);

    @GET("erp_project/app_project_list")
    Call<List<ProJectVO>> project_list (@Query("list_serch_type") String list_serch_type);

    @GET("erp_project/office_group_select")
    Call<List<ProJectVO>> app_office_group_select (@Query("area_code") String area_code , @Query("sub_area_code") String sub_area_code , @Query("office_group") String office_group);

    @GET("erp_project/transp_bizr_select")
    Call<List<ProJectVO>> app_transp_bizr_select (@Query("office_group") String office_group ,@Query("area_code") String area_code ,@Query("sub_area_code") String sub_area_code);

    @GET("erp_project/bus_select")
    Call<List<ProJectVO>> app_bus_select (@Query("transp_bizr_id") String transp_bizr_id , @Query("bus_num") String bus_num);

    @GET("erp_project/app_project_item_list")
    Call<List<ProJectVO>> app_project_item_list (@Query("area_code") String area_code , @Query("sub_area_code") String sub_area_code, @Query("prj_seq") String prj_seq,@Query("unit_version") String unit_version);

    @GET("erp_project/insert_project_work_data")
    Call<Boolean> insert_project_work_data (@QueryMap Map<String,Object> map);

    @GET("erp_project/app_project_detail_info")
    Call<List<ProJectVO>> app_project_detail_info (
            @Query("base_infra_code") String base_infra_code
            ,@Query("area_code") String area_code
            ,@Query("sub_area_code") String sub_area_code
            ,@Query("prj_seq") String prj_seq
            ,@Query("trans_id") String trans_id
            ,@Query("reg_emp_id") String reg_emp_id
            ,@Query("bus_num") String bus_num
            ,@Query("st_date") String st_date);

    @GET("erp_project/app_prj_item_data")
    Call<List<ProJectVO>> app_prj_item_data (
            @Query("area_code") String area_code
            ,@Query("sub_area_code") String sub_area_code
            ,@Query("prj_seq") String prj_seq

            ,@Query("transp_bizr_id") String transp_bizr_id
            ,@Query("bus_id") String bus_id
            ,@Query("reg_date") String reg_date);

    @GET("erp_project/app_prj_detail_serch_val")
    Call<Object> app_prj_detail_serch_val(@Query("project_table_name") String project_table_name);

    @GET("erp_project/app_trouble_history_serch")
    Call<List<Trouble_HistoryListVO>> app_trouble_history_serch(@QueryMap Map<String,Object> map);

    @GET("erp_project/trouble_unit_code_list")
    Call<List<Trouble_CodeVo>> trouble_unit_code_list();

    @GET("erp_project/serch_over_work_list")
    Call<List<Over_Work_VO>> app_serch_over_work_list(@QueryMap Map<String,Object> map);

    @GET("erp_project/insert_over_work")
    Call<Boolean> insert_over_work (@QueryMap Map<String,Object> info_map1 );

    @GET("erp_project/insert_over_work_detail")
    Call<Boolean> insert_over_work_detail (@QueryMap Map<String,Object> data_map1);

    @GET("erp_project/over_work_data_type")
    Call<List<Over_Work_List_VO>> over_work_data_type (@QueryMap Map<String,Object> info_map1 );

    @GET("erp_project/select_over_work_date_info")
    Call<List<Over_Work_List_VO>> select_over_work_date_info (@QueryMap Map<String,Object> info_map1 );

    @GET("erp_project/select_over_work_trouble_list")
    Call<List<Over_Work_List_VO>> select_over_work_trouble_list (@QueryMap Map<String,Object> info_map1 );

    @GET("erp_project/update_over_work")
    Call<Boolean> update_over_work (@QueryMap Map<String,Object> data_map1);

    @GET("erp_project/status2_over_work_insert")
    Call<List<Over_Work_VO>> status2_over_work_insert(@QueryMap Map<String,Object> map);

    @GET("erp_project/update_delete_re_over_work")
    Call<Boolean> update_delete_re_over_work (@QueryMap Map<String,Object> data_map1);

    @GET("erp_project/over_work_confirm")
    Call<Boolean> over_work_confirm (@QueryMap Map<String,Object> data_map1);

    @GET("erp_project/over_work_reject")
    Call<Boolean> over_work_reject (@QueryMap Map<String,Object> data_map1);

    @GET("erp_project/delete_over_work")
    Call<Boolean> delete_over_work (@QueryMap Map<String,Object> data_map1);

    @GET("erp_project/app_prj_garage_select")
    Call<List<ProJectVO>> app_prj_garage_select (@Query("transp_bizr_id") String transp_bizr_id);

    @GET("erp_project/app_unit_install_list")
    Call<List<Unit_InstallVO>> app_unit_install_list (@Query("area_code") String area_code , @Query("sub_area_code") String sub_area_code, @Query("doc_seq") String doc_seq);

    @GET("erp_project/transp_route_num_list")
    Call<List<ProJectVO>> transp_route_num_list(@Query("transp_bizr_id") String transp_bizr_id);

    @GET("erp_project/prj_doc_check_work_list")
    Call<List<ProJectVO>> prj_doc_check_work_list(@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/create_sql_check_work_list")
    Call<Object> create_sql_check_work_list(@QueryMap Map<String,Object> reques_map);

//    Call<List<Prj_Item_VO>> create_sql_check_work_list(@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/prj_all_item_list")
    Call<List<Unit_InstallVO>> prj_all_item_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/prj_header_list")
    Call<List<Unit_InstallVO>> prj_header_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/prj_doc_write")
    Call<Boolean> prj_doc_write (@QueryMap Map<String,Object> reques_map, @Query("job_type_list[]") List<String> job_type_list);

    @GET("erp_project/doc_seq_list")
    Call<List<ProJectVO>> doc_seq_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/prj_work_trans_list")
    Call<List<ProJectVO>> prj_work_trans_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/prj_work_garage_list")
    Call<List<ProJectVO>> prj_work_garage_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/prj_work_job_list")
    Call<List<ProJectVO>> prj_work_job_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/app_busoff_name_find")
    Call<List<ProJectVO>> app_busoff_name_find (@Query("busoff_name") String busoff_name);

    @GET("erp_project/prj_doc_office_list")
    Call<List<ProJectVO>> prj_doc_office_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/prj_doc_garage_list")
    Call<List<ProJectVO>> prj_doc_garage_list (@QueryMap Map<String,Object> reques_map);

    @GET("erp_project/serch_prj_doc_sign_list")
    Call<List<ProJectVO>> serch_prj_doc_sign_list (@QueryMap Map<String,Object> req_map);

    @GET("erp_project/prj_doc_info")
    Call<List<ProJectVO>> prj_doc_info (@QueryMap Map<String,Object> req_map);

    @GET("erp_project/prj_doc_btn_name_list")
    Call<List<ProJectVO>> prj_doc_btn_name_list (@QueryMap Map<String,Object> req_map);

    @GET("erp_project/prj_doc_view_data")
    Call<List<ProJectVO>> prj_doc_view_data (@QueryMap Map<String,Object> req_map);

    @GET("erp_project/unit_item_group_list")
    Call<List<ProJectVO>> unit_item_group_list (@QueryMap Map<String,Object> req_map);





    /*설치 확인서 [작업] 호출*/
    @GET("AndroidRegister/JobNameSpinner")
    Call<List<Bus_OfficeVO>> JobNameSpinner();

    /*설치 확인서 [작업-2] 호출- 버전스피너 아래있는 작업스피너..*/
    @GET("AndroidRegister/PrjBaseInfraJobSpinner")
    Call<List<Bus_OfficeVO>> PrjBaseInfraJobSpinner();

    /*설치 확인서 [조합목록] 호출*/
    @GET("AndroidRegister/OfficeGroupSpinner")
    Call<List<Bus_OfficeVO>> OfficeGroupSpinner();



    /*설치 확인서 [운수사] 호출*/
    @GET("AndroidRegister/BusOffName")
    Call<List<Bus_OfficeVO>> BusOffName(@Query("office_group") String office_group);

    /*설치 확인서 리사이클러뷰: 카메라 앨범/ 촬영기능 etc 호출..*/
    @GET("AndroidRegister/BusOffRecyclerviewMedia")
    Call<List<Bus_OfficeVO>> BusOffRecyclerviewMedia(@Query("office_group") String office_group, @Query("version") String version);


    @GET("AndroidRegister/BusGarage_Route_Spinner")
    Call<List<Bus_OfficeVO>> BusGarageRouteSpinner(@Query("transp_bizr_id") String transp_bizr_id, @Query("bus_id") String bus_id);


    /*설치 확인서 [영업소] 호출*/
    @GET("AndroidRegister/GarageSpinner")
    Call<List<Bus_OfficeVO>> GarageSpinner(@Query("transp_bizr_id") String transp_bizr_id, @Query("bus_id") String bus_id);

    /*설치 확인서 [노선번호] 호출*/
    @GET("AndroidRegister/BusRouteSpinner")
    Call<List<Bus_OfficeVO>> BusRouteSpinner(@Query("transp_bizr_id") String transp_bizr_id, @Query("bus_id") String bus_id);

    /*설치 확인서 [버전] 호출*/
    @GET("AndroidRegister/UnitCode_VersionSpinner")
    Call<List<Bus_OfficeVO>> UnitCode_VersionSpinner();


    /*두번째 fragment*/
    /*품목 스피너*/
    @GET("AndroidRegister/ItemGroupNameSpinner")
    Call<List<Bus_OfficeVO>> ItemGroupNameSpinner(@Query("office_group") String office_group);


    /*상세품목 스피너*/
    @GET("AndroidRegister/ItemEachNameSpinner")
    Call<List<Bus_OfficeVO>> ItemEachNameSpinner(@Query("item_code") String item_code);

    /*품목, 상세품목 대신... 케이블 설치 입력 테이블*/
    @GET("AndroidRegister/cableItemLists")
    Call<List<Bus_OfficeVO>> cableItemLists(@Query("doc_version") String doc_version);

    /*설치확인서 : 기본정보 + 사진경로 & 시리얼번호*/
    @GET("AndroidRegister/insert_prj_def_val")
    Call<String> insert_prj_def_val(@Query("prj_name") String prj_name
                             ,@Query("reg_dtti") String reg_dtti
                             ,@Query("reg_emp_id") String reg_emp_id
                             ,@Query("transp_bizr_id") String transp_bizr_id
                             ,@Query("busoff_name") String busoff_name
                             ,@Query("garage_id") String garage_id
                             ,@Query("garage_name") String garage_name
                             ,@Query("route_id") String route_id
                             ,@Query("route_num") String route_num
                             ,@Query("bus_id") String bus_id
                             ,@Query("bus_num") String bus_num
                             ,@Query("vehicle_num") String vehicle_num
                             ,@Query("job_type") String job_type
                            ,@Query("item_1") String item_1
                            ,@Query("item_2") String item_2
                            ,@Query("item_3") String item_3
                            ,@Query("item_4") String item_4
                            ,@Query("item_5") String item_5
                            ,@Query("item_6") String item_6
                            ,@Query("item_7") String item_7
                            ,@Query("item_8") String item_8
                            ,@Query("item_9") String item_9
                            ,@Query("item_10") String item_10
                            ,@Query("item_11") String item_11
                            ,@Query("item_12") String item_12
                            ,@Query("item_13") String item_13
                            ,@Query("item_14") String item_14
                            ,@Query("item_15") String item_15
                            ,@Query("item_16") String item_16
                            ,@Query("item_17") String item_17
                            ,@Query("item_18") String item_18
                            ,@Query("item_19") String item_19
                            ,@Query("item_20") String item_20
                            ,@Query("item_21") String item_21
                            ,@Query("item_22") String item_22
                            ,@Query("item_23") String item_23
                            ,@Query("item_24") String item_24
                            ,@Query("item_25") String item_25
                            ,@Query("item_26") String item_26
                            ,@Query("item_27") String item_27
                            ,@Query("item_28") String item_28
                            ,@Query("item_29") String item_29
                            ,@Query("item_30") String item_30
                            ,@Query("item_31") String item_31
                            ,@Query("item_32") String item_32
                            ,@Query("item_33") String item_33
                            ,@Query("item_34") String item_34
                            ,@Query("item_35") String item_35
                            ,@Query("item_36") String item_36
                            ,@Query("item_37") String item_37
                            ,@Query("item_38") String item_38
                            ,@Query("item_39") String item_39
                            ,@Query("item_40") String item_40
                            ,@Query("item_41") String item_41
                            ,@Query("item_42") String item_42
                            ,@Query("item_43") String item_43
                            ,@Query("item_44") String item_44
                            ,@Query("item_45") String item_45
                            ,@Query("item_46") String item_46
                            ,@Query("item_47") String item_47
                            ,@Query("item_48") String item_48
                            ,@Query("item_49") String item_49
                            ,@Query("item_50") String item_50 );


    /*뒷페이지*/
    @GET("AndroidRegister/update_prj_def_val2")
    Call<String> update_prj_def_val2(@Query("prj_name") String prj_name
            ,@Query("reg_dtti") String reg_dtti
            ,@Query("reg_emp_id") String reg_emp_id
            ,@Query("transp_bizr_id") String transp_bizr_id
            ,@Query("busoff_name") String busoff_name
            ,@Query("garage_id") String garage_id
            ,@Query("garage_name") String garage_name
            ,@Query("route_id") String route_id
            ,@Query("route_num") String route_num
            ,@Query("bus_id") String bus_id
            ,@Query("bus_num") String bus_num
            ,@Query("vehicle_num") String vehicle_num
            ,@Query("job_type") String job_type
            ,@Query("item_1") String item_1
            ,@Query("item_2") String item_2
            ,@Query("item_3") String item_3
            ,@Query("item_4") String item_4
            ,@Query("item_5") String item_5
            ,@Query("item_6") String item_6
            ,@Query("item_7") String item_7
            ,@Query("item_8") String item_8
            ,@Query("item_9") String item_9
            ,@Query("item_10") String item_10
            ,@Query("item_11") String item_11
            ,@Query("item_12") String item_12
            ,@Query("item_13") String item_13
            ,@Query("item_14") String item_14
            ,@Query("item_15") String item_15
            ,@Query("item_16") String item_16
            ,@Query("item_17") String item_17
            ,@Query("item_18") String item_18
            ,@Query("item_19") String item_19
            ,@Query("item_20") String item_20
            ,@Query("item_21") String item_21
            ,@Query("item_22") String item_22
            ,@Query("item_23") String item_23
            ,@Query("item_24") String item_24
            ,@Query("item_25") String item_25
            ,@Query("item_26") String item_26
            ,@Query("item_27") String item_27
            ,@Query("item_28") String item_28
            ,@Query("item_29") String item_29
            ,@Query("item_30") String item_30
            ,@Query("item_31") String item_31
            ,@Query("item_32") String item_32
            ,@Query("item_33") String item_33
            ,@Query("item_34") String item_34
            ,@Query("item_35") String item_35
            ,@Query("item_36") String item_36
            ,@Query("item_37") String item_37
            ,@Query("item_38") String item_38
            ,@Query("item_39") String item_39
            ,@Query("item_40") String item_40
            ,@Query("item_41") String item_41
            ,@Query("item_42") String item_42
            ,@Query("item_43") String item_43
            ,@Query("item_44") String item_44
            ,@Query("item_45") String item_45
            ,@Query("item_46") String item_46
            ,@Query("item_47") String item_47
            ,@Query("item_48") String item_48
            ,@Query("item_49") String item_49
            ,@Query("item_50") String item_50
            ,@Query("item_61") String item_61
            ,@Query("item_62") String item_62
            ,@Query("item_63") String item_63
            ,@Query("item_64") String item_64
            ,@Query("item_65") String item_65
            ,@Query("item_66") String item_66
            ,@Query("item_67") String item_67
            ,@Query("item_68") String item_68
            ,@Query("item_69") String item_69
            ,@Query("item_70") String item_70
            ,@Query("item_71") String item_71
            ,@Query("item_72") String item_72
            ,@Query("item_73") String item_73
            ,@Query("item_74") String item_74
            ,@Query("item_75") String item_75
            ,@Query("item_76") String item_76
            ,@Query("item_77") String item_77
            ,@Query("item_78") String item_78
            ,@Query("item_79") String item_79
            ,@Query("item_80") String item_80
            ,@Query("item_81") String item_81
            ,@Query("item_82") String item_82
            ,@Query("item_83") String item_83
            ,@Query("item_84") String item_84
            ,@Query("item_85") String item_85
            ,@Query("item_86") String item_86
            ,@Query("item_87") String item_87
            ,@Query("item_88") String item_88
            ,@Query("item_89") String item_89
            ,@Query("item_90") String item_90
            ,@Query("item_91") String item_91
            ,@Query("item_92") String item_92
            ,@Query("item_93") String item_93
            ,@Query("item_94") String item_94
            ,@Query("item_95") String item_95
            ,@Query("item_96") String item_96
            ,@Query("item_97") String item_97
            ,@Query("item_98") String item_98
            ,@Query("item_99") String item_99
            ,@Query("item_100") String item_100);

    /*설치 확인조회 운수사 스피너*/
    @GET("AndroidRegister/BusOffNameSpinner")
    Call<List<Bus_OfficeVO>> BusOffNameSpinner( @Query("table_name") String table_name
                                                , @Query("st_date") String st_date
                                                , @Query("ed_date") String ed_date
                                                , @Query("job_type") String jop_type);


    /*설치 확인조회 영업소 스피너*/
    @GET("AndroidRegister/GarageNameSpinner")
    Call<List<Bus_OfficeVO>> GarageNameSpinner( @Query("table_name") String table_name
                                                , @Query("transp_bizr_id") String transp_bizr_id);


    /*설치 확인조회 리사이클러뷰: 차량리스트*/
    @GET("AndroidRegister/Transp_Bizr_List")
    Call<List<Bus_OfficeVO>> Transp_Bizr_List(@Query("table_name") String table_name
                                                    , @Query("st_date") String st_date
                                                    , @Query("ed_date") String ed_date
                                                    , @Query("transp_bizr_id") String transp_bizr_id
                                                    , @Query("job_type") String job_type);

    /*설치 확인조회 리사이클러뷰: 차량리스트*/
    /*영업소 스피너 [조회버튼]을 누르면 실행*/
    @GET("AndroidRegister/Transp_Bizr_List2")
    Call<List<Bus_OfficeVO>> Transp_Bizr_List2(@Query("table_name") String table_name
                                            , @Query("st_date") String st_date
                                            , @Query("ed_date") String ed_date
                                            , @Query("garage_id") String garage_id
                                            , @Query("transp_bizr_id") String transp_bizr_id
                                            , @Query("job_type") String job_type);

    @GET("AndroidRegister/Transp_Bizr_List2")
    Call<List<Bus_OfficeVO>> MyInstallationDetail(@Query("table_name") String table_name
                                                    , @Query("st_date") String st_date
                                                    , @Query("ed_date") String ed_date
                                                    , @Query("reg_emp_id") String reg_emp_id
                                                    , @Query("finish_yn") String finish_yn);


    /*step3 상단*/
    @GET("AndroidRegister/Transp_Bizr_List_Info_Item")
    Call<List<Bus_OfficeVO>> Transp_Bizr_List_Info_Item(@Query("table_name") String table_name
                                                        , @Query("reg_dtti") String reg_dtti
                                                        , @Query("transp_bizr_id") String transp_bizr_id
                                                        , @Query("bus_id") String bus_id
                                                        , @Query("job_type") String job_type);



    @GET("AndroidRegister/Transp_Bizr_List_Info_Item2")
    Call<List<Bus_OfficeVO>> Transp_Bizr_List_Info_Item2(@Query("table_name") String table_name
                                                        , @Query("reg_dtti") String reg_dtti
                                                        , @Query("transp_bizr_id") String transp_bizr_id
                                                        , @Query("bus_id") String bus_id
                                                        , @Query("job_type") String job_type);


    /*단말기 상세목록 리사이클러뷰*/
    @GET("AndroidRegister/unitLists")
    Call<List<Bus_OfficeVO>> unitLists_detail( @Query("table_name") String table_name
                                                , @Query("transp_bizr_id") String transp_bizr_id
                                                , @Query("job_type") String job_type
                                                , @Query("bus_num") String bus_num
                                                , @Query("garage_name") String garage_name); // 테이블명, 운수사ID,
                                                                                             // 작업타입, BusNums, 영업소명

    /*2단계 단말기, 케이블 상세목록*/
    @GET("AndroidRegister/step2_unitList")
    Call<List<Bus_OfficeVO>> step2_unitList(@Query("table_name") String table_name
                                            , @Query("transp_bizr_id") String transp_bizr_id
                                            , @Query("job_type") String job_type
                                            , @Query("bus_num") String bus_num
                                            , @Query("garage_name") String garage_name);



    /*2단계 완료버튼- insert*/
    @GET("AndroidRegister/insert_installation_lists")
    Call<List<Bus_OfficeVO>> insert_installation_lists( @Query("table_name") String table_name
                                                      , @Query("sign_dtti") String sign_dtti
                                                      , @Query("transp_bizr_id") String transp_bizr_id
                                                      , @Query("job_type") String job_type
                                                      , @Query("reg_emp_id") String reg_emp_id
                                                      , @Query("bus_num") String bus_num
                                                      , @Query("bussoff_name") String bussoff_name
                                                      , @Query("garage_name") String garage_name
                                                      , @Query("sign_name") String sign_name
                                                      , @Query("sign_phone_num") String sign_phone_num
                                                      , @Query("sign_path") String sign_path);


    @GET("AndroidRegister/bus_num_list")
    Call<List<Bus_OfficeVO>> bus_num_list(@Query("transp_bizr_id") String transp_bizr_id);



    //테스트 목적
    // Retrofit Service
    @GET("test/app_busoffice_test")
    Call<List<Bus_infoVo>> app_busoffice_test(@Query("useyn") String useryn);

    /*출고신청 페이지*/

    //지부 스피너..
    @GET("test/DepName")
    Call<List<TestAllVO>> DepNameList(@Query("emp_id") String dep_name
                                      ,@Query("emp_dep_id") String emp_dep_id);

    //분류 스피너..
    @GET("test/TestList")
    Call<List<TestAllVO>> InfraList(@Query("barcode_dep_id") String barcode_dep_id
                                    ,@Query("emp_dep_id") String emp_dep_id);


    //분류 스피너 아이템 선택한 상태현황
    @GET("test/selected_unit_status")
    Call<List<TestAllVO>> selected_unit_status(@Query("barcode_dep_id") String barcode_dep_id
                                              ,@Query("unit_group") String unit_group);  //전달받을 데이터 스프링으로 던저주기


    // 재고 리스트
    // 요건데
    // @get == @RequestMapping 이거는 알고계시져 앗 네
    // 그럼 select_stock_list 이친구는 test/select_stock_list 여기에
    // 파라미터 (@Query("barcode_dep_id") String barcode_dep_id
    //         ,@Query("unit_code") String unit_code
    //         ,@Query("rep_unit_code") String rep_unit_code
    // 이걸 전달하는 함수이져 ? 스프링쪽에 전달한다는 말이져? 네 네네 그럼 다시 돌아가서
    @GET("test/select_stock_list")
    Call<List<TestAllVO>> select_stock_list(@Query("barcode_dep_id") String barcode_dep_id
                                           ,@Query("unit_code") String unit_code
                                           ,@Query("rep_unit_code") String rep_unit_code);


    // 재고리스트(2) ----> 이거 안씀..
   @GET("test/select_stock_list_2")
    Call<List<TestAllVO>> select_stock_list_2(@Query("barcode_dep_id") String barcode_dep_id
                                            ,@Query("unit_code") String unit_code
                                            ,@Query("rep_unit_code") String rep_unit_code);


    //출고위치
    @GET("test/release_location")
    Call<List<TestAllVO>> release_location(@Query("emp_id") String emp_id
                                          ,@Query("barcode_dep_id") String location_id   //@Query("barcode_dep_id") - 스프링 확인하기   == WHERE LOCATION_ID <> #{barcode_dep_id} /*출고위치의 Location_id*/ 스프링과 같아야함.
                                            ,@Query("emp_dep_id") String emp_dep_id);





    //예약상태 (SQL1, SQL2, SQL3) 이걸로 다시...   하나로 관리
    @GET("test/AndroidUnitBookingChk")           // 타입- void (리턴타입 없음)
    Call<Void> AppUnitBookingChk_map(@Query("emp_id") String emp_id
                                    ,@Query("barcode_dep_id") String barcode_dep_id
                                    ,@Query("un_yn") String un_yn
                                    ,@Query("unit_code") String unit_code
                                    ,@Query("rep_unit_code") String rep_unit_code
                                    ,@Query("unit_id") String unit_id
                                    ,@Query("in_yn") String in_yn
                                    ,@Query("req_date") String req_date
                                    ,@Query("notice") String notice
                                    ,@Query("request_dep_id") String request_dep_id
                                    ,@Query("response_dep_id") String response_dep_id);




    //최종목록 확인하기
    @GET("test/Android_FinalBookingChk")
    Call<List<TestAllVO>> FinalBookingChk(@Query("emp_id") String req_emp_id
                                         ,@Query("in_yn") String in_yn);




    //출고신청 완료버튼 누르면 (SQL1-insert,  SQL2-insert, SQL3-update, SQL4-update)  ----  하나로 관리... Void 타입
    @GET("test/AppInsertRequestList")
    Call<Void> AppInsertRequestList(@Query("emp_id") String emp_id
                                   ,@Query("req_date") String req_date
                                   ,@Query("notice") String notice
                                   ,@Query("request_dep_id") String request_dep_id      //입고스피너 개인 김민수씨 개인 지부값 992000203
                                   ,@Query("response_dep_id") String response_dep_id    //출고 스피너 값
                                   ,@Query("barcode_dep_id") String barcode_dep_id );      // emp_dep_id 로그인 한 사람의 지부 (전역변수)






    /*단말기 입출고 현황 페이지*/
    @GET("test/AppInventoryInOutputList")
    Call<List<TestAllVO>> InventoryInOutputList(@Query("st_date") String st_date
                                                ,@Query("ed_date") String ed_date
                                                ,@Query("emp_id") String emp_id
                                                ,@Query("in_yn") String in_yn);


    /*단말기 입출고 현황 페이지- 상세정보*/
    @GET("test/AppInventoryInOutputListDetail")
    Call<List<TestAllVO>> AppInventoryInOutputListDetail(@Query("reg_date") String reg_date
                                                ,@Query("reg_time") String reg_time
                                                ,@Query("req_type") String req_type
                                                ,@Query("emp_id") String emp_id);





    /*입고신청 페이지*/
    /*출고위치 스피너*/
    @GET("test/AppOutLocationSpinner")
    Call<List<TestAllVO>> AppOutLocationSpinnerList(@Query("emp_id") String emp_id
                                                  ,@Query("in_yn") String in_yn
                                                  ,@Query("emp_dep_id") String emp_dep_id);

    /*분류 스피너*/
    @GET("test/AppUnitGroupSpinner")
            Call<List<TestAllVO>> AppUnitGroupSpinnerList(@Query("barcode_dep_id") String barcode_dep_id
                                                         ,@Query("in_yn") String in_yn
                                                         ,@Query("emp_dep_id") String emp_dep_id);

    /*분류 선택현황..*/
    @GET("test/AppUnitGroupSelected")
            Call<List<TestAllVO>> AppUnitGroupSelectedList(@Query("unit_group") String unit_group
                                                          ,@Query("barcode_dep_id") String barcode_dep_id
                                                          ,@Query("in_yn") String un_yn);

    /*입고위치 스피너*/
    @GET("test/AppInventoryInOffice")
    Call<List<TestAllVO>> AppInventoryInOfficeList( @Query("in_yn") String in_yn
                                                    ,@Query("emp_id") String emp_id
                                                    ,@Query("barcode_dep_id") String location_id   //@Query("barcode_dep_id") - 스프링 확인하기   == WHERE LOCATION_ID <> #{barcode_dep_id} /*출고위치의 Location_id*/ 스프링과 같아야함.
                                                    ,@Query("emp_dep_id") String emp_dep_id);

    /*입고 리스트 & 입고리스트 확인 (리사이클러뷰)*/
    /*선택현황 선택하면 입고리스트 목록 보이기*/
    @GET("test/AppSelectInUnitList")
    Call<List<TestAllVO>> AppSelectInUnitList(@Query("barcode_dep_id") String barcode_dep_id
                                    ,@Query("unit_code") String unit_code
                                    ,@Query("rep_unit_code") String rep_unit_code
                                    ,@Query("in_yn") String in_yn
    );



    /*입고리스트 리사이클러뷰 예약, 업데이트, 삭제..등*/    /*하나로 관리
    */
    @GET("test/AppInUnitList")
    Call<Void> AppInUnitList(@Query("barcode_dep_id") String barcode_dep_id
                            ,@Query("unit_code") String unit_code
                            ,@Query("rep_unit_code") String rep_unit_code
                            ,@Query("unit_id") String unit_id
                            ,@Query("in_yn") String in_yn
                            ,@Query("un_yn") String un_yn
                            ,@Query("emp_id") String emp_id
                            ,@Query("req_date") String req_date
                            ,@Query("notice") String notice
                            ,@Query("request_dep_id") String request_dep_id
                            ,@Query("response_dep_id") String response_dep_id);


    /*입고대상 목록 확인버튼*/
    @GET("test/Android_InFinalBookingChk")
    Call<List<TestAllVO>>In_FinalBookingChk(@Query("emp_id") String req_emp_id
                                            ,@Query("in_yn") String in_yn);





    /*입고신청 완료버튼*/ // ----  하나로 관리... Void 타입
    @GET("test/AppInsertRequestList_in")
    Call<Void> AppInsertRequestList_in(@Query("emp_id") String emp_id
                                    ,@Query("req_date") String req_date
                                    ,@Query("notice") String notice
                                    ,@Query("request_dep_id") String request_dep_id      //입고스피너 개인 김민수씨 개인 지부값 992000203 <-> 반대?
                                    ,@Query("response_dep_id") String response_dep_id         //출고 스피너 값 <-> 반대?
                                    ,@Query("barcode_dep_id") String barcode_dep_id         // emp_dep_id 로그인 한 사람의 지부 (전역변수)
                                    ,@Query("in_yn") String in_yn);







    // Retrofit Helper
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

    public static final Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("http://192.168.0.122:8180/controller/")
            .baseUrl("http://ierp.interpass.co.kr/controller/")

            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

}