package com.example.eams;

import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // UI component
    private EditText username, password;
    private Button register, login;
    public DatabaseHelper databaseHelper;
    private final String adminname = "admin";
    private final String adminpassword = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setClickListeners();
        databaseHelper = new DatabaseHelper(this); // Properly initialize the global variable
    }

    private void initializeViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.Register);
        login = findViewById(R.id.submitButton);
    }

    private void setClickListeners() {
        register.setOnClickListener(v -> RegisterUser());
        login.setOnClickListener(v -> LoginUser());
    }

    private void LoginUser() {
        String inputEmail = username.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

        // Hardcoded admin credentials
        if (inputEmail.equals(adminname) && inputPassword.equals(adminpassword)) {
            // Admin login, redirect to welcome screen with admin role
            Intent adminIntent = new Intent(MainActivity.this, AdminHome.class);
            startActivity(adminIntent);
            return;
        }

        // Ensure the email and password fields are not empty
        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the email and password are valid for regular users
        if (databaseHelper.isValidUser(inputEmail)) {
            // Get the user's role and status from the database
            String userRole = databaseHelper.getUserRole(inputEmail);
            String userStatus = databaseHelper.getKeyStatus(inputEmail); // Assuming you have this method

            // If the user role is found, proceed to the appropriate screen based on their status
            if (userRole != null) {
                Intent intent;
                switch (userStatus) {
                    case "accepted":
                        intent = new Intent(MainActivity.this, ActivityWelcome.class);
                        intent.putExtra("roleName", userRole);  // Pass the user's role to the next activity
                        intent.putExtra("email", inputEmail); // Add this line to pass the email
                        break;
                    case "rejected":
                        intent = new Intent(MainActivity.this, RejectedActivity.class); // Activity for rejected users
                        break;
                    case "pending":
                        intent = new Intent(MainActivity.this, PendingActivity.class); // Activity for pending users
                        break;
                    default:
                        intent = new Intent(MainActivity.this, MainActivity.class); // Redirect to main if status is unknown
                        break;
                }
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void RegisterUser() {
        Intent intent = new Intent(MainActivity.this, Registerpage.class);
        startActivity(intent);
    }
}
