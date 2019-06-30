package com.lb.jeddit;

import com.lb.jeddit.controllers.LoginWindowController;
import com.lb.jeddit.controllers.MainWindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene;

        if(new File(System.getProperty("user.dir") + "/src/main/resources/com/lb/jeddit/persistent").list().length > 0) {
            //Token exists, therefore skip login window and login straight to mainwindow
            Login login = new Login("niceorg");
            scene = new Scene(MainWindowController.getInstance());
        } else {
            //No token exists, request loginwindow for user to sign in
            scene = new Scene(new LoginWindowController());
            stage.setResizable(false);
        }

        scene.setFill(Paint.valueOf("#1e1e1e"));

        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(1000);
        stage.setTitle("Jeddit");
        stage.getIcons().add(new Image("com/lb/jeddit/images/logo.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}