package com.ajsoftwaresolutions.moneyex_moneymanager;

public class ListItemMode {



    String amount,titel,catagory,date,eid,iid;



    public ListItemMode() {

    }

    public ListItemMode(String amount, String titel, String catagory, String date, String eid, String iid) {
        this.amount = amount;
        this.titel = titel;
        this.catagory = catagory;
        this.date = date;
        this.eid = eid;
        this.iid = iid;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
