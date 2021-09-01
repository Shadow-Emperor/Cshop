package com.abhi.cshop.model;

public class Product_model {
    String imageURL;
    String pdtid;
    long price;
    String productName;


    public Product_model(){

    }

    public Product_model(String imageURL , String pdtid , long price , String productName) {
        this.imageURL = imageURL;
        this.pdtid = pdtid;
        this.price = price;
        this.productName = productName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPdtid() {
        return pdtid;
    }

    public void setPdtid(String pdtid) {
        this.pdtid = pdtid;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


}
