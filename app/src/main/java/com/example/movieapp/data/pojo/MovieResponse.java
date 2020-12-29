package com.example.movieapp.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;


@SuppressWarnings("all")
public class MovieResponse {
    @SerializedName("page")
    private final int page;

    @SerializedName("results")
    private final List<Results> results;

    @SerializedName("total_pages")
    private final int totalPages;

    @SerializedName("total_results")
    private final int totalResults;

    public MovieResponse(int page, List<Results> results, int totalPages, int totalResults) {
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public int getPage() {
        return page;
    }

    public List<Results> Result() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public static class Results {

        @SerializedName("id")
        private final int id;

        @SerializedName("poster_path")
        private final String posterPath;

        @SerializedName("release_date")
        private final String releaseDate;

        @SerializedName("title")
        private final String title;

        @SerializedName("vote_average")
        private final double voteAverage;

        public Results(int id, String posterPath, String releaseDate, String title, double voteAverage) {
            this.id = id;
            this.posterPath = posterPath;
            this.releaseDate = releaseDate;
            this.title = title;
            this.voteAverage = voteAverage;
        }

        public int getId() {
            return id;
        }


        public String getPosterPath() {
            return posterPath;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public String getTitle() {
            return title;
        }


        public double getVoteAverage() {
            return voteAverage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Results movie = (Results) o;
            return id == movie.id &&
                    voteAverage == movie.voteAverage &&
                    Objects.equals(posterPath, movie.posterPath) &&
                    Objects.equals(releaseDate, movie.releaseDate) &&
                    Objects.equals(title, movie.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, posterPath, releaseDate, title, voteAverage);
        }
    }
}
