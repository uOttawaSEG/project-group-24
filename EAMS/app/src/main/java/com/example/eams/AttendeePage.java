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

public class AttendeePage extends AppCompatActivity {

    private Button logout; // Declare the logout button
    private Button myEvents, signUp; // Declare the myevents and signup buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_page);

        // Apply window insets to handle system bars (e.g., status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the views and set up click listeners
        initializeViews();
        setClickListeners();

        // Initialize the welcome message
        initWelcomeMessage();
    }

    private void initializeViews() {
        logout = findViewById(R.id.logoutButton); // Initialize the logout button
        myEvents = findViewById(R.id.myEventsButton); // Initialize the myEvents button
        signUp = findViewById(R.id.signUpButton); // Initialize the signup button
    }

    private void setClickListeners() {
        // Set up the logout button click listener
        logout.setOnClickListener(v -> logoutUser());
        myEvents.setOnClickListener(v -> openMyEventsPage());
        signUp.setOnClickListener(v -> openSignUpPage());
    }

    private void openMyEventsPage() {
        Intent intent = new Intent(AttendeePage.this, AttendeeEvents.class);
        intent.putExtra("attendeeEmail", getIntent().getStringExtra("userName"));
        startActivity(intent);
    }


    private void openSignUpPage() {
        Intent intent = new Intent(AttendeePage.this, SignUpEventActivity.class);
        intent.putExtra("attendeeEmail", getIntent().getStringExtra("userName"));
        startActivity(intent);
    }

    private void logoutUser() {
        // Log the user out and return to the main activity
        Intent intent = new Intent(AttendeePage.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optionally call finish() to remove this activity from the back stack
    }

    private void initWelcomeMessage() {
        // Retrieve the userName (email) from the intent
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userName");

        // Find the TextViews for displaying the welcome message and email
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        TextView userEmailTextView = findViewById(R.id.userEmailTextView);

        // Update the welcome message
        welcomeTextView.setText("Welcome, Attendee!");

        // Update the user's email

        userEmailTextView.setText("Email: " + userEmail);

    }
}