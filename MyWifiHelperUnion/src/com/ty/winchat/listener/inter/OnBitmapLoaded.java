package com.ty.winchat.listener.inter;

import com.ty.winchat.listener.model.bitmapTill;

import android.graphics.Bitmap;

public interface OnBitmapLoaded {
	/**视屏传送过来图片加载完成的回调方法*/
	 void onBitmapLoaded(bitmapTill bitmap);
}
