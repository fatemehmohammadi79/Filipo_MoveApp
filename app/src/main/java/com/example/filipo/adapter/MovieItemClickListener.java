package com.example.filipo.adapter;

//Create an interface class to define program rules

import android.widget.ImageView;

import com.example.filipo.models.Movie;

public interface MovieItemClickListener {
    //we will need the imageView to make the shared animation between the tow activity
    void onMoveClick(Movie movie, ImageView movieImageView);
}
