package com.example.eams;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
public class Registerpage extends AppCompatActivity {
    private EditText firstname, lastname, email, password, phone, address;
    private Button create;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);

    }

    private void setClickListeners() {
        create.setOnClickListener(v -> createUser());


    }

    private void createUser() {

        //add to database
    }
    private void validateEmail() {
        EditText editTextEmail = findViewById(R.id.email); // Ensure you have the correct ID
        String email = editTextEmail.getText().toString().trim();

        // Check if the email contains '@' and has something before and after it
        if (email.contains("@")) {
            String[] parts = email.split("@");
            if (parts.length == 2 && !parts[0].isEmpty() && !parts[1].isEmpty()) {
                // Valid email
                Toast.makeText(this, "Email is valid", Toast.LENGTH_SHORT).show();
            } else {
                // Invalid email (empty part before or after @)
                Toast.makeText(this, "Invalid email. Please check the format.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Invalid email (does not contain @)
            Toast.makeText(this, "Invalid email. Please include '@'", Toast.LENGTH_SHORT).show();
        }
    }

}