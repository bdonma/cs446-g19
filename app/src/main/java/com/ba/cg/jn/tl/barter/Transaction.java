package com.ba.cg.jn.tl.barter;

import java.util.Map;

/**
 * Created by tiffanylui on 2018-02-28.
 */

public class Transaction {
    private String barterUnit;
    private float barterValue;
    private float cashValue;
    private String creatorId;
    private boolean isActive;
    private boolean isBorrowed;
    private boolean isCompleted;
//    private boolean isRecurring;
//    private int recurringDays;
    private String name;
    private String notes;

    private String date;
    private String transactionId;

    private Map<String, Boolean> acceptedIds;
    private Map<String, Boolean> targetUserIds;

    public Transaction() {
        // Empty constructor required to convert Firebase object into data object.
    }

    public Transaction(String name, String date, String creatorId, Map<String, Boolean> targetUserIds,
                       float cashValue, float barterValue, String barterUnit,
                       boolean isBorrowed, boolean isActive, boolean isCompleted, String notes,
                       Map<String, Boolean> acceptedIds) {
        this.name = name;
        this.date = date;
        this.creatorId = creatorId;
        this.targetUserIds = targetUserIds;
        this.cashValue = cashValue;
        this.barterValue = barterValue;
        this.barterUnit = barterUnit;
        this.isBorrowed = isBorrowed;
        this.isActive = isActive;
        this.isCompleted = isCompleted;
        this.notes = notes;
        this.acceptedIds = acceptedIds;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getName() {
        return this.name;
    }

    public String getDate() {
        return this.date;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public Map<String, Boolean> getTargetUserIds() {
        return this.targetUserIds;
    }

    public float getCashValue() {
        return this.cashValue;
    }

    public float getBarterValue() {
        return this.barterValue;
    }

    public String getBarterUnit() {
        return this.barterUnit;
    }

    public boolean getIsBorrowed() {
        return this.isBorrowed;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public String getNotes() {
        return this.notes;
    }

    public Map<String, Boolean> getAcceptedIds() {
        return this.acceptedIds;
    }

    public void setTransactionId(String transactionId){
        this.transactionId = transactionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setTargetUserIds(Map<String, Boolean> targetUserIds) {
        this.targetUserIds = targetUserIds;
    }

    public void setCashValue(float cashValue) {
        this.cashValue = cashValue;
    }

    public void setBarterValue(float barterValue) {
        this.barterValue = barterValue;
    }

    public void setBarterUnit(String barterUnit) {
        this.barterUnit = barterUnit;
    }

    public void setIsBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAcceptedIds(Map<String, Boolean> acceptedIds) {
        this.acceptedIds = acceptedIds;
    }

    public void setDate(String date) {
        this.date = date;
    }

} // Transaction
