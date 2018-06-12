package com.zachary.lynch.popularmovies.movies;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class MovieData implements Parcelable{
    private String mTitle;
    private String mReleaseDate;
    private int mVoteAverage;
    private String mPlot;
    private String mPosterImage;
    private int mMovieId;
    private String mMovieTrailer;
    private String  mAuthors;
    private String  mReviews;
    private String mTrailerName;

    public String getTrailerName() {
        return mTrailerName;
    }

    public void setTrailerName(String trailerName) {
        mTrailerName = trailerName;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public void setAuthors(String authors) {
        mAuthors = authors;
    }

    public String getReviews() {
        return mReviews;
    }

    public void setReviews(String reviews) {
        mReviews = reviews;
    }


    private int mNumOfTrailers;

    public int getNumOfTrailers() {
        return mNumOfTrailers;
    }

    public void setNumOfTrailers(int numOfTrailers) {
        mNumOfTrailers = numOfTrailers;
    }

    public String getMovieTrailer() {
        return mMovieTrailer;
    }

    public void setMovieTrailer(String movieTrailer) {
        mMovieTrailer = movieTrailer;
    }

    public String getMovieId() {
        return mMovieId + "";
    }

    public void setMovieId(int movieId) {
        mMovieId = movieId;
    }

    public MovieData(){

    }
    public String getPosterImage() {

        return "http://image.tmdb.org/t/p//w185" + mPosterImage;
    }

    public void setPosterImage(String posterImage) {
        mPosterImage = posterImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public int getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getPlot() {
        return mPlot;
    }

    public void setPlot(String plot) {
        mPlot = plot;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeInt(mVoteAverage);
        dest.writeString(mPlot);
        dest.writeString(mPosterImage);
        dest.writeInt(mMovieId);
        dest.writeString(mMovieTrailer);
        dest.writeString(mAuthors);
        dest.writeString(mReviews);
        dest.writeString(mTrailerName);

    }
    private MovieData(Parcel in){
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readInt();
        mPlot = in.readString();
        mPosterImage = in.readString();
        mMovieId = in.readInt();
        mMovieTrailer = in.readString();
        mAuthors = in.readString();
        mReviews = in.readString();
        mTrailerName = in.readString();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int i) {
            return new MovieData[i];
        }
    };






}
