package com.alialfayed.pagination.java.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.alialfayed.pagination.java.R;
import com.alialfayed.pagination.java.databinding.ActivityHomeBinding;
import com.alialfayed.pagination.java.model.ResponseTopMovies;
import com.alialfayed.pagination.java.model.ResultsItem;
import com.alialfayed.pagination.java.utils.CheckValidation;
import com.alialfayed.pagination.java.utils.MessageLog;
import com.alialfayed.pagination.java.utils.PaginationScrollListener;
import com.alialfayed.pagination.java.view.adapters.AdapterTopMoviesPagination;
import com.alialfayed.pagination.java.viewModel.HomeViewModel;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding ;
    private HomeViewModel homeViewModel;
    private AdapterTopMoviesPagination mAdapter;
    private int pageStart = 1 ;
    private Boolean isLoading = false ;
    private Boolean isLastPage = false ;
    private int totalPages = 1 ;
    private int currentPage = pageStart ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);

        homeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(HomeViewModel.class);

        initMyOrderRecyclerView();
        observerDataRequest();
    }

    private void initMyOrderRecyclerView() {
        //attach adapter to  recycler
        mAdapter = new AdapterTopMoviesPagination(this);
        binding.setAdapterTopMovies(mAdapter);
        binding.recyclerMyOrders.setHasFixedSize(true);
        binding.recyclerMyOrders.setItemAnimator(new DefaultItemAnimator());

        loadFirstPage();

        binding.recyclerMyOrders
                .addOnScrollListener(new  PaginationScrollListener((LinearLayoutManager) binding.recyclerMyOrders.getLayoutManager()) {

                    @Override
                    protected void loadMoreItems() {
                        isLoading = true ;
                        currentPage += 1 ;

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNextPage();
                            }
                        } , 1000);
                    }

                    @Override
                    protected int getTotalPageCount() {
                        return totalPages;
                    }

                    @Override
                    protected boolean isLastPage() {
                        return isLastPage;
                    }

                    @Override
                    protected boolean isLoading() {
                        return isLoading;
                    }
                });
    }

    private void loadFirstPage() {
        hideErrorView();
        if (CheckValidation.isConnected(this)) {
            homeViewModel.requestFirstPageTopMovies(currentPage);
        }else{
            showErrorView(null);
        }
    }

    public void loadNextPage() {
        if (CheckValidation.isConnected(this)) {
            homeViewModel.requestFirstNextPageMovies(currentPage);
        }else{
            mAdapter.showRetry(true, fetchErrorMessage(null));
        }
    }

    private void observerDataRequest(){
        homeViewModel.getFirstPageTopMovies().observe(this, it -> {
            if (it instanceof ResponseTopMovies) {
                hideErrorView();
                List<ResultsItem> results = fetchResults((ResponseTopMovies) it);
                binding.mainProgress.setVisibility(View.GONE);
                mAdapter.addAll(results);
                totalPages = ((ResponseTopMovies) it).getTotalPages();

                if (currentPage <= totalPages) mAdapter.addLoadingFooter();
                else isLastPage = true ;
            }else if (it instanceof Throwable){
                showErrorView((Throwable) it);
            }else{
                MessageLog.setLogCat("TAG_TEST" , "Error First Page");
            }
        });

        homeViewModel.getFirstNextTopMovies().observe(this, it -> {

            if (it instanceof ResponseTopMovies) {

                List<ResultsItem> results = fetchResults((ResponseTopMovies) it);
                mAdapter.removeLoadingFooter();
                isLoading = false ;
                mAdapter.addAll(results) ;

                if (currentPage != totalPages) mAdapter.addLoadingFooter();
                else isLastPage = true;

            }else if (it instanceof Throwable){
                mAdapter.showRetry(true, fetchErrorMessage((Throwable) it));
            }else{
                MessageLog.setLogCat("TAG_TEST" , "Error First Page");
            }
        });


    }

    private List<ResultsItem> fetchResults(ResponseTopMovies moviesTopModel) {
        return moviesTopModel.getResults();
    }

    private void showErrorView(Throwable throwable) {
        if (binding.lyError.errorLayout.getVisibility() == View.GONE) {
            binding.lyError.errorLayout.setVisibility(View.VISIBLE);
            binding.mainProgress.setVisibility(View.GONE);

            if (!CheckValidation.isConnected(this)) {
                binding.lyError.errorTxtCause.setText(R.string.error_msg_no_internet);
            } else {
                if (throwable instanceof TimeoutException) {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_timeout);
                } else {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_unknown);
                }
            }
        }
    }

    private void hideErrorView() {
        if (binding.lyError.errorLayout.getVisibility() == View.VISIBLE) {
            binding.lyError.errorLayout.setVisibility(View.GONE);
            binding.mainProgress.setVisibility(View.VISIBLE);
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!CheckValidation.isConnected(this)) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

}