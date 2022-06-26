package com.unit.kmixandblendapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import javax.swing.text.Element;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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


    ArrayList<Products> productsArrayList = new ArrayList<>();
    Scene fxmlFile;
    Parent root;
    Stage window;
    @FXML
    private void manageProducts(ActionEvent event){
        try{
            openModalWindow("products.fxml","Manage Products");
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

    private void getProducts(){
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
                productsArrayList.add(products);
            }
        }catch(Exception ex){
            System.out.println("Error in retrieving products: " +ex.getMessage());
        }
    }


    @FXML
    private void displayDrinks(ActionEvent event){
        int row = 0;
        int column = 0;
        getProducts();
        System.out.println(productsArrayList);
        System.out.println(getTotalProducts());
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

    private int getTotalProducts(){
        JDBCObject jdbcObject = new JDBCObject();
        int total=0;
        Connection conn = jdbcObject.getConnection();
        String query = "SELECT * FROM tblproducts";
        Statement statement;
        ResultSet resultSet;
        try{
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                total++;
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return total;
    }
    private void displayProdBtn(){

    }
    private Button generateBtn(String productName){
        File file = new File("./assets/productImage/" + productName + ".jpg");
        VBox vbProduct = new VBox();

        Label txtProductName = new Label(productName);
        ImageView ivProducts = new ImageView();
        ivProducts.setImage(new Image(file.toURI().toString()));
        ivProducts.setFitHeight(110);
        ivProducts.setFitWidth(100);
        vbProduct.getChildren().add(ivProducts);
        vbProduct.getChildren().add(txtProductName);
        txtProductName.setPrefWidth(100);
        txtProductName.setMinWidth(100);
        txtProductName.setTextAlignment(TextAlignment.CENTER);
        Button button = new Button("",vbProduct);
        button.setAlignment(Pos.CENTER);
        return button;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getProducts();
    }
}
