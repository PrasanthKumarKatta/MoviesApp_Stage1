package com.kpcode4u.prasanthkumar.moviesapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kpcode4u.prasanthkumar.moviesapp.ConnectToInternet.Internet;
import com.kpcode4u.prasanthkumar.moviesapp.adapter.MovieAdapter;
import com.kpcode4u.prasanthkumar.moviesapp.api.Client;
import com.kpcode4u.prasanthkumar.moviesapp.api.Service;
import com.kpcode4u.prasanthkumar.moviesapp.data.FavoriteDBHelper;
import com.kpcode4u.prasanthkumar.moviesapp.data.MoviesContentProvider;
import com.kpcode4u.prasanthkumar.moviesapp.model.Movie;
import com.kpcode4u.prasanthkumar.moviesapp.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
 //   private static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager_key";
    private MovieAdapter adapter;
    ProgressDialog pd;
    private List<Movie> movieList;

    @BindView(R.id.main_content) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler) RecyclerView mRecyclerView;

    public static final String TAG = MovieAdapter.class.getName();

    private AppCompatActivity activity = MainActivity.this;

  //  private FavoriteDBHelper favoriteDBHelper;
    private FavoriteDBHelper favoriteMoviesSQLiteDB;
    private Cursor cursor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        checkInternet();

    }

    private void checkInternet()
    {

        if (Internet.isNetworkAvailable(getApplicationContext())){

            initViews();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Need Permissions");
            builder.setMessage(getString(R.string.internetDialogErrorMsg));
            builder.setPositiveButton(getString(R.string.gotoSeetingsMsg), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent  i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }

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

    private void initViews() {

       /*
        pd.setMessage("Loading");
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      */
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);
        //adapter = new MovieAdapter(this, movieList,this);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

      /*  //scroll to existing position which exist before roatation
        if (saveInstanceState !=null){
            mRecyclerView.scrollToPosition(saveInstanceState.getInt("position"));

        }
      */
        adapter.notifyDataSetChanged();

    //    favoriteDBHelper = new FavoriteDBHelper(activity);

     //   statefulRecyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        checkSortOrder();
    }

    private void initViews2() {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this,movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

     /*
        favoriteDBHelper = new FavoriteDBHelper(activity);
        getAllFavorites();
     */
       // getAllFav();

    }

    @SuppressLint("NewApi")
    private void getAllFav() {

        SQLiteDatabase sqLiteDatabase = favoriteMoviesSQLiteDB.getReadableDatabase();
        cursor = getContentResolver().query(MoviesContentProvider.CONTENT_URI, null, null, null);
        while (cursor.moveToNext()){

            Movie fav_Movie =
                    new Movie(cursor.getString(2),
                            cursor.getString(5),
                            cursor.getString(7),
                            cursor.getDouble(3),
                            cursor.getString(4),
                            cursor.getInt(1),
                            cursor.getString(6));
            /*

            cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, 0
            getIntent().getExtras().getInt(id_key) );     1
            .getString(originalTitle_key));      2
            .getDouble(rating_key));            3
            .getString(release_date_key));     4
            .getString(posterPath_key));      5
            .getString(backDropImg_key));      6
            .getString(overView_key));           7

            */
            //String original_title, String posterPath, String overview,
            // Double voteAverage,String releaseDate, Integer id, String backdrop_path


            movieList.add(fav_Movie);
        }
        cursor.close();
        initViews2();

    }

    private void loadJSON() {

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();
              //  pd.dismiss();
                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);

            Call<MoviesResponse> call = api_Service.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> moviesList = response.body().getResults();

                   // mRecyclerView.setAdapter(new MovieAdapter(getApplicationContext(), moviesList,movieThumbNailClickListener));
                    mRecyclerView.setAdapter(new MovieAdapter(getApplicationContext(), moviesList));

                    mRecyclerView.smoothScrollToPosition(0);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                   // pd.dismiss();
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
               // pd.dismiss();
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
                 //   mRecyclerView.setAdapter(new MovieAdapter(getApplicationContext(), moviesList,movieThumbNailClickListener));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                 //   pd.dismiss();
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
        } else if (sortOder.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG,"Sorting by Favorite");
            getAllFav();
        }else {
            Log.d(LOG_TAG, "Sorting by Vote Average");
            loadJSON1();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkInternet();

        if (movieList.isEmpty()){
            checkSortOrder();
        }else {

        }
    }

    /*
    @Override
    public void onListItemClick(int clickedItemIndex) {

        String toastMessage = "Item #"+ clickedItemIndex +"clicked.";
        mToast = Toast.makeText( this, "", Toast.LENGTH_SHORT);
        mToast.show();

    }
    */

    private void getAllFavorites() {

  /*      SaveIntoDatabase database = new SaveIntoDatabase();
        database.execute();
*/
       // movieList.addAll(favoriteDBHelper.getAllFaavorite());
       // adapter.notifyDataSetChanged();


        /*
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
              // try {
                   movieList.clear();
                   movieList.addAll(favoriteDBHelper.getAllFaavorite());
               *//*
               }catch (Exception e){
                   Log.d("ERROR",e.getMessage());
                   Toast.makeText(activity, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
               }*//*
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
        */
    }

/*    public class SaveIntoDatabase extends AsyncTask<Movie,Movie,Boolean>{


        @Override
        protected Boolean doInBackground(Movie... movies) {
           try{
               movieList.clear();
             //  movieList.addAll(favoriteDBHelper.getAllFaavorite());

           }catch (Exception e){
               Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
           }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            adapter.notifyDataSetChanged();

        }
    }
    */

}