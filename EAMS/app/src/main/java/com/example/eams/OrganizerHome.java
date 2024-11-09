package com.example.eams;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerHome extends AppCompatActivity {

    //UI components
    private Button create, view, logout;
    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home);

        // Get the organizer's email from the intent
        organizerId = getIntent().getStringExtra("email");

        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        create = findViewById(R.id.create);
        view = findViewById(R.id.view);
        logout = findViewById(R.id.logout);
    }

    private void setClickListeners() {
        create.setOnClickListener(v -> openCreateEvents());
        view.setOnClickListener(v -> openViewEvents());
        logout.setOnClickListener(v -> logoutUser());
    }

    private void openCreateEvents() {
        Intent intent = new Intent(OrganizerHome.this, CreateEventActivity.class);
        intent.putExtra("organizerId", organizerId); // Pass the organizer's email to CreateEventActivity
        startActivity(intent);
    }

    private void openViewEvents() {
        // This should be implemented later for viewing events
        
    }

    private void logoutUser() {
        Intent intent = new Intent(OrganizerHome.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
