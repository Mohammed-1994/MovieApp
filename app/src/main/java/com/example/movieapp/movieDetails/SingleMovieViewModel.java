package com.example.movieapp.movieDetails;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieDetailes;
import com.example.movieapp.data.reposotory.Repository;
import com.example.movieapp.data.reposotory.Status;

import java.time.LocalDate;

import io.reactivex.disposables.CompositeDisposable;

public class SingleMovieViewModel extends ViewModel {

    private static final String TAG = "SingleMovieViewModel myTag";


    private CompositeDisposable disposable = new CompositeDisposable();
    private Repository repository = new Repository(disposable);


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public MutableLiveData<MovieDetailes> getMyMovieDetail(int movieId) {
        return repository.getSingleMovie(movieId);

    }

    public MutableLiveData<Status> getNetworkStatus() {
        return repository.mNetworkState;
    }

}
