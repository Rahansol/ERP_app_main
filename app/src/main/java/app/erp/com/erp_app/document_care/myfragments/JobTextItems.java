package app.erp.com.erp_app.document_care.myfragments;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/*아이템 클래스는 뷰를 가지고 있는게 아니라 "값"을 가지고있음*/

public class JobTextItems {
    String jobText;
    String busNum;
    Uri preview_uri;
    String tv;
    String tv_p;
    String preview;




    public JobTextItems() {
    }


    public JobTextItems(String jobText, String busNum, Uri takePic, String tv, String tv_p, String preview) {
        this.jobText = jobText;
        this.busNum = busNum;
        this.preview_uri = takePic;
        this.tv = tv;
        this.tv_p = tv_p;
        this.preview = preview;
    }
}



