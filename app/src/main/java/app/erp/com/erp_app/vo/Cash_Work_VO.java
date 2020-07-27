package app.erp.com.erp_app.vo;

public class Cash_Work_VO {
    private String first_address;
    private String last_address;
    private String add_no;

    private String transp_bizr_id;
    private String busoff_name;
    private String bus_list;
    private String bus_num;

    private String reg_date;
    private String emp_name;
    private String route_num;
    private String address;

    private String table_head;
    private String my_work;

    public Cash_Work_VO() {
    }

    @Override
    public String toString() {
        return "Cash_Work_VO{" +
                "first_address='" + first_address + '\'' +
                ", last_address='" + last_address + '\'' +
                ", add_no='" + add_no + '\'' +
                ", transp_bizr_id='" + transp_bizr_id + '\'' +
                ", busoff_name='" + busoff_name + '\'' +
                ", bus_list='" + bus_list + '\'' +
                ", bus_num='" + bus_num + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", emp_name='" + emp_name + '\'' +
                ", route_num='" + route_num + '\'' +
                ", address='" + address + '\'' +
                ", table_head='" + table_head + '\'' +
                ", my_work='" + my_work + '\'' +
                '}';
    }

    public String getFirst_address() {
        return first_address;
    }

    public void setFirst_address(String first_address) {
        this.first_address = first_address;
    }

    public String getLast_address() {
        return last_address;
    }

    public void setLast_address(String last_address) {
        this.last_address = last_address;
    }

    public String getAdd_no() {
        return add_no;
    }

    public void setAdd_no(String add_no) {
        this.add_no = add_no;
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

    public String getBus_list() {
        return bus_list;
    }

    public void setBus_list(String bus_list) {
        this.bus_list = bus_list;
    }

    public String getBus_num() {
        return bus_num;
    }

    public void setBus_num(String bus_num) {
        this.bus_num = bus_num;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getRoute_num() {
        return route_num;
    }

    public void setRoute_num(String route_num) {
        this.route_num = route_num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTable_head() {
        return table_head;
    }

    public void setTable_head(String table_head) {
        this.table_head = table_head;
    }

    public String getMy_work() {
        return my_work;
    }

    public void setMy_work(String my_work) {
        this.my_work = my_work;
    }
}
