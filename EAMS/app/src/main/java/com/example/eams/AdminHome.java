package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminHome extends AppCompatActivity {
    private Button inbox, rejected, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminhomepage);
        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        inbox = findViewById(R.id.inboxButton);
        rejected = findViewById(R.id.rejectedButton);
        logout = findViewById(R.id.logoutButton);
    }

    private void setClickListeners() {
        inbox.setOnClickListener(v -> openInbox());
        rejected.setOnClickListener(v -> openRejectedRequests());
        logout.setOnClickListener(v -> logoutUser());
    }

    private void openInbox() {
        Intent intent = new Intent(AdminHome.this, InboxActivity.class);
        startActivity(intent);
    }

    private void openRejectedRequests() {
        Intent intent = new Intent(AdminHome.this, RejectedRequests.class);
        startActivity(intent);
    }

    private void logoutUser() {
        Intent intent = new Intent(AdminHome.this, MainActivity.class);
        startActivity(intent);
        finish(); // Ensures the user cannot return to this activity by pressing back
    }
}