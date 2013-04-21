package il.ac.huji.todolist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class TwitterAsyncTask extends AsyncTask<String, Void, ArrayList<Tweet>> {
	
	// for debugging purposes.
	final private static String TAG = "TWITTER_ASYNC";

	private Context _context;
	private String _tag;
//	private long _sinceId;
	private TodoDAL _todoDal;
	private ProgressDialog progressDialog;
	private SimpleCursorAdapter _adapter;
	private long _latestTweetId;

	public TwitterAsyncTask(Context context, String tag, TodoDAL todoDal, SimpleCursorAdapter adapter) {
		_context = context;
		_tag = tag;
//		_sinceId = sinceId;
		_todoDal = todoDal;
		_adapter = adapter;
		
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
		_latestTweetId = prefs.getLong(_tag + "SinceId", 0);
		
		Log.d(TAG, String.valueOf(_latestTweetId));
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(_context);
		progressDialog.setTitle("Twitter");
		//TODO change this line
		progressDialog.setMessage("Checking Twitter for new tweets with #" + _tag + " tag...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(final ArrayList<Tweet> result) {
		progressDialog.dismiss();
		
		if (result.size() > 0) {
			//show add new todo items dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(_context);
			
			builder.setMessage(result.size() + " tweets found. Would you like to do add them as todo items?")
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Create new todo items
//                	long latestTweetIdInResult = 0;
                	for (int i=0; i<result.size(); i++) {
                		if (result.get(i).getId() > _latestTweetId) {
	                		//TODO date
	                		_todoDal.insert(new Task(result.get(i).getText(), new Date(1)));
//	                		_todoDal.insert(new Task(result.get(i).getText(), result.get(i).getCreatedAt()));
                		}
                		
//                    	// so we don't add tweets as todos twice.
//                    	if (latestTweetIdInResult < result.get(i).getId()) {
//                    		latestTweetIdInResult = result.get(i).getId();
//                    	}
                	}
                	
                	Cursor cursor = _todoDal.getCursor();
                	_adapter.changeCursor(cursor);
                	_adapter.notifyDataSetChanged();
                	
//                	// so we don't add tweets as todos twice.
//                	// this if should always be true really.
//                	if (_latestTweetId < latestTweetIdInResult) {
//                		_latestTweetId = latestTweetIdInResult;
//                	}
                }
            })
			.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Do nothing
                }
            });
			
			builder.create().show();
		}
		
		// so that we don't add todo items for tweets we've already seen.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
		SharedPreferences.Editor editor = prefs.edit();
		
		long latestTweetId = findLatestTweetId(result);
		
//		Log.d(TAG, String.valueOf(prefs.getLong(_tag + "SinceId", -1)));
		
		if (latestTweetId > prefs.getLong(_tag + "SinceId", 0)) {
			editor.putLong(_tag + "SinceId", latestTweetId);
			editor.commit();
		}
		
//		Log.d(TAG, String.valueOf(prefs.getLong(_tag + "SinceId", -1)));

		super.onPostExecute(result);
	}

	@Override
	protected ArrayList<Tweet> doInBackground(String... params) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		Twitter twitter = new Twitter(_tag, _latestTweetId);
		try {
			tweets = twitter.getTweets();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return tweets;
	}
	
	private long findLatestTweetId(ArrayList<Tweet> tweets) {
		long latestTweetId = 0;
		
		for (int i=0; i<tweets.size(); i++) {
			if (tweets.get(i).getId() > latestTweetId) {
				latestTweetId = tweets.get(i).getId();
			}
		}
		
		return latestTweetId;
	}

}