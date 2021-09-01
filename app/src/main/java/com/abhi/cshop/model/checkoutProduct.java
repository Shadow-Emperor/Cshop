package com.abhi.cshop.model;

public class checkoutProduct {
    String pdtid;
    long pdprice;
    long count;

    public checkoutProduct() {
    }

    public checkoutProduct(String pdtid , long pdprice , long count) {
        this.pdtid = pdtid;
        this.pdprice = pdprice;
        this.count = count;
    }

    public String getPdtid() {
        return pdtid;
    }

    public void setPdtid(String pdtid) {
        this.pdtid = pdtid;
    }

    public long getPdprice() {
        return pdprice;
    }

    public void setPdprice(long pdprice) {
        this.pdprice = pdprice;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
