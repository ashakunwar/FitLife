package com.example.fitlife.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitlife.data.model.WeeklyPlan;

import java.util.List;

@Dao
public interface WeeklyPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(WeeklyPlan plan);

    @Query("SELECT * FROM weekly_plans ORDER BY dayOfWeek ASC")
    LiveData<List<WeeklyPlan>> getAllPlans();

    @Query("SELECT * FROM weekly_plans WHERE dayOfWeek = :dayOfWeek")
    LiveData<WeeklyPlan> getPlanByDay(int dayOfWeek);

    @Query("DELETE FROM weekly_plans WHERE dayOfWeek = :dayOfWeek")
    void deleteByDay(int dayOfWeek);
}
