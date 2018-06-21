package com.kpcode4u.prasanthkumar.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kpcode4u.prasanthkumar.moviesapp.R;
import com.kpcode4u.prasanthkumar.moviesapp.model.Reviews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prasanth kumar on 10/06/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewInfo> {

    private Context context;
    private List<Reviews> reviewsList;

    public ReviewAdapter(Context context, List<Reviews> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewAdapter.ReviewInfo onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.review_card,parent,false);
        return new ReviewInfo(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewInfo holder, int position) {
        if (reviewsList.get(position).getContent().equals(" "))
        {
            holder.review.setText("No Reviews Found");
        }else {
            holder.review.setText(reviewsList.get(position).getContent());
            holder.review_author.setText("Author: " + reviewsList.get(position).getAuthor());
            //  Toast.makeText(context, ""+reviewsList.get(position).getContent(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewInfo extends RecyclerView.ViewHolder {

        @BindView(R.id.reviews_id) TextView review;
        @BindView(R.id.review_author) TextView review_author;
       // TextView review;

        public ReviewInfo(View itemView) {
            super(itemView);
             ButterKnife.bind(this,itemView);
         //   review = itemView.findViewById(R.id.reviews_id);

        }
    }
}
