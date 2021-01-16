package com.example.movieapp.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.data.api.MovieDbClient;
import com.example.movieapp.data.pojo.SearchedMovie;
import com.example.movieapp.data.reposotory.Status;
import com.example.movieapp.moviePubular.MainActivity;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity myTag";
    private SearchedActivityViewModel viewModel;
    private SearchedPagedListRepo repo;
    private MovieDbClient movieDbClient;
    private ViewModelProvider.Factory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Log.d(TAG, "onCreate: ");
        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void populateResult() {
        MovieDiffUtils diffUtils = new MovieDiffUtils();
        SearchedMoviePagedListAdapter adapter = new SearchedMoviePagedListAdapter(diffUtils, this);

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
        viewModel.listLiveData.observe(this, movieResponse ->
                adapter.submitList(movieResponse)
        );
        try {
            viewModel.statusLiveData.observe(this, status -> {
                ProgressBar progressBar = findViewById(R.id.progress_bar_popular);
                if (status == Status.RUNNING_)
                    progressBar.setVisibility(View.VISIBLE);
                else
                    progressBar.setVisibility(View.GONE);


                TextView textView = findViewById(R.id.txt_error_popular);
                if (viewModel.listIsEmpty() && status == Status.FAILED_)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.GONE);

                adapter.setNetworkStatus(status);


                Log.d(TAG, "onCreate: network status: " + status.name());
            });
        } catch (Exception e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent: ");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            RecentQueries mySuggestions = new RecentQueries();
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    mySuggestions.AUTHORITY, mySuggestions.MODE);
            suggestions.saveRecentQuery(query, null);


            movieDbClient = MovieDbClient.getInstance();
            repo = new SearchedPagedListRepo(movieDbClient);

            viewModel = getViewModel(repo, query);

            populateResult();
        }
    }

    private SearchedActivityViewModel getViewModel(SearchedPagedListRepo repo, String query) {
        Log.d(TAG, "getViewModel: ");

        factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                Log.d(TAG, "create: ");
                return (T) new SearchedActivityViewModel(repo, query);
            }
        };


        return new ViewModelProvider(this, factory).get(SearchedActivityViewModel.class);
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

    static class MovieDiffUtils extends DiffUtil.ItemCallback<SearchedMovie.Results> {
        @Override
        public boolean areItemsTheSame(@NonNull SearchedMovie.Results oldItem, @NonNull SearchedMovie.Results newItem) {
            Log.d(TAG, "areItemsTheSame: " + newItem.getId());
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SearchedMovie.Results oldItem, @NonNull SearchedMovie.Results newItem) {
            Log.d(TAG, "areContentsTheSame: " + newItem.getId());
            return oldItem.equals(newItem);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}