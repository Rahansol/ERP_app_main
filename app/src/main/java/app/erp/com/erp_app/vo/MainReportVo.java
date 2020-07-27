package app.erp.com.erp_app.vo;

public class MainReportVo {

    private String accept_type;
    private String cnt;

    private String dep_code;
    private String dep_name;
    private String emp_id;
    private String emp_name;
    private String tot_cnt;
    private String care_cnt;
    private String move_notice;

    private String no_cnt;

    private String plan_date;
    private String plan_notice;

    private String i_cnt;
    private String t_cnt;

    public MainReportVo() {
    }

    @Override
    public String toString() {
        return "MainReportVo{" +
                "accept_type='" + accept_type + '\'' +
                ", cnt='" + cnt + '\'' +
                ", dep_code='" + dep_code + '\'' +
                ", dep_name='" + dep_name + '\'' +
                ", emp_id='" + emp_id + '\'' +
                ", emp_name='" + emp_name + '\'' +
                ", tot_cnt='" + tot_cnt + '\'' +
                ", care_cnt='" + care_cnt + '\'' +
                ", move_notice='" + move_notice + '\'' +
                ", no_cnt='" + no_cnt + '\'' +
                ", plan_date='" + plan_date + '\'' +
                ", plan_notice='" + plan_notice + '\'' +
                ", i_cnt='" + i_cnt + '\'' +
                ", t_cnt='" + t_cnt + '\'' +
                '}';
    }

    public String getAccept_type() {
        return accept_type;
    }

    public void setAccept_type(String accept_type) {
        this.accept_type = accept_type;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getDep_code() {
        return dep_code;
    }

    public void setDep_code(String dep_code) {
        this.dep_code = dep_code;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getTot_cnt() {
        return tot_cnt;
    }

    public void setTot_cnt(String tot_cnt) {
        this.tot_cnt = tot_cnt;
    }

    public String getCare_cnt() {
        return care_cnt;
    }

    public void setCare_cnt(String care_cnt) {
        this.care_cnt = care_cnt;
    }

    public String getMove_notice() {
        return move_notice;
    }

    public void setMove_notice(String move_notice) {
        this.move_notice = move_notice;
    }

    public String getNo_cnt() {
        return no_cnt;
    }

    public void setNo_cnt(String no_cnt) {
        this.no_cnt = no_cnt;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }

    public String getPlan_notice() {
        return plan_notice;
    }

    public void setPlan_notice(String plan_notice) {
        this.plan_notice = plan_notice;
    }

    public String getI_cnt() {
        return i_cnt;
    }

    public void setI_cnt(String i_cnt) {
        this.i_cnt = i_cnt;
    }

    public String getT_cnt() {
        return t_cnt;
    }

    public void setT_cnt(String t_cnt) {
        this.t_cnt = t_cnt;
    }
}
