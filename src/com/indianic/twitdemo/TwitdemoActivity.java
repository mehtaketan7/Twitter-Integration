package com.indianic.twitdemo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.TwitterException;
import com.indianic.twitdemo.TwitterApp.TwDialogListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TwitdemoActivity extends Activity {
	/** Called when the activity is first created. */
	TwitterApp twitter;
	private CheckBox mTwitterBtn;
	private static final String twitter_consumer_key = "ImzBKKY8WFPqV6BTd9w";
	private static final String twitter_secret_key = "veIbnfHe9tLjrP79GV49Ck2LL3zJs6MVWQm13ns1hks";
	Button btnUpdateStatus, btntimeline, btnfrnd, btnfollowers, btnSendImage,
			btnlogin;
	static ArrayList<TwitterModel> twitfrndid = new ArrayList<TwitterModel>();
	static ArrayList<TwitterModel> twitfollowerid = new ArrayList<TwitterModel>();
	int frndid = 0;
	private static int MAX_IMAGE_DIMENSION = 720;
	public static AndroidHttpClient httpclient = null;
	ImageView imageView;
	TextView txtUser;
	boolean flag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mTwitterBtn = (CheckBox) findViewById(R.id.twitterCheck);

		imageView = (ImageView) findViewById(R.id.logo_imageView);

		mTwitterBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				onTwitterClick();
			}

		});
		btnlogin = (Button) findViewById(R.id.btn_login);
		txtUser = (TextView) findViewById(R.id.txt_username);

		btnlogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!twitter.hasAccessToken() || flag == false) {
					Log.e("log in", "log in");
					// mTwitterBtn.setChecked(false);
					twitter.configureToken();
					twitter.authorize();
					flag = true;
				} else {
					Log.e("log out", "log out");

					// mTwitterBtn.setChecked(true);
					twitter.resetAccessToken();
					// String username = twitter.getUsername();
					// username = (username.equals("")) ? "Unknown" : username;
					//
					// mTwitterBtn.setText("  Twitter (" + username + ")");
					// mTwitterBtn.setTextColor(Color.WHITE);
					txtUser.setText("welcome,");
					btnlogin.setText("Login");
					twitter.shutdown();
					flag = false;
				}

			}
		});
		twitter = new TwitterApp(this, twitter_consumer_key, twitter_secret_key);
		twitter.setListener(mTwLoginDialogListener);
		// frndid = getIntent().getIntExtra("recepnistId", 0);
		// Log.e("this is id in activity",""+frndid);

		// if(frndid != 0){
		// twitter.resetAccessToken();
		//
		// // mTwitterBtn.setChecked(false);
		// // mTwitterBtn
		// // .setText("  Twitter (Not connected)");
		// //mTwitterBtn.setTextColor(Color.GRAY);
		// twitter.sendDirectmessage("this is test",frndid);
		// // Twitter twitter= new TwitterFactory().getInstance();
		// // //twitter = new TwitterApp(getApplicationContext());
		// // //twitter.sendDirectmessage("this is testing of app.", frndid);
		// // DirectMessage message = null;
		// // // The factory instance is re-useable and thread safe.
		// //
		// //https://api.twitter.com/1/friends/ids.json?cursor=-1&screen_name=twitterapi
		// // try {
		// // message = twitter.sendDirectMessage(frndid, "message1");
		// // } catch (TwitterException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// }
		//

		if (twitter.hasAccessToken()) {
			mTwitterBtn.setChecked(true);

			String username = twitter.getUsername();
			username = (username.equals("")) ? "Unknown" : username;

			mTwitterBtn.setText("  Twitter (" + username + ")");
			mTwitterBtn.setTextColor(Color.WHITE);
			// String picurl;
			// try {
			// picurl =
			// "https://api.twitter.com/1/users/profile_image?screen_name="
			// + twitter.getScreenName() + "&size=normal";
			// imageView.setImageBitmap(getBitmap(picurl));
			// } catch (IllegalStateException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (TwitterException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

		btnSendImage = (Button) findViewById(R.id.btn_sendimage);
		btnSendImage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (twitter.hasAccessToken())
					startActivity(new Intent(TwitdemoActivity.this,
							SendImageActivity.class));
				else
					Toast.makeText(getApplicationContext(),
							"Please connect your Twitter account first!",
							Toast.LENGTH_SHORT).show();

			}
		});

		btnUpdateStatus = (Button) findViewById(R.id.btn_updatestatus);
		btnUpdateStatus.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// The factory instance is re-useable and thread safe.
				try {
					twitter.updateStatus("demo app");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		btntimeline = (Button) findViewById(R.id.btn_timeline);
		btntimeline.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				twitter.getfriendlist();
				Intent intent = new Intent(getApplicationContext(),
						FriendList.class);
				startActivity(intent);

			}
		});
		btnfrnd = (Button) findViewById(R.id.btn_frnd);
		btnfrnd.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String frndlink = null;
				try {
					Log.e("user name", "" + twitter.getScreenName());
					Log.e("user id", "" + twitter.getid());
					twitter.getfollowers(twitter.getScreenName());
					frndlink = "https://api.twitter.com/1/friends/ids.json?cursor=-1&screen_name="
							+ twitter.getScreenName();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TwitterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String readTwitterFeed = readYouTubeFeed(frndlink);
				try {
					// listView = (ListView) findViewById(R.id.listView);
					JSONObject jsonObject = new JSONObject(readTwitterFeed);
					Log.e(TwitdemoActivity.class.getName(),
							"Number of entries " + jsonObject.length()
									+ jsonObject);
					// JSONObject job1=jsonObject.getJSONObject("geometry");
					//
					JSONArray jsonid = jsonObject.getJSONArray("ids");
					Log.e("result", "" + jsonid.length() + jsonid);
					for (int i = 0; i < jsonid.length(); i++) {
						Long id = jsonid.getLong(i);
						Log.e("id" + i, "" + id);

						String frndnamelink = "https://api.twitter.com/1/users/show.json?id="
								+ id + "&include_entities=true";
						String readTwitternameFeed = readYouTubeFeed(frndnamelink);
						JSONObject jsonNameObject = new JSONObject(
								readTwitternameFeed);
						Log.e("name json",
								"" + jsonNameObject.getString("name"));
						String Frndname = jsonNameObject.getString("name");

						TwitterModel twitterModel = new TwitterModel();
						twitterModel.setFrndid(id);
						twitterModel.setFrndname(Frndname);
						twitfrndid.add(twitterModel);

					}
					Intent intent = new Intent(TwitdemoActivity.this,
							RecipientList.class);
					startActivity(intent);

					// JSONArray job3 = job2.getString("geometry");
					// JSONObject job3=jsonObject.getJSONObject("geometry");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnfollowers = (Button) findViewById(R.id.btn_follower);
		btnfollowers.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {

					String followerslink = "https://api.twitter.com/1/followers/ids.json?cursor=-1&screen_name="
							+ twitter.getScreenName();
					String readTwitterFeed = readYouTubeFeed(followerslink);
					// listView = (ListView) findViewById(R.id.listView);
					JSONObject jsonObject = new JSONObject(readTwitterFeed);
					Log.e(TwitdemoActivity.class.getName(),
							"Number of entries " + jsonObject.length()
									+ jsonObject);
					// JSONObject job1=jsonObject.getJSONObject("geometry");
					//
					JSONArray jsonid = jsonObject.getJSONArray("ids");
					Log.e("result", "" + jsonid.length() + jsonid);
					for (int i = 0; i < jsonid.length(); i++) {
						Long id = jsonid.getLong(i);
						Log.e("id" + i, "" + id);

						String followernamelink = "https://api.twitter.com/1/users/show.json?id="
								+ id + "&include_entities=true";
						String readTwitternameFeed = readYouTubeFeed(followernamelink);
						JSONObject jsonNameObject = new JSONObject(
								readTwitternameFeed);
						Log.e("name json",
								"" + jsonNameObject.getString("name"));
						String Folowwername = jsonNameObject.getString("name");
						TwitterModel twitterModel = new TwitterModel();
						twitterModel.setFollowersid(id);
						twitterModel.setFollowersname(Folowwername);
						twitfollowerid.add(twitterModel);

					}
					Intent intent = new Intent(TwitdemoActivity.this,
							FollowersList.class);
					startActivity(intent);

					// JSONArray job3 = job2.getString("geometry");
					// JSONObject job3=jsonObject.getJSONObject("geometry");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void onTwitterClick() {
		// TODO Auto-generated method stub
		if (twitter.hasAccessToken()) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage("Delete current Twitter connection?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									twitter.resetAccessToken();

									mTwitterBtn.setChecked(false);
									// imageView.setImageBitmap(null);
									mTwitterBtn
											.setText("  Twitter (Not connected)");
									mTwitterBtn.setTextColor(Color.GRAY);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

									mTwitterBtn.setChecked(true);
								}
							});
			final AlertDialog alert = builder.create();

			alert.show();
		} else {
			mTwitterBtn.setChecked(false);

			twitter.authorize();

		}
	}

	public String readYouTubeFeed(String Link) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		try {
			HttpGet httpGet = new HttpGet(Link);
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(TwitdemoActivity.class.toString(),
						"Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}

	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onComplete(String value) {
			// TODO Auto-generated method stub
			String username = twitter.getUsername();
			username = (username.equals("")) ? "No Name" : username;

			mTwitterBtn.setText("  Twitter  (" + username + ")");
			mTwitterBtn.setChecked(true);
			mTwitterBtn.setTextColor(Color.WHITE);
			// String picurl;
			// try {
			// picurl =
			// "https://api.twitter.com/1/users/profile_image?screen_name="
			// + twitter.getScreenName() + "&size=normal";
			// imageView.setImageBitmap(getBitmap(picurl));
			// } catch (IllegalStateException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (TwitterException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			txtUser.setText("Welcome," + username);
			btnlogin.setText("LogOut");
			Toast.makeText(TwitdemoActivity.this,
					"Connected to Twitter as " + username, Toast.LENGTH_LONG)
					.show();
		}

		public void onError(String value) {
			// TODO Auto-generated method stub
			mTwitterBtn.setChecked(false);
			// imageView.setImageBitmap(null);
			Log.e("error", "" + value);
			Toast.makeText(TwitdemoActivity.this, "Twitter connection failed",
					Toast.LENGTH_LONG).show();
		}
	};

	public static Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
			bis.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null) {
				httpclient.close();
			}
		}
		return bm;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public static byte[] scaleImage(Context context, Uri photoUri)
			throws IOException {
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		int orientation = getOrientation(context, photoUri);

		if (orientation == 90 || orientation == 270) {
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
		} else {
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
		}

		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > MAX_IMAGE_DIMENSION
				|| rotatedHeight > MAX_IMAGE_DIMENSION) {
			float widthRatio = ((float) rotatedWidth)
					/ ((float) MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight)
					/ ((float) MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		} else {
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();

		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		if (orientation > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
					srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		String type = context.getContentResolver().getType(photoUri);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (type.equals("image/png")) {
			srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		} else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
			srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		byte[] bMapArray = baos.toByteArray();
		baos.close();
		return bMapArray;
	}

	public static int getOrientation(Context context, Uri photoUri) {
		/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}
}