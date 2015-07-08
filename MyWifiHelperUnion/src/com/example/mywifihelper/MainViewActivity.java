package com.example.mywifihelper;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.mywifihelper.ServiceActivity.ServerThread;
import com.example.mywifihelper.ServiceActivity.ServerThread1;
import com.example.service.BroadcastFinal;
import com.example.service.ClientNetService;
import com.example.service.ClientSocketIOWriteNet;
import com.example.service.ListenerSocketReceiver;
import com.ty.winchat.ui.VideoChatService;
import com.webdesign688.shot360.R;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
//根据功能跳转到下一个Activity
public class MainViewActivity  extends Base3Activity {
	private ListenerSocketReceiver lisSocketReceiver;
	private EditText mETIpAddress;
	private EditText txtEt;

	private LinearLayout ll1;
	private LinearLayout ll7;
	private LinearLayout ll8;
	public static Intent serviceIntent;
	private ServerSocket server;
	private ServerSocket server1;
	  private List<String> listf =new ArrayList<String>();
	  private List<Socket> list =new ArrayList<Socket>();
	  private List<BufferedWriter> listb =new ArrayList<BufferedWriter>();
      private int next =1;
      private Boolean flag =true;
      
      private Handler handler =new Handler(){
    	  public void handleMessage(Message msg) {
              switch (msg.what) {
  			case 0:
				txtEt.append("\n"+ msg.obj.toString());
				break;
			case 1:
				for(int i=0;i<list.size();i++){
					try {
						  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(list.get(i).getOutputStream()));
						  out.write("OPTION");
						  out.flush();
					} catch (IOException e) {
					}
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
	                  try {
	                		Message.obtain(handler, 0, "还有5s正式拍照").sendToTarget();
						Thread.sleep(5*1000);
						handler.sendEmptyMessage(2);
					} catch (InterruptedException e) {
					}
					}
				}).start();

				break;
			case 2:
				Message.obtain(handler, 0, "已拍照").sendToTarget();
				for(int i=0;i<list.size();i++){
					try {
						  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(list.get(i).getOutputStream()));
						  out.write("CAMERA");
						  out.flush();
					} catch (IOException e) {
					}
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
	                  try {
	                		Message.obtain(handler, 0, "还有10s接收文件").sendToTarget();
						Thread.sleep(10*1000);
						handler.sendEmptyMessage(3);
					} catch (InterruptedException e) {
					}
					}
				}).start();

				break;
			case 3:
				Message.obtain(handler, 0, "收文件").sendToTarget();
				for(int i=0;i<list.size();i++){
					try {
						  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(list.get(i).getOutputStream()));
						  out.write("FILE");
						  out.flush();
					} catch (IOException e) {
						
					}
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
	                  try {
	                	//	Message.obtain(handler, 0, "还有10s接收文件").sendToTarget();
						Thread.sleep(5*1000);
						handler.sendEmptyMessage(5);
					} catch (InterruptedException e) {
					}
					}
				}).start();


				
				
				break;
			case 4:
				flag =true;
				break;
			case 5:
				Message.obtain(handler, 0, "关闭照相机").sendToTarget();

				for(int i=0;i<list.size();i++){
					try {
						  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(list.get(i).getOutputStream()));
						  out.write("OPTAGA");
						  out.flush();
					} catch (IOException e) {
						
					}
				}
			
				new Thread(new Runnable() {
					@Override
					public void run() {
	                  try {
	                	Message.obtain(handler, 0, "还有5s重新开始").sendToTarget();
						Thread.sleep(5*1000);
						handler.sendEmptyMessage(4);
					} catch (InterruptedException e) {
						
					}
					}
				}).start();


				break;
			default:
				break;
			}
    	  };
      };
	private LinearLayout ll2;
	private Thread receiveFileThread;
	private Thread receiveThread;
	private LinearLayout lltest;
      
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_view);
	
      initView();
	
		IntentFilter socketFilter = new IntentFilter();
		socketFilter.addAction(BroadcastFinal.BROAD_LISTENER_SOCKET);
		lisSocketReceiver = new ListenerSocketReceiver();
		registerReceiver(lisSocketReceiver, socketFilter);

		serviceIntent = new Intent();
		// 开启服务
		serviceIntent.setClass(this, ClientNetService.class);
		serviceIntent.setAction("sljppff.service.ClientNetService");
		startService(serviceIntent);

		txtEt = (EditText)findViewById(R.id.et);
	//	Message.obtain(handler, 0, "正在发送至" + ipAddress + ":" +  port).sendToTarget();

		mETIpAddress =(EditText)this.findViewById(R.id.mETIpAddress);
		mETIpAddress.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				BroadcastFinal.CONNECTSOCKET =mETIpAddress.getText().toString();
			}
		});
	}
	@Override
	protected void onResume() {
		if(mETIpAddress.getText().toString().length()>8){
			BroadcastFinal.CONNECTSOCKET =mETIpAddress.getText().toString();
		}else{
			BroadcastFinal.CONNECTSOCKET = "192.168.1.4";
		}
		super.onResume();
	}
	
    private void initView() {
    	ll1=(LinearLayout) this.findViewById(R.id.ll1);
    	ll2=(LinearLayout) this.findViewById(R.id.ll2);
    	ll7=(LinearLayout) this.findViewById(R.id.ll7);
    	ll8=(LinearLayout) this.findViewById(R.id.ll8);
    	lltest =(LinearLayout)this.findViewById(R.id.lltest);
    	lltest.setOnTouchListener(new LayoutOnTouchListener());
    	ll1.setOnTouchListener(new LayoutOnTouchListener());
    	ll2.setOnTouchListener(new LayoutOnTouchListener());
    	ll7.setOnTouchListener(new LayoutOnTouchListener());
    	ll8.setOnTouchListener(new LayoutOnTouchListener());

	}
	private class LayoutOnTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			  int id = v.getId();
	            System.out.println("myssss");
	            switch(id) {
	            case R.id.lltest:
					for(int i=0;i<list.size();i++){
						try {
							  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(list.get(i).getOutputStream()));
							  out.write("TEST");
							  out.flush();
						} catch (IOException e) {
						}
					}
					Intent intent21 =new Intent(getApplicationContext(), VideoChatService.class);
					intent21.putExtra("IP", mETIpAddress.getText().toString());
					startActivityForResult(intent21, 1);
	            	break;
	            case R.id.ll1:
	            	if(flag){
	        			if (ClientNetService.writerThread != null) {
	        				ClientNetService.writerThread.setInitBooleanMain(false);
	        			}
	            	    flag =false;
                        donexttomyfirst();
	            	}
	            	break; 
	            case R.id.ll2:
	            	Intent intent2 =new Intent(getApplicationContext(), ReviewActivity.class);
	            	intent2.putStringArrayListExtra("PATH", (ArrayList<String>) listf);
	            	startActivity(intent2);
	            	break;
	            case R.id.ll7:
	            	Intent intent4=new Intent();
	                intent4.setClass(MainViewActivity.this,ServiceActivity.class);
	                startActivity(intent4);
	                break;    
	            case R.id.ll8:
	            	Intent intent5=new Intent();
	                intent5.setClass(MainViewActivity.this,SetOptionsActivity.class);
	                startActivity(intent5);
	                break; 
	            }
			return false;
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			for(int i=0;i<list.size();i++){
				try {
					  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(list.get(i).getOutputStream()));
					  out.write("OPTAGB");
					  out.flush();
				} catch (IOException e) {
					
				}
			}

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	//逻辑测算
	private void donexttomyfirst() {
	BroadcastFinal.isStart =true;
		//关闭服务
		stopService(serviceIntent);
		//开启控制服务
		if(receiveThread==null){
		receiveThread = new Thread(new Runnable() {
			@Override
			public void run() {
/*				if(server!=null){
					try {
						server.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						
					}
				}
*/           	 try {
           		 if(server==null)
					server=new ServerSocket(9000);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	            while(true){//监听客户端请求，启个线程处理
	            	 try {
	                Socket socket = server.accept();
	                new ServerThread(socket);
	            		} catch (IOException e1) {
							e1.printStackTrace();
						}
	            }
		}});
		receiveThread.start();
		}
      //开启文件服务
		 if(receiveFileThread==null){
			 receiveFileThread = new Thread(new Runnable(){
				

				@Override
				public void run() {
					listf.clear();
/*					if(server1!=null){
						try {
							server1.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							
						}
					}
*/	            	 try {
	                     if(server1==null)
	            		 server1=new ServerSocket(9999);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					boolean flag =true;
					while(true){
						Socket client;
						try {
							client = server1.accept();
							new ServerThread1(client);
						} catch (IOException e1) {
							
						}
					}
				}
			});
			receiveFileThread.start();
		 }
          //照相逻辑
		if(next==1){
			new Thread(new Runnable() {
				@Override
				public void run() {
                  try {
                		Message.obtain(handler, 0, "还有7s开启照相机").sendToTarget();
					Thread.sleep(7*1000);
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					
				}
				}
			}).start();
		}
		 
	}


	/**
	 * 服务器线程类
	 */
	class ServerThread extends Thread{
	    private Socket client;
	    private  InputStream in = null;
	    private String name;
		private BufferedWriter out;
	      
	    public ServerThread(Socket s)throws IOException{
	        client = s;
		      Log.i("LJPPFF", client.toString());

	        out =new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	        in = client.getInputStream();
	        boolean haS =false;
	      for(int i=0;i<list.size();i++){
	    	  if(list.get(i).equals(client)){
	    		  haS =true;
	    	  }
	      }
	      Log.i("LJPPFF","haS"+haS);

	      if(!haS){
	    	  list.add(client);
	    	  listb.add(out);
	      }
	 
	}
	}
	/**
	 * 文件接收 
	 * @author liujun
	 *
	 */
		class ServerThread1 extends Thread{
			 FileOutputStream fos = null;
			 Socket client;
			DataInputStream dis = null;
		      
		    public ServerThread1(Socket s)throws IOException{
				try{
			       client = s;
		           dis = new DataInputStream(client.getInputStream());
		           //文件名和长度
		           String fileName = dis.readUTF();
		           long fileLength = dis.readLong();
					 String savePath = Environment.getExternalStorageDirectory().getPath() + "/"+"1133" ;
					File  file1 =new File(savePath);
					if(!file1.exists()){
						file1.mkdirs();
					}
		             fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/1133/" + fileName));
		             String filestring = Environment.getExternalStorageDirectory().getPath() + "/1133/" + fileName;

		            boolean isflagf =false;
		             for(int i=0;i<listf.size();i++){
		            	if(listf.get(i).equals(filestring)){
		            		isflagf =true;
		            	}
		             }
		             if(!isflagf){
		            	 listf.add(filestring);
		             }
		             
		            byte[] sendBytes =new byte[1024];
		            int transLen =0;
		            while(true){
		                int read =0;
		                read = dis.read(sendBytes);
		                if(read == -1)
		                    break;
		                transLen += read;
		                System.out.println("接收文件进度" +100 * transLen/fileLength +"%...");
		                fos.write(sendBytes,0, read);
		                fos.flush();
		            }
             		Message.obtain(handler, 0,"完成文件"+ filestring).sendToTarget();

		            client.close();
					
				}catch(Exception e){
				}finally{
		           try {
		   			if(dis !=null)
		                   dis.close();
		               if(fos !=null)
		                   fos.close();
					} catch (IOException e) {
						
					}
				}
		  }
	}
	
}
