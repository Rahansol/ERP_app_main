package app.erp.com.erp_app.vo;

import java.io.Serializable;

public class ProJectVO implements Serializable {

    public ProJectVO() {
    }

    private String area_code;
    private String area_name;
    private String sub_area_code;
    private String sub_area_name;

    private String status_code;
    private String status_name;

    private String infra_code;
    private String infra_name;
    private String maintenance_yn;

    private String base_infra_code;
    private String base_infra_name;

    private String seq;
    private String column_name;
    private String column_note;
    private String column_type;
    private String column_len;
    private String primary_yn;

    private String prj_seq;
    private String prj_name;
    private String prj_notice;
    private String st_date;
    private String ed_date;
    private String reg_date;
    private String reg_time;
    private String reg_name;

    private String create_sql;

    private String item_seq;
    private String item_name;
    private String item_type;
    private String select_cnt;
    private String use_yn;
    private String emp_name;

    private String select_seq;

    private String office_group;
    private String transp_bizr_id;
    private String busoff_name;
    private String bus_id;
    private String bus_num;

    private String route_id;
    private String route_num;

    private String job_text;

    private String reg_emp_id;
    private String text;
    private String contents;

    private String garage_id;
    private String garage_name;
    private String garage_address;

    private String page;

    private String doc_seq;
    private String item_group_seq;
    private String item_gruop_name;
    private String item_each_seq;
    private String item_each_name;

    private String reg_dtti;

    private String job_type;
    private String job_name;

    private String write_dtti;
    private String write_date;
    private String sign_date;
    private String sign_man;
    private String car_cnt;

    private String doc_name;
    private String sign_dtti;
    private String sign_tel;
    private String official_type;
    private String sign_image;

    @Override
    public String toString() {
        return "ProJectVO{" +
                "area_code='" + area_code + '\'' +
                ", area_name='" + area_name + '\'' +
                ", sub_area_code='" + sub_area_code + '\'' +
                ", sub_area_name='" + sub_area_name + '\'' +
                ", status_code='" + status_code + '\'' +
                ", status_name='" + status_name + '\'' +
                ", infra_code='" + infra_code + '\'' +
                ", infra_name='" + infra_name + '\'' +
                ", maintenance_yn='" + maintenance_yn + '\'' +
                ", base_infra_code='" + base_infra_code + '\'' +
                ", base_infra_name='" + base_infra_name + '\'' +
                ", seq='" + seq + '\'' +
                ", column_name='" + column_name + '\'' +
                ", column_note='" + column_note + '\'' +
                ", column_type='" + column_type + '\'' +
                ", column_len='" + column_len + '\'' +
                ", primary_yn='" + primary_yn + '\'' +
                ", prj_seq='" + prj_seq + '\'' +
                ", prj_name='" + prj_name + '\'' +
                ", prj_notice='" + prj_notice + '\'' +
                ", st_date='" + st_date + '\'' +
                ", ed_date='" + ed_date + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", reg_name='" + reg_name + '\'' +
                ", create_sql='" + create_sql + '\'' +
                ", item_seq='" + item_seq + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_type='" + item_type + '\'' +
                ", select_cnt='" + select_cnt + '\'' +
                ", use_yn='" + use_yn + '\'' +
                ", emp_name='" + emp_name + '\'' +
                ", select_seq='" + select_seq + '\'' +
                ", office_group='" + office_group + '\'' +
                ", transp_bizr_id='" + transp_bizr_id + '\'' +
                ", busoff_name='" + busoff_name + '\'' +
                ", bus_id='" + bus_id + '\'' +
                ", bus_num='" + bus_num + '\'' +
                ", route_id='" + route_id + '\'' +
                ", route_num='" + route_num + '\'' +
                ", job_text='" + job_text + '\'' +
                ", reg_emp_id='" + reg_emp_id + '\'' +
                ", text='" + text + '\'' +
                ", contents='" + contents + '\'' +
                ", garage_id='" + garage_id + '\'' +
                ", garage_name='" + garage_name + '\'' +
                ", garage_address='" + garage_address + '\'' +
                ", page='" + page + '\'' +
                ", doc_seq='" + doc_seq + '\'' +
                ", item_group_seq='" + item_group_seq + '\'' +
                ", item_gruop_name='" + item_gruop_name + '\'' +
                ", item_each_seq='" + item_each_seq + '\'' +
                ", item_each_name='" + item_each_name + '\'' +
                ", reg_dtti='" + reg_dtti + '\'' +
                ", job_type='" + job_type + '\'' +
                ", job_name='" + job_name + '\'' +
                ", write_dtti='" + write_dtti + '\'' +
                ", write_date='" + write_date + '\'' +
                ", sign_date='" + sign_date + '\'' +
                ", sign_man='" + sign_man + '\'' +
                ", car_cnt='" + car_cnt + '\'' +
                ", doc_name='" + doc_name + '\'' +
                ", sign_dtti='" + sign_dtti + '\'' +
                ", sign_tel='" + sign_tel + '\'' +
                ", official_type='" + official_type + '\'' +
                ", sign_image='" + sign_image + '\'' +
                '}';
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getSub_area_code() {
        return sub_area_code;
    }

    public void setSub_area_code(String sub_area_code) {
        this.sub_area_code = sub_area_code;
    }

    public String getSub_area_name() {
        return sub_area_name;
    }

    public void setSub_area_name(String sub_area_name) {
        this.sub_area_name = sub_area_name;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getInfra_code() {
        return infra_code;
    }

    public void setInfra_code(String infra_code) {
        this.infra_code = infra_code;
    }

    public String getInfra_name() {
        return infra_name;
    }

    public void setInfra_name(String infra_name) {
        this.infra_name = infra_name;
    }

    public String getMaintenance_yn() {
        return maintenance_yn;
    }

    public void setMaintenance_yn(String maintenance_yn) {
        this.maintenance_yn = maintenance_yn;
    }

    public String getBase_infra_code() {
        return base_infra_code;
    }

    public void setBase_infra_code(String base_infra_code) {
        this.base_infra_code = base_infra_code;
    }

    public String getBase_infra_name() {
        return base_infra_name;
    }

    public void setBase_infra_name(String base_infra_name) {
        this.base_infra_name = base_infra_name;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getColumn_note() {
        return column_note;
    }

    public void setColumn_note(String column_note) {
        this.column_note = column_note;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public String getColumn_len() {
        return column_len;
    }

    public void setColumn_len(String column_len) {
        this.column_len = column_len;
    }

    public String getPrimary_yn() {
        return primary_yn;
    }

    public void setPrimary_yn(String primary_yn) {
        this.primary_yn = primary_yn;
    }

    public String getPrj_seq() {
        return prj_seq;
    }

    public void setPrj_seq(String prj_seq) {
        this.prj_seq = prj_seq;
    }

    public String getPrj_name() {
        return prj_name;
    }

    public void setPrj_name(String prj_name) {
        this.prj_name = prj_name;
    }

    public String getPrj_notice() {
        return prj_notice;
    }

    public void setPrj_notice(String prj_notice) {
        this.prj_notice = prj_notice;
    }

    public String getSt_date() {
        return st_date;
    }

    public void setSt_date(String st_date) {
        this.st_date = st_date;
    }

    public String getEd_date() {
        return ed_date;
    }

    public void setEd_date(String ed_date) {
        this.ed_date = ed_date;
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

    public String getReg_name() {
        return reg_name;
    }

    public void setReg_name(String reg_name) {
        this.reg_name = reg_name;
    }

    public String getCreate_sql() {
        return create_sql;
    }

    public void setCreate_sql(String create_sql) {
        this.create_sql = create_sql;
    }

    public String getItem_seq() {
        return item_seq;
    }

    public void setItem_seq(String item_seq) {
        this.item_seq = item_seq;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getSelect_cnt() {
        return select_cnt;
    }

    public void setSelect_cnt(String select_cnt) {
        this.select_cnt = select_cnt;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getSelect_seq() {
        return select_seq;
    }

    public void setSelect_seq(String select_seq) {
        this.select_seq = select_seq;
    }

    public String getOffice_group() {
        return office_group;
    }

    public void setOffice_group(String office_group) {
        this.office_group = office_group;
    }

    public String getTransp_bizr_id() {
        return transp_bizr_id;
    }

    public void setTransp_bizr_id(String transp_bizr_id) {
        this.transp_bizr_id = transp_bizr_id;
    }

    public String getBusoff_name() {
        return busoff_name;
    }

    public void setBusoff_name(String busoff_name) {
        this.busoff_name = busoff_name;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getBus_num() {
        return bus_num;
    }

    public void setBus_num(String bus_num) {
        this.bus_num = bus_num;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getRoute_num() {
        return route_num;
    }

    public void setRoute_num(String route_num) {
        this.route_num = route_num;
    }

    public String getJob_text() {
        return job_text;
    }

    public void setJob_text(String job_text) {
        this.job_text = job_text;
    }

    public String getReg_emp_id() {
        return reg_emp_id;
    }

    public void setReg_emp_id(String reg_emp_id) {
        this.reg_emp_id = reg_emp_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getGarage_id() {
        return garage_id;
    }

    public void setGarage_id(String garage_id) {
        this.garage_id = garage_id;
    }

    public String getGarage_name() {
        return garage_name;
    }

    public void setGarage_name(String garage_name) {
        this.garage_name = garage_name;
    }

    public String getGarage_address() {
        return garage_address;
    }

    public void setGarage_address(String garage_address) {
        this.garage_address = garage_address;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getDoc_seq() {
        return doc_seq;
    }

    public void setDoc_seq(String doc_seq) {
        this.doc_seq = doc_seq;
    }

    public String getItem_group_seq() {
        return item_group_seq;
    }

    public void setItem_group_seq(String item_group_seq) {
        this.item_group_seq = item_group_seq;
    }

    public String getItem_gruop_name() {
        return item_gruop_name;
    }

    public void setItem_gruop_name(String item_gruop_name) {
        this.item_gruop_name = item_gruop_name;
    }

    public String getItem_each_seq() {
        return item_each_seq;
    }

    public void setItem_each_seq(String item_each_seq) {
        this.item_each_seq = item_each_seq;
    }

    public String getItem_each_name() {
        return item_each_name;
    }

    public void setItem_each_name(String item_each_name) {
        this.item_each_name = item_each_name;
    }

    public String getReg_dtti() {
        return reg_dtti;
    }

    public void setReg_dtti(String reg_dtti) {
        this.reg_dtti = reg_dtti;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getWrite_dtti() {
        return write_dtti;
    }

    public void setWrite_dtti(String write_dtti) {
        this.write_dtti = write_dtti;
    }

    public String getWrite_date() {
        return write_date;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }

    public String getSign_date() {
        return sign_date;
    }

    public void setSign_date(String sign_date) {
        this.sign_date = sign_date;
    }

    public String getSign_man() {
        return sign_man;
    }

    public void setSign_man(String sign_man) {
        this.sign_man = sign_man;
    }

    public String getCar_cnt() {
        return car_cnt;
    }

    public void setCar_cnt(String car_cnt) {
        this.car_cnt = car_cnt;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getSign_dtti() {
        return sign_dtti;
    }

    public void setSign_dtti(String sign_dtti) {
        this.sign_dtti = sign_dtti;
    }

    public String getSign_tel() {
        return sign_tel;
    }

    public void setSign_tel(String sign_tel) {
        this.sign_tel = sign_tel;
    }

    public String getOfficial_type() {
        return official_type;
    }

    public void setOfficial_type(String official_type) {
        this.official_type = official_type;
    }

    public String getSign_image() {
        return sign_image;
    }

    public void setSign_image(String sign_image) {
        this.sign_image = sign_image;
    }
}