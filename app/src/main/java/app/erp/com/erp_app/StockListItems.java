package app.erp.com.erp_app;

public class StockListItems {
    //recycler_stock_list.xml 의 items
    //순번, 분류, 제품명, 제품번호


    String rnum;
    String unit_ver;
    String unit_id;
    Boolean checkbox;





    public StockListItems() {
    }

    public StockListItems(String rnum, String unit_ver, String unit_id, Boolean checkbox) {
        this.rnum = rnum;
        this.unit_ver = unit_ver;
        this.unit_id = unit_id;
        this.checkbox = checkbox;
    }



}
