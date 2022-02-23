package com.aditya_verma.foodies;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import android.util.Log;
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

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Jayka extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    //This implementation for firebaseRecyclerOptions/Adapter
    // implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'

    private FirestoreRecyclerAdapter<Model_Foodies, ViewHolder> adapter;

    Database database;
    ProgressBar progressBar;
    public static String Menu_Section;

    private List<Model_Foodies> cache_list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jayka);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Jayka.this, CartActivity.class);
                startActivity(loginIntent);
            }
        });


        database = new Database(this);
        TextView count_text = (TextView) findViewById(R.id.count_cart);

        int count = database.get_count_cart();

        count_text.setText(String.valueOf(count));


        new_method();

        progressBar_method();

        load_cache();

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
                Intent Intent_home = new Intent(Jayka.this, MainActivity.class);
                startActivity(Intent_home);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.notification:
                Intent Intent_history = new Intent(Jayka.this, Shopping_history.class);
                startActivity(Intent_history);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.inquiry:
                String phone_no = MainActivity.shop_phone_no;
                if (ActivityCompat.checkSelfPermission(Jayka.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Jayka.this, new String[]
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        final Query query = rootRef.collection(Menu_Section);

        FirestoreRecyclerOptions<Model_Foodies> options = new FirestoreRecyclerOptions.Builder<Model_Foodies>()
                .setQuery(query, Model_Foodies.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Model_Foodies, ViewHolder>(options) {


            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...
               e.printStackTrace();

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

                if (model.getAvailable().contains("OFF")) {

                    viewHolderOld.old_price.setPaintFlags(viewHolderOld.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    viewHolderOld.name.setText(model.getName());
                    viewHolderOld.new_price.setText("Rs" + model.getNew_price());


                    Glide.with(Jayka.this).load("https://media0.giphy.com/media/Y21AQ2KmCKhzCI50DU/giphy.gif?cid=6c09b952i9hjkjz2w7471l3zcidwj4g58vnn1zkxyoypczg2&rid=giphy.gif").into(viewHolderOld.image);

                    //  viewHolderOld.image.setImageResource(R.mipmap.sorry);
                    viewHolderOld.description.setText("Out of Stock");
                  //  viewHolderOld.button.setVisibility(View.INVISIBLE);
                    viewHolderOld.itemView.setClickable(false);
                    viewHolderOld.tag.setVisibility(View.INVISIBLE);

                    if (divide > 0) {
                        viewHolderOld.old_price.setText("Rs" + model.getOld_price());
                        viewHolderOld.discount.setText(divide + "% OFF");
                    }
                    else {
                        viewHolderOld.old_price.setText("");
                        viewHolderOld.discount.setText("");
                    }

                } else {


                  //  viewHolderOld.button.setVisibility(View.VISIBLE);

                    viewHolderOld.old_price.setPaintFlags(viewHolderOld.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    viewHolderOld.description.setText(model.getDescription());
                    viewHolderOld.name.setText(model.getName());
                    viewHolderOld.new_price.setText("Rs" + model.getNew_price());

                    viewHolderOld.tag.setText(model.getTag());
                    viewHolderOld.timer.setText(model.getTimer());

                    Glide.with(Jayka.this).load("https://media0.giphy.com/media/Y21AQ2KmCKhzCI50DU/giphy.gif?cid=6c09b952i9hjkjz2w7471l3zcidwj4g58vnn1zkxyoypczg2&rid=giphy.gif").into(viewHolderOld.image);

                    Glide.with(viewHolderOld.image.getContext()).load(model.getImage()).placeholder(R.mipmap.loading_img).into(viewHolderOld.image);

                    if (divide > 0) {
                        viewHolderOld.old_price.setText("Rs" + model.getOld_price());
                        viewHolderOld.discount.setText(divide + "% OFF");
                    } else {
                        viewHolderOld.old_price.setText("");
                        viewHolderOld.discount.setText("");
                    }


                  //  viewHolderOld.button.setOnTouchListener(btn_touch);

                    viewHolderOld.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Jayka.this, Add_to_Cart.class);
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
        private TextView description, name, old_price, new_price, tag, discount, timer;
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
            timer = itemView.findViewById(R.id.card_foodies_count_down);

            image = itemView.findViewById(R.id.card_foodies_image);

           // button = itemView.findViewById(R.id.card_foodies_button);
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

        Intent backpressed = new Intent(Jayka.this, Shop_list.class);
        startActivity(backpressed);
        finish();
    }


    private void load_cache(){
        String url2 = "http://api.tvmaze.com/singlesearch/shows?q=lost&embed=episodes";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject emedded = null;
                    try {
                        emedded = response.getJSONObject("_embedded");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray episodes = emedded.getJSONArray("episodes");
                    for (int i = 0; i <episodes.length() ; i++) {

                        JSONObject episode = episodes.getJSONObject(i);
                        String name = episode.getString("name");
                        Log.i("Provera Object", name);

//                        JSONObject imaged = episode.getJSONObject("image");
//                        String image = imaged.getString("medium");
//                        Log.i("Provera Object", String.valueOf(image));

                        int id = episode.getInt("id");
                        Log.d("Provera Object", String.valueOf(id));
                        //   Log.d("Provera","token "+shop_token_id);

                        String image = episode.getString("url");
                        Log.d("Provera Object", image);

                        String new_price  = episode.getString("new_price");
                        Log.d("Provera Object", new_price);

                        String old_price = episode.getString("old_price");
                        Log.d("Provera Object", old_price);

                        String description = episode.getString("description");
                        Log.d("Provera Object", description);

                        String tag = episode.getString("tag");
                        Log.d("Provera Object", tag);

                        String timer = episode.getString("timer");
                        Log.d("Provera Object", timer);

                        String available = episode.getString("available");
                        Log.d("Provera Object", available);


                        Model_Foodies model_foodies_item = new Model_Foodies(
                                description, image, name, new_price, old_price, tag, available, timer
                        );

                        cache_list.add(model_foodies_item);
                        adapter.notifyDataSetChanged();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        Volley.newRequestQueue(this).add(jsonObjReq);
    }



}