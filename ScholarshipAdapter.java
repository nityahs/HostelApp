package com.b108.hosteconnect;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipAdapter extends RecyclerView.Adapter<ScholarshipAdapter.ViewHolder> {
    private List<Scholarship> scholarshipList;

    public ScholarshipAdapter() {
        this.scholarshipList = new ArrayList<>();
    }

    public void setScholarships(List<Scholarship> scholarships) {
        this.scholarshipList = scholarships;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scholarship, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scholarship scholarship = scholarshipList.get(position);
        holder.titleText.setText(scholarship.getTitle());
        holder.descriptionText.setText(scholarship.getDescription());
    }

    @Override
    public int getItemCount() {
        return scholarshipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descriptionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
        }
    }
}