package com.keremkulac.car_parts.Categories;

public class ShoppingCart {
    String name;
    String code;
    String price;
    String downloadUrl;
    String email;
    String date;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    String id;

    public ShoppingCart(String name, String code, String price, String downloadUrl, String email, String date) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.downloadUrl = downloadUrl;
        this.email = email;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
