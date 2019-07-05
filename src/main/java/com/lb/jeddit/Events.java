package com.lb.jeddit;

import com.lb.jeddit.controllers.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.dean.jraw.models.Submission;
import net.dean.jraw.references.SubredditReference;

public class Events {

	private static AnchorPane contentAnchorPane = MainWindowController.getInstance().getContentAnchorPane();
	private static VBox contentVBox = ListPostsController.getInstance().getContentVBox();

	private static OpenPostController openPostController;
	private static OpenSubredditController openSubredditController;
	private static OpenImageController openImageController;

	public static void openSubreddit(SubredditReference subredditReference) {
		contentVBox = ListPostsController.getInstance().getContentVBox();
		openSubredditController = OpenSubredditController.createNewInstance(subredditReference);
		openSubredditController.prefHeightProperty().bind(contentVBox.heightProperty());
		openSubredditController.prefWidthProperty().bind(contentVBox.widthProperty());
		contentVBox.getChildren().add(openSubredditController);
	}

	public static void openPost(Submission submission) {
		contentAnchorPane = MainWindowController.getInstance().getContentAnchorPane();
		openPostController = OpenPostController.createNewInstance(submission);
		openPostController.prefHeightProperty().bind(contentAnchorPane.heightProperty());
		openPostController.prefWidthProperty().bind(contentAnchorPane.widthProperty());
		contentAnchorPane.getChildren().add(openPostController);
	}

	public static void openPost(Submission submission, ExpandedCardController expandedCardController) {
		contentAnchorPane = MainWindowController.getInstance().getContentAnchorPane();
		openPostController = OpenPostController.createNewInstance(submission, expandedCardController);
		openPostController.prefHeightProperty().bind(contentAnchorPane.heightProperty());
		openPostController.prefWidthProperty().bind(contentAnchorPane.widthProperty());
		contentAnchorPane.getChildren().add(openPostController);
	}

	public static void openImage(Image image) {
		openImageController = OpenImageController.createNewInstance(image);
		openImageController.prefHeightProperty().bind(contentAnchorPane.heightProperty());
		openImageController.prefWidthProperty().bind(contentAnchorPane.widthProperty());
		contentAnchorPane.getChildren().add(openImageController);
	}

	public static void closeSubreddit() {
		contentVBox = ListPostsController.getInstance().getContentVBox();
		contentVBox.getChildren().remove(openSubredditController);
	}

	public static void closeSubmission() {
		contentAnchorPane = MainWindowController.getInstance().getContentAnchorPane();
		contentAnchorPane.getChildren().remove(openPostController);
	}

	public static void closeImage() {
		contentAnchorPane = MainWindowController.getInstance().getContentAnchorPane();
		contentAnchorPane.getChildren().remove(openImageController);
	}

}
