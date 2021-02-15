package app.erp.com.erp_app.document_care.myfragments;

import android.widget.ImageView;
import android.widget.TextView;

public class TranspBizrItems {
    Boolean check;
    String jobName;
    String busOffName;
    String garageName;
    String routeNum;
    String busNum;
    String sign;
    String tv_reg_dtti;
    String tv_bus_id;

    public TranspBizrItems() {
    }

    public TranspBizrItems(Boolean check, String jobName, String busOffName, String garageName, String routeNum, String busNum, String sign, String tv_reg_dtti, String tv_bus_id) {
        this.check = check;
        this.jobName = jobName;
        this.busOffName = busOffName;
        this.garageName = garageName;
        this.routeNum = routeNum;
        this.busNum = busNum;
        this.sign = sign;
        this.tv_reg_dtti= tv_reg_dtti;
        this.tv_bus_id= tv_bus_id;
    }

}
