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
            Collections.sort(signedUpEvents, new Comparator<Event>() {
                @Override
                public int compare(Event e1, Event e2) {
                    // Parse the event date and compare
                    String eventDate1 = e1.getEventDate() + " " + e1.getStartTime();
                    String eventDate2 = e2.getEventDate() + " " + e2.getStartTime();

                    // Compare in reverse order to show newest events first
                    return eventDate2.compareTo(eventDate1);
                }
            });

            // Set up an adapter to display the events
            ArrayAdapter<Event> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, signedUpEvents);
            attendeeEventsListView.setAdapter(adapter);

            // Set up long click listener to show event details
            attendeeEventsListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
                Event event = signedUpEvents.get(position);  // Get the event at the clicked position
                showEventDetails(event);  // Show the event details
                return true;  // Return true to indicate the event is handled
            });
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
