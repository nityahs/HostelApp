package com.b108.hosteconnect;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LeaveApplicationActivity extends AppCompatActivity {

    private Button startDateButton, endDateButton, submitButton;
    private EditText reasonEditText;
    private Calendar startDateCalendar, endDateCalendar;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("leave_applications");

        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        submitButton = findViewById(R.id.submitLeaveButton);
        reasonEditText = findViewById(R.id.reasonEditText);

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        // Start Date Picker
        startDateButton.setOnClickListener(v -> showStartDatePicker());

        // End Date Picker
        endDateButton.setOnClickListener(v -> showEndDatePicker());

        // Submit Leave Application
        submitButton.setOnClickListener(v -> submitLeaveApplication());
    }

    private void showStartDatePicker() {
        int year = startDateCalendar.get(Calendar.YEAR);
        int month = startDateCalendar.get(Calendar.MONTH);
        int day = startDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, day1) -> {
                    startDateCalendar.set(Calendar.YEAR, year1);
                    startDateCalendar.set(Calendar.MONTH, month1);
                    startDateCalendar.set(Calendar.DAY_OF_MONTH, day1);
                    updateStartDateButton();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showEndDatePicker() {
        int year = endDateCalendar.get(Calendar.YEAR);
        int month = endDateCalendar.get(Calendar.MONTH);
        int day = endDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, day1) -> {
                    endDateCalendar.set(Calendar.YEAR, year1);
                    endDateCalendar.set(Calendar.MONTH, month1);
                    endDateCalendar.set(Calendar.DAY_OF_MONTH, day1);
                    updateEndDateButton();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateStartDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        startDateButton.setText(sdf.format(startDateCalendar.getTime()));
    }

    private void updateEndDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        endDateButton.setText(sdf.format(endDateCalendar.getTime()));
    }

    private void submitLeaveApplication() {
        String reason = reasonEditText.getText().toString().trim();

        // Validate inputs
        if (reason.isEmpty()) {
            Toast.makeText(this, "Please enter reason for leave", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDateCalendar.getTime().after(endDateCalendar.getTime())) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare leave application data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = sdf.format(startDateCalendar.getTime());
        String endDate = sdf.format(endDateCalendar.getTime());

        LeaveApplication leaveApplication = new LeaveApplication(
                startDate,
                endDate,
                reason,
                "Pending"
        );

        // Save to Firebase
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child(userId)
                .push()
                .setValue(leaveApplication)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Leave application submitted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to submit leave application", Toast.LENGTH_SHORT).show();
                });
    }
}