package com.example.movieapp.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.pojo.SearchedMovie;
import com.example.movieapp.data.reposotory.Status;
import com.example.movieapp.moviePubular.MoviePageListRepo;

import io.reactivex.disposables.CompositeDisposable;

public class SearchedActivityViewModel extends ViewModel {

    private static final String TAG = "SearchedActivityViewModel myTag";
    private SearchedPagedListRepo searchedPagedListRepo;
    public CompositeDisposable disposable = new CompositeDisposable();
    public LiveData<PagedList<SearchedMovie.Results>> listLiveData;
    public LiveData<Status> statusLiveData;
    private String query;

    public SearchedActivityViewModel(SearchedPagedListRepo searchedPagedListRepo, String query) {
        Log.d(TAG, "SearchedActivityViewModel: constructor");
        this.searchedPagedListRepo = searchedPagedListRepo;
        this.query = query;
        listLiveData = searchedPagedListRepo.fetchSearedMovies(disposable ,query);
        statusLiveData = searchedPagedListRepo.getNetworkStatus();
    }

    public Boolean listIsEmpty() {
        try {
            return listLiveData.getValue().isEmpty();
        } catch (Exception e) {
            Log.e(TAG, "listIsEmpty: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared: ");
        super.onCleared();
        disposable.dispose();
    }


}
