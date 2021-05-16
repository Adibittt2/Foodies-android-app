package com.aditya_verma.foodies;

import java.util.ArrayList;
import java.util.List;

public class Model_Foodies {

    // variables for storing our image and name.
    String description,image,name,new_price,old_price,tag,available;



    public Model_Foodies() {
        // empty constructor required for firebase.
    }

    public Model_Foodies(String description, String image, String name, String new_price, String old_price, String tag, String available) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.new_price = new_price;
        this.old_price = old_price;
        this.tag = tag;
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}

