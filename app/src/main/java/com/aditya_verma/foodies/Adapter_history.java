package com.aditya_verma.foodies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Adapter_history extends RecyclerView.Adapter<Adapter_history.ViewHolder> {

    Context context;
    ArrayList<Model> history_table_list;

    public Adapter_history(Context context, ArrayList<Model> history_table_list) {
        this.context = context;
        this.history_table_list = history_table_list;
    }

    @NonNull
    @Override
    public Adapter_history.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_history.ViewHolder holder, int position) {

        final Model model = history_table_list.get(position);

        holder.flavour.setText(model.getFlavour());
        holder.price.setText("Rs" + model.getPrice());
        holder.quantity.setText(model.getQuantity());
        int alpha = Integer.valueOf(model.getPrice())* Integer.valueOf(model.getQuantity());

        holder.price_for_each.setText("Rs" + String.valueOf(alpha) );


        holder.order_time.setText(model.getDate());

    }
    @Override
    public int getItemCount() {
        return history_table_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView flavour,price, quantity,price_for_each,order_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            flavour = (TextView) itemView.findViewById(R.id.tv_product);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            price_for_each = (TextView) itemView.findViewById(R.id.tv_sub_total);
            order_time = (TextView) itemView.findViewById(R.id.order_time);
        }
    }
}