package com.aditya_verma.foodies;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class Shop_list extends AppCompatActivity {
    private static final int REQUEST_CALL =1 ;

    //This implementation for firebaseRecyclerOptions/Adapter
    // implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'

    private FirestoreRecyclerAdapter<Model_Shop_list, ViewHolder> adapter;

    ProgressBar progressBar;
    public static String Shop_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Shop_list.this,CartActivity.class);
                startActivity(loginIntent);
            }
        });


        Database database = new Database(this);
        TextView count_text = (TextView)findViewById(R.id.count_cart);

        int count = database.get_count_cart();

        count_text.setText(String.valueOf(count));


        new_method();

        progressBar_method();

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
                Intent Intent_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Intent_home);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.notification:
                Intent Intent_history = new Intent(getApplicationContext(), Shopping_history.class);
                startActivity(Intent_history);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.inquiry:
                String phone_no = MainActivity.shop_phone_no;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Shop_list.this,new String[]
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        final Query query = rootRef.collection(Shop_name);

        FirestoreRecyclerOptions<Model_Shop_list> options = new FirestoreRecyclerOptions.Builder<Model_Shop_list>()
                .setQuery(query, Model_Shop_list.class)
                .build();

        // main_act_database.addModel(query);

        adapter = new FirestoreRecyclerAdapter<Model_Shop_list, ViewHolder>(options) {


            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...
                e.printStackTrace();
            }

            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_for_shop_list, parent, false);
                return new ViewHolderOld(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Model_Shop_list model) {


                final ViewHolderOld viewHolderOld = (ViewHolderOld) holder;

                viewHolderOld.name.setText(model.getName());

                Glide.with(viewHolderOld.image.getContext()).load(model.getImage()).placeholder(R.mipmap.loading_img).into(viewHolderOld.image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainActivity.title == "Shubhraj") {

                            if (model.getName().equals("Chowmin") | model.getName().equals("Paasta")
                                    | model.getName().equals("Macroni") | model.getName().equals("Chilli & Manchurian")
                            | model.getName().equals("Snacks") | model.getName().equals("Juice & Shake")) {
                                Cake.Menu_Section_Cake = MainActivity.title + " " + model.getName();

                                Intent in = new Intent(Shop_list.this, Cake.class);
                                startActivity(in);
                                finish();
                            } else {
                                Jayka.Menu_Section = MainActivity.title + " " + model.getName();

                                Intent intent = new Intent(Shop_list.this, Jayka.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                       else if (MainActivity.title == "Food Circle") {

                            if (model.getName().equals("Chowmein") | model.getName().equals("Paasta")
                                    | model.getName().equals("Macroni") | model.getName().equals("Chilli")
                                    | model.getName().equals("Rice")) {
                                Cake.Menu_Section_Cake = MainActivity.title + " " + model.getName();

                                Intent in = new Intent(Shop_list.this, Cake.class);
                                startActivity(in);
                                finish();
                            } else {
                                Jayka.Menu_Section = MainActivity.title + " " + model.getName();

                                Intent intent = new Intent(Shop_list.this, Jayka.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Jayka.Menu_Section = MainActivity.title + " " + model.getName();

                            Intent intent = new Intent(Shop_list.this, Jayka.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });


            }

        };

        recyclerView.setAdapter(adapter);
    }

    private class ViewHolderOld extends ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView name;
        private ImageView image;


        public ViewHolderOld(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
            name = itemView.findViewById(R.id.shop_name);
            image = itemView.findViewById(R.id.shop_image);

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


    public void progressBar_method(){

        progressBar.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }
                catch (Exception e){

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();

    }


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backpressed = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backpressed);
        finish();
    }
}


