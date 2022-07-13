package com.unit.kmixandblendapplication;

import javafx.beans.Observable;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {

    File file;
    @FXML
    public ChoiceBox cbProductType;
    @FXML
    public TextField txtprice;
    @FXML
    public TextField txtsize;
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
    public Button btnAddSize;
    @FXML
    public Button btnUpdateSize;
    @FXML
    public Button btnRemoveSize;
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
    public Button btnSave;
    @FXML
    public TableColumn<Products,String> colProductName;


    JDBCObject jdbcObject;

    @FXML
    public Button btnBrowse;
    @FXML
    public ImageView ivProducts;

    private void saveImage(BufferedImage bf) throws IOException {
        File outputFile = new File("./assets/productImage/" + txtProductName.getText() + ".jpg");
        ImageIO.write(bf,"jpg",outputFile);
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title){
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);
        alert.showAndWait();
    }
    @FXML
    private void handleBrowseImage(ActionEvent event){
        try{
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG"); // FILTER TO ACCEPT EXTENSIONS
            FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
            fc.getExtensionFilters().addAll(ext1,ext2);
            file = fc.showOpenDialog(DashboardController.getPrimaryStage());
            BufferedImage bf;
            bf = ImageIO.read(file);
            try{
                Image image = SwingFXUtils.toFXImage(bf,null);
                ivProducts.setImage(image);
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
            saveImage(bf);
        }catch(Exception ex){
            System.out.println(" " + ex.getMessage());
        }
    }
    public void displayProductTypes(){
        String[] prodTypes = {"DRINK","FOOD","COMBO"};
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
    private void insertProductSize(){
        String productSize = txtsize.getText();
        double productPrice = Double.parseDouble(txtprice.getText());
        if(!productSize.isEmpty() && productPrice >= 0.0){
            String query = "INSERT INTO [dbo].[" + txtProductName.getText() + "] (size,price) VALUES ('" +productSize +"','" +productPrice+"')";
            executeQuery(query);
        }
        showProductSize();
        txtprice.setText("");
        txtsize.setText("");

    }
    private void insertProduct(){
        String productName = txtProductName.getText();
        String productType = cbProductType.getValue().toString();
        if(!productName.isEmpty() && !productType.isEmpty()){
            String query = "INSERT INTO [dbo].[tblproducts](productType,productName) VALUES('"+productType+"','"+ productName+"')";
            executeQuery(query);
            try{
                query = "CREATE TABLE " + productName +"(id INT IDENTITY(1,1) PRIMARY KEY, size VARCHAR(30) NOT NULL, price DOUBLE PRECISION NOT NULL)";
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
    private void removeProductSize(){
        Connection conn = jdbcObject.getConnection();
        try{
            ProductSize productSize = tableSize.getSelectionModel().getSelectedItem();
            String query = "DELETE FROM [dbo].[" + txtProductName.getText() + "] WHERE id = '"+productSize.getId()+"'";
            executeQuery(query);
            showProductSize();
        }catch(Exception err){
            System.out.println(err.getMessage());
        }
    }

    @FXML
    private void removeProduct(){
        Connection conn = jdbcObject.getConnection();
        try{//
            Products product = tableProducts.getSelectionModel().getSelectedItem();
            String query = "DELETE FROM [dbo].[tblproducts]" + " WHERE id = '"+product.getId()+"'";
            executeQuery(query);
            query = "DROP TABLE "+ product.getProductName();
            executeQuery(query);
            showProducts();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void editProductSize(ActionEvent event){
        Connection conn = jdbcObject.getConnection();
        if(!txtprice.getText().isEmpty() && !txtsize.getText().isEmpty()){
            try{
                ProductSize productSize = tableSize.getSelectionModel().getSelectedItem();
                String query = "UPDATE [dbo].[" + txtProductName.getText() + "] SET size = '" + txtsize.getText()+"', price = '" + txtprice.getText() + "' WHERE id = '" +productSize.getId()+"'" ;
                executeQuery(query);

                showProductSize();
            }catch(Exception exception){
                System.out.println(exception.getMessage());
            }

        }
    }
    @FXML
    private void editProduct(ActionEvent event){
        Connection conn = jdbcObject.getConnection();
        if(!txtProductName.getText().isEmpty() && !cbProductType.getValue().toString().isEmpty()){
            try{
                Products product = tableProducts.getSelectionModel().getSelectedItem();
                String query = "UPDATE [dbo].[tblproducts] SET productType =  '"+cbProductType.getValue().toString()+"',productName = '"+txtProductName.getText()+"' "+" WHERE id = '"+product.getId()+"'";
                executeQuery(query);
                if(!Objects.equals(product.getProductName(), txtProductName.getText())){
                    query = "DROP TABLE [dbo].["+ product.getProductName() + "]";
                    executeQuery(query);
                    query = "CREATE TABLE " + txtProductName.getText()+"(id INT IDENTITY(1,1) PRIMARY KEY, size VARCHAR(30) NOT NULL, price DOUBLE PRECISION NOT NULL)";
                    executeQuery(query);
                }
                showProducts();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private void addSizeProductListener(){
        tableSize.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if(newSelection != null){
                btnRemoveSize.setDisable(false);
                btnUpdateSize.setDisable(false);
                txtsize.setText(newSelection.getSize());
                txtprice.setText(String.valueOf(newSelection.getPrice()));
            }else{
                txtprice.setText("");
                txtsize.setText("");
                btnRemoveSize.setDisable(true);
                btnUpdateSize.setDisable(true);
            }
        });

    }

    private void addListenerTable(){
        tableProducts.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            if(newSelection!=null){
                btnRemoveProduct.setDisable(false);
                btnUpdateProduct.setDisable(false);
                btnAddSize.setDisable(false);
                btnBrowse.setDisable(false);
                btnSave.setDisable(false);
                txtProductName.setText(newSelection.getProductName());
                cbProductType.setValue(newSelection.getProductType());
                try{
                    file = new File("./assets/productImage/" + newSelection.getProductName() + ".jpg");
                    ivProducts.setImage(new Image(file.toURI().toString()));
                    if(!file.exists()){
                        ivProducts.setImage(new Image(getClass().getResourceAsStream("Image/noimage.jpg")));
                    }
                }catch(Exception ex){
                    System.out.println("" + ex.getMessage());

                }

                showProductSize();
            }else{
                txtProductName.setText("");
                btnAddSize.setDisable(true);
                btnRemoveProduct.setDisable(true);
                btnUpdateProduct.setDisable(true);
                btnSave.setDisable(true);
                btnBrowse.setDisable(true);
            }
        });
    }

    @FXML
    private void saveProductSize(ActionEvent event){
        insertProductSize();
    }
    @FXML
    private void saveProduct(ActionEvent event){
        insertProduct();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        jdbcObject = new JDBCObject();
        displayProductTypes();
        addSizeProductListener();
        addListenerTable();
        showProducts();
    }
}
