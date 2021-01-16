package com.example.movieapp.search;

import android.content.SearchRecentSuggestionsProvider;
import android.util.Log;

public class RecentQueries extends SearchRecentSuggestionsProvider {
    private static final String TAG = "MySuggestions myTag";
    public final static String AUTHORITY = "com.example.movieapp.search.RecentQueries";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public RecentQueries() {
        Log.d(TAG, "RecentQueries: constructor");
        setupSuggestions(AUTHORITY, MODE);
    }
}
