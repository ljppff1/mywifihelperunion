package com.example.mywifihelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class SocketManager {
	private ServerSocket server;
	private  Handler handler = null;
	public SocketManager(Handler handler){
		this.handler = handler;
		final int port = 9999;
		/*while(port > 9000){
			try {
				server = new ServerSocket(port);
				break;
			} catch (Exception e) {
				port--;
			}
		}*/
		//SendMessage(1, port);
		try {
			server = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Thread receiveFileThread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){//接收文件
				  ReceiveFile();
				}
			}
		});
		receiveFileThread.start();
	}
	 void SendMessage(int what, Object obj){
		if (handler != null){
			Message.obtain(handler, what, obj).sendToTarget();
		}
	}
	//接收文件
	void ReceiveFile(){
		 FileOutputStream fos = null;
		 Socket client;
		DataInputStream dis = null;
		try{
			client = server.accept();
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
             client.close();
			
         /*   DataInputStream dataInput = new DataInputStream(name.getInputStream());    
            int size = dataInput.readInt();    
            byte[] data = new byte[size];    
            int len = 0;    
            while (len < size) {    
                len += dataInput.read(data, len, size - len);    
            }    
            ByteArrayOutputStream outPut = new ByteArrayOutputStream();    
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);    
			String savePath = Environment.getExternalStorageDirectory().getPath() + "/" + "1112";
			File  file1 =new File(savePath);
			if(!file1.exists()){
				file1.mkdirs();
			}
			FileOutputStream out = new FileOutputStream(savePath); 
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out); 
			out.flush(); 
			out.close(); */
/*			FileOutputStream file = new FileOutputStream(savePath, false);
			byte[] buffer = new byte[1024];
			int size = -1;
			while ((size = dataStream.read(buffer)) != -1){
				file.write(buffer, 0 ,size);       
			}                            
			file.close();
			dataStream.close();
*/		
             SendMessage(0,  " 接收完成 目录1133");
		}catch(Exception e){
			SendMessage(0, "接收错误:\n" + e.getMessage());
		}finally{
            try {
    			if(dis !=null)
                    dis.close();
                if(fos !=null)
                    fos.close();
				//server.close();
			} catch (IOException e) {
				SendMessage(0, "接收错误:\n" + e.getMessage());
			}
		}
	}
	/**
	
		//接收文件
	void ReceiveFile(So){
		try{
			//接收文件名
			InputStream nameStream = name.getInputStream();
			InputStreamReader streamReader = new InputStreamReader(nameStream);
			BufferedReader br = new BufferedReader(streamReader);
			String fileName = br.readLine();
			br.close();
			streamReader.close();
			nameStream.close();
			name.close();
			SendMessage(0, "正在接收:" + fileName);
			//接收文件数据
			Socket data = server.accept();
			InputStream dataStream = data.getInputStream();
			//String savePath = Environment.getExternalStorageDirectory().getPath() + "/" + fileName;
			String savePath ="1112";
			FileOutputStream file = new FileOutputStream(savePath, false);
			byte[] buffer = new byte[1024];
			int size = -1;
			while ((size = dataStream.read(buffer)) != -1){
				file.write(buffer, 0 ,size);
			}
			file.close();
			dataStream.close();
			data.close();
			SendMessage(0, fileName + " 接收完成");
		}catch(Exception e){
			SendMessage(0, "接收错误:\n" + e.getMessage());
		}
	}
	 */
	public  void SendFile(List<String> filename, List<String> path, String ipAddress, int port){
		try {
			for (int i = 0; i < filename.size(); i++){
			//	Socket name = new Socket(ipAddress, port);
			//	OutputStream outputName = name.getOutputStream();
				//OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
				//BufferedWriter bwName = new BufferedWriter(outputWriter);
			//	bwName.write(fileName.get(i));
				//bwName.close();
				//outputWriter.close();
			//	outputName.close();
			//	name.close();
			//	SendMessage(0, "正在发送" + fileName.get(i));
				
				 try {
			            Socket client = null;
						FileInputStream fis = null;
						DataOutputStream dos = null;
						try {
							client = new Socket(ipAddress, port);
			                //向服务端传送文件
			                File file =new File(path.get(i));
			                fis =new FileInputStream(file);
			                dos =new DataOutputStream(client.getOutputStream());
			                  
			                //文件名和长度
			                dos.writeUTF(file.getName());
			                dos.flush();
			                dos.writeLong(file.length());
			                dos.flush();
			                  
			                //传输文件
			                byte[] sendBytes =new byte[1024];
			                int length =0;
			                while((length = fis.read(sendBytes,0, sendBytes.length)) >0){
			                    dos.write(sendBytes,0, length);
			                    dos.flush();
			                }
			            }catch (Exception e) {
			                e.printStackTrace();
			            }finally{
			                if(fis !=null)
			                    fis.close();
			                if(dos !=null)
			                    dos.close();
			                client.close();
			            }
			        }catch (Exception e) {
			            e.printStackTrace();
			        }
				
			/*	
				Socket data = new Socket(ipAddress, port);
				OutputStream outputData = data.getOutputStream();
				//FileInputStream fileInput = new FileInputStream("111a");
				FileInputStream fileInput = new FileInputStream(path.get(i));
				int size = -1;
				byte[] buffer = new byte[1024];
				while((size = fileInput.read(buffer, 0, 1024)) != -1){
					outputData.write(buffer, 0, size);
				}
				outputData.close();
				fileInput.close();
				data.close();
				SendMessage(0, fileName.get(i) + " 发送完成");*/
			}
			SendMessage(0, "所有文件发送完成");
		} catch (Exception e) {
			SendMessage(0, "发送错误:\n" + e.getMessage());
		} 
	}
}
