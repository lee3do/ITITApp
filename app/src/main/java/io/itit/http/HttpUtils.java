package io.itit.http;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {
    public static Retrofit retrofit;
    public static AppApis appApis;
    public static String baseUrl = "http://itit.io:7001/srv/feed/view/user/";
    public static  String iconUrl = "http://itit.io:7001/srv/feed/favicon/";

    static {
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(chain -> {
            okhttp3.Response orginalResponse = chain.proceed(chain.request());
            return orginalResponse.newBuilder().build();
        }).build();
        retrofit = new Retrofit.Builder().baseUrl("http://itit.io:7001/").client(client)
                .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory
                        (RxJavaCallAdapterFactory.create()).build();
        appApis = retrofit.create(AppApis.class);
    }

    public static String httpGet(String url) throws Exception {
        Call<ResponseBody> call = appApis.httpGet(url);
        Response<ResponseBody> requestBody = call.execute();
        return requestBody.body().string();
    }
}
