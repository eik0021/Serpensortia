package com.example.serpensortia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.List;

public class Action extends Model {
    @Column(name = "action_note")
    public String note;

    @Column(name = "action_date")
    public String date;

    @Column(name = "action_reptile", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Reptile reptile;

    public Action() {
        super();
    }

    public static List<Action> findByReptile(Reptile r){
        return new Select().from(Action.class).where("action_reptile = ?", r.getId()).orderBy("1").execute();
    }

    public static List<Action> findAll() {
        return new Select().from(Action.class).orderBy("id DESC").execute();
    }
}
