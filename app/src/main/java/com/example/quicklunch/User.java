package com.example.quicklunch;

public class User {
    private String name;
    private String lastName;
    private String email;

    // Default constructor for Firebase (required)
    public User() {}

    // Constructor to initialize the User object with the fields
    public User(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters for name, lastName, and email

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
}
