package com.example.projectii.controller;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectii.R;
import com.example.projectii.model.TutorModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TutorRegisterFragment_3 extends Fragment {
    String khaltiPhone, aboutMe, mode;
    Uri imageUri;
    boolean isAvailable;
    EditText et_khlatiPhone, et_aboutMe;
    RadioGroup rg_teachingMode;
    RadioButton selectedRadioButton;
    Switch availableSwitch;
    Button uploadImage, btnCreate;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    TutorModel tutorModel;
    String fileurl;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_tutor_register3, null);
        return convertView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storageReference.child("Images");

        et_khlatiPhone = view.findViewById(R.id.registeredNumber);
        et_aboutMe = view.findViewById(R.id.aboutMe);

        rg_teachingMode = view.findViewById(R.id.teachingMode);

        availableSwitch = view.findViewById(R.id.availability);

        uploadImage = view.findViewById(R.id.uploadImg);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, 1);
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences("TutorData", Context.MODE_PRIVATE);
        String fullName = sp.getString("fullName","");
        String email = sp.getString("email","");
        String address = sp.getString("address","");
        String phone = sp.getString("phone","");
        String password = sp.getString("password","");
        String qualification = sp.getString("qualification","");
        String teachingHrs = sp.getString("teachingHrs","");
        String subjects  = sp.getString("subjects","");
        String level = sp.getString("level","");
        int fees = sp.getInt("fees",0);


        btnCreate = view.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                khaltiPhone = et_khlatiPhone.getText().toString();
                aboutMe = et_aboutMe.getText().toString();
                int teachingMode = rg_teachingMode.getCheckedRadioButtonId();
                selectedRadioButton = rg_teachingMode.findViewById(teachingMode);
                mode = selectedRadioButton.getText().toString();
                isAvailable = availableSwitch.isChecked();

                tutorModel = new TutorModel();
                tutorModel.setFullName(fullName);
                tutorModel.setEmail(email);
                tutorModel.setAddress(address);
                tutorModel.setPhone(phone);
                tutorModel.setPassword(password);
                tutorModel.setRole("Tutor");
                tutorModel.setQualification(qualification);
                tutorModel.setTeachingHour(teachingHrs);
                tutorModel.setSubjects(subjects);
                tutorModel.setFees(fees);
                tutorModel.setLevel(level);
                tutorModel.setKhaltiNumber(khaltiPhone);
                tutorModel.setAbout(aboutMe);
                tutorModel.setMode(mode);
                tutorModel.setAvailable(isAvailable);
                tutorModel.setImageUri(fileurl);

                if (dataValid()) {
                    final ProgressDialog pd = new ProgressDialog(getContext());
                    pd.setTitle("Creating your account");
                    pd.setMessage("Please wait a few seconds");
                    pd.show();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            tutorModel.setUserId(user.getUid());
                            DocumentReference df = firestore.collection("Tutors").document(user.getUid());
                            df.set(tutorModel);
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Signed Up Successfully!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), SignInActivity.class));
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Sorry, could not sign up! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadPicture(imageUri);

        }
    }

    private void uploadPicture(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.setMessage("Please wait a few seconds");
        pd.show();
        StorageReference imageRef = storageReference.child("identityImages").child(imageUri.getLastPathSegment());
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("Url", uri.toString());
                        fileurl=uri.toString();
                        pd.dismiss();
                    }
                });
                Toast.makeText(getActivity(), "Image Uploaded successfully ", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Sorry, could not upload! ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean dataValid() {
        boolean valid;
        if (khaltiPhone.length() == 0) {
            et_khlatiPhone.requestFocus();
            et_khlatiPhone.setError("Field cannot be empty");
            valid = false;
        } else if (!khaltiPhone.matches("(98)[0-9]{8}")) {
            et_khlatiPhone.requestFocus();
            et_khlatiPhone.setError("Enter a valid phone number");
            valid = false;
        } else if (aboutMe.length() == 0) {
            et_aboutMe.requestFocus();
            et_aboutMe.setError("Field cannot be empty");
            valid = false;
        } else if (aboutMe.length() < 50 || aboutMe.length()>250) {
            et_aboutMe.requestFocus();
            et_aboutMe.setError("Must be between 50 to 250 characters");
            valid = false;
        }else if (imageUri == null){
            Toast.makeText(getActivity(), "Please Upload Image ", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }
}