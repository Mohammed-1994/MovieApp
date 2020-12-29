package com.example.movieapp.data.reposotory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieResponse;

import io.reactivex.disposables.CompositeDisposable;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, MovieResponse.Results> {

    private MovieDbClient movieDbClient;
    private CompositeDisposable disposable;
    public MutableLiveData<MovieDataSource> data = new MutableLiveData<>();

    public MovieDataSourceFactory(MovieDbClient movieDbClient, CompositeDisposable disposable) {
        this.movieDbClient = movieDbClient;
        this.disposable = disposable;
    }

    @NonNull
    @Override
    public DataSource<Integer, MovieResponse.Results> create() {

        MovieDataSource movieDataSource = new MovieDataSource(movieDbClient, disposable);

        data.postValue(movieDataSource);

        return movieDataSource;
    }
}
