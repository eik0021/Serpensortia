package com.example.serpensortia.model;

import com.activeandroid.annotation.Column;

import java.security.PublicKey;

public class ActionDto {
    private long actionId;

    private String note;

    private String date;

    private ReptileDto reptile;

    public ActionDto(Action action) {
        this.actionId = action.getId();
        this.note = action.note;
        this.date = action.date;
        this.reptile = action.reptile.getDto();
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ReptileDto getReptile() {
        return reptile;
    }

    public void setReptile(ReptileDto reptile) {
        this.reptile = reptile;
    }
}
