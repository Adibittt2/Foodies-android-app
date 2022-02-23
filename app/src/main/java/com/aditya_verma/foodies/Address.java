package com.aditya_verma.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class Address extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

   public static EditText name,contact,address,name_on_cake;
    public static TextView total_price_address;
    public static TextView location_text;
    public static TextView selected_area_text;
    public static TextView selected_delivery_date_text;
    public static TextView selected_tag_text,discount_value_extra;

    public static Spinner spinner_area,spinner_delivery_date,spinner_tag;

    public static int address_visibility,address_coupon_visibility;

   public static EditText discount_code_text;
    Button apply_code_button;
 public static String discount_value_int_address;

    LinearLayout Coupon_layout_address;



    String longitude_str,latitde_str;
   // Adapter address_adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef_Coupon = db.collection("Coupon").document(MainActivity.title + " Coupon_Code");
    private DocumentReference noteRef_Coupon_value = db.collection("Coupon").document(MainActivity.title + " Coupon_Value");


    private FusedLocationProviderClient fusedLocationProviderClient;

    String[] area = new String[]{" Select near area","Bhadodih", "Maharana Pratap Chowk", "Asnabad Mashjid", "Shubash Chowk",
            "Addi Bunglaw", "Paani Tanki", "Rajgharia Road","C.D. Colony","Vidyapuri","Bishunpur Road",
    "Kalali Road","New Colony","Bajrang Nagar","Jhanjhari Gali","Railway Hospital Road","Block Road","Dr.Gali","Station Road","Jhanda Chowk",
    "Jawahar Talkies","BSNL Telephone Exch.","Telaiya Thana","Sunder Hotel","Purnima Talkies","ChhitraGupt Nagar","Devi Mandap Road","Mahuri Dharamshala",
    "Tara Tand","Darjee Muhallah","Koderma","Chandwara","Banjhedih Power Plant","Other Area","R.L.S.Y road"};

    String[] delivery_date = new String[]{"","Today","Tomorrow"};

    String[] celebration_tag = new String[]{"","Happy B'day", "Happy Anniversay", "Happy Teacher's day", "Merry Christmas","Valentine's day"};

    String total_price;

    String Coupon1,Coupon2,Coupon3,Coupon4,Coupon5,Coupon6;
    String  Coupon_value1,Coupon_value2,Coupon_value3,Coupon_value4,Coupon_value5,Coupon_value6;

    TextView disc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

         name = (EditText)findViewById(R.id.address_name);
         contact = (EditText)findViewById(R.id.address_contact);
         address = (EditText)findViewById(R.id.address_address);
         name_on_cake = (EditText)findViewById(R.id.address_name_on_cake);

        location_text = (TextView)findViewById(R.id.location);

        selected_area_text = (TextView)findViewById(R.id.selected_area_text);
        selected_delivery_date_text = (TextView)findViewById(R.id.selected_delivery_date_text);
        selected_tag_text = (TextView)findViewById(R.id.selected_tag_text);

        total_price_address = (TextView)findViewById(R.id.total_price_address);

        LinearLayout name_on_cake_layout = (LinearLayout) findViewById(R.id.name_on_cake_layout);
        LinearLayout date_of_delivery_layout = (LinearLayout) findViewById(R.id.date_of_delivery_layout);
        LinearLayout tag_layout = (LinearLayout) findViewById(R.id.tag_layout);


        discount_code_text = (EditText)findViewById(R.id.discount_code_text);
        apply_code_button = (Button)findViewById(R.id.apply_code_button);
      //  disc = (TextView)findViewById(R.id.discount_value_extra);

        name_on_cake_layout.setVisibility(address_visibility);
        date_of_delivery_layout.setVisibility(address_visibility);
        tag_layout.setVisibility(address_visibility);

        Intent entry1=getIntent();
        total_price = entry1.getStringExtra("total_price_key");
        total_price_address.setText("Rs "+ total_price);


        spinner_area = (Spinner) findViewById(R.id.spinner_area);
        spinner_area.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, area);


        spinner_area.setAdapter(adapter);


        spinner_delivery_date = (Spinner) findViewById(R.id.spinner_delivery_date);
        spinner_delivery_date.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, delivery_date);


        spinner_delivery_date.setAdapter(adapter2);


        spinner_tag = (Spinner) findViewById(R.id.spinner_tag);
        spinner_tag.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, celebration_tag);

        spinner_tag.setAdapter(adapter3);

        Button btn_pay = (Button)findViewById(R.id.btn_go_for_payment);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().length()==0){
                    name.setError("Enter your name");
                    name.requestFocus();
                 //   name.setText("name hai");
                }
                else if(contact.getText().toString().length()!=10){
                    contact.setError("valid mobile no. is necessary for order confirmation & 0/+91 not allowed");
                    contact.requestFocus();
                 //   contact.setText("1234567890");
                }
                else if(address.getText().toString().length()==0){
                    address.setError("Enter your address");
                    address.requestFocus();
                   // address.setText("address hai");
                }
                else if(selected_area_text.getText().equals(" Select near area")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Choose your near area", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();                }
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


        fetching_method();
        coupon_apply();

        discount_value_int_address = "0";

        Coupon_layout_address = (LinearLayout) findViewById(R.id.apply_code_layout);
        Coupon_layout_address.setVisibility(address_coupon_visibility);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){

            case R.id.spinner_area:
                Arrays.sort(area);
                selected_area_text.setText(area[position]);
                break;

            case R.id.spinner_delivery_date:
                Arrays.sort(delivery_date);

                selected_delivery_date_text.setText(delivery_date[position]);
                break;

            case R.id.spinner_tag:
                Arrays.sort(celebration_tag);
                selected_tag_text.setText(celebration_tag[position]);

        }
       //  Log.d("pppppppppppppoooooooooo","near_area "+selected_area_text);

//        Arrays.sort(delivery_date);
//
//        selected_delivery_date_text.setText(delivery_date[position]);



//        Arrays.sort(celebration_tag);
//
//        selected_tag_text.setText(celebration_tag[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }



    public void coupon_apply(){

        apply_code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(discount_code_text.getText().toString().isEmpty()){

                }
                else {
                    if (discount_code_text.getText().toString().equals(Coupon1)) {
                        discount_value_int_address = Coupon_value1;

                        Toast toast = Toast.makeText(getApplicationContext(), "Coupon Applied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();


                    } else if (discount_code_text.getText().toString().equals(Coupon2)) {
                        int abc = Integer.parseInt(Coupon_value2) * Integer.parseInt(total_price) / 100;
                        discount_value_int_address = String.valueOf(abc);

                        Toast toast = Toast.makeText(getApplicationContext(), "Coupon Applied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (discount_code_text.getText().toString().equals(Coupon3)) {
                        int abc = Integer.parseInt(Coupon_value3) * Integer.parseInt(total_price) / 100;

                        discount_value_int_address = String.valueOf(abc);

                        Toast toast = Toast.makeText(getApplicationContext(), "Coupon Applied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (discount_code_text.getText().toString().equals(Coupon4)) {
                        int abc = Integer.parseInt(Coupon_value4) * Integer.parseInt(total_price) / 100;

                        discount_value_int_address = String.valueOf(abc);

                        Toast toast = Toast.makeText(getApplicationContext(), "Coupon Applied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (discount_code_text.getText().toString().equals(Coupon5)) {
                        int abc = Integer.parseInt(Coupon_value5) * Integer.parseInt(total_price) / 100;

                        discount_value_int_address = String.valueOf(abc);

                        Toast toast = Toast.makeText(getApplicationContext(), "Coupon Applied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else if (discount_code_text.getText().toString().equals(Coupon6)) {
                        int abc = Integer.parseInt(Coupon_value6) * Integer.parseInt(total_price) / 100;

                        discount_value_int_address = String.valueOf(abc);
                        Toast toast = Toast.makeText(getApplicationContext(), "Coupon Applied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {
                        discount_code_text.setError("Wrong Code");
                        discount_code_text.requestFocus();

                    }

                }


                }
        });

    }


    public void fetching_method(){
        noteRef_Coupon.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess (DocumentSnapshot documentSnapshot){
                if (documentSnapshot.exists()) {
                    List<String> login = (List<String>) documentSnapshot.get("Coupon_Code");
                    Coupon1 = ("" + login.toArray()[0]);
                    Coupon2 = ("" + login.toArray()[1]);
                    Coupon3 = ("" + login.toArray()[2]);
                    Coupon4 = ("" + login.toArray()[3]);
                    Coupon5 = ("" + login.toArray()[4]);
                    Coupon6 = ("" + login.toArray()[5]);
                } else {
                    Toast.makeText(Address.this, "Document does not exist", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure (@NonNull Exception e){

                Toast.makeText(Address.this, "Failed to load , try again", Toast.LENGTH_SHORT).show();

            }
        });

        noteRef_Coupon_value.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess (DocumentSnapshot documentSnapshot){
                if (documentSnapshot.exists()) {
                    List<String> login = (List<String>) documentSnapshot.get("Coupon_Value");
                    Coupon_value1 = ("" + login.toArray()[0]);
                    Coupon_value2 = ("" + login.toArray()[1]);
                    Coupon_value3 = ("" + login.toArray()[2]);
                    Coupon_value4 = ("" + login.toArray()[3]);
                    Coupon_value5 = ("" + login.toArray()[4]);
                    Coupon_value6 = ("" + login.toArray()[5]);

                } else {
                    Toast.makeText(Address.this, "Document does not exist", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure (@NonNull Exception e){

                Toast.makeText(Address.this, "Failed to load , try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
