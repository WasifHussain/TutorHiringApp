package com.example.projectii.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.TutorModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DecimalFormat;

public class TopTutorAdapter extends FirestoreRecyclerAdapter<TutorModel, TopTutorAdapter.myViewHolder> {
    public TopTutorAdapter(@NonNull FirestoreRecyclerOptions<TutorModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull TutorModel tutorModel) {
        holder.tv.setText(tutorModel.getFullName());
        holder.tv1.setText(tutorModel.getAddress());
        holder.tv2.setText(tutorModel.getSubjects());
        holder.tv4.setText(tutorModel.getLevel());
        holder.tv3.setText(String.valueOf(tutorModel.getFees()));
        holder.tv5.setText(new DecimalFormat("#.#").format(tutorModel.getAvgRating()));
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tutor, parent, false);
        return new TopTutorAdapter.myViewHolder(convertView);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2, tv3, tv4, tv5;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.fullName);
            tv1 = itemView.findViewById(R.id.address);
            tv2 = itemView.findViewById(R.id.subject);
            tv3 = itemView.findViewById(R.id.fees);
            tv4 = itemView.findViewById(R.id.level);
            tv5 = itemView.findViewById(R.id.ratings);
        }
    }
}
