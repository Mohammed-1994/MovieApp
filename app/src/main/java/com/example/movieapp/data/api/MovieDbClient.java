package com.example.movieapp.data.api;

import android.util.Log;

import com.example.movieapp.data.pojo.MovieDetailes;
import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.pojo.SearchedMovie;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDbClient {

    public static MovieDbClient instance;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static MovieDbInterface movieDbInterface;
    public static final String apiKey = "f5842cc12eef14f985782ec2ae121db7";
    private static final String TAG = "MovieDbClient myTag";
    public static final int FIRST_PAGE = 1;
    public static final int POST_PER_PAGE = 20;

    public MovieDbClient() {
        Log.d(TAG, "MovieDbClient: constructor");
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(chain -> {
            Request.Builder request = chain.request().newBuilder();
            HttpUrl originalHttpUrl = chain.request().url();
            HttpUrl.Builder url = originalHttpUrl.newBuilder().addQueryParameter("api_key", apiKey);
            request.url(url.build());
            return chain.proceed(request.build());
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        movieDbInterface = retrofit.create(MovieDbInterface.class);
    }

    public static MovieDbClient getInstance() {
        if (instance == null)
            instance = new MovieDbClient();
        return instance;
    }

    public Single<MovieDetailes> getMovieDetailesSingle(int movieId) {
        return movieDbInterface.getMovieDetails(movieId);
    }

    public Single<MovieResponse> getMovies(int page) {
        return movieDbInterface.getMovie(page);
    }

    public Single<SearchedMovie> searchMovie(String query) {
        return movieDbInterface.searchMovie(query);
    }

}
