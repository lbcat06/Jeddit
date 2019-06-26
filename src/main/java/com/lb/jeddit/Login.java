package com.lb.jeddit;

import com.lb.jeddit.controllers.LoginWindowController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.oauth.StatefulAuthHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.UUID;
import java.util.logging.Level;

//import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Login {

	private LoginWindowController lc = LoginWindowController.getInstance();
	private static Login login;

	private String username;
	private String password;
	private String authUrl;

	//Change scene
	private boolean rememberMe;
	private boolean loginSuccessful=false;
	private Label statusLabel;

	private StatefulAuthHelper helper;
	private RedditClient redditClient;
	private NetworkAdapter networkAdapter;
	private Credentials credentials;

	//Count how many times to retry login on fail
	private int count = 0;

	//Create headerless browser
	private WebDriver driver = new HtmlUnitDriver();

	private static String[] scopes = {
			"identity",
			"edit",
			"flair",
			"history",
			"mysubreddits",
			"privatemessages",
			"read",
			"report",
			"save",
			"submit",
			"subscribe",
			"vote",
			"wikiread"
	};

	/** CONSTRUCTOR */
	public Login(String username, String password, boolean rememberMe, Label statusLabel) {
		//Status connecting to server
		login = this;
		//lc.connectingToServer();
		this.username = username;
		this.password = password;
		this.rememberMe = rememberMe;
		this.statusLabel = statusLabel;

		createConnection();
	}

	private void createConnection() {
		//Status
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText("connecting to server...");
			}
		});

		//Suppress htmlunit warnings
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

		//Set user agent
		UserAgent userAgent = new UserAgent("bot", "com.example.usefulbot", "v0.1", username);

		//Create network adapter
		networkAdapter = new OkHttpNetworkAdapter(userAgent);

		//Set app credentials
		credentials = Credentials.installedApp("rL110m980Ojugg", "https://reddit.com");

		//Create authHelper for interactive app
		helper = OAuthHelper.interactive(networkAdapter, credentials);

		//Create authUrl and use in authorization
		authUrl = helper.getAuthorizationUrl(true, true, scopes);

		//Login method
		login();

	}

	private void login() {
		//Status logging in
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					//Wait 0.5 sec extra as a buffer for ui to load
					Thread.sleep(500);
					statusLabel.setText("logging in...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		//Load login page
		driver.get(authUrl);

		findMainElements();

		//If invalid login
		try {
			if(loginSuccessful) {
				challengeLogin();
			} else {
				return;
			}
		} catch (Exception e) {
			count=0;
			return;
		}

		//Change Scene
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
//				if(changeSceneBool) {
//					//lc.changeScene();
//				}
			}
		});
	}

	private void findMainElements() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText("fetching elements...");
			}
		});

		//Find elements
		try {
			if(count<3) {
				WebElement usernameElement = driver.findElement(By.id("user_login"));
				WebElement passwordElement = driver.findElement(By.id("passwd_login"));

				//Convert username and password to charsequence
				CharSequence usernameChar = username;
				CharSequence passwordChar = password;

				//Enter chars into fields
				usernameElement.sendKeys(usernameChar);
				passwordElement.sendKeys(passwordChar);

				//Submit username and password
				passwordElement.submit();
			}
		} catch (NoSuchElementException nsee) {
			count++;
			findMainElements();
			return;
		}

//			//Check if failed, redo up to 3 times, if exceeds 3 times login is invalid
//		while(driver.getCurrentUrl().equals("https://www.reddit.com/post/login.compact") && count<3) {
//
//			//Buffer
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			count++;
//			driver.get(authUrl);
//			findMainElements();
//
//			if(count==4) {
//				//Unsuccessful Login
//				Platform.runLater(new Runnable() {
//					@Override
//					public void run() {
//						statusLabel.setText("cannot reach reddit after " + count + " attempts");
//					}
//				});
//			}
//
//			return;
//		}

		//authorizing Login
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText("authorizing login...");
			}
		});

		try {
			//Find authorize application(jeddit) button / trycatch since if not found meaning unsuccessful login
			WebElement authorize = driver.findElement(By.name("authorize"));
			authorize.submit();

			loginSuccessful=true;

		} catch (NoSuchElementException e) {
			//status Unsuccessful Login
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					statusLabel.setText("invalid details");
				}
			});
		} catch (IllegalStateException ise) {
			//status Unsuccessful Login
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					statusLabel.setText("cannot connect to reddit after " + count + " attempts");
				}
			});
		}
	}

	private void challengeLogin() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText("challenging login...");
			}
		});

		//Get code from redirect URI
		String url = driver.getCurrentUrl();
		redditClient = helper.onUserChallenge(url);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText("successful login");
			}
		});

		//Close connection
		driver.quit();
	}

	private void saveToFile() {
		redditClient.getAuthManager().getCurrent();
		redditClient.getAuthManager().getTokenStore();


		AccountHelper helper2 = new AccountHelper(networkAdapter, credentials, redditClient.getAuthManager().getTokenStore(), UUID.randomUUID());
	}

	/** GETTERS & SETTERS */
	public static Login getInstance() {
		return login;
	}

	public RedditClient getRedditClient() {
		return redditClient;
	}

}
