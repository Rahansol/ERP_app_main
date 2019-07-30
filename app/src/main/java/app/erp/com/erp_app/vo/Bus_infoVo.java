package app.erp.com.erp_app.vo;

/**
 * Created by hsra on 2019-06-21.
 */

public class Bus_infoVo {

    private String transp_bizr_id;
    private String busoff_bus ;
    private String bus_barcode ;
    private String bus_id ;
    private String bus_num ;
    private String vehicle_num;
    private String office_group;
    private String busoff_name;
    private String infra_code;
    private String r_num;
//    private String junk_date;

    public Bus_infoVo(String transp_bizr_id, String busoff_bus, String bus_barcode, String bus_id, String bus_num, String vehicle_num, String office_group, String busoff_name, String infra_code, String r_num) {
        this.transp_bizr_id = transp_bizr_id;
        this.busoff_bus = busoff_bus;
        this.bus_barcode = bus_barcode;
        this.bus_id = bus_id;
        this.bus_num = bus_num;
        this.vehicle_num = vehicle_num;
        this.office_group = office_group;
        this.busoff_name = busoff_name;
        this.infra_code = infra_code;
        this.r_num = r_num;
    }

    public String getTransp_bizr_id() {
        return transp_bizr_id;
    }

    public void setTransp_bizr_id(String transp_bizr_id) {
        this.transp_bizr_id = transp_bizr_id;
    }

    public String getBusoff_bus() {
        return busoff_bus;
    }

    public void setBusoff_bus(String busoff_bus) {
        this.busoff_bus = busoff_bus;
    }

    public String getBus_barcode() {
        return bus_barcode;
    }

    public void setBus_barcode(String bus_barcode) {
        this.bus_barcode = bus_barcode;
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

    public String getVehicle_num() {
        return vehicle_num;
    }

    public void setVehicle_num(String vehicle_num) {
        this.vehicle_num = vehicle_num;
    }

    public String getOffice_group() {
        return office_group;
    }

    public void setOffice_group(String office_group) {
        this.office_group = office_group;
    }

    public String getBusoff_name() {
        return busoff_name;
    }

    public void setBusoff_name(String busoff_name) {
        this.busoff_name = busoff_name;
    }

    public String getInfra_code() {
        return infra_code;
    }

    public void setInfra_code(String infra_code) {
        this.infra_code = infra_code;
    }

    public String getR_num() {
        return r_num;
    }

    public void setR_num(String r_num) {
        this.r_num = r_num;
    }
}
