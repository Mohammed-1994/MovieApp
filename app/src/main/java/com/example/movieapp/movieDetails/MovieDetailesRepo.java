package com.example.movieapp.movieDetails;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieDetailes;
import com.example.movieapp.data.reposotory.MovieDetailesNetworkDataSource;
import com.example.movieapp.data.reposotory.Status;

import io.reactivex.disposables.CompositeDisposable;

public class MovieDetailesRepo {
    private static final String TAG = "MovieDetailesRepo, myTag";
    MovieDbClient movieDbClient;
    MovieDetailesNetworkDataSource networkDataSource;
    private static MovieDetailesRepo instance;

    public MovieDetailesRepo(MovieDbClient movieDbClient) {
        Log.d(TAG, "MovieDetailesRepo: constructor");
        this.movieDbClient = movieDbClient;

    }

    public LiveData<MovieDetailes> fetchSingleMovieDetail(CompositeDisposable disposable, int movieId) {
        Log.d(TAG, "fetchSingleMovieDetail: ");
        networkDataSource = new MovieDetailesNetworkDataSource(movieDbClient, disposable);
        networkDataSource.fetchMovieDetailes(movieId);

        return networkDataSource.detailesLiveData;

    }

    public LiveData<Status> fetchMovieDetailStatus() {
        return networkDataSource.networkState;
    }

    public static MovieDetailesRepo getInstance() {
        if (instance == null)
            instance = new MovieDetailesRepo(MovieDbClient.getInstance());
        return instance;
    }
}
