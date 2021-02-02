package app.erp.com.erp_app.document_care.myfragments;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface RetrofitService {

    @Multipart
    @POST("Retrofit/")
    Call<String> uploadFile(@Part MultipartBody.Part filePart);
}
