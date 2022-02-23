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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.io.ByteArrayOutputStream;

public class MainActivity2 extends AppCompatActivity {
    private static final int REQUEST_CALL =1 ;

    //This implementation for firebaseRecyclerOptions/Adapter
    // implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'

    private FirestoreRecyclerAdapter<Image_slider_model, ViewHolder> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        new_method();

    }



    public void new_method() {

        ViewPager recyclerView = findViewById(R.id.viewpager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        final Query query = rootRef.collection("Slider");

        FirestoreRecyclerOptions<Image_slider_model> options = new FirestoreRecyclerOptions.Builder<Image_slider_model>()
                .setQuery(query, Image_slider_model.class)
                .build();

        // main_act_database.addModel(query);

        adapter = new FirestoreRecyclerAdapter<Image_slider_model, ViewHolder>(options) {


            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...
                e.printStackTrace();
            }

            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_card, parent, false);
                return new ViewHolderOld(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Image_slider_model model) {


                final ViewHolderOld viewHolderOld = (ViewHolderOld) holder;


                Glide.with(viewHolderOld.image.getContext()).load(model.getImage()).into(viewHolderOld.image);

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
            image = itemView.findViewById(R.id.image_view);

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


