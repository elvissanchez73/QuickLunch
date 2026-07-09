package com.example.quicklunch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewGuestActivity extends AppCompatActivity {

    private EditText nameInput, lastNameInput, emailInput, passwordInput, hostCodeInput;
    private FirebaseAuth mAuth;  // Firebase Authentication
    private FirebaseFirestore db;  // Firestore Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guest);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.NameInput);
        lastNameInput = findViewById(R.id.LastNameInput);
        emailInput = findViewById(R.id.EmailInput);
        passwordInput = findViewById(R.id.PasswordInput);
        hostCodeInput = findViewById(R.id.HostCodeInput);
        Button registerButton = findViewById(R.id.RegisterButton);

        // Set up button functionality
        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String hostCode = hostCodeInput.getText().toString().trim();

            if (validateInputs(name, lastName, email, password, hostCode)) {
                checkHostCodeAndRegister(name, lastName, email, password, hostCode);
            }
        });
    }

    // Validate input fields
    private boolean validateInputs(String name, String lastName, String email, String password, String hostCode) {
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Please enter your name");
            return false;
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameInput.setError("Please enter your last name");
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return false;
        }
        if (TextUtils.isEmpty(hostCode) || hostCode.length() != 16) {
            hostCodeInput.setError("Host code must be 16 characters");
            return false;
        }
        return true;
    }

    // Check if the host code exists in the Firestore database
    private void checkHostCodeAndRegister(String name, String lastName, String email, String password, String hostCode) {
        db.collection("hosts")
                .whereEqualTo("hostCode", hostCode)  // Assuming hosts are stored with a 'hostCode' field
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Host code exists, proceed with registration
                            Toast.makeText(NewGuestActivity.this, "Host code verified", Toast.LENGTH_SHORT).show();
                            registerGuest(name, lastName, email, password, hostCode);
                        } else {
                            // Host code does not exist
                            Toast.makeText(NewGuestActivity.this, "Invalid host code", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NewGuestActivity.this, "Error checking host code: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NewGuestActivity.this, "Error checking host code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Register new guest
    private void registerGuest(String name, String lastName, String email, String password, String hostCode) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            saveGuestToFirestore(uid, name, lastName, email, hostCode);
                        }
                    } else {
                        Toast.makeText(NewGuestActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Save guest data to Firestore
    private void saveGuestToFirestore(String uid, String name, String lastName, String email, String hostCode) {
        // Count the number of guests already associated with this host
        db.collection("guests")
                .whereEqualTo("hostCode", hostCode)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the number of guests already in the group
                        int groupNumber = task.getResult().size() + 1; // Increment by 1 to get the next group number

                        // Save guest data with the calculated group number
                        Map<String, Object> guestData = new HashMap<>();
                        guestData.put("name", name);
                        guestData.put("lastName", lastName);
                        guestData.put("email", email);
                        guestData.put("hostCode", hostCode);
                        guestData.put("groupNumber", String.valueOf(groupNumber));  // Assign the group number here

                        db.collection("guests").document(uid)
                                .set(guestData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(NewGuestActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                                    // Navigate to MainGuestActivity after saving data
                                    Intent intent = new Intent(NewGuestActivity.this, MainGuestActivity.class);
                                    intent.putExtra("GUEST_NAME", name);
                                    intent.putExtra("GUEST_EMAIL", email);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(NewGuestActivity.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(NewGuestActivity.this, "Error fetching guests", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NewGuestActivity.this, "Error fetching guests", Toast.LENGTH_SHORT).show();
                });
    }
}
