package com.example.movieapp.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.SearchedMovie;
import com.example.movieapp.data.reposotory.Status;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.movieapp.data.api.MovieDbClient.FIRST_PAGE;

public class SearchedDataSource extends PageKeyedDataSource<Integer, SearchedMovie.Results> {

    private static final String TAG = "SearchedDataSource myTag";
    private MovieDbClient movieDbClient;
    private CompositeDisposable disposable;
    private int page = FIRST_PAGE;
    private String query;
    public MutableLiveData<Status> networkStatus = new MutableLiveData<>();

    public SearchedDataSource(MovieDbClient movieDbClient, CompositeDisposable disposable, String query) {
        this.movieDbClient = movieDbClient;
        this.disposable = disposable;
        this.query = query;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, SearchedMovie.Results> callback) {
        Log.d(TAG, "loadInitial: ");
        networkStatus.postValue(Status.RUNNING_);
        disposable.add(movieDbClient.searchMovie(query).subscribeOn(Schedulers.io())
                .subscribe(searchedMovie -> {
                    callback.onResult(searchedMovie.getResults(), null, page + 1);
                    networkStatus.postValue(Status.SUCCESS_);
                }, error -> {
                    Log.e(TAG, "loadInitial: " + error.getMessage());
                    networkStatus.postValue(Status.FAILED_);
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, SearchedMovie.Results> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, SearchedMovie.Results> callback) {
        Log.d(TAG, "loadAfter: pageIndex = : "  + params.key);
        Log.d(TAG, "loadAfter: requested load size = : "  + params.requestedLoadSize);
        networkStatus.postValue(Status.RUNNING_);
        disposable.add(movieDbClient.searchMovie(query).subscribeOn(Schedulers.io())
                .subscribe(searchedMovie -> {
                    if (searchedMovie.getTotalPages() >= params.key)
                        callback.onResult(searchedMovie.getResults(), params.key + 1);
                    else
                        networkStatus.postValue(Status.END_OF_LIST);

                    networkStatus.postValue(Status.SUCCESS_);

                }, error -> {
                    networkStatus.postValue(Status.FAILED_);
                    Log.e(TAG, "loadInitial: " + error.getMessage());
                }));
    }
}
