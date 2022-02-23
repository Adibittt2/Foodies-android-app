package com.aditya_verma.foodies;

public class Model_Shop_list {

    String image, name;

    public Model_Shop_list() {

    }

    public Model_Shop_list(String image, String name) {
        this.image = image;
        this.name = name;
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
}