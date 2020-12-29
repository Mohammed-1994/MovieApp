package com.example.movieapp.moviePubular;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.reposotory.Repository;

import io.reactivex.disposables.CompositeDisposable;

public class MyPobularMoviesViewModel extends ViewModel {
    private static final String TAG = "MyPobularMoviesViewMode myTag";
    private CompositeDisposable disposable = new CompositeDisposable();
    private Repository repository = new Repository(disposable);

    public MutableLiveData<MovieResponse> getMovies(int pageIndex){
        return repository.getPopular(pageIndex);
    }
}
