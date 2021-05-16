package com.aditya_verma.foodies;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public int id;
    public String flavour, price, quantity;
    public byte[] image;
    public String name, mobile, address;
    public List<String> food_list;


    public Model(int id, String flavour, String price, String quantity, byte[] image) {
        this.id = id;
        this.flavour = flavour;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public Model(String flavour, String price, String quantity, byte[] image) {
        this.flavour = flavour;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

//this is only for sending data to firebase

//    public Model(String name, String mobile, String address, List<String> food_list) {
//        this.name = name;
//        this.mobile = mobile;
//        this.address = address;
//        this.food_list = food_list;
//    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getFood_list() {
        return food_list;
    }

    public void setFood_list(List<String> food_list) {
        this.food_list = food_list;
    }
}
