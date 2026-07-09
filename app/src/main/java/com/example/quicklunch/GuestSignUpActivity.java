package com.example.quicklunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GuestSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_sign_up);

        Button signUpButton = findViewById(R.id.signUpButtonGuest);
        Button loginButton = findViewById(R.id.LoginButtonGuest);

        // Set OnClickListener for the Sign Up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to NewHostActivity (New host sign-up page)
                Intent intent = new Intent(GuestSignUpActivity.this, NewGuestActivity.class);
                startActivity(intent);  // Start the NewHostActivity
                finish();
            }
        });

        // Set OnClickListener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to ExistingHostActivity (Host login page)
                Intent intent = new Intent(GuestSignUpActivity.this, ExistingGuestActivity.class);
                startActivity(intent);  // Start the ExistingHostActivity
                finish();
            }
        });
    }
}
