package com.indianic.twitdemo;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FollowersList extends Activity implements OnItemClickListener {

	ListView listView;
	FollowersListAdapter adapter;
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

		listView = (ListView) findViewById(R.id.listview1);
		arrayList = new ArrayList<Long>();
		NamearrayList = new ArrayList<String>();
		// final Long[] Address = new Long[TwitdemoActivity.twitfrndid.size()];
		for (int i = 0; i < TwitdemoActivity.twitfollowerid.size(); i++) {
			// Address[i] = twitfrndid.get(i).getFrndid();
			arrayList.add(TwitdemoActivity.twitfollowerid.get(i)
					.getFollowersid());
			NamearrayList.add(TwitdemoActivity.twitfollowerid.get(i)
					.getFollowersname());
			Log.e("id", ""
					+ TwitdemoActivity.twitfollowerid.get(i).getFollowersid());

		}
		adapter = new FollowersListAdapter(getApplicationContext(), arrayList,
				NamearrayList);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	public class FollowersListAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		private ArrayList<String> NamearrayList;
		private ArrayList<Long> followersarrayList;
		Context context;
		ViewHolder holder;

		public FollowersListAdapter(Context applicationContext,
				ArrayList<Long> arrayList, ArrayList<String> namearrayList) {
			// TODO Auto-generated constructor stub

			this.context = applicationContext;
			followersarrayList = arrayList;
			NamearrayList = namearrayList;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return followersarrayList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return followersarrayList.get(position);
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
				holder.name = (TextView) hView
						.findViewById(R.id.textView_frndName);
				// holder.info = (TextView) hView.findViewById(R.id.info);
				hView.setTag(holder);

			} else {
				holder = (ViewHolder) hView.getTag();
			}
			holder.id.setText(followersarrayList.get(position).toString());
			holder.name.setText(NamearrayList.get(position));
			// ViewHolder holder = (ViewHolder) hView.getTag();
			hView.setTag(holder);

			return hView;

		}
	}

	class ViewHolder {

		TextView id;
		TextView name;
	}

}
