package com.example.eams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendeeList extends AppCompatActivity {

    private TextView eventNameTextView, eventIdTextView;
    private ListView attendeesListView;
    private Button acceptAllButton;
    private DatabaseHelperForEvent dbHelper;  // For event-related operations
    private DatabaseHelper userDbHelper;     // For user-related operations (getUserByEmail)
    private int eventId;
    private String eventName;
    private List<Map<String, String>> attendees;  // Store the list of attendees

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);

        // Initialize views
        eventNameTextView = findViewById(R.id.eventNameTextView);
        eventIdTextView = findViewById(R.id.eventIdTextView);
        attendeesListView = findViewById(R.id.attendeesListView);
        acceptAllButton = findViewById(R.id.acceptAllButton);

        // Initialize database helpers
        dbHelper = new DatabaseHelperForEvent(this);  // For event-related operations
        userDbHelper = new DatabaseHelper(this);      // For user-related operations

        // Get event details passed from the previous activity
        eventId = getIntent().getIntExtra("eventId", -1);

        if (eventId == -1) {
            Toast.makeText(this, "Error: Event ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch event name from the database
        Event event = dbHelper.getEventById(eventId);
        if (event != null) {
            eventName = event.getEventName();
            // Set event details in the TextViews
            eventNameTextView.setText("Event Name: " + eventName);
            eventIdTextView.setText("Event ID: " + eventId);
        } else {
            Toast.makeText(this, "Error: Event not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load attendees for the event
        loadAttendees();

        // Set up button functionality
        setButtonListeners();
    }

    private void loadAttendees() {
        // Fetch the attendees and their statuses from the database
        attendees = dbHelper.getAttendeesWithStatus(eventId);

        if (attendees.isEmpty()) {
            Toast.makeText(this, "No attendees found for this event.", Toast.LENGTH_SHORT).show();
        } else {
            // Create a list of attendees names to display in the ListView
            String[] attendeeNames = new String[attendees.size()];
            for (int i = 0; i < attendees.size(); i++) {
                Map<String, String> attendee = attendees.get(i);
                String name = attendee.get("email");  // or attendee.get("name");
                String status = attendee.get("status");
                attendeeNames[i] = name + " - " + status;  // Combine name and status
            }

            // Use an ArrayAdapter to display the list in the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    attendeeNames
            );
            attendeesListView.setAdapter(adapter);

            // Set long-click listener
            attendeesListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
                Map<String, String> selectedAttendee = attendees.get(position);
                String attendeeEmail = selectedAttendee.get("email");

                // Show accept/decline options
                showAcceptDeclineDialog(attendeeEmail);
                return true;
            });
        }
    }

    private void setButtonListeners() {
        // Accept all attendees
        acceptAllButton.setOnClickListener(v -> {
            boolean updated = dbHelper.updateAllAttendeeStatus(eventId, "approved");
            if (updated) {
                Toast.makeText(this, "All attendees accepted.", Toast.LENGTH_SHORT).show();
                loadAttendees(); // Refresh the list
            } else {
                Toast.makeText(this, "Failed to accept all attendees.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAcceptDeclineDialog(final String attendeeEmail) {
        // Create the alert dialog for accepting or declining the attendee
        new AlertDialog.Builder(this)
                .setTitle("Accept or Decline Attendee")
                .setMessage("Do you want to accept or decline this attendee?")
                .setPositiveButton("Accept", (dialog, which) -> acceptAttendee(attendeeEmail))
                .setNegativeButton("Decline", (dialog, which) -> declineAttendee(attendeeEmail))
                .setNeutralButton("See Details", (dialog, which) -> showUserDetails(attendeeEmail)) // Show user details on button click
                .show();
    }

    private void acceptAttendee(String email) {
        boolean updated = dbHelper.updateAttendeeStatus(eventId, email, "approved");
        if (updated) {
            Toast.makeText(this, "Attendee " + email + " accepted.", Toast.LENGTH_SHORT).show();
            loadAttendees(); // Refresh the list
        } else {
            Toast.makeText(this, "Failed to accept attendee.", Toast.LENGTH_SHORT).show();
        }
    }

    private void declineAttendee(String email) {
        boolean updated = dbHelper.updateAttendeeStatus(eventId, email, "declined");
        if (updated) {
            Toast.makeText(this, "Attendee " + email + " declined.", Toast.LENGTH_SHORT).show();
            loadAttendees(); // Refresh the list
        } else {
            Toast.makeText(this, "Failed to decline attendee.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUserDetails(String email) {
        // Fetch user details from the database using userDbHelper
        UserRegistration user = userDbHelper.getUserByEmail(email); // This uses DatabaseHelper

        if (user != null) {
            // Create the message to show in the dialog
            String message = "Name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                    "Phone: " + user.getPhoneNumber() + "\n" +
                    "Address: " + user.getAddress() + "\n" +
                    "Role: " + user.getRole() + "\n" +
                    "Email: " + user.getEmail();

            // Create the AlertDialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("User Details");  // Title of the dialog
            builder.setMessage(message);      // Set the message (user details)

            // Add a "Close" button to dismiss the dialog
            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());  // Close the dialog

            // Create and show the dialog
            builder.create().show();

        } else {
            // Handle the case where no user is found
            Log.e("showUserDetails", "No user found with email: " + email);
        }
    }

    // This method is used to go back to the previous activity
    public void onBackButtonPressed(View view) {
        finish(); // This will close the current activity and return to the previous one.
    }
}
