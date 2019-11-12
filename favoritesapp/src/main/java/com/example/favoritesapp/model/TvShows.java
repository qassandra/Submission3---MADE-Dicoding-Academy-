package com.example.favoritesapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.favoritesapp.contract.TvDbContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.example.favoritesapp.contract.TvDbContract.getColumnInt;
import static com.example.favoritesapp.contract.TvDbContract.getColumnString;

public class TvShows implements Parcelable {
    private int id;

    public TvShows(int tvId, String title, String releaseDate, String overview, String poster, String average, int count) {
        this.id = tvId;
        this.tvTitle = title;
        this.tvRelease = releaseDate;
        this.tvOverview = overview;
        this.tvPoster = poster;
        this.tvVote = average;
        this.tvVoteCount = count;
    }

    public TvShows() {

    }

    public int getId() {
        return id;
    }


    private String tvTitle;
    private String tvRelease;
    private String tvPoster;
    private String tvOverview;
    private String tvVote;
    private int tvVoteCount;

    public String getTvTitle() {
        return tvTitle;
    }

    public String getTvRelease() {
        return tvRelease;
    }

    public String getTvPoster() {
        return tvPoster;
    }

    public String getTvOverview() {
        return tvOverview;
    }

    public String getTvVote() {
        return tvVote;
    }

    public int getTvVoteCount() {
        return tvVoteCount;
    }

    public TvShows(JSONObject object){
        try {
            Integer tvId = object.getInt("id");
            String title = object.getString("name");
            String airDate = object.getString("first_air_date");
            double average = object.getDouble("vote_average");
            String voteAverage = new DecimalFormat("#.#").format(average);
            int voteCount = object.getInt("vote_count");
            String poster = object.getString("poster_path");
            String overview = object.getString("overview");

            this.tvTitle = title;
            this.tvRelease = airDate;
            this.tvVote = voteAverage;
            this.tvVoteCount = voteCount;
            this.tvPoster = poster;
            this.tvOverview = overview;
            this.id = tvId;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TvShows(Cursor cursor){
        this.id = getColumnInt(cursor, TvDbContract.Columns.TV_ID);
        this.tvTitle = getColumnString(cursor, TvDbContract.Columns.TITLE);
        this.tvOverview = getColumnString(cursor, TvDbContract.Columns.OVERVIEW);
        this.tvVote = getColumnString(cursor, TvDbContract.Columns.AVERAGE);
        this.tvPoster = getColumnString(cursor, TvDbContract.Columns.POSTER);
        this.tvRelease = getColumnString(cursor, TvDbContract.Columns.RELEASE_DATE);
        this.tvVoteCount = getColumnInt(cursor, TvDbContract.Columns.COUNT);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.tvTitle);
        dest.writeString(this.tvRelease);
        dest.writeString(this.tvPoster);
        dest.writeString(this.tvOverview);
        dest.writeString(this.tvVote);
        dest.writeInt(this.tvVoteCount);
    }

    private TvShows(Parcel in) {
        this.id = in.readInt();
        this.tvTitle = in.readString();
        this.tvRelease = in.readString();
        this.tvPoster = in.readString();
        this.tvOverview = in.readString();
        this.tvVote = in.readString();
        this.tvVoteCount = in.readInt();
    }

    public static final Creator<TvShows> CREATOR = new Creator<TvShows>() {
        @Override
        public TvShows createFromParcel(Parcel source) {
            return new TvShows(source);
        }

        @Override
        public TvShows[] newArray(int size) {
            return new TvShows[size];
        }
    };
}
