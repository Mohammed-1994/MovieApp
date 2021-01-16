package com.example.movieapp.data.api;

import com.example.movieapp.data.pojo.MovieDetailes;
import com.example.movieapp.data.pojo.MovieResponse;
import com.example.movieapp.data.pojo.SearchedMovie;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbInterface {

    // movie details with movie's id.
    //https://api.themoviedb.org/3/movie/765123?api_key=f5842cc12eef14f985782ec2ae121db7&language=en-US


    //popular movies
    //https://api.themoviedb.org/3/movie/popular?api_key=f5842cc12eef14f985782ec2ae121db7&language=en-US&page=1

    //base url
    //https://api.themoviedb.org/3/

    //poster image
    //https://image.tmdb.org/t/p/w342/ajKpYK7XdzIYjy9Uy8nkgRboKyv.jpg


    //search movies
//  https://api.themoviedb.org/3/search/movie?api_key=s&language=en-US&query=q&page=1&include_adult=false


    @GET("movie/{movie_id}")
    Single<MovieDetailes> getMovieDetails(@Path("movie_id") int id);

    @GET("movie/popular")
    Single<MovieResponse> getMovie(@Query("page") int page);

    @GET("search/movie")
    Single<SearchedMovie> searchMovie(@Query("query") String query);


}
