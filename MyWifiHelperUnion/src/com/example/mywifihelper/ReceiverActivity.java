package com.example.mywifihelper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.webdesign688.shot360.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
public class ReceiverActivity extends Activity {
	private Button receiver;
	private TextView show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nt);
		initView();
	
	}

	private void initView() {
		receiver =(Button)this.findViewById(R.id.receiver);
		show =(TextView)this.findViewById(R.id.show);
		
	}
	public void receiver(){
		try{
		 //�������շ����׽��� ����  ����send������DatagramPacket��ip��ַ��˿ں�һ��  
        DatagramSocket socket = new DatagramSocket(9001,  
                InetAddress.getByName("192.168.1.11"));  
        //�������ݵ�buf���鲢ָ����С  
        byte[] buf = new byte[1024];  
        //�����������ݰ����洢��buf��  
        DatagramPacket packet = new DatagramPacket(buf, buf.length);  
        //���ղ���  
        socket.receive(packet);  
        byte data[] = packet.getData();// ���յ�����  
        InetAddress address = packet.getAddress();// ���յĵ�ַ  
        System.out.println("���յ��ı�:::" + new String(data));  
        System.out.println("���յ�ip��ַ:::" + address.toString());  
        System.out.println("���յĶ˿�::" + packet.getPort()); // 9004  

        // ���߷����� �ҽ��������  
        String temp = "�ҽ��������";  
        byte buffer[] = temp.getBytes();  
        //�������ݱ���ָ�����͸� �����ߵ�socketaddress��ַ  
        DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length,  
                packet.getSocketAddress());  
        //����  
        socket.send(packet2);  
        //�ر�  
        socket.close();  
    } catch (SocketException e) {  
        e.printStackTrace();  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  
		
	}
	
}