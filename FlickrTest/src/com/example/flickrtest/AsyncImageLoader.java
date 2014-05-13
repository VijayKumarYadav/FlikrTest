/**
 * 
 */
package com.example.flickrtest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

/**
 * Load Image in Async manner. Uses Temporary cache to improve performance
 * @author VijayK
 * 
 */
public class AsyncImageLoader {
	private HashMap<String, SoftReference<Drawable>> drawableMap;

	public AsyncImageLoader() {
		drawableMap = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
		if (drawableMap.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = drawableMap.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final SendHandler handler = new SendHandler(imageUrl, imageCallback);
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				drawableMap.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	static class SendHandler extends Handler {

		private String imageUrl;
		private ImageCallback imageCallback;

		SendHandler(String imageUrl, ImageCallback imageCallback) {
			this.imageUrl = imageUrl;
			this.imageCallback = imageCallback;
		}

		@Override
		public void handleMessage(Message message) {
			imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
		}
	}

	public static Drawable loadImageFromUrl(String url) {
		InputStream inputStream = null;
		try {
			inputStream = new URL(url).openStream();
		} catch (IOException e) {
			//
		}
		return Drawable.createFromStream(inputStream, "src");

	}

	/**
	 * interface to work as a image callback
	 */
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
