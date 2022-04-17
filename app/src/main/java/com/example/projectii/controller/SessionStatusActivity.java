package com.example.projectii.controller;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.projectii.R;
import com.example.projectii.model.ReviewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SessionStatusActivity extends AppCompatActivity {
    TextView tv_startDate, tv_endDate, tv_duration, tv_tutorName, tv_totalFees;
    Button btnComplete;
    String fullName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ReviewModel reviewModel = new ReviewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Session Details");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(SessionStatusActivity.this, R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_startDate = findViewById(R.id.start_date);
        tv_endDate = findViewById(R.id.end_date);
        tv_duration = findViewById(R.id.duration);
        tv_tutorName = findViewById(R.id.tutorName);
        tv_totalFees = findViewById(R.id.totalfees);

        String startDate = getIntent().getExtras().getString("startDate");
        String endDate = getIntent().getExtras().getString("endDate");
        String duration = getIntent().getExtras().getString("duration");
        String sessionId = getIntent().getExtras().getString("sessionId");
        String tutorName = getIntent().getExtras().getString("tutorName");
        Long totalFees = getIntent().getExtras().getLong("totalFees");

        tv_startDate.setText(startDate);
        tv_endDate.setText(endDate);
        tv_duration.setText(duration);
        tv_tutorName.setText(tutorName);
        tv_totalFees.setText(String.valueOf(totalFees));

        btnComplete = findViewById(R.id.complete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference ref = db.collection("Sessions").document(sessionId);
                ref.update("completed", true);
                reviewDialog();
            }
        });
    }

    private void reviewDialog() {
        AlertDialog.Builder alertLayout = new AlertDialog.Builder(SessionStatusActivity.this);
        View view = getLayoutInflater().inflate(R.layout.review_dialog, null);
        alertLayout.setView(view);
        AlertDialog alert = alertLayout.create();
        alert.show();
        RatingBar ratingBar = view.findViewById(R.id.rating);
        EditText editText = view.findViewById(R.id.review);
        Button button = view.findViewById(R.id.submit);
        db.collection("Learners").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                fullName = task.getResult().getString("fullName");
                reviewModel.setLearnerName(fullName);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String review = editText.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                String date = simpleDateFormat.format(Calendar.getInstance().getTime());
                reviewModel.setTutorId(getIntent().getExtras().getString("tutorId"));
                reviewModel.setTutorName(getIntent().getExtras().getString("tutorName"));
                reviewModel.setRating(rating);
                reviewModel.setReview(review);
                reviewModel.setCreatedAt(date);
                db.collection("Reviews")
                        .add(reviewModel)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(SessionStatusActivity.this, "Thank you for the feedback!", Toast.LENGTH_LONG).show();
                                alert.dismiss();
                                startActivity(new Intent(SessionStatusActivity.this, LearnerDashboardActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
