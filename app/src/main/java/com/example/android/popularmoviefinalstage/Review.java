package com.example.android.popularmoviefinalstage;

public class Review {

    private String mReview;
    private String mAuthor;

    public Review(String mReview, String mAuthor){
        this.mReview = mReview;
        this.mAuthor = mAuthor;
    }

    public String getReview(){
        return mReview;
    }
    public String getAuthor(){
        return mAuthor;
    }

    public void setReview(String mReview){
        this.mReview = mReview;
    }
    public void setAuthor(String mAuthor){
        this.mAuthor = mAuthor;
    }
}
