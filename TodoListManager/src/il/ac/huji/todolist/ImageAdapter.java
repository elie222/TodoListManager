package il.ac.huji.todolist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String> _data;

    public ImageAdapter(Context context, int layoutResourceId, ArrayList<String> data) {
    	super(context, layoutResourceId, data);
    	
        mContext = context;
        _data = data;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
//    	Log.d("IMG ADAPTER", "GET VIEW FUNC");
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
        Log.d("IMG ADAPTER", _data.get(position));
        FileInputStream fis;
		try {
			fis = mContext.openFileInput(_data.get(position));
			Bitmap bm = BitmapFactory.decodeStream(fis);
	        imageView.setImageBitmap(bm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return imageView;
    }
}