package com.example.android.popularmoviefinalstage.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "movie")
public class MovieEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int mMovieId;
    private double mMovieRate;
    private String mMoviePoster;
    private String mMovieTitle;
    private String mMovieOverView;
    private String mMovieDate;

    @Ignore
    public MovieEntry(int mMovieId, double mMovieRate, String mMoviePoster,
                      String mMovieTitle, String mMovieOverView, String mMovieDate){
        this.mMovieId = mMovieId;
        this.mMovieRate = mMovieRate;
        this.mMoviePoster = mMoviePoster;
        this.mMovieTitle = mMovieTitle;
        this.mMovieOverView = mMovieOverView;
        this.mMovieDate = mMovieDate;
    }

    public MovieEntry(int id, int mMovieId, double mMovieRate, String mMoviePoster,
                      String mMovieTitle, String mMovieOverView, String mMovieDate){
        this.id = id;
        this.mMovieId = mMovieId;
        this.mMovieRate = mMovieRate;
        this.mMoviePoster = mMoviePoster;
        this.mMovieTitle = mMovieTitle;
        this.mMovieOverView = mMovieOverView;
        this.mMovieDate = mMovieDate;
    }

    public int getId(){
        return id;
    }
    public int getMovieId(){
        return mMovieId;
    }
    public double getMovieRate(){
        return mMovieRate;
    }
    public String getMoviePoster(){
        return mMoviePoster;
    }
    public String getMovieTitle(){
        return mMovieTitle;
    }
    public String getMovieOverView(){
        return mMovieOverView;
    }
    public String getMovieDate(){
        return mMovieDate;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setMovieId(int mMovieId){
        this.mMovieId = mMovieId;
    }
    public void setMovieRate(double mMovieRate){
        this.mMovieRate = mMovieRate;
    }
    public void setMoviePoster(String mMoviePoster){
        this.mMoviePoster = mMoviePoster;
    }
    public void setMovieTitle(String mMovieTitle){
        this.mMovieTitle = mMovieTitle;
    }
    public void setMovieOverView(String mMovieOverView){
        this.mMovieOverView = mMovieOverView;
    }
    public void setMovieDate(String movieDate){
        this.mMovieDate = mMovieDate;
    }
}

