package com.b108.hosteconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView welcomeText;
    private CardView attendanceButton, foodMenuButton, leaveApplicationButton, scholarshipInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        welcomeText = findViewById(R.id.welcomeText);
        if (currentUser != null) {
            welcomeText.setText("Welcome " + currentUser.getEmail());
        }

        attendanceButton = findViewById(R.id.attendanceButton);
        foodMenuButton = findViewById(R.id.foodMenuButton);
        leaveApplicationButton = findViewById(R.id.leaveApplicationButton);
        scholarshipInfoButton = findViewById(R.id.scholarshipInfoButton);

        attendanceButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AttendanceDisplayActivity.class));
        });

        foodMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FoodMenuActivity.class));
        });

        leaveApplicationButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LeaveApplicationActivity.class));
        });

        scholarshipInfoButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScholarshipInfoActivity.class));
        });
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
