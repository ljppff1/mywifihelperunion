package com.example.mywifihelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webdesign688.shot360.R;

public class Base3Activity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected LeftFrament mFrag;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
         AppManager.getAppManager().addActivity(this);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new LeftFrament();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		}else{
			mFrag = (LeftFrament)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
        
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.activity_horizontal_margin);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.activity_horizontal_margin1);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
       android.app.ActionBar actionBar = getActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setDisplayShowCustomEnabled(true);//支持自定義
       actionBar.setLogo(R.drawable.icon_menu);
       actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.touming));
       actionBar.setDisplayHomeAsUpEnabled(false);

       View view=getLayoutInflater().inflate(R.layout.item_actionbar3, null);
    

       
	 actionBar.setCustomView(view);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(AppManager.getAppManager().isMainOrWel()){
			toggle();
			}else{
				AppManager.getAppManager().finishActivity();
			}
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!AppManager.getAppManager().isMainOrWel()){
		if(keyCode==KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			 return true;
		}
		}
		return super.onKeyDown(keyCode, event);
	}

	

}
