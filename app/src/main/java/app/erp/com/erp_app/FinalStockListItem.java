package app.erp.com.erp_app;

public class FinalStockListItem {
    public String in_yn;
    public String un_yn;
    public String barcode_dep_id;
    public String req_emp_id;
    public String req_location;

    public String req_date;
    public String notice;
    public String request_dep_id;
    public String response_dep_id;

    //최종 선택목록 확인 아이템
    public String unit_ver;
    public String unit_id;
    public String unit_code;
    public String rep_unit_code;
    public Boolean checkBox;

    public FinalStockListItem() {
    }


    public FinalStockListItem(String un_yn, String barcode_dep_id, String req_emp_id, String req_location, String req_date, String notice, String request_dep_id, String response_dep_id, String unit_ver, String unit_id, String unit_code, String rep_unit_code, Boolean checkBox) {
        this.un_yn = un_yn;
        this.barcode_dep_id = barcode_dep_id;
        this.req_emp_id = req_emp_id;
        this.req_location = req_location;
        this.req_date = req_date;
        this.notice = notice;
        this.request_dep_id = request_dep_id;
        this.response_dep_id = response_dep_id;
        this.unit_ver = unit_ver;
        this.unit_id = unit_id;
        this.unit_code = unit_code;
        this.rep_unit_code = rep_unit_code;
        this.checkBox = checkBox;
    }
}
