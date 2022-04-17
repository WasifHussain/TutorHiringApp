package com.example.projectii.controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectii.R;
import com.example.projectii.model.LearnerModel;
import com.example.projectii.model.TutorModel;

public class TutorRegisterFragment_1 extends Fragment {
    EditText et_fullName, et_email, et_address, et_phone, et_password;
    Button btnContinue;
    String fullName, email, address, phone, password;
    TextView signIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_tutor_register, null);
        return convertView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_fullName = view.findViewById(R.id.fullname);
        et_email = view.findViewById(R.id.email);
        et_address = view.findViewById(R.id.address);
        et_phone = view.findViewById(R.id.phone);
        et_password = view.findViewById(R.id.password);
        signIn = view.findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SignInActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("statusTutor", true);
                i.putExtras(b);
                startActivity(i);
                getActivity().finish();
            }
        });

        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = et_fullName.getText().toString();
                email = et_email.getText().toString();
                address = et_address.getText().toString();
                phone = et_phone.getText().toString();
                password = et_password.getText().toString();

                SharedPreferences lp = getActivity().getSharedPreferences("TutorData", MODE_PRIVATE);
                SharedPreferences.Editor et = lp.edit();
                et.putString("fullName", fullName );
                et.putString("email", email );
                et.putString("address", address );
                et.putString("phone", phone );
                et.putString("password",password);
                et.commit();
                if (dataValid()) {
                    FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new TutorRegisterFragment_2());
                    ft.commit();
                }
            }
        });
    }

    private boolean dataValid() {
        boolean valid;
        if (fullName.length() == 0) {
            et_fullName.requestFocus();
            et_fullName.setError("Field cannot be empty");
            valid = false;
        } else if (!fullName.matches("[a-zA-Z\\s]+")) {
            et_fullName.requestFocus();
            et_fullName.setError("Enter only alphabetical character");
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
}
