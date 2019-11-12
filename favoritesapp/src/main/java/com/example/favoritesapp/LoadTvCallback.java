package com.example.favoritesapp;

import android.database.Cursor;

public interface LoadTvCallback {
    void preExecute();

    void postExecute(Cursor tvShows);
}
