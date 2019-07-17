package app.erp.com.erp_app.vo;

/**
 * Created by hsra on 2019-06-25.
 */

public class User_InfoVo {
    private String emp_id;
    private String emp_pw;
    private String emp_name;
    private String emp_birthday;
    private String emp_postnumber;
    private String emp_address;
    private String emp_address_old;
    private String join_date;
    private String resign_date;
    private String dep_code;
    private String dep_name;
    private String rank_code;
    private String bank_name;
    private String acc_number;
    private String email;
    private String imm_superior;
    private String tel_no;
    private String useyn;
    private String authority;
    private String app_token_id;

    //emp_call_list info
    private String call_num;
    private String call_ext_num;
    private String st_date;
    private String end_date;

    //전날 ST
    private int tot_st;
    private int tot_cnt;

    public User_InfoVo(String emp_id, String emp_pw, String emp_name, String emp_birthday, String emp_postnumber, String emp_address, String emp_address_old, String join_date, String resign_date, String dep_code, String dep_name, String rank_code, String bank_name, String acc_number, String email, String imm_superior, String tel_no, String useyn, String authority, String app_token_id, String call_num, String call_ext_num, String st_date, String end_date, int tot_st, int tot_cnt) {
        this.emp_id = emp_id;
        this.emp_pw = emp_pw;
        this.emp_name = emp_name;
        this.emp_birthday = emp_birthday;
        this.emp_postnumber = emp_postnumber;
        this.emp_address = emp_address;
        this.emp_address_old = emp_address_old;
        this.join_date = join_date;
        this.resign_date = resign_date;
        this.dep_code = dep_code;
        this.dep_name = dep_name;
        this.rank_code = rank_code;
        this.bank_name = bank_name;
        this.acc_number = acc_number;
        this.email = email;
        this.imm_superior = imm_superior;
        this.tel_no = tel_no;
        this.useyn = useyn;
        this.authority = authority;
        this.app_token_id = app_token_id;
        this.call_num = call_num;
        this.call_ext_num = call_ext_num;
        this.st_date = st_date;
        this.end_date = end_date;
        this.tot_st = tot_st;
        this.tot_cnt = tot_cnt;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_pw() {
        return emp_pw;
    }

    public void setEmp_pw(String emp_pw) {
        this.emp_pw = emp_pw;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_birthday() {
        return emp_birthday;
    }

    public void setEmp_birthday(String emp_birthday) {
        this.emp_birthday = emp_birthday;
    }

    public String getEmp_postnumber() {
        return emp_postnumber;
    }

    public void setEmp_postnumber(String emp_postnumber) {
        this.emp_postnumber = emp_postnumber;
    }

    public String getEmp_address() {
        return emp_address;
    }

    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    public String getEmp_address_old() {
        return emp_address_old;
    }

    public void setEmp_address_old(String emp_address_old) {
        this.emp_address_old = emp_address_old;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getResign_date() {
        return resign_date;
    }

    public void setResign_date(String resign_date) {
        this.resign_date = resign_date;
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

    public String getRank_code() {
        return rank_code;
    }

    public void setRank_code(String rank_code) {
        this.rank_code = rank_code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAcc_number() {
        return acc_number;
    }

    public void setAcc_number(String acc_number) {
        this.acc_number = acc_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImm_superior() {
        return imm_superior;
    }

    public void setImm_superior(String imm_superior) {
        this.imm_superior = imm_superior;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getUseyn() {
        return useyn;
    }

    public void setUseyn(String useyn) {
        this.useyn = useyn;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getApp_token_id() {
        return app_token_id;
    }

    public void setApp_token_id(String app_token_id) {
        this.app_token_id = app_token_id;
    }

    public String getCall_num() {
        return call_num;
    }

    public void setCall_num(String call_num) {
        this.call_num = call_num;
    }

    public String getCall_ext_num() {
        return call_ext_num;
    }

    public void setCall_ext_num(String call_ext_num) {
        this.call_ext_num = call_ext_num;
    }

    public String getSt_date() {
        return st_date;
    }

    public void setSt_date(String st_date) {
        this.st_date = st_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getTot_st() {
        return tot_st;
    }

    public void setTot_st(int tot_st) {
        this.tot_st = tot_st;
    }

    public int getTot_cnt() {
        return tot_cnt;
    }

    public void setTot_cnt(int tot_cnt) {
        this.tot_cnt = tot_cnt;
    }
}
