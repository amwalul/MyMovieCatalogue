package com.example.amwa.mymoviecatalogue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.notification.SettingPref;
import com.example.amwa.mymoviecatalogue.fragment.FavoriteFragment;
import com.example.amwa.mymoviecatalogue.fragment.NowPlayingFragment;
import com.example.amwa.mymoviecatalogue.fragment.SearchFragment;
import com.example.amwa.mymoviecatalogue.fragment.UpcomingFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    private Fragment fragment = new NowPlayingFragment();
    private final String KEY_FRAGMENT = "KEY_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.now_playing_menu:
                        fragment = new NowPlayingFragment();
                        break;
                    case R.id.upcoming_menu:
                        fragment = new UpcomingFragment();
                        break;
                    case R.id.favorite_menu:
                        fragment = new FavoriteFragment();
                        break;
                    case R.id.search_menu:
                        fragment = new SearchFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });

        if (savedInstanceState != null) fragment = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
        loadFragment(fragment);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        getSupportFragmentManager().putFragment(savedInstanceState, KEY_FRAGMENT, fragment);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.action_change_settings) {
            intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        } else if (item.getItemId() == R.id.reminder_settings) {
            intent = new Intent(this, SettingPref.class);
        }

        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
