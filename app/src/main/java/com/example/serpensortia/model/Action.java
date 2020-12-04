package com.example.serpensortia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Action extends Model implements Comparable<Action>{
    @Column(name = "action_note")
    public String note;

    @Column(name = "action_date")
    public String date;

    @Column(name = "action_reptile")
    public Reptile reptile;

    public Action() {
        super();
    }

    public Date getDate(){
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteByReptileId(long id){
        new Delete().from(Action.class).where("action_reptile = ?", id).execute();
    }

    public static List<Action> findByReptile(Reptile r){
        return new Select().from(Action.class).where("action_reptile = ?", r.getId()).orderBy("1").execute();
    }

    public static List<Action> findAll() {
        return new Select().from(Action.class).orderBy("id DESC").execute();
    }

    public static Action findById(long id){
        return new Select().from(Action.class).where("id = ?", id).executeSingle();
    }

    @Override
    public String toString() {
        return date + "\n" + note;
    }

    @Override
    public int compareTo(Action o) {
        if (getDate() == null || o.getDate() == null) {
            return 0;
        }
        return o.getDate().compareTo(getDate());
    }
}
