package com.abhi.cshop.model;

public class cart {
    String image;
    String pdname;
    long pdprice;
    String pdtid;
    long stock;



    public cart() {
    }

    public cart(String image , String pdname , long pdprice , String pdtid , long stock) {
        this.image = image;
        this.pdname = pdname;
        this.pdprice = pdprice;
        this.pdtid = pdtid;
        this.stock = stock;
    }

    public String getImage() {
        return this.image;
    }

    public String getPdname() {
        return this.pdname;
    }

    public long getPdprice() {
        return this.pdprice;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPdname(String pdname) {
        this.pdname = pdname;
    }

    public void setPdprice(long pdprice) {
        this.pdprice = pdprice;
    }

    public String getPdtid() {
        return this.pdtid;
    }

    public void setPdtid(String pdtid) {
        this.pdtid = pdtid;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }
}