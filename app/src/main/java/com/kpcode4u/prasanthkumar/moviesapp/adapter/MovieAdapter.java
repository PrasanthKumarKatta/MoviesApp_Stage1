package com.kpcode4u.prasanthkumar.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kpcode4u.prasanthkumar.moviesapp.DetailsActivity;
import com.kpcode4u.prasanthkumar.moviesapp.OnClickListeners.ItemClickListener;
import com.kpcode4u.prasanthkumar.moviesapp.R;
import com.kpcode4u.prasanthkumar.moviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prasanth kumar on 25/05/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesInfo> {
   private Context context;
   private List<Movie> movieList;


 /*
    public MovieAdapter(Context context, List<Movie> movieList, MovieThumbNailClickListener mOnClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.mOnClickListener = mOnClickListener;
    }
 */

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MoviesInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_card,parent,false);
        return new MoviesInfo(v);
    }

    @Override
    public void onBindViewHolder(MoviesInfo holder, int position) {

      /*
        holder.title.setText(movieList.get(position).getOriginal_title());
        final String vote = Double.toString(movieList.get(position).getVoteAverage());
        holder.userRating.setText(vote+"/10");
      */
        Picasso.with(context)
                .load(movieList.get(position).getPosterPath()).placeholder(R.drawable.loading_gif).into(holder.thumbnail_Img);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(int posi) {
                   // Toast.makeText(context, ""+vote, Toast.LENGTH_SHORT).show();

                passDataToDetails(posi);

            }
        });
    }

    private void passDataToDetails(int posi) {
        if(posi != RecyclerView.NO_POSITION) {
            Movie clickedDataItems = movieList.get(posi);
            Intent i = new Intent(context, DetailsActivity.class);
            i.putExtra("original_title", movieList.get(posi).getOriginal_title());
            i.putExtra("poster_path", movieList.get(posi).getPosterPath());
            i.putExtra("overview", movieList.get(posi).getOverview());
            i.putExtra("vote_average", movieList.get(posi).getVoteAverage());
            i.putExtra("release_date", movieList.get(posi).getReleaseDate());
            i.putExtra("id",movieList.get(posi).getId());
            i.putExtra("backdropImg",movieList.get(posi).getBackdrop_path());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //implements View.OnClickListener
    public class MoviesInfo extends RecyclerView.ViewHolder  implements View.OnClickListener{
        //@BindViews({R.id.title_card, R.id.userRating_card})
      //  List<TextView> title, userRating;

   /*     @BindView(R.id.title_card) TextView title;
        @BindView(R.id.userRating_card) TextView userRating;
   */
   @BindView(R.id.thumbnail_img) ImageView thumbnail_Img;

        ItemClickListener itemClickListener;

        public MoviesInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

//            itemView.setOnClickListener(this);
              thumbnail_Img.setOnClickListener(this);
           /*
            thumbnail_Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = getAdapterPosition();
                    if (posi != RecyclerView.NO_POSITION){
                        Movie clickedDataItems = movieList.get(posi);
                        Intent i = new Intent(context, DetailsActivity.class);
                        i.putExtra("original_title",movieList.get(posi).getOriginal_title());
                        i.putExtra("poster_path",movieList.get(posi).getPosterPath());
                        i.putExtra("overview",movieList.get(posi).getOverview());
                        i.putExtra("vote_average",movieList.get(posi).getVoteAverage());
                        i.putExtra("release_date",movieList.get(posi).getReleaseDate());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        Toast.makeText(v.getContext(), "you clicked "+ clickedDataItems.getOriginal_title(), Toast.LENGTH_SHORT).show();
                    }
                }
             });

            */

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClickListener(this.getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener itemClickListener){

            this.itemClickListener = itemClickListener;
        }

     /*   @Override
        public void onClick(View v) {
            int clickedPosistion = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosistion);

        }
      */
    }
}
