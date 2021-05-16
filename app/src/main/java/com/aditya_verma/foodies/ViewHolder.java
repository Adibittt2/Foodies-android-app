package com.aditya_verma.foodies;

import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;



public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView flavour,price,price_for_each,quantity;
    public ImageView deletion, image;


    public ViewHolder(View itemView) {
        super(itemView);
        flavour = (TextView)itemView.findViewById(R.id.flavour);
        price = (TextView)itemView.findViewById(R.id.price);
        price_for_each = (TextView)itemView.findViewById(R.id.price_for_each);
        image = (ImageView)itemView.findViewById(R.id.image_for_card);
        deletion = (ImageView)itemView.findViewById(R.id.delete_option);
        quantity = (TextView) itemView.findViewById(R.id.quantity_card_for_cart);

    }
}