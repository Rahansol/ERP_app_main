package app.erp.com.erp_app.vo;

/**
 * Created by hsra on 2019-06-21.
 */

public class ReleaseRequestVO {

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

    @Override
    public String toString() {
        return "TestAllVO{" +
                "dep_name='" + dep_name + '\'' +
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
                '}';
    }

    //    private String junk_date;

}
