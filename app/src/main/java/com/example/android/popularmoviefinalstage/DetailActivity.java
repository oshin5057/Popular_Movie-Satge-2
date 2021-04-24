package com.example.android.popularmoviefinalstage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.loader.app.LoaderManager;


import com.example.android.popularmoviefinalstage.database.AppDatabase;
import com.example.android.popularmoviefinalstage.database.MovieEntry;
import com.example.android.popularmoviefinalstage.utilities.NetworkUtils;
import com.example.android.popularmoviefinalstage.utilities.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>
        ,TrailerAdapter.ListItemClickListener{

    private ImageView mPosterShowImageView;

    private TextView mRateTextView;
    private TextView mReleaseDateTextView;
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private ImageButton mFavoriteButton;
    private TextView mNoReviewMessageTextView;

    private RecyclerView reviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    private RecyclerView trailerRecyclerVew;
    private TrailerAdapter mTrailerAdapter;

    private TextView mNetworkConnectionMessage;
    private TextView mNoTrailersMessage;

    private NetworkInfo networkInfo;
    private Movie movie;
    private AppDatabase mDb;

    private boolean isFavorite = true;

    private static final int MOVIE_TRAILER_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterShowImageView = (ImageView)findViewById(R.id.iv_show_poster);
        mRateTextView = (TextView)findViewById(R.id.tv_users_rating);
        mReleaseDateTextView =(TextView)findViewById(R.id.tv_release_date);
        mTitleTextView = (TextView)findViewById(R.id.tv_movie_tittle);
        mOverviewTextView = (TextView)findViewById(R.id.tv_overview);
        mNoReviewMessageTextView = (TextView) findViewById(R.id.tv_no_reviews_message);
        mFavoriteButton = (ImageButton)findViewById(R.id.favorite_button);
        mNetworkConnectionMessage = (TextView) findViewById(R.id.tv_no_network_connection_review_message);
        mNoTrailersMessage = (TextView) findViewById(R.id.no_trailers_message);

        final Intent intent = getIntent();
        movie = intent.getParcelableExtra("movie");

        final int movieId = movie.getMovieId();
        final double rate = movie.getMovieRate();
        final String date = movie.getMovieDate();
        final String tittle = movie.getMovieTitle();
        final String overview = movie.getMovieOverview();
        final String poster = movie.getMoviePoster();

        String posterUrl = MovieAdapter.getImageUri(movie);
        Picasso.with(this)
                .load(posterUrl)
                .error(R.drawable.ic_image_not_available)
                .into(mPosterShowImageView);

        mRateTextView.setText(Double.toString(rate));
        mReleaseDateTextView.setText(date);
        mTitleTextView.setText(tittle);
        mOverviewTextView.setText(overview);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            mNetworkConnectionMessage.setVisibility(View.GONE);
            mNoTrailersMessage.setVisibility(View.GONE);

            LoaderManager.LoaderCallbacks<List<String>> callbacks = DetailActivity.this;
            getSupportLoaderManager().initLoader(MOVIE_TRAILER_LOADER_ID, null, callbacks);
            reviewsAsyncTask.execute();
        }
        else {
            mNetworkConnectionMessage.setVisibility(View.VISIBLE);
            mNoTrailersMessage.setVisibility(View.VISIBLE);
        }

        mDb = AppDatabase.getsInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final MovieEntry checkFavoriteMovieEntry = mDb.movieDao().loadMovieById(movieId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (checkFavoriteMovieEntry == null){
                            isFavorite = false;
                            mFavoriteButton.setBackground(getDrawable(R.drawable.favorite_unfilled));
                        }
                    }
                });
            }
        });

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite){
                    final MovieEntry newMovieEntry = new MovieEntry(movieId, rate, poster, tittle, overview, date);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.movieDao().insertMovie(newMovieEntry);
                        }
                    });
                    mFavoriteButton.setBackground(getDrawable(R.drawable.favorite_filled));
                    isFavorite = true;
                    Toast.makeText(DetailActivity.this, "Added to your favorite list", Toast.LENGTH_SHORT).show();
                }
                else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            MovieEntry movieEntry = mDb.movieDao().loadMovieById(movieId);
                            mDb.movieDao().delete(movieEntry);
                        }
                    });
                    isFavorite = false;
                    mFavoriteButton.setBackground(getDrawable(R.drawable.favorite_unfilled));
                    Toast.makeText(DetailActivity.this, "Removed from your favorite list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reviewRecyclerView = findViewById(R.id.rv_review);
        mReviewAdapter = new ReviewAdapter(this);
        reviewRecyclerView.setAdapter(mReviewAdapter);
        reviewRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);

        trailerRecyclerVew = findViewById(R.id.rv_trailer);
        mTrailerAdapter = new TrailerAdapter(this, this);
        trailerRecyclerVew.setAdapter(mTrailerAdapter);
        trailerRecyclerVew.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerTrailer =
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        trailerRecyclerVew.setLayoutManager(linearLayoutManagerTrailer);
    }

    @Override
    public Loader<List<String>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<List<String>>(this) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Nullable
            @Override
            public List<String> loadInBackground() {
                String id = Integer.toString(movie.getMovieId());
                URL trailerUrl = NetworkUtils.buildTrailerRequestUrl(id);

                String trailerJsonResponse = null;
                try {
                    trailerJsonResponse = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
                }
                catch (IOException e1){
                    e1.printStackTrace();
                }

                List<String> youtubeKeys;
                try {
                    youtubeKeys = OpenMovieJsonUtils.getMovieYoutubeKeyFromJson(trailerJsonResponse);

                    return youtubeKeys;
                }
                catch (JSONException e1){
                    e1.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<List<String>> loader, List<String> data) {
        mTrailerAdapter.setTrailerData(data);
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<List<String>> loader) {

    }

    private AsyncTask<Void, Void, List<Review>> reviewsAsyncTask = new AsyncTask<Void, Void, List<Review>>() {
        @Override
        protected List<Review> doInBackground(Void... voids) {

            String id = Integer.toString(movie.getMovieId());
            URL reviewUrl = NetworkUtils.buildReviewsRequestUrl(id);

            String reviewJsonResponse = null;

            try {
                reviewJsonResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
            }
            catch (IOException e){
                e.printStackTrace();
            }

            List<Review> reviews = null;
            try {
                reviews = OpenMovieJsonUtils.getMovieReviewFromJson(reviewJsonResponse);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews.size() == 0 || reviews == null){
                showNoReviewsMessage();
            }
            else {
                showReviews();
                mReviewAdapter.setReviewData(reviews);
            }
        }
    };

    void showReviews(){
        reviewRecyclerView.setVisibility(View.VISIBLE);
        mNoReviewMessageTextView.setVisibility(View.GONE);
    }

    void showNoReviewsMessage(){
        reviewRecyclerView.setVisibility(View.GONE);
        mNoReviewMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(String youtubeKey) {
        String videoPath = "https://www.youtube.com/watch?v=" + youtubeKey;
        Uri youTubeUri = Uri.parse(videoPath);
        Intent openYouTubeIntent = new Intent(Intent.ACTION_VIEW, youTubeUri);
        startActivity(openYouTubeIntent);
    }
}

