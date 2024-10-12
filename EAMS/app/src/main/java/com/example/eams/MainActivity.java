package com.example.eams;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //UI component
    private EditText username, password;
    private Button register, login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setClickListeners();
    }
    private void initializeViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.Register);
        login = findViewById(R.id.submitButton);

    }

    private void setClickListeners() {
        register.setOnClickListener(v -> RegisterUser());
        login.setOnClickListener(v -> LoginUser());

    }

    private void LoginUser() {
        System.out.println("hello");
    }

    private void RegisterUser() {
        
    }


}