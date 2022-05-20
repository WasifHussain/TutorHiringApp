package com.example.projectii.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.SessionModel;
import com.example.projectii.model.TutorModel;
import com.example.projectii.view.SessionsAdapter;
import com.example.projectii.view.TopTutorAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LearnerHomeFragment extends Fragment {
    RecyclerView rv, rv1;
    FirebaseFirestore fireStore;
    FirestoreRecyclerAdapter adapter, adapter2;
    String fullName;
    TextView tv_fullName;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learner_home,null);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.theme_color));

        rv = view.findViewById(R.id.rv_sessions);
        rv1 = view.findViewById(R.id.rv_toptutors);
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rv1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        tv_fullName = view.findViewById(R.id.learner_fullName);
        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("Learners").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                fullName = task.getResult().getString("fullName");
                tv_fullName.setText(fullName);
            }
        });
        Query query = fireStore.collection("Sessions").whereEqualTo("completed", false).whereEqualTo("learnerID",FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<SessionModel> options = new FirestoreRecyclerOptions.Builder<SessionModel>().
                setQuery(query, SessionModel.class).build();
        adapter = new SessionsAdapter(options, getContext(),tv_fullName.getText().toString());
        rv.setAdapter(adapter);

        Query query1 = fireStore.collection("Tutors").whereEqualTo("available", true).whereGreaterThan("avgRating",4).orderBy("avgRating", Query.Direction.DESCENDING).orderBy("fullName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TutorModel> options1 = new FirestoreRecyclerOptions.Builder<TutorModel>().
                setQuery(query1, TutorModel.class).build();
        adapter2 = new TopTutorAdapter(options1,getContext());
        rv1.setAdapter(adapter2);
        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter2.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter2.startListening();
    }
}
