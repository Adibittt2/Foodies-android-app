package com.aditya_verma.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class Shopping_history extends AppCompatActivity {

   RecyclerView table_recyclerView;
   Database history_database;
    Adapter_history adapter_history;
    public static ArrayList<Model> history_table_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_history);

        history_database = new Database(this);

        history_rec_view();
    }

    public void history_rec_view(){
        table_recyclerView = (RecyclerView) findViewById(R.id.history_rec_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        table_recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        table_recyclerView.setHasFixedSize(true);
        history_database = new Database(this);
        history_table_list = history_database.get_all_data_History();

        if (history_table_list.size() > 0) {
            table_recyclerView.setVisibility(View.VISIBLE);
            adapter_history = new Adapter_history(this, history_table_list);
            table_recyclerView.setAdapter(adapter_history);

        } else {
            table_recyclerView.setVisibility(View.GONE);

          //  table_recyclerView.setTag("Empty");
        }
    }

//    private void setRecview_table() {
//        table_recyclerView = (RecyclerView) findViewById(R.id.table_recyclerview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
//        table_recyclerView.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.setStackFromEnd(true);
//        table_recyclerView.setHasFixedSize(true);
//        payment_database = new Database(this);
//        table_list = payment_database.get_all_data();
//
//        if (table_list.size() > 0) {
//            table_recyclerView.setVisibility(View.VISIBLE);
//            adapter_table_view = new Adapter_table_view(this, table_list);
//            table_recyclerView.setAdapter(adapter_table_view);
//
//        } else {
//            table_recyclerView.setVisibility(View.GONE);
//        }
//    }


}
