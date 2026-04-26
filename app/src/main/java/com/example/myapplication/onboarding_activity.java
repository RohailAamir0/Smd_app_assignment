package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.SessionManager;

public class onboarding_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        Button btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            // Navigate to LoginActivity (not MainActivity directly)
            Intent intent = new Intent(onboarding_activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
