package app.erp.com.erp_app.document_care;

public class InstallCableItems {
    String num;
    String item;
    String item_detail;
    String quantity;
    int minus;
    int plus;

    public InstallCableItems() {
    }


    public InstallCableItems(String num, String item, String item_detail, String quantity, int minus, int plus) {
        this.num = num;
        this.item = item;
        this.item_detail = item_detail;
        this.quantity = quantity;
        this.minus = minus;
        this.plus = plus;
    }
}
