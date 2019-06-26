package com.lb.jeddit;

import com.lb.jeddit.controllers.LoginWindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new LoginWindowController());
        scene.setFill(Paint.valueOf("#1e1e1e"));

        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(1000);
        stage.setTitle("Jeddit");
        stage.getIcons().add(new Image("com/lb/jeddit/images/logo.png"));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}