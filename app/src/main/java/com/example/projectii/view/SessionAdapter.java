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
import com.example.projectii.controller.TutorViewActivity;
import com.example.projectii.model.SessionModel;
import com.example.projectii.model.TutorModel;

import java.util.ArrayList;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MyViewHolder> {

    private ArrayList<SessionModel> list;
    Context c;

    public SessionAdapter(ArrayList<SessionModel> l, Context context) {
        list = l;
        c = context;
    }

    @NonNull
    @Override
    public SessionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sessions, parent, false);
        SessionAdapter.MyViewHolder viewHolder = new SessionAdapter.MyViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SessionModel sessionModel = list.get(position);
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
                i.putExtras(b);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2, tv3, tv4;
        Button btn;
        View view;

        public MyViewHolder(View convertView) {
            super(convertView);
            tv = convertView.findViewById(R.id.start_date);
            tv1 = convertView.findViewById(R.id.end_date);
            tv2 = convertView.findViewById(R.id.tutorName);
            btn = convertView.findViewById(R.id.checkStatus);
            view = convertView;
        }
    }
}
