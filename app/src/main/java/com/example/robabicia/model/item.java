package com.example.robabicia.model;


import com.google.firebase.Timestamp;

import java.util.Date;

public class item {
    private String item_name;
    private String person_name;
    private String item_description;
    private String item_imageUrl;
    private String location;
    private String phone;
    private String price;
    private Timestamp date;


    public item() {
    }

    public item(String item_name, String person_name, String item_description, String item_imageUrl, String location, String phone, Timestamp date ,String price) {
        this.item_name = item_name;
        this.person_name = person_name;
        this.price = price;
        this.item_description = item_description;
        this.item_imageUrl = item_imageUrl;
        this.location = location;
        this.phone = phone;
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_imageUrl() {
        return item_imageUrl;
    }

    public void setItem_imageUrl(String item_imageUrl) {
        this.item_imageUrl = item_imageUrl;
    }
}
