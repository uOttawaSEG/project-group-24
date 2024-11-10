package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import java.util.List;

public class ViewEventActivity extends AppCompatActivity {
    private Button back;
    private ListView upcoming, past;
    private DatabaseHelperForEvent dbHelper;
    private String organizerEmail;
    private List<Event> upcomingEvents, pastEvents;

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

    private void loadEvents() {

    }


}
