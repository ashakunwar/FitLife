package com.example.fitlife.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitlife.data.model.GymLocation;

import java.util.List;

@Dao
public interface GymLocationDao {

    @Insert
    void insert(GymLocation location);

    @Delete
    void delete(GymLocation location);

    @Query("SELECT * FROM gym_locations ORDER BY name ASC")
    LiveData<List<GymLocation>> getAllLocations();

    @Query("SELECT * FROM gym_locations WHERE id = :id")
    LiveData<GymLocation> getLocationById(long id);
}
