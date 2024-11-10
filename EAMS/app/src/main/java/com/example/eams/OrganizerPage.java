package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

public class OrganizerPage extends AppCompatActivity {
    private Button logout;
    private Button create, view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set the content view for this activity
        setContentView(R.layout.activity_organizer_page);

        // Apply window insets to handle system UI like status bar and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the views and set up the click listeners
        initializeViews();
        setClickListeners();

        // Initialize the welcome message with the user's role and email
        initWelcomeMessage();
    }

    // Initialize the views (like buttons and text fields)
    private void initializeViews() {

        logout = findViewById(R.id.logoutButton);
        create = findViewById(R.id.createButton);
        view = findViewById(R.id.viewButton);
    }

    // Set up the listeners for buttons or other UI elements
    private void setClickListeners() {

        logout.setOnClickListener(v -> logoutUser());
        create.setOnClickListener(v -> createEvent());
        view.setOnClickListener(v -> viewEvents());
    }

    private void createEvent() {
        Intent intent = new Intent(OrganizerPage.this, CreateEventActivity.class);
        intent.putExtra("organizerId", getIntent().getStringExtra("userName"));
        startActivity(intent);
    }

    private void viewEvents() {
        Intent intent = new Intent(OrganizerPage.this, ViewEventActivity.class);
        intent.putExtra("organizerId", getIntent().getStringExtra("userName"));
        startActivity(intent);
    }

    // Method to handle logging out and navigating back to the main activity
    private void logoutUser() {
        Intent intent = new Intent(OrganizerPage.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optionally call finish() to remove this activity from the back stack
    }

    // Method to initialize the welcome message and user email based on intent extras
    private void initWelcomeMessage() {
        // Retrieve roleName and userEmail from the intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");

        // Find the TextViews for displaying the welcome message and email
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        TextView userEmailTextView = findViewById(R.id.userEmailTextView);

        welcomeTextView.setText("Welcome, Organizer!");
        userEmailTextView.setText("Email : "+ userName);

    }
}

