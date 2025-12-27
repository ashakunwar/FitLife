package com.example.fitlife.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitlife.R;
import com.example.fitlife.utils.Constants;
import com.example.fitlife.viewmodel.ProfileViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        TextView welcomeText = findViewById(R.id.welcome_text);
        MaterialCardView routinesCard = findViewById(R.id.routines_card);
        MaterialCardView weeklyPlannerCard = findViewById(R.id.weekly_planner_card);
        MaterialCardView mapCard = findViewById(R.id.map_card);
        MaterialButton logoutButton = findViewById(R.id.logout_button);

        // Set up the welcome message
        int userId = sharedPreferences.getInt(Constants.USER_ID_KEY, -1);
        if (userId != -1) {
            profileViewModel.getUserById(userId).observe(this, user -> {
                if (user != null) {
                    welcomeText.setText("Welcome, " + user.getFullName() + "!");
                }
            });
        }

        // Set up card click listeners
        routinesCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RoutineListActivity.class);
            startActivity(intent);
        });

        weeklyPlannerCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, WeeklyPlannerActivity.class);
            startActivity(intent);
        });

        mapCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MapActivity.class);
            startActivity(intent);
        });

        // Set up logout button with confirmation dialog
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // User clicked Yes, so log them out
                        sharedPreferences.edit().remove(Constants.USER_ID_KEY).apply();
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton(android.R.string.no, null) // No action, just close the dialog
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }
}
