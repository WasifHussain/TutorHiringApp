package com.example.projectii.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projectii.R;
import com.example.projectii.model.LearnerModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LearnerRegisterActivity extends AppCompatActivity {
    EditText et_fullname, et_email, et_address, et_phone, et_password;
    Button createAccount;
    String fullName, email, address, phone, password;
    TextView signIn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_register);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        et_fullname = findViewById(R.id.fullname);
        et_email = findViewById(R.id.email);
        et_address = findViewById(R.id.address);
        et_phone = findViewById(R.id.phone);
        et_password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LearnerRegisterActivity.this, SignInActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("status", true);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

        createAccount = findViewById(R.id.btn_create);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fullName = et_fullname.getText().toString();
                email = et_email.getText().toString();
                address = et_address.getText().toString();
                phone = et_phone.getText().toString();
                password = et_password.getText().toString();

                LearnerModel learnerModel = new LearnerModel();
                learnerModel.setFullName(fullName);
                learnerModel.setEmail(email);
                learnerModel.setAddress(address);
                learnerModel.setPhone(phone);
                learnerModel.setPassword(password);
                learnerModel.setRole("Learner");
                //mAuth.createUserWithEmailAndPassword(email, password);
                if (dataValid()) {
//            DBHelper db = new DBHelper(LearnerRegisterActivity.this);
//            db.addLearner(l);
//                    rootNode = FirebaseDatabase.getInstance();
//                    reference = rootNode.getReference("learners");
//                    l.setId(reference.push().getKey());
//                    reference.child(l.getFullName()).setValue(l);
                    final ProgressDialog pd = new ProgressDialog(LearnerRegisterActivity.this);
                    pd.setTitle("Creating your account");
                    pd.setMessage("Please wait a few seconds");
                    pd.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            learnerModel.setUserId(user.getUid());
                            DocumentReference df = firestore.collection("Learners").document(user.getUid());
                            df.set(learnerModel);
                            pd.dismiss();
                            Toast.makeText(LearnerRegisterActivity.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LearnerRegisterActivity.this, SignInActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(LearnerRegisterActivity.this, "Sorry, could not sign up! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private boolean dataValid() {
        boolean valid;
        if (fullName.length() == 0) {
            et_fullname.requestFocus();
            et_fullname.setError("Field cannot be empty");
            valid = false;
        }else if (fullName.length()<=8) {
            et_fullname.requestFocus();
            et_fullname.setError("Name must be greater than 8 characters ");
            valid = false;
        }
        else if (!fullName.matches("[a-zA-Z\\s]+")) {
            et_fullname.requestFocus();
            et_fullname.setError("Enter only alphabetical character");
            valid = false;
        } else if (email.length() == 0) {
            et_email.requestFocus();
            et_email.setError("Field cannot be empty");
            valid = false;
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            et_email.requestFocus();
            et_email.setError("Enter a valid email address");
            valid = false;
        } else if (address.length() == 0) {
            et_address.requestFocus();
            et_address.setError("Field cannot be empty");
            valid = false;
        } else if (phone.length() == 0) {
            et_phone.requestFocus();
            et_phone.setError("Field cannot be empty");
            valid = false;
        } else if (!phone.matches("(98)[0-9]{8}")) {
            et_phone.requestFocus();
            et_phone.setError("Enter a valid phone number");
            valid = false;
        } else if (password.length() == 0) {
            et_password.requestFocus();
            et_password.setError("Field cannot be empty");
            valid = false;
        } else if (password.length() <= 8) {
            et_password.requestFocus();
            et_password.setError("Password must be of at least 8 characters");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LearnerRegisterActivity.this, LearnerDashboardActivity.class));
            finish();
        }
    }
}
