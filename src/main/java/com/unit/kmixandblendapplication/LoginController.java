package com.unit.kmixandblendapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.action.Action;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;


    public void successLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.hide();
        scene = new Scene(root);
        stage.setTitle("K - Mix And Blend | Dashboard");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    private void actionLogin(ActionEvent event) throws IOException {
        Window owner = txtUser.getScene().getWindow();
        System.out.println(txtUser.getText());
        System.out.println(txtPassword.getText());

        if(txtUser.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"Please enter a valid username","Invalid Username");
            return;
        }
        if(txtPassword.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"Please enter a valid password", "Invalid Password");
            return;
        }

        String username = txtUser.getText();
        String password = txtPassword.getText();

        JDBCObject jdbcObject = new JDBCObject();
        boolean isValidCredentials;
        try{
            isValidCredentials = jdbcObject.validateCredentials(username,password);
            if(isValidCredentials){
                successLoginScene(event);
            } else{
                infoBox("Please enter a valid login credential",null,"Failed");
            }
        }catch(Exception exception){
            showAlert(Alert.AlertType.ERROR,owner,"Database is OFFLINE","Database");
        }


    }
    public static void infoBox(String infoMessage,String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title){
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.show();
    }

    private void createDir(){
        File file = new File("assets\\productImage\\");
        boolean dirCreated = file.mkdirs();
        if(dirCreated){
            System.out.println("Directory does not exist... Creating Directory..");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createDir();
    }
}
