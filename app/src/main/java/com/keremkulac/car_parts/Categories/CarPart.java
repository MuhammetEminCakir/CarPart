package com.keremkulac.car_parts.Categories;

import java.util.ArrayList;

public class CarPart {

    String name;
    String code;
    String price;
    String downloadUrl;


    public ArrayList<ShoppingCart> getShoppingCartArrayList() {
        return shoppingCartArrayList;
    }

    public void setShoppingCartArrayList(ArrayList<ShoppingCart> shoppingCartArrayList) {
        this.shoppingCartArrayList = shoppingCartArrayList;
    }

    ArrayList<ShoppingCart> shoppingCartArrayList;

    public CarPart(String name, String code, String price, String downloadUrl) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.downloadUrl = downloadUrl;
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
