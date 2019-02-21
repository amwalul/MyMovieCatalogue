package com.example.amwa.mymoviecatalogue.notification;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.amwa.mymoviecatalogue.BuildConfig;
import com.example.amwa.mymoviecatalogue.entity.MovieItems;
import com.example.amwa.mymoviecatalogue.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SettingPref extends AppCompatPref {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        private RequestQueue requestQueue;
        ArrayList<MovieItems> list = new ArrayList<>();
        DailyReceiver dailyReceiver = new DailyReceiver();
        TodayReleaseReceiver todayReleaseReceiver = new TodayReleaseReceiver();
        SwitchPreference switchDaily, switchRelease;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            requestQueue = Volley.newRequestQueue(getActivity());

            switchDaily = (SwitchPreference) findPreference(getString(R.string.daily_key));
            switchDaily.setOnPreferenceChangeListener(this);

            switchRelease = (SwitchPreference) findPreference(getString(R.string.release_key));
            switchRelease.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            boolean value = (boolean) newValue;
            if (key.equals(getString(R.string.daily_key))) {
                if (value) {
                    dailyReceiver.setAlarm(getActivity());
                } else {
                    dailyReceiver.cancelAlarm(getActivity());
                }
            } else {
                if (value) {
                    setReleaseAlarm();
                } else {
                    todayReleaseReceiver.cancelAlarm(getActivity());
                }
            }
            return true;
        }

        public class GetMovieAsyncTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... strings) {
                getData(strings[0]);
                return null;
            }
        }

        private void setReleaseAlarm() {
            MainPreferenceFragment.GetMovieAsyncTask getDataAsync = new MainPreferenceFragment.GetMovieAsyncTask();
            getDataAsync.execute("https://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY + "&language=en-US");
        }

        public void getData(String url) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();
            final String today = dateFormat.format(date);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray("results");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            MovieItems movieItem = new MovieItems(data);

                            if (movieItem.getReleaseDate().equals(today)) {
                                list.add(movieItem);
                            }
                        }

                        todayReleaseReceiver.setAlarm(getActivity(), list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(request);
        }
    }
}
