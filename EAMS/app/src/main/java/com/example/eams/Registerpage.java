package com.example.eams;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Registerpage extends AppCompatActivity {
    private EditText firstname, lastname, email, password, phone, address;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);

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
        create = findViewById(R.id.submitButton);
    }

    private void setClickListeners() {
        create.setOnClickListener(v -> createUser());
    }

    private void createUser() {
        // Add your database logic here
        Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show();
    }
}
