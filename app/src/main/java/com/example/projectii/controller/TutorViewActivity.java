package com.example.projectii.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.projectii.R;
import com.example.projectii.model.ReviewModel;
import com.example.projectii.model.SessionModel;
import com.example.projectii.view.ReviewAdapter;
import com.example.projectii.view.SessionsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class TutorViewActivity extends AppCompatActivity {
    TextView tv,tv_fullName, tv_email, tv_phone, tv_address,tv_qualification, tv_subjects, tv_mode,
            tv_level, tv_hours, tv_about, tv_ratingCount;
    ImageView iv;
    Button btnHire;
    RecyclerView rv;
    FirebaseFirestore fireStore;
    FirestoreRecyclerAdapter adapter;
    String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv = findViewById(R.id.message_data);
        tv_ratingCount = findViewById(R.id.rating_count);
        tv_fullName = findViewById(R.id.tutor_fullName);
        tv_email = findViewById(R.id.tutor_email);
        tv_phone = findViewById(R.id.tutor_phone);
        tv_address = findViewById(R.id.tutor_address);
        tv_qualification = findViewById(R.id.tutor_qualification);
        tv_subjects = findViewById(R.id.tutor_subjects);
        tv_mode = findViewById(R.id.tutor_mode);
        tv_level = findViewById(R.id.tutor_level);
        tv_hours = findViewById(R.id.tutor_hours);
        tv_about = findViewById(R.id.tutor_about);
        iv = findViewById(R.id.profilepic);

        userId = getIntent().getExtras().getString("uid");
        String fullName = getIntent().getExtras().getString("fullName");
        String email = getIntent().getExtras().getString("email");
        String phone = getIntent().getExtras().getString("phone");
        String address = getIntent().getExtras().getString("address");
        String qualification = getIntent().getExtras().getString("qualification");
        String subjects = getIntent().getExtras().getString("subjects");
        String mode = getIntent().getExtras().getString("mode");
        String level = getIntent().getExtras().getString("level");
        String hours = getIntent().getExtras().getString("teachingHrs");
        String about = getIntent().getExtras().getString("about");
        int fees = getIntent().getExtras().getInt("fees");
        String profilePicUri = getIntent().getExtras().getString("profilePicUri");

        tv_fullName.setText(fullName);
        tv_email.setText(email);
        tv_phone.setText(phone);
        tv_address.setText(address);
        tv_qualification.setText(qualification);
        tv_subjects.setText(subjects);
        tv_mode.setText(mode);
        tv_level.setText(level);
        tv_hours.setText(hours);
        tv_about.setText(about);

        btnHire = findViewById(R.id.hireNow);
        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TutorViewActivity.this,HireTutorActivity.class);
                Bundle b = new Bundle();
                b.putString("uid", userId);
                b.putString("fullName",fullName);
                b.putInt("fees",fees);
                i.putExtras(b);
                startActivity(i);
            }
        });

//        StorageReference storageReference;
//        storageReference = FirebaseStorage.getInstance().getReference();
//        storageReference.child("images");
//        StorageReference profileRef = storageReference.child("ProfilePictures").child(userId);
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(iv);
//            }
//        });
        Glide.with(TutorViewActivity.this).load(profilePicUri).into(iv);

        rv = findViewById(R.id.rv_reviews);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fireStore = FirebaseFirestore.getInstance();
        Query query = fireStore.collection("Reviews").whereEqualTo("tutorId",userId).orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ReviewModel> options = new FirestoreRecyclerOptions.Builder<ReviewModel>().
                setQuery(query, ReviewModel.class).build();
        adapter = new ReviewAdapter(options, TutorViewActivity.this);
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
        adapter.notifyDataSetChanged();
        averageRating();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    private void averageRating(){

       fireStore.collection("Reviews").whereEqualTo("tutorId",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double total = 0.0;
                        double average= 0.0;
                        double count = 0.0;
                        int pos = 0;
                        int neg = 0;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData()+userId);
                                double rating = (double) document.get("rating");
                                total = total +rating;
                                count = count + 1;
                                average = total/count;
                                int c = (int) count;
                                tv_ratingCount.setText(String.valueOf(c));
                                Log.e("reviews", String.valueOf(c));
                                String sentiment_value = (String) document.get("sentiment_value");
                                int s = Integer.parseInt(sentiment_value);

                                if (s==1) {
                                    pos = pos + 1;
                                    Log.e("positive", String.valueOf(pos));
                                }
                                if(s==0){
                                    neg = neg+1;
                                    Log.e("negative",String.valueOf(neg));
                                }
                            }
                            Log.d("User Id",userId);
                            DocumentReference userDoc = fireStore.collection("Tutors").document(userId);
                            userDoc.update("avgRating" ,average );
                            userDoc.update("pos_review_count",pos);
                            userDoc.update("neg_review_count",neg);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
