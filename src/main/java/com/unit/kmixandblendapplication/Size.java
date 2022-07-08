package com.unit.kmixandblendapplication;

public class Size {
    private int id;
    private String size;
    private double price;

    public Size(int id, String size, double price) {
        this.id = id;
        this.size = size;
        this.price = price;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
