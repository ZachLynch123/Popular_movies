package com.zachary.lynch.popularmovies.movies;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailers implements Parcelable {

    private String mTrailer;
    private String mName;

    public Trailers(){}

    public Trailers(JSONObject singleTrailerObject) throws JSONException{
        this.mTrailer = singleTrailerObject.getString("key");
        this.mName = singleTrailerObject.getString("name");

    }



    public String getTrailer() {
        return "https://www.youtube.com/watch?v="+ mTrailer;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailer);
        dest.writeString(mName);

    }
    private Trailers(Parcel in){
        mTrailer = in.readString();
        mName = in.readString();

    }


    public static final Creator<Trailers> CREATOR = new Creator<Trailers>() {
        @Override
        public Trailers createFromParcel(Parcel source) {
            return new Trailers(source);
        }

        @Override
        public Trailers[] newArray(int i) {
            return new Trailers[i];
        }
    };
}
