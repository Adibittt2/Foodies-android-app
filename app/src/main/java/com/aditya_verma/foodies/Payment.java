package com.aditya_verma.foodies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Payment extends AppCompatActivity implements PaymentStatusListener {


    RecyclerView table_recyclerView;
    Adapter_table_view adapter_table_view;
    public static ArrayList<Model> table_list = new ArrayList<>();

    LinearLayout coupon_layout;
    RadioGroup coupon_radio_group;
    public static int visibility, int_deli_value;
    public static String transcDetails;
    public String Upi_id;
    RadioButton coupon_none, coupon_10,coupon_20,coupon_50;



    private DocumentReference documentReference;
    private FirebaseFirestore payment_db;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef_shop = db.collection("Shop_token").document(collection);
    private DocumentReference noteRef_rider = db.collection("Rider_token").document(rider_collection);
    private DocumentReference noteRef2 = db.collection("SMS").document(collection);
    private DocumentReference noteRef_upi = db.collection("Shop_Upi_id").document(collection);


    Button pay_button;
    public static String collection, location_text, rider_collection;
    Database payment_database;
    TextView payment_final_total_price, products_cost, delivery_charge, transactionDetailsTV;
    int final_value, deli_charge_value;
    String total_product_value,mode_of_payment,name,mobile,address,near_area,total_bill_price,date,coupon_value,user_token;
    List<String>food_list;
    String shop_token_id, rider_token_id, receiving_sms;

    TextView discount_value_textview, discount_textview;

     int discount_value_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        token_receiving_method();
// for sending notification to all
        FirebaseMessaging.getInstance().subscribeToTopic("all");


        coupon_layout = (LinearLayout) findViewById(R.id.coupon_layout);
        coupon_radio_group = (RadioGroup)findViewById(R.id.address_coupon_radiogroup);
        coupon_layout.setVisibility(visibility);

        coupon_none = (RadioButton)findViewById(R.id.coupon_none);
        coupon_10 = (RadioButton)findViewById(R.id.coupon_10);
        coupon_20 = (RadioButton)findViewById(R.id.coupon_20);
        coupon_50 = (RadioButton)findViewById(R.id.coupon_50);


        payment_final_total_price = (TextView) findViewById(R.id.payment_final_total_price);
        products_cost = (TextView) findViewById(R.id.products_cost);
        delivery_charge = (TextView) findViewById(R.id.delivery_charge);
        transactionDetailsTV =(TextView)findViewById(R.id.idTVTranscationDetails);

        discount_value_textview = (TextView)findViewById(R.id.discount_value_textview);
        discount_textview = (TextView)findViewById(R.id.discount_textview);



        payment_database = new Database(this);

        payment_db = FirebaseFirestore.getInstance();

        setRecview_table();

        Intent entry2 = getIntent();
        total_product_value = entry2.getStringExtra("total_price_payment_key");
       // String total_product_value = "200";
        products_cost.setText("Rs." + total_product_value);

       // setReceiving_sms();

        pay_button = findViewById(R.id.payment_price_btn);
        pay_button.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                abc();

            }
        });

        coupon_method();

        upi_fetch();

        discount_value_int = Integer.parseInt(Address.discount_value_int_address);
        discount_value_textview = (TextView)findViewById(R.id.discount_value_textview);

        if(Address.discount_value_int_address == "0") {
            discount_value_textview.setVisibility(View.INVISIBLE);
            discount_textview.setVisibility(View.INVISIBLE);
        }
        else {
            discount_value_textview.setText("Rs " + discount_value_int);

        }

    }


    public void coupon_method(){
        if(Integer.parseInt(total_product_value)< 50){
            coupon_50.setVisibility(View.INVISIBLE);
        }
        else{
            coupon_50.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void abc() {
        String pay_price = String.valueOf(final_value);

         name = Address.name.getText().toString();
         mobile = Address.contact.getText().toString();
         address = Address.address.getText().toString();
         near_area = Address.selected_area_text.getText().toString();
         user_token = MainActivity.user_token;

         total_bill_price = payment_final_total_price.getText().toString();

//        int coupon_selectedId = coupon_radio_group.getCheckedRadioButtonId();
//
//
//        if (coupon_selectedId == -1) {
//            Toast toast = Toast.makeText(getApplicationContext(), "Please,select any coupon option", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER,0,0);
//            toast.show();
//        } else {
//            switch (coupon_selectedId) {
//                case R.id.coupon_none:
//                    coupon_value = coupon_none.getText().toString();
//
//                    break;
//
//                case R.id.coupon_10:
//                    coupon_value = coupon_10.getText().toString();
//
//                    break;
//
//                case R.id.coupon_20:
//                    coupon_value = coupon_20.getText().toString();
//
//                    break;
//
//                case R.id.coupon_50:
//                    coupon_value = coupon_50.getText().toString();
//
//            }
//        }

        coupon_value = Address.discount_code_text.getText().toString();

        Cursor cursor = payment_database.get_poora_Data();

        food_list = new ArrayList<String>();

        String selected_tag_str = Address.selected_tag_text.getText().toString();
        String selected_delivery_date_str = Address.selected_delivery_date_text.getText().toString();
        String selected_name_str = Address.name_on_cake.getText().toString();

        while(cursor.moveToNext()){
            food_list.add("{"+ cursor.getString(1) +
                    ","+ "Rs" +cursor.getString(2) +
                    "*" +cursor.getString(3) +"}" + "\n" + selected_tag_str +" " + selected_name_str + "\n" + selected_delivery_date_str);
        }


        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate_new = localDateTime.format(dateTimeFormatter);

         date = formattedDate_new;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        final String transcId = df.format(c);


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);


        if (selectedId == -1) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,select any payment option", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();        } else {
            switch (selectedId) {
                case R.id.radio_btn_COD:

                    addDataToFirestore(name,mobile,address,near_area,location_text,
                                        mode_of_payment,total_bill_price,date,coupon_value,user_token,food_list);

                    order_notifi_sending_method();


                    Intent loginIntent = new Intent(Payment.this,Order_process.class);
                    startActivity(loginIntent);
                    finish();


                    new Sms_sending().execute();

                    payment_database.addModelHistory(date);

                   // payment_database.copy_table();

                    break;

                case R.id.radio_btn_Google_Pay:

                    makePayment(pay_price,transcId);


                    break;

                case R.id.radio_btn_Other:
                    makePayment("50",transcId);


            }
        }
    }


    public void upi_fetch(){
        noteRef_upi.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    Upi_id = (String) documentSnapshot.get("Upi");
                    //    Toast.makeText(MainActivity.this, Shop_On_Off, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Document does not exist", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



    }


    private void makePayment (String amount,String transcationId){
            // on below line we are calling an easy payment method and passing
            // all parameters to it such as upi id,name, description and others.

            final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                    .with(this)
                    // on below line we are adding upi id.
                    .setPayeeVpa(Upi_id)
                    // on below line we are setting name to which we are making oayment.
                    .setPayeeName(MainActivity.title)
                    // on below line we are passing transaction id.
                    .setTransactionId(transcationId)
                    // on below line we are passing transaction ref id.
                    .setTransactionRefId(transcationId)
                    // on below line we are adding description to payment.
                    .setDescription("paying to " + MainActivity.title)
                    // on below line we are passing amount which is being paid.
                    .setAmount(amount+".00")
                  //  .setAmount("1.00")
                    // on below line we are calling a build method to build this ui.
                    .build();
            // on below line we are calling a start
            // payment method to start a payment.
            easyUpiPayment.startPayment();
            // on below line we are calling a set payment
            // status listener method to call other payment methods.
            easyUpiPayment.setPaymentStatusListener(this);
        }

        @Override
        public void onTransactionCompleted(TransactionDetails transactionDetails) {
            // on below line we are getting details about transaction when completed.
           transcDetails = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();
            transactionDetailsTV.setVisibility(View.VISIBLE);
            // on below line we are setting details to our text view.
            transactionDetailsTV.setText(transcDetails);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onTransactionSuccess() {
            // this method is called when transaction is successful and we are displaying a toast message.

            addDataToFirestore(name,mobile,address,near_area,location_text,
                    mode_of_payment,total_bill_price,date,coupon_value,user_token,food_list);

            order_notifi_sending_method();

           // order_notifi_sending_rider_method();

            Toast toast = Toast.makeText(getApplicationContext(),"Transaction successfully completed", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
          //  Error_Activity.load_img = "https://media1.tenor.com/images/4f164fc9de94ae29aa3f3f144f14441a/tenor.gif?itemid=5286441";
            Intent loginIntent = new Intent(Payment.this,Order_process.class);
            startActivity(loginIntent);
            finish();

//            Toast toast1 = Toast.makeText(getApplicationContext(),"Order Successful", Toast.LENGTH_SHORT);
//            toast1.setGravity(Gravity.CENTER,0,0);
//            toast1.show();
            new Sms_sending().execute();

            payment_database.addModelHistory(date);
        }

        @Override
        public void onTransactionSubmitted() {
            // this method is called when transaction is done
            // but it may be successful or failure.
            Log.e("TAG", "TRANSACTION SUBMIT");
        }

        @Override
        public void onTransactionFailed() {
            // this method is called when transaction is failure.
            Toast toast = Toast.makeText(getApplicationContext(),"Failed to complete transaction", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();        }

        @Override
        public void onTransactionCancelled() {
            // this method is called when transaction is cancelled.
            Toast toast = Toast.makeText(getApplicationContext(),"Transaction Cancelled", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();        }

        @Override
        public void onAppNotFound() {
            // this method is called when the users device is not having any app installed for making payment.
            Toast toast = Toast.makeText(getApplicationContext(),"No found payment app for transaction", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
    }



    public void onClick_method(View view)  {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);

        if(MainActivity.title.matches("Maska Chaska")) {

            if(Address.selected_area_text.getText().equals("Banjhedih Power Plant")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Banjhedih");
            }
            else if(Address.selected_area_text.getText().equals("Koderma")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Koderma" );
            }
            else if(Address.selected_area_text.getText().equals("Chandwara")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Chandwara");
            }
            else if(Address.selected_area_text.getText().equals("Other Area")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Other area");
            }
            else{
                int_deli_value = 20;
            }

            if (selectedId == -1) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please, select payment method", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            } else {
                switch (selectedId) {
                    case R.id.radio_btn_COD:

                        if (100 <= Integer.parseInt(total_product_value)) {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs" + final_value);

                            mode_of_payment = "Cash On Delivery";
                            break;
                        } else if (100 > Integer.parseInt(total_product_value)) {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"COD not available for less than Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            radioButton.requestFocus();
                            break;
                        } else {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            //  Toast.makeText(this, "Select Others and pay Rs 50/-", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case R.id.radio_btn_Google_Pay:

                        mode_of_payment = "Prepaid";
                        if (100 > Integer.parseInt(total_product_value)) {
//                            deli_charge_value = int_deli_value;
//                            delivery_charge.setText("Rs." + deli_charge_value);
//                            final_value = deli_charge_value + Integer.parseInt(total_product_value);
//                            payment_final_total_price.setText("Rs." + final_value);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            break;
                        }
                        else {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs" + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs" + final_value);
                            pay_button.setVisibility(View.VISIBLE);
                            break;
                        }

                    case R.id.radio_btn_Other:

                        radioButton.setVisibility(View.INVISIBLE);
                        mode_of_payment = "Others";
                        pay_button.setVisibility(View.INVISIBLE);

                }
            }

        }

        else if(MainActivity.title.matches("Shubhraj")) {

            if(Address.selected_area_text.getText().equals("Banjhedih Power Plant")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Banjhedih");
            }
            else if(Address.selected_area_text.getText().equals("Koderma")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Koderma" );
            }
            else if(Address.selected_area_text.getText().equals("Chandwara")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Chandwara");
            }
            else if(Address.selected_area_text.getText().equals("Other Area")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Other area");
            }
            else{
                int_deli_value = 20;
            }

            if (selectedId == -1) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please, select payment method", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            } else {
                switch (selectedId) {
                    case R.id.radio_btn_COD:

                        if (100 <= Integer.parseInt(total_product_value)) {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs." + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs." + final_value);

                            mode_of_payment = "Cash On Delivery";
                            break;
                        } else if (100 > Integer.parseInt(total_product_value)) {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"COD not available for less than Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            radioButton.requestFocus();
                            break;
                        } else {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            //  Toast.makeText(this, "Select Others and pay Rs 50/-", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case R.id.radio_btn_Google_Pay:

                        mode_of_payment = "Prepaid";
                        if (100 > Integer.parseInt(total_product_value)) {
//                            deli_charge_value = int_deli_value;
//                            delivery_charge.setText("Rs." + deli_charge_value);
//                            final_value = deli_charge_value + Integer.parseInt(total_product_value);
//                            payment_final_total_price.setText("Rs." + final_value);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();                            break;
                        }
                        else {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs" + final_value);
                            pay_button.setVisibility(View.VISIBLE);
                            break;
                        }

                    case R.id.radio_btn_Other:

                        radioButton.setVisibility(View.INVISIBLE);
                        mode_of_payment = "Others";
                        pay_button.setVisibility(View.INVISIBLE);

                }
            }


        }

        else if(MainActivity.title.matches("Jayka")){

            if(Address.selected_area_text.getText().equals("Banjhedih Power Plant")){
                int_deli_value = 60;
            }
            else if(Address.selected_area_text.getText().equals("Koderma")){
                int_deli_value = 60;
            }
            else if(Address.selected_area_text.getText().equals("Chandwara")){
                int_deli_value = 60;
            }
            else if(Address.selected_area_text.getText().equals("Other Area")){
               int_deli_value = 30;
            }
            else{
                int_deli_value = 30;
            }

            if (selectedId == -1) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please, select payment method", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            } else {
                switch (selectedId) {
                    case R.id.radio_btn_COD:

                        if (100 <= Integer.parseInt(total_product_value)) {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs " + final_value);
                            pay_button.setVisibility(View.VISIBLE);

                            mode_of_payment = "Cash On Delivery";
                            break;
                        } else if (100 > Integer.parseInt(total_product_value)) {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            radioButton.requestFocus();
                            break;
                        } else {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            //  Toast.makeText(this, "Select Others and pay Rs 50/-", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case R.id.radio_btn_Google_Pay:

                        radioButton.setVisibility(View.INVISIBLE);
                        pay_button.setVisibility(View.INVISIBLE);
                        mode_of_payment = "Prepaid";
                        Toast toast = Toast.makeText(getApplicationContext(),"Prepaid not excepted by Jayka", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        break;
//                        if (100 > Integer.parseInt(total_product_value)) {
////                            deli_charge_value = int_deli_value;
////                            delivery_charge.setText("Rs." + deli_charge_value);
////                            final_value = deli_charge_value + Integer.parseInt(total_product_value);
////                            payment_final_total_price.setText("Rs." + final_value);
//                            pay_button.setVisibility(View.INVISIBLE);
//                            Toast.makeText(this, "Min Order Rs100", Toast.LENGTH_SHORT).show();
//                            break;
//                        }
//                        else {
//                            deli_charge_value = int_deli_value;
//                            delivery_charge.setText("Rs." + deli_charge_value);
//                            final_value = deli_charge_value + Integer.parseInt(total_product_value);
//                            payment_final_total_price.setText("Rs." + final_value);
//                            pay_button.setVisibility(View.VISIBLE);
//                            break;
//                        }


                    case R.id.radio_btn_Other:

                        radioButton.setVisibility(View.INVISIBLE);
                        mode_of_payment = "Others";
                        pay_button.setVisibility(View.INVISIBLE);

                }
            }


        }

        else if(MainActivity.title.matches("The Cake Corner")){

            if(Address.selected_area_text.getText().equals("Banjhedih Power Plant")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Banjhedih");
            }
            else if(Address.selected_area_text.getText().equals("Koderma")){
                int_deli_value = 40;
            }
            else if(Address.selected_area_text.getText().equals("Chandwara")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Chandwara");
            }
            else if(Address.selected_area_text.getText().equals("Other Area")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Other area");
            }
            else{
                int_deli_value = 30;
            }

            if (selectedId == -1) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please, select payment method", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();            } else {
                switch (selectedId) {
                    case R.id.radio_btn_COD:

                        if (100 <= Integer.parseInt(total_product_value)) {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs " + final_value);

                            mode_of_payment = "Cash On Delivery";
                            break;
                        } else if (100 > Integer.parseInt(total_product_value)) {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();                            radioButton.requestFocus();
                            break;
                        } else {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                          //  Toast.makeText(this, "Select Others and pay Rs 50/-", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case R.id.radio_btn_Google_Pay:

                        mode_of_payment = "Prepaid";
                        if (100 > Integer.parseInt(total_product_value)) {
//                            deli_charge_value = int_deli_value;
//                            delivery_charge.setText("Rs." + deli_charge_value);
//                            final_value = deli_charge_value + Integer.parseInt(total_product_value);
//                            payment_final_total_price.setText("Rs." + final_value);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast.makeText(this, "Min Order Rs100", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs" + final_value);
                            pay_button.setVisibility(View.VISIBLE);
                            break;
                        }

                    case R.id.radio_btn_Other:

                        radioButton.setVisibility(View.INVISIBLE);
                        mode_of_payment = "Others";
                        pay_button.setVisibility(View.INVISIBLE);

                }
            }

        }

        else if(MainActivity.title.matches("Pizza Bazaar")){

            if(Address.selected_area_text.getText().equals("Banjhedih Power Plant")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Banjhedih");
            }
            else if(Address.selected_area_text.getText().equals("Koderma")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Koderma");
            }
            else if(Address.selected_area_text.getText().equals("Chandwara")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Chandwara");
            }
            else if(Address.selected_area_text.getText().equals("Other Area")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Other area");
            }
            else{
                int_deli_value = 30;
            }

            if (selectedId == -1) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please, select Payment method", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();            } else {
                switch (selectedId) {
                    case R.id.radio_btn_COD:

                        if (100 <= Integer.parseInt(total_product_value)) {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs " + final_value);

                            mode_of_payment = "Cash On Delivery";
                            break;
                        } else if (100 > Integer.parseInt(total_product_value)) {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            radioButton.requestFocus();
                            break;
                        } else {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            //  Toast.makeText(this, "Select Others and pay Rs 50/-", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case R.id.radio_btn_Google_Pay:

                        mode_of_payment = "Prepaid";
                        if (100 > Integer.parseInt(total_product_value)) {
//                            deli_charge_value = int_deli_value;
//                            delivery_charge.setText("Rs." + deli_charge_value);
//                            final_value = deli_charge_value + Integer.parseInt(total_product_value);
//                            payment_final_total_price.setText("Rs." + final_value);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();                            break;
                        }
                        else {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs " + final_value);
                            pay_button.setVisibility(View.VISIBLE);
                            break;
                        }

                    case R.id.radio_btn_Other:

                        radioButton.setVisibility(View.INVISIBLE);
                        mode_of_payment = "Others";

           }
            }


        }

        else if(MainActivity.title.matches("Food Circle")){

            if(Address.selected_area_text.getText().equals("Banjhedih Power Plant")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Banjhedih");
            }
            else if(Address.selected_area_text.getText().equals("Koderma")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Koderma");
            }
            else if(Address.selected_area_text.getText().equals("Chandwara")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Chandwara");
            }
            else if(Address.selected_area_text.getText().equals("Other Area")){
                pay_button.setClickable(false);
                pay_button.setText("Choose Other Shop for Other area");
            }
            else{
                int_deli_value = 25;
            }

            if (selectedId == -1) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please, select Payment method", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            } else {
                switch (selectedId) {
                    case R.id.radio_btn_COD:

                        if (100 <= Integer.parseInt(total_product_value)) {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs " + final_value);

                            mode_of_payment = "Cash On Delivery";
                            break;
                        } else if (100 > Integer.parseInt(total_product_value)) {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            radioButton.requestFocus();
                            break;
                        } else {
                            radioButton.setVisibility(View.INVISIBLE);
                            pay_button.setVisibility(View.INVISIBLE);
                            //  Toast.makeText(this, "Select Others and pay Rs 50/-", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case R.id.radio_btn_Google_Pay:

                        mode_of_payment = "Prepaid";
                        if (100 > Integer.parseInt(total_product_value)) {
//                            deli_charge_value = int_deli_value;
//                            delivery_charge.setText("Rs." + deli_charge_value);
//                            final_value = deli_charge_value + Integer.parseInt(total_product_value);
//                            payment_final_total_price.setText("Rs." + final_value);
                            pay_button.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Min. Order Rs100", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            break;
                        }
                        else {
                            deli_charge_value = int_deli_value;
                            delivery_charge.setText("Rs " + deli_charge_value);
                            final_value = deli_charge_value + Integer.parseInt(total_product_value) - discount_value_int;
                            payment_final_total_price.setText("Rs " + final_value);
                            pay_button.setVisibility(View.VISIBLE);
                            break;
                        }

                    case R.id.radio_btn_Other:

                        radioButton.setVisibility(View.INVISIBLE);
                        mode_of_payment = "Others";

                }
            }

        }

    }


    private void setRecview_table() {
        table_recyclerView = (RecyclerView) findViewById(R.id.table_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        table_recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        table_recyclerView.setHasFixedSize(true);
        payment_database = new Database(this);
        table_list = payment_database.get_all_data();

        if (table_list.size() > 0) {
            table_recyclerView.setVisibility(View.VISIBLE);
            adapter_table_view = new Adapter_table_view(this, table_list);
            table_recyclerView.setAdapter(adapter_table_view);

        } else {
            table_recyclerView.setVisibility(View.GONE);
             }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDataToFirestore(String name, String mobile, String address, String near_area,
                                    String location_text, String mode_of_payment,
                                    String total_bill_price, String date, String coupon_value, String user_token, List<String> food_list) {

        LocalDateTime doc_time = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = doc_time.format(timeFormatter);
        // creating a collection reference
        // for our Firebase Firetore database.

             documentReference = payment_db.collection(collection).document(formattedDate);


            // adding our data to our courses object class.
            Model_send_to_firebase model_send_to_firebase = new Model_send_to_firebase(name, mobile, address, near_area, location_text, mode_of_payment, total_bill_price, date, coupon_value, user_token, food_list);

            // below method is use to add data to Firebase Firestore.
            documentReference.set(model_send_to_firebase)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // after the data addition is successful
                            // we are displaying a success toast message.
                          //  Toast.makeText(Payment.this, "Your Order is Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // this method is called when the data addition process is failed.
                    // displaying a toast message when data addition is failed.
                    Toast.makeText(Payment.this, "Server Problem! Please,Try Again \n" + e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            });
    }

    public void token_receiving_method(){
        noteRef_shop.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    shop_token_id = (String) documentSnapshot.get("Shop");

                   // Log.d("pppppppppppppoooooooooo","token "+shop_token_id);


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Document does not exist", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        noteRef_rider.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    rider_token_id = (String) documentSnapshot.get("Shop");

                    // Log.d("pppppppppppppoooooooooo","token "+shop_token_id);


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Document does not exist", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    public void order_notifi_sending_method(){

        String title = "New Order";
        String body = "Check, New Order from "+ Address.selected_area_text.getText().toString().toUpperCase() +
                " Mobile "+  Address.contact.getText().toString().toUpperCase() + " Name " + Address.name.getText().toString().toUpperCase();

        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(shop_token_id,
                title,body,getApplicationContext(),Payment.this);

        notificationsSender.SendNotifications();

    }




//    public void order_notifi_sending_rider_method(){
//
//        String title = "New Order";
//        String body = "Check, You have new Order";
//
//        fcmNotiRiderSender notificationsSender_rider = new fcmNotiRiderSender(rider_token_id,
//                title,body,getApplicationContext(),Payment.this);
//
//        notificationsSender_rider.SendNotificationsRider();
//    }



//    public void setReceiving_sm(){
//
//        noteRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    receiving_sms = (String) documentSnapshot.get("sms");
//
//                    // Log.d("pppppppppppppoooooooooo","token "+shop_token_id);
//
//
//                } else {
//                    Toast toast = Toast.makeText(getApplicationContext(),"Document does not exist", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER,0,0);
//                    toast.show();
//                }
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//    }


    public class Sms_sending extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params){
            StringBuilder sb=null;
            BufferedReader reader=null;
            String serverResponse=null;


            String message_hai = "Verify New ORDER from " +  Address.selected_area_text.getText().toString().toUpperCase()
                    + " Mobile "+ Address.contact.getText().toString() + " Name " + Address.name.getText().toString().toUpperCase();

            try {
                URL url = new URL(" https://www.fast2sms.com/dev/bulkV2?authorization=HRoMyStGT79GItf6IBBoM35hEchTbhil8895JSM6gig8VJS7RDUCX9XoHWtN&route=v3&sender_id=TXTIND&message=" +message_hai + "&language=english&flash=1" + "&numbers=" + MainActivity.shop_phone_no);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                int statusCode = connection.getResponseCode();
                //Log.e("statusCode", "" + statusCode);

                if (statusCode == 200) {
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                }

                connection.disconnect();
                if (sb!=null)
                    serverResponse=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //All your UI operation can be performed here
            System.out.println(s);

        }
    }




}