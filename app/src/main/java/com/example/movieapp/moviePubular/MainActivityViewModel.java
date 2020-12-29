package com.example.movieapp.moviePubular;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.reposotory.Status;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivityViewModel extends ViewModel {
    private static final String TAG = "MainActivityViewModel myTag";
    private MoviePageListRepo moviePageListRepo;
    private CompositeDisposable disposable = new CompositeDisposable();
    public LiveData<PagedList<MovieResponse.Results>> listLiveData ;
    public LiveData<Status> statusLiveData ;


    public MainActivityViewModel(MoviePageListRepo moviePageListRepo) {
        Log.d(TAG, "MainActivityViewModel: ");
        this.moviePageListRepo = moviePageListRepo;
        listLiveData = moviePageListRepo.fetchMoviePagedList(disposable);
        statusLiveData = moviePageListRepo.getNetworkStatus();
    }

    public Boolean listIsEmpty(){
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
