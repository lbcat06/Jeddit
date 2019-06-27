package com.lb.jeddit.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainWindowController extends AnchorPane {

	public MainWindowController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/" + getClass().getSimpleName().substring(0, getClass().getSimpleName().indexOf("Controller")) + ".fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
