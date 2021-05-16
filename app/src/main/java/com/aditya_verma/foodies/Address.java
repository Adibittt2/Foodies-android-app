package com.aditya_verma.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class Address extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

   public static EditText name,contact,address;
    public static TextView total_price_address,location_text,selected_area_text;

    String longitude_str,latitde_str;
   // Adapter address_adapter;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    String[] area = new String[]{" Select near area","Bhadodih", "Maharana Pratap Chowk", "Asnabad Mashjid", "Shubash Chowk",
            "Addi Bunglaw", "Paani Tanki", "Rajgharia Road","C.D. Colony","Vidyapuri","Bishunpur Road",
    "Kalali Road","New Colony","Bajrang Nagar","Jhanjhari Gali","Railway Hospital Road","Block Road","Dr.Gali","Station Road","Jhanda Chowk",
    "Jawahar Talkies","BSNL Telephone Exch.","Telaiya Thana","Sunder Hotel","Purnima Talkies","ChhitraGupt Nagar","Devi Mandap Road","Mahuri Dharamshala",
    "Tara Tand","Darjee Muhallah"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

         name = (EditText)findViewById(R.id.address_name);
         contact = (EditText)findViewById(R.id.address_contact);
         address = (EditText)findViewById(R.id.address_address);
         selected_area_text = (TextView)findViewById(R.id.selected_area_text);

        total_price_address = (TextView)findViewById(R.id.total_price_address);

        Intent entry1=getIntent();
        final String total_price = entry1.getStringExtra("total_price_key");
        total_price_address.setText("Rs."+ total_price);


        final Spinner spinner_area = (Spinner) findViewById(R.id.spinner_area);
        spinner_area.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, area);


        spinner_area.setAdapter(adapter);

//        if (ActivityCompat.checkSelfPermission(Chowmin.this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(Chowmin.this,new String[]
//                    {Manifest.permission.CALL_PHONE},REQUEST_CALL);
//        }
//        else {
//            Intent call = new Intent(Intent.ACTION_CALL);
//            call.setData(Uri.parse("tel:"+ phone_no));
//            startActivity(call);
//        }

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);


                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        GPSTracker gps = new GPSTracker(Address.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

             location_text = (TextView)findViewById(R.id.location);

            latitde_str = new Double(latitude).toString();
            longitude_str = new Double(longitude).toString();
            location_text.setText(latitde_str +","+ longitude_str);

//            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


       // name_address();
        Button btn_pay = (Button)findViewById(R.id.btn_go_for_payment);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length()==0){
                    name.setError("Enter your name");
                    name.requestFocus();
                   // name.setText("name hai");
                }
                else if(contact.getText().toString().length()!=10){
                    contact.setError("valid mobile no. is necessary for order confirmation & 0/+91 not allowed");
                    contact.requestFocus();
                }
                else if(address.getText().toString().length()==0){
                    address.setError("Enter your address");
                    address.requestFocus();
                   // address.setText("address hai");
                }
                else if(selected_area_text.getText().equals(" Select near area")){
                    Toast.makeText(Address.this, "Choose your near area", Toast.LENGTH_SHORT).show();
                }
//                else if(latitde_str.equals("0.0")){
//                    ActivityCompat.requestPermissions(Address.this, new String[]{mPermission},
//                            REQUEST_CODE_PERMISSION);
//                }
                else{
//                    Intent entry2 = new Intent(Address.this, Payment.class);
//                startActivity(entry2);
                    Intent entry2 = new Intent(Address.this, Payment.class);
                    String total_price_payment = total_price;

                    entry2.putExtra("total_price_payment_key",total_price_payment);

                    startActivity(entry2);
            }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Arrays.sort(area);

        selected_area_text.setText(area[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

}