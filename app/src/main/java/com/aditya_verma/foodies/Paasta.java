package com.aditya_verma.foodies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Paasta extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;


    String pri1, pri2,pri3,pri4,pri5,pri6,pri7,pri8,pri9,pri10,pri11,pri12;

    private static final String TAG = "Subhraj Fast Food";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Subhraj Fast Food").document("Data");
    private DocumentReference noteRef1 = db.collection("Subhraj Fast Food").document("Images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paasta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Paasta.this,CartActivity.class);
                startActivity(loginIntent);
            }
        });

        Database database = new Database(this);
        TextView count_text = (TextView)findViewById(R.id.count_cart);

        int count = database.get_count_cart();

        count_text.setText(String.valueOf(count));

        card1();
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
                Intent Intent_home = new Intent(Paasta.this, MainActivity.class);
                startActivity(Intent_home);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.notification:
                Intent Intent_history = new Intent(Paasta.this, Shopping_history.class);
                startActivity(Intent_history);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;


            case R.id.inquiry:
                String phone_no = "9142735862";

                if (ActivityCompat.checkSelfPermission(Paasta.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Paasta.this,new String[]
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

    //Code for product card begining

    public void card1(){

        final ImageView image1 = (ImageView)findViewById(R.id.image1);
        final ImageView image2 = (ImageView)findViewById(R.id.image2);
        final ImageView image3 = (ImageView)findViewById(R.id.image3);
        final ImageView image4 = (ImageView)findViewById(R.id.image4);
        final ImageView image5 = (ImageView)findViewById(R.id.image5);
        final ImageView image6 = (ImageView)findViewById(R.id.image6);

        final TextView flavour1 =(TextView)findViewById(R.id.flavour1);
        final TextView flavour2 = (TextView)findViewById(R.id.flavour2);
        final TextView flavour3 = (TextView)findViewById(R.id.flavour3);
        final TextView flavour4 = (TextView)findViewById(R.id.flavour4);
        final TextView flavour5 = (TextView)findViewById(R.id.flavour5);
        final TextView flavour6 = (TextView)findViewById(R.id.flavour6);

        //  final TextView model1 =(TextView)findViewById(R.id.model1);
        // final TextView model2 =(TextView)findViewById(R.id.model2);

        final TextView old_price1_1 =(TextView)findViewById(R.id.old_price1_1);
        final TextView old_price1_2 =(TextView)findViewById(R.id.old_price1_2);
        final TextView old_price2_1 =(TextView)findViewById(R.id.old_price2_1);
        final TextView old_price2_2 =(TextView)findViewById(R.id.old_price2_2);
        final TextView old_price3_1 =(TextView)findViewById(R.id.old_price3_1);
        final TextView old_price3_2 =(TextView)findViewById(R.id.old_price3_2);
        final TextView old_price4_1 =(TextView)findViewById(R.id.old_price4_1);
        final TextView old_price4_2 =(TextView)findViewById(R.id.old_price4_2);
        final TextView old_price5_1 =(TextView)findViewById(R.id.old_price5_1);
        final TextView old_price5_2 =(TextView)findViewById(R.id.old_price5_2);
        final TextView old_price6_1 =(TextView)findViewById(R.id.old_price6_1);
        final TextView old_price6_2 =(TextView)findViewById(R.id.old_price6_2);

        RadioGroup radioGroup1 = (RadioGroup)findViewById(R.id.radiogroup1);
        final RadioButton price1_1 = (RadioButton)findViewById(R.id.radioButton1_1);
        final RadioButton price1_2 = (RadioButton)findViewById(R.id.radioButton1_2);
        final RadioButton price2_1 = (RadioButton)findViewById(R.id.radioButton2_1);
        final RadioButton price2_2 = (RadioButton)findViewById(R.id.radioButton2_2);
        final RadioButton price3_1 = (RadioButton)findViewById(R.id.radioButton3_1);
        final RadioButton price3_2 = (RadioButton)findViewById(R.id.radioButton3_2);
        final RadioButton price4_1 = (RadioButton)findViewById(R.id.radioButton4_1);
        final RadioButton price4_2 = (RadioButton)findViewById(R.id.radioButton4_2);
        final RadioButton price5_1 = (RadioButton)findViewById(R.id.radioButton5_1);
        final RadioButton price5_2 = (RadioButton)findViewById(R.id.radioButton5_2);
        final RadioButton price6_1 = (RadioButton)findViewById(R.id.radioButton6_1);
        final RadioButton price6_2 = (RadioButton)findViewById(R.id.radioButton6_2);

        Button button1 =(Button)findViewById(R.id.button1);
        Button button2 =(Button)findViewById(R.id.button2);
        Button button3 =(Button)findViewById(R.id.button3);
        Button button4 =(Button)findViewById(R.id.button4);
        Button button5 =(Button)findViewById(R.id.button5);
        Button button6 =(Button)findViewById(R.id.button6);

        old_price1_1.setPaintFlags(old_price1_1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price1_2.setPaintFlags(old_price1_2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price2_1.setPaintFlags(old_price2_1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price2_2.setPaintFlags(old_price2_2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price3_1.setPaintFlags(old_price3_1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price3_2.setPaintFlags(old_price3_2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price4_1.setPaintFlags(old_price4_1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price4_2.setPaintFlags(old_price4_2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price5_1.setPaintFlags(old_price5_1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price5_2.setPaintFlags(old_price5_2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price6_1.setPaintFlags(old_price6_1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        old_price6_2.setPaintFlags(old_price6_2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        noteRef.get() .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> flavour = (List<String>) documentSnapshot.get("Paasta_Name");
                    flavour1.setText("" +flavour.toArray()[0]);
                    flavour2.setText("" +flavour.toArray()[1]);
                    flavour3.setText("" +flavour.toArray()[2]);
                    flavour4.setText("" +flavour.toArray()[3]);
                    flavour5.setText("" +flavour.toArray()[4]);
                    flavour6.setText("" +flavour.toArray()[5]);

                    // List<String> model = (List<String>) documentSnapshot.get("model");
                    //model1.setText("Model :" +model.toArray()[0]);
                    //  model2.setText("Model :" +model.toArray()[1]);

                    List<String> old_price = (List<String>) documentSnapshot.get("Paasta_Old_Price");
                    old_price1_1.setText("" +old_price.toArray()[0]);
                    old_price1_2.setText("" +old_price.toArray()[1]);
                    old_price2_1.setText("" +old_price.toArray()[2]);
                    old_price2_2.setText("" +old_price.toArray()[3]);
                    old_price3_1.setText("" +old_price.toArray()[4]);
                    old_price3_2.setText("" +old_price.toArray()[5]);
                    old_price4_1.setText("" +old_price.toArray()[6]);
                    old_price4_2.setText("" +old_price.toArray()[7]);
                    old_price5_1.setText("" +old_price.toArray()[8]);
                    old_price5_2.setText("" +old_price.toArray()[9]);
                    old_price6_1.setText("" +old_price.toArray()[10]);
                    old_price6_2.setText("" +old_price.toArray()[11]);

                    //sample for string
                    // old_price1_1.setText(documentSnapshot.getString("old_price1_1"));

                    List<String> price = (List<String>) documentSnapshot.get("Paasta_Price");
                    pri1 = (String) price.toArray()[0];
                    pri2 = (String) price.toArray()[1];
                    pri3 = (String) price.toArray()[2];
                    pri4 = (String) price.toArray()[3];
                    pri5 = (String) price.toArray()[4];
                    pri6 = (String) price.toArray()[5];
                    pri7 = (String) price.toArray()[6];
                    pri8 = (String) price.toArray()[7];
                    pri9 = (String) price.toArray()[8];
                    pri10 = (String) price.toArray()[9];
                    pri11 = (String) price.toArray()[10];
                    pri12 = (String) price.toArray()[11];


                    price1_1.setText("(Half)Rs" +price.toArray()[0]);
                    price1_2.setText("(Full)Rs" +price.toArray()[1]);
                    price2_1.setText("(Half)Rs" +price.toArray()[2]);
                    price2_2.setText("(Full)Rs" +price.toArray()[3]);
                    price3_1.setText("(Half)Rs" +price.toArray()[4]);
                    price3_2.setText("(Full)Rs" +price.toArray()[5]);
                    price4_1.setText("(Half)Rs" +price.toArray()[6]);
                    price4_2.setText("(Full)Rs" +price.toArray()[7]);
                    price5_1.setText("(Half)Rs" +price.toArray()[8]);
                    price5_2.setText("(Full)Rs" +price.toArray()[9]);
                    price6_1.setText("(Full)Rs" +price.toArray()[10]);
                    price6_2.setText("(Full)Rs" +price.toArray()[11]);
                }

                else {
                    Toast.makeText(Paasta.this, "Document does not exist", Toast.LENGTH_SHORT).show();

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        //Code for one card end

        noteRef1.get() .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> image = (List<String>) documentSnapshot.get("Paasta");
                    //model1.setText("Model :" +model.toArray()[0]);
                    //  model2.setText("Model :" +model.toArray()[1]);

                    Glide.with(getApplicationContext()).load(image.toArray()[0]).into(image1);
                    Glide.with(getApplicationContext()).load(image.toArray()[1]).into(image2);
                    Glide.with(getApplicationContext()).load(image.toArray()[2]).into(image3);
                    Glide.with(getApplicationContext()).load(image.toArray()[3]).into(image4);
                    Glide.with(getApplicationContext()).load(image.toArray()[4]).into(image5);

                    List<String> image1 = (List<String>) documentSnapshot.get("Paasta");

                    Glide.with(getApplicationContext()).load(image1.toArray()[5]).into(image6);
                }

                else {
                    Toast.makeText(Paasta.this, "Document does not exist", Toast.LENGTH_SHORT).show();

                }
            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        button1.setOnTouchListener(btnTouch);
        button2.setOnTouchListener(btnTouch);
        button3.setOnTouchListener(btnTouch);
        button4.setOnTouchListener(btnTouch);
        button5.setOnTouchListener(btnTouch);
        button6.setOnTouchListener(btnTouch);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup1 = (RadioGroup)findViewById(R.id.radiogroup1);
                int selectedId = radioGroup1.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)findViewById(selectedId);

                if(selectedId == -1){
                    Toast.makeText(Paasta.this, " Please, select the Price", Toast.LENGTH_SHORT).show();
                }

                else{
                    switch (selectedId) {
                        case R.id.radioButton1_1:
                            Intent intent = new Intent(Paasta.this, Add_to_Cart.class);
                            String flavour = flavour1.getText().toString();
                            String price = pri1;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key", price);
                            // intent.putExtra("old_price_key",old_price);

                            Drawable drawable = image1.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                            break;

                        case R.id.radioButton1_2:

                            intent = new Intent(Paasta.this, Add_to_Cart.class);
                            flavour = flavour1.getText().toString();
                            price = pri2;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key",price);
                            // intent.putExtra("old_price_key",old_price);

                            drawable = image1.getDrawable();
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                            baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                    }
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup2 = (RadioGroup)findViewById(R.id.radiogroup2);
                int selectedId = radioGroup2.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)findViewById(selectedId);

                if(selectedId == -1){
                    Toast.makeText(Paasta.this, " Please, select the Price", Toast.LENGTH_SHORT).show();
                }

                else{
                    switch (selectedId) {
                        case R.id.radioButton2_1:
                            Intent intent=new Intent(Paasta.this, Add_to_Cart.class);
                            String flavour = flavour2.getText().toString();
                            String price = pri3;
                            // String old_price = old_price2_1.getText().toString();
                            intent.putExtra("flavour_key",flavour);
                            intent.putExtra("price_key",price);
                            // intent.putExtra("old_price_key",old_price);

                            Drawable drawable = image2.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG,100,baos);
                            byte[] b = baos.toByteArray();
                            intent.putExtra("image_key",b);
                            startActivity(intent);

                            break;

                        case R.id.radioButton2_2:

                            intent = new Intent(Paasta.this, Add_to_Cart.class);
                            flavour = flavour2.getText().toString();
                            price = pri4;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key",price);
                            // intent.putExtra("old_price_key",old_price);

                            drawable = image2.getDrawable();
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                            baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                    }
                }

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.radiogroup3);
                int selectedId = radioGroup3.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (selectedId == -1) {
                    Toast.makeText(Paasta.this, " Please, select the Price", Toast.LENGTH_SHORT).show();
                }
                else {
                    switch(selectedId) {
                        case R.id.radioButton3_1:

                            Intent intent = new Intent(Paasta.this, Add_to_Cart.class);
                            String flavour = flavour3.getText().toString();
                            String price = pri5;
                            // String old_price = old_price2_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key", price);
                            // intent.putExtra("old_price_key",old_price);

                            Drawable drawable = image3.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                            break;

                        case R.id.radioButton3_2:

                            intent = new Intent(Paasta.this, Add_to_Cart.class);
                            flavour = flavour3.getText().toString();
                            price = pri6;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key", price);
                            // intent.putExtra("old_price_key",old_price);

                            drawable = image3.getDrawable();
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                            baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                    }
                }

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.radiogroup4);
                int selectedId = radioGroup4.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (selectedId == -1) {
                    Toast.makeText(Paasta.this, " Please, select the Price", Toast.LENGTH_SHORT).show();
                }
                else{
                    switch (selectedId) {
                        case R.id.radioButton4_1:
                            Intent intent = new Intent(Paasta.this, Add_to_Cart.class);
                            String flavour = flavour4.getText().toString();
                            String price = pri7;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key", price);
                            // intent.putExtra("old_price_key",old_price);

                            Drawable drawable = image4.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                            break;

                        case R.id.radioButton4_2:

                            intent = new Intent(Paasta.this, Add_to_Cart.class);
                            flavour = flavour4.getText().toString();
                            price = pri8;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key",price);
                            // intent.putExtra("old_price_key",old_price);

                            drawable = image4.getDrawable();
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                            baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                    }
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup5 = (RadioGroup) findViewById(R.id.radiogroup5);
                int selectedId = radioGroup5.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (selectedId == -1) {
                    Toast.makeText(Paasta.this, " Please, select the Price", Toast.LENGTH_SHORT).show();
                }
                else{
                    switch (selectedId) {
                        case R.id.radioButton5_1:
                            Intent intent = new Intent(Paasta.this, Add_to_Cart.class);
                            String flavour = flavour5.getText().toString();
                            String price = pri9;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key", price);
                            // intent.putExtra("old_price_key",old_price);

                            Drawable drawable = image5.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                            break;

                        case R.id.radioButton5_2:

                            intent = new Intent(Paasta.this, Add_to_Cart.class);
                            flavour = flavour5.getText().toString();
                            price = pri10;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key",price);
                            // intent.putExtra("old_price_key",old_price);

                            drawable = image5.getDrawable();
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                            baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                    }
                }
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup6 = (RadioGroup) findViewById(R.id.radiogroup6);
                int selectedId = radioGroup6.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (selectedId == -1) {
                    Toast.makeText(Paasta.this, " Please, select the Price", Toast.LENGTH_SHORT).show();
                }
                else{
                    switch (selectedId) {
                        case R.id.radioButton6_1:
                            Intent intent = new Intent(Paasta.this, Add_to_Cart.class);
                            String flavour = flavour6.getText().toString();
                            String price = pri11;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key", price);
                            // intent.putExtra("old_price_key",old_price);

                            Drawable drawable = image6.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                            break;

                        case R.id.radioButton6_2:

                            intent = new Intent(Paasta.this, Add_to_Cart.class);
                            flavour = flavour6.getText().toString();
                            price = pri12;
                            // String old_price = old_price1_1.getText().toString();
                            intent.putExtra("flavour_key", flavour);
                            intent.putExtra("price_key",price);
                            // intent.putExtra("old_price_key",old_price);

                            drawable = image6.getDrawable();
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                            baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat
                                    .PNG, 100, baos);
                            b = baos.toByteArray();
                            intent.putExtra("image_key", b);
                            startActivity(intent);
                    }
                }
            }
        });

    }


    public View.OnTouchListener btnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getBackground().clearColorFilter();
                v.invalidate();
            }
            return false;
        }

    };
}