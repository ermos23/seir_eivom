package com.example.seireivom.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seireivom.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    // Widgets
    TextView title, release_date, duration;
    ImageView imageView;
    RatingBar ratingBar;

    // Click Listener
    OnMovieListener onMovieListener;




    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);


        this.onMovieListener = onMovieListener;


        imageView = itemView.findViewById(R.id.movie_img);
        ratingBar = itemView.findViewById(R.id.rating_bar);

        itemView.setOnClickListener(this);

    }

    public MovieViewHolder(View view) {
        super(view);
    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
