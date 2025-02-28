package com.example.oujdashopproject.Users;

public class Product {
    private int id;
    private String nom;
    private double price;
    private String description;
    private int categoryId;

    public Product(int id, String nom, double price, String description, int categoryId) {
        this.id = id;
        this.nom = nom;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }
}