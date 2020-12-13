package com.example.serpensortia.model;

import com.activeandroid.annotation.Column;

public class FeedingDto implements DtoSaveable{
    private long feedingId;

    private String date;

    private String foodType;

    private Integer itemCount;

    private Integer foodWeight;

    private Integer refused;

   ;private ReptileDto reptile;

    public FeedingDto(Feeding feeding) {
        this.feedingId = feeding.getId();
        this.date = feeding.date;
        this.foodType = feeding.foodType;
        this.itemCount = feeding.itemCount;
        this.foodWeight = feeding.foodWeight;
        this.refused = feeding.refused;
        this.reptile = feeding.reptile.getDto();
    }

    public long getFeedingId() {
        return feedingId;
    }

    public void setFeedingId(long feedingId) {
        this.feedingId = feedingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public ReptileDto getReptile() {
        return reptile;
    }

    public void setReptile(ReptileDto reptile) {
        this.reptile = reptile;
    }

    @Override
    public void saveModel() {
        Feeding feeding = new Feeding();
        feeding.date = this.date;
        feeding.foodType = this.foodType;
        feeding.itemCount = this.itemCount;
        feeding.foodWeight = this.foodWeight;
        feeding.refused = this.refused;
        feeding.reptile = Reptile.findByName(this.reptile.getName());

        feeding.save();
    }
}
