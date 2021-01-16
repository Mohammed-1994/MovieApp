package com.example.movieapp.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.SearchedMovie;
import com.example.movieapp.data.reposotory.Status;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.movieapp.data.api.MovieDbClient.POST_PER_PAGE;

public class SearchedPagedListRepo {


    private static final String TAG = "SearchedPagedListRepo myTag";
    private MovieDbClient movieDbClient;
    private SearchedDataSourceFactory searchedDataSourceFactory;
    private LiveData<PagedList<SearchedMovie.Results>> moviePagedList;

    public SearchedPagedListRepo(MovieDbClient movieDbClient) {
        Log.d(TAG, "SearchedPagedListRepo: constructor");
        this.movieDbClient = movieDbClient;
    }

    public LiveData<PagedList<SearchedMovie.Results>> fetchSearedMovies(CompositeDisposable disposable, String query){
        Log.d(TAG, "fetchSearedMovies: query =  " + query);
        searchedDataSourceFactory = new SearchedDataSourceFactory(movieDbClient, disposable, query);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(POST_PER_PAGE)
                .build();
        moviePagedList = new LivePagedListBuilder(searchedDataSourceFactory, config).build();

        return moviePagedList;

    }

    public LiveData<Status> getNetworkStatus() {
        return Transformations.switchMap(searchedDataSourceFactory.data, status -> status.networkStatus);

    }
}
