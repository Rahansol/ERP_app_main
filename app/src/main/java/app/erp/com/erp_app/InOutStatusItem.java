package app.erp.com.erp_app;

public class InOutStatusItem {
    public String req_type;  // 타입- 출고 또는 입고
    public String reg_date;
    public String reg_time;
    public String req_date;  // 요청일
    public String res_date;  // 물류일

    public String res_name;      // 출고: 강북지부
    public String req_name;      // 입고: 김민수

    public String unit_cnt;      // 신청수량
    public String schedule_cnt;  // 물류예정
    public String cancel_cnt;    // 출고불가
    public String receipt_cnt;   // 수령수량
    public String unrequest_cnt; // 신청취소


    public InOutStatusItem() {
    }

    public InOutStatusItem(String req_type, String reg_date, String reg_time, String req_date, String res_date, String res_name, String req_name, String unit_cnt, String schedule_cnt, String cancel_cnt, String receipt_cnt, String unrequest_cnt) {
        this.req_type = req_type;
        this.reg_date = reg_date;
        this.reg_time = reg_time;
        this.req_date = req_date;
        this.res_date = res_date;
        this.res_name = res_name;
        this.req_name = req_name;
        this.unit_cnt = unit_cnt;
        this.schedule_cnt = schedule_cnt;
        this.cancel_cnt = cancel_cnt;
        this.receipt_cnt = receipt_cnt;
        this.unrequest_cnt = unrequest_cnt;
    }
}
