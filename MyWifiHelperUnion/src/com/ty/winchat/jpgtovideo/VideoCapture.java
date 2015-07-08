package com.ty.winchat.jpgtovideo;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.FrameRecorder.Exception;
import com.googlecode.javacv.cpp.opencv_core;
import com.ty.winchat.listener.model.bitmapTill;
/**
 *	
 */
public class VideoCapture {
		private static int switcher = 0;//¼���
		private static boolean isPaused = false;//��ͣ��
		private static String filepath = "";
		private static String filename = null;
		private static Handler handlerv;
		private static Context context;
		private static Double arandom;
		private static String path1;
		public static  int INDEX_MAX =1;
		public static void start(Context context,String path,final Handler handler,String a,final List<bitmapTill> lists){
			//init
			VideoCapture.context = context;
			if(path!=null){
				filepath = path;
			}else{
				filepath = "1111";
			}
		  handlerv =handler;
		  if(!TextUtils.isEmpty(a)){
		  arandom =Double.valueOf(a);
		  }else{
			  arandom=(double) 5;
		  }
			switcher = 1;
			new Thread(){
				private FFmpegFrameRecorder recorder;
				private File file;
				public void run(){
					OutputStream os = null;
					INDEX_MAX =lists.size()-1;
					if(lists.size()<0){
						return;
					}
					try {
					new FileUtils().creatSDDir(filepath);
					filename ="test.mp4";
					try{
					if(!new FileUtils().isFileExist("video.jpg",filepath)){					
						file = new FileUtils().createFileInSDCard("video.jpg", filepath);
					}else{
						file = new FileUtils().getFile("video.jpg",filepath);
					}
					os = new FileOutputStream(file);
					lists.get(0).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, os);
					os.flush();
					os.close();
						
					 recorder = new FFmpegFrameRecorder(
							new FileUtils().getSDCardRoot()+filepath+File.separator+filename,
							lists.get(0).getBitmap().getWidth(),
							lists.get(0).getBitmap().getHeight());
					path1 =new FileUtils().getSDCardRoot()+filepath+File.separator+filename;
					recorder.setFormat("mp4");
					recorder.setFrameRate(arandom);//¼��֡��
					recorder.start();

					}
					catch(java.lang.Exception e){
						e.printStackTrace();
					}
/*
					//Bitmap testBitmap = getImageFromAssetsFile("1.jpg");
					Bitmap	testBitmap=  BitmapFactory.decodeFile(MyAdapter.mSelectedImage.get(0));
					*/
					
					
					
					int index = 0;
					while(switcher!=0){
							if(!isPaused){	
								try{
							os = new FileOutputStream(file);
								lists.get(index).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, os);
								Log.i("VIDEOCAPTURE","index:"+index+"");
								os.flush();
								os.close();
							
							  opencv_core.IplImage image = cvLoadImage(new FileUtils().getSDCardRoot()+filepath+File.separator+"video.jpg");
								recorder.record(image);
								Log.i("VIDEOCAPTURE","!!! time++++:");
								}catch(java.lang.Exception e){
									
								}
	
							/*	File file = null;
								if(!new FileUtils().isFileExist("video.jpg",filepath)){					
									file = new FileUtils().createFileInSDCard("video.jpg", filepath);
								}else{
									file = new FileUtils().getFile("video.jpg",filepath);
								}
								os = new FileOutputStream(file);
								Bitmap frame = getImageFromAssetsFile(index+".jpg");
								
								list.add("/storage/emulated/0/AndroidData/LJPPFF_20150427111833");
								list.add("/storage/emulated/0/AndroidData/LJPPFF_20150427112024");
								list.add("/storage/emulated/0/AndroidData/LJPPFF_20150427112344");
								list.add("/storage/emulated/0/AndroidData/LJPPFF_20150427112355");
								list.add("/storage/emulated/0/AndroidData/LJPPFF_20150427112405");
								list.add("/storage/emulated/0/AndroidData/LJPPFF_20150427113504");

								
								frame.compress(Bitmap.CompressFormat.JPEG, 100, os);
								Log.i("VIDEOCAPTURE","index:"+index+"");
								os.flush();
								os.close();
								frame.recycle();
								frame = null;*/
								
							//	opencv_core.IplImage image = cvLoadImage(new FileUtils().getSDCardRoot()+filepath+File.separator+"video.jpg");
							//	opencv_core.IplImage image =null;
								/* int n = (int)(Math.random()*6+1);
								if(n==1){
									 image = cvLoadImage("/storage/emulated/0/AndroidData/LJPPFF_20150427113504.png");

								}else if(n==2){
									 image = cvLoadImage("/storage/emulated/0/AndroidData/LJPPFF_20150427112024.png");

								}else if(n==3){
									 image = cvLoadImage("/storage/emulated/0/AndroidData/LJPPFF_20150427112344.png");

								}else if(n==4){
									 image = cvLoadImage("/storage/emulated/0/AndroidData/LJPPFF_20150427112355.png");

								}else if(n==5){
									 image = cvLoadImage("/storage/emulated/0/AndroidData/LJPPFF_20150427112405.png");

								}else if(n==6){
									 image = cvLoadImage("/storage/emulated/0/AndroidData/LJPPFF_20150427113504.png");

								}else{
								 image = cvLoadImage("/storage/emulated/0/AndroidData/LJPPFF_20150427113504.png");
								}*/
							//	Log.i("VIDEOCAPTURE",MyAdapter.mSelectedImage.get(index));
							//	Log.i("VIDEOCAPTURE","!!! time++++:"+index);
							//	 image = cvLoadImage(MyAdapter.mSelectedImage.get(index));

								
							}
							if(index>INDEX_MAX){
								index = 0;
								Log.i("VIDEOCAPTURE","index = 1");
								switcher=0;
								Message msg =new Message();
								msg.what=3;
								msg.obj=path1;  
								 handler.sendMessage(msg);
								

								Log.i("VIDEOCAPTURE","recorder.stop();");
							}else{
								index++;
								Log.i("VIDEOCAPTURE","	index++;"+INDEX_MAX);
								Message msg =new Message();
								msg.what=2;
								msg.obj=index*100/INDEX_MAX +"";  
								if(index*100/INDEX_MAX<=100)
                                 handler.sendMessage(msg);
							}
						}
					try {
						recorder.stop();
						recorder=null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						Log.i("VIDEOCAPTURE","	index++;recorder.stop");
					}catch(FileUtils.NoSdcardException e){
						e.printStackTrace();
						Log.i("VIDEOCAPTURE","eeeeFileUtils.NoSdcardException "+e.toString());
					}
					
				}
			}.start();
		}
		public static void stop(){
			switcher = 0;
			isPaused = false;
		}
		public static void pause(){
			if(switcher==1){
				isPaused = true;
			}
		}
		public static void restart(){
			if(switcher==1){
				isPaused = false;
			}
		}
		public static boolean isStarted(){
			if(switcher==1){
				return true;
			}else{
				return false;
			}
		}
		public static boolean isPaused(){
			return isPaused;
		}
		private static Bitmap getImageFromAssetsFile(String filename){
			Bitmap image = null;
			AssetManager am = context.getResources().getAssets();
			try{			
				InputStream is = am.open(filename);
				image = BitmapFactory.decodeStream(is);
				is.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			return image;
		}
		
		/**
		 * ѹ��ͼƬ
		 * @param image
		 * @return
		 */
		public static   Bitmap comp(Bitmap image) {
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();         
			    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
			    if( baos.toByteArray().length / 1024>512) {//�ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���    
			        baos.reset();//����baos�����baos  
			        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos��  
			    }  
			    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
			    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
			    //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
			    newOpts.inJustDecodeBounds = true;  
			    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
			    newOpts.inJustDecodeBounds = false;  
			    int w = newOpts.outWidth;  
			    int h = newOpts.outHeight;  
			    //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ  
			    float hh =1024f;//�������ø߶�Ϊ800f  
			    float ww = 720f;//�������ÿ��Ϊ480f  
			    //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
			    int be = 1;//be=1��ʾ������  
			    if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����  
			        be = (int) (newOpts.outWidth / ww);  
			    } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����  
			        be = (int) (newOpts.outHeight / hh);  
			    }  
			    if (be <= 0)  
			        be = 1;  
			    newOpts.inSampleSize = be;//�������ű���  
			    newOpts.inPreferredConfig = Config.RGB_565;
			    //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
			    isBm = new ByteArrayInputStream(baos.toByteArray());  
			    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		        try {
					isBm.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			    return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��  
		}
		/**
		 * ����ѹ��
		 * @param image
		 * @return 
		 */ 
		public static   Bitmap compressImage(Bitmap image) {  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��  
	        int options = 90;  
	        while ( baos.toByteArray().length / 1024>129) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
	            baos.reset();//����baos�����baos  
	            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��  
	            options -= 10;//ÿ�ζ�����10  
	        }  
	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��  
	        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ  
	        try {
				isBm.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	        return bitmap;  
	    } 
		
		
		
		
		
}
