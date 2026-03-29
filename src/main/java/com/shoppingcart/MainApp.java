package com.shoppingcart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/shoppingcart/fxml/ShoppingCart.fxml"));

        Scene scene = new Scene(loader.load(), 900, 700);

        // Load stylesheet
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/com/shoppingcart/fxml/style.css")
                ).toExternalForm()
        );

        primaryStage.setTitle("JavaFX Shopping Cart — Aashish Timalsina");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
