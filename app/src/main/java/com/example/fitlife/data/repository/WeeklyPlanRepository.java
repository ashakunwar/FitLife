package com.example.fitlife.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.fitlife.data.dao.WeeklyPlanDao;
import com.example.fitlife.data.database.AppDatabase;
import com.example.fitlife.data.model.WeeklyPlan;

import java.util.List;

public class WeeklyPlanRepository {

    private WeeklyPlanDao weeklyPlanDao;
    private LiveData<List<WeeklyPlan>> allPlans;

    public WeeklyPlanRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        weeklyPlanDao = db.weeklyPlanDao();
        allPlans = weeklyPlanDao.getAllPlans();
    }

    public LiveData<List<WeeklyPlan>> getAllPlans() {
        return allPlans;
    }

    public void insertOrUpdate(WeeklyPlan plan) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weeklyPlanDao.insertOrUpdate(plan);
        });
    }

    public void deleteByDay(int dayOfWeek) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weeklyPlanDao.deleteByDay(dayOfWeek);
        });
    }
}
