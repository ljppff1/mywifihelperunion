package com.ty.winchat.ui;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.webdesign688.shot360.R;
import com.ty.winchat.jpgtovideo.VideoCapture;
import com.ty.winchat.listener.TCPVideoReceiveListener;
import com.ty.winchat.listener.inter.OnBitmapLoaded;
import com.ty.winchat.listener.model.bitmapTill;
import com.ty.winchat.util.Constant;
import com.ty.winchat.util.Util;
import com.ty.winchat.videotest.SurfaceViewTestActivity;
import com.ty.winchat.widget.VideoView;

/**
 * 视屏聊天
 * @author wj
 * @creation 2013-5-15
 */
public class VideoChatService extends Base implements OnBitmapLoaded{
	
	
	private VideoView myView1;
	private VideoView myView2;
	private VideoView myView3;
	private VideoView myView4;
	private VideoView myView5;
	private VideoView myView6;
	private VideoView myView7;
	private VideoView myView8  ;

	private String chatterIP;//记录当前用户ip
    List<bitmapTill> bt =new ArrayList<bitmapTill>();
	 final int REFRESH=0; 
	 
	 
	 private int port=Constant.VIDEO_PORT;
	 
	 private TCPVideoReceiveListener videoReceiveListener;
	// private UDPVoiceListener voiceListener;
	 
	 private boolean stop;//标识activity被遮挡
	 
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
          switch (msg.what) {
          case 1:
        	  for(int i=0;i<bt.size();i++){
  				if(bt.get(i).getIp().equals(((bitmapTill)msg.obj).getIp())){
  					bt.get(i).setBitmap(((bitmapTill)msg.obj).getBitmap());
  					break;
  				}
  			}
        /*	  
        	  for(int i=0;i<bt.size();i++){
  					bt.get(i).setBitmap(((bitmapTill)msg.obj).getBitmap());
  				}
        */

        	  adapter.notifyDataSetChanged();
        	  break;
			case 2:
				progressHorizontal.setProgress(Integer.valueOf((String) msg.obj));    
				break;   
			case 3:
		    
				Intent intent =new Intent(getApplicationContext(), SurfaceViewTestActivity.class);
				intent.putExtra("PATHSTRING",(String) msg.obj);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;

		default:
			break;
		}
		/*	myView1.setBitmap(((bitmapTill)msg.obj).getBitmap());
			myView2.setBitmap(((bitmapTill)msg.obj).getBitmap());
			myView3.setBitmap(((bitmapTill)msg.obj).getBitmap());
			myView4.setBitmap(((bitmapTill)msg.obj).getBitmap());
			myView5.setBitmap(((bitmapTill)msg.obj).getBitmap());
			myView6.setBitmap(((bitmapTill)msg.obj).getBitmap());
			myView7.setBitmap(((bitmapTill)msg.obj).getBitmap())
			myView8.setBitmap(((bitmapTill)msg.obj).getBitmap());
		*/	
			
			
		};
	};
	private GridView gridview;
	private ImageAdapter adapter;
	private Button mBtOverVideo;
	private ProgressBar progressHorizontal;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_chat_service);
		findViews();
		chatterIP=getIntent().getStringExtra("IP");
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					videoReceiveListener=TCPVideoReceiveListener.getInstance();
					videoReceiveListener.setBitmapLoaded(VideoChatService.this);
					if(!videoReceiveListener.isRunning())
						videoReceiveListener.open();//先监听端口，然后连接
					//监听的
				} catch (IOException e1) {
					e1.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showToast("非常抱歉,视屏连接失败");
							finish();
						}
					});
				}				
			}
		}).start();
	}
	
	private void findViews(){
		
		
        progressHorizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
        
		myView1=(VideoView) findViewById(R.id.video_chat_myview1);
		myView2=(VideoView) findViewById(R.id.video_chat_myview2);
		myView3=(VideoView) findViewById(R.id.video_chat_myview3);
		myView4=(VideoView) findViewById(R.id.video_chat_myview4);
		myView5=(VideoView) findViewById(R.id.video_chat_myview5);
		myView6=(VideoView) findViewById(R.id.video_chat_myview6);
		myView7=(VideoView) findViewById(R.id.video_chat_myview7);
		myView8=(VideoView) findViewById(R.id.video_chat_myview8);
		   BitmapDrawable draw=(BitmapDrawable) getResources().getDrawable(R.drawable.white1);
		   Bitmap m=draw.getBitmap();
      for(int i = 0;i<100;i++){
    	  bitmapTill bt1 =new bitmapTill();
    	  bt1.setBitmap(m);
    	  bt1.setIp("192.168.1."+i);
    	  bt.add(bt1);
      }
		
		gridview =(GridView)this.findViewById(R.id.gridview);
		adapter =new ImageAdapter();
		gridview.setAdapter(adapter);

		mBtOverVideo =(Button)this.findViewById(R.id.mBtOverVideo);
		mBtOverVideo.setOnClickListener(listener);
		
	}
	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mBtOverVideo:
				progressHorizontal.setVisibility(View.VISIBLE);
				VideoCapture.start(VideoChatService.this, "1133",handler,"18",bt);
				break;

			default:
				break;
			}
		}
	};
	static class ViewHolder {
		ImageView imageView;
		TextView mTVip;
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return bt.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.item_grid_image,
						parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.mTVip = (TextView) view.findViewById(R.id.mTVjob1);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			holder.imageView.setImageBitmap(bt.get(position).getBitmap());
			holder.mTVip.setText(bt.get(position).getIp());
		/*	
		    String url =list.get(position);
		  
		    
			holder.imageView.setImageBitmap(getLoacalBitmap(list.get(position)));
*/
			return view;
		}

	}
	@Override
	protected void onPause() {
		stop=true;
		super.onPause();
	}


	@Override
	public void onBitmapLoaded(bitmapTill bt) {
		Message msg =new Message();
		msg.obj =bt;
		msg.what =1;
		handler.sendMessage(msg);
/*		if(stop){
			    try {
			    	//代码实现模拟用户按下back键
			    	String keyCommand = "input keyevent " + KeyEvent.KEYCODE_BACK;  
			    	Runtime runtime = Runtime.getRuntime();  
					runtime.exec(keyCommand);
					stop=false;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
*/
		}
	
	
	
	
	
   


	
	protected void setDisplayOrientation(Camera camera, int angle) {
	    try {
	    	Method downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
		    if (downPolymorphic != null) 
		        downPolymorphic.invoke(camera, new Object[] { angle });
	    } catch (Exception e1) {
	      e1.printStackTrace();
	    }
	  }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			videoReceiveListener.close();
		//	voiceListener.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * socket池，用来缓存
	 */
	@Deprecated
	class SocketPool extends Thread{
		private List<Socket> sockets=new LinkedList<Socket>();
		private final int poolSize=30; 
		private boolean go=true;
		
		@Override
		public void run() {
			InetAddress address = null;
			try {
				address = InetAddress.getByName(chatterIP);
				while(go){
					int count=sockets.size();
					if(count<poolSize){
						for(int i=0;i<poolSize-count;i++){
							sockets.add(new Socket(address, port));
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public Socket getSocket(){
			if(!sockets.isEmpty()){
				Socket socket=sockets.get(0);
				sockets.remove(0);
				return socket;
			}
			return null;
		}
		
		public void close(){
			go=false;
			for(Socket socket:sockets){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
