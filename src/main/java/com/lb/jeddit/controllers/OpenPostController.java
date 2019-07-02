package com.lb.jeddit.controllers;

import com.lb.jeddit.models.Client;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import net.dean.jraw.models.Submission;
import net.dean.jraw.tree.CommentNode;

public class OpenPostController extends ScrollPane {

	private Submission submission;
	private ExpandedCardController expandedCardController;
	private Client rc = Client.getInstance();

	public OpenPostController(Submission submission) {
		this.submission = submission;
		this.expandedCardController = new ExpandedCardController(submission);
		initialize();
	}

	public OpenPostController(Submission submission, ExpandedCardController expandedCardController) {
		this.expandedCardController = expandedCardController;
		this.submission = submission;
		initialize();
	}

	private void initialize() {
		getStylesheets().add("com/lb/jeddit/css/OpenPost.css");
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setFitToWidth(true);
		setId("scrollPane");
		//setFitToHeight(true);

		VBox vBox = new VBox();
		vBox.getChildren().add(expandedCardController);
		vBox.setFillWidth(true);
		vBox.setAlignment(Pos.CENTER);
		vBox.setMaxHeight(USE_COMPUTED_SIZE);
		setContent(vBox);

		VBox commentsVbox = new VBox();
		commentsVbox.prefWidthProperty().bind(expandedCardController.widthProperty());
		commentsVbox.setSpacing(5);
		commentsVbox.setFillWidth(true);
		expandedCardController.getChildren().add(commentsVbox);

		//Load comments
		for(CommentNode commentNode : rc.getSubmissionComments(submission.getId())) {
			if(!(commentNode.getSubject().getBody()==null)) {
				CommentController commentController = new CommentController(commentNode, OpenPostController.this);
//			commentController.setId(commentNode.getId());
				commentsVbox.getChildren().add(commentController);
//			commentControllerList.add(commentController);
			}
		}

	}


}
