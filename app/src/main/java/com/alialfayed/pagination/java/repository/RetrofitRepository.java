package com.alialfayed.pagination.java.repository;

import android.app.Application;

import com.alialfayed.pagination.java.api.API;
import com.alialfayed.pagination.java.api.ApiClient;
import com.alialfayed.pagination.java.model.ResponseTopMovies;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alialfayed.pagination.java.utils.Constants.API_KEY;
import static com.alialfayed.pagination.java.utils.Constants.APP_LANGUAGE;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 12/29/2020 - 4:51 PM
 */
public class RetrofitRepository {

    private API instanceApi;
    private static RetrofitRepository retrofitRepository;
    private Application application;

    private RetrofitRepository(Application application) {
        this.application = application;
        instanceApi = ApiClient
                .getApiClientInstance(application.getApplicationContext())
                .getRetrofitClient()
                .create(API.class);
    }

    public static RetrofitRepository getInstance(Application app){
          if (retrofitRepository == null){
              retrofitRepository = new RetrofitRepository(app);
          }
          return  retrofitRepository;
    }

    public void loadPage(MutableLiveData<Object> topMoviesResponse, int page){
        instanceApi.getTopRatedMovies(API_KEY,APP_LANGUAGE , page).enqueue(new Callback<ResponseTopMovies>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTopMovies> call,@NonNull Response<ResponseTopMovies> response) {

                if (response.isSuccessful()){
                    topMoviesResponse.setValue(response.body());
                }else{
                    topMoviesResponse.setValue("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseTopMovies> call,@NonNull Throwable t) {
                topMoviesResponse.setValue(t);
            }
        });
    }
}
