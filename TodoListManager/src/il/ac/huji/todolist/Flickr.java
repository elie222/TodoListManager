package il.ac.huji.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Flickr {
	
	private String _searchQuery;
	private final static String API_KEY = "818fc8131d1e4dee07f53b8e113eb516";
	private static final int MAX_IMAGES_TO_DOWNLOAD = 4;
	
	public Flickr(String searchQuery) {
		_searchQuery = searchQuery;
	}
	
	private static String readStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			buffer.append(line);
			buffer.append('\n');
		}
		return buffer.toString();
	}
	
	public ArrayList<Photo> getPhotos() throws IOException, JSONException {
		
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		URL url = new URL("http://www.flickr.com/services/rest/?method=flickr.photos.search&format=json&api_key=" + API_KEY
				+ "&text=" + _searchQuery + "&per_page=" + MAX_IMAGES_TO_DOWNLOAD);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String response = readStream(conn.getInputStream());
		
		String jsonString = response.substring(response.indexOf("(") + 1, response.lastIndexOf(")"));

		JSONObject json = new JSONObject(jsonString);
		JSONObject photosObj = json.getJSONObject("photos");
		JSONArray photosArr = photosObj.getJSONArray("photo");
		
		for (int i=0; i<photosArr.length(); i++) {
			JSONObject photoObj = photosArr.getJSONObject(i);
			
			String id = photoObj.getString("id");
			String secret = photoObj.getString("secret");
			String server = photoObj.getString("server");
			int farm = photoObj.getInt("farm");

			photos.add(new Photo(id, secret, server, farm));
		}
		
		return photos;
	}

}
