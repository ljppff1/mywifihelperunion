package com.example.mywifihelper;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jpgtovideo.VideoCapture;
import com.example.testalbums.ImageShowManager;
import com.example.util.BitmapUtilities;
import com.example.videotest.SurfaceViewTestActivity;
import com.webdesign688.shot360.R;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


//关于页面，是关于程序的大致介绍，只有一个TextView控件
public class ReviewActivity extends Activity {
    TextView textView;
	private GridView gridview;
	List<String> list =new ArrayList<String>();
	private Button mBtmakevideo;
    private int width = 120;//每个Item的宽度,可以根据实际情况修改
    private int height = 150;//每个Item的高度,可以根据实际情况修改
	private ImageShowManager imageManager;

	//图片缓存用来保存GridView中每个Item的图片，以便释放
    public static Map<String,Bitmap> gridviewBitmapCaches = new HashMap<String,Bitmap>();

    private Handler handler =new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case 2:
				progressHorizontal.setProgress(Integer.valueOf((String) msg.obj));
				break;
			case 3:
		    
				Intent intent =new Intent(getApplicationContext(), SurfaceViewTestActivity.class);
				intent.putExtra("PATHSTRING",(String) msg.obj);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;

    		case 1:
    			Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
    			break;
    		case 4:
    			Toast.makeText(getApplicationContext(), "文件服务出错关闭", 0).show();
    			break;

    		default:
    			break;
    		}
    	}; 
      };
	private ProgressBar progressHorizontal;

      
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_review);
		imageManager = ImageShowManager.from(ReviewActivity.this);

		list =getIntent().getStringArrayListExtra("PATH");
		if(list.size()==1){
			for(int i=0;i<15;i++){
				list.add(list.get(0));
			}
		}else if(list.size()==2){
			for(int i=0;i<7;i++){
				if(i==0||i==3||i==5){
				list.add(list.get(0));
				}
				else{
					list.add(list.get(1));
				}
					
			}
		}
		initView();
				
			}
	private void initView() {
		gridview =(GridView)this.findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter() );
		mBtmakevideo =(Button)this.findViewById(R.id.mBtmakevideo);
		mBtmakevideo.setOnClickListener(listener);
        progressHorizontal = (ProgressBar) findViewById(R.id.progress_horizontal);

		
	}
    
    OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mBtmakevideo:
				if(list.size()>0){ 
				VideoCapture.start(ReviewActivity.this, "1133",handler,"8",list);
				}else{
	    			Toast.makeText(getApplicationContext(), "暂时没有文件", 0).show();
				}
				break;   
			default:
				break;
			}
		}
	};

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.item_grid_image,
						parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.mTVjob1 = (TextView) view.findViewById(R.id.mTVjob1);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String path = list.get(position);
			// 首先检测是否已经有线程在加载同样的资源（如果则取消较早的），避免出现重复加载
			if (cancelPotentialLoad(path, holder.imageView)) {
				AsyncLoadImageTask task = new AsyncLoadImageTask(holder.imageView);
				holder.imageView.setImageDrawable(new LoadingDrawable(task));
				task.execute(path);
			}

		/*	
		    String url =list.get(position);
		  
		    
			holder.imageView.setImageBitmap(getLoacalBitmap(list.get(position)));
*/
			return view;
		}

	}
	/**
	 * 根据imageview，获得正在为此imageview异步加载数据的函数
	 * 
	 * @param imageview
	 * @return
	 */
	private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageview) {
		if (imageview != null) {
			Drawable drawable = imageview.getDrawable();
			if (drawable instanceof LoadingDrawable) {
				LoadingDrawable loadedDrawable = (LoadingDrawable) drawable;
				return loadedDrawable.getLoadImageTask();
			}
		}
		return null;
	}

	/**
	 * 记录imageview对应的加载任务，并且设置默认的drawable
	 * 
	 * @author Administrator
	 * 
	 */
	public static class LoadingDrawable extends ColorDrawable {
		// 引用与drawable相关联的的加载线程
		private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;

		public LoadingDrawable(AsyncLoadImageTask loadImageTask) {
			super(Color.BLUE);
			loadImageTaskReference = new WeakReference<AsyncLoadImageTask>(
					loadImageTask);
		}

		public AsyncLoadImageTask getLoadImageTask() {
			return loadImageTaskReference.get();
		}
	}

	/**
	 * 判断当前的imageview是否在加载相同的资源
	 * 
	 * @param url
	 * @param imageview
	 * @return
	 */
	private boolean cancelPotentialLoad(String url, ImageView imageview) {

		AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
		if (loadImageTask != null) {
			String bitmapUrl = loadImageTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				loadImageTask.cancel(true);
			} else {
				// 相同的url已经在加载中.
				return false;
			}
		}
		return true;
	}
	/**
	 * 负责加载图片的异步线程
	 * 
	 * @author Administrator
	 * 
	 */
	class AsyncLoadImageTask extends AsyncTask<String, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;
		private String url = null;

		public AsyncLoadImageTask(ImageView imageview) {
			super();
			imageViewReference = new WeakReference<ImageView>(imageview);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			/**
			 * 具体的获取bitmap的部分，流程： 从内存缓冲区获取，如果没有向硬盘缓冲区获取，如果没有从sd卡/网络获取
			 */

			Bitmap bitmap = null;
			this.url = params[0];

			// 从内存缓存区域读取
			bitmap = imageManager.getBitmapFromMemory(url);
			if (bitmap != null) {
				Log.d("dqq", "return by 内存");
				return bitmap;
			}
			// 从硬盘缓存区域中读取
			bitmap = imageManager.getBitmapFormDisk(url);
			if (bitmap != null) {
				imageManager.putBitmapToMemery(url, bitmap);
				Log.d("dqq", "return by 硬盘");
				return bitmap;
			}
			
			// 没有缓存则从原始位置读取
			bitmap = BitmapUtilities.getBitmapThumbnail(url,
					ImageShowManager.bitmap_width,
					ImageShowManager.bitmap_height);
			imageManager.putBitmapToMemery(url, bitmap);
			imageManager.putBitmapToDisk(url, bitmap);
			Log.d("dqq", "return by 原始读取");
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap resultBitmap) {
			if (isCancelled()) {
				// 被取消了
				resultBitmap = null;
			}
			if (imageViewReference != null) {
				ImageView imageview = imageViewReference.get();
				AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
				if (this == loadImageTask) {
					imageview.setImageDrawable(null);
					imageview.setImageBitmap(resultBitmap);
				}

			}

			super.onPostExecute(resultBitmap);
		}
	}

	/**
	* 加载本地图片
	* http://bbs.3gstdy.com
	* @param url
	* @return
	*/
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return comp(BitmapFactory.decodeStream(fis));
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	
	/**
	 * 压缩图片
	 * @param image
	 * @return
	 */
	public static   Bitmap comp(Bitmap image) {
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();         
		    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		    if( baos.toByteArray().length / 1024>512) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
		        baos.reset();//重置baos即清空baos  
		        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中  
		    }  
		    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
		    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
		    //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		    newOpts.inJustDecodeBounds = true;  
		    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		    newOpts.inJustDecodeBounds = false;  
		    int w = newOpts.outWidth;  
		    int h = newOpts.outHeight;  
		    //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
		    float hh =1024f;//这里设置高度为800f  
		    float ww = 720f;//这里设置宽度为480f  
		    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		    int be = 1;//be=1表示不缩放  
		    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
		        be = (int) (newOpts.outWidth / ww);  
		    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
		        be = (int) (newOpts.outHeight / hh);  
		    }  
		    if (be <= 0)  
		        be = 1;  
		    newOpts.inSampleSize = be;//设置缩放比例  
		    newOpts.inPreferredConfig = Config.RGB_565;
		    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		    isBm = new ByteArrayInputStream(baos.toByteArray());  
		    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	        try {
				isBm.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			
			}

		    return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
	}
	/**
	 * 质量压缩
	 * @param image
	 * @return 
	 */
	public static   Bitmap compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 90;  
        while ( baos.toByteArray().length / 1024>256) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        try {
			isBm.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
		}
        return bitmap;  
    } 
	
	
	
	
	static class ViewHolder {
		ImageView imageView;
		TextView mTVjob1;
	}

}
