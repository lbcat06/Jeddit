package com.lb.jeddit.controllers;

import com.lb.jeddit.Utils;
import com.lb.jeddit.models.DynamicTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
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

		//subscribe btn
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

		//about btn
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

		//sort menu
		Label risingSearch = new Label(" Rising");
		SVGPath risingSvg = new SVGPath();
		risingSvg.setContent("M18.5 4h-6a.5.5 0 0 0-.35.85l1.64 1.65-3.29 3.29L8.21 7.5a1 1 0 0 0-1.41 0L.65 13.65a.5.5 0 0 0 0 .71l2 2a.5.5 0 0 0 .71 0l4.14-4.15 2.29 2.29a1 1 0 0 0 1.41 0l5.3-5.29 1.65 1.65a.5.5 0 0 0 .85-.36v-6a.5.5 0 0 0-.5-.5z");
		risingSvg.setFill(Paint.valueOf("eeeef2"));
		risingSearch.setGraphic(risingSvg);
		MenuItem risingMenuItem = new MenuItem();
		risingMenuItem.setGraphic(risingSearch);

		Label topSearch = new Label(" Top");
		SVGPath topSvg = new SVGPath();
		topSvg.setContent("M1.25,17.5 L1.25,7.5 L6.25,7.5 L6.25,17.5 L1.25,17.5 Z M12.49995,17.5001 L7.49995,17.5001 L7.49995,5.0001 L4.99995,5.0001 L9.99995,0.0006 L14.99995,5.0001 L12.49995,5.0001 L12.49995,17.5001 Z M13.75,17.5 L13.75,12.5 L18.75,12.5 L18.75,17.5 L13.75,17.5 Z");
		topSvg.setFill(Paint.valueOf("eeeef2"));
		topSearch.setGraphic(topSvg);
		MenuItem topMenuItem = new MenuItem();
		topMenuItem.setGraphic(topSearch);

		Label controversialSearch = new Label(" Controversial");
		SVGPath controversialSvg = new SVGPath();
		controversialSvg.setContent("M16,0L7.25 0 3.5 10.108 8.5 10.108 4.475 20 16 8 11 8z");
		controversialSvg.setFill(Paint.valueOf("eeeef2"));
		controversialSearch.setGraphic(controversialSvg);
		MenuItem controversialMenuItem = new MenuItem();
		controversialMenuItem.setGraphic(controversialSearch);

		Label hotSearch = new Label(" Hot");
		SVGPath hotSvg = new SVGPath();
		hotSvg.setContent("M10.31.61a.5.5,0,0,0-.61,0C9.41.83,2.75,6.07,2.75,11.47a8.77,8.77,0,0,0,3.14,6.91.5.5,0,0,0,.75-.64,3.84,3.84,0,0,1-.55-2A7.2,7.2,0,0,1,10,9.56a7.2,7.2,0,0,1,3.9" + "1,6.23,3.84,3.84,0,0,1-.55,2,.5.5,0,0,0,.75.64,8.77,8.77,0,0,0,3.14-6.91C17.25,6.07,10.59.83,10.31.61Z");
		hotSvg.setFill(Paint.valueOf("eeeef2"));
		hotSearch.setGraphic(hotSvg);
		MenuItem hotMenuItem = new MenuItem();
		hotMenuItem.setGraphic(hotSearch);

		Label newSearch = new Label(" New");
		SVGPath newSvg = new SVGPath();
		newSvg.setContent("M17.16,10L19.07 12.936 15.799 14.188 15.619 17.686 12.237 16.776 10.035 19.5 7.833 16.776 4.451 17.686 4.271 14.188 1 12.936 2.91 10 1 7.065 4.271 5.812 4.451 2.315 7.833 3.224 10.035 .5 12.237 3.224 15.619 2.315 15.799 5.812 19.07 7.065z");
		newSvg.setFill(Paint.valueOf("eeeef2"));
		newSearch.setGraphic(newSvg);
		MenuItem newMenuItem = new MenuItem();
		newMenuItem.setGraphic(newSearch);

		sortMenuButton.getItems().addAll(hotMenuItem, newMenuItem, controversialMenuItem, topMenuItem, risingMenuItem);
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

	//Singleton
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
