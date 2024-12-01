package com.example.eams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class AttendeeEvents extends AppCompatActivity {

    private ListView attendeeEventsListView;
    private DatabaseHelperForEvent dbHelper;
    private String attendeeEmail;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_events);

        // Get attendee email from intent
        attendeeEmail = getIntent().getStringExtra("attendeeEmail");
        if (attendeeEmail == null) {
            Toast.makeText(this, "Error: Attendee email not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize the database helper
        dbHelper = new DatabaseHelperForEvent(this);
        initializeViews();

        // Load and display the events the attendee has signed up for
        loadAttendeeEvents();

        // Set up the back button functionality
        backButton.setOnClickListener(v -> back());
    }

    private void initializeViews() {
        // Set up the ListView and Back Button
        attendeeEventsListView = findViewById(R.id.attendeeEventsListView);
        backButton = findViewById(R.id.backButton);
    }

    private void loadAttendeeEvents() {
        // Fetch the list of events the attendee is registered for
        List<Event> signedUpEvents = dbHelper.getEventsForAttendee(attendeeEmail);

        // If no events are found
        if (signedUpEvents.isEmpty()) {
            Toast.makeText(this, "You have not signed up for any events", Toast.LENGTH_SHORT).show();
        } else {
            // Sort the events by date, with the newest event first
            Collections.sort(signedUpEvents, (e1, e2) -> {
                String eventDate1 = e1.getEventDate() + " " + e1.getStartTime();
                String eventDate2 = e2.getEventDate() + " " + e2.getStartTime();
                return eventDate2.compareTo(eventDate1); // Compare in reverse order
            });

            // Use the custom EventAdapter to display the events
            EventAdapter adapter = new EventAdapter(this, signedUpEvents, dbHelper, attendeeEmail);
            attendeeEventsListView.setAdapter(adapter);

            // Set up long click listener for options
            attendeeEventsListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
                Event event = signedUpEvents.get(position);  // Get the event at the clicked position
                showOptionsDialog(event, signedUpEvents);    // Show options dialog
                return true;  // Indicate the event is handled
            });
        }
    }

    private void showOptionsDialog(Event event, List<Event> signedUpEvents) {
        // Create an AlertDialog with three options
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");

        String[] options = {"Go Back", "See Details", "Delete Event"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Go Back
                    dialog.dismiss();
                    break;

                case 1: // See Details
                    showEventDetails(event);
                    break;

                case 2: // Delete Event
                    tryToRemoveEvent(event, signedUpEvents);
                    break;
            }
        });

        builder.create().show();
    }

    private void tryToRemoveEvent(Event event, List<Event> signedUpEvents) {
        // Get the current date and time
        long currentTimeMillis = System.currentTimeMillis();

        // Parse the event's date and time
        String eventDateTimeStr = event.getEventDate() + " " + event.getStartTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());

        try {
            // Convert event date and time to milliseconds
            long eventTimeMillis = sdf.parse(eventDateTimeStr).getTime();
            long timeDifferenceMillis = eventTimeMillis - currentTimeMillis;

            // Check if the event is more than 24 hours away
            if (timeDifferenceMillis > 24 * 60 * 60 * 1000) { // 24 hours in milliseconds
                // Remove the event for the attendee
                boolean isDeleted = dbHelper.removeEventAttendee(event.getEventId(), attendeeEmail);
                if (isDeleted) {
                    signedUpEvents.remove(event); // Remove the event from the list
                    ((ArrayAdapter) attendeeEventsListView.getAdapter()).notifyDataSetChanged(); // Refresh the ListView
                    Toast.makeText(this, "Event removed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to remove event", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Show error message if the event is within 24 hours
                Toast.makeText(this, "Cannot remove an event within 24 hours of its start time.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // Handle parsing errors
            Toast.makeText(this, "Error parsing event date and time.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEventDetails(Event event) {
        // Create the message with the event details
        String eventDetails = "Event: " + event.getEventName() + "\n" +
                "Date: " + event.getEventDate() + "\n" +
                "Location: " + event.getEventLocation() + "\n" +
                "Description: " + event.getEventDescription();

        // Create an AlertDialog to show event details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Event Details");
        builder.setMessage(eventDetails);

        // Add a "Close" button to dismiss the dialog
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        builder.create().show();
    }

    private void back() {
        // Pass the attendeeEmail correctly when navigating back to AttendeePage
        Intent intent = new Intent(AttendeeEvents.this, AttendeePage.class);
        intent.putExtra("userName", attendeeEmail);
        startActivity(intent);
    }
}
