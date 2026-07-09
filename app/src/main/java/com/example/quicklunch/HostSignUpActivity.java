package com.example.quicklunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HostSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_sign_up);  // Set the layout for HostSignUpActivity

        Button signUpButton = findViewById(R.id.signUpButtonHost);
        Button loginButton = findViewById(R.id.LoginButtonHost);

        // Set OnClickListener for the Sign Up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to NewHostActivity (New host sign-up page)
                Intent intent = new Intent(HostSignUpActivity.this, NewHostActivity.class);
                startActivity(intent);  // Start the NewHostActivity
            }
        });

        // Set OnClickListener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to ExistingHostActivity (Host login page)
                Intent intent = new Intent(HostSignUpActivity.this, ExistingHostActivity.class);
                startActivity(intent);  // Start the ExistingHostActivity
                finish();
            }
        });
    }
}
