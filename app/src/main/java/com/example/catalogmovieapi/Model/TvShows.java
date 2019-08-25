package com.example.catalogmovieapi.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class TvShows implements Parcelable {
    private String tvTitle;
    private String tvRelease;
    private String tvPoster;
    private String tvOverview;
    private String tvVote;
    private int tvVoteCount;

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvRelease() {
        return tvRelease;
    }

    public void setTvRelease(String tvRelease) {
        this.tvRelease = tvRelease;
    }

    public String getTvPoster() {
        return tvPoster;
    }

    public void setTvPoster(String tvPoster) {
        this.tvPoster = tvPoster;
    }

    public String getTvOverview() {
        return tvOverview;
    }

    public void setTvOverview(String tvOverview) {
        this.tvOverview = tvOverview;
    }

    public String getTvVote() {
        return tvVote;
    }

    public void setTvVote(String tvVote) {
        this.tvVote = tvVote;
    }

    public int getTvVoteCount() {
        return tvVoteCount;
    }

    public void setTvVoteCount(int tvVoteCount) {
        this.tvVoteCount = tvVoteCount;
    }

    public TvShows(JSONObject object){
        try {
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
        dest.writeString(this.tvTitle);
        dest.writeString(this.tvRelease);
        dest.writeString(this.tvPoster);
        dest.writeString(this.tvOverview);
        dest.writeString(this.tvVote);
        dest.writeInt(this.tvVoteCount);
    }

    private TvShows(Parcel in) {
        this.tvTitle = in.readString();
        this.tvRelease = in.readString();
        this.tvPoster = in.readString();
        this.tvOverview = in.readString();
        this.tvVote = in.readString();
        this.tvVoteCount = in.readInt();
    }

    public static final Parcelable.Creator<TvShows> CREATOR = new Parcelable.Creator<TvShows>() {
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
