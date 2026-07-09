package com.example.quicklunch;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.google.firebase.firestore.DocumentSnapshot;
import android.content.DialogInterface;
import com.google.firebase.firestore.QuerySnapshot;
import android.util.Log;

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

public class MainHostActivity extends AppCompatActivity {

    private TextView hostCodeTextView;
    private TextView totalStudentCountTextView;
    private EditText menuInput;
    private Button sendMenuButton;
    private Button loadButton; // New Load Button
    private ImageButton settingsButton;
    private TextView groupDetailsTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ListenerRegistration hostListener;
    private String hostCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_host);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Retrieve the host code from the Intent
        hostCode = getIntent().getStringExtra("HOST_CODE");

        // Initialize UI elements
        hostCodeTextView = findViewById(R.id.hostCodeTextView);
        totalStudentCountTextView = findViewById(R.id.totalStudentCountTextView);
        menuInput = findViewById(R.id.editTextMenu);
        sendMenuButton = findViewById(R.id.sendMenuButton);
        loadButton = findViewById(R.id.loadInfoButton); // Initialize Load Button
        settingsButton = findViewById(R.id.settingsButton);
        groupDetailsTextView = findViewById(R.id.groupDetailsTextView);

        // Display the host code
        if (hostCode != null && !hostCode.isEmpty()) {
            hostCodeTextView.setText("Host Code: " + hostCode);
            createHostDocumentIfNotExists(hostCode); // Ensure document exists
            startHostListener(hostCode);
        } else {
            Toast.makeText(this, "Host code not found!", Toast.LENGTH_SHORT).show();
        }

        // Send menu functionality
        sendMenuButton.setOnClickListener(v -> {
            String menu = menuInput.getText().toString().trim();
            if (!menu.isEmpty()) {
                sendMenu(hostCode, menu);
            } else {
                Toast.makeText(MainHostActivity.this, "Please enter a menu to send", Toast.LENGTH_SHORT).show();
            }
        });

        // Load Button - Manually fetch groups
        loadButton.setOnClickListener(v -> loadGroupsData());

        // Settings button to open options dialog
        settingsButton.setOnClickListener(v -> openSettingsDialog());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hostListener != null) {
            hostListener.remove();
        }
    }

    private void startHostListener(String hostCode) {
        if (hostCode == null || hostCode.isEmpty()) {
            Toast.makeText(this, "Host code is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        hostListener = db.collection("hosts")
                .whereEqualTo("hostCode", hostCode)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error listening for host updates", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot hostDocument = querySnapshot.getDocuments().get(0);
                        Long numberStudentsLong = hostDocument.getLong("numberStudents");

                        if (numberStudentsLong != null) {
                            int totalStudents = numberStudentsLong.intValue();
                            totalStudentCountTextView.setText("Total Students: " + totalStudents);
                        } else {
                            totalStudentCountTextView.setText("Total Students: Not Available");
                        }

                        loadGroupsData();
                    }
                });
    }

    private void createHostDocumentIfNotExists(String hostCode) {
        db.collection("hosts").whereEqualTo("hostCode", hostCode)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        Map<String, Object> hostData = new HashMap<>();
                        hostData.put("hostCode", hostCode);
                        hostData.put("numberStudents", 0);
                        hostData.put("groups", new HashMap<>());

                        db.collection("hosts").document(hostCode)
                                .set(hostData)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Host document created successfully"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error creating host document", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error checking host document", e));
    }

    private void loadGroupsData() {
        if (hostCode == null || hostCode.isEmpty()) {
            Toast.makeText(this, "Host code is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("guests")
                .whereEqualTo("hostCode", hostCode) // Retrieve all guests under this host
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        StringBuilder groupDetails = new StringBuilder();
                        int totalStudents = 0;

                        for (DocumentSnapshot guestDocument : querySnapshot.getDocuments()) {
                            String groupName = guestDocument.getString("lastSubmittedGroup"); // Get the group name
                            Long studentCountLong = guestDocument.getLong("lastSubmittedNumber");

                            if (groupName != null && studentCountLong != null) {
                                int studentCount = studentCountLong.intValue();
                                groupDetails.append(groupName).append(" (").append(studentCount).append(" students)\n");
                                totalStudents += studentCount;
                            }
                        }

                        if (groupDetails.length() > 0) {
                            groupDetailsTextView.setText(groupDetails.toString());
                            totalStudentCountTextView.setText("Total Students: " + totalStudents);
                        } else {
                            groupDetailsTextView.setText("No groups available.");
                            totalStudentCountTextView.setText("Total Students: 0");
                        }
                    } else {
                        groupDetailsTextView.setText("No guests found.");
                        totalStudentCountTextView.setText("Total Students: 0");
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching guests: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void sendMenu(String hostCode, String menu) {
        if (hostCode == null || hostCode.isEmpty()) {
            Toast.makeText(this, "Host code is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("hosts").document(hostCode)
                .update("menu", menu)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Menu sent successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error sending menu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Host Settings")
                .setItems(new String[]{"Change Password", "Log Out", "Copy Host Code"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            changePassword();
                            break;
                        case 1:
                            logout();
                            break;
                        case 2:
                            copyHostCode();
                            break;
                    }
                })
                .create()
                .show();
    }

    private void changePassword() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail())
                    .addOnSuccessListener(aVoid -> Toast.makeText(MainHostActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(MainHostActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show());
        }
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainHostActivity.this, SignUpActivity.class));
        finish();
    }

    private void copyHostCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Host Code", hostCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(MainHostActivity.this, "Copied: " + hostCode, Toast.LENGTH_SHORT).show();
    }
}
