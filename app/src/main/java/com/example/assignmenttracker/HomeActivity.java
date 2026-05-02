package com.example.assignmenttracker;

import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    EditText subject, title, deadline;
    Button btnAdd;
    ListView listView;

    ArrayList<String> assignments;
    BaseAdapter adapter;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        subject = findViewById(R.id.subject);
        title = findViewById(R.id.title);
        deadline = findViewById(R.id.deadline);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        assignments = new ArrayList<>();

        // 🔐 SharedPreferences
        sp = getSharedPreferences("Assignments", MODE_PRIVATE);
        editor = sp.edit();

        // 📥 Load saved data
        loadData();

        // 🔥 Adapter
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return assignments.size();
            }

            @Override
            public Object getItem(int position) {
                return assignments.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                }

                TextView text = convertView.findViewById(R.id.text);
                CheckBox checkBox = convertView.findViewById(R.id.checkBox);

                String item = assignments.get(position);

                String[] parts = item.split("@@");
                String textData = parts[0];
                boolean isCompleted = parts.length > 1 && parts[1].equals("1");

                text.setText(textData);
                checkBox.setChecked(isCompleted);

                // Strike effect
                if (isCompleted) {
                    text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    text.setPaintFlags(0);
                }

                checkBox.setOnCheckedChangeListener(null);

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (isChecked && !isCompleted) {

                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("Complete Assignment")
                                .setMessage("Mark this as completed?")
                                .setPositiveButton("Yes", (dialog, which) -> {

                                    text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    assignments.set(position, textData + "@@1");
                                    saveData();

                                    Toast.makeText(HomeActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("No", (dialog, which) -> {
                                    checkBox.setChecked(false);
                                })
                                .show();

                    } else if (!isChecked && isCompleted) {

                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("Undo Completion")
                                .setMessage("Mark as pending again?")
                                .setPositiveButton("Yes", (dialog, which) -> {

                                    text.setPaintFlags(0);
                                    assignments.set(position, textData + "@@0");
                                    saveData();

                                    Toast.makeText(HomeActivity.this, "Marked Pending", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("No", (dialog, which) -> {
                                    checkBox.setChecked(true);
                                })
                                .show();
                    }
                });

                return convertView;
            }
        };

        listView.setAdapter(adapter);

        // ➕ ADD
        btnAdd.setOnClickListener(v -> {
            String data = subject.getText().toString() + " - " +
                    title.getText().toString() + " - " +
                    deadline.getText().toString();

            if (!data.trim().isEmpty()) {

                assignments.add(data + "@@0"); // default pending
                adapter.notifyDataSetChanged();

                saveData();

                Toast.makeText(this, "Assignment Added", Toast.LENGTH_SHORT).show();

                subject.setText("");
                title.setText("");
                deadline.setText("");
            } else {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // 🗑 DELETE
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            assignments.remove(position);
            adapter.notifyDataSetChanged();

            saveData();

            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        // 📅 DATE PICKER
        deadline.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            new DatePickerDialog(this,
                    (view, y, m, d) ->
                            deadline.setText(d + "/" + (m + 1) + "/" + y),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    // 💾 SAVE
    private void saveData() {
        String data = "";

        for (String item : assignments) {
            data += item + "||";
        }

        editor.putString("data", data);
        editor.apply();
    }

    // 📥 LOAD
    private void loadData() {
        String savedData = sp.getString("data", "");

        assignments.clear();

        if (!savedData.equals("")) {
            String[] items = savedData.split("\\|\\|");

            for (String item : items) {
                if (!item.equals("")) {
                    assignments.add(item);
                }
            }
        }
    }
}