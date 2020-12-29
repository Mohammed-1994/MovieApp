package com.example.movieapp.moviePubular;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.reposotory.Status;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity myTag";

    private MainActivityViewModel mainActivityViewModel;
    //    private MyPobularMoviesViewModel myPobularMoviesViewModel;
    private MoviePageListRepo repo;
    private MovieDbClient movieDbClient = MovieDbClient.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        repo = new MoviePageListRepo(movieDbClient);

//        myPobularMoviesViewModel = new ViewModelProvider(this).get(MyPobularMoviesViewModel.class);

        mainActivityViewModel = getViewModel();
        MovieDiffUtils diffUtils = new MovieDiffUtils();
        PopularMoviePagedListAdapter adapter = new PopularMoviePagedListAdapter(diffUtils, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == adapter.MOVIE_VIEW_TYPE)
                    return 1;
                else return 3;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);
        mainActivityViewModel.listLiveData.observe(this, movieResponse ->
                adapter.submitList(movieResponse)
        );

        try {
            mainActivityViewModel.statusLiveData.observe(this, status -> {
                ProgressBar progressBar = findViewById(R.id.progress_bar_popular);
                if (mainActivityViewModel.listIsEmpty() && status == Status.RUNNING_)
                    progressBar.setVisibility(View.VISIBLE);
                else
                    progressBar.setVisibility(View.GONE);


                TextView textView = findViewById(R.id.txt_error_popular);
                if (mainActivityViewModel.listIsEmpty() && status == Status.FAILED_)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.GONE);

                if (!mainActivityViewModel.listIsEmpty())
                    adapter.setNetworkStatus(status);


                Log.d(TAG, "onCreate: network status: " + status.name());
            });
        } catch (Exception e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }

    }

    private MainActivityViewModel getViewModel() {
        Log.d(TAG, "getViewModel: ");
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                Log.d(TAG, "create: ");
                return (T) new MainActivityViewModel(repo);
            }
        };
        return new ViewModelProvider(this, factory).get(MainActivityViewModel.class);
    }


    static class MovieDiffUtils extends DiffUtil.ItemCallback<MovieResponse.Results> {
        @Override
        public boolean areItemsTheSame(@NonNull MovieResponse.Results oldItem, @NonNull MovieResponse.Results newItem) {
            Log.d(TAG, "areItemsTheSame: " + newItem.getId());
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieResponse.Results oldItem, @NonNull MovieResponse.Results newItem) {
            Log.d(TAG, "areContentsTheSame: " + newItem.getId());
            return oldItem.equals(newItem);
        }
    }
}