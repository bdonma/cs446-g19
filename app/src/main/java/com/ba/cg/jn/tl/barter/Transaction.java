package com.ba.cg.jn.tl.barter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tiffanylui on 2018-02-28.
 */

public class Transaction {
    private String name;
    private String creatorId;
    private Map<String, Boolean> targetUserIds;
    private float cashValue;
    private float barterValue;
    private String barterUnit;
    private boolean isBorrowed;
    private boolean isActive;
    private boolean isCompleted;
    private String notes;

    private Map<String, Boolean> acceptedIds;

    // TODO: add date created field

    public Transaction() {
        // Empty constructor required to convert Firebase object into data object.
    }

    public Transaction(String name, String creatorId, Map<String, Boolean> targetUserIds, float cashValue, float barterValue, String barterUnit, boolean isBorrowed, boolean isActive, boolean isCompleted, String notes) {
        this.name = name;
        this.creatorId = creatorId;
        this.targetUserIds = targetUserIds;
        this.cashValue = cashValue;
        this.barterValue = barterValue;
        this.barterUnit = barterUnit;
        this.isBorrowed = isBorrowed;
        this.isActive = isActive;
        this.isCompleted = isCompleted;
        this.notes = notes;
    }

    public String getName() {
        return this.name;
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

} // Transaction
