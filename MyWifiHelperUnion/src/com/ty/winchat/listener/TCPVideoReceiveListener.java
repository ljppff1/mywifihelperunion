package com.ty.winchat.listener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ty.winchat.listener.inter.OnBitmapLoaded;
import com.ty.winchat.listener.model.bitmapTill;
import com.ty.winchat.util.Constant;

/**
 * ����������
 * @author wj
 * @creation 2013-5-16
 */
public class TCPVideoReceiveListener extends TCPListener{
	public static final int THREAD_COUNT=80;//�߳���
	
	private int port=Constant.VIDEO_PORT;
	//��������ͼƬ
	private ExecutorService executors=Executors.newFixedThreadPool(THREAD_COUNT);
	
	private OnBitmapLoaded bitmapLoaded;
	
	boolean isReceived;//�ս���Ĭ�������ڽ������ݵ�
	
	private static TCPVideoReceiveListener instance;
	
	private TCPVideoReceiveListener(){}
	
	public static TCPVideoReceiveListener getInstance(){
		return instance==null?instance=new TCPVideoReceiveListener():instance;
	}
	
	@Override
	void init() {
		setPort(port);
	}
	
	public void onReceiveData(final Socket socket) throws IOException{
		connectionReceive(socket);
	}
	
	private void connectionReceive(final Socket socket){
		executors.execute(new Runnable() {
			@Override
			public void run() {
				try {
				
					Bitmap bitmap = BitmapFactory.decodeStream(socket.getInputStream());
					String ip =socket.getInetAddress().toString();
					String b = ip.replace("/","");
					bitmapTill bt =new bitmapTill();
					bt.setBitmap(bitmap);
					bt.setIp(b);
					bitmapLoaded.onBitmapLoaded(bt);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public OnBitmapLoaded getBitmapLoaded() {
		return bitmapLoaded;
	}

	public void setBitmapLoaded(OnBitmapLoaded bitmapLoaded) {
		this.bitmapLoaded = bitmapLoaded;
	}

	@Override
	public void noticeReceiveError(IOException e) {
		
	}


	@Override
	public void noticeSendFileError(IOException e) {
		
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		isReceived=false;
		executors.shutdownNow();
		instance=null;
	}


}
