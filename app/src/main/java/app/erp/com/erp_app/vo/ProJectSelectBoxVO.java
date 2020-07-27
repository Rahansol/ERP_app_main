package app.erp.com.erp_app.vo;

public class ProJectSelectBoxVO {

    public ProJectSelectBoxVO() {
    }

    private String emp_name;
    private String reg_emp_id;
    private String busoff_name;
    private String transp_bizr_id;

    @Override
    public String toString() {
        return "ProJectSelectBoxVO{" +
                "emp_name='" + emp_name + '\'' +
                ", reg_emp_id='" + reg_emp_id + '\'' +
                ", busoff_name='" + busoff_name + '\'' +
                ", transp_bizr_id='" + transp_bizr_id + '\'' +
                '}';
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getReg_emp_id() {
        return reg_emp_id;
    }

    public void setReg_emp_id(String reg_emp_id) {
        this.reg_emp_id = reg_emp_id;
    }

    public String getBusoff_name() {
        return busoff_name;
    }

    public void setBusoff_name(String busoff_name) {
        this.busoff_name = busoff_name;
    }

    public String getTransp_bizr_id() {
        return transp_bizr_id;
    }

    public void setTransp_bizr_id(String transp_bizr_id) {
        this.transp_bizr_id = transp_bizr_id;
    }
}
