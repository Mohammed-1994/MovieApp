package com.example.movieapp.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.pojo.SearchedMovie;
import com.example.movieapp.data.reposotory.MovieDataSource;

import io.reactivex.disposables.CompositeDisposable;

public class SearchedDataSourceFactory extends DataSource.Factory<Integer, SearchedMovie.Results> {


    private MovieDbClient movieDbClient;
    private CompositeDisposable disposable;
    private String query;
    public MutableLiveData<SearchedDataSource> data = new MutableLiveData<>();

    public SearchedDataSourceFactory(MovieDbClient movieDbClient, CompositeDisposable disposable, String query) {
        this.movieDbClient = movieDbClient;
        this.disposable = disposable;
        this.query = query;
    }

    @NonNull
    @Override
    public DataSource<Integer, SearchedMovie.Results> create() {

        SearchedDataSource searchedDataSource = new SearchedDataSource(movieDbClient, disposable, query);
        data.postValue(searchedDataSource);
        return searchedDataSource;
    }
}
