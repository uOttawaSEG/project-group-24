package com.example.eams;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RejectedRequests extends AppCompatActivity{
    private Button backButton, acceptButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejects); // Ensure this layout exists in res/layout

        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        //TODO
        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
    }

    private void setClickListeners() {
        //TODO
        backButton.setOnClickListener(v -> goBack());
        acceptButton.setOnClickListener(v-> Accepted());
    }

    private void goBack() {
        Intent intent = new Intent(RejectedRequests.this, AdminHome.class);
        startActivity(intent);
        finish();
    }
    private void Accepted() {

    }


}
