package com.example.service;

import java.io.IOException;

import com.example.mywifihelper.ClientActivity;
import com.example.mywifihelper.ServiceActivity;
import com.example.util.AppManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * ���������Ƿ�Ͽ��Ĺ�����
 * 
 * @author hurenji
 */
public class NetListenerUtil{


	/**
	 * ��Socket�Ͽ�ʱ���ر����к�Socket�йص�����
	 */
	public static void clostAllNet(Context context) {

			// �ر�IO��
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
			// �ر�Socket����
			if (ClientNetService.socket != null) {
				try {
					ClientNetService.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ClientNetService.socket = null;
			}
			// �ر�����Socket���߳�
			if (ClientNetService.NetThread != null) {
				ClientNetService.NetThread = null;
			}
			// �˳�ѭ����ȡ���������ݵ�While
			if (ClientNetService.writerThread != null) {
				ClientNetService.writerThread.setInitBooleanMain(false);
			}
			// �ر����������д���ݵ��߳�
			if (ClientNetService.writerThread != null) {
				ClientNetService.writerThread = null;
			}
			// �رշ���
			
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
