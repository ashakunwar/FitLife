package com.example.fitlife.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.fitlife.data.model.User;
import com.example.fitlife.data.repository.UserRepository;

public class ProfileViewModel extends AndroidViewModel {

    private UserRepository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<User> getUserById(int userId) {
        return repository.getUserById(userId);
    }
}
