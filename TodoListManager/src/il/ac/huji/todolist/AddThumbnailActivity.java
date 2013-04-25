package il.ac.huji.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class AddThumbnailActivity extends Activity {

//    private static final String IMAGES_LOCATION = "searchImages";
	private ImageAdapter _imageAdapter;
	private long _selectedItemId;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_thumbnail_layout);
        
        _selectedItemId = -1;
        
        //title
        setTitle("Add Thumbnail");
        
        //gridview
		final GridView gridView = (GridView) findViewById(R.id.gridViewResult);//TODO do i still need this?
		ArrayList<String> imageIds = new ArrayList<String>();
		_imageAdapter = new ImageAdapter(this, 0, imageIds);
		
		gridView.setAdapter(_imageAdapter);
		
		//for debugging only
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(AddThumbnailActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
	            _selectedItemId = id;
	            gridView.setSelection(position);
	        }
	    });
		
        
		//buttons
        findViewById(R.id.btnSearch).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String searchQuery = ((EditText) findViewById(R.id.edtFlickrSearch)).getText().toString();
				
				new FlickrAsyncTask(AddThumbnailActivity.this, gridView, searchQuery).execute();
				
				//TODO delete all thumbnails that were downloaded
			}
		});

        findViewById(R.id.btnOkThumbnailDialog).setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View v) {
        		
        		//TODO delete non-selected thumbnails that were downloaded
        		
				Intent resultIntent = new Intent();
				
				resultIntent.putExtra("title", getIntent().getExtras().getString("title"));
				resultIntent.putExtra("imageId", _selectedItemId);

				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
        
        findViewById(R.id.btnCancelThumbnailDialog).setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View v) {
        		
        		//TODO delete all thumbnailsthat were downloaded
        		
				setResult(RESULT_CANCELED);
				finish();
			}
		});
    }
}