package com.unit.kmixandblendapplication;

public class Orders {
    private String productName;
    private int quantity;
    private double price;
    private double total;

    public Orders(String productName, int quantity, double price, double total) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
