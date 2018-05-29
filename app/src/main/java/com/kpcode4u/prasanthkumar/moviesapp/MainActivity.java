package com.kpcode4u.prasanthkumar.moviesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kpcode4u.prasanthkumar.moviesapp.adapter.MovieAdapter;
import com.kpcode4u.prasanthkumar.moviesapp.api.Client;
import com.kpcode4u.prasanthkumar.moviesapp.api.Service;
import com.kpcode4u.prasanthkumar.moviesapp.model.Movie;
import com.kpcode4u.prasanthkumar.moviesapp.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
 //   private static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager_key";
    private MovieAdapter adapter;
    ProgressDialog pd;
    private List<Movie> movieList;

    @BindView(R.id.main_content) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler) RecyclerView mRecyclerView;

    public static final String TAG = MovieAdapter.class.getName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews(savedInstanceState);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews(savedInstanceState);
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
    }

/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", mRecyclerView.getAdapterPosition());
        super.onSaveInstanceState(outState);

    }
*/

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews(Bundle saveInstanceState) {
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching Movies..");
        pd.setCancelable(false);
        pd.show();

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        //scroll to existing position which exist before roatation
        if (saveInstanceState !=null){
            mRecyclerView.scrollToPosition(saveInstanceState.getInt("position"));

        }

        adapter.notifyDataSetChanged();

     //   statefulRecyclerView.setAdapter(adapter);
        checkSortOrder();
    }

    private void loadJSON() {

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);

            Call<MoviesResponse> call = api_Service.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> moviesList = response.body().getResults();
                    mRecyclerView.setAdapter(new MovieAdapter(getApplicationContext(), moviesList));

                    mRecyclerView.smoothScrollToPosition(0);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error in Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJSON1() {

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);

            Call<MoviesResponse> call = api_Service.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> moviesList = response.body().getResults();
                    mRecyclerView.setAdapter(new MovieAdapter(getApplicationContext(), moviesList));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error in Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "preferences updated");
        checkSortOrder();
    }
    private void checkSortOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular));

        if (sortOder.equals(this.getString(R.string.pref_most_popular))) {
            Log.d(LOG_TAG, "Sorting by Most Popular");
            loadJSON();
        } else {
            Log.d(LOG_TAG, "Sorting by Vote Average");
            loadJSON1();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieList.isEmpty()){
            checkSortOrder();
        }else {

        }
    }

}