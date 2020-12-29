package com.alialfayed.pagination.java.viewModel;

import android.app.Application;

import com.alialfayed.pagination.java.repository.RetrofitRepository;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 12/29/2020 - 4:51 PM
 */
public class HomeViewModel extends AndroidViewModel {

    private RetrofitRepository retrofitRepository;
    private MutableLiveData<Object>topMoviesFirstPageResponse = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        retrofitRepository = RetrofitRepository.getInstance(application);
    }

    public void requestFirstPageTopMovies(int page ){
        retrofitRepository.loadPage(topMoviesFirstPageResponse , page);
    }

    public LiveData<Object> getFirstPageTopMovies(){
        return topMoviesFirstPageResponse;
    }

    public void requestFirstNextPageMovies(int page ){
        retrofitRepository.loadPage(topMoviesFirstPageResponse , page);
    }

    public LiveData<Object> getFirstNextTopMovies(){
        return topMoviesFirstPageResponse;
    }


}
