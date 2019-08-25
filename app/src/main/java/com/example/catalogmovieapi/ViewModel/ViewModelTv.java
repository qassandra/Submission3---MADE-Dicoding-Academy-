package com.example.catalogmovieapi.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.catalogmovieapi.Model.TvShows;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewModelTv extends ViewModel {
    private MutableLiveData<ArrayList<TvShows>> listTv = new MutableLiveData<>();

    public void setListTv(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShows> listItems = new ArrayList<>();
        String API_KEY = "666a2970fcc09551a95e628f24baf8dd";
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte=2019-08-01";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject response = new JSONObject(result);
                    JSONArray list = response.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tvShows = list.getJSONObject(i);
                        TvShows tvItems = new TvShows(tvShows);
                        listItems.add(tvItems);
                    }
                    listTv.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<TvShows>> getTv(){
        return listTv;
    }
}
