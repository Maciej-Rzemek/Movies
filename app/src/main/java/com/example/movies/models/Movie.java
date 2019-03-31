package com.example.movies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("overview")
    @Expose
    private String description;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("vote_average")
    @Expose
    private double rating;
    @SerializedName("backdrop_path")
    @Expose
    private String backdrop;



    public Movie(String originalTitle, int id, String description, String releaseDate, String posterPath, double rating, String backdrop) {
        this.originalTitle = originalTitle;
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.rating = rating;
        this.backdrop = backdrop;
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        originalTitle = in.readString();
        id = in.readInt();
        description = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        rating = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(rating);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "originalTitle='" + originalTitle + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", rating=" + rating + '\'' +
                ", backdrop=" + backdrop + '\'' +
                '}';
    }
}
