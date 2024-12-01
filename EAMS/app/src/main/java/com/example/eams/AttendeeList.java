package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AttendeeList extends AppCompatActivity {

    private TextView eventNameTextView, eventIdTextView;
    private ListView attendeesListView;
    private Button acceptButton, acceptAllButton;
    private DatabaseHelperForEvent dbHelper;
    private int eventId;
    private String eventName;
    private List<String> attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);

        // Initialize views
        eventNameTextView = findViewById(R.id.eventNameTextView);
        eventIdTextView = findViewById(R.id.eventIdTextView);
        attendeesListView = findViewById(R.id.attendeesListView);
        acceptButton = findViewById(R.id.acceptButton);
        acceptAllButton = findViewById(R.id.acceptAllButton);

        // Initialize database helper
        dbHelper = new DatabaseHelperForEvent(this);

        // Get event details passed from the previous activity
        eventId = getIntent().getIntExtra("eventId", -1);
        eventName = getIntent().getStringExtra("eventName");

        if (eventId == -1) {
            Toast.makeText(this, "Error: Event ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set event details in the TextViews
        eventNameTextView.setText("Event Name: " + eventName);
        eventIdTextView.setText("Event ID: " + eventId);

        // Load attendees for the event
        loadAttendees();

        // Set up button functionality
        setButtonListeners();
    }

    private void loadAttendees() {
        // Fetch the attendees from the database
        attendees = dbHelper.getAttendees(eventId);

        if (attendees.isEmpty()) {
            Toast.makeText(this, "No attendees found for this event.", Toast.LENGTH_SHORT).show();
        }

        // Set up the ListView with the fetched attendees
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendees);
        attendeesListView.setAdapter(adapter);
    }

    private void setButtonListeners() {
        // Accept a selected attendee
        acceptButton.setOnClickListener(v -> {
            int position = attendeesListView.getCheckedItemPosition();
            if (position != ListView.INVALID_POSITION) {
                String selectedAttendee = attendees.get(position);
                boolean updated = dbHelper.updateAttendeeStatus(eventId, selectedAttendee, "approved");
                if (updated) {
                    Toast.makeText(this, "Attendee " + selectedAttendee + " accepted.", Toast.LENGTH_SHORT).show();
                    loadAttendees(); // Refresh the list
                } else {
                    Toast.makeText(this, "Failed to accept attendee.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select an attendee to accept.", Toast.LENGTH_SHORT).show();
            }
        });

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
}