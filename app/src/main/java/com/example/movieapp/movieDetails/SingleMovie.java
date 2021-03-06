package com.example.movieapp.movieDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.api.MovieDbInterface;
import com.example.movieapp.data.pojo.MovieDetailes;
import com.example.movieapp.data.reposotory.Status;
import com.example.movieapp.databinding.ActivitySingleMovieBinding;

public class SingleMovie extends AppCompatActivity {

    private static final String TAG = "SingleMovie myTag";
    private SingleMovieViewModel movieViewModel;
    private ActivitySingleMovieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_movie);
        binding.setLifecycleOwner(this);
        movieViewModel = new ViewModelProvider(this).get(SingleMovieViewModel.class);


        updateUi();

    }

    private void updateUi() {
        movieViewModel.getMyMovieDetail(getIntent().getIntExtra("id", 1))
                .observe(this, movieDetails -> {
            Log.d(TAG, "onCreate: onChange");
            Log.d(TAG, "onCreate: " + movieDetails.toString());
            updateMovieDetails(movieDetails);
        });
    }

    private void updateMovieDetails(MovieDetailes movieDetails) {
        binding.progressBar.setVisibility(View.GONE);
        binding.movieTitle.setText(movieDetails.getTitle());
        binding.movieOverview.setText(movieDetails.getOverview());
        binding.movieRating.setText(String.valueOf(movieDetails.getVoteAverage()));
        binding.movieTagline.setText(movieDetails.getTagline());
        binding.movieReleaseDate.setText(movieDetails.getReleaseDate());

        Glide.with(this)
                .load(getString(R.string.image_paht) + movieDetails.getPosterPath())
                .into(binding.ivMoviePoster)
                .onLoadStarted(getResources().getDrawable(R.drawable.placeholder));


        checkNetworkStatus();
    }

    private void checkNetworkStatus() {

        movieViewModel.getNetworkStatus().observe(this, status -> {
            Log.d(TAG, "checkNetworkStatus: status : " + status.name());
            if (status == Status.FAILED_)
                binding.txtError.setVisibility(View.VISIBLE);
            else
                binding.txtError.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);



        return true;


    }
}