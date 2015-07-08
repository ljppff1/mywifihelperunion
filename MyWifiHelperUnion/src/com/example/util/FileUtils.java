package com.example.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 *  
 * @author 谭树林 
 *
 */
public class FileUtils {
	
	File file;
	/**
	 * @return file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file 要设置的 file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment
			.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存Image的目录名
	 */
	private static   String FOLDER_NAME = "/AndroidData/";

	public FileUtils(Context context) {
		mDataRootPath = context.getCacheDir().getPath();
	}

	/**
	 * 获取储存Image的目录
	 * 
	 * @return
	 */
	public static String getStorageDirectory() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? mSdRootPath + FOLDER_NAME
				: mDataRootPath + FOLDER_NAME;
	}

	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * 
	 * @param fileName
	 * @param bitmap
	 * @throws IOException
	 * str 用户id 层的目录
	 */
	public   void savaBitmap(String fileName, Bitmap bitmap) throws IOException {
		if (bitmap == null) {
			return;
		}
	    //Bitmap image = comp(bitmap);
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}
		File file = new File(path + File.separator + fileName);
		this.file = file;
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
	}

	/**
	 * 压缩图片
	 * @param image
	 * @return
	 */
	private   Bitmap comp(Bitmap image) {
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();         
		    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		    if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
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
		    float hh = 800f;//这里设置高度为800f  
		    float ww = 480f;//这里设置宽度为480f  
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
		    return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
	}
	/**
	 * 质量压缩
	 * @param image
	 * @return
	 */
	private   Bitmap compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 90;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
	/**
	 * 从手机或者sd卡获取Bitmap
	 * 
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName) {
		String pathname = getStorageDirectory()+ fileName;
		return BitmapFactory.decodeFile(pathname);
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.exists();
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.length();
	}

	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFileAll(String dir) {
		File dirFile = new File(getStorageDirectory() + File.separator + dir);
		if (!dirFile.exists()) {
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}

		dirFile.delete();
	}

	/**
	 * 删除指定文件
	 * 
	 * @param fileName
	 */
	public void deleteFile(String fileName) {
		File file = new File(getStorageDirectory() +  fileName);
		if (!file.exists()) {
			return;
		}
		file.delete();
	}
 

	/**
	 * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
	 * 
	 * @param sourceFilePath
	 *            待压缩的文件路径
	 * @param zipFilePath
	 *            压缩后存放路径
	 * @param fileName
	 *            压缩后文件的名称
	 * @return flag
	 */
	@SuppressWarnings("resource")
	public boolean fileToZip(String sourceFilePath, String zipFilePath,
			String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFile.exists() == false) {
			System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath
					+ " 不存在. <<<<<<");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				if (zipFile.exists()) {
					boolean b = zipFile.delete();

					System.out.println(">>>>>> " + zipFilePath + " 目录下存在名字为："
							+ fileName + ".zip" + " 打包文件. <<<<<<" + "  已删除："
							+ b);
				}
				File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath
							+ " 里面不存在文件,无需压缩. <<<<<<");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// 创建ZIP实体,并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(
								sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
						if (null != fis)
							fis.close();
						if (null != bis)
							bis.close();
					}
					flag = true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					
					if (null != fis)
						fis.close();
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
					if (null != fis)
						fis.close();
//					deleteFileAll(sourceFilePath.substring(sourceFilePath
//							.indexOf("newDir_")));
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}

		return flag;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newDirPath
	 *            String 复制后路径 如：f:/A/ newFileName 复制后的文件名
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	public void copyFile(String oldPath, String newDirPath, String newFileName) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (!oldfile.exists()) {
				oldfile.mkdirs();
				return;
			}
			if (!oldfile.isFile()) {
				return;
			}
			if (!oldfile.canRead()) {
				return;
			}

			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				File newFile = new File(newDirPath);
				if (!newFile.exists()) {
					newFile.mkdirs();
				}
				FileOutputStream fs = new FileOutputStream(newDirPath
						+ newFileName);
				byte[] buffer = new byte[1024];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}
	public File getFile(String fileName){
		String path = getStorageDirectory()+fileName;
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
		return file;
	}
	 // 将Bitmap转换成InputStream  
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);  
        InputStream is = new ByteArrayInputStream(baos.toByteArray());  
        return is;  
    } 
}