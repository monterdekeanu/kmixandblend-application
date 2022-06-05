package com.unit.kmixandblendapplication;

import javafx.beans.Observable;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {
    @FXML
    public ChoiceBox cbProductType;
    @FXML
    public TextField txtProductName;
    @FXML
    public Button btnAddProduct;
    @FXML
    public Button btnRemoveProduct;
    @FXML
    public TableView<Products> tableProducts;
    @FXML
    public TableView<ProductSize> tableSize;
    @FXML
    public Button btnUpdateProduct;
    @FXML
    public TableColumn<ProductSize,Integer> colSizeId;
    @FXML
    public TableColumn<ProductSize,String> colSize;
    @FXML
    public TableColumn<ProductSize,Double> colPrice;
    @FXML
    public TableColumn<Products,String> colProductType;
    @FXML
    public TableColumn<Products,Integer> colId;
    @FXML
    public TableColumn<Products,String> colProductName;

    JDBCObject jdbcObject;

    public void displayProductTypes(){
        String[] prodTypes = {"DRINK","FOOD"};
        for(String type: prodTypes){
            cbProductType.getItems().add(type);
        }
        cbProductType.setValue("");
    }
    public void showProducts(){
        ObservableList<Products> list = getProductsList();
        colId.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        colProductType.setCellValueFactory(new PropertyValueFactory<Products,String>("productType"));
        colProductName.setCellValueFactory(new PropertyValueFactory<Products,String>("productName"));
        tableProducts.setItems(list);
    }
    public void showProductSize(){
        ObservableList<ProductSize> list = getProductSizeList();
        colSizeId.setCellValueFactory(new PropertyValueFactory<ProductSize,Integer>("id"));
        colSize.setCellValueFactory(new PropertyValueFactory<ProductSize,String>("size"));
        colPrice.setCellValueFactory(new PropertyValueFactory<ProductSize,Double>("price"));
        tableSize.setItems(list);
    }
    private void insertProduct(){
        String productName = txtProductName.getText();
        String productType = cbProductType.getValue().toString();
        if(!productName.isEmpty() && !productType.isEmpty()){
            String query = "INSERT INTO `tblProducts` (productType,productName) VALUES('"+productType+"','"+ productName+"')";
            executeQuery(query);
            try{
                query = "CREATE TABLE " + productName +"(id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, size VARCHAR(30) NOT NULL, price DOUBLE(40,2) NOT NULL)";
                executeQuery(query);
            }catch(Exception ex){
                System.out.println("TABLE CREATION Failed");
                ex.printStackTrace();
            }

            showProducts();
            txtProductName.setText("");
        }
    }

    private ObservableList<ProductSize> getProductSizeList(){
        ObservableList<ProductSize> productSizeList = FXCollections.observableArrayList();
        Connection conn = jdbcObject.getConnection();
        String query = "SELECT * FROM "+txtProductName.getText();
        Statement statement;
        ResultSet resultSet;
        try{
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            ProductSize productSize;
            while(resultSet.next()){
                productSize = new ProductSize(resultSet.getInt("id"),resultSet.getString("size"),resultSet.getDouble("price"));
                productSizeList.add(productSize);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return productSizeList;
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
                products = new Products(resultSet.getInt("id"),resultSet.getString("productType"), resultSet.getString("productName"));
                productList.add(products);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return productList;
    }

    private void executeQuery(String query){
        Connection conn = jdbcObject.getConnection();
        Statement statement;
        try{
            statement = conn.createStatement();
            statement.executeUpdate(query);
        }catch(Exception ex){
            System.out.println("Error while inserting record.");
            ex.printStackTrace();
        }
    }

    @FXML
    private void removeProduct(){
        Connection conn = jdbcObject.getConnection();
        try{
            Products product = tableProducts.getSelectionModel().getSelectedItem();
            String query = "DELETE FROM tblProducts" + " WHERE id = '"+product.getId()+"'";
            executeQuery(query);
            query = "DROP TABLE "+ product.getProductName();
            executeQuery(query);
            query = "ALTER TABLE tblProducts AUTO_INCREMENT = 1";
            executeQuery(query);
            showProducts();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    private void editProduct(ActionEvent event){
        Connection conn = jdbcObject.getConnection();
        if(!txtProductName.getText().isEmpty() && !cbProductType.getValue().toString().isEmpty()){
            try{
                Products product = tableProducts.getSelectionModel().getSelectedItem();
                String query = "UPDATE tblProducts SET productType =  '"+cbProductType.getValue().toString()+"',productName = '"+txtProductName.getText()+"' "+" WHERE id = '"+product.getId()+"'";
                executeQuery(query);
                if(!Objects.equals(product.getProductName(), txtProductName.getText())){
                    query = "RENAME TABLE "+ product.getProductName() + " TO " + txtProductName.getText();
                    executeQuery(query);
                }
                showProducts();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }

    }

    private void displayProductSize(){

    }
    private void addListenerTable(){
        tableProducts.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            if(newSelection!=null){
                btnRemoveProduct.setDisable(false);
                btnUpdateProduct.setDisable(false);
                txtProductName.setText(newSelection.getProductName());
                cbProductType.setValue(newSelection.getProductType());
                showProductSize();
            }else{
                txtProductName.setText("");
                btnRemoveProduct.setDisable(true);
                btnUpdateProduct.setDisable(true);
            }
        });

    }
    @FXML
    private void saveProduct(ActionEvent event){
        insertProduct();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        jdbcObject = new JDBCObject();
        displayProductTypes();
        addListenerTable();
        showProducts();
    }
}
