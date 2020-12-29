package com.example.movieapp.data.reposotory;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieResponse;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.movieapp.data.api.MovieDbClient.FIRST_PAGE;

public class MovieDataSource extends PageKeyedDataSource<Integer, MovieResponse.Results> {
    private static final String TAG = "MovieDataSource myTag";
    private MovieDbClient movieDbClient;
    private CompositeDisposable disposable;
    private int page = FIRST_PAGE;
    public MutableLiveData<Status> networkStatus = new MutableLiveData<>();

    public MovieDataSource(MovieDbClient movieDbClient, CompositeDisposable disposable) {
        this.movieDbClient = movieDbClient;
        this.disposable = disposable;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MovieResponse.Results> callback) {
        networkStatus.postValue(Status.RUNNING_);
        disposable.add(movieDbClient.getMovies(page).subscribeOn(Schedulers.io())
                .subscribe(movieResponse -> {
                    callback.onResult(movieResponse.Result(), null, page + 1);
                    networkStatus.postValue(Status.SUCCESS_);
                }, error -> {
                    networkStatus.postValue(Status.FAILED_);
                    Log.d(TAG, "loadInitial: " + error.getMessage());
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieResponse.Results> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieResponse.Results> callback) {
        Log.d(TAG, "loadAfter: pageIndex = : "  + params.key);
        Log.d(TAG, "loadAfter: requested load size = : "  + params.requestedLoadSize);
        networkStatus.postValue(Status.RUNNING_);
        disposable.add(movieDbClient.getMovies(params.key).subscribeOn(Schedulers.io())
                .subscribe(movieResponse -> {
                    if (movieResponse.getTotalPages() >= params.key) {
                        callback.onResult(movieResponse.Result(), params.key + 1);

                    } else {
                        networkStatus.postValue(Status.END_OF_LIST);
                    }
                    networkStatus.postValue(Status.SUCCESS_);
                }, error -> {
                    networkStatus.postValue(Status.FAILED_);
                    Log.d(TAG, "loadInitial: " + error.getMessage());
                }));
    }
}

