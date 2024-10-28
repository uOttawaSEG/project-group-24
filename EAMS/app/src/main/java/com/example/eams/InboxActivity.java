package com.example.eams;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InboxActivity extends AppCompatActivity {
    private Button reject, accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox); // Ensure this layout exists in res/layout
    }

}
