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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class NewHostActivity extends AppCompatActivity {

    // Declare Firebase Auth and Firestore
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // UI elements
    private EditText nameInput, lastNameInput, emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_host);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        nameInput = findViewById(R.id.nameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInputLoginHost);
        passwordInput = findViewById(R.id.passwordInputLoginHost);
        Button registerButton = findViewById(R.id.RegisterButton);

        // Set OnClickListener for the registerButton
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input data from UI elements
                String name = nameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(NewHostActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new user with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(NewHostActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // If sign-up is successful, get the current user
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    // Generate a 16-character host code
                                    String hostCode = generateHostCode();

                                    // Save user data in Firestore
                                    Host newHost = new Host(name, lastName, email, hostCode);
                                    db.collection("hosts")
                                            .document(user.getUid())  // Use UID as the document ID
                                            .set(newHost)
                                            .addOnSuccessListener(aVoid -> {
                                                // Data saved successfully, navigate to MainHostActivity
                                                Intent intent = new Intent(NewHostActivity.this, MainHostActivity.class);
                                                intent.putExtra("HOST_CODE", hostCode);
                                                startActivity(intent);
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle failure in saving data
                                                Toast.makeText(NewHostActivity.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                // If sign-up fails, show an error message
                                if (task.getException() != null) {
                                    Toast.makeText(NewHostActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Method to generate a random 16-character code
    private String generateHostCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16).toUpperCase();
    }

    // Host class to model the data you want to save
    public static class Host {
        private String name;
        private String lastName;
        private String email;
        private String hostCode;

        public Host(String name, String lastName, String email, String hostCode) {
            this.name = name;
            this.lastName = lastName;
            this.email = email;
            this.hostCode = hostCode;
        }

        // Getters and setters (optional)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getHostCode() {
            return hostCode;
        }

        public void setHostCode(String hostCode) {
            this.hostCode = hostCode;
        }
    }
}
