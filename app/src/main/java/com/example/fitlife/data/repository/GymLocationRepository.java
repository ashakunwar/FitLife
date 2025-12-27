package com.example.fitlife.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.fitlife.data.dao.GymLocationDao;
import com.example.fitlife.data.database.AppDatabase;
import com.example.fitlife.data.model.GymLocation;
import java.util.List;

public class GymLocationRepository {

    private GymLocationDao gymLocationDao;
    private LiveData<List<GymLocation>> allLocations;

    public GymLocationRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        gymLocationDao = db.gymLocationDao();
        allLocations = gymLocationDao.getAllLocations();
    }

    public LiveData<List<GymLocation>> getAllLocations() {
        return allLocations;
    }

    public void insert(GymLocation location) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gymLocationDao.insert(location);
        });
    }

    public void delete(GymLocation location) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gymLocationDao.delete(location);
        });
    }
}
