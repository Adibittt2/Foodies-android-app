package com.aditya_verma.foodies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.aditya_verma.recyclerview_sqlite.R;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    public static Database meradatabase;
    public static ArrayList<Model> allContacts = new ArrayList<>();
    private Adapter mAdapter = null;
    public static TextView total_value, total_product_price;
    Button go_for_address_btn;

    
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
            Toast.makeText(this, "No food items in Cart. Start adding Now", Toast.LENGTH_LONG).show();
        }

    }

    public void abc(){

      Button go_for_address_btn = (Button)findViewById(R.id.btn_go_for_address);
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