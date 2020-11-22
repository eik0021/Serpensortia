package com.example.serpensortia.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

public class Feeding extends SugarRecord {

    @Unique
    @Column(name = "feeding_id")
    private Long feedingId;

    @Column(name = "feeding_date")
    private Integer date;

    @Column(name = "feeding_food_type")
    private String foodType;

    @Column(name = "feeding_item_count")
    private Integer itemCount;

    @Column(name = "feeding_food_weight")
    private Integer foodWeight;

    @Column(name = "feeding_refused")
    private Integer refused;

    private Reptile reptile;

    public Feeding() {
    }

    public Feeding(Integer date, String foodType, Integer itemCount, Integer foodWeight, Integer refused) {
        this.date = date;
        this.foodType = foodType;
        this.itemCount = itemCount;
        this.foodWeight = foodWeight;
        this.refused = refused;
    }

    public Long getFeedingId() {
        return feedingId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getFoodWeight() {
        return foodWeight;
    }

    public void setFoodWeight(Integer foodWeight) {
        this.foodWeight = foodWeight;
    }

    public Integer getRefused() {
        return refused;
    }

    public void setRefused(Integer refused) {
        this.refused = refused;
    }

    public Reptile getReptile() {
        return reptile;
    }

    public void setReptile(Reptile reptile) {
        this.reptile = reptile;
    }
}
