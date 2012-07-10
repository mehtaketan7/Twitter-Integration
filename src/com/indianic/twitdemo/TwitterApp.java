package com.indianic.twitdemo;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

public class TwitterApp {

	private Twitter mTwitter;
	private TwitterSession mSession;
	private AccessToken mAccessToken;
	private CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private OAuthProvider mHttpOauthprovider;
	private String mConsumerKey;
	private String mSecretKey;
	private ProgressDialog mProgressDlg;
	private TwDialogListener mListener;
	private Context context;
	ArrayList<String> frnd = new ArrayList<String>();
	public static final String CALLBACK_URL = "twitterapp://connect";
	private static final String TAG = "TwitterApp";

	public TwitterApp(Context context) {
		this.context = context;
	}

	public TwitterApp(Context context, String consumerKey, String secretKey) {
		this.context = context;

		mTwitter = new TwitterFactory().getInstance();
		mSession = new TwitterSession(context);
		mProgressDlg = new ProgressDialog(context);

		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mConsumerKey = consumerKey;
		mSecretKey = secretKey;

		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey,
				mSecretKey);
		mHttpOauthConsumer.setTokenWithSecret(mConsumerKey, mSecretKey);

		mHttpOauthprovider = new DefaultOAuthProvider(
				"https://api.twitter.com/oauth/request_token",
				"https://api.twitter.com/oauth/access_token",
				"https://api.twitter.com/oauth/authorize");

		mAccessToken = mSession.getAccessToken();
		configureToken();
	}

	@SuppressWarnings("deprecation")
	public void configureToken() {
		// TODO Auto-generated method stub
		if (mAccessToken != null) {
			mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);

			mTwitter.setOAuthAccessToken(mAccessToken);
		}
	}

	public void shutdown() {
		if (mTwitter != null) {
			mTwitter.shutdown();
		}
	}

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void resetAccessToken() {
		Log.e("access token",""+mAccessToken);
		if (mAccessToken != null) {
			mSession.resetAccessToken();

			mAccessToken = null;
		}
	}

	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}

	public interface TwDialogListener {
		public void onComplete(String value);

		public void onError(String value);
	}

	public String getUsername() {
		return mSession.getUsername();

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();

			if (msg.what == 1) {
				if (msg.arg1 == 1)
					mListener.onError("Error getting request token");
				else
					mListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};

	public void updateStatus(String status) throws Exception {
		try {
			mTwitter.updateStatus(status);
		} catch (TwitterException e) {
			throw e;
		}
	}

	public String getScreenName() throws IllegalStateException,
			TwitterException {

		return mTwitter.getScreenName();
	}

	public int getid() throws IllegalStateException, TwitterException {

		return mTwitter.getId();
	}

	public void getfollowers(String Screen) {
		try {
			Log.e("nn", "" + mTwitter.getFollowersIDs(Screen));
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String> getfriendlist() {
		// Twitter twitter = new TwitterFactory().getInstance();
		List<Status> statuses = null;
		// FriendList friendList=new FriendList();
		Paging paging = new Paging(1, 60);
		try {
			statuses = mTwitter.getFriendsTimeline(paging);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("Showing friends timeline.");
		for (Status status : statuses) {
//			System.out.println(status.getUser().getName() + ":"
//					+ status.getText());
//			Log.e("user",
//					"" + status.getUser().getName() + ":" + status.getText());
			// frnd.add(status.getUser().getName());
			TwitterModel.setFrnd(status.getUser().getScreenName());
			TwitterModel.setFrndinfo(status.getText());
		}
		return frnd;

	}

	public void sendDirectmessage(String message1, int id) {
		// int recipientId;
		DirectMessage message = null;
		// The factory instance is re-useable and thread safe.
		// https://api.twitter.com/1/friends/ids.json?cursor=-1&screen_name=twitterapi
		try {
			message = mTwitter.sendDirectMessage(id, message1);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("Sent: " message.getText() + " to @" +
		// message.getRecipientScreenName());
		Log.e("msg",
				"" + message.getText() + ":to @"
						+ message.getRecipientScreenName());
	}

	public void searchtweet() {
		// The factory instance is re-useable and thread safe.

		Query query = new Query("source:twitter4j yusukey");
		QueryResult result;
		try {
			result = mTwitter.search(query);
			for (Tweet tweet : result.getTweets()) {
				System.out.println(tweet.getFromUser() + ":" + tweet.getText());
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void showLoginDialog(String url) {
		// TODO Auto-generated method stub
		final TwDialogListener listener = new TwDialogListener() {

			public void onComplete(String value) {
				// TODO Auto-generated method stub
				processToken(value);
			}

			public void onError(String value) {
				// TODO Auto-generated method stub
				mListener.onError("Failed opening authorization page");
			}
		};
		new TwitterDialog(context, url, listener).show();
	}

	protected void processToken(String callbackUrl) {
		// TODO Auto-generated method stub
		mProgressDlg.setMessage("Finalizing ...");
		mProgressDlg.show();

		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer,
							verifier);

					mAccessToken = new AccessToken(
							mHttpOauthConsumer.getToken(),
							mHttpOauthConsumer.getTokenSecret());

					configureToken();

					User user = mTwitter.verifyCredentials();

					mSession.storeAccessToken(mAccessToken, user.getName());

					what = 0;
				} catch (Exception e) {
					Log.d(TAG, "Error getting access token");

					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			};
		}.start();
	}

	private String getVerifier(String callbackUrl) {
		// TODO Auto-generated method stub
		String verifier = "";

		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for (String parameter : array) {
				String v[] = parameter.split("=");

				if (URLDecoder.decode(v[0]).equals(
						oauth.signpost.OAuth.OAUTH_VERIFIER)) {
					verifier = URLDecoder.decode(v[1]);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return verifier;
	}

	public void authorize() {
		// TODO Auto-generated method stub
		mProgressDlg.setMessage("Initializing ...");
		mProgressDlg.show();
		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;

				try {
					authUrl = mHttpOauthprovider.retrieveRequestToken(
							mHttpOauthConsumer, CALLBACK_URL);

					what = 0;

					Log.d(TAG, "Request token url " + authUrl);
				} catch (Exception e) {
					Log.d(TAG, "1:Failed to get request token");

					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler
						.obtainMessage(what, 1, 0, authUrl));

			};
		}.start();
	}
}
