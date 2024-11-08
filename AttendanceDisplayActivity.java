package com.b108.hosteconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendanceDisplayActivity extends AppCompatActivity {
    private TextView totalDaysText, daysPresentText, attendancePercentageText;
    private ProgressBar attendanceProgressBar;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_display);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize Views
        totalDaysText = findViewById(R.id.totalDaysValue);
        daysPresentText = findViewById(R.id.presentDaysValue);
        attendancePercentageText = findViewById(R.id.attendancePercentageValue);
        attendanceProgressBar = findViewById(R.id.attendanceProgressBar);
        emptyView = findViewById(R.id.emptyView);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("attendance");

        // Fetch and display attendance data
        fetchAttendanceData(currentUser);
    }

    private void fetchAttendanceData(FirebaseUser currentUser) {
        if (currentUser == null) {
            showEmptyState();
            return;
        }

        // Reference to the current user's attendance data
        DatabaseReference userAttendanceRef = databaseReference.child(currentUser.getUid());

        userAttendanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve attendance data
                    Integer totalDays = snapshot.child("total_days").getValue(Integer.class);
                    Integer presentDays = snapshot.child("present_days").getValue(Integer.class);

                    // Default to 0 if null
                    totalDays = totalDays != null ? totalDays : 0;
                    presentDays = presentDays != null ? presentDays : 0;

                    // Update UI
                    updateAttendanceUI(totalDays, presentDays);
                } else {
                    // No attendance data found
                    showEmptyState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
                showErrorState(error.getMessage());
            }
        });
    }

    private void updateAttendanceUI(int totalDays, int presentDays) {
        // Update TextViews
        totalDaysText.setText(String.valueOf(totalDays));
        daysPresentText.setText(String.valueOf(presentDays));

        // Calculate and display attendance percentage
        float attendancePercentage = totalDays > 0
                ? ((float) presentDays / totalDays) * 100
                : 0;

        attendancePercentageText.setText(String.format("%.2f%%", attendancePercentage));

        // Update Progress Bar
        attendanceProgressBar.setMax(totalDays);
        attendanceProgressBar.setProgress(presentDays);

        // Show/Hide empty view
        if (totalDays == 0) {
            showEmptyState();
        } else {
            hideEmptyState();
        }
    }

    private void showEmptyState() {
        emptyView.setVisibility(View.VISIBLE);
        totalDaysText.setText("0");
        daysPresentText.setText("0");
        attendancePercentageText.setText("0%");
        attendanceProgressBar.setProgress(0);
    }

    private void hideEmptyState() {
        emptyView.setVisibility(View.GONE);
    }

    private void showErrorState(String errorMessage) {
        emptyView.setText("Error: " + errorMessage);
        emptyView.setVisibility(View.VISIBLE);
        totalDaysText.setText("0");
        daysPresentText.setText("0");
        attendancePercentageText.setText("0%");
        attendanceProgressBar.setProgress(0);
    }
}