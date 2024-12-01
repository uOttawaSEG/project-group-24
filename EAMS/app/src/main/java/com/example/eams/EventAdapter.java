package com.example.eams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;
    private DatabaseHelperForEvent dbHelper;
    private String attendeeEmail;

    public EventAdapter(Context context, List<Event> events, DatabaseHelperForEvent dbHelper, String attendeeEmail) {
        super(context, R.layout.event_list_item, events);
        this.context = context;
        this.events = events;
        this.dbHelper = dbHelper;
        this.attendeeEmail = attendeeEmail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout if not already done
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.event_list_item, parent, false);
        }

        // Get the event at the current position
        Event event = events.get(position);

        // Get references to the views in the layout
        TextView eventNameTextView = convertView.findViewById(R.id.eventNameTextView);
        TextView attendeeStatusTextView = convertView.findViewById(R.id.attendeeStatusTextView);

        // Set the event name
        eventNameTextView.setText(event.getEventName());

        // Determine the attendee's status (e.g., "Approved" or "Pending")
        String status = dbHelper.getAttendeeStatusForEvent(event.getEventId(), attendeeEmail);
        attendeeStatusTextView.setText(status);

        return convertView;
    }
}
