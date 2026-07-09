package com.example.quicklunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailInput;
    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInputForgotPassword);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();

                if (!email.isEmpty()) {
                    sendResetEmail(email); // Send the reset email
                } else {
                    Toast.makeText(ForgotPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendResetEmail(String email) {
        // Use Firebase to send a password reset email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Reset password email sent to " + email, Toast.LENGTH_SHORT).show();
                        // Redirect to SignUpActivity
                        startActivity(new Intent(ForgotPassword.this, SignUpActivity.class));
                        finish();
                    } else {
                        // Handle failure (e.g., invalid email, no account exists)
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Failed to send email";
                        Toast.makeText(ForgotPassword.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
