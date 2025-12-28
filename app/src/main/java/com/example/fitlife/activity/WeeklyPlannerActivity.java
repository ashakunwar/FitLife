package com.example.fitlife.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fitlife.R;
import com.example.fitlife.adapter.WeeklyPlannerAdapter;
import com.example.fitlife.data.model.WeeklyPlan;
import com.example.fitlife.data.model.WorkoutRoutine;
import com.example.fitlife.utils.DayDisplayInfo;
import com.example.fitlife.viewmodel.WeeklyPlannerViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeeklyPlannerActivity extends AppCompatActivity {

    private WeeklyPlannerViewModel weeklyPlannerViewModel;
    private WeeklyPlannerAdapter adapter;
    private List<WorkoutRoutine> routineList = new ArrayList<>();
    private Map<Long, WorkoutRoutine> routineMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner);

        RecyclerView recyclerView = findViewById(R.id.week_days_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeeklyPlannerAdapter();
        recyclerView.setAdapter(adapter);

        weeklyPlannerViewModel = new ViewModelProvider(this).get(WeeklyPlannerViewModel.class);

        setupWeekDays();

        weeklyPlannerViewModel.getAllRoutines().observe(this, routines -> {
            this.routineList = routines;
            routineMap.clear();
            for (WorkoutRoutine routine : routines) {
                routineMap.put(routine.getId(), routine);
            }
            // We need to re-trigger the plans observer to update the adapter with the new routine names
            weeklyPlannerViewModel.getAllPlans().observe(this, plans -> {
                Map<Integer, WeeklyPlan> planMap = new HashMap<>();
                for (WeeklyPlan plan : plans) {
                    planMap.put(plan.getDayOfWeek(), plan);
                }
                adapter.setWeeklyData(planMap, routineMap);
            });
        });

        adapter.setOnDayClickListener(new WeeklyPlannerAdapter.OnDayClickListener() {
            @Override
            public void onDayClicked(int dayOfWeek) {
                showAssignRoutineDialog(dayOfWeek);
            }

            @Override
            public void onDayCompletionChanged(WeeklyPlan plan, boolean isCompleted) {
                plan.setCompleted(isCompleted);
                weeklyPlannerViewModel.insertOrUpdate(plan);
            }
        });
    }

    private void setupWeekDays() {
        List<DayDisplayInfo> weekDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            String dayName = dayFormat.format(calendar.getTime());
            String date = dateFormat.format(calendar.getTime());
            boolean isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                              calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
            weekDays.add(new DayDisplayInfo(dayName, date, isToday));
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        adapter.setWeekDays(weekDays);
    }

    private void showAssignRoutineDialog(int dayOfWeek) {
        List<String> routineNames = new ArrayList<>();
        routineNames.add("Rest Day"); // Add Rest Day option
        for (WorkoutRoutine routine : routineList) {
            routineNames.add(routine.getName());
        }

        new AlertDialog.Builder(this)
                .setTitle("Assign Routine")
                .setItems(routineNames.toArray(new String[0]), (dialog, which) -> {
                    if (which == 0) { // Rest Day
                        weeklyPlannerViewModel.deleteByDay(dayOfWeek);
                    } else {
                        WorkoutRoutine selectedRoutine = routineList.get(which - 1); // Adjust index for Rest Day
                        WeeklyPlan newPlan = new WeeklyPlan(dayOfWeek, selectedRoutine.getId(), false);
                        weeklyPlannerViewModel.insertOrUpdate(newPlan);
                    }
                })
                .show();
    }
}
