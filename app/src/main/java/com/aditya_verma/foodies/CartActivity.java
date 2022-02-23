package com.aditya_verma.foodies;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.aditya_verma.recyclerview_sqlite.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.aditya_verma.foodies.Address.location_text;

public class CartActivity extends AppCompatActivity {

    public static Database meradatabase;
    public static ArrayList<Model> allContacts = new ArrayList<>();
    private Adapter mAdapter = null;
    public static TextView total_value, total_product_price;
    Button go_for_address_btn;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        abc();
        total_value =(TextView) findViewById(R.id.total_value);
        total_product_price = (TextView)findViewById(R.id.total_product_price);

        go_for_address_btn = (Button)findViewById(R.id.btn_go_for_address);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        meradatabase = new Database(this);
        allContacts = meradatabase.get_all_data();

        if (allContacts.size() > 0) {
            go_for_address_btn.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter = new Adapter(this, allContacts);
            recyclerView.setAdapter(mAdapter);
        }
        else {
            go_for_address_btn.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(),"Your cart is empty", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CartActivity.this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                // get the location here

                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location locatio) {
                        if(locatio != null){
                            Double lat = locatio.getLatitude();
                            Double longt = locatio.getLongitude();

                            Payment.location_text=lat+" , "+longt;

                        }
                    }
                });

                fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        go_for_address_btn.setVisibility(View.INVISIBLE);
                    }
                });

            }

            else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }



    }

    public void abc(){

      final Button go_for_address_btn = (Button)findViewById(R.id.btn_go_for_address);
      go_for_address_btn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {

              Intent entry1 = new Intent(CartActivity.this, Address.class);
              String total_price = total_value.getText().toString();

              entry1.putExtra("total_price_key",total_price);

              startActivity(entry1);

          }
      });


    }
}