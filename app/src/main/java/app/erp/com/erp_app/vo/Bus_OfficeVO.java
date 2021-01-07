package app.erp.com.erp_app.vo;

public class Bus_OfficeVO {

    private String office_group;
    private String area;
    private String transp_bizr_id;
    private String busoff_name;
    private String busoff_addr;
    private String busoff_tel;
    private String route_id;
    private String route_num;
    private String rnum;
    private String garage_name;
    private String garage_id;
    private String unit_ver;

    @Override
    public String toString() {
        return "Bus_OfficeVO{" +
                "office_group='" + office_group + '\'' +
                ", area='" + area + '\'' +
                ", transp_bizr_id='" + transp_bizr_id + '\'' +
                ", busoff_name='" + busoff_name + '\'' +
                ", busoff_addr='" + busoff_addr + '\'' +
                ", busoff_tel='" + busoff_tel + '\'' +
                ", route_id='" + route_id + '\'' +
                ", route_num='" + route_num + '\'' +
                ", rnum='" + rnum + '\'' +
                ", garage_name='" + garage_name + '\'' +
                ", garage_id='" + garage_id + '\'' +
                ", unit_ver='" + unit_ver + '\'' +
                '}';
    }



    public String getUnit_ver() {
        return unit_ver;
    }

    public void setUnit_ver(String unit_ver) {
        this.unit_ver = unit_ver;
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


    public String getGarage_name() {
        return garage_name;
    }

    public void setGarage_name(String garage_name) {
        this.garage_name = garage_name;
    }

    public String getGarage_id() {
        return garage_id;
    }

    public void setGarage_id(String garage_id) {
        this.garage_id = garage_id;
    }



    public String getOffice_group() {
        return office_group;
    }

    public void setOffice_group(String office_group) {
        this.office_group = office_group;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getBusoff_addr() {
        return busoff_addr;
    }

    public void setBusoff_addr(String busoff_addr) {
        this.busoff_addr = busoff_addr;
    }

    public String getBusoff_tel() {
        return busoff_tel;
    }

    public void setBusoff_tel(String busoff_tel) {
        this.busoff_tel = busoff_tel;
    }

    public String getRnum() {
        return rnum;
    }

    public void setRnum(String rnum) {
        this.rnum = rnum;
    }


}
