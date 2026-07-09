package com.example.quicklunch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    // Constructor to initialize Firebase Authentication and Firestore references
    public FirebaseHelper() {
        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
    }

    // Method to get the Firebase Authentication instance
    public FirebaseAuth getAuth() {
        return auth;
    }

    // Method to get the Firestore instance
    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    // Method to get the current authenticated user's ID
    public String getCurrentUserId() {
        // Return the user's unique ID, or null if not authenticated
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }

    // Method to save user data to Firestore
    public void saveUserData(String name, String lastName, String email) {
        String userId = getCurrentUserId();

        // Check if the user is authenticated
        if (userId != null) {
            // Create a User object to save to Firestore
            User user = new User(name, lastName, email);

            // Save the user data to Firestore under the "users" collection
            getFirestore().collection("users").document(userId)
                    .set(user)  // Use set() to save the document with the user data
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successfully saved user data
                            System.out.println("User data saved successfully.");
                        } else {
                            // Error saving user data
                            System.out.println("Error saving user data: " + task.getException().getMessage());
                        }
                    });
        } else {
            // User is not authenticated
            System.out.println("User is not authenticated.");
        }
    }

    // Method to retrieve user data from Firestore
    public void getUserData() {
        String userId = getCurrentUserId();

        // Check if the user is authenticated
        if (userId != null) {
            // Retrieve the user document from the Firestore "users" collection
            getFirestore().collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successfully retrieved user data
                            if (task.getResult().exists()) {
                                String name = task.getResult().getString("name");
                                String lastName = task.getResult().getString("lastName");
                                String email = task.getResult().getString("email");
                                System.out.println("User Data: " + name + " " + lastName + " " + email);
                            } else {
                                System.out.println("User data does not exist.");
                            }
                        } else {
                            // Error retrieving user data
                            System.out.println("Error retrieving user data: " + task.getException().getMessage());
                        }
                    });
        } else {
            // User is not authenticated
            System.out.println("User is not authenticated.");
        }
    }
}
