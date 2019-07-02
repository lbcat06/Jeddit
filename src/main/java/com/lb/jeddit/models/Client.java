package com.lb.jeddit.models;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.*;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.pagination.Paginator;
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.tree.CommentNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {

	private RedditClient redditClient;
	private static Client client;
	private boolean doneFlag=false;

	//Client attribute
	private String username="";
	private List<KarmaBySubreddit> karma = new ArrayList<>();
	private int totalKarma=0;

	public void createClient(String username, String password, boolean changeSceneBool) {
		setAttributes();
	}

	private void setAttributes() {
		this.karma = redditClient.me().karma();
		this.username = redditClient.me().getUsername();
		calculations();

		doneFlag=true;
	}

	private void calculations() {
		//Calculate users total karma
		for(KarmaBySubreddit karmaBySubreddit : karma) {
			totalKarma += karmaBySubreddit.getLinkKarma()+karmaBySubreddit.getCommentKarma();
		}
	}

	/** GET POSTS */
	private Iterator<Listing<Submission>> submissionIterator;


	private List<SubmissionReference> submissionReferenceList = new ArrayList<>();

	//Fetch submission
	private SubmissionReference fetchSubmissionReference(String id) {
		SubmissionReference submissionReference = redditClient.submission(id);
		submissionReferenceList.add(submissionReference);
		return submissionReference;
	}

	public SubmissionReference getSubmissionReference(String id) {
		//Search for submission reference in list
		for(SubmissionReference submissionReference: submissionReferenceList) {
			if(submissionReference.getId().equals(id)) {
				return submissionReference;
			}
		}

		//If not in list fetch it
		return fetchSubmissionReference(id);
	}

	public List<CommentNode> getSubmissionComments(String id) {
		Iterator<CommentNode<PublicContribution<?>>> commentIterator = getSubmissionReference(id).comments().walkTree().iterator();

		List<CommentNode> commentNodeList = new ArrayList<>();

		while(commentIterator.hasNext()) {
			commentNodeList.add(commentIterator.next());
		}

		return commentNodeList;
	}


	//Front page
	public Listing<Submission> getFrontPage(int page, SubredditSort subredditSort, TimePeriod timePeriod) {
		if(page==1) {
			submissionIterator = redditClient.frontPage()
					.limit(10)
					.sorting(subredditSort)
					.timePeriod(timePeriod)
					.build()
					.iterator();
		}

		return submissionIterator.next();
	}

	//Get user subscriptions
	public List<Subreddit> getSubscriptions() {
		BarebonesPaginator<Subreddit> subredditBarebonesPaginator = redditClient.me().subreddits("subscriber")
				.limit(Paginator.RECOMMENDED_MAX_LIMIT)
				.build();

		List<Subreddit> subscribed = new ArrayList<>();

		for (Listing<Subreddit> subredditListing : subredditBarebonesPaginator) {
			subscribed.addAll(subredditListing);
		}

		return subscribed;
	}


	/** GETTERS & SETTERS */

	public static Client getInstance() {
		if (client == null)
			client = new Client();
		return client;
	}
	public static Client createNewInstance() {
		client = new Client();
		return client;
	}

	public String getUsername() {
		return username;
	}

	public int getTotalKarma() {
		return totalKarma;
	}

	public boolean getDoneFlag() {
		return doneFlag;
	}

	public void setRedditClient(RedditClient redditClient) {
		this.redditClient = redditClient;
	}

}
