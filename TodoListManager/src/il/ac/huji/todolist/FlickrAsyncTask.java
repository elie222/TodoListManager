package il.ac.huji.todolist;

import java.io.IOException;
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
	private ImageView _imageMap;
	private Bitmap _map;
	private ProgressDialog _progressDialog;

	public FlickrAsyncTask(Context context, GridView resultGridView) {
		_context = context;
		_resultGridView = resultGridView;
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
//		resultTextView.setText(String.format("Coordinates: %2.2f, %2.2f",
//				result[0], result[1]));
		_imageMap.setImageBitmap(_map);
		_imageMap.requestLayout();
		super.onPostExecute(result);
	}

	@Override
	protected double[] doInBackground(String... params) {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Flickr flickr = new Flickr(params[0]);
		
		try {
			photos = flickr.getPhotos();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// TODO change this line
		_map = loadBitmap(photos.get(0).getImageThumbnailAddress());
		return new double[] { flickr.getLatitude(), flickr.getLongitude() };
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