package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RejectedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected);

        TextView messageTextView = findViewById(R.id.rejectedMessageTextView);
        messageTextView.setText("Your application has been rejected. Please contact the admin for further details.");

        // Initialize the back button and set its click listener
        Button backToLoginButton = findViewById(R.id.backToLoginButton);
        backToLoginButton.setOnClickListener(v -> goBackToLogin());
    }

    private void goBackToLogin() {
        Intent intent = new Intent(RejectedActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Call finish() if you want to remove RejectedActivity from the back stack
    }
}
