package com.example.catalogmovieapi.activity;

import android.content.Intent;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.example.catalogmovieapi.R;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.catalogmovieapi.fragments.FavoriteMovieFragment;
import com.example.catalogmovieapi.fragments.FavoriteTvFragment;
import com.example.catalogmovieapi.fragments.TabLayoutFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    CircleImageView profileCircleImageView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    String index = "0";


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
        switch (item.getItemId()){
            case R.id.action_language:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
            case R.id.searchView:
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);
            case R.id.notifications:
                startActivity(new Intent(this, SettingsActivity.class));
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra(SearchActivity.KEYWORD, s);
        intent.putExtra(SearchActivity.INDEX, index);
        Log.i("INDEX", " " + index);
    startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
