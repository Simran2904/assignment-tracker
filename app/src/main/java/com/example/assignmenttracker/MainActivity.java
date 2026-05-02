package com.example.assignmenttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;
    TextView goToSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        goToSignup = findViewById(R.id.goToSignup);

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);

        btnLogin.setOnClickListener(v -> {

            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();

            // ✅ Validation
            if(e.isEmpty() || p.isEmpty()){
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String savedEmail = sp.getString("email", "");
            String savedPassword = sp.getString("password", "");

            if(e.equals(savedEmail) && p.equals(savedPassword)){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                // ✅ Move to Home
                startActivity(new Intent(this, DashboardActivity.class));

                // 🔥 Optional (prevents going back to login)
                finish();

            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        goToSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
}