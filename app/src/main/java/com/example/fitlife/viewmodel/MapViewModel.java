package com.example.fitlife.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.fitlife.data.model.GymLocation;
import com.example.fitlife.data.repository.GymLocationRepository;
import java.util.List;

public class MapViewModel extends AndroidViewModel {

    private GymLocationRepository repository;
    private LiveData<List<GymLocation>> allLocations;

    public MapViewModel(@NonNull Application application) {
        super(application);
        repository = new GymLocationRepository(application);
        allLocations = repository.getAllLocations();
    }

    public LiveData<List<GymLocation>> getAllLocations() {
        return allLocations;
    }

    public void insert(GymLocation location) {
        repository.insert(location);
    }

    public void delete(GymLocation location) {
        repository.delete(location);
    }
}
