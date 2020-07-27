package app.erp.com.erp_app.vo;

import java.io.Serializable;

public class Over_Work_List_VO implements Serializable {

    public Over_Work_List_VO() {
    }

    private String work_date;
    private String reg_date;
    private String reg_time;
    private String emp_id;
    private String dep_code;
    private String status;
    private String st;
    private String notice;
    private String sign_emp_id;
    private String sign_date;
    private String sign_time;
    private String sign_notice;

    private String emp_name;

    private String t_reg_date;
    private String t_reg_time;
    private String office_group;
    private String busoff_name;
    private String route_id;
    private String trouble_high_name;
    private String trouble_low_name;
    private String fix_st;

    private String display_type;

    @Override
    public String toString() {
        return "Over_Work_List_VO{" +
                "work_date='" + work_date + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", emp_id='" + emp_id + '\'' +
                ", dep_code='" + dep_code + '\'' +
                ", status='" + status + '\'' +
                ", st='" + st + '\'' +
                ", notice='" + notice + '\'' +
                ", sign_emp_id='" + sign_emp_id + '\'' +
                ", sign_date='" + sign_date + '\'' +
                ", sign_time='" + sign_time + '\'' +
                ", sign_notice='" + sign_notice + '\'' +
                ", emp_name='" + emp_name + '\'' +
                ", t_reg_date='" + t_reg_date + '\'' +
                ", t_reg_time='" + t_reg_time + '\'' +
                ", office_group='" + office_group + '\'' +
                ", busoff_name='" + busoff_name + '\'' +
                ", route_id='" + route_id + '\'' +
                ", trouble_high_name='" + trouble_high_name + '\'' +
                ", trouble_low_name='" + trouble_low_name + '\'' +
                ", fix_st='" + fix_st + '\'' +
                ", display_type='" + display_type + '\'' +
                '}';
    }

    public String getWork_date() {
        return work_date;
    }

    public void setWork_date(String work_date) {
        this.work_date = work_date;
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

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getDep_code() {
        return dep_code;
    }

    public void setDep_code(String dep_code) {
        this.dep_code = dep_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getSign_emp_id() {
        return sign_emp_id;
    }

    public void setSign_emp_id(String sign_emp_id) {
        this.sign_emp_id = sign_emp_id;
    }

    public String getSign_date() {
        return sign_date;
    }

    public void setSign_date(String sign_date) {
        this.sign_date = sign_date;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getSign_notice() {
        return sign_notice;
    }

    public void setSign_notice(String sign_notice) {
        this.sign_notice = sign_notice;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getT_reg_date() {
        return t_reg_date;
    }

    public void setT_reg_date(String t_reg_date) {
        this.t_reg_date = t_reg_date;
    }

    public String getT_reg_time() {
        return t_reg_time;
    }

    public void setT_reg_time(String t_reg_time) {
        this.t_reg_time = t_reg_time;
    }

    public String getOffice_group() {
        return office_group;
    }

    public void setOffice_group(String office_group) {
        this.office_group = office_group;
    }

    public String getBusoff_name() {
        return busoff_name;
    }

    public void setBusoff_name(String busoff_name) {
        this.busoff_name = busoff_name;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getTrouble_high_name() {
        return trouble_high_name;
    }

    public void setTrouble_high_name(String trouble_high_name) {
        this.trouble_high_name = trouble_high_name;
    }

    public String getTrouble_low_name() {
        return trouble_low_name;
    }

    public void setTrouble_low_name(String trouble_low_name) {
        this.trouble_low_name = trouble_low_name;
    }

    public String getFix_st() {
        return fix_st;
    }

    public void setFix_st(String fix_st) {
        this.fix_st = fix_st;
    }

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }
}
