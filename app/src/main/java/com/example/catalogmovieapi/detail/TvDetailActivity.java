package com.example.catalogmovieapi.detail;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.model.TvShows;

import java.util.Objects;

import static com.example.catalogmovieapi.widget.FavoriteWidget.sendRefreshBroadcast;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.AVERAGE;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.COUNT;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.TV_ID;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.OVERVIEW;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.POSTER;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.RELEASE_DATE;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.TITLE;
import static com.example.catalogmovieapi.contract.TvDbContract.Columns.CONTENT_URI_TV;


public class TvDetailActivity extends AppCompatActivity {
    TextView title, releaseDate, voteAverage, reviewedBy, overview, description, reviewRate;
    ImageView poster;
    ProgressBar progressBar;
    private TvShows tvShows;

    public static String EXTRA_ITEM = "extra_item";
    public static String EXTRA_POSITION = "extra_position";
    private int movieId;
    private int position;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.tv_show_title);
        releaseDate = findViewById(R.id.tv_show_release);
        reviewRate = findViewById(R.id.tv_detail_reviewedRate);
        voteAverage = findViewById(R.id.tv_show_average);
        reviewedBy = findViewById(R.id.tv_detail_reviewedBy);
        overview = findViewById(R.id.overview);
        description = findViewById(R.id.tv_show_desc);
        poster = findViewById(R.id.image_tv);
        tvShows = getIntent().getParcelableExtra(EXTRA_ITEM);
        movieId = tvShows.getId();

        progressBar = findViewById(R.id.progressBarDetail);
        progressBar.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String url = "https://image.tmdb.org/t/p/w185" + tvShows.getTvPoster();
                        String people = Integer.toString(tvShows.getTvVoteCount());
                        String reviewed = getString(R.string.reviewed) + " " + people + " " + getString(R.string.people);

                        title.setText(tvShows.getTvTitle());
                        releaseDate.setText(tvShows.getTvRelease());
                        voteAverage.setText(tvShows.getTvVote());
                        description.setText(tvShows.getTvOverview());
                        Glide.with(TvDetailActivity.this).load(url).into(poster);

                        overview.setText(R.string.overview);
                        reviewRate.setText(R.string.reviewRate);
                        reviewedBy.setText(reviewed);


                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }).start();
        if (tvShows != null){
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isFavorite = false;
        } else {
            tvShows = new TvShows();
        }
        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) tvShows = new TvShows(cursor);
                cursor.close();
            }
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(tvShows.getTvTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isFavorite(movieId)){
            isFavorite = true;
            menu.getItem(0).setIcon(R.drawable.ic_favorite_white_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        invalidateOptionsMenu();

        if (item.getItemId() == R.id.favorite) {
            if (!isFavorite) {
                isFavorite = true;
                item.setIcon(R.drawable.ic_favorite_white_24dp);
                Toast.makeText(TvDetailActivity.this, "Add to Favorites", Toast.LENGTH_SHORT).show();
            } else {
                isFavorite = false;
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                Toast.makeText(TvDetailActivity.this, "Deleted from Favorites", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();
            intent.putExtra(EXTRA_ITEM, tvShows);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();

            values.put(TV_ID, tvShows.getId());
            values.put(TITLE, tvShows.getTvTitle());
            values.put(RELEASE_DATE, tvShows.getTvRelease());
            values.put(OVERVIEW, tvShows.getTvOverview());
            values.put(COUNT, tvShows.getTvVoteCount());
            values.put(POSTER, tvShows.getTvPoster());
            values.put(AVERAGE, tvShows.getTvVote());

            if (isFavorite) {
                getContentResolver().insert(CONTENT_URI_TV, values);
            } else {
                intent.putExtra(EXTRA_POSITION, position);
                getContentResolver().delete(Objects.requireNonNull(getIntent().getData()), null, null);

            }
            sendRefreshBroadcast(getApplicationContext());

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isFavorite(int id) {
        Uri uri = CONTENT_URI_TV.buildUpon().appendPath(String.valueOf(id)).build();
        @SuppressLint("Recycle")
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            return cursor.moveToFirst();
        }
        return true;
    }

}
