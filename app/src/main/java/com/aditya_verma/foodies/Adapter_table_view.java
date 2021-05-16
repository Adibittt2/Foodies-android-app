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

import java.util.ArrayList;
import java.util.List;

public class Adapter_table_view extends RecyclerView.Adapter<Adapter_table_view.ViewHolder> {

    Context context;
    ArrayList<Model> table_list;

    public Adapter_table_view(Context context, ArrayList<Model> table_list) {
        this.context = context;
        this.table_list = table_list;
    }

    @NonNull
    @Override
    public Adapter_table_view.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_table_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_table_view.ViewHolder holder, int position) {

        final Model model_table_view = table_list.get(position);

        holder.flavour.setText(model_table_view.getFlavour());
        holder.price.setText("Rs" + model_table_view.getPrice());
        holder.quantity.setText(model_table_view.getQuantity());
        int alpha = Integer.parseInt(model_table_view.getPrice())
                * Integer.parseInt(model_table_view.getQuantity());

        holder.price_for_each.setText("Rs" + alpha);

    }
    @Override
    public int getItemCount() {
        return table_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView flavour,price, quantity,price_for_each;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            flavour = (TextView) itemView.findViewById(R.id.tv_product);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            price_for_each = (TextView) itemView.findViewById(R.id.tv_sub_total);
        }
    }
}