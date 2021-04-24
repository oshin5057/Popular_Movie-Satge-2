package com.example.android.popularmoviefinalstage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmoviefinalstage.database.AppDatabase;
import com.example.android.popularmoviefinalstage.database.MovieEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getsInstance(this.getApplication());
        movies = appDatabase.movieDao().loadAllMovies();
    }

    public LiveData<List<MovieEntry>> getMovies(){
        return movies;
    }
}
