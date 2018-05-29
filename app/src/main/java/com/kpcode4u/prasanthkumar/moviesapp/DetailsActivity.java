package com.kpcode4u.prasanthkumar.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
  //  @BindView(R.id.toolBar) Toolbar toolbar;
    @BindView(R.id.thumbnail_img_header) ImageView img;
    @BindView(R.id.title_details) TextView nameOfMovie;
    @BindView(R.id.plotSynopsis) TextView plotSynopsis;
    @BindView(R.id.user_Rating) TextView userRating;
    @BindView(R.id.release_dates) TextView releaseDate;
    @BindView(R.id.appbar) AppBarLayout appBarLayout;

    private static final  String posterPath_key ="poster_path";
    private static final  String originalTitle_key ="original_title";
    private static final  String overView_key ="overview";
    private static final  String rating_key ="vote_average";
    private static final  String release_date_key ="release_date";


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
            String thumbnail = getIntent().getExtras().getString(posterPath_key);
            String movieName = getIntent().getExtras().getString(originalTitle_key);
            String synopsis = getIntent().getExtras().getString(overView_key);
            Double vote_average = getIntent().getDoubleExtra(rating_key,0);
            String rating = Double.toString(vote_average);
          //  String rating = getIntent().getExtras().getString(rating_key);
            String dateOfRelease = getIntent().getExtras().getString(release_date_key);

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
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle(getIntent().getExtras().getString(originalTitle_key));
                    isShow = true;
                }
                else if(isShow){
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }

            }
        });
    }
}
