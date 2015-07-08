package com.example.mywifihelper;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.webdesign688.shot360.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeftFrament extends Fragment {
    private View inflate;
	private ListView mListView;
	private boolean mLoginTag;
	@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	        inflate = inflater.inflate(R.layout.leftframe, null);  
	return inflate;
}
  @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 mListView= (ListView) inflate.findViewById(R.id.listView_menu);
		 inflate.findViewById(R.id.progressBar_index).setVisibility(View.GONE);
		 mListView.setVisibility(View.VISIBLE);
		 initListView();
		selectListview();

	}
            
private void initListView() {
    MyAdapter adapter=new MyAdapter();
	mListView.setAdapter(adapter);		
}
public class MyAdapter extends BaseAdapter{
@Override
public View getView(int position, View convertView, ViewGroup parent) {
	 View layout = getActivity().getLayoutInflater().inflate(R.layout.item_menu, null);
	 ImageView imageView = (ImageView) layout.findViewById(R.id.imageView_listview_menu);
	 TextView textView= (TextView) layout.findViewById(R.id.textview_listview_textview);
	 if (position==0) {
		 //  imageView.setBackgroundResource(R.drawable.icon_login);
			 textView.setText("…Ë÷√ip");
	}
	 if (position==1) {
		//   imageView.setBackgroundResource(R.drawable.icon_login);
			 textView.setText(" ‘≈ƒ");
	}
	 if (position==2) {
		//   imageView.setBackgroundResource(R.drawable.icon_login);
			 textView.setText("...");
	}
	 
	
	return layout;
}
@Override
public int getCount() {
	// TODO Auto-generated method stub
	return 3;
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
};

private void selectListview() {
       mListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
		
			if(position==0){
				Intent intent=new Intent(getActivity(),SetIPActivity.class);
				startActivity(intent);
			}
			if(position==1){
		      
			}
			if(position==2){
		      /*  Intent intent=new Intent(getActivity(),IndexNew1Activity.class);
		        application.setmString_actionbar3("Ãÿª›");
		        intent.putExtra("OT", 2);
				startActivity(intent);*/
			}
			

			
		}
	});		
}

}
