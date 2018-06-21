package com.kpcode4u.prasanthkumar.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpcode4u.prasanthkumar.moviesapp.OnClickListeners.ItemClickListener;
import com.kpcode4u.prasanthkumar.moviesapp.R;
import com.kpcode4u.prasanthkumar.moviesapp.model.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prasanth kumar on 08/06/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context context;
    private List<Trailer> trailerList;


    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_card,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, final int position) {

   //     Toast.makeText(context, ""+trailerList.get(position).getName(), Toast.LENGTH_SHORT).show();
        // holder.trailer_Title.setText(trailerList.get(position).getName());
        holder.trailer_Title.setText(""+trailerList.get(position).getName());

      /*   holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(int posi) {
              //shareTriler(position);
            }
        });
      */
    }

    public void shareTriler(int pos) {
         //TrailerAdapter activity = TrailerAdapter.this;

        /*ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setChooserTitle("Share this Trailer with:")
                .setText("https://www.youtube.com/watch?v="+trailerList.get(pos).getKey())
                .startChooser();
*/
    }

    private void passTrailerDataToYoutube(int posi) {
        if (posi !=RecyclerView.NO_POSITION){
            Trailer clickedDataItems = trailerList.get(posi);
            String videoId = trailerList.get(posi).getKey();
            Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
            intent.putExtra("VIDEO_ID", videoId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        //    Toast.makeText(context, "you Clicked"+clickedDataItems.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    //implements View.OnClickListener
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.trailer_thumbnail_img) ImageView imageView;
        @BindView(R.id.title_trailer) TextView trailer_Title;
        @BindView(R.id.share_trailer) ImageView share_trailer_tv;
        ItemClickListener itemClickListener;


        //  ItemClickListener itemClickListener;

        public TrailerViewHolder(View itemView) {
            super(itemView);
           ButterKnife.bind(this,itemView);

           // itemView.setOnClickListener(this);

            trailer_Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= getAdapterPosition();
                    passTrailerDataToYoutube(pos);
                }
            });
            share_trailer_tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClickListener(this.getLayoutPosition());

        }
        public void setItemClickListener(ItemClickListener itemClickListener){

            this.itemClickListener = itemClickListener;
        }
    }
}
