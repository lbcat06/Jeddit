package com.lb.jeddit;

import com.lb.jeddit.models.Client;
import javafx.application.Platform;
import javafx.scene.control.Label;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Login {

	//private LoginWindowController lc = LoginWindowController.getInstance();
	private static Login login;

	private String username, password, authUrl;

	//Change scene
	private boolean rememberMe;
	private Label statusLabel;

	private StatefulAuthHelper helper;
	private RedditClient redditClient;
	private NetworkAdapter networkAdapter;
	private Credentials credentials;

	//Count how many times to retry login on fail
	//private int count = 0;

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
		//login = this;
		this.username = username;
		this.password = password;
		this.rememberMe = rememberMe;
		this.statusLabel = statusLabel;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText("logging in...");
			}
		});

		createConnection();
		login();
	}

	//Saved data
	public Login(String username) {
		this.username = username;
		createConnection();
		readFromFile();
		Client.getInstance().setRedditClient(redditClient);
	}

	private void createConnection() {
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
	}

	private void login() {
		WebDriver driver = new HtmlUnitDriver();

		//Suppress htmlunit warnings
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

		setStatus("fetching elements...");

		//Load web page
		driver.get(authUrl);

		//Find login elements
//		try {
//			if(count<3) {
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
//			}
//		} catch (NoSuchElementException nsee) {
//			count++;
//			login();
//			return;
//		}

		setStatus("authorizing login...");

		try {
			//Find authorize application(jeddit) button / trycatch since if not found meaning unsuccessful login
			WebElement authorize = driver.findElement(By.name("authorize"));
			authorize.submit();
		} catch (NoSuchElementException e) {
			setStatus("invalid details");
		} catch (IllegalStateException ise) {
			setStatus("cannot connect to reddit");
		}

		setStatus("challenging login...");

		//Get code from redirect URI
		String url = driver.getCurrentUrl();
		redditClient = helper.onUserChallenge(url);
		driver.quit();

		setStatus("successful login");

		if(rememberMe) {
			saveToFile();
			accounts.set(0, redditClient.me().getUsername());
		}
	}

	private List<String> accounts = new ArrayList<>();

	private void readFromFile() {
		//Create token store
		JsonFileTokenStore tokenStore = new JsonFileTokenStore(new File(System.getProperty("user.dir") + "/src/main/resources/com/lb/jeddit/persistent/"+username+"token.json"));

		//Load token store information
		tokenStore.load();

		//Create helper
		AccountHelper helper = new AccountHelper(networkAdapter, credentials, tokenStore, UUID.randomUUID());

		//Return user
		redditClient = helper.switchToUser(username);
	}

	private void saveToFile() {
		//Create tokenStore
		JsonFileTokenStore tokenStore = new JsonFileTokenStore(new File(System.getProperty("user.dir") + "/src/main/resources/com/lb/jeddit/persistent/"+username+"token.json"));

		//Update tokenStore with information
		tokenStore.storeRefreshToken(redditClient.me().getUsername().toLowerCase(), redditClient.getAuthManager().getRefreshToken());

		//Save to file
		tokenStore.persist();

		//Save accounts list to file
		try {
			FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/src/org/nice/reddit/resources/persistent/accounts.obj");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(accounts);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.out.println(readFromFile().me().karma());
	}

	private void setStatus(String status) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText(status);
			}
		});
	}


	/** GETTERS & SETTERS */
	public static Login getInstance() {
		return login;
	}

	public RedditClient getRedditClient() {
		return redditClient;
	}

}
