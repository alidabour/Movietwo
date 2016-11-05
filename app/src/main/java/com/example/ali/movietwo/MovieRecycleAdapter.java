package com.example.ali.movietwo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Dabour on 11/2/2016.
 */

public class MovieRecycleAdapter extends RecyclerView.Adapter<MovieRecycleAdapter.ViewHolder> {
    Context context;
    String[] postersURL;
    String[] titles;
    Movie[] movies;
    OnClickHandler onClickHandler;

    public interface OnClickHandler {
        void onClick(int position);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(Movie movie);
    }

    MovieRecycleAdapter(Context context, Movie[] movies) {
        this.context = context;
        this.movies = movies;
        //this.postersURL=postersURL;
        //this.titles=title;
        //this.onClickHandler = onClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String posterURL = "http://image.tmdb.org/t/p/w185/" + movies[position].getPoster_path();
        String title = movies[position].getTitle();
        holder.title.setText(title);
        Glide.with(context).load(posterURL).into(holder.poster);
        Log.v("Test", posterURL);
    }

    @Override
    public int getItemCount() {
        return movies.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("Test", "Image Clicked");
                    ((Callback) context).onItemSelected(movies[getAdapterPosition()]);
                }
            });
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.v("Test","Card Clicked");
                }
            });*/
        }


       /* @Override
        public void onClick(View view) {
            onClickHandler.onClick(getAdapterPosition());
            Log.v("Test","OnClick"+getAdapterPosition());
        }*/
    }

}
