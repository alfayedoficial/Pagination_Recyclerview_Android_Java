package com.alialfayed.pagination.java.api;

import com.alialfayed.pagination.java.model.ResponseTopMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.alialfayed.pagination.java.utils.Constants.MOVIE_TOP_RATED_API;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do : API Interface of Retrofit Client
 * Date 12/29/2020 - 4:53 PM
 */
public interface API {

    @GET(MOVIE_TOP_RATED_API)
    Call<ResponseTopMovies> getTopRatedMovies(@Query("api_key") String apiKey
            , @Query("language") String language
            , @Query("page") int pageIndex);


}
