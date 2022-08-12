package com.example.projectii.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projectii.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Spinner spinnerRole;
    Button signIn;
    String email, password, srole;
    TextView signUp;
    FirebaseFirestore fireStore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        spinnerRole = findViewById(R.id.roles);

        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status = getIntent().getExtras().getBoolean("status");
                boolean statusTutor = getIntent().getExtras().getBoolean("statusTutor");
                if (status) {
                    Intent i = new Intent(SignInActivity.this, LearnerRegisterActivity.class);
                    startActivity(i);
                    finish();
                }
                if (statusTutor) {
                    Intent i = new Intent(SignInActivity.this, TutorRegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        signIn = findViewById(R.id.btn_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();
                srole = spinnerRole.getSelectedItem().toString();
                if (dataValid()) {
                    final ProgressDialog pd = new ProgressDialog(SignInActivity.this);
                    pd.setTitle("Signing in....");
                    pd.setMessage("Please wait a few seconds");
                    pd.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            checkUserAccess(authResult.getUser().getUid());
                            pd.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(SignInActivity.this, "Sorry, could not sign in! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private boolean dataValid() {
        boolean valid;
        if (email.length() == 0) {
            et_email.requestFocus();
            et_email.setError("Field cannot be empty");
            valid = false;
        } else if (password.length() == 0) {
            et_password.requestFocus();
            et_password.setError("Field cannot be empty");
            valid = false;
        } else if (spinnerRole.getSelectedItem().toString().trim().equals("Select your role")) {
            TextView errorText = (TextView) spinnerRole.getSelectedView();
            errorText.setError("");
            errorText.setText("Select your role");
            errorText.setTextColor(Color.RED);
            spinnerRole.performClick();
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private void checkUserAccess(String uid) {
        if (srole.equals("Learner")) {
            DocumentReference df = fireStore.collection("Learners").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG", "onSuccess" + documentSnapshot.getData());
                    if (srole.equals(documentSnapshot.getString("role"))) {
                        SharedPreferences lp = getSharedPreferences("state",MODE_PRIVATE);
                        SharedPreferences.Editor et = lp.edit();
                        et.putBoolean("loginState",true);
                        et.putString("role",documentSnapshot.getString("role"));
                        et.apply();
                        Toast.makeText(SignInActivity.this, "Signed in Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, LearnerDashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "You are not registered as Learner", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (srole.equals("Tutor")) {
            DocumentReference df = fireStore.collection("Tutors").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG", "onSuccess" + documentSnapshot.getData());
                    if (srole.equals(documentSnapshot.getString("role"))) {
                        SharedPreferences lp = getSharedPreferences("state",MODE_PRIVATE);
                        SharedPreferences.Editor et = lp.edit();
                        et.putBoolean("loginState",true);
                        et.putString("role",documentSnapshot.getString("role"));
                        et.apply();
                        Toast.makeText(SignInActivity.this, "Signed in Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, TutorDashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "You are not registered as Tutor", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(SignInActivity.this, "Sorry, your role has not been defined!", Toast.LENGTH_LONG).show();
        }
    }
}

