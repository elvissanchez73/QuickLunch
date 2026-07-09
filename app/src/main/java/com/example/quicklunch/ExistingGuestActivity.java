package com.example.quicklunch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ExistingGuestActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;  // Input fields for email and password
    private FirebaseAuth mAuth;  // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_guest);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInputLoginGuest);
        passwordInput = findViewById(R.id.passwordInputLoginGuest);
        TextView loginTextView = findViewById(R.id.LoginButtonGuest);  // Change to TextView
        TextView forgotPasswordTextView = findViewById(R.id.ForgotGuest);  // Change to TextView

        // Login functionality when the login TextView is clicked
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (validateInputs(email, password)) {
                    loginGuest(email, password);
                }
            }
        });

        // Forgot password functionality
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingGuestActivity.this, ForgotPassword.class);
                startActivity(intent);  // Navigate to ForgotPassword activity
            }
        });
    }

    // Validate user input
    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Please enter your password");
            return false;
        }
        return true;
    }

    // Login using Firebase Authentication
    private void loginGuest(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful, navigate to MainGuestActivity
                        Toast.makeText(ExistingGuestActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ExistingGuestActivity.this, MainGuestActivity.class);
                        intent.putExtra("GUEST_EMAIL", email);
                        startActivity(intent);
                    } else {
                        // Login failed
                        Toast.makeText(ExistingGuestActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
