package com.example.mywifihelper;




import java.util.ArrayList;
import java.util.List;

import com.example.util.SharedPreferenceDB;
import com.webdesign688.shot360.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
//关于页面，是关于程序的大致介绍，只有一个TextView控件
public class SetIPActivity extends Base3Activity {
    TextView textView;
	private TextView tv_postbuild_rentsale;
	private ListView mLvArea1;
	private List<String> area1List =new ArrayList<String>();
	private int totalCount;
	private Integer COUNT1;
	private AreaAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitysetip);
		//默认8个 192.168.0.1 -8
/*		SharedPreferences mySharedPreferences1 = getSharedPreferences(
				"AREA", Activity.MODE_PRIVATE);
*/	
		String COUNT = getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("COUNT", "8");
		COUNT1 =Integer.valueOf(COUNT);
		totalCount =SharedPreferenceDB.getcount(getApplicationContext());
		if(totalCount<COUNT1){
			for(int i =0;i<COUNT1;i++ ){
				if(totalCount==1){
					SharedPreferenceDB.addSp("192.168.0."+(i+1), getApplicationContext(), "1");
				}else{
					if(i>totalCount){
						SharedPreferenceDB.addSp("192.168.0."+(i+1), getApplicationContext(), "1");	
					}
				}
			}
		}
		
		area1List =SharedPreferenceDB.getList(getApplicationContext());
		
		initView();
		
		
		
			}
	private void initView() {
		tv_postbuild_rentsale =(TextView)this.findViewById(R.id.tv_postbuild_rentsale);
		tv_postbuild_rentsale.setOnClickListener(listener);
		
		mLvArea1 =(ListView)this.findViewById(R.id.mLvArea1);
		mLvArea1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				
			}

		});
		adapter =new AreaAdapter();
		mLvArea1.setAdapter(adapter);

		
		
	}
	
	
	
	class Holder{
		TextView area2name,area2name1;
		}

	private class AreaAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return area1List.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return area1List.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if(convertView==null){
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.item_area2, null);
				holder = new Holder();
				holder.area2name =(TextView)convertView.findViewById(R.id.area2name);
				holder.area2name1 =(TextView)convertView.findViewById(R.id.area2name1);
				convertView.setTag(holder);
	
			}else{
				holder =(Holder)convertView.getTag();
			}
   			holder.area2name.setText("   "+position+"  "+Integer.valueOf(360/COUNT1)*position+"度");
   			
			return convertView;
		}
		
	}
	
	
	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_postbuild_rentsale:
				Intent intent1=new Intent(SetIPActivity.this,ListviewActivity.class);
				intent1.putExtra("title", "设置ip数量");
				intent1.putExtra("id", 1);
				startActivityForResult(intent1, 0);

				break;

			default:
				break;
			}
		}
	};
	private int mRentsale_Num;
	

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg1) {
		case 1:
			tv_postbuild_rentsale.setText(arg2.getStringExtra("name")+"  ");
			mRentsale_Num = arg2.getIntExtra("name", 8);
			
			// 存放回显的文字
			SharedPreferences mySharedPreferences1 = getSharedPreferences(
					"AREA", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor1 = mySharedPreferences1.edit();
			editor1.putString("COUNT", arg2.getStringExtra("name")+"");
			editor1.commit();
			
			String COUNT = getSharedPreferences("AREA",Activity.MODE_PRIVATE).getString("COUNT", "8");
			COUNT1 =Integer.valueOf(COUNT);
			totalCount =SharedPreferenceDB.getcount(getApplicationContext());
			if(totalCount<COUNT1){
				for(int i =0;i<COUNT1;i++ ){
					if(totalCount==1){
						SharedPreferenceDB.addSp("192.168.0."+(i+1), getApplicationContext(), "1");
					}else{
						if(i>totalCount){
							SharedPreferenceDB.addSp("192.168.0."+(i+1), getApplicationContext(), "1");	
						}
					}
				}
			}

			area1List =SharedPreferenceDB.getList(getApplicationContext());
			adapter.notifyDataSetChanged();
			
			break;
			
		}}

}
