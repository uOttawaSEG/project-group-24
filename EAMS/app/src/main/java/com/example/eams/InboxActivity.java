package com.example.eams;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity {
    private TextView registrationInfo;
    private Button backButton;
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private ArrayAdapter<UserRegistration> arrayAdapter; // Declare ArrayAdapter here
    private List<UserRegistration> userList; // Declare userList here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        initializeViews();
        setClickListeners();
        databaseHelper = new DatabaseHelper(this);

        listView = findViewById(R.id.listviews);

        // Load the pending users from the database
        loadPendingUsers();

        // Set up long click listener for the list view
        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            UserRegistration user = userList.get(position);
            showUpdateDeleteDialog(user.getEmail(), user.getFirstName() + " " + user.getLastName());
            return true;
        });
    }

    private void initializeViews() {
        registrationInfo = findViewById(R.id.registrationInfo);
        backButton = findViewById(R.id.backButton);
    }

    private void setClickListeners() {
        backButton.setOnClickListener(v -> goBackToMain());
    }

    private void goBackToMain() {
        Intent intent = new Intent(InboxActivity.this, AdminHome.class);
        startActivity(intent);
        finish();
    }

    private void loadPendingUsers() {
        // Retrieve the pending users list from the database
        userList = databaseHelper.getUserStatus("pending");
        Log.d("InboxActivity", "User List: " + userList.toString());

        // Create an ArrayAdapter for UserRegistration objects
        arrayAdapter = new ArrayAdapter<UserRegistration>(this, android.R.layout.simple_list_item_1, userList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
                }

                UserRegistration user = getItem(position);

                TextView text1 = convertView.findViewById(android.R.id.text1);
                TextView text2 = convertView.findViewById(android.R.id.text2);

                if (user != null) {
                    text1.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                    text2.setText(user.getEmail());
                }

                return convertView;
            }
        };

        listView.setAdapter(arrayAdapter);
    }

    // Method to show update/delete dialog
    private void showUpdateDeleteDialog(final String userId, String userName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.status_dialog, null);
        dialogBuilder.setView(dialogView);

        Button buttonAccept = dialogView.findViewById(R.id.acceptButton);
        Button buttonReject = dialogView.findViewById(R.id.rejectButton);
        Button statusBackButton = dialogView.findViewById(R.id.statusBackButton);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Accept button click listener
        buttonAccept.setOnClickListener(v -> {
            acceptUser(userId); // Implement accept logic here
            alertDialog.dismiss();
        });

        // Reject button click listener
        buttonReject.setOnClickListener(v -> {
            rejectUser(userId); // Implement reject logic here
            alertDialog.dismiss();
        });

        // Back button click listener
        statusBackButton.setOnClickListener(v -> alertDialog.dismiss());
    }

    // Method to accept a user by updating their status
    private void acceptUser(String userId) {
        boolean isUpdated = databaseHelper.updateUserStatus(userId, "accepted");
        if (isUpdated) {
            Toast.makeText(this, "User accepted: " + userId, Toast.LENGTH_SHORT).show();
            refreshPendingUsers(); // Refresh the list
        } else {
            Toast.makeText(this, "Failed to accept user: " + userId, Toast.LENGTH_SHORT).show();
        }
    }

    // Method to reject a user by updating their status
    private void rejectUser(String userId) {
        boolean isUpdated = databaseHelper.updateUserStatus(userId, "rejected");
        if (isUpdated) {
            Toast.makeText(this, "User rejected: " + userId, Toast.LENGTH_SHORT).show();
            refreshPendingUsers(); // Refresh the list
        } else {
            Toast.makeText(this, "Failed to reject user: " + userId, Toast.LENGTH_SHORT).show();
        }
    }

    // Method to refresh the list of pending users
    private void refreshPendingUsers() {
        loadPendingUsers(); // Reload the pending users from the database
    }
}
