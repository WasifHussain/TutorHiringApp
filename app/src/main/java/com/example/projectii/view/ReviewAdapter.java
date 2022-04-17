package com.example.projectii.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.ReviewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.FirebaseFirestore;

public class ReviewAdapter extends FirestoreRecyclerAdapter<ReviewModel, ReviewAdapter.ReviewViewHolder> {
    Context c;
    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<ReviewModel> options, Context context) {
        super(options);
        c = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position, @NonNull ReviewModel model) {
        holder.tv.setText(model.getLearnerName());
        holder.tv1.setText(model.getCreatedAt());
        holder.tv2.setText(model.getReview());
        holder.ratingBar.setRating(model.getRating());
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_review_rating, parent, false);
        return new ReviewAdapter.ReviewViewHolder(convertView);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2;
        RatingBar ratingBar;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.reviewer);
            tv1 = itemView.findViewById(R.id.datetime);
            tv2 = itemView.findViewById(R.id.review);
            ratingBar = itemView.findViewById(R.id.ratings);
        }
    }
}
