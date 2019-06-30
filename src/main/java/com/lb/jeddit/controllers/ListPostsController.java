package com.lb.jeddit.controllers;

import com.lb.jeddit.models.Client;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;

import java.util.List;

public class ListPostsController extends ScrollPane {

	private Client rc = Client.getInstance();

	private static ListPostsController listPostsController;
	private VBox vBox = new VBox();

	/** CONSTRUCTOR */
	private ListPostsController() {
		loadContent();
	}

	private void loadContent() {
		setContent(vBox);
		setHbarPolicy(ScrollBarPolicy.NEVER);

		//Utils.smoothScrolling(this);

		getStylesheets().add("com/lb/jeddit/css/ListPosts.css");
		setId("scrollPane");

		//Space between each card
		vBox.setSpacing(25);
		vBox.setAlignment(Pos.CENTER);
		vBox.prefWidthProperty().bind(widthProperty());
		vBox.prefHeightProperty().bind(heightProperty());

		//Load more cards on scroll

	}

	/** LOAD CARDS */

	private static final int DEFAULT_ITERATION_COUNT = 5;

	public void getFrontPage(SubredditSort subredditSort, TimePeriod timePeriod, boolean loadingNew) {
		clearPosts();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=1; i<DEFAULT_ITERATION_COUNT; i++) {
					createSubmissionCard(rc.getFrontPage(i, subredditSort, timePeriod));
				}
			}
		}).start();
	}

	private void createSubmissionCard(List<Submission> submissionList) {
		try {

			for(Submission submission : submissionList) {
				ExpandedCardController expandedCardController = new ExpandedCardController(submission);
				//expandedCardController.setOnMouseClicked(openPost(submission));

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						vBox.getChildren().add(expandedCardController);
					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	//Clear VBox containing posts
	public void clearPosts() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vBox.getChildren().clear();
			}
		});
	}

	/** EVENTS */

	/** GETTERS & SETTERS */

	public static ListPostsController getInstance() {
		if (listPostsController == null)
			listPostsController = new ListPostsController();
		return listPostsController;
	}

	public static ListPostsController createNewInstance() {
		listPostsController = new ListPostsController();
		return listPostsController;
	}
}
