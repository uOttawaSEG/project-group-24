package com.example.eams;

import static org.junit.Assert.*;
import android.content.Context;

import org.junit.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

public class UnitTests {
    Context context;

    //Test #1 Even creation
    @Test
    public void testEventCreation() {
        Event event = new Event(1,
                "Team Meeting",
                "Monthly team sync-up meeting",
                "2024-12-01",
                "10:00",
                "12:00",
                "Conference Room 1",
                true,
                "John Doe");

        assertEquals(1, event.getEventId());
        assertEquals("Team Meeting", event.getEventName());
        assertEquals("Monthly team sync-up meeting", event.getEventDescription());
        assertEquals("2024-12-01", event.getEventDate());
        assertEquals("10:00", event.getStartTime());
        assertEquals("12:00", event.getEndTime());
        assertEquals("Conference Room 1", event.getEventLocation());
        assertTrue(event.isRequiresApproval());
        assertEquals("John Doe", event.getEventOrganizer());
        assertTrue(event.getAttendees().isEmpty());
    }

    //Test #2 Attendee Info from Db
    @Test
    public void testAttendeeInfo() {
        UserRegistration attendee = new UserRegistration(
                "Alice",
                "Smith",
                "alice.smith@example.com",
                "password123",
                "1234567890",
                "123 Street",
                "attendee"
        );

        assertEquals("Alice", attendee.getFirstName());
        assertEquals("Smith", attendee.getLastName());
        assertEquals("alice.smith@example.com", attendee.getEmail());
        assertEquals("password123", attendee.getPassword());
        assertEquals("1234567890", attendee.getPhoneNumber());
        assertEquals("123 Street", attendee.getAddress());
        assertEquals("attendee", attendee.getRole());
    }

    //Test #3 organizer Info
    @Test
    public void testOrganizerInfo() {
        UserRegistration organizer = new UserRegistration(
                "John",
                "Doe",
                "john.doe@example.com",
                "password123",
                "0987654321",
                "789 Boulevard",
                "organizer"
        );

        assertEquals("John", organizer.getFirstName());
        assertEquals("Doe", organizer.getLastName());
        assertEquals("john.doe@example.com", organizer.getEmail());
        assertEquals("password123", organizer.getPassword());
        assertEquals("0987654321", organizer.getPhoneNumber());
        assertEquals("789 Boulevard", organizer.getAddress());
        assertEquals("organizer", organizer.getRole());
    }

    //Test #4
    @Test
    public void testInvalidEmail() {
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        String email = "invalid-email";
        String password = "password456";
        String phone = "0987654321";
        String address = "456 Elm St";
        String role = "Organizer";

        // Act
        UserRegistration user = new UserRegistration(firstName, lastName, email, password, phone, address, role);

        // Assert
        assertEquals(email, user.getEmail()); // We can only assert what was set
        assertFalse(user.getEmail().contains("@")); // A simple way to assert an invalid email
    }
}
