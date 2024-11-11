package com.example.eams;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class AttendeeList extends AppCompatActivity {

    private ListView attendeesListView;
    private DatabaseHelperForEvent dbHelper;
    private int eventId; // Change to int type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge support for the app
        setContentView(R.layout.activity_attendee_list);

        // Get the event ID passed from the previous activity (now as int)
        eventId = getIntent().getIntExtra("eventId", -1);  // Default to -1 if not found
        if (eventId == -1) {
            Toast.makeText(this, "Error: Event ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelperForEvent(this);
        attendeesListView = findViewById(R.id.attendeesListView);

        // Adjust the view padding to avoid overlap with system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadAttendees();
    }

    private void loadAttendees() {
        // Fetch the event details by event ID
        Event event = dbHelper.getEventById(eventId);
        if (event != null) {
            List<String> attendees = event.getAttendees(); // Get the list of attendees
            if (attendees.isEmpty()) {
                attendees.add("No attendees yet");
            }

            // Set the attendees to the ListView
            ArrayAdapter<String> attendeesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendees);
            attendeesListView.setAdapter(attendeesAdapter);
        } else {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
        }
    }
}
