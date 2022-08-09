package com.example.menu.models;

import com.google.firebase.database.PropertyName;

public class Dish {
    String id;
    String name;
    String description;
    String image;
    String category;
    boolean isVegetarian;
    float calories;
    float rating;
    float price;

    public Dish() {
    }

    public Dish(String id, String name, String description, String image, String category, boolean isVegetarian, float calories, float rating, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.category = category;
        this.isVegetarian = isVegetarian;
        this.calories = calories;
        this.rating = rating;
        this.price = price;
    }

    public Dish(String id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Dish(String name, float price) {
        this.name = name;
        this.price = price;
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("category")
    public String getCategory() {
        return category;
    }

    @PropertyName("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @PropertyName("isVegetarian")
    public boolean getIsVegetarian() {
        return isVegetarian;
    }

    @PropertyName("isVegetarian")
    public void setIsVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    @PropertyName("calories")
    public float getCalories() {
        return calories;
    }

    @PropertyName("calories")
    public void setCalories(float calories) {
        this.calories = calories;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("image")
    public String getImage() {
        return image;
    }
    @PropertyName("image")
    public void setImage(String image) {
        this.image = image;
    }

    @PropertyName("rating")
    public float getRating() {
        return rating;
    }

    @PropertyName("rating")
    public void setRating(float rating) {
        this.rating = rating;
    }

    @PropertyName("price")
    public float getPrice() {
        return price;
    }

    @PropertyName("price")
    public void setPrice(float price) {
        this.price = price;
    }
}
