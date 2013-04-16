package il.ac.huji.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Twitter {
	
	final static String HASH_SYMBOL = "%23";
	
	private String _tag;
	
	public Twitter(String tag) {
		_tag = tag;
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

	public void getNewTweets() throws IOException, JSONException {
		
		URL url = new URL("http://search.twitter.com/search.json?q=" + HASH_SYMBOL + _tag);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String response = readStream(conn.getInputStream());//TODO crashes on this line.
		
		Log.d("TWITTER", response);

		JSONObject json = new JSONObject(response);
//		JSONArray arr = json.getJSONArray("results");
//		JSONObject res = arr.getJSONObject(0);
//		JSONObject geo = res.getJSONObject("geometry");
//		JSONObject loc = geo.getJSONObject("location");
//		latitude = loc.getDouble("lat");
//		longitude = loc.getDouble("lng");
	}

}
