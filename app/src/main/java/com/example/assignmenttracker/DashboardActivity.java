package com.example.assignmenttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    TextView welcomeText, totalTasks, completedTasks, pendingTasks, message;
    ProgressBar progressBar;
    Button goToAssignments, logout, profileBtn;

    SharedPreferences userSP, assignmentSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeText = findViewById(R.id.welcomeText);
        totalTasks = findViewById(R.id.totalTasks);
        completedTasks = findViewById(R.id.completedTasks);
        pendingTasks = findViewById(R.id.pendingTasks);
        message = findViewById(R.id.message);
        progressBar = findViewById(R.id.progressBar);
        goToAssignments = findViewById(R.id.goToAssignments);
        logout = findViewById(R.id.logout);
        profileBtn = findViewById(R.id.profileBtn);

        userSP = getSharedPreferences("UserData", MODE_PRIVATE);
        assignmentSP = getSharedPreferences("Assignments", MODE_PRIVATE);

        String name = userSP.getString("name", "User");
        welcomeText.setText("Welcome, " + name + " 👋");

        goToAssignments.setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class))
        );

        profileBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        logout.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        loadDashboardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {

        String savedData = assignmentSP.getString("data", "");

        int total = 0;
        int completed = 0;

        if (!savedData.equals("")) {

            String[] items = savedData.split("\\|\\|");

            for (String item : items) {
                if (!item.trim().equals("")) {
                    total++;

                    if (item.contains("@@1")) {
                        completed++;
                    }
                }
            }
        }

        int pending = total - completed;

        totalTasks.setText("Total: " + total);
        completedTasks.setText("Completed: " + completed);
        pendingTasks.setText("Pending: " + pending);

        int percent = (total == 0) ? 0 : (completed * 100 / total);
        progressBar.setProgress(percent);

        if (total == 0) {
            message.setText("📚 No assignments yet. Start adding!");
        } else if (completed == total) {
            message.setText("🔥 Great job! All tasks done!");
        } else if (completed > 0) {
            message.setText("👍 Keep going!");
        } else {
            message.setText("⏳ Start working on your tasks!");
        }
    }
}