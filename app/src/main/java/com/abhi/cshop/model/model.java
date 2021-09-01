package com.abhi.cshop.model;


public class model {
    String imageURL;
    String pdtid;
    long price;
    String productName;
    String description;
    long stock;

    public model(String imageURL , String pdtid , long price , String productName , String description , long stock) {
        this.imageURL = imageURL;
        this.pdtid = pdtid;
        this.price = price;
        this.productName = productName;
        this.description = description;
        this.stock = stock;
    }

    public model() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public String getPdtid() {
        return this.pdtid;
    }

    public long getPrice() {
        return this.price;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPdtid(String pdtid) {
        this.pdtid = pdtid;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }
}
