package app.erp.com.erp_app.vo;

import java.io.Serializable;

public class T_Trouble_HistoryListVO {

    private String reg_date;
    private String reg_time;
    private String dep_code;
    private String reg_emp_id;
    private String infra_code;
    private String transp_bizr_id;
    private String garage_id;
    private String service_id;
    private String route_id;
    private String bus_id;
    private String trouble_high_cd;
    private String trouble_low_cd;
    private String trouble_care_cd;
    private String ars_trouble_high_cd;
    private String ars_trouble_low_cd;
    private String unit_change_yn;
    private String unit_before_id;
    private String unit_after_id;
    private String notice;
    private String ars_notice;
    private String direct_care;
    private String care_emp_id;
    private String care_date;
    private String care_time;
    private String move_distance;
    private String move_time;
    private String wait_time;
    private String work_time;
    private String restore_yn;
    private String analysis_yn;
    private String bs_yn;
    private String mintong;
    private String st;
    private String ars_yn;
    private String job_viewer;
    private String job_viewer_view;
    private String driver_tel_num;
    private String report_date;

    private String trouble_high_cd_before;
    private String trouble_low_cd_before;
    private String unit_code_before;


    //업데이트 할때 where 절
    private String w_reg_date;
    private String w_care_time;
    private String w_unit_code;
    private String w_trouble_high_cd;
    private String w_trouble_low_cd;
    private String w_transp_bizr_id;



    //trouble b_code
    private String unit_code;
    private String ars_unit_code;
    private String unit_name;

    @Override
    public String toString() {
        return "T_Trouble_HistoryListVO{" +
                "reg_date='" + reg_date + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", dep_code='" + dep_code + '\'' +
                ", reg_emp_id='" + reg_emp_id + '\'' +
                ", infra_code='" + infra_code + '\'' +
                ", transp_bizr_id='" + transp_bizr_id + '\'' +
                ", garage_id='" + garage_id + '\'' +
                ", service_id='" + service_id + '\'' +
                ", route_id='" + route_id + '\'' +
                ", bus_id='" + bus_id + '\'' +
                ", trouble_high_cd='" + trouble_high_cd + '\'' +
                ", trouble_low_cd='" + trouble_low_cd + '\'' +
                ", trouble_care_cd='" + trouble_care_cd + '\'' +
                ", ars_trouble_high_cd='" + ars_trouble_high_cd + '\'' +
                ", ars_trouble_low_cd='" + ars_trouble_low_cd + '\'' +
                ", unit_change_yn='" + unit_change_yn + '\'' +
                ", unit_before_id='" + unit_before_id + '\'' +
                ", unit_after_id='" + unit_after_id + '\'' +
                ", notice='" + notice + '\'' +
                ", ars_notice='" + ars_notice + '\'' +
                ", direct_care='" + direct_care + '\'' +
                ", care_emp_id='" + care_emp_id + '\'' +
                ", care_date='" + care_date + '\'' +
                ", care_time='" + care_time + '\'' +
                ", move_distance='" + move_distance + '\'' +
                ", move_time='" + move_time + '\'' +
                ", wait_time='" + wait_time + '\'' +
                ", work_time='" + work_time + '\'' +
                ", restore_yn='" + restore_yn + '\'' +
                ", analysis_yn='" + analysis_yn + '\'' +
                ", bs_yn='" + bs_yn + '\'' +
                ", mintong='" + mintong + '\'' +
                ", st='" + st + '\'' +
                ", ars_yn='" + ars_yn + '\'' +
                ", job_viewer='" + job_viewer + '\'' +
                ", job_viewer_view='" + job_viewer_view + '\'' +
                ", driver_tel_num='" + driver_tel_num + '\'' +
                ", report_date='" + report_date + '\'' +
                ", trouble_high_cd_before='" + trouble_high_cd_before + '\'' +
                ", trouble_low_cd_before='" + trouble_low_cd_before + '\'' +
                ", unit_code_before='" + unit_code_before + '\'' +
                ", w_reg_date='" + w_reg_date + '\'' +
                ", w_care_time='" + w_care_time + '\'' +
                ", w_unit_code='" + w_unit_code + '\'' +
                ", w_trouble_high_cd='" + w_trouble_high_cd + '\'' +
                ", w_trouble_low_cd='" + w_trouble_low_cd + '\'' +
                ", w_transp_bizr_id='" + w_transp_bizr_id + '\'' +
                ", unit_code='" + unit_code + '\'' +
                ", ars_unit_code='" + ars_unit_code + '\'' +
                ", unit_name='" + unit_name + '\'' +
                '}';
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

    public String getDep_code() {
        return dep_code;
    }

    public void setDep_code(String dep_code) {
        this.dep_code = dep_code;
    }

    public String getReg_emp_id() {
        return reg_emp_id;
    }

    public void setReg_emp_id(String reg_emp_id) {
        this.reg_emp_id = reg_emp_id;
    }

    public String getInfra_code() {
        return infra_code;
    }

    public void setInfra_code(String infra_code) {
        this.infra_code = infra_code;
    }

    public String getTransp_bizr_id() {
        return transp_bizr_id;
    }

    public void setTransp_bizr_id(String transp_bizr_id) {
        this.transp_bizr_id = transp_bizr_id;
    }

    public String getGarage_id() {
        return garage_id;
    }

    public void setGarage_id(String garage_id) {
        this.garage_id = garage_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getTrouble_high_cd() {
        return trouble_high_cd;
    }

    public void setTrouble_high_cd(String trouble_high_cd) {
        this.trouble_high_cd = trouble_high_cd;
    }

    public String getTrouble_low_cd() {
        return trouble_low_cd;
    }

    public void setTrouble_low_cd(String trouble_low_cd) {
        this.trouble_low_cd = trouble_low_cd;
    }

    public String getTrouble_care_cd() {
        return trouble_care_cd;
    }

    public void setTrouble_care_cd(String trouble_care_cd) {
        this.trouble_care_cd = trouble_care_cd;
    }

    public String getArs_trouble_high_cd() {
        return ars_trouble_high_cd;
    }

    public void setArs_trouble_high_cd(String ars_trouble_high_cd) {
        this.ars_trouble_high_cd = ars_trouble_high_cd;
    }

    public String getArs_trouble_low_cd() {
        return ars_trouble_low_cd;
    }

    public void setArs_trouble_low_cd(String ars_trouble_low_cd) {
        this.ars_trouble_low_cd = ars_trouble_low_cd;
    }

    public String getUnit_change_yn() {
        return unit_change_yn;
    }

    public void setUnit_change_yn(String unit_change_yn) {
        this.unit_change_yn = unit_change_yn;
    }

    public String getUnit_before_id() {
        return unit_before_id;
    }

    public void setUnit_before_id(String unit_before_id) {
        this.unit_before_id = unit_before_id;
    }

    public String getUnit_after_id() {
        return unit_after_id;
    }

    public void setUnit_after_id(String unit_after_id) {
        this.unit_after_id = unit_after_id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getArs_notice() {
        return ars_notice;
    }

    public void setArs_notice(String ars_notice) {
        this.ars_notice = ars_notice;
    }

    public String getDirect_care() {
        return direct_care;
    }

    public void setDirect_care(String direct_care) {
        this.direct_care = direct_care;
    }

    public String getCare_emp_id() {
        return care_emp_id;
    }

    public void setCare_emp_id(String care_emp_id) {
        this.care_emp_id = care_emp_id;
    }

    public String getCare_date() {
        return care_date;
    }

    public void setCare_date(String care_date) {
        this.care_date = care_date;
    }

    public String getCare_time() {
        return care_time;
    }

    public void setCare_time(String care_time) {
        this.care_time = care_time;
    }

    public String getMove_distance() {
        return move_distance;
    }

    public void setMove_distance(String move_distance) {
        this.move_distance = move_distance;
    }

    public String getMove_time() {
        return move_time;
    }

    public void setMove_time(String move_time) {
        this.move_time = move_time;
    }

    public String getWait_time() {
        return wait_time;
    }

    public void setWait_time(String wait_time) {
        this.wait_time = wait_time;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getRestore_yn() {
        return restore_yn;
    }

    public void setRestore_yn(String restore_yn) {
        this.restore_yn = restore_yn;
    }

    public String getAnalysis_yn() {
        return analysis_yn;
    }

    public void setAnalysis_yn(String analysis_yn) {
        this.analysis_yn = analysis_yn;
    }

    public String getBs_yn() {
        return bs_yn;
    }

    public void setBs_yn(String bs_yn) {
        this.bs_yn = bs_yn;
    }

    public String getMintong() {
        return mintong;
    }

    public void setMintong(String mintong) {
        this.mintong = mintong;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getArs_yn() {
        return ars_yn;
    }

    public void setArs_yn(String ars_yn) {
        this.ars_yn = ars_yn;
    }

    public String getJob_viewer() {
        return job_viewer;
    }

    public void setJob_viewer(String job_viewer) {
        this.job_viewer = job_viewer;
    }

    public String getJob_viewer_view() {
        return job_viewer_view;
    }

    public void setJob_viewer_view(String job_viewer_view) {
        this.job_viewer_view = job_viewer_view;
    }

    public String getDriver_tel_num() {
        return driver_tel_num;
    }

    public void setDriver_tel_num(String driver_tel_num) {
        this.driver_tel_num = driver_tel_num;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public String getTrouble_high_cd_before() {
        return trouble_high_cd_before;
    }

    public void setTrouble_high_cd_before(String trouble_high_cd_before) {
        this.trouble_high_cd_before = trouble_high_cd_before;
    }

    public String getTrouble_low_cd_before() {
        return trouble_low_cd_before;
    }

    public void setTrouble_low_cd_before(String trouble_low_cd_before) {
        this.trouble_low_cd_before = trouble_low_cd_before;
    }

    public String getUnit_code_before() {
        return unit_code_before;
    }

    public void setUnit_code_before(String unit_code_before) {
        this.unit_code_before = unit_code_before;
    }

    public String getW_reg_date() {
        return w_reg_date;
    }

    public void setW_reg_date(String w_reg_date) {
        this.w_reg_date = w_reg_date;
    }

    public String getW_care_time() {
        return w_care_time;
    }

    public void setW_care_time(String w_care_time) {
        this.w_care_time = w_care_time;
    }

    public String getW_unit_code() {
        return w_unit_code;
    }

    public void setW_unit_code(String w_unit_code) {
        this.w_unit_code = w_unit_code;
    }

    public String getW_trouble_high_cd() {
        return w_trouble_high_cd;
    }

    public void setW_trouble_high_cd(String w_trouble_high_cd) {
        this.w_trouble_high_cd = w_trouble_high_cd;
    }

    public String getW_trouble_low_cd() {
        return w_trouble_low_cd;
    }

    public void setW_trouble_low_cd(String w_trouble_low_cd) {
        this.w_trouble_low_cd = w_trouble_low_cd;
    }

    public String getW_transp_bizr_id() {
        return w_transp_bizr_id;
    }

    public void setW_transp_bizr_id(String w_transp_bizr_id) {
        this.w_transp_bizr_id = w_transp_bizr_id;
    }

    public String getUnit_code() {
        return unit_code;
    }

    public void setUnit_code(String unit_code) {
        this.unit_code = unit_code;
    }

    public String getArs_unit_code() {
        return ars_unit_code;
    }

    public void setArs_unit_code(String ars_unit_code) {
        this.ars_unit_code = ars_unit_code;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
