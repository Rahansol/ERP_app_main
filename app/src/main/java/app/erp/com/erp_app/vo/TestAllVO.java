package app.erp.com.erp_app.vo;

/**
 * Created by hsra on 2019-06-21.
 */

public class TestAllVO {
    private String emp_dep_id;

    private String dep_name;   //지부명
    private String barcode_dep_id;  //  == location_id 같음..
    private String location_id;    // == barcode_dep_id 같음..
    private String dep_code;    //지부코드
    private String emp_id;      //직원 ID
    private String unit_group;  //단말기 분류
    private String unit_explain; //달말기 상태현황에 단말기 분류의 [종류]들
    private String unit_code;   //단말기 코드
    private String rep_unit_code;  //단말기 버전별 코드
    private String unit_cnt; //단말기별 총 count
    private String rnum;   //번호?순번?
    private String unit_ver;    // ex; V2.0 복수하차..
    private String unit_id;   //제품번호
    private String un_yn;      //선택해제
    private String in_yn;
    private String reg_date;
    private String reg_time;
    private String req_emp_id;
    private String req_type;
    private String res_name;
    private String req_name;
    private String res_date;
    private String schedule_cnt;
    private String req_date;

    @Override
    public String toString() {
        return "TestAllVO{" +
                "emp_dep_id='" + emp_dep_id + '\'' +
                ", dep_name='" + dep_name + '\'' +
                ", barcode_dep_id='" + barcode_dep_id + '\'' +
                ", location_id='" + location_id + '\'' +
                ", dep_code='" + dep_code + '\'' +
                ", emp_id='" + emp_id + '\'' +
                ", unit_group='" + unit_group + '\'' +
                ", unit_explain='" + unit_explain + '\'' +
                ", unit_code='" + unit_code + '\'' +
                ", rep_unit_code='" + rep_unit_code + '\'' +
                ", unit_cnt='" + unit_cnt + '\'' +
                ", rnum='" + rnum + '\'' +
                ", unit_ver='" + unit_ver + '\'' +
                ", unit_id='" + unit_id + '\'' +
                ", un_yn='" + un_yn + '\'' +
                ", in_yn='" + in_yn + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", req_emp_id='" + req_emp_id + '\'' +
                ", req_type='" + req_type + '\'' +
                ", res_name='" + res_name + '\'' +
                ", req_name='" + req_name + '\'' +
                ", res_date='" + res_date + '\'' +
                ", schedule_cnt='" + schedule_cnt + '\'' +
                ", req_date='" + req_date + '\'' +
                ", notice='" + notice + '\'' +
                ", exe_type='" + exe_type + '\'' +
                ", request_dep_id='" + request_dep_id + '\'' +
                ", response_dep_id='" + response_dep_id + '\'' +
                ", receipt_cnt='" + receipt_cnt + '\'' +
                ", unrequest_cnt='" + unrequest_cnt + '\'' +
                ", cancel_cnt='" + cancel_cnt + '\'' +
                ", req_location='" + req_location + '\'' +
                '}';
    }

    private String notice;

    public String getExe_type() {
        return exe_type;
    }

    public void setExe_type(String exe_type) {
        this.exe_type = exe_type;
    }

    private String exe_type;

    private String request_dep_id;
    private String response_dep_id;

    public String getRequest_dep_id() {
        return request_dep_id;
    }

    public void setRequest_dep_id(String request_dep_id) {
        this.request_dep_id = request_dep_id;
    }

    public String getResponse_dep_id() {
        return response_dep_id;
    }

    public void setResponse_dep_id(String response_dep_id) {
        this.response_dep_id = response_dep_id;
    }


    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }



    public TestAllVO() {
    }

    private String receipt_cnt;

    public TestAllVO(String emp_dep_id, String dep_name, String barcode_dep_id, String location_id, String dep_code, String emp_id, String unit_group, String unit_explain, String unit_code, String rep_unit_code, String unit_cnt, String rnum, String unit_ver, String unit_id, String un_yn, String in_yn, String reg_date, String reg_time, String req_emp_id, String req_type, String res_name, String req_name, String res_date, String schedule_cnt, String receipt_cnt, String req_date, String unrequest_cnt, String cancel_cnt, String req_location) {
        this.emp_dep_id = emp_dep_id;
        this.dep_name = dep_name;
        this.barcode_dep_id = barcode_dep_id;
        this.location_id = location_id;
        this.dep_code = dep_code;
        this.emp_id = emp_id;
        this.unit_group = unit_group;
        this.unit_explain = unit_explain;
        this.unit_code = unit_code;
        this.rep_unit_code = rep_unit_code;
        this.unit_cnt = unit_cnt;
        this.rnum = rnum;
        this.unit_ver = unit_ver;
        this.unit_id = unit_id;
        this.un_yn = un_yn;
        this.in_yn = in_yn;
        this.reg_date = reg_date;
        this.reg_time = reg_time;
        this.req_emp_id = req_emp_id;
        this.req_type = req_type;
        this.res_name = res_name;
        this.req_name = req_name;
        this.res_date = res_date;
        this.schedule_cnt = schedule_cnt;
        this.receipt_cnt = receipt_cnt;
        this.req_date = req_date;
        this.unrequest_cnt = unrequest_cnt;
        this.cancel_cnt = cancel_cnt;
        this.req_location = req_location;
    }



    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }




    private String unrequest_cnt;
    private String cancel_cnt;

    private String req_location;

    public String getReq_location() {
        return req_location;
    }

    public void setReq_location(String req_location) {
        this.req_location = req_location;
    }

    public String getEmp_dep_id() {
        return emp_dep_id;
    }

    public void setEmp_dep_id(String emp_dep_id) {
        this.emp_dep_id = emp_dep_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getReq_emp_id() {
        return req_emp_id;
    }

    public void setReq_emp_id(String req_emp_id) {
        this.req_emp_id = req_emp_id;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getIn_yn() {
        return in_yn;
    }

    public void setIn_yn(String in_yn) {
        this.in_yn = in_yn;
    }


    public String getUn_yn() {
        return un_yn;
    }

    public void setUn_yn(String un_yn) {
        this.un_yn = un_yn;
    }
    // getter, setter, toString @override 하기....


    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getBarcode_dep_id() {
        return barcode_dep_id;
    }

    public void setBarcode_dep_id(String barcode_dep_id) {
        this.barcode_dep_id = barcode_dep_id;
    }

    public String getDep_code() {
        return dep_code;
    }

    public void setDep_code(String dep_code) {
        this.dep_code = dep_code;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getUnit_group() {
        return unit_group;
    }

    public void setUnit_group(String unit_group) {
        this.unit_group = unit_group;
    }

    public String getUnit_explain() {
        return unit_explain;
    }

    public void setUnit_explain(String unit_explain) {
        this.unit_explain = unit_explain;
    }

    public String getUnit_code() {
        return unit_code;
    }

    public void setUnit_code(String unit_code) {
        this.unit_code = unit_code;
    }

    public String getRep_unit_code() {
        return rep_unit_code;
    }

    public void setRep_unit_code(String rep_unit_code) {
        this.rep_unit_code = rep_unit_code;
    }

    public String getUnit_cnt() {
        return unit_cnt;
    }

    public void setUnit_cnt(String unit_cnt) {
        this.unit_cnt = unit_cnt;
    }

    public String getRnum() {
        return rnum;
    }

    public void setRnum(String rnum) {
        this.rnum = rnum;
    }

    public String getUnit_ver() {
        return unit_ver;
    }

    public void setUnit_ver(String unit_ver) {
        this.unit_ver = unit_ver;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public String getReq_name() {
        return req_name;
    }

    public void setReq_name(String req_name) {
        this.req_name = req_name;
    }

    public String getRes_date() {
        return res_date;
    }

    public void setRes_date(String res_date) {
        this.res_date = res_date;
    }

    public String getSchedule_cnt() {
        return schedule_cnt;
    }

    public void setSchedule_cnt(String schedule_cnt) {
        this.schedule_cnt = schedule_cnt;
    }

    public String getReceipt_cnt() {
        return receipt_cnt;
    }

    public void setReceipt_cnt(String receipt_cnt) {
        this.receipt_cnt = receipt_cnt;
    }

    public String getUnrequest_cnt() {
        return unrequest_cnt;
    }

    public void setUnrequest_cnt(String unrequest_cnt) {
        this.unrequest_cnt = unrequest_cnt;
    }

    public String getCancel_cnt() {
        return cancel_cnt;
    }

    public void setCancel_cnt(String cancel_cnt) {
        this.cancel_cnt = cancel_cnt;
    }

    //    private String junk_date;

}
