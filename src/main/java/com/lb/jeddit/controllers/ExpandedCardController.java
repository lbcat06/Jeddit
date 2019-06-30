package com.lb.jeddit.controllers;

import com.lb.jeddit.Utils;
import com.lb.jeddit.models.Client;
import com.lb.jeddit.models.DynamicTextArea;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import net.dean.jraw.models.Submission;

public class ExpandedCardController extends AnchorPane {

	/************ FXML IMPORT ************/

	@FXML
	private AnchorPane mainAnchorPane;

	@FXML
	private AnchorPane infoAnchorPane;

	@FXML
	private VBox vBox;

	@FXML
	private Label score;

	@FXML
	private StackPane imageBorder;

	@FXML
	private Label upVoteBtn;

	@FXML
	private Label downVoteBtn;

	@FXML
	private Label commentCountBtn;

	@FXML
	private Label giveAwardBtn;

	@FXML
	private HBox postInfo;

	/*************************************/

	private Client rc = Client.getInstance();
	private Submission submission;

	public ExpandedCardController(Submission submission) {
		this.submission = submission;
		Utils.loadFXML(this);
	}

	public void initialize() {
		score.setText(Utils.formatInt(submission.getScore()));
		commentCountBtn.setText(Utils.formatInt(submission.getCommentCount()));

		getStylesheets().add("com/lb/jeddit/css/ExpandedCard.css");

		DynamicTextArea title = new DynamicTextArea();
		title.setText(submission.getTitle());
		title.getStyleClass().add("dynamicTextArea");
		title.setId("title");
		title.setComputeHeight(true);
		vBox.getChildren().add(title);

		DynamicTextArea subreddit = new DynamicTextArea();
		subreddit.setText("r/"+submission.getSubreddit() + " • ");
		subreddit.getStyleClass().add("dynamicTextArea");
		subreddit.setId("subreddit");
		subreddit.setComputeWidth(true);

		DynamicTextArea authorUploaded = new DynamicTextArea();
		authorUploaded.setText(Utils.calculateDate(submission));
		authorUploaded.getStyleClass().add("dynamicTextArea");
		authorUploaded.setComputeWidth(true);

		HBox gildings = new HBox();

		postInfo.getChildren().addAll(subreddit, authorUploaded, gildings);

		if(submission.getGilded()==1) {
			if(submission.getGildings().getPlatinums() > 0) {
				ImageView imageView = new ImageView(new Image("com/lb/jeddit/images/platinum.png"));
				imageView.setFitHeight(21);
				imageView.setPreserveRatio(true);
				Label lbl = new Label(submission.getGildings().getGolds() + "");
				lbl.setMinWidth(USE_COMPUTED_SIZE);
				lbl.setMaxWidth(USE_COMPUTED_SIZE);
				lbl.setPrefWidth(USE_COMPUTED_SIZE);
				lbl.setGraphic(imageView);
				lbl.setAlignment(Pos.CENTER);
				lbl.setPadding(new Insets(0, 10, 0, 0));
				gildings.getChildren().add(lbl);
			}
			if (submission.getGildings().getGolds() > 0) {
				ImageView imageView = new ImageView(new Image("com/lb/jeddit/images/gold.png"));
				imageView.setFitHeight(21);
				imageView.setPreserveRatio(true);
				Label lbl = new Label(submission.getGildings().getGolds() + "");
				lbl.setMinWidth(USE_COMPUTED_SIZE);
				lbl.setMaxWidth(USE_COMPUTED_SIZE);
				lbl.setPrefWidth(USE_COMPUTED_SIZE);
				lbl.setGraphic(imageView);
				lbl.setAlignment(Pos.CENTER);
				lbl.setPadding(new Insets(0, 10, 0, 0));
				gildings.getChildren().add(lbl);
			}
			if (submission.getGildings().getSilvers() > 0) {
				ImageView imageView = new ImageView(new Image("com/lb/jeddit/images/silver.png"));
				imageView.setFitHeight(15);
				imageView.setPreserveRatio(true);
				Label lbl = new Label(submission.getGildings().getGolds() + "");
				lbl.getStyleClass().add("gilding");
				lbl.setMinWidth(USE_COMPUTED_SIZE);
				lbl.setMaxWidth(USE_COMPUTED_SIZE);
				lbl.setPrefWidth(USE_COMPUTED_SIZE);
				lbl.setGraphic(imageView);
				lbl.setAlignment(Pos.CENTER);
				lbl.setPadding(new Insets(0, 10, 0, 0));
				gildings.getChildren().add(lbl);
			}
		}

		//Check whether submission has image
		if(!(submission.getPostHint()==null) && submission.getPostHint().equals("image")) {
			Window window = Window.getWindows().get(0);

			Image image = new Image(submission.getUrl());

			ImageView imageView = new ImageView(image);
			imageView.setPreserveRatio(true);
			imageView.setCache(true);
			imageView.setCacheHint(CacheHint.QUALITY);
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					//Add image of original size to screen
					OpenImageController openImageController = new OpenImageController(image);
					openImageController.prefHeightProperty().bind(window.heightProperty());
					openImageController.prefWidthProperty().bind(window.widthProperty());
					MainWindowController.getInstance().getContentAnchorPane().getChildren().add(openImageController);

				}
			});

			if(image.getHeight()>window.getHeight()-130) {
				imageView.fitHeightProperty().bind(window.heightProperty().multiply(0.6));
				if(imageView.getFitWidth()>vBox.getBoundsInLocal().getWidth()) {
					imageView.fitWidthProperty().bind(vBox.widthProperty().multiply(0.9));
				} else if (image.getHeight() - image.getWidth() > 1100) {
					imageView.fitHeightProperty().bind(window.heightProperty().multiply(0.8));
				}
			} else {
				imageView.fitWidthProperty().bind(window.heightProperty().multiply(0.6));
			}

			setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					openPost();
				}
			});

			vBox.setAlignment(Pos.CENTER);
			vBox.setSpacing(10);
			vBox.getChildren().add(imageView);
		}
	}

	private void openPost() {
		OpenPostController openPostController = new OpenPostController(submission, ExpandedCardController.this);
		openPostController.prefHeightProperty().bind(MainWindowController.getInstance().getContentAnchorPane().heightProperty());
		openPostController.prefWidthProperty().bind(MainWindowController.getInstance().getContentAnchorPane().widthProperty());
		MainWindowController.getInstance().getContentAnchorPane().getChildren().add(openPostController);
	}

}
