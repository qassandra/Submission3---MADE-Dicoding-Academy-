package com.example.catalogmovieapi.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class Movies implements Parcelable {
    private String movieTitle;
    private String movieRelease;
    private String movieDescription;
    private String moviePhoto;

    public String getMovieAverage() {
        return movieAverage;
    }

    public void setMovieAverage(String movieAverage) {
        this.movieAverage = movieAverage;
    }

    public int getMoviePeople() {
        return moviePeople;
    }

    public void setMoviePeople(int moviePeople) {
        this.moviePeople = moviePeople;
    }

    private String movieAverage;
    private int moviePeople;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public void setMovieRelease(String movieRelease) {
        this.movieRelease = movieRelease;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMoviePhoto() {
        return moviePhoto;
    }

    public void setMoviePhoto(String moviePhoto) {
        this.moviePhoto = moviePhoto;
    }


    public Movies(JSONObject object){
        try {
            String movieTitle = object.getString("original_title");
            String movieRelease = object.getString("release_date");
            String movieDescription = object.getString("overview");
            String moviePhoto = object.getString("poster_path");
            double vote = object.getDouble("vote_average");
            String movieAverage = new DecimalFormat("#.#").format(vote);
            int people = object.getInt("vote_count");


            this.movieAverage = movieAverage;
            this.moviePeople = people;
            this.movieTitle = movieTitle;
            this.movieRelease = movieRelease;
            this.movieDescription = movieDescription;
            this.moviePhoto = moviePhoto;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieTitle);
        dest.writeString(this.movieRelease);
        dest.writeString(this.movieDescription);
        dest.writeString(this.moviePhoto);
        dest.writeInt(Integer.parseInt(this.movieAverage));
        dest.writeInt(this.moviePeople);
    }

    private Movies(Parcel in) {
        this.movieTitle = in.readString();
        this.movieRelease = in.readString();
        this.movieDescription = in.readString();
        this.moviePhoto = in.readString();
        this.movieAverage = String.valueOf(in.readInt());
        this.moviePeople = in.readInt();
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
