package com.abhi.cshop.model;

public class historyDetails {
    String image;
    String name;
    long price;
    long count;

    public historyDetails() {
    }

    public historyDetails(String image , String name , long price , long count) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.count = count;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
