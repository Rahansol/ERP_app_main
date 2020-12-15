package app.erp.com.erp_app;

public class StockListItems {
    //recycler_stock_list.xml 의 items
    //순번, 분류, 제품명, 제품번호


    String rnum;
    String unit_ver;
    String unit_id;
    Boolean checkbox;

    String un_yn;
    String in_yn;
    String emp_id;
    String rep_unit_code;
    String unit_code;
    String barcode_dep_id;

    String req_date;
    String notice;
    String request_dep_id;
    String response_dep_id;


    public StockListItems() {
    }


    public StockListItems(String rnum, String unit_ver, String unit_id, Boolean checkbox, String un_yn, String in_yn, String emp_id, String rep_unit_code, String unit_code, String barcode_dep_id, String req_date, String notice, String request_dep_id, String response_dep_id) {
        this.rnum = rnum;
        this.unit_ver = unit_ver;
        this.unit_id = unit_id;
        this.checkbox = checkbox;
        this.un_yn = un_yn;
        this.in_yn = in_yn;
        this.emp_id = emp_id;
        this.rep_unit_code = rep_unit_code;
        this.unit_code = unit_code;
        this.barcode_dep_id = barcode_dep_id;
        this.req_date = req_date;
        this.notice = notice;
        this.request_dep_id = request_dep_id;
        this.response_dep_id = response_dep_id;
    }
}
