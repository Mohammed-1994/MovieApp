package com.example.movieapp.data.pojo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("all")
public class MovieDetailes {

    @SerializedName("id")
    private final int id;

    @SerializedName("original_title")
    private final String originalTitle;

    @SerializedName("overview")
    private final String overview;

    @SerializedName("popularity")
    private final double popularity;

    @SerializedName("poster_path")
    private final String posterPath;

    @SerializedName("release_date")
    private final String releaseDate;


    @SerializedName("status")
    private final String status;

    @SerializedName("tagline")
    private final String tagline;

    @SerializedName("title")
    private final String title;

    @SerializedName("vote_average")
    private final double voteAverage;

    @SerializedName("vote_count")
    private final int voteCount;

    public MovieDetailes(String homepage, int id, String imdbId, String originalLanguage,
                         String originalTitle, String overview, double popularity, String posterPath,
                         String releaseDate, int revenue, int runtime,
                         String status, String tagline, String title, boolean video, double voteAverage,
                         int voteCount) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }


    public int getId() {
        return id;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    @Override
    public String toString() {
        return "MovieDetailes{" +
                "id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", status='" + status + '\'' +
                ", tagline='" + tagline + '\'' +
                ", title='" + title + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }
}
