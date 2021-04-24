package com.example.android.popularmoviefinalstage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(Context context){
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView authorTV;
        private ExpandableTextView reviewTV;
        private TextView toggleTV;

        public ViewHolder(View itemView){
            super(itemView);
            authorTV = itemView.findViewById(R.id.tv_author_name);
            reviewTV = itemView.findViewById(R.id.expandable_text_view);
            toggleTV = itemView.findViewById(R.id.tv_text_toggle);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        TextView authorTextView = holder.authorTV;
        final ExpandableTextView expandableTextView = holder.reviewTV;
        final TextView textToggle = holder.toggleTV;

        String reviewString = review.getReview();
        String adjusted = reviewString.replaceAll("(?m)^[ \t]*\r?\n", "");

        authorTextView.setText(review.getAuthor());
        expandableTextView.setText(adjusted);

        int length = adjusted.length();

        if (length<180){
            textToggle.setVisibility(View.INVISIBLE);
        }
        else {
            textToggle.setVisibility(View.VISIBLE);
        }

        expandableTextView.setAnimationDuration(750L);
        expandableTextView.setInterpolator(new OvershootInterpolator());

        textToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandableTextView.isExpanded()){
                    expandableTextView.collapse();
                    textToggle.setText(R.string.expand_view_more);
                }
                else {
                    expandableTextView.expand();
                    textToggle.setText(R.string.collapse);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) {
            return 0;
        }
        return mReviews.size();
    }

    public void setReviewData(List<Review> reviewData){
        mReviews = reviewData;
        notifyDataSetChanged();
    }
}
