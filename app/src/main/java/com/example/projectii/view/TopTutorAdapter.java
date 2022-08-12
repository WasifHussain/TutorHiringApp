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
import com.example.projectii.R;
import com.example.projectii.controller.TutorViewActivity;
import com.example.projectii.model.TutorModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class TopTutorAdapter extends FirestoreRecyclerAdapter<TutorModel, TopTutorAdapter.myViewHolder> {
    Context c;
    public TopTutorAdapter(@NonNull FirestoreRecyclerOptions<TutorModel> options, Context context) {
        super(options);
        c = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull TutorModel tutorModel) {
        holder.tv.setText(tutorModel.getFullName());
//      holder.tv1.setText(tutorModel.getAddress());
//      holder.tv2.setText(tutorModel.getSubjects());
//      holder.tv4.setText(tutorModel.getLevel());
        holder.tv3.setText(String.valueOf(tutorModel.getFees()));
        holder.tv5.setText(new DecimalFormat("#.#").format(tutorModel.getAvgRating()));

//        StorageReference storageReference;
//        storageReference = FirebaseStorage.getInstance().getReference();
//        storageReference.child("images");
//        StorageReference profileRef = storageReference.child("ProfilePictures").child(tutorModel.getUserId());
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(holder.iv);
//            }
//        }
        Glide.with(c).load(tutorModel.getProfilePicUri()).placeholder(R.drawable.img_learnerprofile).into(holder.iv);
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
                b.putString("profilePicUri",tutorModel.getProfilePicUri());
                i.putExtras(b);
                c.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_toptutor, parent, false);
        return new TopTutorAdapter.myViewHolder(convertView);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2, tv3, tv4, tv5;
        ImageView iv;
        View view;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.fullName);
//            tv1 = itemView.findViewById(R.id.address);
//            tv2 = itemView.findViewById(R.id.subject);
            tv3 = itemView.findViewById(R.id.fees);
//            tv4 = itemView.findViewById(R.id.level);
            tv5 = itemView.findViewById(R.id.ratings);
            iv = itemView.findViewById(R.id.profilepic);
            view = itemView;
        }
    }
}
