package com.alialfayed.pagination.java.api;

import android.annotation.SuppressLint;
import android.content.Context;

import com.alialfayed.pagination.java.utils.CheckValidation;
import com.alialfayed.pagination.java.utils.Constants;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do : Create an Singleton Object API Client and get instance of retrofit object
 * Date 5/12/2020 - 3:04 AM
 */
@SuppressLint("StaticFieldLeak")
public class ApiClient {

    private static ApiClient apiClientInstance;
    private static Retrofit retrofitInstance;
    private final  long cashSize=(5 * 1024 * 1024);
    private Context context=null;
    private Cache cashe;

    private ApiClient(Context context) {
        this.context=context;
        this.cashe=new Cache(context.getCacheDir(),cashSize);
    }

    public static ApiClient getApiClientInstance(Context context) {
        if (apiClientInstance == null) {
            apiClientInstance = new ApiClient(context);
        }
        return apiClientInstance;
    }

    public Retrofit getRetrofitClient() {
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .cache(cashe)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request requst = chain.request();
                        if(CheckValidation.isConnected(context)){
                            requst=requst.newBuilder().header("Cache-Control", "public, max-age=" + 5).build();
                        }else {
                            requst = requst.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(requst);
                    }
                })
                .build();
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_API)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

}
