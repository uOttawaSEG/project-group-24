package com.example.eams;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class InboxActivity extends AppCompatActivity{
    private TextView registrationInfo;
    private Button acceptButton, rejectButton, backButton;
    private DatabaseHelper databaseHelper;
    private List<UserRegistration> pendingRegistrations;
    private int currentIndex = 0; // Tracks the current registration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        initializeViews();
        setClickListeners();
        databaseHelper = new DatabaseHelper(this);

        ListView listView = findViewById(R.id.listviews);
        // Retrieve the user list from the database
        List<UserRegistration> userList = databaseHelper.connectToDatabase();

        Log.d("InboxActivity", "User List: " + userList.toString());


        // Create an ArrayAdapter for UserRegistration objects
        ArrayAdapter<UserRegistration> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);

        listView.setAdapter(arrayAdapter);
    }

    private void initializeViews() {
        acceptButton = findViewById(R.id.acceptButton);
        rejectButton = findViewById(R.id.rejectButton);
        backButton = findViewById(R.id.backButton);
    }

    private void setClickListeners() {
        acceptButton.setOnClickListener(v -> Accepting());
        rejectButton.setOnClickListener(v -> Rejecting());
        backButton.setOnClickListener(v -> goBackToMain());
    }

    private void Rejecting() {
        Intent intent = new Intent();
        startActivity(intent);
    }

    private void Accepting() {
        Intent intent = new Intent();
        startActivity(intent);
    }

    private void goBackToMain() {
        Intent intent = new Intent(InboxActivity.this, AdminHome.class);
        startActivity(intent);
        finish();
    }

}
