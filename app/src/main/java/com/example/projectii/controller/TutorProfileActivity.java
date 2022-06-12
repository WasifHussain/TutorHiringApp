package com.example.projectii.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.projectii.R;
import com.example.projectii.model.LearnerModel;
import com.example.projectii.model.TutorModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class TutorProfileActivity extends AppCompatActivity {
    EditText et_fullName, et_email, et_address, et_phone, et_password, et_qualification, et_teachingHrs, et_subjects, et_fees, et_khlatiPhone, et_aboutMe;
    Spinner spinnerLevel;
    Switch availableSwitch;
    Button btnEdit;
    String fullName, email, address, phone, password, qualification, teachingHrs, subjects, level, khaltiPhone, aboutMe, imageUri, mode;
    String new_name, new_email, new_address, new_phone, new_password, new_qualification, new_teachingHrs, new_subjects, new_level, new_khaltiPhone, new_aboutMe;
    Double avgRating;
    int new_fees;
    Long fees;
    boolean isAvailable;
    boolean new_Availability;

    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(TutorProfileActivity.this, R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout l = findViewById(R.id.myLayout);
        et_fullName = findViewById(R.id.fullname);
        et_email = findViewById(R.id.email);
        et_address = findViewById(R.id.address);
        et_phone = findViewById(R.id.phone);
        et_password = findViewById(R.id.password);
        et_qualification = findViewById(R.id.qualification);
        et_teachingHrs = findViewById(R.id.teachingHrs);
        et_subjects = findViewById(R.id.subjects);
        et_fees = findViewById(R.id.fees);
        et_khlatiPhone = findViewById(R.id.registeredNumber);
        et_aboutMe = findViewById(R.id.aboutMe);

        spinnerLevel = findViewById(R.id.level);
        availableSwitch = findViewById(R.id.availability);

        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("Tutors").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                fullName = task.getResult().getString("fullName");
                email = task.getResult().getString("email");
                address = task.getResult().getString("address");
                phone = task.getResult().getString("phone");
                password = task.getResult().getString("password");
                qualification = task.getResult().getString("qualification");
                teachingHrs = task.getResult().getString("teachingHour");
                subjects = task.getResult().getString("subjects");
                fees = task.getResult().getLong("fees");
                khaltiPhone = task.getResult().getString("khaltiNumber");
                aboutMe = task.getResult().getString("about");
                isAvailable = task.getResult().getBoolean("available");
                level = task.getResult().getString("level");
                String[] levels = getResources().getStringArray(R.array.level_arrays);

                avgRating = task.getResult().getDouble("avgRating");
                imageUri = task.getResult().getString("imageUri");
                mode = task.getResult().getString("mode");

                et_fullName.setText(fullName);
                et_email.setText(email);
                et_phone.setText(phone);
                et_address.setText(address);
                et_password.setText(password);
                et_qualification.setText(qualification);
                et_teachingHrs.setText(teachingHrs);
                et_subjects.setText(subjects);
                et_fees.setText(String.valueOf(fees));
                et_khlatiPhone.setText(khaltiPhone);
                et_aboutMe.setText(aboutMe);
                availableSwitch.setChecked(isAvailable);
                spinnerLevel.setSelection(Arrays.asList(levels).indexOf(level));
            }
        });
        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_name = et_fullName.getText().toString();
                new_email = et_email.getText().toString();
                new_address = et_address.getText().toString();
                new_phone = et_phone.getText().toString();
                new_password = et_password.getText().toString();
                new_qualification = et_qualification.getText().toString();
                new_teachingHrs = et_teachingHrs.getText().toString();
                new_subjects = et_subjects.getText().toString();
                new_fees = Integer.parseInt(et_fees.getText().toString());
                new_khaltiPhone = et_khlatiPhone.getText().toString();
                new_aboutMe = et_aboutMe.getText().toString();
                new_Availability = availableSwitch.isChecked();
                new_level = spinnerLevel.getSelectedItem().toString();

                TutorModel tutorModel = new TutorModel();
                tutorModel.setFullName(new_name);
                tutorModel.setEmail(new_email);
                tutorModel.setAddress(new_address);
                tutorModel.setPhone(new_phone);
                tutorModel.setPassword(new_password);
                tutorModel.setQualification(new_qualification);
                tutorModel.setLevel(new_level);
                tutorModel.setTeachingHour(new_teachingHrs);
                tutorModel.setSubjects(new_subjects);
                tutorModel.setFees(new_fees);
                tutorModel.setKhaltiNumber(new_khaltiPhone);
                tutorModel.setAbout(new_aboutMe);
                tutorModel.setAvailable(new_Availability);
                tutorModel.setRole("Tutor");
                tutorModel.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                tutorModel.setMode(mode);
                tutorModel.setImageUri(imageUri);
                tutorModel.setAvgRating(avgRating);
                if (dataValid()) {
                    DocumentReference df = fireStore.collection("Tutors").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    df.set(tutorModel);
                    Snackbar.make(l,"Profile updated successfully!",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean dataValid() {
        boolean valid;
        if (new_name.length() == 0) {
            et_fullName.requestFocus();
            et_fullName.setError("Field cannot be empty");
            valid = false;
        } else if (new_name.length() <= 8) {
            et_fullName.requestFocus();
            et_fullName.setError("Name must be greater than 8 characters ");
            valid = false;
        } else if (!new_name.matches("[a-zA-Z\\s]+")) {
            et_fullName.requestFocus();
            et_fullName.setError("Enter only alphabetical character");
            valid = false;
        } else if (new_email.length() == 0) {
            et_email.requestFocus();
            et_email.setError("Field cannot be empty");
            valid = false;
        } else if (!new_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            et_email.requestFocus();
            et_email.setError("Enter a valid email address");
            valid = false;
        } else if (new_address.length() == 0) {
            et_address.requestFocus();
            et_address.setError("Field cannot be empty");
            valid = false;
        } else if (new_phone.length() == 0) {
            et_phone.requestFocus();
            et_phone.setError("Field cannot be empty");
            valid = false;
        } else if (!new_phone.matches("(98)[0-9]{8}")) {
            et_phone.requestFocus();
            et_phone.setError("Enter a valid phone number");
            valid = false;
        } else if (new_password.length() == 0) {
            et_password.requestFocus();
            et_password.setError("Field cannot be empty");
            valid = false;
        } else if (new_password.length() <= 8) {
            et_password.requestFocus();
            et_password.setError("Password must be of at least 8 characters");
            valid = false;
        } else if (new_qualification.length() == 0) {
            et_qualification.requestFocus();
            et_qualification.setError("Field cannot be empty");
            valid = false;
        } else if (new_qualification.length() <= 15) {
            et_qualification.requestFocus();
            et_qualification.setError("Invalid Length of Qualification");
            valid = false;
        } else if (!new_qualification.matches("[a-zA-Z\\s]+")) {
            et_qualification.requestFocus();
            et_qualification.setError("Enter only alphabetical character");
            valid = false;
        } else if (spinnerLevel.getSelectedItem().toString().trim().equals("Choose a Level")) {
            TextView errorText = (TextView) spinnerLevel.getSelectedView();
            errorText.setError("");
            errorText.setText("Choose one");
            errorText.setTextColor(Color.RED);
            spinnerLevel.performClick();
            valid = false;
        } else if (new_teachingHrs.length() == 0) {
            et_teachingHrs.requestFocus();
            et_teachingHrs.setError("Field cannot be empty");
            valid = false;
        } else if (new_teachingHrs.length() <= 6) {
            et_teachingHrs.requestFocus();
            et_teachingHrs.setError("Provide in proper format");
            valid = false;
        } else if (new_subjects.length() == 0) {
            et_subjects.requestFocus();
            et_subjects.setError("Field cannot be empty");
            valid = false;
        } else if (new_subjects.length() > 100) {
            et_subjects.requestFocus();
            et_subjects.setError("Subjects out of limit");
            valid = false;
        } else if (!new_subjects.matches("[a-zA-Z\\s]+")) {
            et_subjects.requestFocus();
            et_subjects.setError("Provide in proper format");
            valid = false;
        } else if (new_fees < 100 || new_fees > 3000) {
            et_fees.requestFocus();
            et_fees.setError("Must be in range 100 to 3000");
            valid = false;
        } else if (new_khaltiPhone.length() == 0) {
            et_khlatiPhone.requestFocus();
            et_khlatiPhone.setError("Field cannot be empty");
            valid = false;
        } else if (!new_khaltiPhone.matches("(98)[0-9]{8}")) {
            et_khlatiPhone.requestFocus();
            et_khlatiPhone.setError("Enter a valid phone number");
            valid = false;
        } else if (new_aboutMe.length() == 0) {
            et_aboutMe.requestFocus();
            et_aboutMe.setError("Field cannot be empty");
            valid = false;
        } else if (new_aboutMe.length() < 50 || new_aboutMe.length() > 250) {
            et_aboutMe.requestFocus();
            et_aboutMe.setError("Must be between 50 to 250 characters");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
