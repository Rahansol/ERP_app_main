package app.erp.com.erp_app.document_care.myfragments;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitHelper {

    public static Retrofit newRetrofit(){
        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl("http://192.168.0.122:8180/controller/");
        builder.addConverterFactory(ScalarsConverterFactory.create());
        return builder.build();
    }
}
