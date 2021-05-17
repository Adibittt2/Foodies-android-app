package com.aditya_verma.foodies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    RecyclerView table_recyclerView,bc;
    Adapter_table_view adapter_table_view;
    public static ArrayList<Model> table_list = new ArrayList<>();

    private DocumentReference documentReference;
    private FirebaseFirestore payment_db;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Shop_token").document(collection);
    private DocumentReference noteRef2 = db.collection("SMS").document(collection);




    FirebaseMessaging firebaseMessaging;

    Button pay_button;
    public static String collection;
    Database payment_database;
    TextView payment_final_total_price, products_cost, delivery_charge, transactionDetailsTV;
    int final_value, deli_charge_value;
    String total_product_value,mode_of_payment,name,mobile,address,location_text,near_area,total_bill_price,date;
    List<String>food_list;
    String shop_token_id, receiving_sms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        token_receiving_method();
// for sending notification to all
        FirebaseMessaging.getInstance().subscribeToTopic("all");


        payment_final_total_price = (TextView) findViewById(R.id.payment_final_total_price);
        products_cost = (TextView) findViewById(R.id.products_cost);
        delivery_charge = (TextView) findViewById(R.id.delivery_charge);
        transactionDetailsTV =(TextView)findViewById(R.id.idTVTranscationDetails);

        payment_database = new Database(this);

        payment_db = FirebaseFirestore.getInstance();

        setRecview_table();

        Intent entry2 = getIntent();
        total_product_value = entry2.getStringExtra("total_price_payment_key");
       // String total_product_value = "200";
        products_cost.setText("Rs." + total_product_value);

        setReceiving_sms();

        pay_button = findViewById(R.id.payment_price_btn);
        pay_button.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                abc();

            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void abc() {
        String pay_price = String.valueOf(final_value);

         name = Address.name.getText().toString();
         mobile = Address.contact.getText().toString();
         address = Address.address.getText().toString();
         location_text = Address.location_text.getText().toString();
         near_area = Address.selected_area_text.getText().toString();

         total_bill_price = payment_final_total_price.getText().toString();

        Cursor cursor = payment_database.get_poora_Data();

        food_list = new ArrayList<String>();

        while(cursor.moveToNext()){
            food_list.add("{"+ cursor.getString(1) +
                    ","+ "Rs" +cursor.getString(2) +
                    "*" +cursor.getString(3) +"}");
        }


        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
        String formattedDate_new = localDateTime.format(dateTimeFormatter);

         date = formattedDate_new;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        final String transcId = df.format(c);


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);


        if (selectedId == -1) {
            Toast.makeText(this, "Please,select any one", Toast.LENGTH_SHORT).show();
        } else {
            switch (selectedId) {
                case R.id.radio_btn_COD:

                    addDataToFirestore(name,mobile,address,near_area,location_text,
                                        mode_of_payment,total_bill_price,date,food_list);

                    order_notifi_sending_method();

                    new Sms_sending().execute();

                    break;

                case R.id.radio_btn_Google_Pay:

                    makePayment(pay_price,transcId);


                    break;

                case R.id.radio_btn_Other:
                    makePayment("50",transcId);

            }
        }
    }


    private void makePayment (String amount,String transcationId){
            // on below line we are calling an easy payment method and passing
            // all parameters to it such as upi id,name, description and others.

            final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                    .with(this)
                    // on below line we are adding upi id.
                    .setPayeeVpa("9142735862@okbizaxis")
                    // on below line we are setting name to which we are making oayment.
                    .setPayeeName("Foodies")
                    // on below line we are passing transaction id.
                    .setTransactionId(transcationId)
                    // on below line we are passing transaction ref id.
                    .setTransactionRefId(transcationId)
                    // on below line we are adding description to payment.
                    .setDescription("paying to Foodies")
                    // on below line we are passing amount which is being paid.
                    .setAmount(amount+".00")
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
            String transcDetails = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();
            transactionDetailsTV.setVisibility(View.VISIBLE);
            // on below line we are setting details to our text view.
            transactionDetailsTV.setText(transcDetails);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onTransactionSuccess() {
            // this method is called when transaction is successful and we are displaying a toast message.

            addDataToFirestore(name,mobile,address,near_area,location_text,
                    mode_of_payment,total_bill_price,date,food_list);

            order_notifi_sending_method();


            Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();

           new Sms_sending().execute();
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
            Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTransactionCancelled() {
            // this method is called when transaction is cancelled.
            Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAppNotFound() {
            // this method is called when the users device is not having any app installed for making payment.
            Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show();
        }



    public void onClick_method(View view)  {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);

        if (selectedId == -1) {
            Toast.makeText(this, "Please,select any one", Toast.LENGTH_SHORT).show();
        } else {
            switch (selectedId) {
                case R.id.radio_btn_COD:
                    if (100 <= Integer.parseInt(total_product_value) && Integer.parseInt(total_product_value) < 500) {
                        deli_charge_value = 20;
                        delivery_charge.setText("Rs." + deli_charge_value);
                        final_value = deli_charge_value + Integer.parseInt(total_product_value);
                        payment_final_total_price.setText("Rs." + final_value);
                        Toast.makeText(this, "For Free Delivery use Google Pay, Phone Pe or Paytm", Toast.LENGTH_SHORT).show();

                        mode_of_payment = "Cash On Delivery";
                        break;
                    } else if (100 > Integer.parseInt(total_product_value)) {
                        radioButton.setVisibility(View.INVISIBLE);
                        pay_button.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "COD is not available for less than 100", Toast.LENGTH_SHORT).show();
                        radioButton.requestFocus();
                        break;
                    } else {
                        radioButton.setVisibility(View.INVISIBLE);
                        pay_button.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "Select Others and pay Rs 50/-", Toast.LENGTH_SHORT).show();
                        break;
                    }

                case R.id.radio_btn_Google_Pay:

                    mode_of_payment = "Prepaid";
                    if (100 > Integer.parseInt(total_product_value)) {
                        deli_charge_value = 20;
                        delivery_charge.setText("Rs." + deli_charge_value);
                        final_value = deli_charge_value + Integer.parseInt(total_product_value);
                        payment_final_total_price.setText("Rs." + final_value);
                        pay_button.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "For free delivery order min. Rs 100", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        deli_charge_value = 0;
                        delivery_charge.setText("Rs." + deli_charge_value);
                        final_value = deli_charge_value + Integer.parseInt(total_product_value);
                        payment_final_total_price.setText("Rs." + final_value);
                        pay_button.setVisibility(View.VISIBLE);
                        break;
                    }

                case R.id.radio_btn_Other:

                    mode_of_payment = "Others";
                    if (499 < Integer.parseInt(total_product_value)) {
                        deli_charge_value = 0;
                        delivery_charge.setText("Rs." + deli_charge_value);
                        final_value = deli_charge_value + Integer.parseInt(total_product_value);
                        payment_final_total_price.setText("Rs." + final_value);
                        Toast.makeText(this, "Pay Rs 50 as security and get return at delivery time", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        radioButton.setVisibility(View.INVISIBLE);
                        pay_button.setVisibility(View.INVISIBLE);
                         break;
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
                                    String total_bill_price, String date, List<String> food_list) {

        LocalDateTime doc_time = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("E, dd MMM YY HH : mm : ss");
        String formattedDate = doc_time.format(timeFormatter);
        // creating a collection reference
        // for our Firebase Firetore database.

             documentReference = payment_db.collection(collection).document(formattedDate);


            // adding our data to our courses object class.
            Model_send_to_firebase model_send_to_firebase = new Model_send_to_firebase(name, mobile, address, near_area, location_text, mode_of_payment, total_bill_price, date, food_list);

            // below method is use to add data to Firebase Firestore.
            documentReference.set(model_send_to_firebase)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // after the data addition is successful
                            // we are displaying a success toast message.
                            Toast.makeText(Payment.this, "Your Order is Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // this method is called when the data addition process is failed.
                    // displaying a toast message when data addition is failed.
                    Toast.makeText(Payment.this, "Server Problem! Please,Try Again \n" + e, Toast.LENGTH_SHORT).show();

                }
            });
    }

    public void token_receiving_method(){
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    shop_token_id = (String) documentSnapshot.get("Shop");

                   // Log.d("pppppppppppppoooooooooo","token "+shop_token_id);


                } else {
                    Toast.makeText(Payment.this, "Document does not exist", Toast.LENGTH_SHORT).show();

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
        String body = "Check, you have new Order";


        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(shop_token_id,
                title,body,getApplicationContext(),Payment.this);

        notificationsSender.SendNotifications();

    }

    public void setReceiving_sms(){

        noteRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    receiving_sms = (String) documentSnapshot.get("sms");

                    // Log.d("pppppppppppppoooooooooo","token "+shop_token_id);


                } else {
                    Toast.makeText(Payment.this, "Document does not exist", Toast.LENGTH_SHORT).show();

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    public class Sms_sending extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb=null;
            BufferedReader reader=null;
            String serverResponse=null;


            String message_hai = "Hi "+ name + ", \n"+ receiving_sms;

            try {

                URL url = new URL(" https://www.fast2sms.com/dev/bulkV2?authorization=HRoMyStGT79GItf6IBBoM35hEchTbhil8895JSM6gig8VJS7RDUCX9XoHWtN&route=v3&sender_id=TXTIND&message=" +message_hai + "&language=english&flash=0" + "&numbers=" + mobile);
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