package app.erp.com.erp_app.document_care.myfragments;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/*아이템 클래스는 뷰를 가지고 있는게 아니라 "값"을 가지고있음*/

public class JobTextItems {
    String jobText;
    String busNum;
    Uri takePic;
    String tv;  //미리보기 -> X 로 텍스트값 변경...
    String preview;


    public JobTextItems() {
    }

    public JobTextItems(String jobText, String busNum, Uri takePic, String tv, String preview) {
        this.jobText = jobText;
        this.busNum = busNum;
        this.takePic = takePic;
        this.tv = tv;
        this.preview = preview;
    }



}
