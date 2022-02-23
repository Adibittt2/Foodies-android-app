package com.aditya_verma.foodies;

public class Model_Cake {

    String description, image, name, new_price1, new_price2, old_price1, old_price2, tag, available;

    public Model_Cake() {
        // empty constructor required for firebase.
    }

    public Model_Cake(String description, String image, String name, String new_price1, String new_price2, String old_price1, String old_price2, String tag, String available) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.new_price1 = new_price1;
        this.new_price2 = new_price2;
        this.old_price1 = old_price1;
        this.old_price2 = old_price2;
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

    public String getNew_price1() {
        return new_price1;
    }

    public void setNew_price1(String new_price1) {
        this.new_price1 = new_price1;
    }

    public String getNew_price2() {
        return new_price2;
    }

    public void setNew_price2(String new_price2) {
        this.new_price2 = new_price2;
    }

    public String getOld_price1() {
        return old_price1;
    }

    public void setOld_price1(String old_price1) {
        this.old_price1 = old_price1;
    }

    public String getOld_price2() {
        return old_price2;
    }

    public void setOld_price2(String old_price2) {
        this.old_price2 = old_price2;
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