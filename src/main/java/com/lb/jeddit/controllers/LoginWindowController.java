package com.lb.jeddit.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.lb.jeddit.Login;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginWindowController extends AnchorPane {

	/* IMPORT FXML */
	@FXML
	private JFXCheckBox rememberCheckbox;
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label statusLabel;
	@FXML
	private Button loginBtn;
	@FXML
	private Label skipLogin;
	//------------------------

	private static LoginWindowController loginWindowController;

	public LoginWindowController() {
		//Load FXML file
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/" + getClass().getSimpleName().substring(0, getClass().getSimpleName().indexOf("Controller")) + ".fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void initialize() {
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				requestFocus();
			}
		});

		addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			switch(e.getCode()) {
				case ESCAPE:
					System.exit(0);
					break;
				case ENTER:
					Login login = new Login(usernameTextField.getText(), passwordField.getText(), !rememberCheckbox.isDisable(), statusLabel);
					break;
			}
		});

		statusLabel.textProperty().addListener(loginStatus());
		loginBtn.setOnMouseClicked(loginEvent());
		skipLogin.setOnMouseClicked(userlessLogin());
	}

	/* Events */

	/* Login button pressed */
	private EventHandler<MouseEvent> loginEvent() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Login login = new Login(usernameTextField.getText(), passwordField.getText(), !rememberCheckbox.isDisable(), statusLabel);
					}
				}).start();
				loginBtn.setDisable(true);
			}
		};
	}

	/* Listen for text changes (status of login) from com.lb.jeddit.Login */
	private ChangeListener<String> loginStatus() {
		return new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
				if(statusLabel.getText().equals("invalid details") || statusLabel.getText().contains("cannot reach reddit after")) {
					loginBtn.setDisable(false);
				} else if (statusLabel.getText().equals("successful login")) {
					//CHANGE SCENE
				}
			}
		};
	}

	/* Listen for userless mode */
	private EventHandler<MouseEvent> userlessLogin() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				//CHANGE SCENE
			}
		};
	}

//	/* Enter pressed assuming both fields are filled */
//	private EventHandler<KeyEvent> loginEventKey() {
//		return new EventHandler<KeyEvent>() {
//			@Override
//			public void handle(KeyEvent keyEvent) {
//				switch(keyEvent.getCode()) {
//					case ESCAPE:
//						System.exit(1);
//						break;
//					case ENTER:
//						loginBtn.fire();
//						break;
//				}
//			}
//		};
//	}

	/* Singleton */
	public static LoginWindowController getInstance() {
		if (loginWindowController == null)
			loginWindowController = new LoginWindowController();
		return loginWindowController;
	}

	/* Getters & Setters */
	public Label getStatusLabel() {
		return statusLabel;
	}
}
