package com.example.eams;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
    }
}
