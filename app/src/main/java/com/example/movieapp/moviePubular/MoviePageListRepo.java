package com.example.movieapp.moviePubular;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.reposotory.MovieDataSource;
import com.example.movieapp.data.reposotory.MovieDataSourceFactory;
import com.example.movieapp.data.reposotory.Status;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.movieapp.data.api.MovieDbClient.POST_PER_PAGE;

public class MoviePageListRepo {
    private static final String TAG = "MoviePageListRepo myTag";
    private MovieDbClient movieDbClient;
    private MovieDataSourceFactory movieDataSourceFactory;
    private LiveData<PagedList<MovieResponse.Results>> moviePagedList;

    public MoviePageListRepo(MovieDbClient movieDbClient) {
        Log.d(TAG, "MoviePageListRepo: ");
        this.movieDbClient = movieDbClient;
    }

    public LiveData<PagedList<MovieResponse.Results>> fetchMoviePagedList(CompositeDisposable disposable) {
        Log.d(TAG, "fetchMoviePagedList: ");
        movieDataSourceFactory = new MovieDataSourceFactory(movieDbClient, disposable);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(POST_PER_PAGE)
                .build();
        moviePagedList = new LivePagedListBuilder(movieDataSourceFactory, config).build();

        return moviePagedList;
    }


    public LiveData<Status> getNetworkStatus() {
        return Transformations.switchMap(movieDataSourceFactory.data, status -> status.networkStatus);

    }


}
