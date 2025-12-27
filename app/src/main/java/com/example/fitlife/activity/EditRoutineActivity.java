package com.example.fitlife.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.fitlife.R;
import com.example.fitlife.data.model.GymLocation;
import com.example.fitlife.data.model.WorkoutRoutine;
import com.example.fitlife.viewmodel.MapViewModel;
import com.example.fitlife.viewmodel.RoutineListViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditRoutineActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTINE_ID = "com.example.fitlife.EXTRA_ROUTINE_ID";

    private LinearLayout exerciseContainer;
    private ImageView routineImageView;
    private Spinner locationSpinner;
    private TextInputEditText nameEditText, instructionsEditText, equipmentEditText;

    private Uri imageUri;
    private RoutineListViewModel routineListViewModel;
    private MapViewModel mapViewModel;

    private long currentRoutineId = -1;
    private WorkoutRoutine currentRoutine = null;
    private List<GymLocation> gymLocations = new ArrayList<>();
    private long selectedLocationId = -1;

    private final ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success) {
                    routineImageView.setImageURI(imageUri);
                }
            });

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    routineImageView.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        // Initialize ViewModels
        routineListViewModel = new ViewModelProvider(this).get(RoutineListViewModel.class);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        // Find Views
        exerciseContainer = findViewById(R.id.exercise_container);
        locationSpinner = findViewById(R.id.location_spinner);
        routineImageView = findViewById(R.id.routine_image);
        nameEditText = findViewById(R.id.routine_name);
        instructionsEditText = findViewById(R.id.instructions);
        equipmentEditText = findViewById(R.id.equipment);
        MaterialButton addExerciseButton = findViewById(R.id.add_exercise_button);
        MaterialButton saveRoutineButton = findViewById(R.id.save_routine_button);

        // Set Listeners
        addExerciseButton.setOnClickListener(v -> addExerciseView(null));
        routineImageView.setOnClickListener(v -> showImagePickerDialog());
        saveRoutineButton.setOnClickListener(v -> saveRoutine());

        setupLocationSpinner();

        // Check for Edit Mode
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ROUTINE_ID)) {
            setTitle("Edit Routine");
            currentRoutineId = intent.getLongExtra(EXTRA_ROUTINE_ID, -1);
            if (currentRoutineId != -1) {
                routineListViewModel.getRoutineById(currentRoutineId).observe(this, routine -> {
                    if (routine != null) {
                        currentRoutine = routine;
                        populateUI(routine);
                    }
                });
            }
        } else {
            setTitle("Create Routine");
            addExerciseView(null); // Add one empty row for new routines
        }
    }

    private void setupLocationSpinner() {
        mapViewModel.getAllLocations().observe(this, locations -> {
            gymLocations = locations;
            List<String> locationNames = new ArrayList<>();
            locationNames.add("No location"); // Default option
            int selectionIndex = 0;
            for (int i = 0; i < locations.size(); i++) {
                locationNames.add(locations.get(i).getName());
                if (currentRoutine != null && currentRoutine.getLocationId() == locations.get(i).getId()) {
                    selectionIndex = i + 1; // Adjust for "No location" at index 0
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);
            locationSpinner.setSelection(selectionIndex);
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocationId = (position > 0) ? gymLocations.get(position - 1).getId() : -1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLocationId = -1;
            }
        });
    }

    private void populateUI(WorkoutRoutine routine) {
        nameEditText.setText(routine.getName());
        instructionsEditText.setText(routine.getInstructions());
        equipmentEditText.setText(routine.getEquipment());

        if (routine.getImageUri() != null && !routine.getImageUri().isEmpty()) {
            imageUri = Uri.parse(routine.getImageUri());
            Glide.with(this).load(imageUri).into(routineImageView);
        }

        exerciseContainer.removeAllViews();
        if (routine.getExercises() != null && !routine.getExercises().isEmpty()) {
            String[] exercises = routine.getExercises().split("\\n");
            for (String exercise : exercises) {
                if (!TextUtils.isEmpty(exercise)) {
                    addExerciseView(exercise);
                }
            }
        }
    }

    private void addExerciseView(String exerciseData) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View exerciseView = inflater.inflate(R.layout.item_exercise_row, exerciseContainer, false);

        TextInputEditText exerciseNameEditText = exerciseView.findViewById(R.id.exercise_name);
        TextInputEditText setsEditText = exerciseView.findViewById(R.id.sets);
        TextInputEditText repsEditText = exerciseView.findViewById(R.id.reps);

        if (exerciseData != null) {
            try {
                String[] parts = exerciseData.split(":");
                String name = parts[0].trim();
                String[] setsAndReps = parts[1].trim().split("sets x");
                String sets = setsAndReps[0].trim();
                String reps = setsAndReps[1].replace("reps", "").trim();

                exerciseNameEditText.setText(name);
                setsEditText.setText(sets);
                repsEditText.setText(reps);
            } catch (Exception e) {
                exerciseNameEditText.setText(exerciseData); // Fallback if parsing fails
            }
        }

        exerciseView.findViewById(R.id.delete_exercise_button).setOnClickListener(v -> exerciseContainer.removeView(exerciseView));
        exerciseContainer.addView(exerciseView);
    }

    private void showImagePickerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Choose Image Source")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        imageUri = createTempImageUri();
                        takePictureLauncher.launch(imageUri);
                    } else {
                        pickImageLauncher.launch("image/*");
                    }
                }).show();
    }

    private Uri createTempImageUri() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
            return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);
        } catch (IOException e) {
            Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void saveRoutine() {
        String name = nameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a routine name", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder exercisesStringBuilder = new StringBuilder();
        for (int i = 0; i < exerciseContainer.getChildCount(); i++) {
            View exerciseView = exerciseContainer.getChildAt(i);
            TextInputEditText exerciseNameEditText = exerciseView.findViewById(R.id.exercise_name);
            TextInputEditText setsEditText = exerciseView.findViewById(R.id.sets);
            TextInputEditText repsEditText = exerciseView.findViewById(R.id.reps);
            String exerciseName = exerciseNameEditText.getText().toString().trim();
            String sets = setsEditText.getText().toString().trim();
            String reps = repsEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(exerciseName)) {
                exercisesStringBuilder.append(exerciseName).append(": ").append(TextUtils.isEmpty(sets) ? "0" : sets).append(" sets x ").append(TextUtils.isEmpty(reps) ? "0" : reps).append(" reps\n");
            }
        }

        WorkoutRoutine routineToSave = (currentRoutine != null) ? currentRoutine : new WorkoutRoutine();
        routineToSave.setName(name);
        routineToSave.setExercises(exercisesStringBuilder.toString());
        routineToSave.setEquipment(equipmentEditText.getText().toString().trim());
        routineToSave.setInstructions(instructionsEditText.getText().toString().trim());
        routineToSave.setLocationId(selectedLocationId);
        if (imageUri != null) {
            routineToSave.setImageUri(imageUri.toString());
        }

        if (routineToSave.getId() == 0) { // It's a new routine
            routineListViewModel.insert(routineToSave);
            Toast.makeText(this, "Routine created successfully", Toast.LENGTH_SHORT).show();
        } else { // It's an existing routine
            routineListViewModel.update(routineToSave);
            Toast.makeText(this, "Routine updated successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
