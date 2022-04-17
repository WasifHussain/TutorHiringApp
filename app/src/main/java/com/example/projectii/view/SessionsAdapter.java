package com.example.projectii.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.controller.SessionStatusActivity;
import com.example.projectii.model.SessionModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SessionsAdapter extends FirestoreRecyclerAdapter<SessionModel, SessionsAdapter.myviewholder>{
    Context c;
    String learnerName;
    public SessionsAdapter(@NonNull FirestoreRecyclerOptions<SessionModel> options, Context context,String fullName) {
        super(options);
        c = context;
        learnerName = fullName;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull SessionModel sessionModel) {
        holder.tv.setText(sessionModel.getStart_date());
        holder.tv1.setText(sessionModel.getEnd_date());
        holder.tv2.setText(sessionModel.getTutorName());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(c, SessionStatusActivity.class);
                Bundle b = new Bundle();
                b.putString("startDate", sessionModel.getStart_date());
                b.putString("endDate", sessionModel.getEnd_date());
                b.putString("duration", sessionModel.getDuration());
                b.putBoolean("completed", sessionModel.isCompleted());
                b.putLong("totalFees",sessionModel.getTotal_fees());
                b.putString("tutorName",sessionModel.getTutorName());
                b.putString("tutorId", sessionModel.getTutorID());
                b.putString("sessionId",sessionModel.getSessionId());
                b.putString("learnerName",learnerName);
                i.putExtras(b);
                c.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sessions, parent, false);
        return new myviewholder(convertView);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2;
        Button btn;
        View view;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.start_date);
            tv1 = itemView.findViewById(R.id.end_date);
            tv2 = itemView.findViewById(R.id.tutorName);
            btn = itemView.findViewById(R.id.checkStatus);
            itemView = view;
        }
    }
}

