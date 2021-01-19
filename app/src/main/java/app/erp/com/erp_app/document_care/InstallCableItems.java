package app.erp.com.erp_app.document_care;

import android.graphics.drawable.Icon;

public class InstallCableItems {
    String item;
    String item_detail;
    String tv_quantity_recycler;
    String btn_delete;

    public InstallCableItems() {
    }


    public InstallCableItems(String item, String item_detail, String tv_quantity_recycler, String btn_delete) {
        this.item = item;
        this.item_detail = item_detail;
        this.tv_quantity_recycler= tv_quantity_recycler;
        this.btn_delete = btn_delete;
    }
}
