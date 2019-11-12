package com.example.catalogmovieapi.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.catalogmovieapi.contract.MovieDbContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.example.catalogmovieapi.contract.MovieDbContract.getColumnInt;
import static com.example.catalogmovieapi.contract.MovieDbContract.getColumnString;

public class Movies implements Parcelable {
    private String movieTitle;
    private String movieRelease;
    private String movieDescription;
    private String moviePhoto;

    public Movies(int movieId, String title, String releaseDate, String overview, String poster, String average, int count) {
        this.movieId = movieId;
        this.movieTitle = title;
        this.movieRelease = releaseDate;
        this.moviePhoto = poster;
        this.movieAverage = average;
        this.movieDescription = overview;
        this.moviePeople = count;
    }

    public Movies() {

    }

    public int getMovieId() {
        return movieId;
    }


    private int movieId;

    public String getMovieAverage() {
        return movieAverage;
    }

    public int getMoviePeople() {
        return moviePeople;
    }

    private String movieAverage;
    private int moviePeople;

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public String getMoviePhoto() {
        return moviePhoto;
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
            Integer id = object.getInt("id");


            this.movieAverage = movieAverage;
            this.moviePeople = people;
            this.movieTitle = movieTitle;
            this.movieRelease = movieRelease;
            this.movieDescription = movieDescription;
            this.moviePhoto = moviePhoto;
            this.movieId = id;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
   public Movies(Cursor cursor){
        this.movieId = getColumnInt(cursor, MovieDbContract.Columns.MOVIE_ID);
        this.movieTitle = getColumnString(cursor, MovieDbContract.Columns.TITLE);
        this.movieDescription = getColumnString(cursor, MovieDbContract.Columns.OVERVIEW);
        this.movieAverage = getColumnString(cursor, MovieDbContract.Columns.AVERAGE);
        this.moviePhoto = getColumnString(cursor, MovieDbContract.Columns.POSTER);
        this.movieRelease = getColumnString(cursor, MovieDbContract.Columns.RELEASE_DATE);
        this.moviePeople = getColumnInt(cursor, MovieDbContract.Columns.COUNT);
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
        dest.writeInt(this.movieId);
        dest.writeString(this.movieAverage);
        dest.writeInt(this.moviePeople);
    }

    private Movies(Parcel in) {
        this.movieTitle = in.readString();
        this.movieRelease = in.readString();
        this.movieDescription = in.readString();
        this.moviePhoto = in.readString();
        this.movieId = in.readInt();
        this.movieAverage = in.readString();
        this.moviePeople = in.readInt();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
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
