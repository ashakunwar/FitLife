package com.example.fitlife.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gym_locations")
public class GymLocation {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private double latitude;
    private double longitude;

    public GymLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
