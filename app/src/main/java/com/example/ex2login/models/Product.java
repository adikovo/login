package com.example.ex2login.models;

public class Product {
    private String name;
    private int quantity;

    public Product() {
        // Required empty constructor for Firebase
    }

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
} 