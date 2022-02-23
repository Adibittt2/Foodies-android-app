package com.aditya_verma.foodies;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Model_send_to_firebase {
    String name, mobile, address, near_area, location_text, mode_of_payment, total_bill_price, date, coupon_value, user_token;
    List<String> food_list;

    public Model_send_to_firebase(String name, String mobile, String address, String near_area, String location_text, String mode_of_payment, String total_bill_price, String date, String coupon_value, String user_token, List<String> food_list) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.near_area = near_area;
        this.location_text = location_text;
        this.mode_of_payment = mode_of_payment;
        this.total_bill_price = total_bill_price;
        this.date = date;
        this.coupon_value = coupon_value;
        this.user_token = user_token;
        this.food_list = food_list;
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

    public String getNear_area() {
        return near_area;
    }

    public void setNear_area(String near_area) {
        this.near_area = near_area;
    }

    public String getLocation_text() {
        return location_text;
    }

    public void setLocation_text(String location_text) {
        this.location_text = location_text;
    }

    public String getMode_of_payment() {
        return mode_of_payment;
    }

    public void setMode_of_payment(String mode_of_payment) {
        this.mode_of_payment = mode_of_payment;
    }

    public String getTotal_bill_price() {
        return total_bill_price;
    }

    public void setTotal_bill_price(String total_bill_price) {
        this.total_bill_price = total_bill_price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public List<String> getFood_list() {
        return food_list;
    }

    public void setFood_list(List<String> food_list) {
        this.food_list = food_list;
    }
}