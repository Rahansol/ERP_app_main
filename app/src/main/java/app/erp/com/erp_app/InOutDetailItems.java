package app.erp.com.erp_app;

public class InOutDetailItems {


    public String num;
    public String unit_ver;
    public String unit_id;
    public String unit_code;
    public String rep_unit_code;
    public String exe_type;

    /*public String reg_date;
    public String reg_time;
    public String emp_id;
    public String req_type;
*/

    public InOutDetailItems(String num, String unit_ver, String unit_id, String unit_code, String rep_unit_code, String exe_type) {
        this.num = num;
        this.unit_ver = unit_ver;
        this.unit_id = unit_id;
        this.unit_code = unit_code;
        this.rep_unit_code = rep_unit_code;
        this.exe_type = exe_type;
    }


}
