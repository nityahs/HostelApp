package com.b108.hosteconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodMenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodMenuAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;
    private List<FoodMenuItem> foodMenuItems;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_empty_view);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("food_menu");

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyView = findViewById(R.id.emptyView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodMenuItems = new ArrayList<>();
        adapter = new FoodMenuAdapter(this, foodMenuItems);
        recyclerView.setAdapter(adapter);

        // Fetch food menu
        fetchFoodMenu();
    }

    private void fetchFoodMenu() {
        showLoading();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodMenuItems.clear();

                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();

                    // Process Breakfast
                    processMealType(dateSnapshot, "breakfast", date, foodMenuItems);

                    // Process Lunch
                    processMealType(dateSnapshot, "lunch", date, foodMenuItems);

                    // Process Dinner
                    processMealType(dateSnapshot, "dinner", date, foodMenuItems);
                }

                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showError(error.getMessage());
            }
        });
    }

    private void processMealType(DataSnapshot dateSnapshot, String mealType, String date, List<FoodMenuItem> foodMenuItems) {
        DataSnapshot mealSnapshot = dateSnapshot.child(mealType);
        if (mealSnapshot.exists()) {
            String menu = mealSnapshot.child("menu").getValue(String.class);
            String time = mealSnapshot.child("time").getValue(String.class);

            // Get taken_by map
            Map<String, Boolean> takenBy = new HashMap<>();
            DataSnapshot takenBySnapshot = mealSnapshot.child("taken_by");
            for (DataSnapshot userSnapshot : takenBySnapshot.getChildren()) {
                takenBy.put(
                        userSnapshot.getKey(),
                        Boolean.TRUE.equals(userSnapshot.getValue(Boolean.class))
                );
            }

            FoodMenuItem item = new FoodMenuItem(date, mealType, menu, time, takenBy);
            foodMenuItems.add(item);
        }
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);

        if (foodMenuItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No food menu items available");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    private void showLoading() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setText("Error: " + errorMessage);
    }
}