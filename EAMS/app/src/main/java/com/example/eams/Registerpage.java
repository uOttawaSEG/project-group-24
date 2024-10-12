package com.example.eams;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
        roleName="Attendee";


    }

    private void setClickListeners() {
        create.setOnClickListener(v -> createUser());
        role.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                roleName = "Organizer";
            } else {
                roleName = "Attendee";
            }
        });
    }

    private void createUser() {
        System.out.println("this ois the role" + roleName);

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

        }
        else if(!isValidPhoneNumber(phoneInput)){
            Toast.makeText(this, "Please enter a phone number in this format: 0123456789", Toast.LENGTH_SHORT).show();
        }
        else if(!passwordInput.equals(confirmPassInput)){
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
        }
        else if(!isEmailValid(emailInput)){
            Toast.makeText(this, "Please put a valid email", Toast.LENGTH_SHORT).show();
        }



    }

    public boolean isEmailValid(String email){
        //code from https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
        String regex = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {

        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
