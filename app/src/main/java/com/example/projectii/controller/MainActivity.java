package com.example.projectii.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.projectii.R;

public class MainActivity extends AppCompatActivity {
    Button btnLearner, btnTutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        btnLearner = findViewById(R.id.btn_learner);
        btnTutor =  findViewById(R.id.btn_tutor);

        btnLearner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LearnerRegisterActivity.class);
                startActivity(i);
            }
        });
        btnTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,TutorRegisterActivity.class);
                startActivity(i);
            }
        });

    }
}