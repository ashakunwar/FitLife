package com.example.fitlife.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.fitlife.data.dao.GymLocationDao;
import com.example.fitlife.data.dao.UserDao;
import com.example.fitlife.data.dao.WeeklyPlanDao;
import com.example.fitlife.data.dao.WorkoutRoutineDao;
import com.example.fitlife.data.model.GymLocation;
import com.example.fitlife.data.model.User;
import com.example.fitlife.data.model.WeeklyPlan;
import com.example.fitlife.data.model.WorkoutRoutine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, WorkoutRoutine.class, WeeklyPlan.class, GymLocation.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract WorkoutRoutineDao workoutRoutineDao();
    public abstract WeeklyPlanDao weeklyPlanDao();
    public abstract GymLocationDao gymLocationDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = 
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fitlife_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
