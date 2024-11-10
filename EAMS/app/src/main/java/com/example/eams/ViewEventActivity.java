package com.example.eams;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewEventActivity extends AppCompatActivity {
    private Button back;
    private ListView upcoming, past;
    private DatabaseHelperForEvent dbHelper;
    private String organizerEmail;
    private List<Event> upcomingEvents, pastEvents;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        organizerEmail = getIntent().getStringExtra("organizerId");
        if (organizerEmail == null) {
            Toast.makeText(this, "Error: Organizer ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelperForEvent(this);
        initializeViews();
        setClickListeners();
        loadEvents();
    }

    private void initializeViews() {
        back = findViewById(R.id.backButton);
        upcoming = findViewById(R.id.upcomingEventsList);
        past = findViewById(R.id.pastEventsList);
    }

    private void setClickListeners() {
        back.setOnClickListener(v -> finish());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadEvents() {
        // Fetch all events from the database
        List<Event> allEvents = dbHelper.getAllEvents();

        // Separate events into upcoming and past based on the event's end time
        upcomingEvents = new ArrayList<>();
        pastEvents = new ArrayList<>();

        for (Event event : allEvents) {
            if (event.isPastEvent()) {
                pastEvents.add(event);
            } else {
                upcomingEvents.add(event);
            }
        }

        // Set upcoming events to the ListView
        ArrayAdapter<Event> upcomingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, upcomingEvents);
        upcoming.setAdapter(upcomingAdapter);

        // Set past events to the ListView
        ArrayAdapter<Event> pastAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pastEvents);
        past.setAdapter(pastAdapter);

        // Set long click listeners on ListView items to show options (Details, Delete, or Go Back)
        upcoming.setOnItemLongClickListener((adapterView, view, position, id) -> {
            Event event = upcomingEvents.get(position);
            showOptionsDialog(event);
            return true;
        });

        past.setOnItemLongClickListener((adapterView, view, position, id) -> {
            Event event = pastEvents.get(position);
            showOptionsDialog(event);
            return true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showOptionsDialog(final Event event) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("What would you like to do?")
                .setPositiveButton("Details", (dialog, id) -> showEventDetails(event))
                .setNegativeButton("Delete", (dialog, id) -> deleteEvent(event))
                .setNeutralButton("Go Back", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showEventDetails(Event event) {
        // Create a dialog to display the event details
        AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(this);
        detailsDialogBuilder.setTitle("Event Details");

        // Format the details to display in the dialog
        String detailsMessage = "Name: " + event.getEventName() + "\n" +
                "Start Time: " + event.getStartTime() + "\n" +
                "End Time: " + event.getEndTime() + "\n" +
                "Description: " + event.getEventDescription() + "\n" +
                "Location: " + event.getEventLocation() + "\n" +
                //"Organizer: " + event.getEventOrganizer() + "\n" + //fix later now give addresse
                "Requires Approval: " + (event.isRequiresApproval() ? "Yes" : "No") + "\n" +
                "Attendees: " + (event.getAttendees().isEmpty() ? "No attendees yet" : String.join(", ", event.getAttendees()));

        detailsDialogBuilder.setMessage(detailsMessage);
        detailsDialogBuilder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        detailsDialogBuilder.create().show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteEvent(Event event) {
        boolean isDeleted = dbHelper.deleteEvent(event.getEventId());
        if (isDeleted) {
            Toast.makeText(this, "Event deleted successfully!", Toast.LENGTH_SHORT).show();
            loadEvents();  // Refresh the events list
        } else {
            Toast.makeText(this, "Failed to delete event.", Toast.LENGTH_SHORT).show();
        }
    }
}
