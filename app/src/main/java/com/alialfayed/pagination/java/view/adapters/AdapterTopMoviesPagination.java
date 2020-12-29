package com.alialfayed.pagination.java.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alialfayed.pagination.java.BR;
import com.alialfayed.pagination.java.R;
import com.alialfayed.pagination.java.databinding.ItemLoadingBinding;
import com.alialfayed.pagination.java.databinding.ItemMovieBinding;
import com.alialfayed.pagination.java.model.ResultsItem;
import com.alialfayed.pagination.java.view.activities.HomeActivity;
import com.alialfayed.pagination.java.view.interfaceies.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 12/29/2020 - 5:20 PM
 */
public class AdapterTopMoviesPagination extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PaginationAdapterCallback {

    private HomeActivity mActivity;
    private int item = 0 ;
    private int loading = 1 ;

    private Boolean isLoadingAdded  = false;
    private Boolean retryPageLoad = false;

    private String errorMsg = "";

    private List<ResultsItem> moviesModels  = new ArrayList();

    public AdapterTopMoviesPagination(HomeActivity mActivity) {
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          if(viewType == item){
              ItemMovieBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_movie, parent, false);
              return new TopMoviesVH(binding);
        }else{
              ItemLoadingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_loading, parent, false) ;
              return new  LoadingVH(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResultsItem model = moviesModels.get(position);
        if(getItemViewType(position) == item){
            TopMoviesVH myOrderVH = (TopMoviesVH) holder;
            myOrderVH.itemRowBinding.movieProgress.setVisibility(View.VISIBLE);
            if (model.getReleaseDate() != null){
                myOrderVH.bind(model);
            }else{
                moviesModels.remove(position);
            }
        }else{
            LoadingVH loadingVH = (LoadingVH) holder;
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreErrorlayout.setVisibility(View.VISIBLE);
                loadingVH.itemRowBinding.loadmoreProgress.setVisibility(View.GONE);

                if(errorMsg != null) loadingVH.itemRowBinding.loadmoreErrortxt.setText(errorMsg);
                else loadingVH.itemRowBinding.loadmoreErrortxt.setText(mActivity.getString(R.string.error_msg_unknown));
            } else {
                loadingVH.itemRowBinding.loadmoreErrorlayout.setVisibility(View.GONE);
                loadingVH.itemRowBinding.loadmoreProgress.setVisibility(View.VISIBLE);
            }

            loadingVH.itemRowBinding.loadmoreRetry.setOnClickListener(view -> {
                showRetry(false, "");
                retryPageLoad();
            });
            loadingVH.itemRowBinding.loadmoreErrorlayout.setOnClickListener(view -> {
                showRetry(false, "");
                retryPageLoad();
            });
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(moviesModels.size(), 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return  item;
        }else {
            if (position == moviesModels.size() - 1 && isLoadingAdded) {
                return loading;
            } else {
                return  item;
            }
        }
    }

    @Override
    public void retryPageLoad() {
        mActivity.loadNextPage();
    }

    public static class TopMoviesVH extends RecyclerView.ViewHolder {

        ItemMovieBinding itemRowBinding;
        public TopMoviesVH(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            itemRowBinding = binding;
        }

        void bind(Object obj){
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }

    }

    public static class LoadingVH extends RecyclerView.ViewHolder{

        ItemLoadingBinding itemRowBinding;
        public LoadingVH(@NonNull ItemLoadingBinding binding) {
            super(binding.getRoot());
            itemRowBinding = binding;
        }
    }
    public void showRetry( Boolean show,  String errorMsg){
        retryPageLoad = show ;
        notifyItemChanged(moviesModels.size() - 1);
        this.errorMsg = errorMsg ;
    }

    public void add(ResultsItem moive) {
        moviesModels.add(moive);
        notifyItemInserted(moviesModels.size() - 1);
    }

    public void addAll(List<ResultsItem> movies) {
        for (int i = 0 ; i < movies.size() ; i++){
            add(movies.get(i));
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ResultsItem());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int  position  = moviesModels.size() -1 ;
        ResultsItem movie  = moviesModels.get(position);

        if(movie != null){
            moviesModels.remove(position);
            notifyItemRemoved(position);
        }
    }


}
