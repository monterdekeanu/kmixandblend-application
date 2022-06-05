package com.unit.kmixandblendapplication;

public class ProductSize {
    private int id;
    private String size;
    private double price;

    public ProductSize(int id, String size, double price) {
        this.id = id;
        this.size = size;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }
}
