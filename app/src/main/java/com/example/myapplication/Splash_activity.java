package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.imgSplash);
        logo.setAlpha(0f);
        logo.animate().alpha(1f).setDuration(2000);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash_activity.this, onboarding_activity.class));
            finish();
        }, 5000);
    }
}