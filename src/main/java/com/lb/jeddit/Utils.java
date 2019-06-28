package com.lb.jeddit;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.dean.jraw.models.Submission;
import org.joda.time.*;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Utils {

	//Format int to use k/m/G eg: 5700 => 5.7k
	public static String formatInt(long value) {
		NavigableMap<Long, String> suffixes = new TreeMap<>();
		suffixes.put(1_000L, "k");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "G");

		//Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
		if (value == Long.MIN_VALUE) return formatInt(Long.MIN_VALUE + 1);
		if (value < 0) return "-" + formatInt(-value);
		if (value < 1000) return Long.toString(value); //deal with easy case

		Map.Entry<Long, String> e = suffixes.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = value / (divideBy / 10); //the number part of the output times 10
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}

	public static String calculateDate(Submission submission) {
		DateTime currentTime = new DateTime(DateTimeZone.UTC);
		DateTime postedTime = new DateTime(submission.getCreated());

		//Show most appropriate time
		String yearDiffP = String.valueOf(Years.yearsBetween(postedTime, currentTime));
		String yearDiff = yearDiffP.substring(1, yearDiffP.length() - 1);
		String authorUploaded = "Posted by " + submission.getAuthor() + " " + yearDiff + " years ago";

		if (Integer.parseInt(yearDiff) == 0) {

			String monthDiffP = String.valueOf(Months.monthsBetween(postedTime, currentTime));
			String monthDiff = monthDiffP.substring(1, monthDiffP.length() - 1);
			authorUploaded = "Posted by " + submission.getAuthor() + " " + monthDiff + " months ago";

			if (Integer.parseInt(monthDiff) == 0) {

				String dayDiffPT = String.valueOf(Days.daysBetween(postedTime, currentTime));
				String dayDiff = dayDiffPT.substring(1, dayDiffPT.length() - 1);
				authorUploaded = "Posted by " + submission.getAuthor() + " " + dayDiff + " days ago";

				if (Integer.parseInt(dayDiff) == 0) {

					String hourDiffPT = String.valueOf(Hours.hoursBetween(postedTime, currentTime));
					String hourDiff = hourDiffPT.substring(2, hourDiffPT.length() - 1);
					authorUploaded = "Posted by " + submission.getAuthor() + " " + hourDiff + " hours ago";

					if (Integer.parseInt(hourDiff) == 0) {

						String minDiffPT = String.valueOf(Minutes.minutesBetween(postedTime, currentTime));
						String minDiff = minDiffPT.substring(2, minDiffPT.length() - 1);
						authorUploaded = "Posted by " + submission.getAuthor() + " " + minDiff + " minutes ago";

						if (Integer.parseInt(minDiff) == 0) {

							String secDiffPT = String.valueOf(Seconds.secondsBetween(postedTime, currentTime));
							String secDiff = secDiffPT.substring(2, secDiffPT.length() - 1);
							authorUploaded = "Posted by " + submission.getAuthor() + " " + secDiff + " seconds ago";

						}
					}
				}
			}
		}

		return authorUploaded;
	}

	public static EventHandler<MouseEvent> requestFocusOnClick() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
							Node node = (Node) mouseEvent.getSource();
							node.requestFocus();
						} catch (Exception e) {
							System.out.println("SOURCE IS NOT A NODE");
							e.printStackTrace();
						}
					}
				});
			}
		};
	}

	public static ImageView roundImageViewCorners(ImageView imageView) {
		imageView.setPreserveRatio(false);
		imageView.setFitWidth(90);
		imageView.setFitHeight(65);

		//Round image
		Rectangle clip = new Rectangle();
		clip.setWidth(imageView.getFitWidth());
		clip.setHeight(imageView.getFitHeight());

		clip.setArcHeight(12);
		clip.setArcWidth(12);
		clip.setStroke(Color.TRANSPARENT);

		imageView.setClip(clip);

		return imageView;
	}

	public static void loadFXML(Object classObj) {
		try {
			FXMLLoader loader = new FXMLLoader(classObj.getClass().getResource("../fxml/" + classObj.getClass().getSimpleName().substring(0, classObj.getClass().getSimpleName().indexOf("Controller")) + ".fxml"));
			loader.setRoot(classObj);
			loader.setController(classObj);
			loader.load();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}



}
