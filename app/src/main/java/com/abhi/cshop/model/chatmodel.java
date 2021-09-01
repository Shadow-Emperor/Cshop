package com.abhi.cshop.model;

public class chatmodel {

    private String Message;
    private String Date;
    private String Time;
    private String SendBy;

    public chatmodel() {
    }

    public chatmodel(String Message , String Date , String Time, String SendBy) {
        this.Message = Message;
        this.Date = Date;
        this.Time = Time;
        this.SendBy = SendBy;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getSendBy() {
        return SendBy;
    }

    public void setSendBy(String sendBy) {
        SendBy = sendBy;
    }
}
