package app.erp.com.erp_app.document_care.myInstallSignFragments;

public class TranspBizrItems2 {
    Boolean check;      //체크박스
    String jobName;     //작업
    String jobType;
    String busOffName;  //운수사명
    String transpBizrId; //운수사ID
    String garageName;  //영업소명
    String routeNum;    //노선
    String busNum;     //차량번호
    String docDtti;   //확인서- "완료"
    String tv_reg_dtti; //등록시간
    String busId;  //차대번호??
    String tableName;

    public TranspBizrItems2() {
    }

    public TranspBizrItems2(Boolean check, String jobName, String jobType, String busOffName,String transpBizrId, String garageName, String routeNum, String busNum, String docDtti, String tv_reg_dtti, String busId, String tableName) {
        this.check = check;
        this.jobName = jobName;
        this.jobType= jobType;
        this.busOffName = busOffName;
        this.transpBizrId= transpBizrId;
        this.garageName = garageName;
        this.routeNum = routeNum;
        this.busNum = busNum;
        this.docDtti = docDtti;
        this.tv_reg_dtti= tv_reg_dtti;
        this.busId= busId;
        this.tableName= tableName;
    }
}