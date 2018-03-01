package com.ba.cg.jn.tl.barter;

/**
 * Created by tiffanylui on 2018-02-28.
 */

public class Transaction {
    private String targetUser;
    private String id;
    private String moneyValue;
    private String barterItem;
    private String moneyPerBarter;

    public Transaction() {
    }

    public Transaction(String targetUser, String id, String moneyValue, String barterItem, String moneyPerBarter) {
        this.targetUser = targetUser;
        this.id = id;
        this.moneyValue = moneyValue;
        this.barterItem = barterItem;
        this.moneyPerBarter = moneyPerBarter;
    }

    public void setBarterItem(String barterItem) {
        this.barterItem = barterItem;
    }

    public String getBarterItem() {
        return barterItem;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(String moneyValue) {
        this.moneyValue = moneyValue;
    }

    public String getMoneyPerBarter() {
        return moneyPerBarter;
    }

    public void setMoneyPerBarter(String moneyPerBarter) {
        this.moneyPerBarter = moneyPerBarter;
    }
}
