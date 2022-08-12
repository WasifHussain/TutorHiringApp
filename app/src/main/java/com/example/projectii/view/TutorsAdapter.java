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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;



//Not being implemented in this project


public class TutorsAdapter extends FirestoreRecyclerAdapter<TutorModel, TutorsAdapter.myViewHolder> {
    Context c;
    private ArrayList<TutorModel> list;
    ArrayList<TutorModel> backup;
    public TutorsAdapter(@NonNull FirestoreRecyclerOptions<TutorModel> options, Context context,ArrayList<TutorModel> l) {
        super(options);
        c = context;
        list = l;
        backup = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public TutorsAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tutor, parent, false);
        return new TutorsAdapter.myViewHolder(convertView);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull TutorModel tutorModel) {
//        TutorModel tutorModel = list.get(position);
        holder.tv.setText(tutorModel.getFullName());
        holder.tv1.setText(tutorModel.getAddress());
        holder.tv2.setText(tutorModel.getSubjects());
        holder.tv4.setText(tutorModel.getLevel());
        holder.tv3.setText(String.valueOf(tutorModel.getFees()));
        holder.tv5.setText(new DecimalFormat("#.#").format(tutorModel.getAvgRating()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(c, TutorViewActivity.class);
                Bundle b = new Bundle();
                b.putString("uid", tutorModel.getUserId());
                b.putString("fullName", tutorModel.getFullName());
                b.putString("email", tutorModel.getEmail());
                b.putString("phone", tutorModel.getPhone());
                b.putString("address", tutorModel.getAddress());
                b.putString("qualification", tutorModel.getQualification());
                b.putString("subjects", tutorModel.getSubjects());
                b.putString("mode", tutorModel.getMode());
                b.putString("level", tutorModel.getLevel());
                b.putString("teachingHrs",tutorModel.getTeachingHour());
                b.putString("about", tutorModel.getAbout());
                b.putInt("fees",tutorModel.getFees());
                i.putExtras(b);
                c.startActivity(i);
            }
        });
    }
    public void filterList(ArrayList<TutorModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2, tv3, tv4, tv5;
        View view;
        public myViewHolder(@NonNull View convertView) {
            super(convertView);
            tv = convertView.findViewById(R.id.fullName);
            tv1 = convertView.findViewById(R.id.address);
            tv2 = convertView.findViewById(R.id.subject);
            tv3 = convertView.findViewById(R.id.fees);
            tv4 = convertView.findViewById(R.id.level);
            tv5 = convertView.findViewById(R.id.ratings);
            view = convertView;
        }
    }
}
