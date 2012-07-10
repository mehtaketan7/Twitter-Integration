package com.indianic.twitdemo;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FriendList extends Activity {
	ListView listView;
	FriendListAdapter adapter;
	public ArrayList<String> arrayList;
	public ArrayList<String> infoArrayList;
	TwitterApp twitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		listView = (ListView) findViewById(R.id.listview1);
		arrayList = new ArrayList<String>();
		infoArrayList = new ArrayList<String>();
		arrayList = TwitterModel.getFrnd();
		infoArrayList = TwitterModel.getFrndinfo();
		Log.e("size", "" + arrayList.size());
		Log.e("size1", "" + infoArrayList.size());
		adapter = new FriendListAdapter(getApplicationContext(), arrayList,
				infoArrayList);
		listView.setAdapter(adapter);
	}

	/**
	 * Definition of the list adapter
	 */
	public class FriendListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<String> arrayList;
		private ArrayList<String> frndarrayList1;
		// LayoutInflater inflater;
		Context context;
		ViewHolder holder;

		// FriendsList friendsList;
		public FriendListAdapter(Context context, ArrayList<String> list,
				ArrayList<String> frndarrayList) {
			this.context = context;
			arrayList = list;
			frndarrayList1 = frndarrayList;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
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
				hView = mInflater.inflate(R.layout.listrow, null);

				holder.id = (TextView) hView.findViewById(R.id.textView_id);
				holder.info = (TextView) hView.findViewById(R.id.textView_info);
				// holder.info = (TextView) hView.findViewById(R.id.info);
				hView.setTag(holder);

			} else {
				holder = (ViewHolder) hView.getTag();
			}
			holder.id.setText(position + ": " + arrayList.get(position));
			holder.info.setText(frndarrayList1.get(position));
			// ViewHolder holder = (ViewHolder) hView.getTag();
			hView.setTag(holder);

			return hView;
		}

	}

	class ViewHolder {

		TextView id;
		TextView info;
	}
}
