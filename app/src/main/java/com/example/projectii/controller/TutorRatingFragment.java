package com.example.projectii.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.ReviewModel;
import com.example.projectii.model.SessionModel;
import com.example.projectii.view.ReviewAdapter;
import com.example.projectii.view.TabSessionAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TutorRatingFragment extends Fragment {
    RecyclerView rv;
    FirebaseFirestore fireStore;
    FirestoreRecyclerAdapter adapter;
    TextView tv;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_rating, null);
        rv = view.findViewById(R.id.rv);
        tv = view.findViewById(R.id.message_data);
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        fireStore = FirebaseFirestore.getInstance();

        Query query = fireStore.collection("Reviews").whereEqualTo("tutorId",FirebaseAuth.getInstance().getCurrentUser().getUid()).orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ReviewModel> options = new FirestoreRecyclerOptions.Builder<ReviewModel>().
                setQuery(query, ReviewModel.class).build();
        adapter = new ReviewAdapter(options, getContext());
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
        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
