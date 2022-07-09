package com.unit.kmixandblendapplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SizesController extends DashboardController implements Initializable {
    @FXML
    private VBox vbSizes;
    @FXML
    private Label labelProduct;

    @FXML
    private void getSizes(){
        JDBCObject jdbcObject = new JDBCObject();
        int totalBtn = 0;
        try{
            Connection conn = jdbcObject.getConnection();
            Statement statement;
            ResultSet resultSet;
            System.out.println(getCurrentOrder());
            String query = "SELECT * FROM " + getCurrentOrder();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                totalBtn++;
                Size size = new Size(resultSet.getInt("id"),resultSet.getString("size"),resultSet.getDouble("price"));
                Button sizeBtn = new Button(size.getSize());
                sizeBtn.setPrefWidth(120);
                sizeBtn.setPrefHeight(40);
                sizeBtn.setTextFill(Color.WHITE);
                sizeBtn.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12));
                sizeBtn.setStyle("-fx-background-color:  #0abde3");
                sizeBtn.setOnAction(event -> {
                    try {
                        generateOrder(getCurrentOrder(),size.getSize());
                        Stage stage = (Stage)vbSizes.getScene().getWindow();
                        stage.close();
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());

                    }
                });
                vbSizes.getChildren().add(sizeBtn);
            }
            vbSizes.setPrefHeight(60 + (totalBtn*50));
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getSizes();
    }
}
