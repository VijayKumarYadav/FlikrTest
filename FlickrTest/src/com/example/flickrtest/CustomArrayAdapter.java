/**
 * 
 */
package com.example.flickrtest;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.flickrtest.AsyncImageLoader.ImageCallback;

/**
 * Array Adapter for List
 * @author VijayK
 * 
 */
public class CustomArrayAdapter extends ArrayAdapter<Model> {
	private final LayoutInflater mInflater;
	private ViewCache viewCache;
	private AsyncImageLoader asyncImageLoader;
	private ListView listView;

	public CustomArrayAdapter(Context context, ListView listView) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		asyncImageLoader = new AsyncImageLoader();
		this.listView = listView;
	}

	/**
	 * Set the data
	 * @param data
	 */
	public void setData(List<Model> data) {
		clear();
		if (data != null) {
			for (Model appEntry : data) {
				add(appEntry);
			}
		}
	}

	/**
	 * Populate new items in the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.single_item, parent, false);
			viewCache = new ViewCache(view);
			view.setTag(viewCache);
		} else {
			view = convertView;
			viewCache = (ViewCache) view.getTag();
		}

		Model item = getItem(position);

		// Load the image and set it on the ImageView
		String imageUrl = getUrl(item);
		ImageView imageView = viewCache.getImageView();
		imageView.setTag(imageUrl);
		Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
				if (imageViewByTag != null) {
					imageViewByTag.setImageDrawable(imageDrawable);
				}
			}
		});
		imageView.setImageDrawable(cachedImage);
		// Set the text on the TextView
		viewCache.getTitleView().setText(item.getTitle());
		viewCache.getAuthorView().setText(item.getAuthorName());

		return view;
	}

	/**
	 * Get Image URL
	 * @param item
	 * @return URL in string form
	 */
	private String getUrl(Model item) {
		return "http://farm" + item.getFarmId() + ".staticflickr.com/" + item.getServerId() + "/" + item.getId() + "_" + item.getSecretId() + "_t.jpg"; 
	}
	
	/**
	 * Class use to cache the view to avoid expensive call of findViewById
	 *
	 */
	public static class ViewCache {
		private View baseView;
		private TextView titleView;
		private TextView authorView;
		private ImageView imageView;

		public ViewCache(View baseView) {
			this.baseView = baseView;
		}

		public TextView getTitleView() {
			if (titleView == null) {
				titleView = (TextView) baseView.findViewById(R.id.title);
			}
			return titleView;
		}
		
		public TextView getAuthorView() {
			if (authorView == null) {
				authorView = (TextView) baseView.findViewById(R.id.author);
			}
			return authorView;
		}


		public ImageView getImageView() {
			if (imageView == null) {
				imageView = (ImageView) baseView.findViewById(R.id.image_icon);
			}
			return imageView;
		}
	}
}