package com.example.projectii.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.projectii.R;
import com.example.projectii.view.TabAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class TutorDashboardActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnLogout;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentTransaction fragmentTransaction;
    private ActionBarDrawerToggle at;
    TextView tv_name, tv_rating;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ImageView iv;
    Uri imageUri;
    DocumentReference userDoc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_dashboard);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbarId);
        navigationView = findViewById(R.id.nav);
        View header = navigationView.getHeaderView(0);
        tv_name = header.findViewById(R.id.name);
        tv_rating = header.findViewById(R.id.ratings);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tutor Dashboard");

        //For toggle
        at = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(at);
        at.syncState();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storageReference.child("images");
        userDoc = FirebaseFirestore.getInstance().collection("Tutors").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        iv = header.findViewById(R.id.profile_img);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, 1);
            }
        });
//        StorageReference profileRef = storageReference.child("ProfilePictures").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(iv);
//            }
//        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore.getInstance().collection("Tutors").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                tv_name.setText(task.getResult().getString("fullName"));
                tv_rating.setText(String.valueOf(task.getResult().get("avgRating")));
                Glide.with(TutorDashboardActivity.this).load(task.getResult().getString("profilePicUri")).placeholder(R.drawable.img_learnerprofile).into(iv);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.menu_home){
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frameLayout,new HomeFragment());
//                    fragmentTransaction.commit();
                }
                if (id == R.id.menu_profile ){
                    startActivity(new Intent(TutorDashboardActivity.this,TutorProfileActivity.class));
                }
                if(id == R.id.menu_about){
                    AlertDialog.Builder alertLayout = new AlertDialog.Builder(TutorDashboardActivity.this);
                    alertLayout.setTitle("About us");
                    alertLayout.setMessage("This app has been developed by Wasif Hussain.");
                    alertLayout.setPositiveButton("Thank you", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alert = alertLayout.create();
                    alert.show();
                }
                if (id == R.id.menu_help){
                    AlertDialog.Builder alertLayout = new AlertDialog.Builder(TutorDashboardActivity.this);
                    alertLayout.setTitle("Help");
                    alertLayout.setMessage("Call at: 9866040078"+"\n\n"+"Or"+"\n\n"+"Send email at : help@guiders.com");
                    alertLayout.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alert = alertLayout.create();
                    alert.show();
                }
                if(id == R.id.menu_rate){
                    AlertDialog.Builder alertLayout = new AlertDialog.Builder(TutorDashboardActivity.this);
                  View  view = getLayoutInflater().inflate(R.layout.review_dialog, null);
                    alertLayout.setView(view);
                    AlertDialog alert = alertLayout.create();
                    alert.show();
                }
                if(id == R.id.menu_logout){
                    SharedPreferences sp = getSharedPreferences("state",MODE_PRIVATE);
                    SharedPreferences.Editor et = sp.edit();
                    et.putBoolean("loginState",false);
                    et.apply();
                    mAuth.signOut();
                    startActivity(new Intent(TutorDashboardActivity.this,MainActivity.class));
                    finish();
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        final TabAdapter adapter = new TabAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // iv.setImageURI(imageUri);
            uploadPicture();

        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(TutorDashboardActivity.this);
        pd.setTitle("Changing your profile picture");
        pd.setMessage("Please wait a few seconds");
        pd.show();
        StorageReference imageRef = storageReference.child("ProfilePictures").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userDoc.update("profilePicUri" ,uri.toString() );
                        Picasso.get().load(uri).into(iv);
                    }
                });
                pd.dismiss();
                Toast.makeText(TutorDashboardActivity.this, "Profile picture changed successfully ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(TutorDashboardActivity.this, "Sorry, could not change your profile picture! ", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(TutorDashboardActivity.this,TutorRegisterActivity.class));
            finish();
        }
    }
}
