package app.erp.com.erp_app.document_care.myfragments;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

import retrofit2.http.PUT;

public class G {
    public static int position;

    public static String dtti;
    public static String dtti2;
    public static String prjName;
    public static String transpBizrId; //운수사 고정번호
    public static String busoffName; //운수사명
    public static String TempBusId;  //차량번호  // == BUS_ID  //한글이 숫자로 변형된 차량번호
    public static String TempBusNum;   // 한글포함 차량번로
    public static String regEmpId;
    public static String garageId;
    public static String garageName;
    public static String routeId;
    public static String routeNum;
    public static String vehicleNum;
    public static String jopType;
    public static String Last_seq;

    //프로젝트 업무 화면에서 사용할 변수들..
    public static String PRJ_NAME;
    public static String TABLE_NAME;
    public static String BUSOFF_NAME;     // 운수사명
    public static String TRANSP_BIZR_ID; // 운수사 ID
    public static String GARAGE_NAME;
    public static String JOB_NAME;   //작업명
    public static String JOB_TYPE;   //작업타입
    public static String BUS_NUM_REG_DTTI;
    public static String BUS_ID;
    public static String BUS_NUM;
    public static String st_bus_list;  //차량넘버 ex)경기10아1234
    public static String st_bus_list_id;  //차량아이디 ex)141...

    public static String EMP_ID;   //msookim


    //차량 바코드 넘버, 일련번호
    public static String BARCODE;


    //프로젝트 업무
    //차량별 설치입력
    //myInstallSignFragments 패키지- fragments2 에서 사용
    public static String TRANSP_BIZR_ID_VAL;
    public static String GARAGE_NAME_VAL;
    public static String ROUTE_NUM_VAL;
    public static String JOB_TYPE_VAL;
    public static String BUS_NUM_VAL;


    public static Uri CAPTURED_IMAGE_URI;        // for 사진촬영
    public static String CAPTURED_IMAGE_PATH;    // for 사진촬영
    public static Bitmap CAPTURED_IMAGE_BITMAP;  // for 사진앨범

}
