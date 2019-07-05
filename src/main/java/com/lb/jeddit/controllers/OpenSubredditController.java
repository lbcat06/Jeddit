package com.lb.jeddit.controllers;

import com.lb.jeddit.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import net.dean.jraw.models.Subreddit;
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

	/**/

	private Subreddit subreddit;

	private OpenSubredditController(SubredditReference subredditReference) {
		subreddit = subredditReference.about();
		Utils.loadFXML(this);
	}

	public void initialize() {
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
