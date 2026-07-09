package com.example.quicklunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Button btnHost = findViewById(R.id.btnHost);
        Button btnGuest = findViewById(R.id.btnGuest);

        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Host Sign-Up
                startActivity(new Intent(SignUpActivity.this, HostSignUpActivity.class));
                finish();
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Guest Sign-Up
                startActivity(new Intent(SignUpActivity.this, GuestSignUpActivity.class));
                finish();
            }
        });
    }


}