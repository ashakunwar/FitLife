package com.example.fitlife.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitlife.R;
import com.example.fitlife.data.database.AppDatabase;
import com.example.fitlife.data.model.User;
import com.example.fitlife.data.repository.UserRepository;
import com.example.fitlife.utils.PasswordHasher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepository = new UserRepository(getApplication());

        TextInputEditText fullNameEditText = findViewById(R.id.full_name);
        TextInputEditText emailEditText = findViewById(R.id.email);
        TextInputEditText passwordEditText = findViewById(R.id.password);
        TextInputEditText confirmPasswordEditText = findViewById(R.id.confirm_password);
        MaterialButton signupButton = findViewById(R.id.signup_button);
        TextView loginLink = findViewById(R.id.login_link);

        signupButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.databaseWriteExecutor.execute(() -> {
                User existingUser = userRepository.getUserByUsername(email);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show());
                } else {
                    User newUser = new User();
                    newUser.fullName = fullName;
                    newUser.username = email; // Using email as username
                    newUser.password = PasswordHasher.hashPassword(password);
                    userRepository.insert(newUser);

                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "Registration successful. Please log in.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            });
        });

        loginLink.setOnClickListener(v -> {
            // Go back to login screen
            finish();
        });
    }
}
