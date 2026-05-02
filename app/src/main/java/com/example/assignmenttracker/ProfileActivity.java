package com.example.assignmenttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView nameText, emailText;
    Button editProfile, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        editProfile = findViewById(R.id.editProfile);
        logout = findViewById(R.id.logout);

        // 📦 Load user data
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);

        String name = sp.getString("name", "User");
        String email = sp.getString("email", "No Email");

        nameText.setText(name);
        emailText.setText(email);

        // ✏️ Edit profile (simple redirect to signup)
        editProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

        // 🚪 Logout
        logout.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}