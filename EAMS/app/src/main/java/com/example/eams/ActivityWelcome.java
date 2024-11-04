package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityWelcome extends AppCompatActivity {
    private Button logout, proceed;

    // This variable sets the delayed milli seconds for the page to automatically redirect to another
    private int delayMillis = 3000;
    
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
        proceed.setOnClickListener(v -> toHome());

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

    private void toHome(){
        Intent intent = getIntent();
        Intent next;
        String role = intent.getStringExtra("roleName");
        //proceed to different home page base on roleName
        if(role.equals("Attendee")){
            //for deliverable 4
            next = null;
            Toast.makeText(this, "Attendee function is not implemented until deliverable 4.", Toast.LENGTH_SHORT).show();
        }
        else if(role.equals("Organizer")){
            next = new Intent(ActivityWelcome.this, OrganizerHome.class);
            startActivity(next);
        }
        else{
            Toast.makeText(this, "Error occured, cannot proceed!", Toast.LENGTH_SHORT).show();
        }
    }
}