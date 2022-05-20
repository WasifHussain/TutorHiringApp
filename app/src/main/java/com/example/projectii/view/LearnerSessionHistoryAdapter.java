package com.example.projectii.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.SessionModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LearnerSessionHistoryAdapter extends FirestoreRecyclerAdapter<SessionModel, LearnerSessionHistoryAdapter.myviewholder> {
    Context c;

    public LearnerSessionHistoryAdapter(@NonNull FirestoreRecyclerOptions<SessionModel> options, Context context) {
        super(options);
        c = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull SessionModel sessionModel) {
        holder.tv.setText(sessionModel.getStart_date());
        holder.tv1.setText(sessionModel.getEnd_date());
        holder.tv2.setText(sessionModel.getDuration());
        holder.tv3.setText(sessionModel.getTutorName());
        holder.tv4.setText(String.valueOf(sessionModel.getTotal_fees()));
        holder.tv5.setText(String.valueOf(sessionModel.isCompleted()));
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_session_history, parent, false);
        return new myviewholder(convertView);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2,tv3,tv4,tv5;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.start_date);
            tv1 = itemView.findViewById(R.id.end_date);
            tv2 = itemView.findViewById(R.id.duration);
            tv3 = itemView.findViewById(R.id.tutorName);
            tv4 = itemView.findViewById(R.id.totalfees);
            tv5 = itemView.findViewById(R.id.status);
        }
    }

}
