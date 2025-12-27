package com.example.fitlife.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitlife.data.model.WorkoutRoutine;

import java.util.List;

@Dao
public interface WorkoutRoutineDao {

    @Insert
    void insert(WorkoutRoutine routine);

    @Update
    void update(WorkoutRoutine routine);

    @Delete
    void delete(WorkoutRoutine routine);

    @Query("SELECT * FROM workout_routines ORDER BY name ASC")
    LiveData<List<WorkoutRoutine>> getAllRoutines();

    @Query("SELECT * FROM workout_routines WHERE id = :id")
    LiveData<WorkoutRoutine> getRoutineById(long id);

    @Query("UPDATE workout_routines SET isCompleted = :isCompleted WHERE id = :routineId")
    void updateCompletionStatus(long routineId, boolean isCompleted);
}
