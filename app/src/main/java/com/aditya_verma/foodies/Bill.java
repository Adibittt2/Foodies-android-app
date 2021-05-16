package com.aditya_verma.foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.protobuf.StringValue;

import java.util.ArrayList;

public class Bill extends AppCompatActivity {

    private ArrayList<String> data1 = new ArrayList<String>();
    private ArrayList<String> data2 = new ArrayList<String>();
    private ArrayList<String> data3 = new ArrayList<String>();
    private ArrayList<String> data4 = new ArrayList<String>();

    EditText et_product, et_price, et_quantity;
    Button btn_add;
    TextView tv_total;
    Database koi_v_data;
//    Model model;
//    Adapter bill_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        koi_v_data = new Database(this);

          et_product = (EditText) findViewById(R.id.et_product_name);
          et_price = (EditText) findViewById(R.id.et_price);
          et_quantity = (EditText) findViewById(R.id.et_quantity);
          tv_total = (TextView) findViewById(R.id.et_sub_total);
          btn_add = (Button) findViewById(R.id.btn_add);
      //  Button btn_show = (Button) findViewById(R.id.btn_show);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    public void add(){
        int tot;
        String et1 = et_product.getText().toString();
        int et2 = Integer.parseInt(et_price.getText().toString());
        int et3 = Integer.parseInt(et_quantity.getText().toString());

        tot = et2 * et3;

        data1.add(et1);
        data2.add("Rs" + et2);
        data3.add(String.valueOf(et3));
        data4.add(String.valueOf(tot));

        TableLayout table = (TableLayout)findViewById(R.id.table_layout);

        TableRow row = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        TextView t3 = new TextView(this);
        TextView t4 = new TextView(this);

        int   sum = 0;

        for(int i =0; i<data1.size();i++){

           String product = data1.get(i);
           String price = data2.get(i);
           String quantity = data3.get(i);
           String sub_total = data4.get(i);

            t1.setText(product);
            t2.setText(price);
            t3.setText(quantity);
            t4.setText(sub_total);

            sum = sum + Integer.valueOf(sub_total);
        }

        row.addView(t1);
        row.addView(t2);
        row.addView(t3);
        row.addView(t4);
        table.addView(row);

        tv_total.setText(String.valueOf(sum));
        et_product.setText("");
        et_price.setText("");
        et_quantity.setText("");
        et_product.requestFocus();
    }
}