package com.example.flickrtest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

/**
 * Class responsible for getting data from flikr and parsing them
 * @author VijayK
 *
 */
public class FlickrManager {

	// String to create Flickr API urls
	private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
	private static final String FLICKR_PHOTOS_SEARCH_STRING = "flickr.photos.search";
	private static final String FLICKR_GET_PHOTOS_STRING = "flickr.people.getPublicPhotos";
	private static final int FLICKR_PHOTOS_SEARCH_ID = 1;
	private static final int FLICKR_GET_PHOTOS = 2;
	private static final int NUMBER_OF_PHOTOS = 20;
	private static final String APIKEY_SEARCH_STRING = "&api_key=48ec9bfe715de3bcaa25ce45ab25ecb5";

	private static final String TAGS_STRING = "&tags=";
	private static final String USER_ID_STRING = "&user_id=";
	private static final String FORMAT_STRING = "&format=json";
	
	public static final int PHOTO_THUMB = 80;

	private static int CONNECT_TIMEOUT_MS = 5000;
	private static int READ_TIMEOUT_MS = 15000;

	/**
	 * Method to create URL
	 * @param methodId - showing which request it is
	 * @param parameter - attribute to be pass to the URL
	 * @return url string
	 */
	public static String createURL(int methodId, String parameter) {
		String method_type = "";
		String url = null;
		switch (methodId) {
		case FLICKR_PHOTOS_SEARCH_ID:
			method_type = FLICKR_PHOTOS_SEARCH_STRING;
			url = FLICKR_BASE_URL + method_type + APIKEY_SEARCH_STRING + TAGS_STRING + parameter + FORMAT_STRING 
					+ "&media=photos";
			break;
		case FLICKR_GET_PHOTOS:
			method_type = FLICKR_GET_PHOTOS_STRING;
			url = FLICKR_BASE_URL + method_type + APIKEY_SEARCH_STRING  + USER_ID_STRING + parameter + FORMAT_STRING;
			break;
		}
		return url;
	}

	/**
	 * Get data and parse
	 * @param param - parameter
	 * @return List<Model>
	 */
	public List<Model> getData(String param) {

		String jsonString = null;
		String url = null;
		List<Model> modelList = new ArrayList<Model>();
		if(param != null)
			url = createURL(FLICKR_GET_PHOTOS,param);
		else 
			url = createURL(FLICKR_PHOTOS_SEARCH_ID, "squirrel");
		try {
			ByteArrayOutputStream baos = readBytes(url);
			jsonString = baos.toString();
			JSONObject root = new JSONObject(jsonString.replace("jsonFlickrApi(", "").replace(")", ""));
			JSONObject photos = root.getJSONObject("photos");
			JSONArray imageJSONArray = photos.getJSONArray("photo");
			for (int i = 0; i < imageJSONArray.length(); i++) {
				JSONObject item = imageJSONArray.getJSONObject(i);
				Model model = new Model();
				model.setAuthorName(item.getString("owner"));
				model.setTitle(item.getString("title"));
				model.setId(item.getString("id"));
				model.setSecretId(item.getString("secret"));
				model.setFarmId(item.getString("farm"));
				model.setServerId(item.getString("server"));
				modelList.add(model);
			}
			return modelList;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Read all bytes from urlS
	 * 
	 * @param urlS
	 * @return ByteArrayOutputStream with content or null
	 */
	public static ByteArrayOutputStream readBytes(String urlS) {
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		HttpURLConnection httpURLConnection = null;
		try {
			// HTTP connection reuse which was buggy pre-froyo
			if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
				System.setProperty("http.keepAlive", "false");
			}
			URL url = new URL(urlS);
			Log.i("URL", url.toString());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			int response = httpURLConnection.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
				httpURLConnection.setReadTimeout(READ_TIMEOUT_MS);
				is = new BufferedInputStream(httpURLConnection.getInputStream());

				int size = 1024;
				byte[] buffer = new byte[size];

				baos = new ByteArrayOutputStream();
				int read = 0;
				while ((read = is.read(buffer)) != -1) {
					if (read > 0) {
						baos.write(buffer, 0, read);
						buffer = new byte[size];
					}

				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (httpURLConnection != null) {
				try {
					httpURLConnection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return baos;
	}
}
