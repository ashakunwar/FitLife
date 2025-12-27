package com.example.fitlife.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "weekly_plans",
        foreignKeys = @ForeignKey(entity = WorkoutRoutine.class,
                                  parentColumns = "id",
                                  childColumns = "routineId",
                                  onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"dayOfWeek"}, unique = true)})
public class WeeklyPlan {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int dayOfWeek; // 1 for Monday, 7 for Sunday
    private long routineId;
    private boolean isCompleted;

    public WeeklyPlan(int dayOfWeek, long routineId, boolean isCompleted) {
        this.dayOfWeek = dayOfWeek;
        this.routineId = routineId;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(long routineId) {
        this.routineId = routineId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
