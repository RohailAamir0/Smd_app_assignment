package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.SessionManager;

public class Splash_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.imgSplash);
        logo.setAlpha(0f);
        logo.animate().alpha(1f).setDuration(2000);

        new Handler().postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(Splash_activity.this);
            if (sessionManager.isLoggedIn()) {
                // Skip onboarding and login → go directly to MainActivity
                startActivity(new Intent(Splash_activity.this, MainActivity.class));
            } else {
                startActivity(new Intent(Splash_activity.this, onboarding_activity.class));
            }
            finish();
        }, 3000);
    }
}