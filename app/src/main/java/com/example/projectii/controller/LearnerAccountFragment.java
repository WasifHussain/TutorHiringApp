package com.example.projectii.controller;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projectii.R;
import com.example.projectii.model.LearnerModel;
import com.example.projectii.model.TutorModel;
import com.example.projectii.view.LearnerAccountOptionsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class LearnerAccountFragment extends Fragment {
    Button btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore fireStore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ImageView iv;
    Uri imageUri;
    TextView tv1, tv2, tv3, tv4;
    EditText et1, et2, et3, et4, et5;
    Button edit;
    String fullName, email, phone, address, password;
    String new_name, new_email, new_address, new_phone, new_password;
    ListView lv;
    String[] options = {"Edit Profile", "History", "Help", "About ", "Rate us"};
    int[] icons = {R.drawable.ic_baseline_person_outline_24, R.drawable.ic_baseline_history_24, R.drawable.ic_baseline_help_outline_24, R.drawable.ic_outline_info_24, R.drawable.ic_outline_star_outline_24};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learner_account, null);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.theme_color));

        tv1 = view.findViewById(R.id.learner_fullName);
        tv2 = view.findViewById(R.id.learner_email);
        tv3 = view.findViewById(R.id.learner_phone);
        tv4 = view.findViewById(R.id.learner_address);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storageReference.child("images");
        iv = view.findViewById(R.id.learnerProfile);
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
//                 Picasso.get().load(uri).into(iv);
//            }
//        });


        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("Learners").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                fullName = task.getResult().getString("fullName");
                email = task.getResult().getString("email");
                address = task.getResult().getString("address");
                phone = task.getResult().getString("phone");
                password = task.getResult().getString("password");
                Glide.with(getContext()).load(task.getResult().getString("profilePicUri")).placeholder(R.drawable.img_learnerprofile).into(iv);
                tv1.setText(fullName);
                tv2.setText(email);
                tv3.setText(phone);
                tv4.setText(address);
            }
        });

        lv = view.findViewById(R.id.options);
        lv.setAdapter(new LearnerAccountOptionsAdapter(getContext(), options, icons));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    editProfile();
                }
                if (i == 1){
                    startActivity(new Intent(getActivity(),LearnerHistoryActivity.class));
                }
                if (i == 2){
                    AlertDialog.Builder alertLayout = new AlertDialog.Builder(getContext());
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
                if (i==3){
                    AlertDialog.Builder alertLayout = new AlertDialog.Builder(getContext());
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
                if (i == 4){
                    AlertDialog.Builder alertLayout = new AlertDialog.Builder(getContext());
                    view = getLayoutInflater().inflate(R.layout.review_dialog, null);
                    alertLayout.setView(view);
                    AlertDialog alert = alertLayout.create();
                    alert.show();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getActivity().getSharedPreferences("state",MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                et.putBoolean("loginState",false);
                et.apply();
                mAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
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
        final ProgressDialog pd = new ProgressDialog(getContext());
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
                        DocumentReference userDoc = fireStore.collection("Learners").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        userDoc.update("profilePicUri",uri.toString());
                        Picasso.get().load(uri).into(iv);
                    }
                });
                pd.dismiss();
                Toast.makeText(getActivity(), "Profile picture changed successfully ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Sorry, could not change your profile picture! ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void editProfile() {
        AlertDialog.Builder alertLayout = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.fragment_learner_edit_profile, null);
        alertLayout.setView(view);
        AlertDialog alert = alertLayout.create();
        alert.show();
        et1 = view.findViewById(R.id.fullname);
        et2 = view.findViewById(R.id.email);
        et4 = view.findViewById(R.id.phone);
        et3 = view.findViewById(R.id.address);
        et5 = view.findViewById(R.id.password);
        et1.setText(fullName);
        et2.setText(email);
        et3.setText(address);
        et4.setText(phone);
        et5.setText(password);
        edit = view.findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_name = et1.getText().toString();
                new_email = et2.getText().toString();
                new_address = et3.getText().toString();
                new_phone = et4.getText().toString();
                new_password = et5.getText().toString();

                LearnerModel learnerModel = new LearnerModel();
                learnerModel.setFullName(new_name);
                learnerModel.setEmail(new_email);
                learnerModel.setAddress(new_address);
                learnerModel.setPhone(new_phone);
                learnerModel.setPassword(new_password);
                learnerModel.setRole("Learner");
                learnerModel.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (dataValid()) {
                    DocumentReference df = fireStore.collection("Learners").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    df.set(learnerModel);
                    alert.dismiss();
                    Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
     }

    private boolean dataValid() {
        boolean valid;
        if (new_name.length() == 0) {
            et1.requestFocus();
            et1.setError("Field cannot be empty");
            valid = false;
        } else if (new_name.length() <= 10) {
            et1.requestFocus();
            et1.setError("Name must be greater than 10 characters ");
            valid = false;
        } else if (!new_name.matches("[a-zA-Z\\s]+")) {
            et1.requestFocus();
            et1.setError("Enter only alphabetical character");
            valid = false;
        } else if (new_email.length() == 0) {
            et2.requestFocus();
            et2.setError("Field cannot be empty");
            valid = false;
        } else if (!new_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            et2.requestFocus();
            et2.setError("Enter a valid email address");
            valid = false;
        } else if (new_address.length() == 0) {
            et3.requestFocus();
            et3.setError("Field cannot be empty");
            valid = false;
        } else if (new_phone.length() == 0) {
            et4.requestFocus();
            et4.setError("Field cannot be empty");
            valid = false;
        } else if (!new_phone.matches("(98)[0-9]{8}")) {
            et4.requestFocus();
            et4.setError("Enter a valid phone number");
            valid = false;
        } else if (new_password.length() == 0) {
            et5.requestFocus();
            et5.setError("Field cannot be empty");
            valid = false;
        } else if (new_password.length() <= 8) {
            et5.requestFocus();
            et5.setError("Password must be of at least 8 characters");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }
}
