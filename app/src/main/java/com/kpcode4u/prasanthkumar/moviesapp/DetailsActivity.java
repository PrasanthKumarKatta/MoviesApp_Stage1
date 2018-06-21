package com.kpcode4u.prasanthkumar.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.kpcode4u.prasanthkumar.moviesapp.adapter.ReviewAdapter;
import com.kpcode4u.prasanthkumar.moviesapp.adapter.TrailerAdapter;
import com.kpcode4u.prasanthkumar.moviesapp.api.Client;
import com.kpcode4u.prasanthkumar.moviesapp.api.Service;
import com.kpcode4u.prasanthkumar.moviesapp.data.FavoriteContract;
import com.kpcode4u.prasanthkumar.moviesapp.data.FavoriteDBHelper;
import com.kpcode4u.prasanthkumar.moviesapp.data.MoviesContentProvider;
import com.kpcode4u.prasanthkumar.moviesapp.model.Movie;
import com.kpcode4u.prasanthkumar.moviesapp.model.Reviews;
import com.kpcode4u.prasanthkumar.moviesapp.model.ReviewsResponse;
import com.kpcode4u.prasanthkumar.moviesapp.model.Trailer;
import com.kpcode4u.prasanthkumar.moviesapp.model.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
  //  @BindView(R.id.toolBar) Toolbar toolbar;
    @BindView(R.id.backdrop_image) ImageView backDrop_img;
    @BindView(R.id.thumbnail_img_header) ImageView img;
    @BindView(R.id.title_details) TextView nameOfMovie;
    @BindView(R.id.plotSynopsis) TextView plotSynopsis;
    @BindView(R.id.user_Rating) TextView userRating;
    @BindView(R.id.release_dates) TextView releaseDate;
    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.recyclerView_trailer) RecyclerView recyclerView;
    @BindView(R.id.recyclerView_reviews) RecyclerView recyclerView1;
    @BindView(R.id.favorite_button) MaterialFavoriteButton materialFavoriteButton;


    private TrailerAdapter adapter;
    private List<Trailer> trailerList;

    private ReviewAdapter adapter_review;
    private List<Reviews> reviewsList;

    private FavoriteDBHelper favoriteDBHelper;
    private FavoriteDBHelper favDB = new FavoriteDBHelper(DetailsActivity.this);
    private Movie favorite;
    private final AppCompatActivity activity = DetailsActivity.this;

    private static final  String posterPath_key ="poster_path";
    private static final  String originalTitle_key ="original_title";
    private static final  String overView_key ="overview";
    private static final  String rating_key ="vote_average";
    private static final  String release_date_key ="release_date";
    private static final  String id_key ="id";
    private static final  String backDropImg_key ="backdropImg";
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        initCollapsingToolbar();

        Intent intent = getIntent();
        if (intent.hasExtra(originalTitle_key)){

            id = getIntent().getExtras().getInt(id_key);

            String thumbnail = getIntent().getExtras().getString(posterPath_key);
            String movieName = getIntent().getExtras().getString(originalTitle_key);
            String synopsis = getIntent().getExtras().getString(overView_key);
            Double vote_average = getIntent().getDoubleExtra(rating_key,0);
            String rating = Double.toString(vote_average);
          //  String rating = getIntent().getExtras().getString(rating_key);
            String dateOfRelease = getIntent().getExtras().getString(release_date_key);
            String backDropImg = getIntent().getExtras().getString(backDropImg_key);

            Picasso.with(this)
                    .load(""+backDropImg)
                    .placeholder(R.drawable.loading_gif)
                    .into(backDrop_img);

            Picasso.with(this)
                    .load(""+thumbnail)
                    .placeholder(R.drawable.loading_gif)
                    .into(img);
            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);

        }
        else {
            Toast.makeText(this, "No Data from API", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int movieId =getIntent().getExtras().getInt(id_key);
        if(favDB.search(movieId)){
            materialFavoriteButton.setFavorite(true);

        }
      materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                   // SharedPreferences.Editor editor = (SharedPreferences.Editor) getSharedPreferences("com.kpcode4u.prasanthkumar.moviesapp.DetailsActivity",MODE_PRIVATE);

                    Toast.makeText(DetailsActivity.this, "true condition", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("com.kpcode4u.prasanthkumar.moviesapp.DetailsActivity",MODE_PRIVATE).edit();
                    editor.putBoolean("FavoriteAdded",true);
                    editor.apply();

                 //   saveFavorite();
                    saveFav();

                    Snackbar.make(buttonView,"Add to Favorite",
                              Snackbar.LENGTH_SHORT).show();

                } else {

                    int movie_id = getIntent().getExtras().getInt(id_key);
/*

                     favoriteDBHelper = new FavoriteDBHelper(DetailsActivity.this);
                     favoriteDBHelper.deleteFavorite(movie_id);
*/

                    String stringId = Integer.toString(id);
                    int uri = getContentResolver().delete(MoviesContentProvider.CONTENT_URI, stringId,null);

                    SharedPreferences.Editor editor =
                             getSharedPreferences("com.kpcode4u.prasanthkumar.moviesapp.DetailsActivity",MODE_PRIVATE).edit();
                    editor.putBoolean("Favorite Removed",true);
                    editor.apply();

                Snackbar.make(buttonView,"Removed from Favorite",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        initViews();
        initViews1();

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0){
                    //collapsingToolbarLayout.setTitle(getIntent().getExtras().getString(originalTitle_key));
                    collapsingToolbarLayout.setTitle("Movie Details");
                    isShow = true;
                }
                else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }

            }
        });
    }
    private void initViews(){
        trailerList = new ArrayList<>();
        adapter = new TrailerAdapter(this, trailerList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.notifyDataSetChanged();

        loadJSON();

    }

    private void loadJSON() {
        int movie_id = getIntent().getExtras().getInt(id_key);

       // Toast.makeText(this, ""+movie_id, Toast.LENGTH_SHORT).show();

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(this, "Please get your API key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client();
            Service api_Service =client.getClient().create(Service.class);
            Call<TrailerResponse> call = api_Service.getMovieTrailer(movie_id,BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailer = response.body().getResults();

                  //  Toast.makeText(DetailsActivity.this, ""+trailer, Toast.LENGTH_SHORT).show();
                    Log.d("JsonResponse",trailer.toString());

                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(),trailer));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(DetailsActivity.this, "Error Fetching Trailer Data", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    private void initViews1() {
        reviewsList = new ArrayList<>();
        adapter_review = new ReviewAdapter(this, reviewsList);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView1.setAdapter(adapter_review);
        recyclerView1.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.notifyDataSetChanged();

        loadJSON1();

    }

    private void loadJSON1() {
        int movie_id = getIntent().getExtras().getInt(id_key);
       // Toast.makeText(this, ""+movie_id, Toast.LENGTH_SHORT).show();
        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(this, "Please get your API key from themoviedb.org", Toast.LENGTH_SHORT).show();
            }
            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            Call<ReviewsResponse> call = apiService.getMovieReviews(movie_id,BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {

                   // List<Reviews> reviews = response.body().getResults();
                    List<Reviews> reviews = response.body().getResults();

                   // Toast.makeText(DetailsActivity.this, ""+reviews, Toast.LENGTH_SHORT).show();

                    recyclerView1.setAdapter(new ReviewAdapter(getApplicationContext(),reviews));
                    recyclerView1.smoothScrollToPosition(0);

                }

                @Override
                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(DetailsActivity.this, "Error Fetching Trailer Data", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){

            Log.d("Error_DetailsActivity",e.getMessage());
            Toast.makeText(DetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public void saveFavorite(){

        favoriteDBHelper = new FavoriteDBHelper(activity);
        favorite = new Movie();
/*
        int movie_id = getIntent().getExtras().getInt(id_key);
        String originalTitle = getIntent().getExtras().getString(originalTitle_key);
        String rate = getIntent().getExtras().getString(rating_key);
        String poster = getIntent().getExtras().getString(posterPath_key);
        String backDrop = getIntent().getExtras().getString(backDropImg_key);
        String overView = getIntent().getExtras().getString(overView_key);

        favorite.setId(movie_id);
        //nameOfMovie.getText().toString().trim()
        favorite.setOriginal_title(originalTitle);
        favorite.setVoteAverage(Double.parseDouble(rate));
        favorite.setPosterPath(poster);
        favorite.setBackdrop_path(backDrop);
        //plotSynopsis.getText().toString().trim()
        favorite.setOverview(overView);

        favoriteDBHelper.addFavorite(favorite);
*/

    }

    private void saveFav() {
        Toast.makeText(activity, "saveFav method", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = favDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, getIntent().getExtras().getInt(id_key));
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, getIntent().getExtras().getString(originalTitle_key));
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, getIntent().getExtras().getDouble(rating_key));
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASEDATE,getIntent().getExtras().getString(release_date_key));
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, getIntent().getExtras().getString(posterPath_key));
         /* Movie movie = new Movie();
        try {
            InputStream inputStream = new URL(movie.getPosterPath()).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            //set bitmap value to picture
         //  movie.setPicture(Utility.getPictureByteOfArray(bitmap));
            movie.setPicture(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        //   cv.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_blob, Utility.getPictureByteOfArray(movie.getPicture()));

        cv.put(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_IMG, getIntent().getExtras().getString(backDropImg_key));
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, getIntent().getExtras().getString(overView_key));

        Uri uri = getContentResolver().insert(MoviesContentProvider.CONTENT_URI,cv);
        Toast.makeText(activity, "Table inserted", Toast.LENGTH_SHORT).show();
    }

}
