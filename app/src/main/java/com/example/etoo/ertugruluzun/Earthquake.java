package com.example.etoo.ertugruluzun;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*
    I created this class with my own
 */
@Entity(tableName = "Earhthquake")
public class Earthquake {


    @ColumnInfo(name = "place")
    private String place;

    @PrimaryKey
    private long timeInMilliseconds;

    @ColumnInfo(name = "magnitude")
    private double magnitude;

    @ColumnInfo(name = "url")
    private String url;

    public Earthquake(String place, long timeInMilliseconds, double magnitude, String url) {
        this.place = place;
        this.timeInMilliseconds = timeInMilliseconds;
        this.magnitude = magnitude;
        this.url = url;

    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public void setTimeInMilliseconds(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
