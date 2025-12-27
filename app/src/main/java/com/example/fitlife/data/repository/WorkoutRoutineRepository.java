package com.example.fitlife.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.fitlife.data.dao.WorkoutRoutineDao;
import com.example.fitlife.data.database.AppDatabase;
import com.example.fitlife.data.model.WorkoutRoutine;

import java.util.List;

public class WorkoutRoutineRepository {

    private WorkoutRoutineDao workoutRoutineDao;
    private LiveData<List<WorkoutRoutine>> allRoutines;

    public WorkoutRoutineRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        workoutRoutineDao = db.workoutRoutineDao();
        allRoutines = workoutRoutineDao.getAllRoutines();
    }

    public LiveData<List<WorkoutRoutine>> getAllRoutines() {
        return allRoutines;
    }

    public LiveData<WorkoutRoutine> getRoutineById(long id) {
        return workoutRoutineDao.getRoutineById(id);
    }

    public void insert(WorkoutRoutine routine) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutRoutineDao.insert(routine);
        });
    }

    public void update(WorkoutRoutine routine) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutRoutineDao.update(routine);
        });
    }

    public void delete(WorkoutRoutine routine) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutRoutineDao.delete(routine);
        });
    }

    public void updateCompletionStatus(long routineId, boolean isCompleted) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutRoutineDao.updateCompletionStatus(routineId, isCompleted);
        });
    }
}
