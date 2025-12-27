package com.example.fitlife.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitlife.data.model.WeeklyPlan;
import com.example.fitlife.data.model.WorkoutRoutine;
import com.example.fitlife.data.repository.WeeklyPlanRepository;
import com.example.fitlife.data.repository.WorkoutRoutineRepository;

import java.util.List;

public class WeeklyPlannerViewModel extends AndroidViewModel {

    private WeeklyPlanRepository weeklyPlanRepository;
    private WorkoutRoutineRepository routineRepository;
    private LiveData<List<WeeklyPlan>> allPlans;
    private LiveData<List<WorkoutRoutine>> allRoutines;

    public WeeklyPlannerViewModel(@NonNull Application application) {
        super(application);
        weeklyPlanRepository = new WeeklyPlanRepository(application);
        routineRepository = new WorkoutRoutineRepository(application);
        allPlans = weeklyPlanRepository.getAllPlans();
        allRoutines = routineRepository.getAllRoutines();
    }

    public LiveData<List<WeeklyPlan>> getAllPlans() {
        return allPlans;
    }

    public LiveData<List<WorkoutRoutine>> getAllRoutines() {
        return allRoutines;
    }

    public void insertOrUpdate(WeeklyPlan plan) {
        weeklyPlanRepository.insertOrUpdate(plan);
    }

    public void deleteByDay(int dayOfWeek) {
        weeklyPlanRepository.deleteByDay(dayOfWeek);
    }
}
