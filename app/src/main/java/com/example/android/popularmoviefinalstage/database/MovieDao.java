package com.example.android.popularmoviefinalstage.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("select * from movie order by id")
    LiveData<List<MovieEntry>> loadAllMovies();

    @Query("select * from movie where mMovieId = :movieId")
    MovieEntry loadMovieById (int movieId);

    @Insert
    void insertMovie (MovieEntry movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movieEntry);

    @Delete
    void delete(MovieEntry movieEntry);

}
