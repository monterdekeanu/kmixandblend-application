package com.unit.kmixandblendapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("K-Mix and Blend | Login");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("Icon.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}