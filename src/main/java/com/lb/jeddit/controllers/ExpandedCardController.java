package com.lb.jeddit.controllers;

import com.lb.jeddit.Events;
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
import net.dean.jraw.ApiException;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.VoteDirection;
import net.dean.jraw.references.SubmissionReference;

public class ExpandedCardController extends VBox {

	/************ FXML IMPORT ************/

	@FXML
	private VBox mainAnchorPane;

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
	private SubmissionReference submissionReference;

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
		authorUploaded.setText("Posted by u/" + submission.getAuthor() + Utils.calculateDate(submission.getCreated()));
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
					Events.openImage(image);
//					OpenImageController openImageController = new OpenImageController(image);
//					openImageController.prefHeightProperty().bind(window.heightProperty());
//					openImageController.prefWidthProperty().bind(window.widthProperty());
//					MainWindowController.getInstance().getContentAnchorPane().getChildren().add(openImageController);
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
				public void handle(MouseEvent mouseEvent) { Events.openPost(submission, ExpandedCardController.this); }
			});

			vBox.setAlignment(Pos.CENTER);
			vBox.setSpacing(10);
			vBox.getChildren().add(imageView);
		}

		if(submission.getVote() == VoteDirection.UP) {
			upVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/up-24-orange.png")));
			upVoteBtn.setOnMouseClicked(cancelVote());
			downVoteBtn.setOnMouseClicked(downVote());
		} else if(submission.getVote() == VoteDirection.DOWN) {
			downVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/down-24-blue.png")));
			upVoteBtn.setOnMouseClicked(cancelVote());
			downVoteBtn.setOnMouseClicked(downVote());
		} else {
			upVoteBtn.setOnMouseClicked(upVote());
			downVoteBtn.setOnMouseClicked(downVote());
		}
	}

//	private void openPost() {
//		OpenPostController openPostController = OpenPostController.createNewInstance(submission, ExpandedCardController.this);
//		openPostController.prefHeightProperty().bind(MainWindowController.getInstance().getContentAnchorPane().heightProperty());
//		openPostController.prefWidthProperty().bind(MainWindowController.getInstance().getContentAnchorPane().widthProperty());
//		MainWindowController.getInstance().getContentAnchorPane().getChildren().add(openPostController);
//	}

	private EventHandler<MouseEvent> cancelVote() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				try {
					if(submissionReference==null) {
						submissionReference = rc.getSubmissionReference(submission.getId());
					}
					upVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/up-24-grey.png")));
					downVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/down-24-grey.png")));
					submissionReference.setVote(VoteDirection.NONE);
					upVoteBtn.setOnMouseClicked(upVote());
					downVoteBtn.setOnMouseClicked(downVote());
				} catch (ApiException apie) {
					//TOO MANY REQUESTS
					Utils.toast("Too many requests.");
				}
			}
		};
	}

	private EventHandler<MouseEvent> upVote() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				try {
					if (submissionReference == null) {
						System.out.println("getting submission reference");
						submissionReference = rc.getSubmissionReference(submission.getId());
					}
					submissionReference.upvote();
					upVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/up-24-orange.png")));
					downVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/down-24-grey.png")));
					upVoteBtn.setOnMouseClicked(cancelVote());
				} catch (ApiException apie) {
					//TOO MANY REQUESTS
					Utils.toast("Too many requests.");
				}
			}
		};
	}

	private EventHandler<MouseEvent> downVote() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if(submissionReference==null) {
					submissionReference = rc.getSubmissionReference(submission.getId());
				}
				submissionReference.downvote();
				upVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/up-24-grey.png")));
				downVoteBtn.setGraphic(new ImageView(new Image("com/lb/jeddit/images/down-24-blue.png")));
				downVoteBtn.setOnMouseClicked(cancelVote());
			}
		};
	}

	public void setOpenPostEvent(boolean bool) {
		if(bool) {
			setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					Events.openPost(submission, ExpandedCardController.this);
				}
			});
		} else {
			setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					//DO NOTHING
				}
			});
		}
	}

}
