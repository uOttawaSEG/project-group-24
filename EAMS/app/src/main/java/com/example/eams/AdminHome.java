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

public class AdminHome extends AppCompatActivity {
    private Button inbox, rejected, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminhomepage);
        initializeViews();
        setClickListeners();

    }

    private void setClickListeners() {
        logout.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        Intent intent = new Intent(AdminHome.this, MainActivity.class);
        startActivity((intent));
    }

    private void initializeViews() {

        inbox = findViewById(R.id.inboxButton);
        rejected = findViewById(R.id.rejectedButton);
        logout = findViewById(R.id.logoutButton);
    }
}
