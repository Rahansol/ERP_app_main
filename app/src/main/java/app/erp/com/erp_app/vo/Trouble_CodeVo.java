package app.erp.com.erp_app.vo;

/**
 * Created by hsra on 2019-07-01.
 */

public class Trouble_CodeVo {

    public Trouble_CodeVo() {
    }

    private String trouble_high_name;
    private String trouble_high_cd;
    private String trouble_low_cd;
    private String trouble_low_name;

    private String unit_code;
    private String unit_name;

    private String trouble_care_cd;
    private String trouble_care_name;

    private String infra_code;
    private String service_id;
    private String useyn;

    @Override
    public String toString() {
        return "Trouble_CodeVo{" +
                "trouble_high_name='" + trouble_high_name + '\'' +
                ", trouble_high_cd='" + trouble_high_cd + '\'' +
                ", trouble_low_cd='" + trouble_low_cd + '\'' +
                ", trouble_low_name='" + trouble_low_name + '\'' +
                ", unit_code='" + unit_code + '\'' +
                ", unit_name='" + unit_name + '\'' +
                ", trouble_care_cd='" + trouble_care_cd + '\'' +
                ", trouble_care_name='" + trouble_care_name + '\'' +
                ", infra_code='" + infra_code + '\'' +
                ", service_id='" + service_id + '\'' +
                ", useyn='" + useyn + '\'' +
                '}';
    }

    public String getTrouble_high_name() {
        return trouble_high_name;
    }

    public void setTrouble_high_name(String trouble_high_name) {
        this.trouble_high_name = trouble_high_name;
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

    public String getTrouble_low_name() {
        return trouble_low_name;
    }

    public void setTrouble_low_name(String trouble_low_name) {
        this.trouble_low_name = trouble_low_name;
    }

    public String getUnit_code() {
        return unit_code;
    }

    public void setUnit_code(String unit_code) {
        this.unit_code = unit_code;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getTrouble_care_cd() {
        return trouble_care_cd;
    }

    public void setTrouble_care_cd(String trouble_care_cd) {
        this.trouble_care_cd = trouble_care_cd;
    }

    public String getTrouble_care_name() {
        return trouble_care_name;
    }

    public void setTrouble_care_name(String trouble_care_name) {
        this.trouble_care_name = trouble_care_name;
    }

    public String getInfra_code() {
        return infra_code;
    }

    public void setInfra_code(String infra_code) {
        this.infra_code = infra_code;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getUseyn() {
        return useyn;
    }

    public void setUseyn(String useyn) {
        this.useyn = useyn;
    }
}
