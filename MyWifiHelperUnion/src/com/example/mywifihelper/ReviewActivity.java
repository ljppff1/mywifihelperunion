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


//����ҳ�棬�ǹ��ڳ���Ĵ��½��ܣ�ֻ��һ��TextView�ؼ�
public class ReviewActivity extends Activity {
    TextView textView;
	private GridView gridview;
	List<String> list =new ArrayList<String>();
	private Button mBtmakevideo;
    private int width = 120;//ÿ��Item�Ŀ��,���Ը���ʵ������޸�
    private int height = 150;//ÿ��Item�ĸ߶�,���Ը���ʵ������޸�
	private ImageShowManager imageManager;

	//ͼƬ������������GridView��ÿ��Item��ͼƬ���Ա��ͷ�
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
    			Toast.makeText(getApplicationContext(), "�ļ��������ر�", 0).show();
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
	    			Toast.makeText(getApplicationContext(), "��ʱû���ļ�", 0).show();
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
			// ���ȼ���Ƿ��Ѿ����߳��ڼ���ͬ������Դ�������ȡ������ģ�����������ظ�����
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
	 * ����imageview���������Ϊ��imageview�첽�������ݵĺ���
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
	 * ��¼imageview��Ӧ�ļ������񣬲�������Ĭ�ϵ�drawable
	 * 
	 * @author Administrator
	 * 
	 */
	public static class LoadingDrawable extends ColorDrawable {
		// ������drawable������ĵļ����߳�
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
	 * �жϵ�ǰ��imageview�Ƿ��ڼ�����ͬ����Դ
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
				// ��ͬ��url�Ѿ��ڼ�����.
				return false;
			}
		}
		return true;
	}
	/**
	 * �������ͼƬ���첽�߳�
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
			 * ����Ļ�ȡbitmap�Ĳ��֣����̣� ���ڴ滺������ȡ�����û����Ӳ�̻�������ȡ�����û�д�sd��/�����ȡ
			 */

			Bitmap bitmap = null;
			this.url = params[0];

			// ���ڴ滺�������ȡ
			bitmap = imageManager.getBitmapFromMemory(url);
			if (bitmap != null) {
				Log.d("dqq", "return by �ڴ�");
				return bitmap;
			}
			// ��Ӳ�̻��������ж�ȡ
			bitmap = imageManager.getBitmapFormDisk(url);
			if (bitmap != null) {
				imageManager.putBitmapToMemery(url, bitmap);
				Log.d("dqq", "return by Ӳ��");
				return bitmap;
			}
			
			// û�л������ԭʼλ�ö�ȡ
			bitmap = BitmapUtilities.getBitmapThumbnail(url,
					ImageShowManager.bitmap_width,
					ImageShowManager.bitmap_height);
			imageManager.putBitmapToMemery(url, bitmap);
			imageManager.putBitmapToDisk(url, bitmap);
			Log.d("dqq", "return by ԭʼ��ȡ");
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap resultBitmap) {
			if (isCancelled()) {
				// ��ȡ����
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
	* ���ر���ͼƬ
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
        while ( baos.toByteArray().length / 1024>256) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
            baos.reset();//����baos�����baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��  
            options -= 10;//ÿ�ζ�����10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ  
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
