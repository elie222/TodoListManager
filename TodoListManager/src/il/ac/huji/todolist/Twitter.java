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

import android.util.Log;

public class Twitter {

	private String _tag;
	private long _sinceId;

	public Twitter(String tag, long sinceId) {
		_tag = tag;
		_sinceId = sinceId;
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

	//TODO limit to first 100 tweets
	public ArrayList<Tweet> getTweets() throws IOException, JSONException {
		
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		URL url = new URL("http://search.twitter.com/search.json?q=%23" + _tag
				+ "&rpp=100&since_id=" + _sinceId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String response = readStream(conn.getInputStream());

		JSONObject json = new JSONObject(response);
//		Log.d("TWITTER", json.toString());
		JSONArray jsonArr = json.getJSONArray("results");
		
		for (int i=0; i<jsonArr.length(); i++) {
//			Log.d("TWITTER", tweet);
			
			long id = jsonArr.getJSONObject(i).getLong("id");
			String text = jsonArr.getJSONObject(i).getString("text");
			String createdAt = jsonArr.getJSONObject(i).getString("created_at");
			tweets.add(new Tweet(id, text, createdAt));
		}
		
		return tweets;
	}

}