package com.abhi.cshop.model;

public class history {
    String CreditCardNo;
    String Date;
    long NoofProducts;
    long Price;
    String PurchaseId;


    public history() {
    }

    public history(String creditCardNo , String date , long noofProducts , long price , String purchaseId) {
        CreditCardNo = creditCardNo;
        Date = date;
        NoofProducts = noofProducts;
        Price = price;
        PurchaseId = purchaseId;
    }

    public String getCreditCardNo() {
        return CreditCardNo;
    }

    public String getDate() {
        return Date;
    }

    public long getNoofProducts() {
        return NoofProducts;
    }

    public long getPrice() {
        return Price;
    }

    public String getPurchaseId() {
        return PurchaseId;
    }
}
