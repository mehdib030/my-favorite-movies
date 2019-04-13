package com.e.myfavoritemovies.model;

/**
 * This class represents a movie object model
 * original title
 * movie poster image thumbnail
 * A plot synopsis (called overview in the api)
 * user rating (called vote_average in the api)
 * release date
 */
public class Movie {
    private String originalTitle;
    private String image;
    private String plotSynopsis;
    private String rating;
    private String releaseDate;

    public Movie(){}

    public Movie(String originalTitle, String image, String plotSynopsis, String rating, String releaseDate){
        this.originalTitle=originalTitle;
        this.image=image;
        this.plotSynopsis=plotSynopsis;
        this.rating=rating;
        this.releaseDate=releaseDate;
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




}
