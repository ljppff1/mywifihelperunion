package com.example.mywifihelper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.webdesign688.shot360.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListviewActivity extends FragmentActivity {

	private ListView mListView;
	private String[] mStringLists;
	private int[] mNumLists;
	private int mId;
	private  ArrayList<Data> mDataList=new ArrayList<ListviewActivity.Data>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		
		Intent intent = getIntent();
		String string_Title = intent.getStringExtra("title");
		
		mId = intent.getIntExtra("id", 0);
		if (mId==1) {
			mStringLists = getResources().getStringArray(R.array.postbuild_rentsale);
			mNumLists=new int[]{0,1,2,3};
			initListView();
		}
		
		
		FragmentManager sfm = getSupportFragmentManager();
		FragmentTransaction ft = sfm.beginTransaction();
		Frame_Title    frame_Title= new Frame_Title();
		frame_Title.setTitle(string_Title);
		ft.add(R.id.frame_title, frame_Title);
		ft.commit();
		
	}

	class Data{
		String   DistrictID;
		String   DistrictNameHK;
		String   DistrictNameEN;
		@Override
		public String toString() {
			return "Data [DistrictID=" + DistrictID + ", DistrictNameHK="
					+ DistrictNameHK + ", DistrictNameEN=" + DistrictNameEN
					+ "]";
		}
	}
	private void initListView() {
		mListView = (ListView) findViewById(R.id.listView_fangfan);
		mListView.setAdapter(new MyAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent data=new Intent();
				switch (mId) {
				 case 1:
					 data.putExtra("name", mStringLists[arg2]);
					 data.putExtra("num", mNumLists[arg2]);
						setResult(1, data);
						finish();
					break;
              
				default:
					break;
				}
			}
		
		});
	}
 
	class MyAdapter extends  BaseAdapter{
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = getLayoutInflater().inflate(R.layout.item_listview_post, null);
			TextView  mTextView = (TextView) layout.findViewById(R.id.tv_listview_post_title);
			if (mId==4) {
				mTextView.setText(mDataList.get(position).DistrictNameHK+" "+mDataList.get(position).DistrictNameEN);
			}
			else if (mId==15) {
				mTextView.setText(mDataList.get(position).DistrictNameHK+" "+mDataList.get(position).DistrictNameEN);
			}
			else if (mId==16) {
				mTextView.setText(mDataList.get(position).DistrictNameHK+" "+mDataList.get(position).DistrictNameEN);
			}
			else if (mId==17) {
				mTextView.setText(mDataList.get(position).DistrictNameHK+" "+mDataList.get(position).DistrictNameEN);
			}
			
			else {
				mTextView.setText(mStringLists[position]);
			}
			return layout;
		}
		@Override
		public int getCount() {
			if (mId==4) {
				return  mDataList.size();
			}
			else if (mId==15) {
				return  mDataList.size();
			}
			else if (mId==16) {
				return  mDataList.size();
			}
			else if (mId==17) {
				return  mDataList.size();
			}
			else {
				return mStringLists.length;
			}
			// TODO Auto-generated method stub
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		}

}
