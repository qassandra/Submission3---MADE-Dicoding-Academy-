package com.example.catalogmovieapi;

import android.database.Cursor;

public interface LoadTvCallback {
    void preExecute();

    void postExecute(Cursor tvShows);
}
