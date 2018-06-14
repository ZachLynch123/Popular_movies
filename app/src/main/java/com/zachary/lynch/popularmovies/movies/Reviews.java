package com.zachary.lynch.popularmovies.movies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Reviews implements Parcelable{

    private String mAuthor;
    private String mContent;

    public Reviews(){}

    public Reviews(JSONObject review) throws JSONException{
        this.mAuthor = review.getString("author");
        this.mContent = review.getString("content");
    }



    public String getAuthor() {
        return mAuthor;
    }


    public String getContent() {
        return mContent;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
    private Reviews(Parcel in){
        mAuthor = in.readString();
        mContent = in.readString();
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
