package com.unit.kmixandblendapplication;

public class Products {
    private int id;
    private String productName;

    public Products(int id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }
}
