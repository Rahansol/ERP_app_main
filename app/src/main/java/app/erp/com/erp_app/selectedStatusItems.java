package app.erp.com.erp_app;

public class selectedStatusItems {
    String unitName;
    String unitTotalNum;
    /*int unitSelectedNum;*/

    //선택현황 아이템을 클릭하면 재고리스트에 보여지는 데이터....
    //전달할 데이터 추가해주기...
    String unitCode;
    String repUnitCode;

    /*String unYn;
    String inYn;
    String empId;
    String barcode_dep*/

    public selectedStatusItems() {
    }


    public selectedStatusItems(String unitName, String unitTotalNum, String unitCode, String repUnitCode) {
        this.unitName = unitName;
        this.unitTotalNum = unitTotalNum;
        /*this.unitSelectedNum = unitSelectedNum;*/

        this.unitCode = unitCode;
        this.repUnitCode = repUnitCode;
    }
}
