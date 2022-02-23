package com.aditya_verma.foodies;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSessionManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

//import com.aditya_verma.foodies.ViewHolder;
//import com.aditya_verma.recyclerview_sqlite.Contacts;
//import com.aditya_verma.recyclerview_sqlite.R;
//import com.aditya_verma.recyclerview_sqlite.SqliteDatabase;

import com.google.protobuf.StringValue;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<ViewHolder>  {

    private Context context;
    private ArrayList<Model> listContacts;
    private ArrayList<Integer> data1 = new ArrayList<Integer>();
    private int adapter_sum = 0;


    private Database meradatabase;



    public Adapter(Context context, ArrayList<Model> listContacts) {
        this.context = context;
        this.listContacts = listContacts;
        meradatabase = new Database(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_for_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         final Model model = listContacts.get(position);

        holder.flavour.setText(model.getFlavour());
        holder.price.setText("Rs"+ model.getPrice());
        holder.quantity.setText("Qty:"+ model.getQuantity());

        int alpha = Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity());

       holder.price_for_each.setText("Rs"+ alpha);

//       data1.add(alpha);
//
//       int adapter_sum = 0;
//        for(int i =0; i<data1.size(); i++) {
//
//            int product = data1.get(i);
//
//            adapter_sum = adapter_sum + product;
//
//            CartActivity.total_value.setText(String.valueOf(adapter_sum));
//            CartActivity.total_product_price.setText("Rs."+ adapter_sum);
//
//        }


            adapter_sum = adapter_sum + alpha;

            CartActivity.total_value.setText(String.valueOf(adapter_sum));
            CartActivity.total_product_price.setText("Rs." + adapter_sum);



        byte[] apple = model.getImage();
        Bitmap bi = BitmapFactory.decodeByteArray(apple,0,apple.length);
        holder.image.setImageBitmap(bi);


        holder.deletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database

                meradatabase.deleteModel(model.getId());
                meradatabase.deleteModelHistory(model.getId());

                //refresh the activity page.
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }

}