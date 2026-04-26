package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // If already logged in, skip to MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegisterLink = findViewById(R.id.tvRegisterLink);

        // Style the "Register" part in blue
        String fullText = "Don't have an account? Register";
        SpannableString spannable = new SpannableString(fullText);
        spannable.setSpan(new ForegroundColorSpan(0xFF2196F3),
                fullText.indexOf("Register"), fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegisterLink.setText(spannable);

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        findViewById(R.id.btnLoginBack).setOnClickListener(v -> finish());
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        // Save session if Remember Me is checked
                        if (cbRememberMe.isChecked()) {
                            sessionManager.saveSession(uid, email);
                            sessionManager.setRememberMe(true);
                        } else {
                            // Save session temporarily (cleared on logout)
                            sessionManager.saveSession(uid, email);
                            sessionManager.setRememberMe(false);
                        }
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
