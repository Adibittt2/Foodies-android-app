package com.aditya_verma.foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Error_Activity extends AppCompatActivity {

    ImageView img;
    public static String load_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        img = (ImageView)findViewById(R.id.imageView);


//        Glide.with(this).load("https://media1.tenor.com/images/4f164fc9de94ae29aa3f3f144f14441a/tenor.gif?itemid=5286441").into(img);

       // load_img = "https://cdn.dribbble.com/users/1070140/screenshots/3403336/wifi2_1.gif";

        if(load_img == "no internet"){

            img.setImageResource(R.mipmap.sorry);
        }
        else {
            Glide.with(this).load(load_img).into(img);
        }

    }
}