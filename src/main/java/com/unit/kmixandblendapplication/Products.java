package com.unit.kmixandblendapplication;

public class Products {
    private int id;
    private String productName;

    private String productType;

    public Products(int id, String productType, String productName) {
        this.id = id;
        this.productName = productName;
        this.productType = productType;
    }

    public String getProductType(){
        return productType;
    }
    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }
}
