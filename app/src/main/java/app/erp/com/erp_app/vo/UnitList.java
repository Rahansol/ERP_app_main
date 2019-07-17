package app.erp.com.erp_app.vo;

/**
 * Created by hsra on 2019-06-24.
 * 단말기 바코드 리스트 레이아웃
 */

public class UnitList {

    private String index_num;
    private String area_code;
    private String area_name;
    private String unit_name;
    private String eb_barcode;

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

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getIndex_num() {
        return index_num;
    }

    public void setIndex_num(String index_num) {
        this.index_num = index_num;
    }

    public String getEb_barcode() {
        return eb_barcode;
    }

    public void setEb_barcode(String eb_barcode) {
        this.eb_barcode = eb_barcode;
    }
}
