package com.b108.hosteconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.FoodMenuViewHolder> {
    private Context context;
    private List<FoodMenuItem> foodMenuItems;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public FoodMenuAdapter(Context context, List<FoodMenuItem> foodMenuItems) {
        this.context = context;
        this.foodMenuItems = foodMenuItems;
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("food_menu");
    }

    @NonNull
    @Override
    public FoodMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_menu, parent, false);
        return new FoodMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodMenuViewHolder holder, int position) {
        FoodMenuItem item = foodMenuItems.get(position);

        // Set date and meal type
        holder.dateTextView.setText(item.getDate());
        holder.mealTypeTextView.setText(item.getMealType().toUpperCase());

        // Set menu and time
        holder.menuTextView.setText(item.getMenu());
        holder.timeTextView.setText(item.getTime());

        // Set take button state
        updateTakeButton(holder, item);
    }

    private void updateTakeButton(FoodMenuViewHolder holder, FoodMenuItem item) {
        String currentUserId = mAuth.getCurrentUser().getUid();

        // Check if current user has taken the meal
        boolean isTaken = item.isCurrentUserTaken(currentUserId);

        holder.takeButton.setText(isTaken ? "Untake" : "Take");
        holder.takeButton.setOnClickListener(v -> toggleMealStatus(item, currentUserId));
    }

    private void toggleMealStatus(FoodMenuItem item, String currentUserId) {
        DatabaseReference mealRef = databaseReference
                .child(item.getDate())
                .child(item.getMealType())
                .child("taken_by")
                .child(currentUserId);

        // Toggle the current user's taken status
        boolean currentStatus = item.isCurrentUserTaken(currentUserId);
        mealRef.setValue(!currentStatus)
                .addOnSuccessListener(aVoid -> {
                    // Local update not needed as Firebase listener will update the view
                    Toast.makeText(context,
                            !currentStatus ? "Meal Taken" : "Meal Untaken",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context,
                            "Failed to update meal status",
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return foodMenuItems.size();
    }

    static class FoodMenuViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, mealTypeTextView, menuTextView, timeTextView;
        Button takeButton;

        public FoodMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateText);
            mealTypeTextView = itemView.findViewById(R.id.mealTypeText);
            menuTextView = itemView.findViewById(R.id.menuText);
            timeTextView = itemView.findViewById(R.id.timeText);
            takeButton = itemView.findViewById(R.id.takeButton);
        }
    }
}