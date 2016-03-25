package com.example.soujanya.moiveapp.modal;

/**
 * Created by soujanya on 10/28/15.
 */
public class ImageItem {

    public String imageUrl;
    public String plotSynopsis;
    public String userRating;
    public String releaseDate;
    public String backdropPath;


    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public ImageItem(String imageUrl,String movieId,String plotSynopsis,String title,String releaseDate,String userRating,String popular,String backdropPath){
        super();
        this.imageUrl = imageUrl;
        this.movieId = movieId;
        this.plotSynopsis = plotSynopsis;
        this.title = title;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.popular = popular;
        this.backdropPath = backdropPath;

    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "imageUrl='" + imageUrl + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", userRating='" + userRating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", popular='" + popular + '\'' +
                ", title='" + title + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }

    public String popular;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    String movieId;

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }



    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }




    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
