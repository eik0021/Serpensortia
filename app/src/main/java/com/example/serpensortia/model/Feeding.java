package com.example.serpensortia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.example.serpensortia.model.Reptile;


public class Feeding extends Model {

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
}
