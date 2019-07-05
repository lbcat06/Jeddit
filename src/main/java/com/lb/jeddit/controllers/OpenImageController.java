package com.lb.jeddit.controllers;

import com.lb.jeddit.Events;
import com.lb.jeddit.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class OpenImageController extends ScrollPane {

	@FXML
	private ImageView imageView;

	@FXML
	private StackPane stackPane;

	@FXML
	private Label closeBtn;

	private Image image;
	private int count = 0;

	private OpenImageController(Image image) {
		this.image = image;
		Utils.loadFXML(this);
		setFitToWidth(true);

		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		imageView.setCache(true);
		imageView.setCacheHint(CacheHint.QUALITY);
		imageView.setFitHeight(image.getHeight());
		imageView.setFitWidth(image.getWidth());

		widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
				if(image.getWidth()>getWidth()) {
					imageView.fitWidthProperty().bind(widthProperty().multiply(0.7));
				} else {
					imageView.fitWidthProperty().unbind();
				}
				if(image.getHeight()>getHeight() && image.getHeight()-image.getWidth()<1000) {
					imageView.fitHeightProperty().bind(heightProperty().multiply(0.8));
				} else {
					stackPane.prefHeightProperty().bind(heightProperty().multiply(0.95));
				}
			}
		});

		closeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				//((Pane)getParent()).getChildren().remove(OpenImageController.this);
				Events.closeImage();
			}
		});
	}

	@Override
	public void layoutChildren() {
		super.layoutChildren();
		if(count<50) {
			if(image.getWidth()>getWidth()) {
				imageView.fitWidthProperty().bind(widthProperty().multiply(0.7));
			} else {
				imageView.fitWidthProperty().unbind();
			}
			if(image.getHeight()>getHeight() && image.getHeight()-image.getWidth()<1000) {
				imageView.fitHeightProperty().bind(heightProperty().multiply(0.8));
			} else {
				stackPane.prefHeightProperty().bind(heightProperty().multiply(0.95));
			}
			count++;
		}
	}


	private static OpenImageController openImageController;
	public static OpenImageController getInstance() {
		if (openImageController == null)
			openImageController = new OpenImageController(null);
		return openImageController;
	}

	public static OpenImageController createNewInstance(Image image) {
		openImageController = new OpenImageController(image);
		return openImageController;
	}


}
