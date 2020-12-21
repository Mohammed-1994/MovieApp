package com.example.movieapp.data.reposotory;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieDetailes;
import com.example.movieapp.movieDetails.MovieDetailesRepo;

import io.reactivex.Observable;
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

}
