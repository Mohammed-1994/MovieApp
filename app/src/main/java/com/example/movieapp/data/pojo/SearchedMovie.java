package com.example.movieapp.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;


@SuppressWarnings("all")
public class SearchedMovie {
    @SerializedName("page")
    private final int page;

    @SerializedName("results")
    private final List<Results> results;

    @SerializedName("total_pages")
    private final int totalPages;

    @SerializedName("total_results")
    private final int totalResults;

    public SearchedMovie(int page, List<Results> results, int totalPages, int totalResults) {
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public int getPage() {
        return page;
    }

    public List<Results> getResults() {
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

        @SerializedName("video")
        private final boolean video;

        @SerializedName("vote_average")
        private final double voteAverage;


        public Results(int id, String posterPath, String releaseDate, String title, boolean video, double voteAverage) {
            this.id = id;
            this.posterPath = posterPath;
            this.releaseDate = releaseDate;
            this.title = title;
            this.video = video;
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

        public boolean isVideo() {
            return video;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Results results = (Results) o;
            return id == results.id &&
                    video == results.video &&
                    Double.compare(results.voteAverage, voteAverage) == 0 &&
                    Objects.equals(posterPath, results.posterPath) &&
                    Objects.equals(releaseDate, results.releaseDate) &&
                    Objects.equals(title, results.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, posterPath, releaseDate, title, video, voteAverage);
        }
    }
}
