package com.zachary.lynch.popularmovies.movies;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieData /*implements Parcelable*/{
    private String mTitle;
    private String mReleaseDate;
    private int mVoteAverage;
    private String mPlot;

    public MovieData(){

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

/*
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
    }
    private MovieData(Parcel in){
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readInt();
        mPlot = in.readString();
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
    };*/






}