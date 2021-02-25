package app.erp.com.erp_app.document_care.myfragments;

import android.widget.ImageView;
import android.widget.TextView;

public class TranspBizrItems {
    Boolean check;      //체크박스
    String jobName;     //작업
    String busOffName;  //운수사명
    String garageName;  //영업소명
    String routeNum;    //노선
    String busNum;     //차량번호
    String docDtti;   //확인서- "완료"
    String tv_reg_dtti; //등록시간
    String busId;  //차대번호??
    String tableName;

    public TranspBizrItems() {
    }

    public TranspBizrItems(Boolean check, String jobName, String busOffName, String garageName, String routeNum, String busNum, String docDtti, String tv_reg_dtti, String busId, String tableName) {
        this.check = check;
        this.jobName = jobName;
        this.busOffName = busOffName;
        this.garageName = garageName;
        this.routeNum = routeNum;
        this.busNum = busNum;
        this.docDtti = docDtti;
        this.tv_reg_dtti= tv_reg_dtti;
        this.busId= busId;
        this.tableName= tableName;
    }

}
