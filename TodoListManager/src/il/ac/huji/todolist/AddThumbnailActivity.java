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

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_thumbnail_layout);
        
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
	            Toast.makeText(AddThumbnailActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
		
        
		//buttons
        findViewById(R.id.btnSearch).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String searchQuery = ((EditText) findViewById(R.id.edtFlickrSearch)).getText().toString();
				
				new FlickrAsyncTask(AddThumbnailActivity.this, gridView, searchQuery).execute();
			}
		});

        findViewById(R.id.btnOkThumbnailDialog).setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				
				long imageId = gridView.getSelectedItemId();//TODO need to change this. doesnt work i think
				
				resultIntent.putExtra("title", getIntent().getExtras().getString("title"));
				resultIntent.putExtra("imageId", imageId);

				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
        
        findViewById(R.id.btnCancelThumbnailDialog).setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});    
    }
}




//public class AddThumbnailActivity extends Activity {
//	String mImagesPath;
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//    	mImagesPath = this.getFilesDir().getParent() + "/images/";
//    	createImagesDir(mImagesPath);
//        copyImagesToStorage();
//        loadGridView();
//    }
//    
//    /**
//     * Method handles the logic for setting the adapter for the gridview
//     */
//    private void loadGridView(){
//    	
//    	GridView lLazyGrid = (GridView) this.findViewById(R.id.gridview);
//		try {
//			LazyImageAdapter lLazyAdapter = new LazyImageAdapter(this.getApplicationContext(),
//					null,
//					mImagesPath);
//	    	lLazyGrid.setAdapter(lLazyAdapter);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	
//    }
//    
//    /**
//     * Copy images from assets to storage
//     */
//    private void copyImagesToStorage(){
//    	AssetManager lAssetManager = getAssets();
//        String[] lFiles = null;
//        String lTag = "copyImageFail";
//        
//        try {
//        	// get all of the files in the assets directory
//            lFiles = lAssetManager.list("");
//        } catch (IOException e) {
//            Log.e(lTag, e.getMessage());
//        }
//        
//        
//        for(int i=0; i<lFiles.length; i++) {
//        	// We have a file to copy
//        	
//
//            try {
//            	
//            	// copy the file
//            	copyFile(lFiles[i], mImagesPath + lFiles[i]);
//            } catch(Exception e) {
//                Log.e(lTag, e.getMessage());
//            }       
//        }
//    }
//    
//    /**
//     * Method copies the contents of one stream to another
//     * @param aIn stream to copy from
//     * @param aOut stream to copy to
//     * @throws IOException
//     */
//    private void copyFile(String aIn, String aOut) throws IOException {
//    	byte[] lBuffer = new byte[1024];
//        int lRead;
//        final int lOffset = 0;
//        
//    	// create an in and out stream
//        InputStream lIn = getAssets().open(aIn);
//        OutputStream lOut = new FileOutputStream(aOut);
//        
//        // Copy contents while there is data
//        while((lRead = lIn.read(lBuffer)) != -1){
//          lOut.write(lBuffer, lOffset, lRead);
//        }
//        
//        // clean up after our streams
//    	lIn.close();
//    	lIn = null;
//    	lOut.flush();
//    	lOut.close();
//    	lOut = null;
//    }
//    
//    /**
//     * Create the directory specified at aPath if it does not exist
//     * @param aPath directory to check for and create
//     */
//    private void createImagesDir(String aPath){
//    	File lDir = new File(aPath);
//    	if(!lDir.exists()){
//    		lDir.mkdir();
//    	}
//    }
//}
