package com.lb.jeddit.models;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class DynamicTextArea extends TextArea {

	private Text text = new Text();
	private final int PADDING = 20;
	private final int LINE_SPACING = 5;
	private boolean computeHeight=false;
	private boolean computeWidth=false;
	private int count=0;

	public DynamicTextArea() {
		setEditable(false);
		setWrapText(true);
		//setPadding(new Insets(-5,0,-10,0));
		getStylesheets().add("com/lb/jeddit/css/DynamicTextArea.css");

		//add changelistener

	}

	private void computeHeight() {
		//SETUP TEXT NODE
		text.setLineSpacing(LINE_SPACING);
		text.setFont(getFont());
		text.setWrappingWidth(getWidth());
		text.setText(getText());

		double textHeight = text.getBoundsInLocal().getHeight();

		setHeight(textHeight);
		setMaxHeight(textHeight);
		setMinHeight(textHeight);
		setPrefHeight(textHeight);
	}

	private void computeWidth() {

		if(getText().isBlank()) {
			setWidth(0);
			setMaxWidth(0);
			setMinWidth(0);
			setPrefWidth(0);
			return;
		}

		text.setFont(getFont());
		text.setText(getText());

		double textWidth = text.getBoundsInLocal().getWidth();

		setWidth(textWidth);
		setMaxWidth(textWidth);
		setMinWidth(textWidth);
		setPrefWidth(textWidth);

		computeHeight();
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		if(count<30) {
			if (computeHeight) {
				computeHeight();
			}
			if(computeWidth) {
				computeWidth();
			}
			count++;
		}

	}

	public void setComputeHeight(boolean computeHeight) {
		this.computeHeight = computeHeight;
		count = 0;
		if(computeHeight) {
			widthProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
					computeHeight();
				}
			});
		}
	}
	public void setComputeWidth(boolean computeWidth) {
		this.computeWidth = computeWidth;
		count = 0;
		if(computeWidth) {
			heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
					computeWidth();
				}
			});
		}
	}
}
