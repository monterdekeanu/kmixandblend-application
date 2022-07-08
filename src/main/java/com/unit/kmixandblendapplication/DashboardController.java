package com.unit.kmixandblendapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Button btnAddTable;
    @FXML
    private Button btnManageProducts;
    @FXML
    private GridPane productGrid;
    @FXML
    private Button btnDisplayDrinks;
    private static Stage pStage;
    private static String currentOrder;
    ArrayList<Products> productsArrayList;
    Scene fxmlFile;
    Parent root;
    Stage window;
    public String getCurrentOrder(){
        return currentOrder;
    }
    @FXML
    private void manageProducts(ActionEvent event){
        try{
            openModalWindow("products.fxml","Manage Products");
        }catch(Exception ex){

        }
    }
    private void sizeSelection(String productName){
        try{
            openModalWindow("sizeboard.fxml","Size Selection");
        }catch(Exception ex){

        }
    }
    private void setPrimaryStage(Stage pStage){
        DashboardController.pStage = pStage;
    }

    static Stage getPrimaryStage(){
        return pStage;
    }


    private void openModalWindow(String resource, String title) throws IOException {
        System.out.println("WORKING");
        root = FXMLLoader.load(getClass().getResource(resource));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
//        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle(title);
        setPrimaryStage(window);
        window.showAndWait();
    }

    private ArrayList<Products> getProducts(){
        ArrayList<Products> productList = new ArrayList<>();
        JDBCObject jdbcObject = new JDBCObject();
        Connection conn = jdbcObject.getConnection();
        Statement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tblproducts";
        try{
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            Products products;
            while(resultSet.next()){
                products = new Products(resultSet.getInt("id"),resultSet.getString("productType"),resultSet.getString("productName"));
                productList.add(products);
            }
        }catch(Exception ex){
            System.out.println("Error in retrieving products: " +ex.getMessage());
        }
        return productList;
    }


    @FXML
    private void displayDrinks(ActionEvent event){
        productsArrayList = getProducts();
        productGrid.getChildren().clear();
        int row = 0;
        int column = 0;
        for(Products prod : productsArrayList){
            if(Objects.equals(prod.getProductType(), "DRINK")){
                productGrid.add(generateBtn(prod.getProductName()),column,row);
                column++;
                if(column == 4){
                    row++;
                    column = 0;
                }
            }

        }
    }

    @FXML
    private void displayFood(ActionEvent event){
        productGrid.getChildren().clear();
        productsArrayList = getProducts();
        int row = 0;
        int column = 0;
        for(Products prod : productsArrayList){
            if(Objects.equals(prod.getProductType(), "FOOD")||Objects.equals(prod.getProductType(), "COMBO")){
                productGrid.add(generateBtn(prod.getProductName()),column,row);
                column++;
                if(column == 4){
                    row++;
                    column = 0;
                }
            }

        }
    }

    private Button generateBtn(String productName){
        File file = new File("./assets/productImage/" + productName + ".jpg");
        ImageView ivProducts = new ImageView();
        ivProducts.setImage(new Image(file.toURI().toString()));
        VBox vbProduct = new VBox();
        if(!file.exists()){
            ivProducts.setImage(new Image(getClass().getResourceAsStream("Image/noimage.jpg")));
        }
        Label txtProductName = new Label(productName);
        ivProducts.setFitHeight(70);
        ivProducts.setFitWidth(90);
        vbProduct.getChildren().add(ivProducts);
        vbProduct.getChildren().add(txtProductName);
        txtProductName.setPrefWidth(90);
        txtProductName.setMinWidth(90);
        txtProductName.setTextAlignment(TextAlignment.CENTER);
        txtProductName.setAlignment(Pos.CENTER);
        vbProduct.setAlignment(Pos.CENTER);
        Button button = new Button("",vbProduct);
        button.setMinHeight(70);
        button.setMinWidth(110);
        button.setPrefWidth(100);
        button.setAlignment(Pos.CENTER);

        button.setOnAction(e -> {
            try{
                currentOrder = txtProductName.getText();
                openModalWindow("sizeboard.fxml","Size Selection");
                displayOrders();
            }catch(Exception ex){
            }
            updateTotal();
        });
        return button;
    }

    @FXML
    public TableView<Orders> tvOrders;
    @FXML
    public TableColumn<Orders,String> colProduct;
    @FXML
    public TableColumn<Orders,Integer> colQty;
    @FXML
    public TableColumn<Orders,Double> colPrice;
    @FXML
    public TableColumn<Orders,Double> colTotal;
    public static ObservableList<Orders> ordersList = FXCollections.observableArrayList();

    public void generateOrder(String productName,String prodSize){
        JDBCObject jdbcObject = new JDBCObject();
        Orders orders;
//        orders = new Orders(productName,1,75.25,100);
        try{
            Connection conn = jdbcObject.getConnection();
            Statement statement;
            ResultSet resultSet;
            String query = "SELECT * FROM " + productName;
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                if(resultSet.getString("size").equals(prodSize)){
                    boolean found = false;
                    int index = 0;
                    for(Orders order : ordersList){
                        if(order.getProductName().equals(productName + "("+prodSize+")")){
                            orders = new Orders(productName + "("+prodSize+")",order.getQuantity() + 1,order.getPrice(),order.getTotal() + resultSet.getDouble("price"));
                            found = true;
                            ordersList.set(index,orders);
                        }
                        index++;
                    }
                    if(!found){
                        orders = new Orders(productName + "("+prodSize+")",1,resultSet.getDouble("price"),resultSet.getDouble("price"));
                        ordersList.add(orders);
                    }

                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public void displayOrders(){
        colProduct.setCellValueFactory(new PropertyValueFactory<Orders, String>("productName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Orders, Double>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Orders, Double>("total"));
        tvOrders.setItems(ordersList);
    }

    @FXML
    private void clearOrders(){
        ordersList.clear();
        tvOrders.setItems(ordersList);
        updateTotal();
    }

    @FXML
    public Label labelTotal;
    private void updateTotal(){
        double total = 0;
        for(Orders order: ordersList){
            total += order.getTotal();
        }
        labelTotal.setText(String.valueOf(total));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        getProducts();
    }
}
