package com.example.service;

import java.io.IOException;

import com.example.mywifihelper.ClientActivity;
import com.example.mywifihelper.ServiceActivity;
import com.example.util.AppManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 监听网络是否断开的工具类
 * 
 * @author hurenji
 */
public class NetListenerUtil{


	/**
	 * 当Socket断开时，关闭所有和Socket有关的连接
	 */
	public static void clostAllNet(Context context) {

			// 关闭IO流
			if (ClientNetService.br != null) {
				try {
					ClientNetService.br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ClientNetService.br = null;
			}
			if (ClientNetService.bw != null) {
				try {
					ClientNetService.bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ClientNetService.bw = null;
			}
			// 关闭Socket连接
			if (ClientNetService.socket != null) {
				try {
					ClientNetService.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ClientNetService.socket = null;
			}
			// 关闭启动Socket的线程
			if (ClientNetService.NetThread != null) {
				ClientNetService.NetThread = null;
			}
			// 退出循环读取服务器数据的While
			if (ClientNetService.writerThread != null) {
				ClientNetService.writerThread.setInitBooleanMain(false);
			}
			// 关闭向服务器读写数据的线程
			if (ClientNetService.writerThread != null) {
				ClientNetService.writerThread = null;
			}
			// 关闭服务
			
		  if(ClientActivity.serviceIntent!=null){
			  context.stopService(ClientActivity.serviceIntent);
		  }
			
			Intent intent = new Intent();
			intent.setAction(BroadcastFinal.BROAD_LISTENER_SOCKET);
			Bundle bundle = new Bundle();
			bundle.putInt("flag", 0);
			intent.putExtra("bundle", bundle);
			context.sendBroadcast(intent);

	}

}
