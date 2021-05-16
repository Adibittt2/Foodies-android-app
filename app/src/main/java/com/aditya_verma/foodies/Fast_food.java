package com.aditya_verma.foodies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class Fast_food extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_food);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fast_food_entry();

    }
    //this is code for menu items in app bar/toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent Intent_home = new Intent(Fast_food.this, MainActivity.class);
                startActivity(Intent_home);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.notification:
                Intent Intent_history = new Intent(Fast_food.this, Shopping_history.class);
                startActivity(Intent_history);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;


            case R.id.inquiry:
                String phone_no = "9142735862";

                if (ActivityCompat.checkSelfPermission(Fast_food.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Fast_food.this,new String[]
                            {Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }
                else {
                    Intent call = new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:"+ phone_no));
                    startActivity(call);
                }
                return true;

            case R.id.feedback:

                Intent feedback = new Intent(getApplicationContext(),Feedback.class);
                startActivity(feedback);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void fast_food_entry(){
        ImageButton btn1 =(ImageButton)findViewById(R.id.imageButton1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entry = new Intent(Fast_food.this,Pizza.class);
                startActivity(entry);
            }
        });

        ImageButton btn2 =(ImageButton)findViewById(R.id.imageButton2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entry = new Intent(Fast_food.this,Chowmin.class);
                startActivity(entry);
            }
        });

        ImageButton btn3 =(ImageButton)findViewById(R.id.imageButton3);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entry = new Intent(Fast_food.this,Macroni.class);
                startActivity(entry);
            }
        });
        ImageButton btn4 =(ImageButton)findViewById(R.id.imageButton4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entry = new Intent(Fast_food.this,Paasta.class);
                startActivity(entry);
            }
        });
        ImageButton btn5 =(ImageButton)findViewById(R.id.imageButton5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entry = new Intent(Fast_food.this,Chilli_and_Manchurian.class);
                startActivity(entry);
            }
        });

        ImageButton btn6 =(ImageButton)findViewById(R.id.imageButton6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entry = new Intent(Fast_food.this,Burger.class);
                startActivity(entry);
            }
        });
        ImageButton btn7 =(ImageButton)findViewById(R.id.imageButton7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entry = new Intent(Fast_food.this,Roll_Momo.class);
                startActivity(entry);
            }
        });
    }
}