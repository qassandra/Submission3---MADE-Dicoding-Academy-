package com.example.catalogmovieapi;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.catalogmovieapi.ItemFragments.MoviesFragment;
import com.example.catalogmovieapi.ItemFragments.TvShowsFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment;
                    switch (menuItem.getItemId()){
                        case R.id.nav_movies:
                            fragment = new MoviesFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout,fragment,fragment.getClass().getSimpleName()).commit();
                            return true;
                        case R.id.nav_tvshow:
                            fragment = new TvShowsFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState == null){
            navigationView.setSelectedItemId(R.id.nav_movies);
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

}
