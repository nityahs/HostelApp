package com.b108.hosteconnect;

import java.util.Map;

public class FoodMenuItem {
    private String date;
    private String mealType;
    private String menu;
    private String time;
    private Map<String, Boolean> takenBy;

    public FoodMenuItem() {}

    public FoodMenuItem(String date, String mealType, String menu, String time, Map<String, Boolean> takenBy) {
        this.date = date;
        this.mealType = mealType;
        this.menu = menu;
        this.time = time;
        this.takenBy = takenBy;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Boolean> getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(Map<String, Boolean> takenBy) {
        this.takenBy = takenBy;
    }

    // Check if current user has taken the meal
    public boolean isCurrentUserTaken(String currentUserId) {
        return takenBy != null && Boolean.TRUE.equals(takenBy.get(currentUserId));
    }
}