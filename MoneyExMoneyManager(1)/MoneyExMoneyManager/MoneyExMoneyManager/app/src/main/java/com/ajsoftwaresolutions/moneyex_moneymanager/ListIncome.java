package com.ajsoftwaresolutions.moneyex_moneymanager;

public class ListIncome {

    String amount,title_i,catagory,date,iid;

    public ListIncome() {
    }

    public ListIncome(String amount, String title_i, String catagory, String date, String iid) {
        this.amount = amount;
        this.title_i = title_i;
        this.catagory = catagory;
        this.date = date;
        this.iid = iid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTitle_i() {
        return title_i;
    }

    public void setTitle_i(String title_i) {
        this.title_i = title_i;
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

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
}
