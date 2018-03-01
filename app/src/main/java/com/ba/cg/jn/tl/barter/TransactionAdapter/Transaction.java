package com.ba.cg.jn.tl.barter.TransactionAdapter;

import java.util.Date;

/**
 * Created by JasonNgo on 2018-02-28.
 */

public class Transaction {
    private String title, dateCreated, value;
    private Boolean doIOwe;

    public Transaction() {

    }

    public Transaction(String title, String dateCreated, String value, Boolean doIOwe) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.value = value;
        this.doIOwe = doIOwe;
    }

    // Getters

    public String getTitle() {
        return title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getValue() {
        return value;
    }

    public Boolean getDoIOwe() {
        return doIOwe;
    }

    // Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDoIOwe(Boolean doIOwe) {
        this.doIOwe = doIOwe;
    }

} // Transaction
