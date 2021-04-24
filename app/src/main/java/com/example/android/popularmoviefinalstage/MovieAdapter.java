package com.example.android.popularmoviefinalstage;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> mMovie;
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(Movie clickedMovie);
    }

    public MovieAdapter(Context context, ListItemClickListener listener){
        this.mContext = context;
        this.mOnClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView poster;
        public TextView posterTitle;

        public ViewHolder(View itemView){
            super(itemView);
            poster = itemView.findViewById(R.id.iv_movie_poster);
            posterTitle = itemView.findViewById(R.id.tv_movie_poster_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Movie clickedItem = mMovie.get(clickedPosition);
            mOnClickListener.onListItemClick(clickedItem);
        }
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_poster, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Movie movie = mMovie.get(position);
        ImageView posterImageView = holder.poster;
        TextView titleTextView = holder.posterTitle;
        titleTextView.setText(movie.getMovieTitle());
        String posterUri = getImageUri(movie);

        Picasso.with(mContext)
                .load(posterUri)
                .error(R.drawable.ic_image_not_available)
                .into(posterImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovie == null){
            return 0;
        }
        return mMovie.size();
    }

    public void setMovieData(List<Movie> movieData){
        mMovie = movieData;
        notifyDataSetChanged();
    }

    public static String getImageUri(Movie movie){

        final String BASE_URL = "image.tmdb.org";
        final String SIZE = "w500";
        String posterPath = movie.getMoviePoster();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(BASE_URL)
                .appendPath("t")
                .appendPath("p")
                .appendPath(SIZE)
                .appendEncodedPath(posterPath);

        String myUrl = builder.build().toString();
        return myUrl;
    }
}

