package com.lb.jeddit.controllers;

import com.lb.jeddit.Utils;
import com.lb.jeddit.models.DynamicTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.references.SubredditReference;

public class OpenSubredditController extends VBox {

	@FXML
	private AnchorPane header;

	@FXML
	private Label subredditName;

	@FXML
	private MenuButton sortMenuButton;

	@FXML
	private AnchorPane toolbar;

	@FXML
	private Label subscribeBtn;

	@FXML
	private Label aboutBtn;

	/**/

	private Subreddit subreddit;
	private SubredditReference subredditReference;

	private OpenSubredditController(SubredditReference subredditReference) {
		this.subredditReference = subredditReference;
		subreddit = subredditReference.about();
		Utils.loadFXML(this);
	}

	public void initialize() {
		getStylesheets().add("com/lb/jeddit/css/OpenSubreddit.css");
		subredditName.setText("r/"+subreddit.getName());

		try {
			Image image = new Image(subreddit.getBannerImage());
			ImageView imageView = new ImageView(image);
			imageView.maxHeight(header.getHeight());
			header.setRightAnchor(imageView, 0.0);
			header.setLeftAnchor(imageView, 0.0);
			header.setBottomAnchor(imageView, 0.0);
			header.setTopAnchor(imageView, 0.0);
			header.getChildren().add(imageView);
		} catch (Exception e) {
			try {
				//DOES NOT HAVE BANNER
				header.setStyle("-fx-background-color: " + subreddit.getKeyColor() + ";");
				subredditName.setStyle("-fx-text-fill: " + Utils.getForeGroundColorBasedOnBGBrightness(Color.web(subreddit.getKeyColor())));
			} catch (Exception e2) {
				//DOES NOT HAVE KEY COLOR
				header.setStyle("-fx-background-color: #232323;");
			}
		}

		//Toolbar
		if(subreddit.isUserSubscriber()) {
			subscribeBtn.setText("UNSUBSCRIBE");
			subscribeBtn.setOnMouseClicked(e -> {
				subredditReference.unsubscribe();
				updateSubscribeBtn();
			});
		} else {
			subscribeBtn.setText("SUBSCRIBE");
			subscribeBtn.setOnMouseClicked(e -> {
				subredditReference.subscribe();
				updateSubscribeBtn();
			});
		}

		aboutBtn.setOnMouseClicked(e -> {
			VBox vBox = ListPostsController.getInstance().getContentVBox();
			vBox.getChildren().remove(1, vBox.getChildren().size());

			if(aboutBtn.getText().equals("ABOUT")) {
				//LOAD ABOUT PAGE
				DynamicTextArea about = new DynamicTextArea();
				about.setText(subreddit.getSidebar());
				about.getStyleClass().add("dynamicTextArea");
				about.setComputeHeight(true);
				about.maxWidthProperty().bind(vBox.widthProperty().multiply(0.8));

				vBox.getChildren().add(about);
				aboutBtn.setText("SUBMISSIONS");
			} else {
				//LOAD SUBMISSIONS
				ListPostsController.getInstance().getSubreddit(subreddit.getName(), SubredditSort.HOT, TimePeriod.ALL);
				aboutBtn.setText("ABOUT");
			}
		});
	}

	//isUserSubscriber boolean does not update, therefore must check using local boolean OR text of subscribeBtn node
	private void updateSubscribeBtn() {
		if(subscribeBtn.getText().equals("SUBSCRIBE")) {
			subscribeBtn.setText("UNSUBSCRIBE");
			subscribeBtn.setOnMouseClicked(e -> {
				subredditReference.unsubscribe();
				updateSubscribeBtn();
			});
		} else {
			subscribeBtn.setText("SUBSCRIBE");
			subscribeBtn.setOnMouseClicked(e -> {
				subredditReference.subscribe();
				updateSubscribeBtn();
			});
		}
	}

	private static OpenSubredditController openSubredditController;
	public static OpenSubredditController getInstance() {
		if (openSubredditController == null)
			openSubredditController = new OpenSubredditController(null);
		return openSubredditController;
	}

	public static OpenSubredditController createNewInstance(SubredditReference subredditReference) {
		openSubredditController = new OpenSubredditController(subredditReference);
		return openSubredditController;
	}

}
