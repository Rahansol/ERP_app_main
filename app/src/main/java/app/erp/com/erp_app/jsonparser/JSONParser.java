package app.erp.com.erp_app.jsonparser;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.erp.com.erp_app.vo.Image_Path_VO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Pratik Butani
 */
public class JSONParser {

    /**
     * Upload URL of your folder with php file name...
     * You will find this file in php_upload folder in this project
     * You can copy that folder and paste in your htdocs folder...
     */
  //  private static final String URL_UPLOAD_IMAGE = "http://ierp.interpass.co.kr/controller/barcode/app_ftp_upload.do";
    private static final String URL_UPLOAD_IMAGE = "http://192.168.0.122:8180/controller/erp_project/app_sign_ftp_upload.do";
    private static final String URL_SIGN_UPLOAD_IMAGE = "http://192.168.0.122:8180/controller/erp_project/app_sign_ftp_upload.do";
    //private static final String URL_SIGN_UPLOAD_IMAGE = "http://ierp.interpass.co.kr/controller/erp_project/app_sign_ftp_upload.do";

    private static File sourceFile;
    /**
     * Upload Image
     *
     * @param sourceImageFile
     * @return
     */
    public static Boolean uploadImage(Map<String , Object> sourceImageFile) {

        boolean reuslt = false;
        Log.d("sourceImageFile",""+sourceImageFile.size());

        try {

            StringBuilder sb = new StringBuilder();
            Set<?> set = sourceImageFile.keySet();
            Iterator<?> it = set.iterator();
            String filename = "";
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            while(it.hasNext()){
                String key = (String)it.next();
                if(key != null){

                    String file_name_and_path = (String)sourceImageFile.get(key);
                    String[] name_path_arry = file_name_and_path.split("&");
                    sourceFile = new File(name_path_arry[0]);
                    filename = name_path_arry[1];
//                    filename = (String)((String) sourceImageFile.get(key)).substring(sourceImageFile.lastIndexOf("/")+1).substring();
//                    sb.append("------------------------------------------------------------\n");
//                    sb.append("key = "+key+",\t\t\tvalue = "+sourceImageFile.get(key)+"\n");
//                    Log.i("values info : " , ""+sb);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                            .addFormDataPart("result", "my_image")
                            .build();

                    Request request = new Request.Builder()
                            .url(URL_UPLOAD_IMAGE)
                            .post(requestBody)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    String res = response.body().string();
                }
            }
            reuslt = true;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            reuslt = false;
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            reuslt = false;
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return reuslt;
    }

    public static Boolean sign_uploadImage(Map<String , Object> sourceImageFile) {

        boolean reuslt = false;
        Log.d("sourceImageFile",""+sourceImageFile.size());

        try {

            String filename = "";

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            String file_name_and_path = (String)sourceImageFile.get("sign");
            String[] name_path_arry = file_name_and_path.split("&");

            sourceFile = new File(name_path_arry[0]);
            filename = name_path_arry[1];

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("result", "my_image")
                    .build();

            Request request = new Request.Builder()
                    .url(URL_SIGN_UPLOAD_IMAGE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            reuslt = true;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            reuslt = false;
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            reuslt = false;
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return reuslt;
    }
}
