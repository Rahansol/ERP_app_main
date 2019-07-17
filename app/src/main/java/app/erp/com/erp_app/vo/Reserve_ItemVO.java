package app.erp.com.erp_app.vo;

import java.util.ArrayList;

/**
 * Created by hsra on 2019-06-27.
 */

public class Reserve_ItemVO {

    private String index_num;
    private String area_name;
    private String unit_name;
    private ArrayList<String> bus_num;
    private String unit_barcde;
    private ArrayList<String> trouble_high_code;
    private ArrayList<String> trouble_low_code;
    private String notice;

    public String getIndex_num() {
        return index_num;
    }

    public void setIndex_num(String index_num) {
        this.index_num = index_num;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public ArrayList<String> getBus_num() {
        return bus_num;
    }

    public void setBus_num(ArrayList<String> bus_num) {
        this.bus_num = bus_num;
    }

    public String getUnit_barcde() {
        return unit_barcde;
    }

    public void setUnit_barcde(String unit_barcde) {
        this.unit_barcde = unit_barcde;
    }

    public ArrayList<String> getTrouble_high_code() {
        return trouble_high_code;
    }

    public void setTrouble_high_code(ArrayList<String> trouble_high_code) {
        this.trouble_high_code = trouble_high_code;
    }

    public ArrayList<String> getTrouble_low_code() {
        return trouble_low_code;
    }

    public void setTrouble_low_code(ArrayList<String> trouble_low_code) {
        this.trouble_low_code = trouble_low_code;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}


