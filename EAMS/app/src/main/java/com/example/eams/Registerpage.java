package com.example.eams;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Registerpage extends AppCompatActivity {
    private EditText firstname, lastname, email, password, phone, address,confirmpass;
    private ToggleButton role;
    private String roleName;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);

        initializeViews();
        setClickListeners();
        role.setOnCheckedChangeListener((buttonView, is Checked)) ->{
            if (role.isChecked()){
                roleName="Organizer";
            }
            else{
                roleName="Attendee";
            }
        }
    }

    private void initializeViews() {
        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        confirmpass=findViewById(R.id.confirmpassword);
        role =findViewById(R.id.role1);
        create = findViewById(R.id.submitButton);
    }

    private void setClickListeners() {
        create.setOnClickListener(v -> createUser());
    }

    private void createUser() {
        System.out.println(role);
        String firstName = firstname.getText().toString().trim();
        String lastName = lastname.getText().toString().trim();
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();
        String confirmPassInput = confirmpass.getText().toString().trim();
        String phoneInput = phone.getText().toString().trim();
        String addressInput = address.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || emailInput.isEmpty() ||
                passwordInput.isEmpty() || phoneInput.isEmpty() || addressInput.isEmpty() ||
                confirmPassInput.isEmpty() || roleName==null)
        {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show();
    }
}
