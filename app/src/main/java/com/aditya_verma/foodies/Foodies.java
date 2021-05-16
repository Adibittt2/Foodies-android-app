package com.aditya_verma.foodies;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.io.ByteArrayOutputStream;

public class Foodies extends AppCompatActivity {
    private static final int REQUEST_CALL =1 ;

    //This implementation for firebaseRecyclerOptions/Adapter
    // implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'

    private FirestoreRecyclerAdapter<Model_Foodies, ViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Foodies.this,CartActivity.class);
                startActivity(loginIntent);
            }
        });


        Database database = new Database(this);
        TextView count_text = (TextView)findViewById(R.id.count_cart);

        int count = database.get_count_cart();

        count_text.setText(String.valueOf(count));


        new_method();

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
                Intent Intent_home = new Intent(Foodies.this, MainActivity.class);
                startActivity(Intent_home);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.notification:
                Intent Intent_history = new Intent(Foodies.this, Shopping_history.class);
                startActivity(Intent_history);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.inquiry:
                String phone_no = "9142735862";

                if (ActivityCompat.checkSelfPermission(Foodies.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Foodies.this,new String[]
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


    public void new_method() {

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        final Query query = rootRef.collection("Foodies");

        FirestoreRecyclerOptions<Model_Foodies> options = new FirestoreRecyclerOptions.Builder<Model_Foodies>()
                .setQuery(query, Model_Foodies.class)
                .build();

        // main_act_database.addModel(query);

        adapter = new FirestoreRecyclerAdapter<Model_Foodies, ViewHolder>(options) {


            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...

            }

            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_foodies, parent, false);
                return new ViewHolderOld(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Model_Foodies model) {


                final ViewHolderOld viewHolderOld = (ViewHolderOld) holder;

                int difference = Integer.parseInt(model.getOld_price()) - Integer.parseInt(model.getNew_price());
                int divide = difference * 100 / Integer.parseInt(model.getOld_price());

                if(model.getAvailable().contains("OFF")){
                    viewHolderOld.old_price.setPaintFlags(viewHolderOld.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    viewHolderOld.name.setText(model.getName());
                    viewHolderOld.new_price.setText("Rs" + model.getNew_price());
                    viewHolderOld.old_price.setText("Rs" + model.getOld_price());
                    viewHolderOld.image.setImageResource(R.mipmap.sorry);
                    viewHolderOld.description.setText("Out of Stock");
                    viewHolderOld.button.setVisibility(View.INVISIBLE);
                    viewHolderOld.tag.setVisibility(View.INVISIBLE);
                    viewHolderOld.discount.setText(divide + "% OFF");
                    
                }
                else{

                    viewHolderOld.old_price.setPaintFlags(viewHolderOld.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                viewHolderOld.description.setText(model.getDescription());
                viewHolderOld.name.setText(model.getName());
                viewHolderOld.new_price.setText("Rs" + model.getNew_price());
                viewHolderOld.old_price.setText("Rs" + model.getOld_price());
                viewHolderOld.tag.setText(model.getTag());

                Glide.with(viewHolderOld.image.getContext()).load(model.getImage()).into(viewHolderOld.image);

                viewHolderOld.discount.setText(divide + "% OFF");

                viewHolderOld.button.setOnTouchListener(btn_touch);

                viewHolderOld.button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Foodies.this, Add_to_Cart.class);
                        String flavour = model.getName();
                        String price = model.getNew_price();
                        intent.putExtra("flavour_key", flavour);
                        intent.putExtra("price_key", price);

                        Drawable drawable = viewHolderOld.image.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        intent.putExtra("image_key", b);
                        startActivity(intent);
                    }

                });

            }

                }

        };

        recyclerView.setAdapter(adapter);
    }

    private class ViewHolderOld extends ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView description,name, old_price, new_price,tag,discount;
        private ImageView image;
        private Button button;


        public ViewHolderOld(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
            description = itemView.findViewById(R.id.card_foodies_description);
           // avialable = itemView.findViewById(R.id.card_foodies_avialable);
            name = itemView.findViewById(R.id.card_foodies_name);
            old_price = itemView.findViewById(R.id.card_foodies_old_price);
            new_price = itemView.findViewById(R.id.card_foodies_new_price);
            tag = itemView.findViewById(R.id.card_foodies_tag);
            discount = itemView.findViewById(R.id.card_foodies_discount);

            image = itemView.findViewById(R.id.card_foodies_image);

            button = itemView.findViewById(R.id.card_foodies_button);
        }

    }

    public View.OnTouchListener btn_touch = new View.OnTouchListener() {
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


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}


