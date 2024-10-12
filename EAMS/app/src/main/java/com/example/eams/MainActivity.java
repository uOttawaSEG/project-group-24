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
    //UI component

    private EditText username, password;
    private Button register, login;
    public DatabaseHelper databaseHelper;
    private final String adminname ="admin";
    private final String adminpassword ="123";



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
            Intent welcomeIntent = new Intent(MainActivity.this, ActivityWelcome.class);
            welcomeIntent.putExtra("roleName", "Administrator");
            startActivity(welcomeIntent);
            return;
        }

        // Ensure the email and password fields are not empty
        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the email and password are valid for regular users
        if (databaseHelper.isValidUser(inputEmail)) {
            // Get the user's role from the database
            String userRole = databaseHelper.getUserRole(inputEmail);

            // If the user role is found, proceed to the welcome screen with the user's role
            if (userRole != null) {
                Intent welcomeIntent = new Intent(MainActivity.this, ActivityWelcome.class);
                welcomeIntent.putExtra("roleName", userRole);  // Pass the user's role to the next activity
                startActivity(welcomeIntent);
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