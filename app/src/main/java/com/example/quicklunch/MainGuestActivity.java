package com.example.quicklunch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainGuestActivity extends AppCompatActivity {

    private EditText studentCountInput, nameofGroup;
    private Button submitLunchNumberButton;
    private ImageButton settingsButton;
    private TextView groupNumberTextView, menuTextView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String guestUid, hostCode;
    private ListenerRegistration menuListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        guestUid = (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : null;

        // Initialize UI elements
        studentCountInput = findViewById(R.id.studentCount);
        nameofGroup = findViewById(R.id.groupName);
        submitLunchNumberButton = findViewById(R.id.submitLunchNumberButton);
        groupNumberTextView = findViewById(R.id.groupNumberTextView);
        menuTextView = findViewById(R.id.menuTextView);
        settingsButton = findViewById(R.id.settingsButton);

        // Set initial states
        groupNumberTextView.setText("Loading...");
        menuTextView.setText("Menu: Waiting for update...");

        // Load the group number
        loadGroupNumber();

        // Submit button functionality
        submitLunchNumberButton.setOnClickListener(v -> submitLunchData());

        // Settings button to open options dialog
        settingsButton.setOnClickListener(v -> openSettingsDialog());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (menuListener != null) menuListener.remove();
    }

    private void loadGroupNumber() {
        if (guestUid != null) {
            db.collection("guests").document(guestUid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            hostCode = documentSnapshot.getString("hostCode");
                            if (hostCode != null) {
                                groupNumberTextView.setText("Group Number: " + hostCode);
                                startMenuListener(hostCode);
                            } else {
                                groupNumberTextView.setText("Host code not found");
                            }
                        } else {
                            groupNumberTextView.setText("Group Number: Not Found");
                        }
                    })
                    .addOnFailureListener(e -> showToast("Error loading group number"));
        }
    }

    private void startMenuListener(String hostCode) {
        menuListener = db.collection("hosts")
                .whereEqualTo("hostCode", hostCode)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        showToast("Error listening for menu updates");
                        return;
                    }

                    if (snapshots != null && !snapshots.isEmpty()) {
                        String menu = snapshots.getDocuments().get(0).getString("menu");
                        menuTextView.setText("Menu: " + (menu != null ? menu : "Not Available"));
                    }
                });
    }

    private void submitLunchData() {
        String studentCount = studentCountInput.getText().toString().trim();
        String groupName = nameofGroup.getText().toString().trim();

        if (studentCount.isEmpty() || groupName.isEmpty()) {
            showToast("Please enter a valid number of students and a valid group name");
            return;
        }

        int numberOfStudents = Integer.parseInt(studentCount);
        submitLunchNumber(numberOfStudents, groupName);
    }

    private void submitLunchNumber(int numberOfStudents, String groupName) {
        if (guestUid == null) {
            showToast("Error: User not authenticated");
            return;
        }

        Map<String, Object> guestUpdate = new HashMap<>();
        guestUpdate.put("lastSubmittedGroup", groupName);
        guestUpdate.put("lastSubmittedNumber", numberOfStudents);

        db.collection("guests").document(guestUid)
                .set(guestUpdate, SetOptions.merge())
                .addOnSuccessListener(aVoid -> showToast("Lunch number saved successfully!"))
                .addOnFailureListener(e -> showToast("Error saving lunch number"));
    }

    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings")
                .setItems(new String[]{"Change Password", "Log Out", "Change Host Code"}, (dialog, which) -> {
                    switch (which) {
                        case 0: changePassword(); break;
                        case 1: logout(); break;
                        case 2: changeHostCode(); break;
                    }
                })
                .create().show();
    }

    private void changePassword() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail())
                    .addOnSuccessListener(aVoid -> showToast("Password reset email sent"))
                    .addOnFailureListener(e -> showToast("Failed to send password reset email"));
        }
    }

    private void logout() {
        mAuth.signOut();
        showToast("Logged out successfully");
        startActivity(new Intent(MainGuestActivity.this, SignUpActivity.class));
        finish();
    }

    private void changeHostCode() {
        EditText inputHostCode = new EditText(this);
        inputHostCode.setHint("Enter new Host Code");

        new AlertDialog.Builder(this)
                .setTitle("Change Host Code")
                .setView(inputHostCode)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newHostCode = inputHostCode.getText().toString().trim();
                    if (!newHostCode.isEmpty()) updateHostCodeInFirestore(newHostCode);
                    else showToast("Please enter a valid host code");
                })
                .setNegativeButton("Cancel", null)
                .create().show();
    }

    private void updateHostCodeInFirestore(String newHostCode) {
        if (guestUid != null) {
            db.collection("guests").document(guestUid)
                    .set(Map.of("hostCode", newHostCode), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        hostCode = newHostCode;
                        groupNumberTextView.setText("Group Number: " + hostCode);
                        showToast("Host code updated successfully");
                    })
                    .addOnFailureListener(e -> showToast("Error updating host code"));
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
