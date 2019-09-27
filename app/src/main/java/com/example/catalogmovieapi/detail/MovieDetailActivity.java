package com.example.catalogmovieapi.detail;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.model.Movies;

import java.util.Objects;

import static com.example.catalogmovieapi.db.MovieDbContract.Columns.AVERAGE;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.CONTENT_URI_MOVIE;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.COUNT;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.MOVIE_ID;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.OVERVIEW;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.POSTER;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.RELEASE_DATE;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.TITLE;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    TextView title, releaseDate, voteAverage, reviewRate, description, overview, reviewedBy;
    ImageView poster;
    ProgressBar progressBar;
    private Movies movies;
    public static final String EXTRA_ITEM = "extra_item";
    private int movieId;
    private int position;
    private boolean isFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.tv_movie_title);
        releaseDate = findViewById(R.id.tv_movie_release);
        voteAverage = findViewById(R.id.tv_movie_average);
        description = findViewById(R.id.tv_movie_desc);
        poster = findViewById(R.id.image_movie);
        progressBar = findViewById(R.id.progressBarDetail);
        overview = findViewById(R.id.overview);
        reviewRate = findViewById(R.id.tv_detail_reviewedRate);
        reviewedBy = findViewById(R.id.tv_detail_reviewedBy);
        movies = getIntent().getParcelableExtra(EXTRA_ITEM);
        movieId = movies.getMovieId();

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
                        movies = getIntent().getParcelableExtra(EXTRA_ITEM);


                        String url = "https://image.tmdb.org/t/p/w185" + movies.getMoviePhoto();
                        String people = Integer.toString(movies.getMoviePeople());
                        String reviewed = getString(R.string.reviewed) + " " + people + " " + getString(R.string.people);

                        title.setText(movies.getMovieTitle());
                        releaseDate.setText(movies.getMovieRelease());
                        voteAverage.setText(movies.getMovieAverage());
                        description.setText(movies.getMovieDescription());
                        Glide.with(MovieDetailActivity.this).load(url).into(poster);

                        overview.setText(R.string.overview);
                        reviewRate.setText(R.string.reviewRate);
                        reviewedBy.setText(reviewed);


                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }).start();


        if (movies != null){
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isFavorite = false;
        } else {
            movies = new Movies();
        }
        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movies = new Movies(cursor);
                cursor.close();
            }
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(movies.getMovieTitle());
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

        if (item.getItemId() == R.id.favorite){
            if (!isFavorite){
                isFavorite = true;
                item.setIcon(R.drawable.ic_favorite_white_24dp);
                Toast.makeText(MovieDetailActivity.this, "Add to Favorites", Toast.LENGTH_SHORT).show();
            } else {
                isFavorite = false;
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                Toast.makeText(MovieDetailActivity.this, "Deleted from Favorites", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();
            intent.putExtra(EXTRA_ITEM, movies);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();

            values.put(MOVIE_ID, movies.getMovieId());
            values.put(TITLE, movies.getMovieTitle());
            values.put(RELEASE_DATE, movies.getMovieRelease());
            values.put(OVERVIEW, movies.getMovieDescription());
            values.put(COUNT, movies.getMoviePeople());
            values.put(POSTER, movies.getMoviePhoto());
            values.put(AVERAGE, movies.getMovieAverage());

            if (isFavorite){
                getContentResolver().insert(CONTENT_URI_MOVIE, values);
            } else {
                intent.putExtra(EXTRA_POSITION, position);
                getContentResolver().delete(Objects.requireNonNull(getIntent().getData()), null, null);

            }
        } else if (item.getItemId() == R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isFavorite(int id) {
        Uri uri = CONTENT_URI_MOVIE.buildUpon().appendPath(String.valueOf(id)).build();
        @SuppressLint("Recycle")
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            return cursor.moveToFirst();
        }
        return true;
    }

}
