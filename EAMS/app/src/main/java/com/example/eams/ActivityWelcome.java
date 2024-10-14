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

public class ActivityWelcome extends AppCompatActivity {
    private Button logout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setClickListeners();
        initWelcomeMessage();
    }

    private void initializeViews() {
        logout = findViewById(R.id.logoutButton);
    }

    private void setClickListeners() {
        logout.setOnClickListener(v -> logoutUser());

    }

    private void logoutUser() {
        Intent intent = new Intent(ActivityWelcome.this, MainActivity.class);
        startActivity((intent));
    }


    private void initWelcomeMessage(){
        Intent intent = getIntent();
        String role = intent.getStringExtra("roleName");
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome! \n You are logged in as\n" + role);
    }
}