package com.e.myfavoritemovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a movie object model
 * original title
 * movie poster image thumbnail
 * A plot synopsis (called overview in the api)
 * user rating (called vote_average in the api)
 * release date
 */
public class Movie implements Parcelable {

    private String id;
    private String originalTitle;
    private String image;
    private String plotSynopsis;
    private String rating;
    private String releaseDate;
    private boolean favorite;

    private List<Review> reviews = new ArrayList();
    private List<Trailer> trailers = new ArrayList();

    public Movie(){}

    public Movie(String id, String originalTitle, String image, String plotSynopsis, String rating, String releaseDate){
        this.id=id;
        this.originalTitle=originalTitle;
        this.image=image;
        this.plotSynopsis=plotSynopsis;
        this.rating=rating;
        this.releaseDate=releaseDate;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        image = in.readString();
        plotSynopsis = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        favorite = in.readByte() != 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(image);
        parcel.writeString(plotSynopsis);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
        parcel.writeByte((byte) (favorite ? 1 : 0));
    }
}
