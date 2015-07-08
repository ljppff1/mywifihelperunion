package com.example.service;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mywifihelper.MainViewActivity;
import com.example.mywifihelper.ServiceActivity;
import com.example.mywifihelper.SetOptionsActivity;
import com.example.util.AppManager;
import com.ty.winchat.ui.VideoChatClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
/**
 * 处理Socket是的广播类
 * 
 * @author hurenji
 */

public class OperConnReceiver extends BroadcastReceiver {

	private Context context;
	private String json_result;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.context = context;
		
		Bundle bundle = intent.getBundleExtra("bundle");
		json_result = bundle.getString("json");
        
		Toast.makeText(context, json_result, 0).show();
		
		if(!TextUtils.isEmpty(json_result)&&json_result.length()>=6){
			int jj=json_result.length();
			for(int i=0;i<json_result.length()-5;i++){
				if(json_result.substring(i, i+6).equals("CAMERA")){
					Log.d("LJPPFF", "MakeCamera");
					   if(AppManager.getAppManager().hasActivity(SetOptionsActivity.class)){
                 SetOptionsActivity.MakeCamera();
					   }
				}
			}
		}
 		if(!TextUtils.isEmpty(json_result)&&json_result.length()>=4){
			for(int i=0;i<json_result.length()-3;i++){
				if(json_result.substring(i, i+4).equals("FILE")){
					Log.d("LJPPFF", "SendFile");
	                 if(AppManager.getAppManager().hasActivity(SetOptionsActivity.class)){
                 SetOptionsActivity.SendFile();
	                 }
				}
			}
		}
 		if(!TextUtils.isEmpty(json_result)&&json_result.length()>=4){
			for(int i=0;i<json_result.length()-3;i++){
				if(json_result.substring(i, i+4).equals("TEST")){
					Log.d("LJPPFF", "TEST");
					Intent intent1 =new Intent(context.getApplicationContext(), VideoChatClient.class);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent1.putExtra("IP", BroadcastFinal.CONNECTSOCKET);
					context.startActivity(intent1);
				}
			}
		}
		if(!TextUtils.isEmpty(json_result)&&json_result.length()>=6){
			for(int i=0;i<json_result.length()-5;i++){
				if(json_result.substring(i, i+6).equals("OPTION")){

					Intent intent1 =new Intent(context.getApplicationContext(),SetOptionsActivity.class);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent1);
				}
			}
		}
		if(!TextUtils.isEmpty(json_result)&&json_result.length()>=6){
			for(int i=0;i<json_result.length()-5;i++){
				if(json_result.substring(i, i+6).equals("OPTAGA")){
                 if(AppManager.getAppManager().hasActivity(SetOptionsActivity.class)){
                	AppManager.getAppManager().finishActivity();
                 }
				}
			}
		}
		if(!TextUtils.isEmpty(json_result)&&json_result.length()>=6){
			for(int i=0;i<json_result.length()-5;i++){
				if(json_result.substring(i, i+6).equals("OPTAGB")){
                 if(AppManager.getAppManager().hasActivity(VideoChatClient.class)){
                	AppManager.getAppManager().finishActivity();
                 }
				}
			}
		}
	}

	
}
