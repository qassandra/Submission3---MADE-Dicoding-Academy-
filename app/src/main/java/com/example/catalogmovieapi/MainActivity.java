package com.example.catalogmovieapi;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.catalogmovieapi.fragments.FavoriteMovieFragment;
import com.example.catalogmovieapi.fragments.FavoriteTvFragment;
import com.example.catalogmovieapi.fragments.TabLayoutFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CircleImageView profileCircleImageView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView view = findViewById(R.id.drawer_view);

        view.setNavigationItemSelectedListener(this);

        profileCircleImageView = findViewById(R.id.imageView);
        if (savedInstanceState == null){
            Fragment currentFragment = new TabLayoutFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,currentFragment).commit();
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.movies);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        drawer.removeDrawerListener(toggle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int id = menuItem.getItemId();
        if (id == R.id.home ){
            fragment = new TabLayoutFragment();
        } else if (id == R.id.movie){
            getSupportActionBar().setTitle(R.string.favorite_movies);
            fragment = new FavoriteMovieFragment();
        } else if (id == R.id.tv){
            getSupportActionBar().setTitle(R.string.favorite_tv_shows);
            fragment = new FavoriteTvFragment();
        }

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
