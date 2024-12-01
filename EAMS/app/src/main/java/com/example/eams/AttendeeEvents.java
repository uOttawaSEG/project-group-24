package com.example.eams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;
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

        // Get attendee email from intent (pass this when navigating to this activity)
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
        backButton = findViewById(R.id.backButton); // Initialize back button
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

            // Use the custom EventAdapter to display the events with status
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
                    deleteEventFromList(event, signedUpEvents);
                    break;
            }
        });

        builder.create().show();
    }

    private void deleteEventFromList(Event event, List<Event> signedUpEvents) {
        boolean isDeleted = dbHelper.removeEventAttendee(event.getEventId(), attendeeEmail);

        if (isDeleted) {
            // Remove the event from the list and update the adapter
            signedUpEvents.remove(event);
            ((ArrayAdapter) attendeeEventsListView.getAdapter()).notifyDataSetChanged();

            Toast.makeText(this, "Event removed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to remove event", Toast.LENGTH_SHORT).show();
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
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // Close the dialog
            }
        });

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
