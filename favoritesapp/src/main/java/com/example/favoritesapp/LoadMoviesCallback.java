package com.example.favoritesapp;

import android.database.Cursor;

public interface LoadMoviesCallback {
    void preExecute();

    void postExecute(Cursor movies);
}
