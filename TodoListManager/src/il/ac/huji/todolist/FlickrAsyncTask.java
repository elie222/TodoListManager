package il.ac.huji.todolist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class FlickrAsyncTask extends AsyncTask<String, Void, double[]> {

	private Context _context;
	private GridView _resultGridView;
	private ProgressDialog _progressDialog;
	private String _searchQuery;

	public FlickrAsyncTask(Context context, GridView resultGridView, String searchQuery) {
		_context = context;
		_resultGridView = resultGridView;
		_searchQuery = searchQuery;
	}

	@Override
	protected void onPreExecute() {
		_progressDialog = new ProgressDialog(_context);
		_progressDialog.setTitle("Searching");
		_progressDialog.setMessage("Retrieving results from Flickr...");
		_progressDialog.setCancelable(false);
		_progressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(double[] result) {
		_progressDialog.dismiss();
		
		super.onPostExecute(result);
	}

	@Override
	protected double[] doInBackground(String... params) {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Flickr flickr = new Flickr(_searchQuery);
		
		try {
			photos = flickr.getPhotos();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		for (int i=0; i<)
		
		//TODO download image and save to local storage
		
		
//		return new double[] { flickr.getLatitude(), flickr.getLongitude() };
		return new double[] { 0, 0 };
	}

	private static Bitmap loadBitmap(URL url) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}