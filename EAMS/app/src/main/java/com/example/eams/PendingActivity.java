package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PendingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        TextView messageTextView = findViewById(R.id.pendingMessageTextView);
        messageTextView.setText("Your application is still pending. Please wait for approval.");

        // Initialize the back button and set its click listener
        Button backToLoginButton = findViewById(R.id.backToLoginButton);
        backToLoginButton.setOnClickListener(v -> goBackToLogin());
    }

    private void goBackToLogin() {
        Intent intent = new Intent(PendingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
