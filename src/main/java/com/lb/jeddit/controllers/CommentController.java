package com.lb.jeddit.controllers;

import com.lb.jeddit.Utils;
import com.lb.jeddit.models.DynamicTextArea;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import net.dean.jraw.tree.CommentNode;

import java.util.ArrayList;
import java.util.List;


public class CommentController extends HBox {

	/************ FXML IMPORT ************/

	@FXML
	private VBox commentUserInfo;

	@FXML
	private Region indent;

	@FXML
	private Rectangle rectangle;

	@FXML
	private HBox tools;

	@FXML
	private Label replyBtn;

	@FXML
	private Label giveAwardBtn;

	/*************************************/

	private CommentNode comment;
	private List<String> childrenIds = new ArrayList<>();
	private int index;

	public CommentController(CommentNode comment, int index) {
		this.comment = comment;
		this.index = index;
		Utils.loadFXML(this);
	}


	public void initialize() {
		getStylesheets().add("com/lb/jeddit/css/Comment.css");
		setId("root");
		int depth = comment.getDepth();

		indent.setMaxWidth((depth * 15));
		indent.setMinWidth((depth * 15));
		indent.setPrefWidth((depth * 15));

		HBox userInfo = new HBox();

		//Author
		DynamicTextArea author = new DynamicTextArea();
		author.setText("u/"+this.comment.getSubject().getAuthor());
		author.getStyleClass().add("dynamicTextArea");
		author.setComputeWidth(true);
		userInfo.getChildren().add(author);

		//Score
		DynamicTextArea score = new DynamicTextArea();
		if(comment.getSubject().getScore()==1) {
			score.appendText("   " + comment.getSubject().getScore() + " point");
		} else {
			score.appendText("   " + comment.getSubject().getScore() + " points");
		}
		score.setText(score.getText() + " • " + Utils.calculateDate(comment.getSubject().getCreated()));
		score.getStyleClass().add("dynamicTextArea");
		score.setComputeWidth(true);
		userInfo.getChildren().add(score);

		//Comment
		DynamicTextArea comment = new DynamicTextArea();
		comment.setText(this.comment.getSubject().getBody());
		comment.getStyleClass().add("dynamicTextArea");
		comment.setComputeHeight(true);

		//Coloured Rectangle
		rectangle.heightProperty().bind(heightProperty());
		rectangle.setOnMouseClicked(childrenEvent());
		rectangle.setWidth(5);

		for(double i=0; i<depth; i+=9) {
			if(depth==1+i) {
				rectangle.setStyle("-fx-fill: #ed190e;" +
						"-fx-cursor: hand;"); //RED
			} else if (depth==2+i) {
				rectangle.setStyle("-fx-fill: #ef6c15;" +
						"-fx-cursor: hand;"); //ORANGE
			} else if (depth==3+i) {
				rectangle.setStyle("-fx-fill: #efef15;" +
						"-fx-cursor: hand;"); //YELLOW
			} else if (depth==4+i) {
				rectangle.setStyle("-fx-fill: #82ef15;" +
						"-fx-cursor: hand;"); //GREEN
			} else if (depth==5+i) {
				rectangle.setStyle("-fx-fill: #15efd9;" +
						"-fx-cursor: hand;"); //TURQUOISE
			} else if (depth==6+i) {
				rectangle.setStyle("-fx-fill: #15bcef;" +
						"-fx-cursor: hand;"); //LIGHT BLUE
			} else if (depth==7+i) {
				rectangle.setStyle("-fx-fill: #1527ef;" +
						"-fx-cursor: hand;"); //DARK BLUE
			} else if (depth==8+i) {
				rectangle.setStyle("-fx-fill: #7415ef;" +
						"-fx-cursor: hand;"); //PURPLE
			} else if (depth==9+i) {
				rectangle.setStyle("-fx-fill: #ef15d2;" +
						"-fx-cursor: hand;"); //PINK
			}
		}

		Platform.runLater(() -> {
			int indexOfTools = commentUserInfo.getChildren().indexOf(tools);
			commentUserInfo.getChildren().add(indexOfTools, comment);
			int indexOfComment = commentUserInfo.getChildren().indexOf(comment);
			commentUserInfo.getChildren().add(indexOfComment, userInfo);
		});

		//Fit to size
		userInfo.prefHeightProperty().bind(author.heightProperty());
		commentUserInfo.setPrefHeight(USE_COMPUTED_SIZE);
		prefHeightProperty().bind(commentUserInfo.heightProperty());
	}

	//Open post on click event
	private EventHandler<MouseEvent> childrenEvent() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Platform.runLater(() -> {
					collapseComment();
					hideChildren(index + 1);
				});
			}
		};
	}

	private void hideChildren(int currentIndex) {
		CommentController child = OpenPostController.getInstance().getCommentControllerList().get(currentIndex);
		if(child.comment.getDepth()>comment.getDepth()) {
			((VBox)child.getParent()).getChildren().remove(child);
			hideChildren(currentIndex+1);
		}
	}

	private void collapseComment() {
		commentUserInfo.getChildren().remove(1, commentUserInfo.getChildren().size());
		setHeight(USE_COMPUTED_SIZE);
	}

}
