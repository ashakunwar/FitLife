package com.example.fitlife.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitlife.data.model.WorkoutRoutine;
import com.example.fitlife.data.repository.WorkoutRoutineRepository;

import java.util.List;

public class RoutineListViewModel extends AndroidViewModel {

    private WorkoutRoutineRepository repository;
    private LiveData<List<WorkoutRoutine>> allRoutines;

    public RoutineListViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRoutineRepository(application);
        allRoutines = repository.getAllRoutines();
    }

    public LiveData<List<WorkoutRoutine>> getAllRoutines() {
        return allRoutines;
    }

    public LiveData<WorkoutRoutine> getRoutineById(long id) {
        return repository.getRoutineById(id);
    }

    public void insert(WorkoutRoutine routine) {
        repository.insert(routine);
    }

    public void update(WorkoutRoutine routine) {
        repository.update(routine);
    }

    public void delete(WorkoutRoutine routine) {
        repository.delete(routine);
    }

    public void updateCompletionStatus(long routineId, boolean isCompleted) {
        repository.updateCompletionStatus(routineId, isCompleted);
    }
}
