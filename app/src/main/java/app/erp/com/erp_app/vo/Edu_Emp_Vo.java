package app.erp.com.erp_app.vo;

public class Edu_Emp_Vo {
    private String emp_id;
    private String dep_code;
    private String dep_name;
    private String emp_name;
    private boolean check_val;

    public Edu_Emp_Vo() {
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

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public boolean isCheck_val() {
        return check_val;
    }

    public void setCheck_val(boolean check_val) {
        this.check_val = check_val;
    }
}
