package com.example.movieapp.data.reposotory;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieDetailes;
import com.example.movieapp.data.pojo.MovieResponse;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Repository {
    private static final String TAG = "Repository myTag";
    private CompositeDisposable disposable;
    public MutableLiveData<MovieDetailes> movieDetailes = new MutableLiveData<>();
    private MovieDbClient movieDbClient = MovieDbClient.getInstance();
    public MutableLiveData<Status> mNetworkState = new MutableLiveData<>();


    public Repository(CompositeDisposable disposable) {
        this.disposable = disposable;
    }


    public MutableLiveData<MovieDetailes> getSingleMovie(int movieId) {

        Log.d(TAG, "getSingleMovie: ");
        mNetworkState.postValue(Status.RUNNING_);
        Single<MovieDetailes> detailesSingle = movieDbClient.getMovieDetailesSingle(movieId)
                .subscribeOn(Schedulers.io());

        SingleObserver<MovieDetailes> observer = new SingleObserver<MovieDetailes>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(MovieDetailes value) {
                Log.d(TAG, "onSuccess: ");
                mNetworkState.postValue(Status.SUCCESS_);
                movieDetailes.postValue(value);
            }

            @Override
            public void onError(Throwable e) {
                mNetworkState.postValue(Status.FAILED_);
                Log.d(TAG, "onError: " + e.getMessage());
            }
        };


        detailesSingle.cache()
                .subscribe(observer);

        return movieDetailes;
    }

    public MutableLiveData<MovieResponse> getPopular(int pageIndex) {
        MutableLiveData<MovieResponse> movies = new MutableLiveData<>();
        Single<MovieResponse> movieResponseSingle = movieDbClient.getMovies(pageIndex)
                .subscribeOn(Schedulers.io());

        SingleObserver<MovieResponse> observer = new SingleObserver<MovieResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(MovieResponse value) {
                if (value != null){
                    Log.d(TAG, "onSuccess: nonnull");
                    Log.d(TAG, "onSuccess: page = " + value.getPage());
                    Log.d(TAG, "onSuccess: total pages = " + value.getTotalPages());
                    Log.d(TAG, "onSuccess: total result = " + value.getTotalResults());
                    Log.d(TAG, "onSuccess: result size = " + value.Result().size());
                }else
                    Log.d(TAG, "onSuccess: null");
                movies.postValue(value);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        };

        movieResponseSingle.subscribe(observer);

        return movies;
    }

}
