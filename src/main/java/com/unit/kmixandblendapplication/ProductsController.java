package com.unit.kmixandblendapplication;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {
    @FXML
    public TextField txtProductName;
    @FXML
    public Button btnAddProduct;
    @FXML
    public Button btnRemoveProduct;
    @FXML
    public TableView<Products> tableProducts;
    @FXML
    public TableColumn<Products,Integer> colId;
    @FXML
    public TableColumn<Products,String> colProductName;

    JDBCObject jdbcObject;

    public void showProducts(){
        ObservableList<Products> list = getProductsList();
        colId.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<Products,String>("productName"));
        tableProducts.setItems(list);
    }

    private ObservableList<Products> getProductsList() {
        ObservableList<Products> productList = FXCollections.observableArrayList();
        Connection conn = jdbcObject.getConnection();
        String query = "SELECT * FROM tblproducts";
        Statement statement;
        ResultSet resultSet;
        try{
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            Products products;
            while(resultSet.next()){
                products = new Products(resultSet.getInt("id"), resultSet.getString("productName"));
                productList.add(products);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return productList;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        jdbcObject = new JDBCObject();
        showProducts();
    }
}
