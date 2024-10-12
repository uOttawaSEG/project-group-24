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

        // Ensure the email and password fields are not empty
        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed to check if the email and password are valid
        if (databaseHelper.isValidUser(inputEmail)) {
            Intent intent = new Intent(MainActivity.this, ActivityWelcome.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }


    }

    private void RegisterUser() {
        Intent intent = new Intent(MainActivity.this, Registerpage.class);
        startActivity(intent);
    }


}