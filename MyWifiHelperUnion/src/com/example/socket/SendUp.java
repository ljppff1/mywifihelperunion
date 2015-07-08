package com.example.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
/**
 * 发送UDP消息的函数
 * @author paulwang
 *
 */
public class SendUp implements Runnable{
    
	private static String ip; 
	String ips;
   
	InetAddress inetAddress=null; 
	Thread t=null;    
    MulticastSocket multicastSocket=null; 
    InetAddress serverAddress=null;
	DatagramSocket socket=null;   
	private volatile boolean isFirstRuning= true;
	public void up(String number,String IP,String myname,String file,String file_length)
	{
	    ip=IP; 	
		ips=number+":"+IP+":"+myname+":"+file+":"+file_length;

		try {
			

			if(socket==null){
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.bind(new InetSocketAddress(3333));
			}

			serverAddress=InetAddress.getByName(IP);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		t=new Thread(this);
		t.start();
	}
	@Override
	public void run() {
	
	DatagramPacket datagramPacket=null;
	byte[] data=ips.getBytes();
	datagramPacket=new DatagramPacket(data,data.length, serverAddress,3024);
	try {
		
		socket.send(datagramPacket);
		System.out.println("999999999999999999999999999999已发送");
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
	
		
}
}