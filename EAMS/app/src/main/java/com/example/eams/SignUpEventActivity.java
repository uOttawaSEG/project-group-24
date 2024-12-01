package com.example.eams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class SignUpEventActivity extends AppCompatActivity {
    private Button backButton;
    private ListView eventsListView;
    private SearchView searchView;
    private DatabaseHelperForEvent dbHelper;
    private String attendeeEmail;
    private List<Event> availableEvents;
    private List<Event> filteredEvents; // List to hold filtered events

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_event);

        // Get attendee email from intent
        attendeeEmail = getIntent().getStringExtra("attendeeEmail");
        if (attendeeEmail == null) {
            Toast.makeText(this, "Error: Attendee email not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelperForEvent(this);
        initializeViews();
        setClickListeners();
        loadEvents();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        eventsListView = findViewById(R.id.eventsListView);
        searchView = findViewById(R.id.searchView);
        filteredEvents = new ArrayList<>(); // Initialize the filteredEvents list
    }

    private void setClickListeners() {
        backButton.setOnClickListener(v -> finish());

        // Listen for search query text changes
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterEvents(query); // Filter events when user submits a query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEvents(newText); // Filter events as user types
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadEvents() {
        // Get all events from the database
        availableEvents = dbHelper.getAllEvents();

        // Get today's date
        LocalDate today = LocalDate.now();

        // Get the list of events the attendee is already signed up for
        List<Event> signedUpEvents = dbHelper.getEventsForAttendee(attendeeEmail);

        // Filter out events that are:
        // - in the past
        // - the user is already signed up for
        // - conflicting with events the user is already signed up for
        availableEvents.removeIf(event -> {
            // Convert eventDate string to LocalDate (assuming the format is "yyyy-MM-dd")
            LocalDate eventDate = LocalDate.parse(event.getEventDate(), DateTimeFormatter.ISO_DATE);

            // Check for conflicts with signed-up events
            boolean isConflicting = signedUpEvents.stream().anyMatch(signedUpEvent -> {
                // Compare date
                if (!signedUpEvent.getEventDate().equals(event.getEventDate())) {
                    return false; // Different dates are not conflicts
                }

                // Compare time (assuming times are in "HH:mm" 24-hour format)
                return timesOverlap(
                        signedUpEvent.getStartTime(), signedUpEvent.getEndTime(),
                        event.getStartTime(), event.getEndTime()
                );
            });

            // Exclude past events, already signed-up events, and conflicting events
            return eventDate.isBefore(today) ||
                    dbHelper.isAttendeeRegistered(event.getEventId(), attendeeEmail) ||
                    isConflicting;
        });

        // Sort the events by date in descending order (newest first)
        availableEvents.sort((event1, event2) -> {
            LocalDate eventDate1 = LocalDate.parse(event1.getEventDate(), DateTimeFormatter.ISO_DATE);
            LocalDate eventDate2 = LocalDate.parse(event2.getEventDate(), DateTimeFormatter.ISO_DATE);
            return eventDate2.compareTo(eventDate1); // Descending order
        });

        // Clear the filtered list before repopulating
        filteredEvents.clear();

        // Add all remaining available events to filteredEvents
        filteredEvents.addAll(availableEvents);

        // Create adapter for the ListView using filteredEvents
        ArrayAdapter<Event> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, filteredEvents);
        eventsListView.setAdapter(adapter);

        // Notify the adapter about data change
        adapter.notifyDataSetChanged();

        // Set click listener for event selection
        eventsListView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = filteredEvents.get(position);
            showSignUpDialog(selectedEvent);
        });
    }

    // Helper method to check if two time ranges overlap
    private boolean timesOverlap(String start1, String end1, String start2, String end2) {
        // Parse the times into minutes (assuming format is "HH:mm")
        int start1Minutes = parseTimeToMinutes(start1);
        int end1Minutes = parseTimeToMinutes(end1);
        int start2Minutes = parseTimeToMinutes(start2);
        int end2Minutes = parseTimeToMinutes(end2);

        // Check for overlap
        return (start1Minutes < end2Minutes && end1Minutes > start2Minutes);
    }

    // Helper method to parse time in "HH:mm" format into minutes
    private int parseTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }



    private void filterEvents(String keyword) {
        filteredEvents.clear();
        if (keyword.isEmpty()) {
            filteredEvents.addAll(availableEvents); // Show all if no keyword
        } else {
            for (Event event : availableEvents) {
                // Check if title or description contains the keyword (case insensitive)
                if (event.getEventName().toLowerCase().contains(keyword.toLowerCase()) ||
                        event.getEventDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredEvents.add(event);
                }
            }
        }

        // Notify the adapter about data chang
        ((ArrayAdapter) eventsListView.getAdapter()).notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showSignUpDialog(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Up for Event")
                .setMessage("Would you like to sign up for " + event.getEventName() + "?")
                .setPositiveButton("Sign Up", (dialog, id) -> {
                    signUpForEvent(event);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void signUpForEvent(Event event) {
        // First check if already registered
        if (dbHelper.isAttendeeRegistered(event.getEventId(), attendeeEmail)) {
            Toast.makeText(this, "You are already registered for this event", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the event requires approval
        if (event.isRequiresApproval()) {
            // Add to pending approvals
            boolean success = dbHelper.addEventAttendee(event.getEventId(), attendeeEmail, "pending");
            if (success) {
                Toast.makeText(this, "Sign-up request sent for approval", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to sign up for event", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Direct sign-up
            boolean success = dbHelper.addEventAttendee(event.getEventId(), attendeeEmail, "approved");
            if (success) {
                Toast.makeText(this, "Successfully signed up for event", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to sign up for event", Toast.LENGTH_SHORT).show();
            }
        }
        loadEvents();
    }
}
