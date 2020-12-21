package com.example.movieapp.data.reposotory;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.api.MovieDbInterface;
import com.example.movieapp.data.pojo.MovieDetailes;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailesNetworkDataSource {
    private static final String TAG = "MovieDetailesNetworkDat myTag";


    private MutableLiveData<Status> mNetworkState;
    public LiveData<Status> networkState;

    private MutableLiveData<MovieDetailes> mMovieDetailesData;
    public LiveData<MovieDetailes> detailesLiveData;

    private MovieDbClient movieDbClient;
    private CompositeDisposable disposable;

    public MovieDetailesNetworkDataSource(MovieDbClient dbInterface, CompositeDisposable disposable) {
        this.movieDbClient = dbInterface;
        this.disposable = disposable;
    }

    public MutableLiveData<Status> getmNetworkState() {
        return mNetworkState;
    }

    public MutableLiveData<MovieDetailes> getmMovieDetailesData() {
        return mMovieDetailesData;
    }


    public void fetchMovieDetailes(int movieId) {
        mNetworkState.postValue(Status.RUNNING_);

        try {
            disposable.add(
                    movieDbClient.getMovieDetailesSingle(movieId).subscribeOn(Schedulers.io())
                            .subscribe(movieDetailes -> {
                                mMovieDetailesData.postValue(movieDetailes);
                                mNetworkState.postValue(Status.SUCCESS_);
                            }, w -> {
                                Log.d(TAG, "fetchMovieDetailes: " + w.getMessage());
                                mNetworkState.postValue(Status.FAILED_);
                            })
            );
        } catch (Exception e) {
            Log.d(TAG, "fetchMovieDetailes: " + e.getMessage());

        }
    }
}
