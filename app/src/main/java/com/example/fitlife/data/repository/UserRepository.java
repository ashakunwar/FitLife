package com.example.fitlife.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.fitlife.data.dao.UserDao;
import com.example.fitlife.data.database.AppDatabase;
import com.example.fitlife.data.model.User;

public class UserRepository {

    private UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
    }

    public User getUser(String username, String password) {
        return userDao.getUser(username, password);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public void insert(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }
}
