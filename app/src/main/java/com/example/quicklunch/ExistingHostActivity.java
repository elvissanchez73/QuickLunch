package com.example.quicklunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class ExistingHostActivity extends AppCompatActivity {

    private EditText emailInput;    // For email input
    private EditText passwordInput; // For password input
    private FirebaseAuth auth;      // FirebaseAuth instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_host);

        // Initialize views
        emailInput = findViewById(R.id.emailInputLoginHost);
        passwordInput = findViewById(R.id.passwordInputLoginHost);
        Button loginButton = findViewById(R.id.LoginButton);
        TextView forgotPasswordText = findViewById(R.id.ForgotHost);  // Updated to TextView

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance();

        // Set OnClickListener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Validate the input fields
                if (!email.isEmpty() && !password.isEmpty()) {
                    // Attempt to log in with the provided credentials
                    loginUser(email, password);
                } else {
                    // Handle empty input case (optional: show a Toast or Snackbar)
                    if (email.isEmpty()) {
                        emailInput.setError("Please enter your email");
                    }
                    if (password.isEmpty()) {
                        passwordInput.setError("Please enter your password");
                    }
                }
            }
        });

        // Set OnClickListener for the forgot password text
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingHostActivity.this, ForgotPassword.class);
                startActivity(intent); // Start the ForgotPasswordHost activity
            }
        });
    }

    // Method to log in the user
    private void loginUser(String email, String password) {
        // Sign in with Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If login is successful, get the user ID
                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null) {
                            String userId = user.getUid(); // Get the logged-in user's UID
                            // Retrieve the hostCode from Firestore
                            retrieveHostCode(userId);
                        }
                    } else {
                        // If login fails, show an error message
                        Toast.makeText(ExistingHostActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to retrieve hostCode from Firestore
    private void retrieveHostCode(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference hostRef = db.collection("hosts").document(userId);

        // Get the document from Firestore
        hostRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String hostCode = document.getString("hostCode");
                    if (hostCode != null) {
                        // Successfully retrieved hostCode
                        Intent intent = new Intent(ExistingHostActivity.this, MainHostActivity.class);
                        intent.putExtra("HOST_CODE", hostCode);
                        startActivity(intent);  // Start the MainHostActivity
                    } else {
                        // Host code is missing
                        Toast.makeText(ExistingHostActivity.this, "Host code not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Document doesn't exist in Firestore
                    Toast.makeText(ExistingHostActivity.this, "Host not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error retrieving document
                Toast.makeText(ExistingHostActivity.this, "Error retrieving host code", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
