package il.ac.huji.todolist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.SimpleCursorAdapter;

public class TwitterAsyncTask extends AsyncTask<String, Void, ArrayList<Tweet>> {

	private Context _context;
	private String _tag;
	private TodoDAL _todoDal;
	private ProgressDialog progressDialog;
	private SimpleCursorAdapter _adapter;

	public TwitterAsyncTask(Context context, String tag, TodoDAL todoDal, SimpleCursorAdapter adapter) {
		_context = context;
		_tag = tag;
		_todoDal = todoDal;
		_adapter = adapter;
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
                	for (int i=0; i<result.size(); i++) {
                		//TODO date
                		_todoDal.insert(new Task(result.get(i).getText(), new Date(1)));
//                		_todoDal.insert(new Task(result.get(i).getText(), result.get(i).getCreatedAt()));

                    	Cursor cursor = _todoDal.getCursor();
                    	_adapter.changeCursor(cursor);
                    	_adapter.notifyDataSetChanged();
                    	
                	}
                }
            })
            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Do nothing
                }
            });
			
			builder.create().show();
		}

		super.onPostExecute(result);
	}

	@Override
	protected ArrayList<Tweet> doInBackground(String... params) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		Twitter twitter = new Twitter(_tag);
		try {
			tweets = twitter.getTweets();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return tweets;
	}

}