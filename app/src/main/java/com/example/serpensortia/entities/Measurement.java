package com.example.serpensortia.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

public class Measurement extends SugarRecord {

    @Unique
    @Column(name = "measurement_id")
    private Long measurementId;

    @Column(name = "measurement_date")
    private Integer date;

    @Column(name = "measurement_length")
    private String length;

    @Column(name = "measurement_weight")
    private String weight;

    private Reptile reptile;

    public Measurement() {
    }

    public Measurement(Integer date, String length, String weight, Reptile reptile) {
        this.date = date;
        this.length = length;
        this.weight = weight;
        this.reptile = reptile;
    }

    public Long getMeasurementId() {
        return measurementId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Reptile getReptile() {
        return reptile;
    }

    public void setReptile(Reptile reptile) {
        this.reptile = reptile;
    }
}
