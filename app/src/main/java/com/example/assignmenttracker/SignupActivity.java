package com.example.assignmenttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, password;
    Button btnSignup;
    TextView goToLogin;   // ✅ FIXED

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnSignup = findViewById(R.id.btnSignup);
        goToLogin = findViewById(R.id.goToLogin); // ✅ IMPORTANT

        // 🔘 Signup Button
        btnSignup.setOnClickListener(v -> {

            String n = name.getText().toString().trim();
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();

            if(n.isEmpty() || e.isEmpty() || p.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("name", n);
            editor.putString("email", e);
            editor.putString("password", p);
            editor.apply();

            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // 🔗 Go to Login
        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}