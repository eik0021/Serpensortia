package com.example.serpensortia.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

public class Schedule extends SugarRecord {
    @Unique
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "schedule_note")
    private String note;

    @Column(name = "schedule_date")
    private Integer date;

    private Reptile reptile;

    public Schedule() {
    }

    public Schedule(String note, Integer date, Reptile reptile) {
        this.note = note;
        this.date = date;
        this.reptile = reptile;
    }

    public Long getScheduleId() {
        return scheduleId;
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
