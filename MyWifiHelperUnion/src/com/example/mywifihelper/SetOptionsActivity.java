package com.example.mywifihelper;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.service.BroadcastFinal;
import com.example.service.ClientNetService;
import com.example.service.ListenerSocketReceiver;
import com.example.util.AppManager;
import com.example.util.FileUtils;
import com.webdesign688.shot360.R;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;
/**
 * 拍照
 * @author liujun
 *
 */
public class SetOptionsActivity extends Activity {
	private static SocketManager socketManager;
	private static SurfaceView surface;
	private static SurfaceHolder holder;
	private static Camera camera;
	private GestureDetector gestureDetector;
	private static FileUtils fileUtils;
	private ListenerSocketReceiver lisSocketReceiver;
	private Intent serviceIntent;
	private static String imagename;
    private static Context context =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=SetOptionsActivity.this;
		/*IntentFilter socketFilter = new IntentFilter();
		socketFilter.addAction(BroadcastFinal.BROAD_LISTENER_SOCKET);
		lisSocketReceiver = new ListenerSocketReceiver();
		registerReceiver(lisSocketReceiver, socketFilter);

		serviceIntent = new Intent();
		// 开启服务
		serviceIntent.setClass(this, ClientNetService.class);
		serviceIntent.setAction("sljppff.service.ClientNetService");
		startService(serviceIntent);*/
		AppManager.getAppManager().addActivity(this);
		socketManager = new SocketManager(handler);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_set_options);
		fileUtils =new FileUtils(this);
		surface = (SurfaceView) findViewById(R.id.camera_surface);
		holder = surface.getHolder();
		holder.addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				camera.stopPreview();
				camera.release();
				camera = null;
				holder = null;
				surface = null;
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (camera == null) {
					camera = Camera.open();
					camera.setDisplayOrientation(90);
					try {
						camera.setPreviewDisplay(holder);
						camera.startPreview();
					} catch (IOException e) {
					
					}
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
		gestureDetector = new GestureDetector(this, new MyGestureListener());
	}

	@Override
	protected void onResume() {
	
		super.onResume();
	}

	static PictureCallback jpeg = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				camera.stopPreview();
             //保存图片到某个地方 。
				String imagename1 = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		imagename = "LJPPFF" + "_" + imagename1 + ".png";
		try {
			fileUtils.savaBitmap(imagename, bitmap);
		} catch (IOException e) {
		
		}
	
       //startagain();
			} catch (Exception e) {
			
			}
		}

		
	};
	private static void startagain() {
		surface = (SurfaceView)((Activity) context).findViewById(R.id.camera_surface);
		holder = surface.getHolder();
		holder.addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				camera.stopPreview();
				camera.release();
				camera = null;
				holder = null;
				surface = null;
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (camera == null) {
					camera = Camera.open();
					camera.setDisplayOrientation(90);
					try {
						camera.setPreviewDisplay(holder);
						camera.startPreview();
					} catch (IOException e) {
					
					}
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
    public  static void MakeCamera(){
		camera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (success) {
					Log.d("LJPPFF", "onAutoFocus  success");
					Parameters params = camera.getParameters();
					//获取预览的各种分辨率
					List<Size> supportedPreviewSizes = params.getSupportedPreviewSizes(); 
					//获取摄像头支持的各种分辨率
					int a =0;
				     for(int i=0;i<supportedPreviewSizes.size();i++){
				    	 if((supportedPreviewSizes.get(i).width)>1024){
			                 a =i;
			                 break;
	                   	}
				     }
				     if(a==0){
				    	 a =supportedPreviewSizes.size();
				     }
				    // a =supportedPreviewSizes.size()-1;
					params.setPictureFormat(PixelFormat.JPEG);
					params.setPreviewSize(  supportedPreviewSizes.get(a).width,   supportedPreviewSizes.get(a).height);
					params.set("rotation", 90);
				
					camera.setParameters(params);
					Log.d("LJPPFF", "camera.setParameters");

					camera.takePicture(new ShutterCallback() {
						
						@Override
						public void onShutter() {
							
						}
					}, new PictureCallback() {
						@Override
						public void onPictureTaken(byte[] data, Camera camera) {
							Log.d("LJPPFF", "onPictureTaken");

						}
					}, jpeg);
				}
			}
		});

    }
  private Handler	handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			}
		}
	};

    public static void SendFile(){
		Thread sendThread = new Thread(new Runnable(){
			@Override
			public void run() {
				List<String> filename =new ArrayList<String>();
				filename.clear();
				filename.add(imagename);
				List<String> path =new ArrayList<String>();
				path.clear();
				try {
					path.add(FileUtils.getStorageDirectory()+imagename);
					socketManager.SendFile(filename, path, BroadcastFinal.CONNECTSOCKET, 9999);

				} catch (Exception e) {
				
				}
			}
		});
		sendThread.start();

    }
	public static String  getSDCardRoot() throws Exception{
		String SDCardRoot;
		if(isCardExist()){
			SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		}else{
			throw new Exception();
		}
		return SDCardRoot;
	}
	public static boolean isCardExist(){
		boolean isCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false;
		return isCardExist;	
	}

	class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			camera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					if (success) {
						Parameters params = camera.getParameters();
						//获取预览的各种分辨率
						List<Size> supportedPreviewSizes = params.getSupportedPreviewSizes(); 
						//获取摄像头支持的各种分辨率
						int a =0;
					     for(int i=0;i<supportedPreviewSizes.size();i++){
					    	 if((supportedPreviewSizes.get(i).width)>1024){
				                 a =i;
				                 break;
		                   	}
					     }
					     if(a==0){
					    	 a =supportedPreviewSizes.size();
					     }
					    // a =supportedPreviewSizes.size()-1;
						params.setPictureFormat(PixelFormat.JPEG);
						params.setPreviewSize(  supportedPreviewSizes.get(a).width,   supportedPreviewSizes.get(a).height);
						params.set("rotation", 90);
					
						camera.setParameters(params);
                        
						camera.takePicture(new ShutterCallback() {
							
							@Override
							public void onShutter() {
								
							}
						}, new PictureCallback() {
							@Override
							public void onPictureTaken(byte[] data, Camera camera) {
								
							}
						}, jpeg);
					}
				}
			});
			return super.onSingleTapConfirmed(e);
		}
	}
}
