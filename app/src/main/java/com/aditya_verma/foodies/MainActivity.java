package com.aditya_verma.foodies;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef1 = db.collection("Shop_On_Off").document("Cakery_Order");
    private DocumentReference noteRef2 = db.collection("Shop_On_Off").document("S_Raj_Order");


    String Cakery_On_Off,S_Raj_On_Off;
    public  ImageButton cakery;
    private Database main_activity_database;
    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
   // FirebaseFirestore db;
    private SliderView sliderView;

    private FirebaseRemoteConfig remoteConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_activity_database = new Database(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        check_internet();

        shop_on_off();
        // creating a new array list fr our array list.
        sliderDataArrayList = new ArrayList<>();

        // initializing our slider view and
        // firebase firestore instance.
        sliderView = findViewById(R.id.slider);

        // calling our method to load images.
        loadImages();

        Cakery_entry();

        HashMap<String,Object> defaultsRate = new HashMap<>();
        defaultsRate.put("new Version Code",String.valueOf(getVersionCode()));

        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(10).build();

        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(defaultsRate);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()){
                    final String new_version_code = remoteConfig.getString("new_version_code");
                    if(Integer.parseInt(new_version_code) > getVersionCode()){
                        showDialog(new_version_code);
                    }
                    else{
                    }
                }
            }
        });
    }

    public void check_internet(){
        ConnectivityManager manager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null!=activeNetwork) {
//            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//
//                Toast.makeText(this, "wifi Enabled", Toast.LENGTH_SHORT).show();
//            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//
//                Toast.makeText(this, "Data Network Enabled", Toast.LENGTH_SHORT).show();
//            }
        }
           else{
            Intent loginIntent = new Intent(MainActivity.this,Error_Activity.class);
            startActivity(loginIntent);
            finish();

            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show();
           }
    }

    private void showDialog(String VersionFromRemoteConfig){
        final AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("New Update Available")
            .setMessage("Previous Version: 1 \nLatest Version here:" + VersionFromRemoteConfig)
            .setPositiveButton("UPDATE",null).show();
        dialog.setCancelable(false);

    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

    final Drawable positiveButtonDrawable = getResources().getDrawable(R.drawable.badge_shape);
        positiveButton.setBackground(positiveButtonDrawable);
        positiveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=\" + \"com.aditya_verma.foodies")));
            }
            catch (android.content.ActivityNotFoundException anfe){
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id="+"com.aditya_verma.foodies")));
            }
        }
    });
}

    public void Cakery_entry() {


        final ImageButton foodies_fast_food = (ImageButton) findViewById(R.id.main_foodies);


        foodies_fast_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (S_Raj_On_Off.equals("OFF")) {
                    foodies_fast_food.setImageResource(R.mipmap.sorry);
                    Toast.makeText(MainActivity.this, "Shop is not open", Toast.LENGTH_SHORT).show();
                } else if (S_Raj_On_Off.equals("ON")) {

                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Do you want to delete all items from your cart")
                            .setMessage("Do you want to delete all items from your cart")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", null).show();
                    // dialog.setCancelable(false);

                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    final Drawable negativeButtonDrawable = getResources().getDrawable(R.drawable.badge_shape);
                    negativeButton.setBackground(negativeButtonDrawable);
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    final Drawable positiveButtonDrawable1 = getResources().getDrawable(R.drawable.badge_shape);
                    positiveButton.setBackground(positiveButtonDrawable1);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            main_activity_database.delete_all_Model();
                            Intent entry = new Intent(MainActivity.this, Foodies.class);
                            startActivity(entry);
                            dialog.hide();
                            Payment.collection = "Foodies_Order";
                        }
                    });


                }
            }
        });

         cakery = (ImageButton) findViewById(R.id.main_cakery);
        cakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cakery_On_Off.equals("OFF")) {

                    cakery.setImageResource(R.mipmap.sorry);
                    Toast.makeText(MainActivity.this, "Shop is not open", Toast.LENGTH_SHORT).show();
                } else if (Cakery_On_Off.equals("ON")) {

                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Do you want to delete all items from your cart")
                            .setMessage("Do you want to delete all items from your cart")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", null).show();
                    // dialog.setCancelable(false);

                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    final Drawable negativeButtonDrawable = getResources().getDrawable(R.drawable.badge_shape);
                    negativeButton.setBackground(negativeButtonDrawable);
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    final Drawable positiveButtonDrawable1 = getResources().getDrawable(R.drawable.badge_shape);
                    positiveButton.setBackground(positiveButtonDrawable1);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            main_activity_database.delete_all_Model();
                            Intent entry = new Intent(MainActivity.this, Cake.class);
                            startActivity(entry);
                            dialog.hide();
                            Payment.collection = "Cakery_Order";
                        }
                    });

                }
            }
        });

       final ImageButton shubhraj_fast_food = (ImageButton) findViewById(R.id.imageButton3);


        shubhraj_fast_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (S_Raj_On_Off.equals("OFF")) {
                    shubhraj_fast_food.setImageResource(R.mipmap.sorry);
                    Toast.makeText(MainActivity.this, "Shop is not open", Toast.LENGTH_SHORT).show();
                } else if (S_Raj_On_Off.equals("ON")) {

                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Do you want to delete all items from your cart")
                            .setMessage("Do you want to delete all items from your cart")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", null).show();
                    // dialog.setCancelable(false);

                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    final Drawable negativeButtonDrawable = getResources().getDrawable(R.drawable.badge_shape);
                    negativeButton.setBackground(negativeButtonDrawable);
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    final Drawable positiveButtonDrawable1 = getResources().getDrawable(R.drawable.badge_shape);
                    positiveButton.setBackground(positiveButtonDrawable1);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            main_activity_database.delete_all_Model();
                            Intent entry = new Intent(MainActivity.this, Fast_food.class);
                            startActivity(entry);
                            dialog.hide();
                            Payment.collection = "S_Raj_Order";
                        }
                    });


                }
            }
        });
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
                Intent Intent_home = new Intent(MainActivity.this,MainActivity.class);
                startActivity(Intent_home);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.notification:
                Intent Intent_history = new Intent(MainActivity.this,Shopping_history.class);
                startActivity(Intent_history);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;


            case R.id.inquiry:
                    String phone_no = "9142735862";
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]
                            {Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }
                else {
                    Intent call = new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:"+ phone_no));
                    startActivity(call);
                }
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
//here code of toolbar ends

    private void loadImages() {
        // getting data from our collection and after
        // that calling a method for on success listener.
        db.collection("Slider").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // inside the on success method we are running a for loop
                // and we are getting the data from Firebase Firestore
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    // after we get the data we are passing inside our object class.
                    SliderData sliderData = documentSnapshot.toObject(SliderData.class);
                    SliderData model = new SliderData();

                    // below line is use for setting our
                    // image url for our modal class.
                    model.setImgUrl(sliderData.getImgUrl());

                    // after that we are adding that
                    // data inside our array list.
                    sliderDataArrayList.add(model);

                    // after adding data to our array list we are passing
                    // that array list inside our adapter class.
                    adapter = new SliderAdapter(MainActivity.this, sliderDataArrayList);

                    // belows line is for setting adapter
                    // to our slider view
                    sliderView.setSliderAdapter(adapter);

                    // below line is for setting animation to our slider.
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                    // below line is for setting auto cycle duration.
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                    // below line is for setting
                    // scroll time animation
                    sliderView.setScrollTimeInSec(2);

                    // below line is for setting auto
                    // cycle animation to our slider
                    sliderView.setAutoCycle(true);

                    // below line is use to start
                    // the animation of our slider view.
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we get any error from Firebase we are
                // displaying a toast message for failure
                Toast.makeText(MainActivity.this, "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getVersionCode(){
        PackageInfo packageInfo = null;
        try{
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("MYLOG","Name not Found Exception" +e.getMessage());
        }
        return Objects.requireNonNull(packageInfo).versionCode;
    }

    public void shop_on_off(){
        noteRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    Cakery_On_Off = (String) documentSnapshot.get("Shop");
                //    Toast.makeText(MainActivity.this, Shop_On_Off, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        noteRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    S_Raj_On_Off = (String) documentSnapshot.get("Shop");
                    //    Toast.makeText(MainActivity.this, Shop_On_Off, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }




}