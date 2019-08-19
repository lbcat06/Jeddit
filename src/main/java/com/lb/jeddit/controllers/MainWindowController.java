package com.lb.jeddit.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.lb.jeddit.Utils;
import com.lb.jeddit.models.Client;
import com.lb.jeddit.models.DynamicTextArea;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.jfoenix.controls.JFXScrollPane.smoothScrolling;

public class MainWindowController extends AnchorPane {

	/************ FXML IMPORT ************/

	@FXML
	private AnchorPane mainAnchorPane;

	@FXML
	private AnchorPane contentAnchorPane;

	@FXML
	private TextField searchTextField;

	@FXML
	private Button homeButton;

	@FXML
	private JFXHamburger hamburger;

	@FXML
	private JFXDrawer drawer;

	@FXML
	private VBox drawerContent;

	@FXML
	private VBox drawerSubs;

	@FXML
	private ScrollPane drawerScrollPane;

	@FXML
	private MenuButton menuButton;

	@FXML
	private TextField searchSubreddit;

	/*************************************/

	private ListPostsController listPostsController = ListPostsController.getInstance();
	private Client rc = Client.getInstance();
	private static MainWindowController mainWindowController;

	public MainWindowController() {
		Utils.loadFXML(this);
	}

	public void initialize() {
		getStylesheets().add("com/lb/jeddit/css/MainWindow.css");
		//Search bar event on 'enter' key press
//		searchTextField.setOnKeyPressed(searchOnEnterEvent(""));
		//Start on listPostController
		contentAnchorPane.getChildren().add(listPostsController);


		//START UP
		new Thread(new Runnable() {
			@Override
			public void run() {
				addSubscriptions();
				listPostsController.getFrontPage(SubredditSort.BEST, TimePeriod.ALL, false);
			}
		}).start();



		listPostsController.prefWidthProperty().bind(contentAnchorPane.widthProperty());
		listPostsController.prefHeightProperty().bind(contentAnchorPane.heightProperty());

		//Open Settings on button press
		homeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
//				listPostsController.clearPosts();
//				listPostsController.getResults("", 1, "");
			}
		});


		/** DRAWER */

		//Create Hamburger
		drawer.setSidePane(drawerContent);
		drawerScrollPane.setFitToWidth(true);

		//Decline click evenets when closed
		drawer.setOnDrawerClosing(new EventHandler<JFXDrawerEvent>() {
			@Override
			public void handle(JFXDrawerEvent jfxDrawerEvent) {
				drawer.setMouseTransparent(true);
			}
		});

		//Accept click events when open
		drawer.setOnDrawerOpening(new EventHandler<JFXDrawerEvent>() {
			@Override
			public void handle(JFXDrawerEvent jfxDrawerEvent) {
				drawer.setMouseTransparent(false);
			}
		});

		drawer.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				drawer.close();
			}
		});

		//Open menu
		hamburger.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				drawer.open();
			}
		});

		/** ACCOUNT COMBO BOX */

		//Main menuButton
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<1; i++) {
					//System.out.println("LOOP OUTSIDE IF");
//					if(!rc.getDoneFlag()) {
//						i--;
//
//						//Buffer
//						try {
//							Thread.sleep(200);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
				}
//				Label mainLbl = new Label(rc.getUsername() + " \n" + rc.getTotalKarma() + " Karma");
//				Platform.runLater(new Runnable() {
//					@Override
//					public void run() {
//						menuButton.setGraphic(mainLbl);
//					}
//				});
			}
		}).start();

		//Log out
		Label logout = new Label(" Logout");
		SVGPath logoutSvg = new SVGPath();
		logoutSvg.setContent("M15,2 L5,2 C4.447,2 4,2.447 4,3 L4,9 L9.586,9 L8.293,7.707 C7.902,7.316 7.902,6.684 8.293,6.293 C8.684,5.902 9.316,5.902 9.707,6.293 L12.707,9.293 C13.098,9.684 13.098,10.316 12.707,10.707 L9.707,13.707 C9.512,13.902 9.256,14 9,14 C8.744,14 8.488,13.902 8.293,13.707 C7.902,13.316 7.902,12.684 8.293,12.293 L9.586,11 L4,11 L4,17 C4,17.553 4.447,18 5,18 L15,18 C15.553,18 16,17.553 16,17 L16,3 C16,2.447 15.553,2 15,2");
		logoutSvg.setFill(Paint.valueOf("eeeef2"));
		logout.setGraphic(logoutSvg);

		Label settings = new Label(" Settings");
		SVGPath settingsSvg = new SVGPath();
		settingsSvg.setContent("M 11.423828 2 C 11.179828 2 10.969688 2.1769687 10.929688 2.4179688 L 10.646484 4.1230469 C 10.159736 4.2067166 9.689176 4.3360771 9.2363281 4.5039062 L 8.1347656 3.1679688 C 7.9797656 2.9789688 7.7100469 2.9297344 7.4980469 3.0527344 L 6.5019531 3.6289062 C 6.2899531 3.7509062 6.1972031 4.0083281 6.2832031 4.2363281 L 6.8886719 5.8535156 C 6.513238 6.1663963 6.1663963 6.513238 5.8535156 6.8886719 L 4.2363281 6.2832031 C 4.0083281 6.1972031 3.7509062 6.2899531 3.6289062 6.5019531 L 3.0527344 7.4980469 C 2.9297344 7.7100469 2.9789688 7.9797656 3.1679688 8.1347656 L 4.5039062 9.2363281 C 4.3360771 9.689176 4.2067166 10.159736 4.1230469 10.646484 L 2.4179688 10.929688 C 2.1769687 10.970688 2 11.178828 2 11.423828 L 2 12.576172 C 2 12.820172 2.1769687 13.030312 2.4179688 13.070312 L 4.1230469 13.353516 C 4.2067166 13.840264 4.3360771 14.310824 4.5039062 14.763672 L 3.1679688 15.865234 C 2.9789687 16.020234 2.9307344 16.289953 3.0527344 16.501953 L 3.6289062 17.498047 C 3.7509062 17.710047 4.0083281 17.802797 4.2363281 17.716797 L 5.8535156 17.111328 C 6.1663963 17.486762 6.513238 17.833604 6.8886719 18.146484 L 6.2832031 19.763672 C 6.1972031 19.992672 6.2909531 20.249094 6.5019531 20.371094 L 7.4980469 20.947266 C 7.7100469 21.069266 7.9797656 21.020031 8.1347656 20.832031 L 9.234375 19.496094 C 9.6877476 19.664236 10.15912 19.793178 10.646484 19.876953 L 10.929688 21.582031 C 10.970688 21.823031 11.178828 22 11.423828 22 L 12.576172 22 C 12.820172 22 13.030312 21.823031 13.070312 21.582031 L 13.353516 19.876953 C 13.840264 19.793283 14.310824 19.663923 14.763672 19.496094 L 15.865234 20.832031 C 16.020234 21.021031 16.289953 21.069266 16.501953 20.947266 L 17.498047 20.371094 C 17.710047 20.249094 17.802797 19.991672 17.716797 19.763672 L 17.111328 18.146484 C 17.486762 17.833604 17.833604 17.486762 18.146484 17.111328 L 19.763672 17.716797 C 19.992672 17.802797 20.249094 17.709047 20.371094 17.498047 L 20.947266 16.501953 C 21.069266 16.289953 21.020031 16.020234 20.832031 15.865234 L 19.496094 14.765625 C 19.664236 14.312252 19.793178 13.84088 19.876953 13.353516 L 21.582031 13.070312 C 21.823031 13.029312 22 12.821172 22 12.576172 L 22 11.423828 C 22 11.179828 21.823031 10.969688 21.582031 10.929688 L 19.876953 10.646484 C 19.793283 10.159736 19.663923 9.689176 19.496094 9.2363281 L 20.832031 8.1347656 C 21.021031 7.9797656 21.069266 7.7100469 20.947266 7.4980469 L 20.371094 6.5019531 C 20.249094 6.2899531 19.991672 6.1972031 19.763672 6.2832031 L 18.146484 6.8886719 C 17.833604 6.513238 17.486762 6.1663963 17.111328 5.8535156 L 17.716797 4.2363281 C 17.802797 4.0073281 17.709047 3.7509062 17.498047 3.6289062 L 16.501953 3.0527344 C 16.289953 2.9307344 16.020234 2.9799687 15.865234 3.1679688 L 14.765625 4.5039062 C 14.312252 4.3357635 13.84088 4.2068225 13.353516 4.1230469 L 13.070312 2.4179688 C 13.029312 2.1769687 12.821172 2 12.576172 2 L 11.423828 2 z M 11 6.0898438 L 11 9.1738281 A 3 3 0 0 0 9 12 A 3 3 0 0 0 9.0507812 12.548828 L 6.3789062 14.089844 C 6.1382306 13.438833 6 12.736987 6 12 C 6 9.0161425 8.1553612 6.5637988 11 6.0898438 z M 13 6.0898438 C 15.844639 6.5637988 18 9.0161425 18 12 C 18 12.737875 17.86037 13.440133 17.619141 14.091797 L 14.947266 12.546875 A 3 3 0 0 0 15 12 A 3 3 0 0 0 13 9.1757812 L 13 6.0898438 z M 13.947266 14.277344 L 16.628906 15.826172 C 15.530388 17.156023 13.868625 18 12 18 C 10.131375 18 8.4696124 17.156023 7.3710938 15.826172 L 10.050781 14.279297 A 3 3 0 0 0 12 15 A 3 3 0 0 0 13.947266 14.277344 z");
		settingsSvg.setFill(Paint.valueOf("eeeef2"));
		settings.setGraphic(settingsSvg);

		Label myAccount = new Label(" My Account");
		SVGPath accountSvg = new SVGPath();
		accountSvg.setContent("M146.8 142.6h-37.6c-31.1 0-56.5 25.3-56.5 56.5 0 5.2 4.2 9.4 9.4 9.4h131.8c5.2 0 9.4-4.2 9.4-9.4 0-31.2-25.3-56.5-56.5-56.5zM128 130.7c20.1 0 36.4-16.3 36.4-36.4v-9.4c0-20.1-16.3-36.4-36.4-36.4S91.6 64.8 91.6 84.9v9.4c0 20.1 16.3 36.4 36.4 36.4z");
		accountSvg.setFill(Paint.valueOf("eeeef2"));
		myAccount.setGraphic(accountSvg);

		//logout.setPrefWidth(161);

		//menuButton.setGraphic(mainLbl);

		MenuItem myAccountMenuItem = new MenuItem();
		myAccountMenuItem.setGraphic(myAccount);

		MenuItem settingsMenuItem = new MenuItem();
		settingsMenuItem.setGraphic(settings);

		SeparatorMenuItem separator = new SeparatorMenuItem();
		separator.setId("separator");

		MenuItem logoutMenuItem = new MenuItem();
		logoutMenuItem.setGraphic(logout);
//		logoutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent actionEvent) {
//				openLogin.run();
//			}
//		});

		menuButton.getItems().add(myAccountMenuItem);
		menuButton.getItems().add(settingsMenuItem);
		menuButton.getItems().add(separator);
		menuButton.getItems().add(logoutMenuItem);

		menuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if(!menuButton.isShowing()) {
					contentAnchorPane.requestFocus();
				}
			}
		});

		menuButton.setOnHidden(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				contentAnchorPane.requestFocus();
			}
		});


		//Focused on background on startup
		mainAnchorPane.requestFocus();
		mainAnchorPane.setOnMouseClicked(Utils.requestFocusOnClick());

		//Search through subscribed subreddits
		searchSubreddit.textProperty().addListener(searchSubreddit());
	}

	private ChangeListener<String> searchSubreddit() {
		return new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

				//If empty re-order in original order
				if(searchSubreddit.getText().isBlank()) {
					drawerSubs.getChildren().clear();
					for(Button btn : subscriptions) {
						drawerSubs.getChildren().add(btn);
					}
					return;
				}

				for(Button btn : subscriptions) {

					//Remove if does not match what user is typing
					if(!(btn.getText().contains(searchSubreddit.getText()))) {
						drawerSubs.getChildren().remove(btn);
					}

					//If matches what user is typing but not currently present, add it.
					else if(btn.getText().contains(searchSubreddit.getText()) &&
							!drawerSubs.getChildren().contains(btn)) {
						drawerSubs.getChildren().add(btn);
					}

				}
			}
		};
	}

	List<Button> subscriptions = new ArrayList<>();

	//Add subscriptions to drawer
	private void addSubscriptions() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Subreddit> subredditList = rc.getSubscriptions();

				for(Subreddit subreddit : subredditList) {
					Button btn = new Button();
					btn.setText("r/"+subreddit.getName());
					btn.prefWidthProperty().bind(drawerSubs.widthProperty());
					btn.setAlignment(Pos.CENTER_LEFT);
					btn.setOnMouseClicked(e -> listPostsController.getSubreddit(subreddit.getName(), SubredditSort.HOT, TimePeriod.ALL));
					btn.setId("subsBtn");
					subscriptions.add(btn);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							drawerSubs.getChildren().add(btn);
							drawerSubs.setSpacing(3);
							drawerSubs.setPadding(new Insets(0, 0, 0, 11));
						}
					});
				}
			}
		}).start();

		smoothScrolling(drawerScrollPane);
	}

	public void toast(String toast) {
		DynamicTextArea dynamicTextArea = new DynamicTextArea();

		//ANCHOR TOAST TO BOTTOM RIGHT OF APPLICATION
		mainWindowController.getChildren().add(dynamicTextArea);
		mainWindowController.setRightAnchor(dynamicTextArea, 50.0);
		mainWindowController.setBottomAnchor(dynamicTextArea, 50.0);

		dynamicTextArea.setText(toast);
		dynamicTextArea.setId("toast");
		dynamicTextArea.addPadding(20);
		dynamicTextArea.setComputeHeight(true);

		//REMOVE TOAST AFTER 10 SECONDS
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					mainWindowController.getChildren().remove(dynamicTextArea);
				});
			}
		}, 10*1000);
	}


	public AnchorPane getContentAnchorPane() {
		return contentAnchorPane;
	}

	public static MainWindowController getInstance() {
		if (mainWindowController == null)
			mainWindowController = new MainWindowController();
		return mainWindowController;
	}

	public static MainWindowController createNewInstance() {
		mainWindowController = new MainWindowController();
		return mainWindowController;
	}


}
