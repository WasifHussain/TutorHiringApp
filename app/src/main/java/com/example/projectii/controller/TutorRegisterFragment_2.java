package com.example.projectii.controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectii.R;
import com.example.projectii.model.TutorModel;

public class TutorRegisterFragment_2 extends Fragment {
    EditText et_qualification, et_teachingHrs, et_subjects, et_fees;
    Button btnContinue;
    Spinner spinnerLevel;
    String qualification, teachingHrs, subjects, level;
    int fees;
    TextView test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_tutor_register2, null);
        return convertView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_qualification = view.findViewById(R.id.qualification);
        et_teachingHrs = view.findViewById(R.id.teachingHrs);
        et_subjects = view.findViewById(R.id.subjects);
        et_fees = view.findViewById(R.id.fees);

        spinnerLevel = view.findViewById(R.id.level);

        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qualification = et_qualification.getText().toString();
                teachingHrs = et_teachingHrs.getText().toString();
                subjects = et_subjects.getText().toString();
                level = spinnerLevel.getSelectedItem().toString();
                try {
                    fees = Integer.parseInt(et_fees.getText().toString());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                SharedPreferences lp = getActivity().getSharedPreferences("TutorData", Context.MODE_APPEND);
                SharedPreferences.Editor et = lp.edit();
                et.putString("qualification", qualification );
                et.putString("teachingHrs", teachingHrs );
                et.putString("subjects", subjects );
                et.putString("level", level );
                et.putInt("fees",fees);
                et.commit();

                if (dataValid()) {
                    FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new TutorRegisterFragment_3());
                    ft.commit();
                }
            }
        });
    }

    private boolean dataValid() {
        boolean valid;
        if (qualification.length() == 0) {
            et_qualification.requestFocus();
            et_qualification.setError("Field cannot be empty");
            valid = false;
        } else if (qualification.length() <= 15) {
            et_qualification.requestFocus();
            et_qualification.setError("Invalid Length of Qualification");
            valid = false;
        } else if (!qualification.matches("[a-zA-Z\\s]+")) {
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
        }
        else if(teachingHrs.length()==0){
            et_teachingHrs.requestFocus();
            et_teachingHrs.setError("Field cannot be empty");
            valid = false;
        }
        else if(teachingHrs.length()<=6){
            et_teachingHrs.requestFocus();
            et_teachingHrs.setError("Provide in proper format");
            valid = false;
        }
        else if(subjects.length()==0){
            et_subjects.requestFocus();
            et_subjects.setError("Field cannot be empty");
            valid = false;
        }
        else if(subjects.length()>100){
            et_subjects.requestFocus();
            et_subjects.setError("Subjects out of limit");
            valid = false;
        }
        else if(!subjects.matches("[a-zA-Z\\s]+")){
            et_subjects.requestFocus();
            et_subjects.setError("Provide in proper format");
            valid = false;
        }
        else if(fees<100 || fees>3000){
          et_fees.requestFocus();
         et_fees.setError("Must be in range 100 to 3000");
          valid = false;
    }
        else {
            valid = true;
        }
        return valid;
    }
}
