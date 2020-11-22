package com.example.serpensortia.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

public class Action extends SugarRecord {
    @Unique
    @Column(name = "action_id")
    private Long actionId;

    @Column(name = "action_note")
    private String note;

    @Column(name = "action_date")
    private Integer date;

    private Reptile reptile;

    public Action() {
    }

    public Action(String note, Integer date, Reptile reptile) {
        this.note = note;
        this.date = date;
        this.reptile = reptile;
    }

    public Long getActionId() {
        return actionId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Reptile getReptile() {
        return reptile;
    }

    public void setReptile(Reptile reptile) {
        this.reptile = reptile;
    }
}
