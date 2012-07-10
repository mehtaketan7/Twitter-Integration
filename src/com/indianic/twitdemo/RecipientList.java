package com.indianic.twitdemo;

import java.util.ArrayList;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.indianic.twitdemo.FriendList.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecipientList extends Activity implements OnItemClickListener{
	
	ListView listView;
	RecipientListAdapter adapter;
	public ArrayList<Long> arrayList;
	public ArrayList<String> NamearrayList;
	TwitterApp twitter;
	private static final String twitter_consumer_key = "ImzBKKY8WFPqV6BTd9w";
	private static final String twitter_secret_key = "veIbnfHe9tLjrP79GV49Ck2LL3zJs6MVWQm13ns1hks";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
	//	twitter=new 
		listView = (ListView) findViewById(R.id.listview1);
		arrayList = new ArrayList<Long>();
		NamearrayList=new ArrayList<String>();
		//final Long[] Address = new Long[TwitdemoActivity.twitfrndid.size()];
		for (int i = 0; i < TwitdemoActivity.twitfrndid.size(); i++) {
			//Address[i] = twitfrndid.get(i).getFrndid();
			arrayList.add(TwitdemoActivity.twitfrndid.get(i).getFrndid());
			NamearrayList.add(TwitdemoActivity.twitfrndid.get(i).getFrndname());
			Log.e("id", "" + TwitdemoActivity.twitfrndid.get(i).getFrndid());

		}
		adapter = new RecipientListAdapter(getApplicationContext(), arrayList,NamearrayList);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public class RecipientListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<String> namearrayList;
		private ArrayList<Long> frndarrayList1;
		// LayoutInflater inflater;
		Context context;
		ViewHolder holder;

		public RecipientListAdapter(Context applicationContext,
				ArrayList<Long> arrayList, ArrayList<String> namearrayList) {
			// TODO Auto-generated constructor stub
			
			this.context = applicationContext;
			frndarrayList1 = arrayList;
			this.namearrayList=namearrayList;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return frndarrayList1.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return frndarrayList1.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View hView = convertView;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				holder = new ViewHolder();
				hView = mInflater.inflate(R.layout.frndlistrow, null);

				holder.id = (TextView) hView.findViewById(R.id.textView_frndid);
				holder.name = (TextView) hView.findViewById(R.id.textView_frndName);
				// holder.info = (TextView) hView.findViewById(R.id.info);
				hView.setTag(holder);

			} else {
				holder = (ViewHolder) hView.getTag();
			}
			holder.id.setText(frndarrayList1.get(position).toString());
			holder.name.setText(namearrayList.get(position));
			// ViewHolder holder = (ViewHolder) hView.getTag();
			hView.setTag(holder);

			return hView;
		
		}
	}

	class ViewHolder {

		TextView id;
		TextView name;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.e("item id",""+arg0.getItemAtPosition(arg2).toString());
		int id=Integer.parseInt(arg0.getItemAtPosition(arg2).toString());
		twitter = new TwitterApp(getApplicationContext(), twitter_consumer_key,twitter_secret_key);
		//twitter.sendDirectmessage("this is testing", Integer.parseInt(arg0.getItemAtPosition(arg2).toString()));
		Intent intent=new Intent(RecipientList.this,TwitdemoActivity.class);
		intent.putExtra("recepnistId",id);
		startActivity(intent);
		//Twitter mtwitter= new TwitterFactory().getInstance();
		// DirectMessage message = null;
			// The factory instance is re-useable and thread safe.
			//https://api.twitter.com/1/friends/ids.json?cursor=-1&screen_name=twitterapi 
		//twitter.authorize();
			twitter.sendDirectmessage("this is test",id);
			
//		   // System.out.println("Sent: " message.getText() + " to @" + message.getRecipientScreenName());
//		    Log.e("msg",""+message.getText() +":to @"+message.getRecipientScreenName());
		//twitter.getUsername();
		
	}
}
