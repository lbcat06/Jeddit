package com.lb.jeddit.controllers;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.dean.jraw.models.Submission;

public class OpenPostController extends ScrollPane {

	private Submission submission;
	private ExpandedCardController expandedCardController;

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
		setFitToHeight(true);

		AnchorPane anchorPane = new AnchorPane();
		anchorPane.getChildren().add(expandedCardController);
		anchorPane.setRightAnchor(expandedCardController, 100.0);
		anchorPane.setLeftAnchor(expandedCardController, 100.0);
		setContent(anchorPane);

		VBox commentsVbox = new VBox();
		expandedCardController.getChildren().add(commentsVbox);


	}


}
