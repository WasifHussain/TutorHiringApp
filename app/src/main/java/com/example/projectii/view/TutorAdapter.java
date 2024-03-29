package com.example.projectii.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.projectii.R;
import com.example.projectii.controller.LearnerDashboardActivity;
import com.example.projectii.controller.TutorViewActivity;
import com.example.projectii.model.TutorModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.MyViewHolder> {

    private ArrayList<TutorModel> list;
    ArrayList<TutorModel> backup;
    Context c;

    public TutorAdapter(ArrayList<TutorModel> l, Context context) {
        list = l;
        backup = new ArrayList<>(list);
        c = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tutor, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TutorModel tutorModel = list.get(position);
        holder.tv.setText(tutorModel.getFullName());
        holder.tv1.setText(tutorModel.getAddress());
        holder.tv2.setText(tutorModel.getSubjects());
        holder.tv4.setText(tutorModel.getLevel());
        holder.tv3.setText(String.valueOf(tutorModel.getFees()));
        holder.tv5.setText(new DecimalFormat("#.#").format(tutorModel.getAvgRating()));
//        StorageReference storageReference;
//        storageReference = FirebaseStorage.getInstance().getReference();
//        storageReference.child("images");
//        StorageReference profileRef = storageReference.child("ProfilePictures").child(tutorModel.getUserId());
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(c).load(uri).placeholder(R.drawable.img_profile).into(holder.iv);
////                Picasso.get().load(uri).into(holder.iv);
//            }
//        });
        Glide.with(c).load(tutorModel.getProfilePicUri()).placeholder(R.drawable.img_profile).into(holder.iv);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(c,TutorViewActivity.class);
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
                b.putString("profilePicUri",tutorModel.getProfilePicUri());
                i.putExtras(b);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<TutorModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2, tv3, tv4, tv5;
        ImageView iv;
        View view;

        public MyViewHolder(View convertView) {
            super(convertView);
            tv = convertView.findViewById(R.id.fullName);
            tv1 = convertView.findViewById(R.id.address);
            tv2 = convertView.findViewById(R.id.subject);
            tv3 = convertView.findViewById(R.id.fees);
            tv4 = convertView.findViewById(R.id.level);
            tv5 = convertView.findViewById(R.id.ratings);
            iv = convertView.findViewById(R.id.profilepic);
            view = convertView;
        }
    }
}

