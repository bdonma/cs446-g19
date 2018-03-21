package com.ba.cg.jn.tl.barter;

import java.util.ArrayList;
/**
 * Created by tiffanylui on 2018-02-28.
 */

public class Transaction {
    private String name;
    private String creatorId;
    private ArrayList<String> targetUserIds;
    private float cashValue;
    private float barterValue;
    private String barterUnit;
    private boolean isActive;
    private boolean isCompleted;
    private String notes;

    public Transaction(String name, String creatorId, ArrayList<String> targetUserIds, float cashValue, float barterValue, String barterUnit, boolean isActive, boolean isCompleted, String notes) {
        this.name = name;
        this.creatorId = creatorId;
        this.targetUserIds = targetUserIds;
        this.cashValue = cashValue;
        this.barterValue = barterValue;
        this.barterUnit = barterUnit;
        this.isActive = isActive;
        this.isCompleted = isCompleted;
        this.notes = notes;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCashValue(float cashValue){
        this.cashValue = cashValue;
    }

    public void setBarterValue(float barterValue){
        this.barterValue = barterValue;
    }

    public void setBarterUnit(String barterUnit){
        this.barterUnit = barterUnit;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

    public void setIsCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public String getName(){
        return name;
    }

    public String getCreatorId(){
        return creatorId;
    }

    public ArrayList<String> getTargetUserIds(){
        return targetUserIds;
    }

    public float getCashValue(){
        return cashValue;
    }

    public float getBarterValue(){
        return barterValue;
    }

    public String getBarterUnit(){
        return barterUnit;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public boolean getIsCompleted(){
        return isCompleted;
    }

    public String getNotes(){
        return notes;
    }
}
