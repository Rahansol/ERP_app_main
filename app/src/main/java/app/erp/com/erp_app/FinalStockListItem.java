package app.erp.com.erp_app;

public class FinalStockListItem {
    //최종 선택목록 확인 아이템


    public String unit_ver;
    public String unit_id;
    public String unit_code;
    public String rep_unit_code;

    public FinalStockListItem() {
    }


    public FinalStockListItem(String unit_ver, String unit_id, String unit_code, String rep_unit_code) {
        this.unit_ver = unit_ver;
        this.unit_id = unit_id;
        this.unit_code = unit_code;
        this.rep_unit_code = rep_unit_code;
    }
}
