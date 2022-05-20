package com.example.projectii.controller;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.SessionModel;
import com.example.projectii.view.LearnerSessionHistoryAdapter;
import com.example.projectii.view.SessionsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LearnerHistoryActivity extends AppCompatActivity {
    FirebaseFirestore fireStore;
    RecyclerView rv ;
    FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Session History");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(LearnerHistoryActivity.this, R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fireStore = FirebaseFirestore.getInstance();

        Query query = fireStore.collection("Sessions").whereEqualTo("learnerID",FirebaseAuth.getInstance().getCurrentUser().getUid()).orderBy("tutorName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<SessionModel> options = new FirestoreRecyclerOptions.Builder<SessionModel>().
                setQuery(query, SessionModel.class).build();
        adapter = new LearnerSessionHistoryAdapter(options, LearnerHistoryActivity.this);
        rv.setAdapter(adapter);
    }    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
