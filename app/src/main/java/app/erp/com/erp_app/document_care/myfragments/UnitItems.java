package app.erp.com.erp_app.document_care.myfragments;

import android.graphics.drawable.Drawable;

public class UnitItems {

    String line;
    int layoutBox;  //
    String text;
    String val;

    String text2;
    String val2;

    String dtti;
    String busNum;
    String routeNum;
    String empName;
    String unitItem1;
    String unitItem2;
    String unitItem3;
    String unitItem4;
    String unitItem5;
    String unitItem6;
    String unitItem7;
    String unitItem8;
    String unitItem9;
    String unitItem10;
    String unitItem11;
    String unitItem12;


    public UnitItems(String text, String val, String text2, String val2) {
        this.text = text;
        this.val = val;
        this.text2 = text2;
        this.val2 = val2;
    }

    public UnitItems(int layoutBox) {
        this.layoutBox = layoutBox;
    }

    public UnitItems(String line) {
        this.line = line;
    }


    public UnitItems() {
    }


    public UnitItems(String dtti, String busNum, String routeNum, String empName, String unitItem1, String unitItem2, String unitItem3, String unitItem4, String unitItem5, String unitItem6, String unitItem7, String unitItem8, String unitItem9, String unitItem10, String unitItem11, String unitItem12) {
        this.dtti = dtti;
        this.busNum = busNum;
        this.routeNum = routeNum;
        this.empName = empName;
        this.unitItem1 = unitItem1;
        this.unitItem2 = unitItem2;
        this.unitItem3 = unitItem3;
        this.unitItem4 = unitItem4;
        this.unitItem5 = unitItem5;
        this.unitItem6 = unitItem6;
        this.unitItem7 = unitItem7;
        this.unitItem8 = unitItem8;
        this.unitItem9 = unitItem9;
        this.unitItem10 = unitItem10;
        this.unitItem11 = unitItem11;
        this.unitItem12 = unitItem12;
    }



}
