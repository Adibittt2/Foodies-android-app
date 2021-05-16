
package com.aditya_verma.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;



public class Add_to_Cart extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;

    private ImageView images;
    private TextView prices,flavours;

    Button addtocart;
    Database mdatabase;
    EditText quantities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to__cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // mdatabase = new Database(this,"DATABASE_NAME",null,5);
        mdatabase = new Database(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Add_to_Cart.this,CartActivity.class);
                startActivity(loginIntent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        TextView count_text = (TextView)findViewById(R.id.count_cart);

        int count = mdatabase.get_count_cart();
        mdatabase.close();
        count_text.setText(String.valueOf(count));

        images=(ImageView)findViewById(R.id.add_to_cart_image);
        flavours=(TextView)findViewById(R.id.add_to_cart_flavour);
        prices=(TextView)findViewById(R.id.add_to_cart_price);
        quantities=(EditText)findViewById(R.id.quantity_cart);

//        Spinner dropdown = (Spinner) findViewById(R.id.spinner_area_cart);
//        quantity_no = new String[]{"1","2","3","4","5","6","7","8","9","10"};
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quantity_no);
//
//        dropdown.setAdapter(adapter);
//

        // oldprices=(TextView)findViewById(R.id.add_to_cart_old_price);
        //oldprices.setPaintFlags(oldprices.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);


        Intent intent=getIntent();
        final String flavour=intent.getStringExtra("flavour_key");
        final String price_no=intent.getStringExtra("price_key");
     //   final String old_price=intent.getStringExtra("old_price_key");
        prices.setText("Rs"+ price_no);
        //oldprices.setText(old_price);
        flavours.setText(flavour);

//this code to import image

        Bundle bundle = getIntent().getExtras();
         byte[] b = bundle.getByteArray("image_key");
         Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        images.setImageBitmap(bmp);

//       importing image ends.


        addtocart=(Button)findViewById(R.id.add_to_cart);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String flavour = flavours.getText().toString();
                final String price = price_no;
                final String quantity = quantities.getText().toString();

                Drawable drawable = images.getDrawable();
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                byte[] image = baos.toByteArray();

                if(quantity.matches("0"))
                {
                    Toast.makeText(Add_to_Cart.this, "Quantity = 0 is invalid ", Toast.LENGTH_SHORT).show();
                }
                else if(quantity.matches("")){
                    Toast.makeText(Add_to_Cart.this, "Please,fill Quantity", Toast.LENGTH_SHORT).show();
                }
                else{
                    Model model = new Model(flavour, price,quantity,image);

                    mdatabase.addModel(model);
                    Toast.makeText(Add_to_Cart.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                }

                finish();
                startActivity(getIntent());
            }
        });

    }


//    @Override
//    public void onBackPressed(){
//        startActivity(getIntent());
//        super.onBackPressed();
//
//    }

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
                Intent Intent_home = new Intent(Add_to_Cart.this, MainActivity.class);
                startActivity(Intent_home);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.notification:
                Intent Intent_history = new Intent(Add_to_Cart.this, Shopping_history.class);
                startActivity(Intent_history);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.inquiry:
                String phone_no = "9142735862";

                if (ActivityCompat.checkSelfPermission(Add_to_Cart.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Add_to_Cart.this,new String[]
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

}
