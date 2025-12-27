package com.example.fitlife.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitlife.R;
import com.example.fitlife.adapter.RoutineAdapter;
import com.example.fitlife.data.model.WorkoutRoutine;
import com.example.fitlife.viewmodel.MapViewModel;
import com.example.fitlife.viewmodel.RoutineListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RoutineListActivity extends AppCompatActivity {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    public static final String EXTRA_ROUTINE_ID = "com.example.fitlife.EXTRA_ROUTINE_ID";

    private RoutineListViewModel routineListViewModel;
    private MapViewModel mapViewModel;
    private WorkoutRoutine routineToSendSms; // Temporary holder for the routine
    private String phoneToSendSms;      // Temporary holder for the phone number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_routines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final RoutineAdapter adapter = new RoutineAdapter(this);
        recyclerView.setAdapter(adapter);

        // Set up ViewModels
        routineListViewModel = new ViewModelProvider(this).get(RoutineListViewModel.class);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        // Observe routines
        routineListViewModel.getAllRoutines().observe(this, adapter::setRoutines);

        // Observe locations and pass them to the adapter
        mapViewModel.getAllLocations().observe(this, adapter::setLocations);

        adapter.setOnRoutineListener(new RoutineAdapter.OnRoutineListener() {
            @Override
            public void onEditClicked(WorkoutRoutine routine) {
                Intent intent = new Intent(RoutineListActivity.this, EditRoutineActivity.class);
                intent.putExtra(EditRoutineActivity.EXTRA_ROUTINE_ID, routine.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClicked(WorkoutRoutine routine) {
                new AlertDialog.Builder(RoutineListActivity.this)
                        .setTitle("Delete Routine")
                        .setMessage("Are you sure you want to delete this routine?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            routineListViewModel.delete(routine);
                            Toast.makeText(RoutineListActivity.this, "Routine deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_delete)
                        .show();
            }

            @Override
            public void onShareSmsClicked(WorkoutRoutine routine) {
                showSmsDialog(routine);
            }

            @Override
            public void onRoutineCompletionChanged(WorkoutRoutine routine, boolean isCompleted) {
                routineListViewModel.updateCompletionStatus(routine.getId(), isCompleted);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_routine);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(RoutineListActivity.this, EditRoutineActivity.class);
            startActivity(intent);
        });
    }

    private void showSmsDialog(WorkoutRoutine routine) {
        // ... (SMS dialog logic as before)
    }

    private void sendSms(String phoneNumber, WorkoutRoutine routine) {
        // ... (SMS sending logic as before)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // ... (Permission result logic as before)
    }
}
