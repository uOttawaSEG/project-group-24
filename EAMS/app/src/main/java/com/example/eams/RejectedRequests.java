package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RejectedRequests extends AppCompatActivity {
    private Button backButton;
    private ListView rejectedUsersListView;
    private DatabaseHelper databaseHelper;
    private List<UserRegistration> rejectedUsers; // List to hold rejected users

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejects); // Ensure this layout exists in res/layout

        databaseHelper = new DatabaseHelper(this); // Initialize the database helper
        rejectedUsers = databaseHelper.getUserStatus("rejected"); // Get rejected users

        initializeViews();
        setClickListeners();
        displayRejectedUsers(); // Display the rejected users
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        rejectedUsersListView = findViewById(R.id.rejectedUsersListView); // Initialize ListView
    }

    private void setClickListeners() {
        backButton.setOnClickListener(v -> goBack());

        // Set a long-click listener for accepting users
        rejectedUsersListView.setOnItemLongClickListener((parent, view, position, id) -> {
            showAcceptRejectDialog(position);
            return true;
        });
    }

    private void goBack() {
        Intent intent = new Intent(RejectedRequests.this, AdminHome.class);
        startActivity(intent);
        finish();
    }

    private void displayRejectedUsers() {
        ArrayList<String> userNames = new ArrayList<>();
        for (UserRegistration user : rejectedUsers) {
            userNames.add(user.getFirstName() + " " + user.getLastName()); // Assuming you have getters for first and last name
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        rejectedUsersListView.setAdapter(adapter);
    }

    private void showAcceptRejectDialog(int position) {
        UserRegistration selectedUser = rejectedUsers.get(position);
        String email = selectedUser.getEmail(); // Get email of the selected user

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.accept_reject_dialog, null); // Make sure this layout exists
        dialogBuilder.setView(dialogView);

        Button acceptButton = dialogView.findViewById(R.id.acceptButton);
        Button backButton = dialogView.findViewById(R.id.backButton); // Button to go back
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        acceptButton.setOnClickListener(v -> {
            acceptUser(position); // Accept user method
            alertDialog.dismiss();
        });

        backButton.setOnClickListener(v -> alertDialog.dismiss()); // Dismiss dialog without any action
    }

    private void acceptUser(int position) {
        UserRegistration selectedUser = rejectedUsers.get(position);
        String email = selectedUser.getEmail(); // Get email of the selected user

        // Call the method to accept the user
        boolean isAccepted = databaseHelper.updateUserStatus(email, "accepted");
        if (isAccepted) {
            Toast.makeText(this, "User accepted: " + email, Toast.LENGTH_SHORT).show();
            // Refresh the list of rejected users
            rejectedUsers.remove(position);
            displayRejectedUsers();
        } else {
            Toast.makeText(this, "Failed to accept user: " + email, Toast.LENGTH_SHORT).show();
        }
    }
}
