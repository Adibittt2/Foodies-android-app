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
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.INotificationSideChannel;
import android.view.Gravity;
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
import java.util.List;

public class Cake extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    //This implementation for firebaseRecyclerOptions/Adapter
    // implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'

    private FirestoreRecyclerAdapter<Model_Cake, ViewHolder> adapter;

    ProgressBar progressBar;
    public static String Menu_Section_Cake;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Cake.this, CartActivity.class);
                startActivity(loginIntent);
            }
        });


        Database database = new Database(this);
        TextView count_text = (TextView) findViewById(R.id.count_cart);

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
                    ActivityCompat.requestPermissions(Cake.this, new String[]
                            {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    Intent call = new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:" + phone_no));
                    startActivity(call);
                }
                return true;

            case R.id.feedback:

                Intent feedback = new Intent(getApplicationContext(), Feedback.class);
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        final Query query = rootRef.collection(Menu_Section_Cake);

        FirestoreRecyclerOptions<Model_Cake> options = new FirestoreRecyclerOptions.Builder<Model_Cake>()
                .setQuery(query, Model_Cake.class)
                .build();

        // main_act_database.addModel(query);

        adapter = new FirestoreRecyclerAdapter<Model_Cake, ViewHolder>(options) {

            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...

                e.printStackTrace();
            }

            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card2_for_foodies, parent, false);
                return new ViewHolderOld(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Model_Cake model) {


                final ViewHolderOld viewHolderOld = (ViewHolderOld) holder;

                int difference1 = Integer.parseInt(model.getOld_price1()) - Integer.parseInt(model.getNew_price1());
                int divide1 = difference1 * 100 / Integer.parseInt(model.getOld_price1());

                int difference2 = Integer.parseInt(model.getOld_price2()) - Integer.parseInt(model.getNew_price2());
                int divide2 = difference2 * 100 / Integer.parseInt(model.getOld_price2());


                if (model.getAvailable().contains("OFF")) {
                    viewHolderOld.old_price1.setPaintFlags(viewHolderOld.old_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolderOld.old_price2.setPaintFlags(viewHolderOld.old_price2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    viewHolderOld.name.setText(model.getName());
                    viewHolderOld.new_price1.setText("Rs" + model.getNew_price1());
                    viewHolderOld.new_price2.setText("Rs" + model.getNew_price2());
                    viewHolderOld.old_price1.setText("Rs" + model.getOld_price1());
                    viewHolderOld.old_price2.setText("Rs" + model.getOld_price2());
                    Glide.with(Cake.this).load("https://media0.giphy.com/media/Y21AQ2KmCKhzCI50DU/giphy.gif?cid=6c09b952i9hjkjz2w7471l3zcidwj4g58vnn1zkxyoypczg2&rid=giphy.gif").into(viewHolderOld.image);
                    viewHolderOld.description.setText("Out of Stock");
                    viewHolderOld.itemView.setClickable(false);
                    viewHolderOld.tag.setVisibility(View.INVISIBLE);


                    if (divide1 > 0) {
                        viewHolderOld.old_price1.setText("Rs" + model.getOld_price1());
                        viewHolderOld.discount1.setText(divide1 + "% OFF");
                    }
                    else {
                        viewHolderOld.old_price1.setText("");
                        viewHolderOld.discount1.setText("");
                    }

                    if (divide2 > 0) {
                        viewHolderOld.old_price2.setText("Rs" + model.getOld_price2());
                        viewHolderOld.discount2.setText(divide2 + "% OFF");
                    }
                    else {
                        viewHolderOld.old_price2.setText("");
                        viewHolderOld.discount2.setText("");
                    }



                }

                else {

                    viewHolderOld.old_price1.setPaintFlags(viewHolderOld.old_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolderOld.old_price2.setPaintFlags(viewHolderOld.old_price2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    viewHolderOld.description.setText(model.getDescription());
                    viewHolderOld.name.setText(model.getName());
                    viewHolderOld.new_price1.setText("Rs" + model.getNew_price1());
                    viewHolderOld.new_price2.setText("Rs" + model.getNew_price2());
                    viewHolderOld.old_price1.setText("Rs" + model.getOld_price1());
                    viewHolderOld.old_price2.setText("Rs" + model.getOld_price2());
                    viewHolderOld.tag.setText(model.getTag());

                    Glide.with(viewHolderOld.image.getContext()).load(model.getImage()).placeholder(R.mipmap.loading_img).into(viewHolderOld.image);

//                      viewHolderOld.discount1.setText(divide1 + "% OFF");
//                       viewHolderOld.discount2.setText(divide2 + "% OFF");

                    if (divide1 > 0) {
                        viewHolderOld.old_price1.setText("Rs" + model.getOld_price1());
                        viewHolderOld.discount1.setText(divide1 + "% OFF");
                    }
                    else {
                        viewHolderOld.old_price1.setText("");
                        viewHolderOld.discount1.setText("");
                    }

                    if (divide2 > 0) {
                        viewHolderOld.old_price2.setText("Rs" + model.getOld_price2());
                        viewHolderOld.discount2.setText(divide2 + "% OFF");
                    }
                    else {
                        viewHolderOld.old_price2.setText("");
                        viewHolderOld.discount2.setText("");
                    }

                    viewHolderOld.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // RadioGroup radioGroup1 = (RadioGroup)findViewById(R.id.card2_radiogroup);
                            int selectedId = viewHolderOld.radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton)findViewById(selectedId);

                            if(selectedId == -1){
                                Toast toast = Toast.makeText(getApplicationContext(),"Select Price for half/full plate", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();                        }

                            else{
                                switch (selectedId) {
                                    case R.id.card2_radiobtn1_1:
                                        Intent intent = new Intent(Cake.this, Add_to_Cart.class);
                                        String flavour = model.getName();
                                        String price = model.getNew_price1();
                                        intent.putExtra("flavour_key", flavour);
                                        intent.putExtra("price_key", price);

                                        Drawable drawable = viewHolderOld.image.getDrawable();
                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat
                                                .PNG, 100, baos);
                                        byte[] b = baos.toByteArray();
                                        intent.putExtra("image_key", b);
                                        startActivity(intent);
                                        break;

                                    case R.id.card2_radiobtn1_2:

                                        intent = new Intent(Cake.this, Add_to_Cart.class);
                                        flavour = model.getName();
                                        price = model.getNew_price2();
                                        intent.putExtra("flavour_key", flavour);
                                        intent.putExtra("price_key", price);

                                        drawable = viewHolderOld.image.getDrawable();
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

               // viewHolderOld.button.setOnTouchListener(btn_touch);





            }

        };

        recyclerView.setAdapter(adapter);
    }

    private class ViewHolderOld extends ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView description, name, old_price1, old_price2, new_price1, new_price2, tag, discount1, discount2;
        private ImageView image;
        RadioGroup radioGroup;


        public ViewHolderOld(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
            description = itemView.findViewById(R.id.card2_foodies_description);
            name = itemView.findViewById(R.id.card2_foodies_name);
            radioGroup = itemView.findViewById(R.id.card2_radiogroup);
            old_price1 = itemView.findViewById(R.id.card2_foodies_old_price1_1);
            old_price2 = itemView.findViewById(R.id.card2_foodies_old_price1_2);
            new_price1 = itemView.findViewById(R.id.card2_radiobtn1_1);
            new_price2 = itemView.findViewById(R.id.card2_radiobtn1_2);
            tag = itemView.findViewById(R.id.card2_foodies_tag);
            discount1 = itemView.findViewById(R.id.card2_foodies_discount1_1);
            discount2 = itemView.findViewById(R.id.card2_foodies_discount1_2);

            image = itemView.findViewById(R.id.card2_foodies_image);

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


    public void progressBar_method() {

        progressBar.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

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

        Intent backpressed = new Intent(Cake.this, Shop_list.class);
        startActivity(backpressed);
        finish();
    }

}