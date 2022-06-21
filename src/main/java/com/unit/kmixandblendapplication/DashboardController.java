package com.unit.kmixandblendapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Button btnAddTable;
    @FXML
    private Button btnManageProducts;
    private static Stage pStage;

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




}
