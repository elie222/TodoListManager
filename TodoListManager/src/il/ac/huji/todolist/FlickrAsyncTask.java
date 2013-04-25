package il.ac.huji.todolist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

public class FlickrAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

	private Context _context;
	private GridView _resultGridView;
	private ProgressDialog _progressDialog;
	private String _searchQuery;
	
	private static final int MAX_PHOTOS_TO_DOWNLOAD = 4;

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
	protected void onPostExecute(ArrayList<String> result) {
		_progressDialog.dismiss();
		
		Log.d("FLICKR ASYNC", "LEN: " + result.size());
		
		ImageAdapter imageAdapter = new ImageAdapter(_context, 0, result);
		_resultGridView.setAdapter(imageAdapter);
		
		super.onPostExecute(result);
	}

	@Override
	protected ArrayList<String> doInBackground(String... params) {
		ArrayList<String> ids = new ArrayList<String>(MAX_PHOTOS_TO_DOWNLOAD);
		
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Flickr flickr = new Flickr(_searchQuery);
		
		try {
			photos = flickr.getPhotos();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		//download a maximum of MAX_PHOTOS_TO_DOWNLOAD images and save them to local storage
		for (int i=0; i<photos.size() && i < MAX_PHOTOS_TO_DOWNLOAD; i++) {
			try {
				Bitmap bm = loadBitmap(photos.get(i).getImageThumbnailURL());
				String location = photos.get(i).getId();
				FileOutputStream out = _context.openFileOutput(location, Context.MODE_PRIVATE);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.close();
				
				ids.add(location);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ids;
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