package com.example.projectii.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectii.R;
import com.example.projectii.model.SessionModel;
import com.example.projectii.model.TutorModel;
import com.example.projectii.view.SessionsAdapter;
import com.example.projectii.view.TopTutorAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class LearnerHomeFragment extends Fragment {
    RecyclerView rv, rv1,rv2;
    FirebaseFirestore fireStore;
    FirestoreRecyclerAdapter adapter, adapter2,adapter3;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String fullName;
    ImageView iv;
    TextView tv_fullName, tv;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learner_home,null);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.theme_color));
        iv = view.findViewById(R.id.profile);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseStorage = FirebaseStorage.getInstance();
//        storageReference = firebaseStorage.getReference();
//        storageReference.child("images");
//        StorageReference profileRef = storageReference.child("ProfilePictures").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(iv);
//            }
//        });


        tv = view.findViewById(R.id.message_data);
        rv = view.findViewById(R.id.rv_sessions);
        rv1 = view.findViewById(R.id.rv_toptutors);
        rv2 = view.findViewById(R.id.rv_besttutors);
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rv1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rv2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        tv_fullName = view.findViewById(R.id.learner_fullName);
        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("Learners").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                fullName = task.getResult().getString("fullName");
                Glide.with(getContext()).load(task.getResult().getString("profilePicUri")).placeholder(R.drawable.img_learnerprofile).into(iv);
                tv_fullName.setText(fullName);
            }
        });
        Query query = fireStore.collection("Sessions").whereEqualTo("completed", false).whereEqualTo("learnerID",FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<SessionModel> options = new FirestoreRecyclerOptions.Builder<SessionModel>().
                setQuery(query, SessionModel.class).build();
        adapter = new SessionsAdapter(options, getContext(),tv_fullName.getText().toString());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                int n = adapter.getItemCount();
                Log.e("msg", String.valueOf(n));
                if(n == 0) {
                    tv.setVisibility(View.VISIBLE);
                }
                else {
                    tv.setVisibility(View.GONE);
                }
            }
        });
        rv.setAdapter(adapter);

        Query query1 = fireStore.collection("Tutors").whereEqualTo("available", true).whereGreaterThan("avgRating",4).orderBy("avgRating", Query.Direction.DESCENDING).orderBy("fullName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TutorModel> options1 = new FirestoreRecyclerOptions.Builder<TutorModel>().
                setQuery(query1, TutorModel.class).build();
        adapter2 = new TopTutorAdapter(options1,getContext());
        rv1.setAdapter(adapter2);

        Query query2 = fireStore.collection("Tutors").whereEqualTo("available", true).orderBy("pos_review_count", Query.Direction.DESCENDING).orderBy("fullName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TutorModel> options2 = new FirestoreRecyclerOptions.Builder<TutorModel>().
                setQuery(query2, TutorModel.class).build();
        adapter3 = new TopTutorAdapter(options2,getContext());
        rv2.setAdapter(adapter3);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter2.stopListening();
        adapter3.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter2.startListening();
        adapter3.startListening();
    }
}
