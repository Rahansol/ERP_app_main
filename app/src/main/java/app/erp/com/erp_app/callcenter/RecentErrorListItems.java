package app.erp.com.erp_app.callcenter;

public class RecentErrorListItems {
    String reg_date;          //접수일
    String reg_emp_name;      //접수자
    String unit_before_id;    //교체 전
    String care_date;         //처리일
    String care_emp_name;     //처리자
    String unit_after_id;     //교체 후

    String busoff_name;
    String garage_id;
    String route_num;

    String unit_name;         //단말기명
    String trouble_high_name; //대분류
    String trouble_low_name;  //소분류
    String trouble_care_name; //조치
    String notice;

    public RecentErrorListItems() {
    }


    public RecentErrorListItems(String reg_date, String reg_emp_name, String unit_before_id, String care_date, String care_emp_name, String unit_after_id, String busoff_name, String garage_id, String route_num, String unit_name, String trouble_high_name, String trouble_low_name, String trouble_care_name, String notice) {
        this.reg_date = reg_date;
        this.reg_emp_name = reg_emp_name;
        this.unit_before_id = unit_before_id;
        this.care_date = care_date;
        this.care_emp_name = care_emp_name;
        this.unit_after_id = unit_after_id;
        this.busoff_name = busoff_name;
        this.garage_id = garage_id;
        this.route_num = route_num;
        this.unit_name = unit_name;
        this.trouble_high_name = trouble_high_name;
        this.trouble_low_name = trouble_low_name;
        this.trouble_care_name = trouble_care_name;
        this.notice = notice;
    }
}
