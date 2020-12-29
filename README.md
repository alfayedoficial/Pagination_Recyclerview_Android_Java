# Pagination_Recyclerview_Android_Java
Pagination Recyclerview Android Java We use the Pagination Library of Android Jetpack in this app to fetch data from the database to recyclerView by retrofit API, This code is following the principles of MVVM design pattern and LiveData.

<p align="left">
  <a href="https://github.com/sweetalert2/sweetalert2/actions"><img alt="Build Status" src="https://github.com/sweetalert2/sweetalert2/workflows/build/badge.svg"></a>
</p>

---

Screenshot Picture
-----
<p align="center">
  <img src="https://github.com/alfayedoficial/Pagination_Recyclerview_Android_Java/blob/master/demo/sc1.png" width="350" title="Screen1">
  <img src="https://github.com/alfayedoficial/Pagination_Recyclerview_Android_Java/blob/master/demo/sc2.png" width="350" title="Screen2">
  <img src="https://github.com/alfayedoficial/Pagination_Recyclerview_Android_Java/blob/master/demo/sc3.png" width="350" title="Screen2">
  <img src="https://github.com/alfayedoficial/Pagination_Recyclerview_Android_Java/blob/master/demo/sc4.png" width="350" title="Screen2">
/>

[Watch the Demo App](https://github.com/alfayedoficial/Pagination_Recyclerview_Android_Java/blob/master/demo/java.webm)

Installation
------------

```java

 // add dependence of pagination to gradel script 
 def paging_version = "2.1.2"
 implementation "androidx.paging:paging-runtime:$paging_version" // pagination
 
 // add another dependencies  [Optional]
 implementation 'com.github.bumptech.glide:glide:4.11.0' // For Image
 annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
 
 def retrofit2_version = "2.8.1" //Retrofit2
 implementation "com.squareup.retrofit2:retrofit:$retrofit2_version"
 implementation "com.squareup.retrofit2:converter-gson:$retrofit2_version"
  
 def lifecycle_version = "2.2.0"  //lifecycle
 implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
 implementation "android.arch.lifecycle:extensions:$lifecycle_version"
 annotationProcessor "android.arch.lifecycle:compiler:$lifecycle_version"
 
 ```
 
Create Abstract Class PaginationScrollListener
--------

```java
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();
    protected abstract int getTotalPageCount();
    protected abstract boolean isLastPage();
    protected abstract boolean isLoading();

} 
 ```
 
Usage
-----

```java
    private int pageStart = 1 ;
    private Boolean isLoading = false ;
    private Boolean isLastPage = false ;
    private int totalPages = 1 ;
    private int currentPage = pageStart ;
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
 
 ```
 
 Create Adapter Class AdapterTopMoviesPagination
--------

```java
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
 
 ```

Please note that [Pagination Library is well-supported and Free License](https://github.com/alfayedoficial/Pagination_Recyclerview_Android_Java), so you can use app and edit.


