package com.example.filipo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.filipo.data.DataSours;
import com.example.filipo.models.Movie;
import com.example.filipo.adapter.MovieAdapter;
import com.example.filipo.adapter.MovieItemClickListener;
import com.example.filipo.R;
import com.example.filipo.adapter.SliderPagerAdapter;
import com.example.filipo.models.slide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements MovieItemClickListener {

    private List<slide> slideList;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView NewMoviesRV;
    private RecyclerView PopularMoviesRV;
    private RequestQueue requestQueue;
    List<Movie> lstMoviePopular = new ArrayList<>();
    List<Movie> lstMovieNew = new ArrayList<>();
    String URL;
    String RESULT_URL;
    String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    String BASE_URL = "https://api.themoviedb.org/3/movie/";
    String API_KEY = "?api_key=ab66cda7c4961a2384e5c24949cf99fe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestQueue = Volley.newRequestQueue(this);


        iniView();
        iniSlider();
        iniNewMovie();
        iniPopularMovie();

    }

    public void iniPopularMovie() {
        RESULT_URL = "popular";
        URL = BASE_URL + RESULT_URL + API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject results = jsonArray.getJSONObject(i);
                        String title = results.getString("title");
                        String poster_path = results.getString("poster_path");
                        String backdrop_path = results.getString("backdrop_path");
                        String overview = results.getString("overview");
                        String release_date = results.getString("release_date");
                        lstMoviePopular.add(new Movie(title, IMAGE_URL + poster_path + API_KEY, IMAGE_URL + backdrop_path + API_KEY, release_date,overview));
                    }
                    ShowRecyclerviewPopular();
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
        requestQueue.add(jsonObjectRequest);
    }

    private void ShowRecyclerviewPopular() {
        MovieAdapter movieAdapterPopular = new MovieAdapter(this, lstMoviePopular, this);
        PopularMoviesRV.setAdapter(movieAdapterPopular);
        PopularMoviesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void iniNewMovie() {
        RESULT_URL = "now_playing";
        URL = BASE_URL + RESULT_URL + API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject results = jsonArray.getJSONObject(i);
                        String title = results.getString("title");
                        String poster_path = results.getString("poster_path");
                        String backdrop_path = results.getString("backdrop_path");
                        String overview = results.getString("overview");
                        String release_date = results.getString("release_date");
                        lstMovieNew.add(new Movie(title, IMAGE_URL + poster_path + API_KEY, IMAGE_URL + backdrop_path + API_KEY, release_date,overview));
                    }
                    ShowRecyclerviewNew();
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
        requestQueue.add(jsonObjectRequest);
    }

    private void ShowRecyclerviewNew() {
        MovieAdapter movieAdapterNew = new MovieAdapter(this, lstMovieNew, this);
        NewMoviesRV.setAdapter(movieAdapterNew);
        NewMoviesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void iniSlider() {
        //Show four new movies
        //get api rest image and title new film
        slideList = new ArrayList<>();
        RESULT_URL = "upcoming";
        URL = BASE_URL + RESULT_URL + API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 1; i <= 10; i++) {
                        JSONObject results = jsonArray.getJSONObject(i);
                        String title = results.getString("title");
                        String backdrop_path = results.getString("backdrop_path");
                        slideList.add(new slide(IMAGE_URL + backdrop_path + API_KEY, title));
                    }
                    ShowSlider();
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
        requestQueue.add(jsonObjectRequest);

        //setup time for slider
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        indicator.setupWithViewPager(sliderpager, true);
    }

    private void ShowSlider() {
        SliderPagerAdapter adapter = new SliderPagerAdapter(this, slideList);
        sliderpager.setAdapter(adapter);
    }

    private void iniView() {
        sliderpager = (ViewPager) findViewById(R.id.slider_pager);
        indicator = (TabLayout) findViewById(R.id.indicator);
        NewMoviesRV = (RecyclerView) findViewById(R.id.RV_movieNew);
        PopularMoviesRV = (RecyclerView) findViewById(R.id.Rv_movePopular);
    }

    @Override
    public void onMoveClick(Movie movie, ImageView movieImageView) {
        //there we send movie information to detail activity
        //also we'll create transition animation between the tow activity
        Intent intent = new Intent(this, MovieDetailActivity.class);
        //send movie information to detail activity
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("imgURL", movie.getThumbnail());
        intent.putExtra("imgCover", movie.getCoverPhoto());
        intent.putExtra("dName", movie.getReleaseDate());
        intent.putExtra("description", movie.getDescription());
        //create animation
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, movieImageView, "sharedName");
        startActivity(intent, options.toBundle());
    }

    class SliderTimer extends TimerTask {
        //
        @Override
        public void run() {
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem() < slideList.size() - 1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem() + 1);
                    } else
                        sliderpager.setCurrentItem(0);
                }
            });
        }
    }
}
