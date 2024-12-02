package com.mah.tictactoe;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Set a delay for the splash screen (e.g., 3 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the LoginActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the SplashActivity so the user cannot return to it
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}