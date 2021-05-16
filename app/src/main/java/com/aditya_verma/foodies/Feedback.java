package com.aditya_verma.foodies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class Feedback extends AppCompatActivity {

    FirebaseMessaging firebaseMessaging;

    String token;


    EditText feedback_et;
   Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback_et = (EditText)findViewById(R.id.feedback_et);

        btn_submit = (Button)findViewById(R.id.button_submit);


        firebaseMessaging.getInstance().subscribeToTopic("all");

        // fcm settings for perticular user

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = Objects.requireNonNull(task.getResult()).getToken();

                        }
                    }

                });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                feedback_sending_method();

                Toast.makeText(Feedback.this, "Thank You for Feedback", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void feedback_sending_method() {

        LocalDateTime doc_time = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("E, dd MMM YY HH : mm : ss");
        String formattedDate = doc_time.format(timeFormatter);


        HashMap<String,String> feedback = new HashMap<>();
        feedback.put("token",token);
        feedback.put("feedback",feedback_et.getText().toString());
        feedback.put("time",formattedDate);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("USERS").push();
        reference.setValue(feedback);

    }

}