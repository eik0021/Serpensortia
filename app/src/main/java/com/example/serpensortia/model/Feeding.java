package com.example.serpensortia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.serpensortia.model.Reptile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Feeding extends Model implements Comparable<Feeding> {

    @Column(name = "feeding_date")
    public String date;

    @Column(name = "feeding_food_type")
    public String foodType;

    @Column(name = "feeding_item_count")
    public Integer itemCount;

    @Column(name = "feeding_food_weight")
    public Integer foodWeight;

    @Column(name = "feeding_refused")
    public Integer refused;

    @Column(name = "feeding_reptile")
    public Reptile reptile;

    public Feeding() {
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

    public static Feeding findById(long id){
        return new Select().from(Feeding.class).where("id = ?", id).executeSingle();
    }

    public static void deleteByReptileId(long id){
        new Delete().from(Feeding.class).where("feeding_reptile = ?", id).execute();
    }

    public static List<Feeding> findAll() {
        return new Select().from(Feeding.class).orderBy("id DESC").execute();
    }

    @Override
    public String toString() {
        return date + "\n potrava " + (refused == 1 ? "odmítnuta" : "přijata");
    }

    @Override
    public int compareTo(Feeding o) {
        if (getDate() == null || o.getDate() == null) {
            return 0;
        }
        return o.getDate().compareTo(getDate());
    }

    public static List<FeedingDto> getAllDto(){
        List<Feeding> feedings = findAll();
        List<FeedingDto> result = new ArrayList<>();

        for(Feeding f : feedings){
            result.add(new FeedingDto(f));
        }

        return result;
    }
}
