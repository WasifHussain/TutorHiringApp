package com.example.projectii.controller;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.projectii.R;
import com.example.projectii.model.SessionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.utils.Constant;
import com.khalti.widget.KhaltiButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class HireTutorActivity extends AppCompatActivity {
    Button btn_start, btn_today, btn_confirm;
    TextView tv_fees, tv_totalFees, tv_fees2, tv_days;
    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;
    int fees;
    Long totalFees, tf, difference;
    String fullName, userId, learnerId,dayDifference;
    Long differenceDates;
    KhaltiButton khaltiButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_tutor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hiring Confirmation");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        khaltiButton = (KhaltiButton) findViewById(R.id.khalti_btn);
        khaltiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fees = getIntent().getExtras().getInt("fees");
        fullName = getIntent().getExtras().getString("fullName");
        userId = getIntent().getExtras().getString("uid");

        tv_fees = findViewById(R.id.tutor_fees);
        tv_fees.setText(String.valueOf(fees));
        tv_fees2 = findViewById(R.id.tv_fees);
        tv_fees2.setText(String.valueOf(fees));
        tv_days = findViewById(R.id.tv_days);
        tv_totalFees = findViewById(R.id.tutor_total_fees);

        btn_start = findViewById(R.id.bt_startDate);
        btn_today = findViewById(R.id.bt_today);
        btn_confirm = findViewById(R.id.confirm_duration);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        learnerId = user.getUid();

        durationCalculation();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void durationCalculation() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        btn_today.setText(date);
        btn_start.setText(date);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(HireTutorActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT
                        , dateSetListener1, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                btn_start.setText(date);
            }
        };

        btn_today.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(HireTutorActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT
                        , dateSetListener2, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                btn_today.setText(date);
            }
        };
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String sDate = btn_start.getText().toString();
                String eDate = btn_today.getText().toString();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date1 = simpleDateFormat1.parse(sDate);
                    Date date2 = simpleDateFormat1.parse(eDate);

                    long startDate = date1.getTime();
                    long endDate = date2.getTime();

                    if (startDate <= endDate) {
                        difference = Math.abs(endDate - startDate);
                        differenceDates = difference / (24 * 60 * 60 * 1000);
                        dayDifference = Long.toString(differenceDates);
                        tv_days.setText(dayDifference);
                        totalFees = fees * differenceDates;
                        tv_totalFees.setText(String.valueOf(totalFees));
                        tf = totalFees;

                        khaltiImplementation(userId, fullName, tf);
                    } else {
                        Toast.makeText(HireTutorActivity.this, "Invalid Duration", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void khaltiImplementation(String tutorId, String fullName, Long paisa) {

        Config.Builder builder = new Config.Builder("test_public_key_4e6fd18930454b7c962b50f510ea9d0f", tutorId, fullName, paisa, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                SessionModel sessionModel = new SessionModel();
                sessionModel.setStart_date(btn_start.getText().toString());
                sessionModel.setEnd_date(btn_today.getText().toString());
                sessionModel.setTotal_fees(totalFees);
                sessionModel.setDuration(dayDifference);
                sessionModel.setTutorID(userId);
                sessionModel.setLearnerID(learnerId);
                sessionModel.setCompleted(false);
                sessionModel.setTutorName(fullName);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Sessions").add(sessionModel)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                String sessionId = documentReference.getId();
                                db.collection("Sessions").document(documentReference.getId()).update("sessionId",sessionId);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
                Log.i("success", data.toString());
                try {
                    String stringSenderEmail = "wasifbca19@oic.edu.np";
                    String stringReceiverEmail = "hussainwasif33@gmail.com";
                    String stringPasswordSenderEmail = "@@wasif321";

                    String stringHost = "smtp.gmail.com";

                    Properties properties = System.getProperties();

                    properties.put("mail.smtp.host", stringHost);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");

                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                        }
                    });

                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                    mimeMessage.setSubject("Congratulations! You are hired.");
                    mimeMessage.setText("Hello "+ fullName+", \n\nYou are hired for " + dayDifference+ " days."+"\nStart Date: "+ btn_start.getText().toString()
                                    +"\nEnd Date: "+btn_today.getText().toString()+
                            "\n\n Your total fees for this session will be: Rs. "+ totalFees  +"\n\n Once you have completed your session, fees will be transferred to your Khalti number \n Thank You! \n\n Team Guiders");

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                Toast.makeText(HireTutorActivity.this,"Congratulations tutor has been hired successfully",Toast.LENGTH_LONG).show();
                startActivity(new Intent(HireTutorActivity.this,LearnerDashboardActivity.class));
                finish();
            }
        });
        Config config = builder.build();
        khaltiButton.setCheckOutConfig(config);
        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
        khaltiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                khaltiCheckOut.show();
            }
        });
    }
}
