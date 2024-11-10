package com.example.eams;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {
    private EditText titleInput, descriptionInput, addressInput;
    private Button dateButton, startTimeButton, endTimeButton, createButton, backButton;
    private Switch approvalSwitch;
    private Calendar selectedDate = Calendar.getInstance();
    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private DatabaseHelperForEvent databaseHelper;
    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Get organizer ID from intent
        organizerId = getIntent().getStringExtra("organizerId");
        if (organizerId == null) {
            Toast.makeText(this, "Error: Organizer ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseHelper = new DatabaseHelperForEvent(this);
        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        titleInput = findViewById(R.id.eventTitle);
        descriptionInput = findViewById(R.id.eventDescription);
        addressInput = findViewById(R.id.eventAddress);
        dateButton = findViewById(R.id.dateButton);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        createButton = findViewById(R.id.createButton);
        backButton = findViewById(R.id.backButton);
        approvalSwitch = findViewById(R.id.approvalSwitch);
    }

    private void setClickListeners() {
        dateButton.setOnClickListener(v -> showDatePicker());
        startTimeButton.setOnClickListener(v -> showTimePicker(true));
        endTimeButton.setOnClickListener(v -> showTimePicker(false));
        createButton.setOnClickListener(v -> createEvent());
        backButton.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar current = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    updateDateButton();
                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker(boolean isStartTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    // Round to nearest 30 minutes
                    minute = (minute / 30) * 30;

                    Calendar time = isStartTime ? startTime : endTime;
                    time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    time.set(Calendar.MINUTE, minute);

                    if (isStartTime && time.before(Calendar.getInstance())) {
                        // If start time is before the current time, show a toast message
                        Toast.makeText(CreateEventActivity.this, "Start time cannot be in the past", Toast.LENGTH_SHORT).show();
                    } else {
                        updateTimeButton(isStartTime);
                    }
                },
                12, 0, false
        );
        timePickerDialog.show();
    }

    private void updateDateButton() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        dateButton.setText(dateFormat.format(selectedDate.getTime()));
    }

    private void updateTimeButton(boolean isStartTime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        if (isStartTime) {
            startTimeButton.setText(timeFormat.format(startTime.getTime()));
        } else {
            endTimeButton.setText(timeFormat.format(endTime.getTime()));
        }
    }

    private void createEvent() {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        boolean requiresApproval = approvalSwitch.isChecked();

        // Validation
        if (title.isEmpty() || description.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dateButton.getText().toString().equals("Select Date")) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startTimeButton.getText().toString().equals("Start Time") ||
                endTimeButton.getText().toString().equals("End Time")) {
            Toast.makeText(this, "Please select both start and end times", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endTime.before(startTime)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format date and times
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String dateStr = dateFormat.format(selectedDate.getTime());
        String startTimeStr = timeFormat.format(startTime.getTime());
        String endTimeStr = timeFormat.format(endTime.getTime());

        // Save to database with actual organizer ID
        boolean success = databaseHelper.addEvent(
                title,
                description,
                dateStr,
                startTimeStr,
                endTimeStr,
                address,
                requiresApproval,
                organizerId  // Use the actual organizer ID
        );

        if (success) {
            Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show();
        }
    }
}
