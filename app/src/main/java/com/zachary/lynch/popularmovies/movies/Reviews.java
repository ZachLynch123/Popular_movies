package com.zachary.lynch.popularmovies.movies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Reviews implements Parcelable{

    private String author;
    private String content;

    public Reviews(){}

    public Reviews(JSONObject review) throws JSONException{
        this.author = review.getString("author");
        this.content = review.getString("content");
    }

    public Reviews(Parcel source) {
    }


    public String getAuthor() {
        return author;
    }


    public String getContent() {
        return content;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel source) {
            return new Reviews(source);
        }

        @Override
        public Reviews[] newArray(int i) {
            return new Reviews[i];
        }
    };




}
