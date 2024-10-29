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
import java.util.List;

public class InboxActivity extends AppCompatActivity {
    private TextView registrationInfo;
    private Button backButton;
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

        // Retrieve the user list from the database using your SQL query
        List<UserRegistration> userList = databaseHelper.connectToDatabase();

        Log.d("InboxActivity", "User List: " + userList.toString());

        // Create an ArrayAdapter for UserRegistration objects
        ArrayAdapter<UserRegistration> arrayAdapter = new ArrayAdapter<UserRegistration>(this, android.R.layout.simple_list_item_1, userList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Inflate the view if necessary
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
                }

                // Get the current user
                UserRegistration user = getItem(position);

                // Find the TextViews in the layout
                TextView text1 = convertView.findViewById(android.R.id.text1);
                TextView text2 = convertView.findViewById(android.R.id.text2);

                // Set the user information (you can customize this as needed)
                if (user != null) {
                    text1.setText(String.format("%s %s", user.getFirstName(), user.getLastName())); // Display full name
                    text2.setText(user.getEmail()); // Display email or any other attribute
                }

                return convertView;
            }
        };

        listView.setAdapter(arrayAdapter);

        // Set up long click listener for the list view
        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            UserRegistration user = userList.get(position); // Get the clicked user
            showUpdateDeleteDialog(user.getEmail(), user.getFirstName() + " " + user.getLastName()); // Call your method to show the dialog
            return true;
        });
    }

    private void initializeViews() {
        registrationInfo = findViewById(R.id.registrationInfo);
        backButton = findViewById(R.id.backButton); // Keep only backButton
    }

    private void setClickListeners() {
        backButton.setOnClickListener(v -> goBackToMain()); // Only backButton listener
    }

    private void goBackToMain() {
        Intent intent = new Intent(InboxActivity.this, AdminHome.class);
        startActivity(intent);
        finish();
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
        statusBackButton.setOnClickListener(v -> {
            alertDialog.dismiss(); // Just close the dialog
        });
    }

    // Placeholder method for accepting a user
    private void acceptUser(String userId) {
        // Add your logic to accept the user (e.g., update the database)
        Toast.makeText(this, "User accepted: " + userId, Toast.LENGTH_SHORT).show();
    }

    // Placeholder method for rejecting a user
    private void rejectUser(String userId) {
        // Add your logic to reject the user (e.g., update the database)
        Toast.makeText(this, "User rejected: " + userId, Toast.LENGTH_SHORT).show();
    }

    private void Back() {
        // Add your logic to reject the user (e.g., update the database)

    }


}
